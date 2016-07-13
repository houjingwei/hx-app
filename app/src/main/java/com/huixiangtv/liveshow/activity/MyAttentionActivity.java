package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.MyAttentionAdapter;
import com.huixiangtv.liveshow.model.Fans;
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

public class MyAttentionActivity extends BaseBackActivity   {




    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    private PullToRefreshListView refreshView;



    int page = 1;


    MyAttentionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention);
        x.view().inject(this);
        initview();


    }

    private void initview() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.myconcern));

        adapter = new MyAttentionAdapter(this);
        refreshView = (PullToRefreshListView) findViewById(R.id.refreshView);
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshView.setHeaderLayout(new HuixiangLoadingLayout(this));
        refreshView.setFooterLayout(new HuixiangLoadingLayout(this));
        refreshView.setAdapter(adapter);
        loadFanedMe();
        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        loadFanedMe();
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        page++;
                        loadFanedMe();
                    }
                }, 1000);

            }
        });




    }

    private void loadFanedMe(){

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", page + "");
        paramsMap.put("pageSize", "10");


        RequestUtils.sendPostRequest(Api.GETCOLLECTARTIST, paramsMap, new ResponseCallBack<Fans>() {
            @Override
            public void onSuccessList(List<Fans> data) {
                if (data != null && data.size() > 0) {
                    if(page==1){
                        adapter.clear();
                    }
                    adapter.addList(data);
                }else{
                    if (page == 1) {
                        CommonHelper.noData("还没有关注任何人哦",refreshView.getRefreshableView(),MyAttentionActivity.this, 2);
                    }
                }
                refreshView.onRefreshComplete();



            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(MyAttentionActivity.this, e.getMessage());
                refreshView.onRefreshComplete();
            }
        }, Fans.class);
    }

}
