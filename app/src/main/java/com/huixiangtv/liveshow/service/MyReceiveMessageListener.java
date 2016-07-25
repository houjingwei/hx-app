package com.huixiangtv.liveshow.service;

import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.model.ChatMessage;
import com.huixiangtv.liveshow.model.LiveMsg;
import com.huixiangtv.liveshow.model.MsgExt;
import com.huixiangtv.liveshow.model.UnreadCount;

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


    public static UnreadCount count = new UnreadCount();

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
            sysMsg();
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

    private void sysMsg() {
        (new AsyncTask<String, String, UnreadCount>() {
            @Override
            protected UnreadCount doInBackground(String... params) {
                count = calcuCount();
                return count;
            }

            @Override
            protected void onPostExecute(UnreadCount unreadCount) {
                super.onPostExecute(unreadCount);
                handleSystemMessage();
                handleNewFriendMessage();
                handleApplyJoinGroupMessage();
            }
        }).execute();
    }




    public static  UnreadCount calcuCount() {
        Conversation.ConversationType[] types = new Conversation.ConversationType[]{Conversation.ConversationType.SYSTEM};
        App.imClient.getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                int totalCount = 0;
                final int[] groupCount = {0};
                final int[] friendCount = {0};
                if(null!=conversations){
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

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        },types);

        return count;
    }



    public void setMsgRead(final int flag) {
        //flag 1朋友消息 2组群消息
        (new AsyncTask<String, String, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                String clearType = (flag==1?"applyJoinGroup":"applyJoinGroup");
                clear(clearType);
                return null;
            }
        }).execute();

    }

    private void clear(final String clearType) {
        Conversation.ConversationType[] types = new Conversation.ConversationType[]{Conversation.ConversationType.SYSTEM};
        App.imClient.getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                for (Conversation cs:conversations) {
                    //cs 所有消息
                    App.imClient.getLatestMessages(Conversation.ConversationType.SYSTEM, cs.getTargetId(), 10000,new RongIMClient.ResultCallback<List<Message>>(){
                        @Override
                        public void onSuccess(List<Message> messages) {
                            if(null!=messages){
                                List<Integer> msgIds = new ArrayList<Integer>();
                                for (Message message : messages) {
                                    TextMessage tm = (TextMessage) message.getContent();
                                    final MsgExt msgExt = JSON.parseObject(String.valueOf(tm.getExtra()), MsgExt.class);
                                    if(msgExt.getMsgType().equals(clearType)){
                                        msgIds.add(message.getMessageId());
                                    }
                                }
                                if(msgIds.size()>0){
                                    int[] ids = new int[msgIds.size()];
                                    for (int i=0;i<msgIds.size();i++){
                                        ids[i] = msgIds.get(i);
                                    }
                                    App.imClient.deleteMessages(ids,null);
                                }
                                //重新进行计算
                                calcuCount();
                            }
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });

                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        },types);
    }


    private void handleChatMessage(Message msg) {
        EventBus.getDefault().post(msg,"msg");
    }
    public void handleLiveMessage(LiveMsg msg) {
        EventBus.getDefault().post(msg,"live_tag");
    }
    private void handleSystemMessage() {EventBus.getDefault().post(null,"sys_msg");}
    private void handleApplyJoinGroupMessage() {
        EventBus.getDefault().post(null,"apply_join_gruop");
    }
    private void handleNewFriendMessage() {
        EventBus.getDefault().post(null,"new_friend");
    }



}
