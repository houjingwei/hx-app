package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.ChatMsgAdapter;
import com.huixiangtv.liveshow.model.ChatMessage;
import com.huixiangtv.liveshow.model.MsgExt;
import com.huixiangtv.liveshow.model.User;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class ChatMsgActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.refreshView)
    PullToRefreshListView refreshView;

    @ViewInject(R.id.tvSendMsg)
    TextView tvSendMsg;
    @ViewInject(R.id.etChatMsg)
    EditText etChatMsg;




    User toUser = null;

    ChatMsgAdapter adapter ;

    String targetId = "";
    String userName = "";

    private final String  TAG = "ChatMsgActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_msg);
        x.view().inject(this);
        targetId = getIntent().getStringExtra("targetId");
        userName =  getIntent().getStringExtra("userName");
        initView();
        loadData();

        //注册eventBus
        EventBus.getDefault().register(this);
        Log.i("eventBus","注册eventBus");
    }


    @Subscriber(tag = "msg", mode = ThreadMode.MAIN)
    public void chatMsg(Message message) {
        TextMessage tm = (TextMessage) message.getContent();
        ChatMessage cm = new ChatMessage();
        cm.setSendStatus(message.getSentStatus().name());
        final MsgExt msgExt = JSON.parseObject(String.valueOf(tm.getExtra()), MsgExt.class);
        cm.setContent(tm.getContent().toString());
        cm.setExt(msgExt);
        adapter.add(cm);
    }

    private void loadData() {
        //查询目标对象
        CommonHelper.userInfo(targetId, new ApiCallback<User>() {
            @Override
            public void onSuccess(User data) {
                toUser = data;
            }
        });




        App.imClient.getLatestMessages(Conversation.ConversationType.PRIVATE, targetId, 100,new RongIMClient.ResultCallback<List<Message>>(){

            @Override
            public void onSuccess(List<Message> messages) {
                List<ChatMessage> ls = new ArrayList<ChatMessage>();
                if(null!=messages){
                    for (Message message : messages) {
                        TextMessage tm = (TextMessage) message.getContent();
                        ChatMessage cm = new ChatMessage();
                        cm.setSendStatus(message.getSentStatus().name());
                        final MsgExt msgExt = JSON.parseObject(String.valueOf(tm.getExtra()), MsgExt.class);
                        cm.setContent(tm.getContent().toString());
                        cm.setExt(msgExt);
                        ls.add(cm);
                    }
                }

                if(ls.size()>0){
                    adapter.addList(ls);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });



    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(userName);
        commonTitle.saveShow(View.VISIBLE);
        commonTitle.getSave().setText("清空");
        commonTitle.getSave().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.imClient.clearMessages(Conversation.ConversationType.PRIVATE, targetId, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Log.i(TAG,aBoolean+"");
                        adapter.clear();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }
        });

        adapter = new ChatMsgAdapter(ChatMsgActivity.this);
        refreshView.setMode(PullToRefreshBase.Mode.DISABLED);
        refreshView.setAdapter(adapter);

        tvSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendMsg();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendMsg() throws Exception{

            if (TextUtils.isEmpty(etChatMsg.getText().toString())) {
                CommonHelper.showTip(ChatMsgActivity.this, "不可以发送空消息哦~");
                etChatMsg.requestFocus();
                return;
            }
            final TextMessage tm = TextMessage.obtain(etChatMsg.getText().toString());
            final Map<String,String> map = new HashMap<String,String>();
            map.put("photo",App.getLoginUser().getPhoto());
            map.put("nickName",App.getLoginUser().getNickName());
            map.put("uid",App.getLoginUser().getUid());
            final ObjectMapper mapper = new ObjectMapper();
            if(null!=toUser){
                map.put("toPhoto",toUser.getPhoto());
                map.put("toNickName",toUser.getNickName());
                map.put("toUid",toUser.getUid());

                String jsonStr = mapper.writeValueAsString(map);
                tm.setExtra(jsonStr);
            }else{
                CommonHelper.userInfo(targetId, new ApiCallback<User>() {
                    @Override
                    public void onSuccess(User data)  {
                        map.put("toPhoto",toUser.getPhoto());
                        map.put("toNickName",toUser.getNickName());
                        map.put("toUid",toUser.getUid());
                        String jsonStr = null;
                        try {
                            jsonStr = mapper.writeValueAsString(map);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        tm.setExtra(jsonStr);
                    }
                });
            }

            App.imClient.sendMessage(Conversation.ConversationType.PRIVATE, targetId,
                    tm, null, null, new RongIMClient.SendMessageCallback() {
                        @Override
                        public void onSuccess(Integer integer) {
                            ChatMessage msg = new ChatMessage();
                            MsgExt ext = new MsgExt();
                            ext.setMsgType("1");
                            ext.setNickName(App.getLoginUser().getNickName());
                            ext.setPhoto(App.getLoginUser().getPhoto());
                            msg.setExt(ext);
                            msg.setSendStatus(Message.SentStatus.SENT.name());
                            msg.setContent(etChatMsg.getText().toString());
                            adapter.add(msg);
                        }

                        @Override
                        public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

                        }
                    }, null);



    }
}
