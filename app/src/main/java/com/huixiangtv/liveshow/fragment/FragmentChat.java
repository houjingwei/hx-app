package com.huixiangtv.liveshow.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.ChatAdapter;
import com.huixiangtv.liveshow.model.MsgContent;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;


public class FragmentChat extends Fragment implements View.OnClickListener {

    View mRootView;


    private ListView mListView;
    private ChatAdapter adapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_chat, container, false);
        initView();
        initData();
        return mRootView;
    }


    /**
     * 获取数据
     */
    private void initData() {
        App.imClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if(null!=conversations && conversations.size()>0){
                    adapter.addList(conversations);
                }else{
                    List<Conversation> ccc = new ArrayList<Conversation>();
                    Conversation cs = new Conversation();
                    cs.setConversationTitle("hahahah");
                    cs.setReceivedTime(1468835349);
                    cs.setUnreadMessageCount(5);
                    MsgContent mc = new MsgContent();
                    UserInfo ui = new UserInfo("1","zijing", Uri.parse("http://img0.imgtn.bdimg.com/it/u=1850159850,51447102&fm=21&gp=0.jpg"));
                    mc.setUserInfo(ui);
                    cs.setLatestMessage(mc);
                    cs.setConversationType(Conversation.ConversationType.GROUP);


                    Conversation cs1 = new Conversation();
                    cs1.setConversationTitle("这是什么呀");
                    cs1.setReceivedTime(1468835499);
                    cs1.setUnreadMessageCount(0);
                    MsgContent mc1 = new MsgContent();
                    UserInfo ui1 = new UserInfo("2","代言", Uri.parse("http://img1.imgtn.bdimg.com/it/u=2266405879,637065588&fm=23&gp=0.jpg"));
                    mc1.setUserInfo(ui1);
                    cs1.setLatestMessage(mc1);
                    cs1.setConversationType(Conversation.ConversationType.PRIVATE);


                    ccc.add(cs);
                    ccc.add(cs1);
                    adapter.addList(ccc);
                }

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }



    private void initView() {
        mListView = (ListView) mRootView.findViewById(R.id.listView);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_chat_head, null, false);
        mListView.addHeaderView(view);

        adapter = new ChatAdapter(getActivity());
        mListView.setAdapter(adapter);

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

    }



}
