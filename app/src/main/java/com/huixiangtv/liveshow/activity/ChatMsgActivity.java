package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import com.huixiangtv.liveshow.utils.KeyBoardUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
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
    String type = "";
    String title = "";
    private String groupPhoto = "";

    private final String  TAG = "ChatMsgActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_msg);
        x.view().inject(this);
        targetId = getIntent().getStringExtra("targetId");
        title =  getIntent().getStringExtra("userName");
        type = getIntent().getStringExtra("type");
        groupPhoto = getIntent().getStringExtra("groupPhoto");
        initView();
        loadData();

        //注册eventBus
        EventBus.getDefault().register(this);
        Log.i("eventBus","注册eventBus");
    }


    @Subscriber(tag = "msg", mode = ThreadMode.MAIN)
    public void chatMsg(Message message) {
        if(message.getTargetId().equals(targetId)) {
            TextMessage tm = (TextMessage) message.getContent();
            ChatMessage cm = new ChatMessage();
            cm.setSendStatus(message.getSentStatus().name());
            final MsgExt msgExt = JSON.parseObject(String.valueOf(tm.getExtra()), MsgExt.class);
            cm.setContent(tm.getContent().toString());
            cm.setExt(msgExt);
            adapter.add(cm);
        }
    }

    private void loadData() {


        Conversation.ConversationType t = null;
        if(type.equals("1")){
            t = Conversation.ConversationType.PRIVATE;
            //查询目标对象
            CommonHelper.userInfo(targetId, new ApiCallback<User>() {
                @Override
                public void onSuccess(User data) {
                    toUser = data;
                }
            });
        }else if(type.equals("2")){
            t = Conversation.ConversationType.GROUP;
        }
        App.imClient.getHistoryMessages(t, targetId, -1,100,new RongIMClient.ResultCallback<List<Message>>(){

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
                    //倒序排列
                    Collections.reverse(ls);
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
        commonTitle.setTitleText(title);
        commonTitle.saveShow(View.VISIBLE);
        commonTitle.getSave().setText("清空");
        commonTitle.getSave().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conversation.ConversationType t = null;
                if(type.equals("1")){
                    t = Conversation.ConversationType.PRIVATE;
                }else if(type.equals("2")){
                    t = Conversation.ConversationType.GROUP;
                }
                App.imClient.clearMessages(t, targetId, new RongIMClient.ResultCallback<Boolean>() {
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
        adapter.setMsgType(type);
        refreshView.setMode(PullToRefreshBase.Mode.DISABLED);
        refreshView.setAdapter(adapter);
        refreshView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeyBoard();
            }
        });
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
            }
        });

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

    /**
     * 关闭键盘
     */
    private void hideKeyBoard() {
        KeyBoardUtils.closeKeybord(etChatMsg,ChatMsgActivity.this);
    }

    private void sendMsg() throws Exception{
        final String[] jsonStr = {null};
            if (TextUtils.isEmpty(etChatMsg.getText().toString())) {
                CommonHelper.showTip(ChatMsgActivity.this, "不可以发送空消息哦~");
                etChatMsg.requestFocus();
                return;
            }
            final TextMessage tm = TextMessage.obtain(etChatMsg.getText().toString());
            tm.setContent(etChatMsg.getText().toString());

            final Map<String,String> map = new HashMap<String,String>();
            map.put("photo",App.getLoginUser().getPhoto());
            map.put("nickName",App.getLoginUser().getNickName());
            map.put("uid",App.getLoginUser().getUid());
            map.put("title",title);
            final ObjectMapper mapper = new ObjectMapper();
            if(type.equals("1") && null!=toUser){
                map.put("toPhoto",toUser.getPhoto());
                map.put("toNickName",toUser.getNickName());
                map.put("toUid",toUser.getUid());
            }else if(type.equals("1") && null==toUser){
                CommonHelper.userInfo(targetId, new ApiCallback<User>() {
                    @Override
                    public void onSuccess(User data)  {
                    map.put("toPhoto",toUser.getPhoto());
                    map.put("toNickName",toUser.getNickName());
                    map.put("toUid",toUser.getUid());
                    }
                });
            }
            jsonStr[0] = mapper.writeValueAsString(map);
            tm.setExtra(jsonStr[0]);
            Conversation.ConversationType t = null;
            if(type.equals("1")){
                t = Conversation.ConversationType.PRIVATE;
            }else if(type.equals("2")){
                t = Conversation.ConversationType.GROUP;
            }
            App.imClient.sendMessage(t, targetId,
                    tm, tm.getContent(), tm.getExtra(), new RongIMClient.SendMessageCallback() {
                        @Override
                        public void onSuccess(Integer integer) {

                            ChatMessage msg = new ChatMessage();
                            MsgExt ext = new MsgExt();
                            ext.setPhoto(App.getLoginUser().getPhoto());
                            ext.setNickName(App.getLoginUser().getNickName());
                            ext.setUid(App.getLoginUser().getUid());
                            msg.setExt(ext);
                            msg.setContent(etChatMsg.getText().toString());
                            adapter.add(msg);
                            etChatMsg.setText("");
                        }

                        @Override
                        public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

                        }
                    }, null);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(type.equals("1")){
            CommonHelper.clearMessagesUnReadStatus(Conversation.ConversationType.PRIVATE,targetId);
        }else if(type.equals("2")){
            CommonHelper.clearMessagesUnReadStatus(Conversation.ConversationType.GROUP,targetId);
        }

    }
}
