package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.MyFansAdapter;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.EnumUpdateTag;
import com.huixiangtv.live.utils.widget.pullView.PullToRefreshLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class FanedMeActivity extends BaseBackActivity   {




    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    PtrClassicFrameLayout ptrClassicFrameLayout;
    RecyclerView mRecyclerView;

    List<Fans> fansList ;

    int page = 1;




    private PullToRefreshListView mPullToRefreshScrollView;
    private ListView mDataLv;

    MyFansAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faned_me);
        x.view().inject(this);
        initview();


    }

    private void initview() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.myconcern));

        ptrClassicFrameLayout = (PtrClassicFrameLayout) this.findViewById(R.id.test_recycler_view_frame);

        mRecyclerView = (RecyclerView) this.findViewById(R.id.test_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        adapter = new MyFansAdapter(this);
        mRecyclerView.setAdapter(adapter);
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page=1;
                loadData(true);
                //ptrClassicFrameLayout.setLoadMoreEnable(true);
            }
        });
        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                page++;
                loadData(false);
            }
        });


        ptrClassicFrameLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                ptrClassicFrameLayout.autoRefresh(true);
            }
        }, 150);




    }

    private void loadData(boolean bool) {
        fansList = getData();
        adapter.addList(fansList);
        if(bool) {
            ptrClassicFrameLayout.refreshComplete();
            //ptrClassicFrameLayout.setLoadMoreEnable(true);
        }else{
            ptrClassicFrameLayout.loadMoreComplete(true);
        }


    }

    public List<Fans> getData() {
        List<Fans> ls = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open("myFans.json"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            ls = JSON.parseArray(stringBuilder.toString(),Fans.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ls;
    }
}
