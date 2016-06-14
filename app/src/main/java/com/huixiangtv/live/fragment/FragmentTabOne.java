package com.huixiangtv.live.fragment;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.huixiangtv.live.adapter.LiveBannerAdapter;
import com.huixiangtv.live.adapter.ViewHolder;
import com.huixiangtv.live.model.BannerModel;
import com.huixiangtv.live.model.ChatMessage;
import com.huixiangtv.live.model.HistoryMsg;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.model.LiveMsg;
import com.huixiangtv.live.model.MsgExt;
import com.huixiangtv.live.model.PlayUrl;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.EnumUpdateTag;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.BannerView;
import com.huixiangtv.live.utils.widget.LinearLayoutForListView;
import com.huixiangtv.live.utils.widget.SwitchPageControlView;
import com.huixiangtv.live.utils.widget.SwitchScrollLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentTabOne extends RootFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static boolean isLoad =false;
    private SwitchScrollLayout mSwitchScrollLayout;
    private DataLoading dataLoad;
    public SwitchPicHandler switchPicHandler;
    private static final float APP_PAGE_SIZE = 1.0f;
    private SwitchPageControlView pageControl;
    private int currentViewPage = 1;
    private final int PAGE_SIZE = 120;
    private int currPage = 1;
    private ColaProgress cp = null;
    private TextView tvAddress;
    private BannerView bannerView;
    private TextView tvInfo, tvLoveCount, tvWeight,tvbName1,tvContent1,tvbName2,tvContent2,tvbName3,tvContent3,tvbName4,tvContent4;
    private PullToRefreshScrollView mRefreshLayout;
    private List<BannerModel> guangGao = new ArrayList<BannerModel>();
    private View mRootView;
    MainActivity activity;
    private LinearLayout ll_search;
    private BaseAdapter adapter;
    private LinearLayout llInfo;
    private ScrollView sv;
    private static List<Live> viewpageModelList = new ArrayList<Live>();
    private LinearLayoutForListView listview;
    private LiveListBroadcast receiver;
    private FrameLayout flInfo;
    private String ACTION = "com.android.broadcast.RECEIVER_ACTION";
    private LinearLayout llone_viewpager;


    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tab_one, container, false);
        activity = (MainActivity) getActivity();

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
        pageControl = (SwitchPageControlView) view.findViewById(R.id.pageControl);
        ll_search = (LinearLayout) view.findViewById(R.id.ll_search);
        tvInfo = (TextView) view.findViewById(R.id.tvInfo);
        tvLoveCount = (TextView) view.findViewById(R.id.tvLoveCount);
        tvWeight = (TextView) view.findViewById(R.id.tvWeight);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        bannerView = (BannerView) view.findViewById(R.id.banner);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int) (App.screenWidth * 0.28));
        bannerView.setLayoutParams(layoutParams);
        tvbName1 = (TextView) view.findViewById(R.id.tvbName1);
        tvContent1 = (TextView) view.findViewById(R.id.tvContent1);
        tvbName2 = (TextView) view.findViewById(R.id.tvbName2);
        tvContent2 = (TextView) view.findViewById(R.id.tvContent2);
        tvbName3 = (TextView) view.findViewById(R.id.tvbName3);
        tvContent3 = (TextView) view.findViewById(R.id.tvContent3);
        tvbName4 = (TextView) view.findViewById(R.id.tvbName4);
        tvContent4 = (TextView) view.findViewById(R.id.tvContent4);
        mRefreshLayout = (PullToRefreshScrollView) view.findViewById(R.id.refreshLayout);
        listview = (LinearLayoutForListView) view.findViewById(R.id.listview);
        listview.setVisibility(View.GONE);
        listview.setOnItemClickListener(new LinearLayoutForListView.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, Object item, int position) {
                Live live = (Live) adapter.getItem(position);
                loadUrlAndShow(live);
            }

        });
        listview.setVisibility(View.GONE);
        mRefreshLayout.setMode(PullToRefreshBase.Mode.BOTH);
        sv = (ScrollView) view.findViewById(R.id.sv);
        llone_viewpager = (LinearLayout) view.findViewById(R.id.llone_viewpager);
        llone_viewpager.setVisibility(View.GONE);
        mSwitchScrollLayout = (SwitchScrollLayout) view.findViewById(R.id.ScrollLayoutTest);
        llInfo = (LinearLayout) view.findViewById(R.id.llInfo);
        flInfo = (FrameLayout) view.findViewById(R.id.flInfo);
    }

    private void loadUrlAndShow(final Live live) {
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("lid",live.getLid());
        RequestUtils.sendPostRequest(Api.PLAY_URL, params, new ResponseCallBack<PlayUrl>() {
            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }

            @Override
            public void onSuccess(PlayUrl data) {
                super.onSuccess(data);
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("isPlay","true");
                map.put("lid",live.getLid());
                map.put("playUrl",data.getUrl());
                ForwardUtils.target(getActivity(),Constant.LIVE,map);
            }

            @Override
            public void onSuccessList(List<PlayUrl> datas) {
                super.onSuccessList(datas);
                if(null!=datas && datas.size()>0){
                    PlayUrl data = datas.get(0);
                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put("isPlay","true");
                    map.put("playUrl",data.getUrl());
                    map.put("lid",live.getLid());
                    ForwardUtils.target(getActivity(),Constant.LIVE,map);
                }
            }
        },PlayUrl.class);
    }

    @Override
        protected void initData () {
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

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", currPage + "");
        paramsMap.put("pageSize", PAGE_SIZE + "");
        paramsMap.put("cNo", "TJ");


        RequestUtils.sendPostRequest(Api.LIVE_LIST, paramsMap, new ResponseCallBack<Live>() {
            @Override
            public void onSuccessList(List<Live> data) {

                if (data != null && data.size() > 0) {

                    if (currPage == 1) {
                        listview.setVisibility(View.VISIBLE);
                    }
                    if (enumUpdateTag == EnumUpdateTag.UPDATE) {

                        listview.removeAllViews();
                        currentViewPage =1;
                        switchPicHandler = new SwitchPicHandler(getContext(), 1);
                        //起一个线程更新数据
                        SwitchPicThread switchPicThread = new SwitchPicThread();
                        new Thread(switchPicThread).start();
                    }

                    Long totalCount = Long.parseLong(data.size() + "");
                    if (0 == totalCount) {
                        Toast.makeText(getActivity(), "已经没有更多内容了", Toast.LENGTH_LONG).show();
                    } else {
                        adapter = new TabOneAdapter(getContext(), data, R.layout.index_list_pic);
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
                CommonHelper.showTip(getActivity(),e.getMessage());
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
            String city = item.getCity()==null?"":item.getCity();
            tvTitle.setText(item.getNickName()+" "+city);
            tvTime.setText(item.getTime());
            ImageView ivIcon = helper.getView(R.id.ivIcon);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivIcon.getLayoutParams();
            params.height = (int) (App.screenWidth * 0.75);
            ivIcon.setLayoutParams(params);
            ImageUtils.display(ivIcon,item.getImg2());

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
                    activity.hideTitle(false);
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
        paramsMap.put("pageSize", PAGE_SIZE + "");
        paramsMap.put("cNo", "TJ");

        RequestUtils.sendPostRequest(Api.LIVE_LIST, paramsMap, new ResponseCallBack<Live>() {
            @Override
            public void onSuccessList(List<Live> data) {

                isLoad = true;
                if (data != null && data.size() > 0) {

                    Long totalCount = Long.parseLong(data.size() + "");
                    if (0 == totalCount) {
                        CommonHelper.showTip(getActivity(),"已经没有更多内容了");
                    } else {
                        int pageNo = (int) Math.ceil(data.size() / APP_PAGE_SIZE);
                        for (int i = 0; i < pageNo; i++) {
                            ListView appPage = new ListView(getContext());
                            // get the "i" page data
                            if(currentViewPage ==1) {
                                viewpageModelList.clear();
                                mSwitchScrollLayout.removeAllViews();
                            }

                            viewpageModelList.addAll(data);
                            appPage.setAdapter(new LiveBannerAdapter(getActivity(), getContext(), data, i));
                            appPage.setOnItemClickListener(listener);
                            mSwitchScrollLayout.addView(appPage);

                            currentViewPage++;

                        }
                        Live live = viewpageModelList.get(0);
                        initViewInfo(live);

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
                CommonHelper.showTip(getActivity(),e.getMessage());
            }
        }, Live.class);

    }


    private void loadMsg(String lid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("chatroom", lid);
        RequestUtils.sendPostRequest(Api.HISTORY_MSG, params, new ResponseCallBack<HistoryMsg>() {
            @Override
            public void onSuccess(HistoryMsg data) {
                super.onSuccess(data);
                resetTextView();
                if (null != data.getLastMsg() && data.getLastMsg().size() > 0) {
                    int i = 0;
                    for (ChatMessage message : data.getLastMsg()) {

                        MsgExt ext = message.getExt();
                        if (null != ext) {
                            if(i==0) {
                                tvContent1.setText(message.getContent().toString());
                                tvbName1.setText(ext.getNickName()+": ");
                            }
                            else if(i==1)
                            {
                                tvContent2.setText(message.getContent().toString());
                                String name =ext.getNickName().toString()+": ";
                                tvbName2.setText(name);
                            }
                            else if(i==2)
                            {
                                tvContent3.setText(message.getContent().toString());
                                tvbName3.setText(ext.getNickName()+": ");
                            }
                            else if(i==3)
                            {
                                tvContent4.setText(message.getContent().toString());
                                tvbName4.setText(ext.getNickName()+": ");
                            }
                            i++;
                        }
                        if (i == 4) break;
                    }

                }
                else
                {
                    resetTextView();
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, HistoryMsg.class);
    }

    private void resetTextView(){
        tvbName1.setText("");
        tvContent1.setText("");
        tvbName2.setText("");
        tvContent2.setText("");
        tvbName3.setText("");
        tvContent3.setText("");
        tvbName4.setText("");
        tvContent4.setText("");
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
                        generatePageControl(currentIndex);
                        Live live = viewpageModelList.get(currentIndex);
                        initViewInfo(live);

                    }
                }
            });
        }


        private void generatePageControl(int currentIndex) {
            if ((count - 1) == currentIndex) {
                if(isLoad) {
                    isLoad=false;
                    SwitchPicThread m = new SwitchPicThread();
                    new Thread(m).start();
                }
            }
        }
    }

    private void initViewInfo(Live live)
    {
        //CommonHelper.viewSetBackageImag(live.getPhoto(),llInfo);
        tvInfo.setText(live.getNickName());
        tvLoveCount.setText(live.getLoveCount());
        String hei =  live.getHeight()==null?"165 Cm  ":live.getHeight() + "Cm  ";
        String wei =  live.getWeight()==null?"4 5 Kg  ":live.getWeight() + "Kg  ";
        String bwh =  live.getBwh() == null?"    " :"三围:  "+live.getBwh();
        tvWeight.setText(hei+ wei+bwh);
        tvAddress.setText(live.getCity()==null?"":live.getCity());
        loadMsg(live.getLid());
    }

    public AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            ForwardUtils.target(getActivity(), Constant.START_LIVE, null);        }

    };




    public class ProgressThreadPicAsyncTask extends AsyncTask<Integer, Integer, String> {


        @Override
        protected String doInBackground(Integer... params) {
             return "";
        }

        @Override
        protected void onPostExecute(String result) {
        }


        @Override
        protected void onPreExecute() {
        }


    }
}
