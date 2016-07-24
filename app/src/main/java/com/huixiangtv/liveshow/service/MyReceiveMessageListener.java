package com.huixiangtv.liveshow.service;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.model.LiveMsg;

import org.simple.eventbus.EventBus;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * Created by hjw on 16/7/22.
 */
public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {


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
            if(message.getContent() instanceof  TextMessage){
                TextMessage tm = (TextMessage) message.getContent();
                final LiveMsg msg = JSON.parseObject(String.valueOf(tm.getExtra()), LiveMsg.class);
                Log.i("eventBus","收到了系统消息"+msg.getMsgType());
            }
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

    private void handleChatMessage(Message message) {
        EventBus.getDefault().post(message,"msg");
    }

    public void handleLiveMessage(LiveMsg msg) {
        EventBus.getDefault().post(msg,"live_tag");
    }



}
