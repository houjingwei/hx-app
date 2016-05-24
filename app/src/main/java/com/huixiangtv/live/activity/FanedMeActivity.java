package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.MyFansAdapter;
import com.huixiangtv.live.ui.CommonTitle;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class FanedMeActivity extends BaseBackActivity {




    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    List<Fans> fansList ;

    int page = 1;




    private PullToRefreshScrollView mPullToRefreshScrollView;
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

        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshLayout);
        mDataLv = (ListView) findViewById(R.id.data);
        View view = LayoutInflater.from(FanedMeActivity.this).inflate(R.layout.search_view, null, false);
        mDataLv.addHeaderView(view);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new MyFansAdapter(this);
        mDataLv.setAdapter(adapter);

        mPullToRefreshScrollView.setIsUpListen(new PullToRefreshScrollView.isUpListen() {
            @Override
            public void isUp(boolean isUp) {
                if (isUp) {

                }
            }

            @Override
            public void isTouch(boolean isTouch) {

            }
        });
        mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                loadData();
            }
        });

        loadData();
    }

    private void loadData() {
        fansList = getData();

        mPullToRefreshScrollView.onRefreshComplete();
        adapter.addList(fansList);
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
