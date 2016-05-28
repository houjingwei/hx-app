package com.huixiangtv.live.fragment;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
import com.huixiangtv.live.adapter.LiveBannerAdapter;
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
import com.huixiangtv.live.utils.widget.SwitchPageControlView;
import com.huixiangtv.live.utils.widget.SwitchScrollLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentTabOne extends RootFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private SwitchScrollLayout mSwitchScrollLayout;
    private DataLoading dataLoad;
    public SwitchPicHandler switchPicHandler;
    private static final float APP_PAGE_SIZE = 1.0f;
    private SwitchPageControlView pageControl;
    private int currentViewPage = 0;
    private final int PAGE_SIZE = 12;
    private int currPage = 1;
    private ColaProgress cp = null;
    private BannerView bannerView;
    private TextView tvInfo, tvLoveCount, tvWeight;
    private PullToRefreshScrollView mRefreshLayout;
    private List<BannerModel> guangGao = new ArrayList<BannerModel>();
    private View mRootView;
    MainActivity activity;
    private LinearLayout ll_search;
    private LoadingView loadView;
    private BaseAdapter adapter;
    private LinearLayout llInfo;
    private ScrollView sv;
    private List<Live> commonModelList = new ArrayList<Live>();
    private static List<Live> viewpageModelList = new ArrayList<Live>();
    private LinearLayoutForListView listview;
    private LiveListBroadcast receiver;
    private String ACTION = "com.android.broadcast.RECEIVER_ACTION";
    private LinearLayout llone_viewpager;

    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tab_one, container, false);
        activity = (MainActivity) getActivity();
        activity.setTitleBar(getString(R.string.today_rm));
        activity.hideTitle(false);
        receiver = new LiveListBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        //动态注册BroadcastReceiver
        getActivity().registerReceiver(receiver, filter);
        return mRootView;
    }

    @Override
    protected void initLayout(View view) {
        //loadView = (LoadingView) view.findViewById(R.id.loadView);
        //loadView.setVisibility(View.VISIBLE);
        pageControl = (SwitchPageControlView) view.findViewById(R.id.pageControl);
        ll_search = (LinearLayout) view.findViewById(R.id.ll_search);
        tvInfo = (TextView) view.findViewById(R.id.tvInfo);
        tvLoveCount = (TextView) view.findViewById(R.id.tvLoveCount);
        tvWeight = (TextView) view.findViewById(R.id.tvWeight);
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
        mRefreshLayout.setMode(PullToRefreshBase.Mode.BOTH);
        sv = (ScrollView) view.findViewById(R.id.sv);
        llone_viewpager = (LinearLayout) view.findViewById(R.id.llone_viewpager);
        llone_viewpager.setVisibility(View.GONE);
        mSwitchScrollLayout = (SwitchScrollLayout) view.findViewById(R.id.ScrollLayoutTest);
        llInfo = (LinearLayout) view.findViewById(R.id.llInfo);
        //llInfo.getBackground().setAlpha(200);

    }

    @Override
    protected void initData() {

//        dialog =new CustomProgressDialog(getActivity(), "正在加载中",R.anim.loading_frame);
//        dialog.show();

        initAdapter(EnumUpdateTag.UPDATE);
        setListener();
        getBanner();
        dataLoad = new DataLoading();

        switchPicHandler = new SwitchPicHandler(getContext(), 1);
        //起一个线程更新数据
        SwitchPicThread switchPicThread = new SwitchPicThread();
        new Thread(switchPicThread).start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    /**
     * loading and init list
     */
    private void initAdapter(final EnumUpdateTag enumUpdateTag) {


        //put params
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", currPage + "");
        paramsMap.put("pagesize", PAGE_SIZE + "");
        paramsMap.put("cNo", "");


        RequestUtils.sendPostRequest(Api.LIVE_LIST, paramsMap, new ResponseCallBack<Live>() {
            @Override
            public void onSuccessList(List<Live> data) {

                if (data != null && data.size() > 0) {

                    if (currPage == 1) {
                        listview.setVisibility(View.VISIBLE);
                        //loadView.setVisibility(View.GONE);
                    }
                    if (enumUpdateTag == EnumUpdateTag.UPDATE) {
                        commonModelList.clear();
                        listview.removeAllViews();
                    }
                    for (Live live : data) {
                        commonModelList.add(live);
                    }
                    Long totalCount = Long.parseLong(data.size() + "");
                    if (0 == totalCount) {
                        Toast.makeText(getActivity(), "已经没有更多内容了", Toast.LENGTH_LONG).show();
                    } else {
                        adapter = new TabOneAdapter(getContext(), commonModelList, R.layout.index_list_pic);
                        listview.setAdapter(adapter);
                    }
                } else {
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
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivIcon.getLayoutParams();
            params.height = (int) (App.screenWidth * 0.75);
            ivIcon.setLayoutParams(params);
            ImageUtils.display(ivIcon, item.getPhoto());

        }
    }

    @Override
    protected void onNoDoubleClick(View view) {

    }


    public void setListener() {

        mRefreshLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                listview.setVisibility(View.GONE);
                //loadView.setVisibility(View.VISIBLE);
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

    private void getBanner() {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", currPage + "");
        paramsMap.put("pagesize", PAGE_SIZE + "");
        paramsMap.put("groupName", "tj");
        RequestUtils.sendPostRequest(Api.CONTENT_GET_BANNER, paramsMap, new ResponseCallBack<BannerModel>() {
            @Override
            public void onSuccessList(List<BannerModel> data) {

                if (data != null && data.size() > 0) {

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


    private class LiveListBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getStringExtra("type") != null) {
                if (intent.getStringExtra("type").toString().equals("0")) {
                    mRefreshLayout.setVisibility(View.GONE);
                    llone_viewpager.setVisibility(View.VISIBLE);

                    activity.hideTitle(true);
                } else if (intent.getStringExtra("type").toString().equals("1")) {
                    llone_viewpager.setVisibility(View.GONE);
                    mRefreshLayout.setVisibility(View.VISIBLE);
                    activity.hideTitle(false);
                }
            }

        }
    }

    /**
     * load  more data
     */
    private void loadMore() {
        //put params
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", currentViewPage + "");
        paramsMap.put("pagesize", PAGE_SIZE + "");
        paramsMap.put("cNo", "");


        RequestUtils.sendPostRequest(Api.LIVE_LIST, paramsMap, new ResponseCallBack<Live>() {
            @Override
            public void onSuccessList(List<Live> data) {

                if (data != null && data.size() > 0) {

                    Long totalCount = Long.parseLong(data.size() + "");
                    if (0 == totalCount) {
                        Toast.makeText(getActivity(), "已经没有更多内容了", Toast.LENGTH_LONG).show();
                    } else {
                        int pageNo = (int) Math.ceil(data.size() / APP_PAGE_SIZE);
                        for (int i = 0; i < pageNo; i++) {
                            GridView appPage = new GridView(getContext());
                            // get the "i" page data
                            if(currentViewPage ==1)
                                viewpageModelList.clear();

                            viewpageModelList.addAll(data);
                            appPage.setAdapter(new LiveBannerAdapter(getContext(), data, i));
                            appPage.setNumColumns(1);
                            appPage.setOnItemClickListener(listener);
                            mSwitchScrollLayout.addView(appPage);
                        }
                        //loading page
                        pageControl.bindScrollViewGroup(mSwitchScrollLayout);
                        //loading paging data
                        dataLoad.bindScrollViewGroup(mSwitchScrollLayout);
                    }
                } else {

                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                mRefreshLayout.onRefreshComplete();
                showToast("当有网络不可用，请检查您的网络设置");
            }
        }, Live.class);

    }


    class SwitchPicThread implements Runnable {
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String msglist = "1";
            Message msg = new Message();
            Bundle b = new Bundle();
            b.putString("rmsg", msglist);
            msg.setData(b);
            FragmentTabOne.this.switchPicHandler.sendMessage(msg);
        }
    }


    class SwitchPicHandler extends Handler {
        public SwitchPicHandler(Context conn, int a) {
        }
        public SwitchPicHandler(Looper L) {
            super(L);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            String rmsg = b.getString("rmsg");
            if ("1".equals(rmsg)) {
                // do nothing
                loadMore();
            }
        }
    }

    //pading data
    class DataLoading {
        private int count;

        public void bindScrollViewGroup(SwitchScrollLayout scrollViewGroup) {
            this.count = scrollViewGroup.getChildCount();
            scrollViewGroup.setOnScreenChangeListenerDataLoad(new SwitchScrollLayout.OnScreenChangeListenerDataLoad() {
                public void onScreenChange(int currentIndex) {
                    generatePageControl(currentIndex);
                }
            });
            scrollViewGroup.setOnScreenChangeListener(new SwitchScrollLayout.OnScreenChangeListener() {
                @Override
                public void onScreenChange(int currentIndex) {
                    if (viewpageModelList != null) {
                        Live live = viewpageModelList.get(currentIndex);
                        tvInfo.setText(live.getNickName());
                        tvLoveCount.setText(live.getLoveCount());
                        tvWeight.setText(live.getHeight() + "Cm     " + live.getWeight() + "    三围：" + live.getBwh());
                    }
                }
            });
        }

        private void generatePageControl(int currentIndex) {
            if ((count - 3) == currentIndex + 1) {
                SwitchPicThread m = new SwitchPicThread();
                new Thread(m).start();
            }
        }
    }


    public AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            showToast("click");
        }

    };


}
