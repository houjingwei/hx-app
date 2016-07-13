package com.huixiangtv.liveshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.TopicAdapter;
import com.huixiangtv.liveshow.model.Topic;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.List;

public class TopicActivity extends BaseBackActivity{


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;



    private  PullToRefreshScrollView mPullToRefreshScrollView;
    private ListView mDataLv;

    TopicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        x.view().inject(this);
        initView();
        initTopic();
        setListener();
    }

    private void initTopic() {
        adapter = new TopicAdapter(this);
        mDataLv.setAdapter(adapter);
        mDataLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Topic topic = (Topic) adapter.getItem(i);
                Intent intent=new Intent();
                intent.putExtra("tid",topic.getTid());
                intent.putExtra("topic",topic.getTopic());
                setResult(RESULT_OK, intent);
                onBackPressed();

            }
        });
        loadTopic();
    }

    /**
     * 加载主题
     */
    private void loadTopic() {
        RequestUtils.sendPostRequest(Api.TOPIC, null, new ResponseCallBack<Topic>() {
            @Override
            public void onSuccessList(List<Topic> data) {
                mPullToRefreshScrollView.onRefreshComplete();
                adapter.addList(data);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, Topic.class);

    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.selTheme));


        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshLayout);
        mDataLv = (ListView) findViewById(R.id.data);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);

    }


    public void setListener() {

        mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                loadTopic();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                loadTopic();
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
}
