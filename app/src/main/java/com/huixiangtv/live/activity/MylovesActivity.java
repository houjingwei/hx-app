package com.huixiangtv.live.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.MyFansAdapter;
import com.huixiangtv.live.adapter.MyLovesAdapter;
import com.huixiangtv.live.model.Love;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.StringUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class MylovesActivity extends BaseBackActivity {

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private ListView listView;
    int page = 1;
    MyLovesAdapter adapter;

    List<Love> lovesList;

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
        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshLayout);
        listView = (ListView) findViewById(R.id.data);
        View view = LayoutInflater.from(MylovesActivity.this).inflate(R.layout.activity_myloves_head, null, false);
        TextView tvMyLoves = (TextView) view.findViewById(R.id.tvMyLoves);
        if(null!=App.getLoginUser()) {
            tvMyLoves.setText(StringUtil.isNotEmpty(App.getLoginUser().getLoves())?App.getLoginUser().getLoves():0+"个");
        }else{
            tvMyLoves.setText("0个");
        }
        listView.addHeaderView(view);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new MyLovesAdapter(this);
        listView.setAdapter(adapter);
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
        lovesList = getData();
        adapter.addList(lovesList);
        mPullToRefreshScrollView.onRefreshComplete();
    }

    public List<Love> getData() {
        List<Love> ls = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open("loves.json"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            ls = JSON.parseArray(stringBuilder.toString(),Love.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ls;
    }
}
