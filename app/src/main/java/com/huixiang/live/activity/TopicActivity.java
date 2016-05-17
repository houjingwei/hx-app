package com.huixiang.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huixiang.live.Api;
import com.huixiang.live.App;
import com.huixiang.live.R;
import com.huixiang.live.adapter.TopicAdapter;
import com.huixiang.live.model.Topic;
import com.huixiang.live.service.RequestUtils;
import com.huixiang.live.service.ResponseCallBack;
import com.huixiang.live.service.ServiceException;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.List;

public class TopicActivity extends BaseBackActivity implements View.OnClickListener{


    @ViewInject(R.id.title)
    TextView txTitle;
    @ViewInject(R.id.back)
    ImageView ivBack;


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
        txTitle.setText(R.string.selTheme);
        ivBack.setOnClickListener(this);
        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshLayout);
        mDataLv = (ListView) findViewById(R.id.data);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);


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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
}
