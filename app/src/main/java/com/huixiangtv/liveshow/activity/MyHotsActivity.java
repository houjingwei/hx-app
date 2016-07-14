package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.MyAttentionAdapter;
import com.huixiangtv.liveshow.model.Fans;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.ui.HuixiangLoadingLayout;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.image.ImageUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyHotsActivity extends BaseBackActivity {

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;


    private PullToRefreshListView refreshView;


    int page = 1;
    MyAttentionAdapter adapter;

    LinearLayout ll1;
    LinearLayout ll2;
    LinearLayout ll3;
    ImageView ivPhoto1;
    ImageView ivPhoto2;
    ImageView ivPhoto3;
    TextView tvNickName1;
    TextView tvNickName2;
    TextView tvNickName3;
    TextView tvHot1;
    TextView tvHot2;
    TextView tvHot3;

    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_hots);
        x.view().inject(this);
        initview();
    }

    private void initview() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.labenHotRank));

        adapter = new MyAttentionAdapter(MyHotsActivity.this);
        adapter = new MyAttentionAdapter(this);
        refreshView = (PullToRefreshListView) findViewById(R.id.refreshView);
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshView.setHeaderLayout(new HuixiangLoadingLayout(this));
        refreshView.setFooterLayout(new HuixiangLoadingLayout(this));
        refreshView.setAdapter(adapter);
        view = LayoutInflater.from(MyHotsActivity.this).inflate(R.layout.my_hot_header, null, false);
        refreshView.getRefreshableView().addHeaderView(view);


        ll1 = (LinearLayout) view.findViewById(R.id.ll1);
        ll2 = (LinearLayout) view.findViewById(R.id.ll2);
        ll3 = (LinearLayout) view.findViewById(R.id.ll3);
        ivPhoto1 = (ImageView) view.findViewById(R.id.ivPhoto1);
        ivPhoto2 = (ImageView) view.findViewById(R.id.ivPhoto2);
        ivPhoto3 = (ImageView) view.findViewById(R.id.ivPhoto3);
        tvNickName1 = (TextView) view.findViewById(R.id.tvNickName1);
        tvNickName2 = (TextView) view.findViewById(R.id.tvNickName2);
        tvNickName3 = (TextView) view.findViewById(R.id.tvNickName3);
        tvHot1 = (TextView) view.findViewById(R.id.tvHot1);
        tvHot2 = (TextView) view.findViewById(R.id.tvHot2);
        tvHot3 = (TextView) view.findViewById(R.id.tvHot3);

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
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", page + "");
        params.put("pageSize", Constant.PAGE_SIZE);
        RequestUtils.sendPostRequest(Api.MY_HOTS, params, new ResponseCallBack<Fans>() {
            @Override
            public void onSuccessList(List<Fans> data) {
                super.onSuccessList(data);
                if (data != null && data.size() > 0) {
                    if (page == 1) {
                        adapter.clear();
                        Log.i("noData","123");

                        Log.i("noData","2");
                        List<Fans> fansList = setTop3AndFanslist(data);
                        adapter.addList(fansList);

                    } else {
                        adapter.addList(data);
                    }
                }else{
                    if (page == 1) {
                        refreshView.getRefreshableView().removeHeaderView(view);
                        CommonHelper.noData("暂无人气贡献记录哦",refreshView.getRefreshableView(),MyHotsActivity.this,1);
                    }
                }

                refreshView.onRefreshComplete();


            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(MyHotsActivity.this, e.getMessage());
                refreshView.onRefreshComplete();
            }
        }, Fans.class);

    }

    private List<Fans> setTop3AndFanslist(List<Fans> fansList) {
        List<Fans> top3 = null;
        if (null != fansList && fansList.size() > 3) {
            top3 = fansList.subList(0, 3);
            fansList = fansList.subList(3, fansList.size());
        } else {
            top3 = fansList;
            fansList = null;
        }
        if (null != top3) {
            for (int i = 0; i < top3.size(); i++) {
                Fans fans = top3.get(i);
                if (i == 0) {
                    ll1.setVisibility(View.VISIBLE);
                    ImageUtils.displayAvator(ivPhoto1, fans.getPhoto());
                    tvNickName1.setText(fans.getNickName());
                    tvHot1.setText(fans.getDevoteValue());
                } else if (i == 1) {
                    ll2.setVisibility(View.VISIBLE);
                    ImageUtils.displayAvator(ivPhoto2, fans.getPhoto());
                    tvNickName2.setText(fans.getNickName());
                    tvHot2.setText(fans.getDevoteValue());
                } else if (i == 2) {
                    ll3.setVisibility(View.VISIBLE);
                    ImageUtils.displayAvator(ivPhoto3, fans.getPhoto());
                    tvNickName3.setText(fans.getNickName());
                    tvHot3.setText(fans.getDevoteValue());
                }
            }
        }

        return fansList;
    }


}