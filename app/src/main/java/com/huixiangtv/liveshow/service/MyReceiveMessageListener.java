package com.huixiangtv.liveshow.service;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.model.ChatMessage;
import com.huixiangtv.liveshow.model.LiveMsg;
import com.huixiangtv.liveshow.model.MsgExt;
import com.huixiangtv.liveshow.model.UnreadCount;
import com.huixiangtv.liveshow.utils.GaussUtils;
import com.huixiangtv.liveshow.utils.StringUtil;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * Created by hjw on 16/7/22.
 */
public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {


    public UnreadCount count = new UnreadCount();

    /* 持有私有静态实例，防止被引用，此处赋值为null，目的是实现延迟加载 */
    private static MyReceiveMessageListener instance = null;

    /* 私有构造方法，防止被实例化 */
    private MyReceiveMessageListener() {
    }

    /* 1:懒汉式，静态工程方法，创建实例 */
    public static MyReceiveMessageListener getInstance() {
        if (instance == null) {
            instance = new MyReceiveMessageListener();
        }
        return instance;
    }






    @Override
    public boolean onReceived(Message message, int i) {
        Log.i("eventBus","收到了消息"+message.getContent());


        if(message.getConversationType().equals(Conversation.ConversationType.PRIVATE)){
            Log.i("eventBus","收到了私聊消息"+message.getContent());
            handleChatMessage(message);
        }else if(message.getConversationType().equals(Conversation.ConversationType.GROUP)){
            Log.i("eventBus","收到了群聊消息"+message.getContent());
        }else if(message.getConversationType().equals(Conversation.ConversationType.SYSTEM)){
            Log.i("eventBus","收到了系统消息"+message.getContent());
            sysMsg(message);
        }else if(message.getConversationType().equals(Conversation.ConversationType.CHATROOM)){
            Log.i("eventBus","收到了聊天室消息"+message.getContent());
            if (message.getContent() instanceof TextMessage) {
                TextMessage tm = (TextMessage) message.getContent();
                final LiveMsg msg = JSON.parseObject(String.valueOf(tm.getExtra()), LiveMsg.class);
                msg.setContent(tm.getContent().toString());
                if(null!= App.getLoginUser() && !msg.getUid().equals(App.getLoginUser().getUid())){
                    handleLiveMessage(msg);
                }else if(null==App.getLoginUser()){
                    handleLiveMessage(msg);
                }
            }
        }
        return false;
    }

    private void sysMsg(final Message message) {
        (new AsyncTask<String, String, UnreadCount>() {
            @Override
            protected UnreadCount doInBackground(String... params) {
                count = calcuCount();
                return count;
            }

            @Override
            protected void onPostExecute(UnreadCount unreadCount) {
                super.onPostExecute(unreadCount);
                handleSystemMessage(message);
            }
        }).execute();
    }



    private UnreadCount calcuCount() {
        Conversation.ConversationType[] types = new Conversation.ConversationType[]{Conversation.ConversationType.SYSTEM};
        App.imClient.getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                int totalCount = 0;
                final int[] groupCount = {0};
                final int[] friendCount = {0};
                for (Conversation cs:conversations) {
                    totalCount+=cs.getUnreadMessageCount();
                    //cs 所有消息
                    App.imClient.getLatestMessages(Conversation.ConversationType.SYSTEM, cs.getTargetId(), 10000,new RongIMClient.ResultCallback<List<Message>>(){
                        @Override
                        public void onSuccess(List<Message> messages) {
                            if(null!=messages){
                                for (Message message : messages) {
                                    TextMessage tm = (TextMessage) message.getContent();
                                    ChatMessage cm = new ChatMessage();
                                    cm.setSendStatus(message.getSentStatus().name());
                                    final MsgExt msgExt = JSON.parseObject(String.valueOf(tm.getExtra()), MsgExt.class);
                                    if(msgExt.getMsgType().equals("applyJoinGroup")){
                                        groupCount[0]++;
                                    }else if(msgExt.getMsgType().equals("applyFriend")){
                                        friendCount[0]++;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });

                    count.setGroupUnReadCount(groupCount[0]);
                    count.setNewFriendUnReadCount(friendCount[0]);
                    count.setTotalCount(totalCount);


                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        },types);

        return count;
    }

    private void handleChatMessage(Message message) {
        EventBus.getDefault().post(message,"msg");
    }
    public void handleLiveMessage(LiveMsg msg) {
        EventBus.getDefault().post(msg,"live_tag");
    }
    private void handleSystemMessage(Message message) {EventBus.getDefault().post(message,"sys_msg");}



}
