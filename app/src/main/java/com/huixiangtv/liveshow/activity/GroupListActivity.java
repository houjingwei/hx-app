package com.huixiangtv.liveshow.activity;

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
    private int page = 1;
    private String pageSize = "5";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        x.view().inject(this);
        initView();
        initLayout();
        initData();
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
                ForwardUtils.target(GroupListActivity.this, Constant.GROUP_CHAT_INFO, null);
            }
        });


    }


    protected void initLayout() {

        refreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        refreshView.setHeaderLayout(new HuixiangLoadingLayout(this));
        refreshView.setFooterLayout(new HuixiangLoadingLayout(this));

        View groupListHeadView = LayoutInflater.from(this).inflate(R.layout.group_list_head, null, false);
        refreshView.getRefreshableView().addHeaderView(groupListHeadView);

        refreshView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
    }



    protected void initData() {
        adapter = new GroupListAdapter(this);
        refreshView.setAdapter(adapter);
        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        loadData();
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        loadData();
                    }
                }, 1000);

            }
        });


        loadData();


    }

    private void loadData() {

        App.imClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (null != conversations && conversations.size() > 0) {
                    adapter.addList(conversations);
                } else {
                    List<Conversation> ccc = new ArrayList<Conversation>();
                    Conversation cs = new Conversation();
                    cs.setConversationTitle("hahahah");
                    cs.setReceivedTime(1468835349);
                    cs.setUnreadMessageCount(5);
                    MsgContent mc = new MsgContent();
                    UserInfo ui = new UserInfo("1", "zijing", Uri.parse("http://img0.imgtn.bdimg.com/it/u=1850159850,51447102&fm=21&gp=0.jpg"));
                    mc.setUserInfo(ui);
                    cs.setLatestMessage(mc);
                    cs.setConversationType(Conversation.ConversationType.GROUP);


                    Conversation cs1 = new Conversation();
                    cs1.setConversationTitle("这是什么呀");
                    cs1.setReceivedTime(1468835499);
                    cs1.setUnreadMessageCount(0);
                    MsgContent mc1 = new MsgContent();
                    UserInfo ui1 = new UserInfo("2", "代言", Uri.parse("http://img1.imgtn.bdimg.com/it/u=2266405879,637065588&fm=23&gp=0.jpg"));
                    mc1.setUserInfo(ui1);
                    cs1.setLatestMessage(mc1);
                    cs1.setConversationType(Conversation.ConversationType.PRIVATE);


                    ccc.add(cs);
                    ccc.add(cs1);
                    adapter.clear();
                    adapter.addList(ccc);
                    refreshView.onRefreshComplete();
                }

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                refreshView.onRefreshComplete();
            }
        });
    }

}
