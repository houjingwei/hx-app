package com.huixiangtv.liveshow.activity;

//
//import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
//import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

//public class RefreshAndLoadmoreActivity extends BaseBackActivity implements  BGARefreshLayout.BGARefreshLayoutDelegate {
//
//    private BGARefreshLayout mRefreshLayout;
//    private ListView mDataLv;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_refresh_and_loadmore);
//        initView();
//        setListener();
//        processLogic(savedInstanceState);
//    }
//
//
//    public void initView() {
//        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.refreshLayout);
//        mDataLv = (ListView) findViewById(R.id.data);
//    }
//
//    public void setListener() {
//        mRefreshLayout.setDelegate(this);
//    }
//
//    public void processLogic(Bundle savedInstanceState) {
//        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(App.getContext(), true));
//    }
//
//
//    @Override
//    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
//        mDataLv.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mRefreshLayout.endRefreshing();
//            }
//        }, 2000);
//    }
//
//    @Override
//    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
//        mDataLv.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mRefreshLayout.endLoadingMore();
//            }
//        }, 2000);
//        return true;
//    }
//}

public class RefreshAndLoadmoreActivity
{}
