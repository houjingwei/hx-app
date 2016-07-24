package com.huixiangtv.liveshow.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.ChatAdapter;
import com.huixiangtv.liveshow.model.Friend;
import com.huixiangtv.liveshow.model.MsgContent;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import me.drakeet.materialdialog.MaterialDialog;


public class FragmentChat extends BaseFragment  implements View.OnClickListener {

    View mRootView;
    private ListView mListView;
    private ChatAdapter adapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_chat, container, false);
        Log.i("fetchData","onCreateViewFragmentChat");
        initView();
        return mRootView;
    }


    /**
     * 获取数据
     */
    private void initData() {
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
        });
    }



    private void initView() {
        mListView = (ListView) mRootView.findViewById(R.id.listView);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_chat_head, null, false);
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
                Conversation cs = (Conversation) adapter.getItem(position-1);
                Map<String,String> map = new HashMap<String,String>();
                map.put("targetId",cs.getTargetId());
                ForwardUtils.target(getActivity(), Constant.CHAT_MSG,map);
            }
        });



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
        prepareFetchData(true);

    }


    @Override
    public void fetchData() {
        Log.i("fetchData","queryFragmentChat");
        initData();
    }


}
