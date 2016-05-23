package com.huixiangtv.live.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.MainActivity;
import com.huixiangtv.live.adapter.CommonAdapter;
import com.huixiangtv.live.adapter.ViewHolder;
import com.huixiangtv.live.model.BannerModel;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.utils.EnumUpdateTag;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.BannerView;
import com.huixiangtv.live.utils.widget.LinearLayoutForListView;
import com.huixiangtv.live.utils.widget.LoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentTabOne extends RootFragment implements  AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private final int PAGE_SIZE = 12;
    private int currPage = 1;
    private int totalPage = 1;
    private ColaProgress cp = null;
    private BannerView bannerView;
    private PullToRefreshScrollView mRefreshLayout;
    private List<BannerModel> guangGao = new ArrayList<BannerModel>();
    private View mRootView;
    MainActivity activity;
    private LinearLayout ll_search;
    private LoadingView loadView;
    private BaseAdapter adapter;
    private ScrollView sv;
    private List<Live> commonModelList = new ArrayList<Live>();
    private LinearLayoutForListView listview;
    RelativeLayout rlpd;


    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tab_one, container, false);
        activity = (MainActivity) getActivity();
        activity.setTitleBar(getString(R.string.today_rm));
        activity.hideTitle(false);

        return mRootView;
    }

    @Override
    protected void initLayout(View view) {
        loadView = (LoadingView) view.findViewById(R.id.loadView);
        loadView.setVisibility(View.VISIBLE);
        ll_search = (LinearLayout) view.findViewById(R.id.ll_search);
        bannerView = (BannerView) view.findViewById(R.id.banner);
        mRefreshLayout = (PullToRefreshScrollView) view.findViewById(R.id.refreshLayout);
        listview = (LinearLayoutForListView) view.findViewById(R.id.listview);
        listview.setVisibility(View.GONE);
        listview.setOnItemClickListener(new LinearLayoutForListView.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, Object item, int position) {
                showToast("gotolive");
            }
        });
        listview.setVisibility(View.GONE);
        loadView.setVisibility(View.VISIBLE);
        mRefreshLayout.setMode(PullToRefreshBase.Mode.BOTH);
        sv = (ScrollView) view.findViewById(R.id.sv);

        mRefreshLayout.setIsUpListen(new PullToRefreshScrollView.isUpListen() {
            @Override
            public void isUp(boolean isUp) {
                if (isUp) {
//                    if(ll_search.getVisibility() ==View.VISIBLE || mRefreshLayout.getMode() == PullToRefreshBase.Mode.MANUAL_REFRESH_ONLY) {
//                        mRefreshLayout.setMode(PullToRefreshBase.Mode.BOTH);
//                    }

                }
            }

            @Override
            public void isTouch(boolean isTouch) {

            }
        });

        //待用 莫删除
        //mRefreshLayout.setOnTouchListener(new TouchListenerImpl());

    }
    @Override
    protected void initData() {

//        dialog =new CustomProgressDialog(getActivity(), "正在加载中",R.anim.loading_frame);
//        dialog.show();

        initAdapter(EnumUpdateTag.UPDATE);
        setListener();

        getBanner();
//        RequestUtils.sendPostRequest(Api.INDEX_BANNER, null, new ResponseCallBack<PositionAdvertBO>() {
//            @Override
//            public void onSuccessList(List<PositionAdvertBO> data) {
//
//                PositionAdvertBO positionAdvertBO = new PositionAdvertBO();
//
//                positionAdvertBO = data.get(0);
//            }
//            @Override
//            public void onFailure(ServiceException e) {
//                super.onFailure(e);
//            }
//        }, PositionAdvertBO.class);

    }

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(mScrollView, 0);
    }


    /**
     * loading and init list
     */
    private void initAdapter(final EnumUpdateTag enumUpdateTag) {


        //put params
        Map<String, String> paramsMap = new HashMap<String,String>();
        paramsMap.put("page",currPage+"");
        paramsMap.put("pagesize",PAGE_SIZE+"");
        paramsMap.put("cNo","");



        RequestUtils.sendPostRequest(Api.LIVE_LIST, paramsMap, new ResponseCallBack<Live>() {
            @Override
            public void onSuccessList(List<Live> data) {

                if(data!=null && data.size()>0) {

                    if(currPage==1)
                    {
                        listview.setVisibility(View.VISIBLE);
                        loadView.setVisibility(View.GONE);
                    }
                    if (enumUpdateTag == EnumUpdateTag.UPDATE) {
                        commonModelList.clear();
                        listview.removeAllViews();
                    }
                    for (Live live : data) {
                        commonModelList.add(live);
                    }
                    Long totalCount = Long.parseLong(data.size()+"");
                    if (0 == totalCount)
                    {
                        Toast.makeText(getActivity(),"已经没有更多内容了",Toast.LENGTH_LONG).show();
                    }
                    else {
                        adapter = new TabOneAdapter(getContext(), commonModelList, R.layout.index_list_pic);
                        listview.setAdapter(adapter);
                    }
                }
                else
                {

                }
                mRefreshLayout.onRefreshComplete();
                ll_search.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                mRefreshLayout.onRefreshComplete();
                showToast("当有网络不可用，请检查您的网络设置");
            }
        }, Live.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }


    private class TabOneAdapter extends CommonAdapter<Live> {

        public TabOneAdapter(Context context, List<Live> listData, int itemLayoutId) {
            super(context, listData, itemLayoutId);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void convert(ViewHolder helper, int position, Live item) {

            TextView tvTitle = helper.getView(R.id.tvTitle);
            TextView tvTime = helper.getView(R.id.tvTime);
            tvTitle.setText(item.getNickName());
            tvTime.setText(item.getTime());
            ImageView ivIcon = helper.getView(R.id.ivIcon);


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)ivIcon.getLayoutParams();
            params.height = (int) (App.screenWidth*0.75);
            ivIcon.setLayoutParams(params);

            ImageUtils.display(ivIcon,item.getPhoto());

        }
    }


    private class TouchListenerImpl implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    break;
                case MotionEvent.ACTION_MOVE:
                    int scrollY=view.getScrollY();
                    int height=view.getHeight();
                    int scrollViewMeasuredHeight=mRefreshLayout.getChildAt(0).getMeasuredHeight();
                    if(scrollY>2 || scrollY<=1){


                        if(ll_search.getVisibility() ==View.VISIBLE) {
                            mRefreshLayout.setMode(PullToRefreshBase.Mode.BOTH);
                        }
                        else {
                            mRefreshLayout.setMode(PullToRefreshBase.Mode.MANUAL_REFRESH_ONLY);
                            ll_search.setVisibility(View.VISIBLE);
                            return true;
                        }
                    }
                    if((scrollY+height)==scrollViewMeasuredHeight){

                    }
                    break;

                default:
                    break;
            }
            return false;
        }

    };

    private Runnable mScrollView = new Runnable() {

        @Override
        public void run() {

            sv.scrollTo(0,258);

        }

    };

    @Override
    protected void onNoDoubleClick(View view) {

    }


    public void setListener() {

        mRefreshLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                listview.setVisibility(View.GONE);
                loadView.setVisibility(View.VISIBLE);
                currPage = 1;
                initAdapter(EnumUpdateTag.UPDATE);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                currPage++;
                initAdapter(EnumUpdateTag.MORE);
            }
        });

        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });
    }

    /**
     * Open Search
     */
    private void startSearch() {
        ForwardUtils.target(getActivity(), Constant.SEARCH, null);
    }

    private void getBanner(){

        Map<String, String> paramsMap = new HashMap<String,String>();
        paramsMap.put("page",currPage+"");
        paramsMap.put("pagesize",PAGE_SIZE+"");
        paramsMap.put("groupName","");
        RequestUtils.sendPostRequest(Api.CONTENT_GET_BANNER, paramsMap, new ResponseCallBack<BannerModel>() {
            @Override
            public void onSuccessList(List<BannerModel> data) {

                if(data!=null && data.size()>0) {

                    guangGao.clear();
                    guangGao.addAll(data);
                    bannerView.setPositionAdvertBO(guangGao);

                }

            }
            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, BannerModel.class);


    }

}
