package com.huixiang.live.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huixiang.live.Api;
import com.huixiang.live.model.Topic;
import com.huixiang.live.service.RequestUtils;
import com.huixiang.live.service.ResponseCallBack;
import com.huixiang.live.service.ServiceException;
import com.huixiang.live.utils.widget.LinearLayoutForListView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiang.live.App;
import com.huixiang.live.R;
import com.huixiang.live.activity.MainActivity;
import com.huixiang.live.adapter.CommonAdapter;
import com.huixiang.live.adapter.ViewHolder;
import com.huixiang.live.model.CommonModel;
import com.huixiang.live.model.PositionAdvertBO;
import com.huixiang.live.utils.widget.BannerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentTabOne extends Fragment implements  AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,  View.OnClickListener {

    private final int PAGE_SIZE = 12;
    private int currPage = 1;
    private int totalPage = 1;

    private BannerView bannerView;
    private PullToRefreshScrollView mRefreshLayout;
    private List<PositionAdvertBO> guangGao = new ArrayList<PositionAdvertBO>();
    private View mRootView;
    MainActivity activity;
    private LinearLayout ll_search;
    private BaseAdapter adapter;
    private ScrollView sv;
    private List<CommonModel> commonModelList = new ArrayList<CommonModel>();
    private LinearLayoutForListView listview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tab_one, container, false);
        activity = (MainActivity) getActivity();
        activity.setTitleBar(getString(R.string.today_rm));
        activity.hideTitle(false);

        findView();
        BindBinnerData();
        initAdapter();
        setListener();

        return mRootView;
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
    private void initAdapter() {
        int img_arr[] ={R.drawable.test_pic1,R.drawable.test_pic2,R.drawable.test_pic3,R.drawable.test_pic4,R.drawable.test_pic5};
        //模拟数据
        commonModelList.clear();
        for (int i = 0; i < 5; i++) {
            CommonModel commonModel1 = new CommonModel();
            //commonModel1.iconUrl = "http://f1.jgyes.com/4,013f52a5e0fd91";
            commonModel1.title = "超级女生2016排位赛"+i;
            commonModel1.setTime("5月30日 17：5"+i);
            commonModel1.img_id = img_arr[i];
            commonModelList.add(commonModel1);
        }
        adapter = new TabOneAdapter(getContext(), commonModelList, R.layout.index_list_pic);
        listview.setAdapter(adapter);
        mRefreshLayout.onRefreshComplete();
        ll_search.setVisibility(View.GONE);


//
//
//        Map<String, String> paramsMap = new HashMap<String,String>();
//        paramsMap.put("page",currPage+"");
//        paramsMap.put("pagesize",PAGE_SIZE+"");


//        RequestUtils.sendPostRequest(Api.LIVE_LIST, paramsMap, new ResponseCallBack<CommonModel>() {
//            @Override
//            public void onSuccessList(List<CommonModel> data) {
//
//                CommonModel CommonModel = new CommonModel();
//
//                CommonModel = data.get(0);
//            }
//            @Override
//            public void onFailure(ServiceException e) {
//                super.onFailure(e);
//            }
//        }, CommonModel.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }


    private class TabOneAdapter extends CommonAdapter<CommonModel> {

        public TabOneAdapter(Context context, List<CommonModel> listData, int itemLayoutId) {
            super(context, listData, itemLayoutId);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void convert(ViewHolder helper, int position, CommonModel item) {

            TextView tvTitle = helper.getView(R.id.tvTitle);
            TextView tvTime = helper.getView(R.id.tvTime);
            tvTitle.setText(item.title);
            tvTime.setText(item.getTime());
            ImageView ivIcon = helper.getView(R.id.ivIcon);
            //BitmapHelper.getInstance(mContext).display(ivIcon, item.iconUrl, "" , BitmapHelper.DefaultSize.BIG);

            ivIcon.setBackground(getResources().getDrawable(item.img_id));
        }
    }


    private void BindBinnerData() {

        PositionAdvertBO positionAdvertBO = new PositionAdvertBO();
        positionAdvertBO.setAdImgPath("http://f1.jgyes.com/4,013f52a5e0fd91");
        PositionAdvertBO positionAdvertBO1 = new PositionAdvertBO();
        positionAdvertBO1.setAdImgPath("http://img3.imgtn.bdimg.com/it/u=1206514979,2546214886&fm=21&gp=0.jpg");
        PositionAdvertBO positionAdvertBO2 = new PositionAdvertBO();
        positionAdvertBO2.setAdImgPath("http://f1.jgyes.com/3,013e1fcbd9d368");

        List<PositionAdvertBO> positionAdvertBOList = new ArrayList<PositionAdvertBO>();
        positionAdvertBOList.add(positionAdvertBO);
        positionAdvertBOList.add(positionAdvertBO1);
        positionAdvertBOList.add(positionAdvertBO2);
        guangGao.clear();
        guangGao.addAll(positionAdvertBOList);
        bannerView.setPositionAdvertBO(guangGao);

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

    private void findView() {
        ll_search = (LinearLayout) mRootView.findViewById(R.id.ll_search);
        bannerView = (BannerView) mRootView.findViewById(R.id.banner);
        mRefreshLayout = (PullToRefreshScrollView) mRootView.findViewById(R.id.refreshLayout);
        listview = (LinearLayoutForListView) mRootView.findViewById(R.id.listview);
        listview.setOnItemClickListener(new LinearLayoutForListView.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, Object item, int position) {
                Toast.makeText(getActivity(), "gotoLive", Toast.LENGTH_LONG).show();
            }
        });
        mRefreshLayout.setMode(PullToRefreshBase.Mode.MANUAL_REFRESH_ONLY);
        sv = (ScrollView) mRootView.findViewById(R.id.sv);

        mRefreshLayout.setIsUpListen(new PullToRefreshScrollView.isUpListen() {
            @Override
            public void isUp(boolean isUp) {
                if (isUp) {
                    if(ll_search.getVisibility() ==View.VISIBLE || mRefreshLayout.getMode() == PullToRefreshBase.Mode.MANUAL_REFRESH_ONLY) {
                        mRefreshLayout.setMode(PullToRefreshBase.Mode.BOTH);
                    }

                }
            }

            @Override
            public void isTouch(boolean isTouch) {
                if (isTouch) {
                }
            }
        });
        mRefreshLayout.setOnTouchListener(new TouchListenerImpl());
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
    public void onClick(View v) {

    }

    public void setListener() {

        mRefreshLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                 initAdapter();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                currPage++;
                if (currPage <= totalPage) {

                } else {
                    Toast.makeText(getActivity(),"已经没有更多内容了",Toast.LENGTH_LONG).show();
                }
                initAdapter();
            }
        });
    }


}
