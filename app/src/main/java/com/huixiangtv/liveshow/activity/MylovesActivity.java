package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.MyLovesAdapter;
import com.huixiangtv.liveshow.model.Love;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.ui.HuixiangLoadingLayout;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.StringUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MylovesActivity extends BaseBackActivity {

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;
    private PullToRefreshListView refreshView;

    int page = 1;
    MyLovesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myloves);
        x.view().inject(this);
        initview();
    }

    private void initview() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.myLoves));

        adapter = new MyLovesAdapter(this);
        refreshView = (PullToRefreshListView) findViewById(R.id.refreshView);
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshView.setHeaderLayout(new HuixiangLoadingLayout(this));
        refreshView.setFooterLayout(new HuixiangLoadingLayout(this));
        refreshView.setAdapter(adapter);
        View view = LayoutInflater.from(MylovesActivity.this).inflate(R.layout.activity_myloves_head, null, false);
        refreshView.getRefreshableView().addHeaderView(view);

        TextView tvMyLoves = (TextView) view.findViewById(R.id.tvMyLoves);
        if(null!=App.getLoginUser()) {
            tvMyLoves.setText(StringUtil.isNotEmpty(App.getLoginUser().getLoves())?App.getLoginUser().getLoves():0+"个");
        }else{
            tvMyLoves.setText("0个");
        }

        loadData();
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

    }

    private void loadData() {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", page + "");
        paramsMap.put("pageSize", "10");
        RequestUtils.sendPostRequest(Api.LOVE_DETAIL, paramsMap, new ResponseCallBack<Love>() {
            @Override
            public void onSuccessList(List<Love> data) {

                if (data != null && data.size() > 0) {
                    if(page==1){
                        adapter.clear();
                    }
                    adapter.addList(data);
                }
                refreshView.onRefreshComplete();

            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(MylovesActivity.this, e.getMessage());
                refreshView.onRefreshComplete();
            }
        }, Love.class);
    }


}