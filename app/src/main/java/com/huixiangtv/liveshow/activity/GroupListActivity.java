package com.huixiangtv.liveshow.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.GroupListAdapter;
import com.huixiangtv.liveshow.model.ChatGroup;
import com.huixiangtv.liveshow.model.Live;
import com.huixiangtv.liveshow.model.MsgContent;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.ui.HuixiangLoadingLayout;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        x.view().inject(this);
        initView();
        loadData();
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
        refreshView.getRefreshableView().addHeaderView(groupListHeadView);

        refreshView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>1) {
                    ForwardUtils.target(GroupListActivity.this, Constant.CHAT_MSG, null);
                }
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

}
