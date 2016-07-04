package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.MyCircleAdapter;
import com.huixiangtv.live.model.Dynamic;
import com.huixiangtv.live.model.Fans;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.ui.HuixiangLoadingLayout;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.StringUtil;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.WidgetUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCircleActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;


    private PullToRefreshListView refreshView;
    MyCircleAdapter adapter ;

    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_circle);
        x.view().inject(this);
        initView();
        loadData();

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
        adapter.setPhotoWidth(WidgetUtil.dip2px(this,80));
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


        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        disTime = "";
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

    private void toPushDynamic() {
        ForwardUtils.target(MyCircleActivity.this, Constant.PUSH_DYNAMIC, null);
    }

    private void initHeadInfo(View view) {
        ImageView ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        if(null!= App.getLoginUser()){
            ImageUtils.displayAvator(ivPhoto,App.getLoginUser().getPhoto());
        }
    }


    /**
     * 加载我的相册动态
     */
    private void loadData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", page + "");
        params.put("pageSize", Constant.PAGE_SIZE);
        RequestUtils.sendPostRequest(Api.MY_DYNAMIC, params, new ResponseCallBack<Dynamic>() {
            @Override
            public void onSuccessList(List<Dynamic> data) {
                super.onSuccessList(data);
                if (data != null && data.size() > 0) {
                    resetData(data);
                    if(page==1){
                        adapter.clear();
                    }
                    adapter.addList(data);
                }else{
                    if (page == 1) {
                        CommonHelper.noData("暂无人气贡献记录哦",refreshView.getRefreshableView(),MyCircleActivity.this,1);
                    }
                }

                refreshView.onRefreshComplete();


            }

            @Override
            public void onSuccess(Dynamic data) {
                super.onSuccess(data);
                refreshView.onRefreshComplete();
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                refreshView.onRefreshComplete();
                CommonHelper.showTip(MyCircleActivity.this, e.getMessage());

            }
        }, Dynamic.class);
    }


    String disTime = "";
    private void resetData(List<Dynamic> data) {
        for (Dynamic dn : data) {
            if(StringUtil.isNotEmpty(dn.getDate())){
                String strTime = com.huixiangtv.live.utils.DateUtils.formatDisplayTime2(dn.getDate(),"yyyy-MM-dd HH:mm:ss");
                if(!strTime.equals(disTime)){
                    dn.setMarginTop(true);
                    disTime = strTime;
                    if(disTime.equals("今天")){
                        dn.setMonth("");
                        dn.setDay("");
                        dn.setLastDate("");
                    }else{
                        if(strTime.contains(",")){
                            String[] md = strTime.split(",");
                            dn.setMonth(md[0]);
                            dn.setDay(md[1]);
                            dn.setLastDate("");
                        }else{
                            dn.setMonth("");
                            dn.setDay("");
                            dn.setLastDate(disTime);
                        }
                    }
                }else{
                    dn.setMonth("");
                    dn.setDay("");
                    dn.setLastDate("");
                }

            }
            Log.i("dndateTime",dn.getLastDate()+"**"+dn.getMonth()+"**"+dn.getDay());
        }
    }


    public void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }
}
