package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.GroupListAdapter;
import com.huixiangtv.liveshow.model.Live;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.ui.HuixiangLoadingLayout;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

                ForwardUtils.target(GroupListActivity.this, Constant.GROUP_CHAT_INFO, null);
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

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", page + "");
        paramsMap.put("pageSize", pageSize + "");
        paramsMap.put("cNo", "TJ");


        RequestUtils.sendPostRequest(Api.LIVE_LIST, paramsMap, new ResponseCallBack<Live>() {
            @Override
            public void onSuccessList(List<Live> data) {
                if (data != null && data.size() > 0) {
                    if (page == 1) {
                        adapter.clear();
                        adapter.addList(data);
                    } else {
                        adapter.addList(data);
                    }

                } else {
                    if (page == 1) {
                        CommonHelper.noData("暂无直播", refreshView.getRefreshableView(), GroupListActivity.this, 2);
                    }
                }
                refreshView.onRefreshComplete();
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                refreshView.onRefreshComplete();
            }
        }, Live.class);
    }

}
