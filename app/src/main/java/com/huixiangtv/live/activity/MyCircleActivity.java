package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.MyCircleAdapter;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.ui.HuixiangLoadingLayout;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.ImageUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class MyCircleActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;


    private PullToRefreshListView refreshView;
    MyCircleAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_circle);
        x.view().inject(this);
        initView();
        initData(); 

    }



    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.my_circle));
        commonTitle.imgShow(View.VISIBLE);
        commonTitle.getImg().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发布动态
                toPushDynamic();
            }
        });
        refreshView = (PullToRefreshListView) findViewById(R.id.refreshView);
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshView.setHeaderLayout(new HuixiangLoadingLayout(this));
        refreshView.setFooterLayout(new HuixiangLoadingLayout(this));
        refreshView.setAdapter(adapter);
        adapter = new MyCircleAdapter(this);
        refreshView.setAdapter(adapter);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_my_circle_head, null, false);
        refreshView.getRefreshableView().addHeaderView(view);

        View todayAddView = LayoutInflater.from(this).inflate(R.layout.activity_my_circle_today_view, null, false);
        refreshView.getRefreshableView().addHeaderView(todayAddView);
        todayAddView.findViewById(R.id.ivAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //发布动态
            toPushDynamic();
            }
        });
        initHeadInfo(view);

    }

    private void toPushDynamic() {
        ForwardUtils.target(MyCircleActivity.this, Constant.PUSH_DYNAMIC, null);
    }

    private void initHeadInfo(View view) {
        ImageView ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        if(null!= App.getLoginUser()){
            ImageUtils.displayAvator(ivPhoto,App.getLoginUser().getPhoto());
        }
    }


    private void initData() {
    }


}
