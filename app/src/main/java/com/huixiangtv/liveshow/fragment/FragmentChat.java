package com.huixiangtv.liveshow.fragment;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.ChatAdapter;
import com.huixiangtv.liveshow.model.MsgExt;
import com.huixiangtv.liveshow.model.UnreadCount;
import com.huixiangtv.liveshow.model.User;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import me.drakeet.materialdialog.MaterialDialog;


public class FragmentChat extends BaseFragment  implements View.OnClickListener {

    View mRootView;
    private ListView mListView;
    private ChatAdapter adapter;

    TextView tvUnRead;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_chat, container, false);

        initView();
        return mRootView;
    }


    @Subscriber(tag = "msg", mode = ThreadMode.ASYNC)
    public void msg(Message msg) {
        Log.i("eventBus","msg");
        Log.i("qunimade","FragmentChat+msg");
        setCount();

    }

    @Subscriber(tag = "sys_msg", mode = ThreadMode.ASYNC)
    public void sysMsg(Message msg) {
        Log.i("eventBus","msg");
        Log.i("qunimade","FragmentChat+msg");
        setCount();

    }


    private void setCount(){
        App._myReceiveMessageListener.calcuCount(new ApiCallback<UnreadCount>() {
            @Override
            public void onSuccess(UnreadCount data) {
                if(data.getTotalCount()>0){
                    tvUnRead.setText(data.getTotalCount()+"");
                    tvUnRead.setVisibility(View.VISIBLE);
                }else{
                    tvUnRead.setVisibility(View.GONE);
                }
                initData();
            }
        });


    }


    /**
     * 获取数据
     */
    private void initData() {
        Conversation.ConversationType[] types = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,Conversation.ConversationType.GROUP};
        App.imClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                adapter.clear();
                if(null!=conversations && conversations.size()>0){
                    adapter.addList(conversations);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        },types);
    }



    private void initView() {
        mListView = (ListView) mRootView.findViewById(R.id.listView);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_chat_head, null, false);
        tvUnRead = (TextView) view.findViewById(R.id.tvUnRead);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForwardUtils.target(getActivity(), Constant.FRIEND,null);
            }
        });
        mListView.addHeaderView(view);

        adapter = new ChatAdapter(getActivity());
        mListView.setAdapter(adapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Conversation cs = (Conversation) adapter.getItem(position-1);
                delete(cs);
                return true;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Conversation cs = (Conversation) adapter.getItem(position-1);
                final Map<String,String> map = new HashMap<String,String>();
                map.put("targetId",cs.getTargetId());
                if(cs.getConversationType().equals(Conversation.ConversationType.PRIVATE)){
                    map.put("type","1");
                    CommonHelper.userInfo(cs.getTargetId(), new ApiCallback<User>() {
                        @Override
                        public void onSuccess(User data) {
                            map.put("userName",data.getNickName());
                            ForwardUtils.target(getActivity(), Constant.CHAT_MSG,map);
                            celarCount(cs.getConversationType(),cs.getTargetId());
                        }

                        @Override
                        public void onFailure(ServiceException e) {
                            super.onFailure(e);
                            CommonHelper.showTip(getActivity(),"对话用户出错");
                        }
                    });
                }else if(cs.getConversationType().equals(Conversation.ConversationType.GROUP)){
                    map.put("type","2");
                    if(cs.getLatestMessage() instanceof TextMessage){
                        TextMessage tm = (TextMessage) cs.getLatestMessage();
                        MsgExt msgExt = JSON.parseObject(String.valueOf(tm.getExtra()), MsgExt.class);
                        map.put("userName", msgExt.getTitle());
                    }
                    ForwardUtils.target(getActivity(), Constant.CHAT_MSG,map);
                    celarCount(cs.getConversationType(),cs.getTargetId());

                }


            }
        });





    }

    private void celarCount(Conversation.ConversationType conversationType, String targetId) {
        CommonHelper.clearMessagesUnReadStatus(conversationType,targetId);
    }

    private void delete(final Conversation cs) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
        mMaterialDialog.setPositiveButton("删除", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
                App.imClient.removeConversation(cs.getConversationType(), cs.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        CommonHelper.showTip(getActivity(),"删除成功");
                        initData();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }
        }).setNegativeButton("放弃", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.setTitle("回想提示");
        mMaterialDialog.setMessage("确定要删除会话吗");
        mMaterialDialog.show();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        Log.i("eventBus","register_chat");
        setCount();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("eventBus","unregister_chat");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void fetchData() {
        Log.i("fetchData","queryFragmentChat");
        initData();
    }


}
