package com.huixiangtv.live.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.FriendCircleAdapter;
import com.huixiangtv.live.model.Dynamic;
import com.huixiangtv.live.model.DynamicComment;
import com.huixiangtv.live.model.DynamicImage;
import com.huixiangtv.live.model.DynamicpPraise;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.HuixiangLoadingLayout;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentCircle extends Fragment {

    private final String PAGESIZE = "10";
    View mRootView;
    private PullToRefreshListView refreshView;
    private FriendCircleAdapter adapter;
    int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_circle, container, false);
        initView();
        return mRootView;
    }



    private void initView() {
        refreshView = (PullToRefreshListView) mRootView.findViewById(R.id.refreshView);
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshView.setHeaderLayout(new HuixiangLoadingLayout(getActivity()));
        refreshView.setFooterLayout(new HuixiangLoadingLayout(getActivity()));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_circle_head, null, false);
        refreshView.getRefreshableView().addHeaderView(view);

        //点击进入自己的相册圈
        view.findViewById(R.id.ivPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.OWN_CIRCLE, null);
                }
            }
        });
        initHeadInfo(view);
        adapter = new FriendCircleAdapter(getActivity());
        refreshView.setAdapter(adapter);

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
        loadData();

    }

    private void loadData() {

        bindDynamicInfo();

    }


    private void initHeadInfo(View view) {
        ImageView ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        TextView tvNickName = (TextView) view.findViewById(R.id.tvNickName);
        if (null != App.getLoginUser()) {
            tvNickName.setText(App.getLoginUser().getNickName());
            ImageUtils.displayAvator(ivPhoto, App.getLoginUser().getPhoto());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }
    /**
     *  获取圈子列表
     */
    private void bindDynamicInfo() {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", page + "");
        paramsMap.put("pageSize", PAGESIZE);

        RequestUtils.sendPostRequest(Api.DYNAMIC_OWNDYNAMIC, paramsMap, new ResponseCallBack<Dynamic>() {
            @Override
            public void onSuccessList(List<Dynamic> data) {
                if (data != null && data.size() > 0) {
                    if (page == 1) {
                        adapter.clear();
                        adapter.addList(data);
                    } else {
                        adapter.addList(data);
                    }
                } else {
                    if (page == 1) {
                        CommonHelper.noData("暂无动态", refreshView.getRefreshableView(), getActivity(), 2);
                    }
                }
                refreshView.onRefreshComplete();
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(getActivity(), "暂无动态:" + e.getMessage());
                refreshView.onRefreshComplete();

            }
        }, Dynamic.class);
    }



}
