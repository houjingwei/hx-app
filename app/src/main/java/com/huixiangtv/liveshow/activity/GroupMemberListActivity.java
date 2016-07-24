package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.MemberAdapter;
import com.huixiangtv.liveshow.model.Member;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.ui.HuixiangLoadingLayout;
import com.huixiangtv.liveshow.utils.CommonHelper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stone on 16/7/18.
 */
public class GroupMemberListActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.refreshView)
    PullToRefreshListView refreshView;
    private MemberAdapter adapter;
    private int page = 1;
    private String pageSize = "500";
    String groupId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmember);
        x.view().inject(this);
        groupId = getIntent().getStringExtra("groupId");
        initView();
        initData();
    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.group_member));
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshView.setHeaderLayout(new HuixiangLoadingLayout(this));
        refreshView.setFooterLayout(new HuixiangLoadingLayout(this));

        refreshView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
    }





    protected void initData() {
            adapter = new MemberAdapter(this);
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

    /**
     * 加载列表数据
     */
    private void loadData() {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", page + "");
        paramsMap.put("pageSize", pageSize + "");
        paramsMap.put("gid", groupId);


        RequestUtils.sendPostRequest(Api.GET_GROUP_MEMBER, paramsMap, new ResponseCallBack<Member>() {
            @Override
            public void onSuccessList(List<Member> data) {
                if (data != null && data.size() > 0) {
                    if (page == 1) {
                        adapter.clear();
                        adapter.addList(data);
                    } else {
                        adapter.addList(data);
                    }

                } else {
                    if (page == 1) {
                        CommonHelper.noData("暂无群成员记录", refreshView.getRefreshableView(), GroupMemberListActivity.this,2);
                    }
                }
                refreshView.onRefreshComplete();
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                refreshView.onRefreshComplete();
            }
        }, Member.class);
    }


}
