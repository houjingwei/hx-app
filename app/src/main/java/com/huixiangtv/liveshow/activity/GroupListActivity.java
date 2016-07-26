package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.GroupListAdapter;
import com.huixiangtv.liveshow.model.ChatGroup;
import com.huixiangtv.liveshow.model.UnreadCount;
import com.huixiangtv.liveshow.model.User;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by Stone on 16/7/18.
 */
public class GroupListActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.refreshView)
    PullToRefreshListView refreshView;
    private GroupListAdapter adapter;
    private String page = "1";
    private String pageSize = "10000";

    private TextView tvUnRead;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        x.view().inject(this);
        //注册eventBus
        EventBus.getDefault().register(this);
        Log.i("eventBus","注册eventBus");
        initView();
        loadData();
    }

    @Subscriber(tag = "apply_join_gruop", mode = ThreadMode.MAIN)
    public void friend(Message msg) {
        Log.i("eventBus","永不执行"+ App._myReceiveMessageListener.count.toString());
        if(null!=tvUnRead ){
            setCount();
        }
    }

    private void setCount() {
        App._myReceiveMessageListener.calcuCount(new ApiCallback<UnreadCount>() {
            @Override
            public void onSuccess(UnreadCount data) {
                if(data.getNewFriendUnReadCount()>0){
                    tvUnRead.setText(data.getGroupUnReadCount()+"");
                    tvUnRead.setVisibility(View.VISIBLE);
                }else{
                    tvUnRead.setVisibility(View.GONE);
                }
            }
        });

    }


    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.group_name));
        commonTitle.imgShow(View.VISIBLE);
        ImageView commonTitleImg = commonTitle.getImg();
        commonTitleImg.setImageDrawable(getResources().getDrawable(R.mipmap.v3_create_group));
        commonTitleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForwardUtils.target(GroupListActivity.this, Constant.ADD_GROUP, null);
            }
        });


        refreshView.setMode(PullToRefreshBase.Mode.DISABLED);
        View groupListHeadView = LayoutInflater.from(this).inflate(R.layout.group_list_head, null, false);

        tvUnRead = (TextView) groupListHeadView.findViewById(R.id.tvUnRead);
        refreshView.getRefreshableView().addHeaderView(groupListHeadView);
        groupListHeadView.findViewById(R.id.llGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForwardUtils.target(GroupListActivity.this,Constant.NEW_GROUP,null);
                App._myReceiveMessageListener.setMsgRead(2);
            }
        });

        adapter = new GroupListAdapter(this);
        refreshView.setAdapter(adapter);


    }

    private void loadData() {
        Map<String,String> params = new HashMap<>();
        params.put("page",page);
        params.put("pageSize",pageSize);
        RequestUtils.sendPostRequest(Api.MY_JOIN_GROUPS, params, new ResponseCallBack<ChatGroup>() {
            @Override
            public void onSuccessList(List<ChatGroup> data) {
                super.onSuccessList(data);
                adapter.addList(data);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, ChatGroup.class);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(App.refreshGrouplist){
            App.refreshGrouplist = false;
            page = "1";
            loadData();
        }
    }
}
