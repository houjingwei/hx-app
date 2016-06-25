package com.huixiangtv.live.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.MainActivity;
import com.huixiangtv.live.adapter.ListViewPagerAdapter;
import com.huixiangtv.live.adapter.LiveAdapter;
import com.huixiangtv.live.common.CommonUtil;
import com.huixiangtv.live.model.BannerModel;
import com.huixiangtv.live.model.ChatMessage;
import com.huixiangtv.live.model.HistoryMsg;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.model.MsgExt;
import com.huixiangtv.live.model.PlayUrl;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.EnumUpdateTag;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.widget.BannerView;
import com.huixiangtv.live.utils.widget.FlyTxtView;
import com.huixiangtv.live.utils.widget.amin.DepthPageTransformer;
import com.huixiangtv.live.utils.widget.parallax.Mode;
import com.huixiangtv.live.utils.widget.parallax.ParallaxViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class FragmentTabOne extends RootFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListViewPagerAdapter listPager;
    private static boolean isLoad = false;
    private LinearLayout main;
    public SwitchPicHandler switchPicHandler;
    private int currentViewPage = 1;
    private final int PAGE_SIZE = 5;
    private int currPage = 1;
    private RelativeLayout rotRl;
    private ColaProgress cp = null;
    private TextView tvAddress;
    private BannerView bannerView;
    private FlyTxtView tvInfo;
    private TextView tvLoveCount, tvWeight, tvbName1, tvContent1, tvbName2, tvContent2, tvbName3, tvContent3, tvbName4, tvContent4, tvbName5, tvContent5;
    private List<BannerModel> guangGao = new ArrayList<BannerModel>();
    private View mRootView;
    MainActivity activity;
    private LinearLayout ll_search;
    private LiveAdapter adapter;
    private LinearLayout llInfo;
    private ScrollView sv;
    private static List<Live> viewpageModelList = new ArrayList<Live>();
    private ListView listview;
    private LiveListBroadcast receiver;
    private FrameLayout flInfo;
    private String ACTION = "com.android.broadcast.RECEIVER_ACTION";
    private LinearLayout llone_viewpager;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private ParallaxViewPager vpager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tab_one, container, false);
        activity = (MainActivity) getActivity();
//        int flag = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        Window window = activity.getWindow();
//        window.setFlags(flag, flag);
//        activity.hideTitle(false);
        receiver = new LiveListBroadcast();
        initLayout(mRootView);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        //动态注册BroadcastReceiver
        getActivity().registerReceiver(receiver, filter);
        initData();
        return mRootView;
    }


    protected void initLayout(View view) {
//        pageControl = (SwitchPageControlView) view.findViewById(R.id.pageControl);
        tvInfo = (FlyTxtView) view.findViewById(R.id.tvInfo);
        tvInfo.setTextSize(16);
        tvInfo.startAnimation();
        tvLoveCount = (TextView) view.findViewById(R.id.tvLoveCount);
        tvWeight = (TextView) view.findViewById(R.id.tvWeight);
        tvWeight = (TextView) view.findViewById(R.id.tvWeight);
        tvWeight = (TextView) view.findViewById(R.id.tvWeight);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.test_list_view_frame);
        main = (LinearLayout) view.findViewById(R.id.main);
        tvbName1 = (TextView) view.findViewById(R.id.tvbName1);
        tvContent1 = (TextView) view.findViewById(R.id.tvContent1);
        tvbName2 = (TextView) view.findViewById(R.id.tvbName2);
        tvContent2 = (TextView) view.findViewById(R.id.tvContent2);
        tvbName3 = (TextView) view.findViewById(R.id.tvbName3);
        tvContent3 = (TextView) view.findViewById(R.id.tvContent3);
        tvbName4 = (TextView) view.findViewById(R.id.tvbName4);
        tvContent4 = (TextView) view.findViewById(R.id.tvContent4);
        tvbName5 = (TextView) view.findViewById(R.id.tvbName5);
        tvContent5 = (TextView) view.findViewById(R.id.tvContent5);
        listview = (ListView) view.findViewById(R.id.test_list_view);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Live live = (Live) adapter.getItem((position - 1));
                loadUrlAndShow(live);
            }
        });

        adapter = new LiveAdapter(getActivity());
        listview.setAdapter(adapter);

        //add head to listview
        View bannView = LayoutInflater.from(getActivity()).inflate(R.layout.live_banner, null, false);
        listview.addHeaderView(bannView);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (App.screenWidth * 0.28));
        bannerView = (BannerView) bannView.findViewById(R.id.banner);
        bannerView.setLayoutParams(layoutParams);
        rotRl = (RelativeLayout) view.findViewById(R.id.rotRl);

        llone_viewpager = (LinearLayout) view.findViewById(R.id.llone_viewpager);
        llone_viewpager.setVisibility(View.GONE);
//        mSwitchScrollLayout = (SwitchScrollLayout) view.findViewById(R.id.ScrollLayoutTest);
        llInfo = (LinearLayout) view.findViewById(R.id.llInfo);

        LinearLayout.LayoutParams layoutp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (App.screenWidth * 0.28));
        //llInfo.setLayoutParams(layoutp);
        flInfo = (FrameLayout) view.findViewById(R.id.flInfo);

        vpager = (ParallaxViewPager) view.findViewById(R.id.vpager);


    }


    private ViewPager.OnPageChangeListener listeners = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int currentIndex) {
//            if (currentIndex == listPager.lcontant.size() - 1) {
//                generatePageControl(listPager.lcontant.size(), currentIndex);
//            }
            tvInfo.startAnimation();
            tvInfo.setTextSize(16);
            Live live = listPager.lcontant.get(currentIndex).get(0);
            initViewInfo(live);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            if(arg0 == 1){
                if (isLoad) {
                    //起一个线程更新数据
                    SwitchPicThread switchPicThread = new SwitchPicThread();
                    new Thread(switchPicThread).start();
                }

            }
        }
    };



    private void loadUrlAndShow(final Live live) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("lid", live.getLid());
        RequestUtils.sendPostRequest(Api.PLAY_URL, params, new ResponseCallBack<PlayUrl>() {
            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }

            @Override
            public void onSuccess(PlayUrl data) {
                super.onSuccess(data);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("isPlay", "true");
                map.put("lid", live.getLid());
                map.put("playUrl", data.getUrl());
                ForwardUtils.target(getActivity(), Constant.LIVE, map);
            }

            @Override
            public void onSuccessList(List<PlayUrl> datas) {
                super.onSuccessList(datas);
                if (null != datas && datas.size() > 0) {
                    PlayUrl data = datas.get(0);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("isPlay", "true");
                    map.put("playUrl", data.getUrl());
                    map.put("lid", live.getLid());
                    ForwardUtils.target(getActivity(), Constant.LIVE, map);
                }
            }
        }, PlayUrl.class);
    }



    protected void initData() {


        initAdapter(EnumUpdateTag.UPDATE);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                currPage = 1;
                initAdapter(EnumUpdateTag.UPDATE);
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                currPage++;
                initAdapter(EnumUpdateTag.MORE);
            }

        });
        getBanner();
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

                        adapter.clear();
                        adapter.addList(data);
                    } else {
                        adapter.addList(data);
                    }
                    if (enumUpdateTag == EnumUpdateTag.UPDATE) {
                        if (isLoad) {
                            currentViewPage = 1;
                            //起一个线程更新数据
                            SwitchPicThread switchPicThread = new SwitchPicThread();
                            new Thread(switchPicThread).start();
                        }
                    }
                } else {
                    if (currPage == 1) {
                        CommonHelper.noData("暂无人气贡献记录哦", listview, getActivity());
                    } else {
                        //ptrClassicFrameLayout.setLoadMoreEnable(false);
                    }
                }
                ptrClassicFrameLayout.loadComplete(true);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                ptrClassicFrameLayout.loadComplete(false);
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


    @Override
    protected void onNoDoubleClick(View view) {

    }


    /**
     * Open Search
     */
    private void startSearch() {
        ForwardUtils.target(getActivity(), Constant.SEARCH, null);
    }

    private void getBanner() {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", "1");
        paramsMap.put("pagesize", "10");
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
                    ptrClassicFrameLayout.setVisibility(View.GONE);
                    llone_viewpager.setVisibility(View.VISIBLE);
//                    activity.hideTitle(false);
                    rotRl.setVisibility(View.GONE);
                } else if (intent.getStringExtra("type").toString().equals("1")) {
                    llone_viewpager.setVisibility(View.GONE);
                    ptrClassicFrameLayout.setVisibility(View.VISIBLE);
//                    activity.hideTitle(false);
                    rotRl.setVisibility(View.VISIBLE);
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
                        CommonHelper.showTip(getActivity(), "已经没有更多内容了");
                    } else {
                        if (currentViewPage == 1) {
                            if (null == listPager) {

                                listPager = new ListViewPagerAdapter(getActivity(), getContext(), data, 1);
                                vpager.setOnPageChangeListener(listeners);
                                vpager.setAdapter(listPager);
                                Live live = listPager.lcontant.get(0).get(0);
                                initViewInfo(live);

                            } else {
                                listPager.list.clear();
                                listPager.lcontant.clear();
                                listPager.mListViewPager.clear();
                                listPager = new ListViewPagerAdapter(getActivity(), getContext(), data, 1);
                                vpager.setAdapter(listPager);
                                vpager.setOnPageChangeListener(listeners);
                                Live live = listPager.lcontant.get(0).get(0);
                                initViewInfo(live);
                            }
                        } else {
                            if(vpager==null || listPager==null){
                                listPager = new ListViewPagerAdapter(getActivity(), getContext(), data, 1);
                                vpager.setAdapter(listPager);
                                vpager.setOnPageChangeListener(listeners);
                                Live live = listPager.lcontant.get(0).get(0);
                                initViewInfo(live);
                            }
                            else {
                                listPager.ListViewPagerAdapterLoadMore(getContext(), data, 1);
                                listPager.notifyDataSetChanged();
                            }
                        }

                        currentViewPage++;


                    }
                } else {

                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
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
                            if (i == 0) {
                                tvContent1.setText(message.getContent().toString());
                                tvbName1.setText(ext.getNickName() + ": ");
                            } else if (i == 1) {
                                tvContent2.setText(message.getContent().toString());
                                String name = ext.getNickName().toString() + ": ";
                                tvbName2.setText(name);
                            } else if (i == 2) {
                                tvContent3.setText(message.getContent().toString());
                                tvbName3.setText(ext.getNickName() + ": ");
                            } else if (i == 3) {
                                tvContent4.setText(message.getContent().toString());
                                tvbName4.setText(ext.getNickName() + ": ");
                            } else if (i == 4) {
                                tvContent5.setText(message.getContent().toString());
                                tvbName5.setText(ext.getNickName() + ": ");
                            }
                            i++;
                        }
                        if (i == 5) break;
                    }

                } else {
                    resetTextView();
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, HistoryMsg.class);
    }

    private void resetTextView() {
        tvbName1.setText("");
        tvContent1.setText("");
        tvbName2.setText("");
        tvContent2.setText("");
        tvbName3.setText("");
        tvContent3.setText("");
        tvbName4.setText("");
        tvContent4.setText("");
        tvbName5.setText("");
        tvContent5.setText("");
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

    private void generatePageControl(int count, int currentIndex) {
        if ((count - 1) == currentIndex) {
            if (isLoad) {
                isLoad = false;
                SwitchPicThread m = new SwitchPicThread();
                new Thread(m).start();
            }
        }
    }

    private void initViewInfo(Live live) {
        //CommonHelper.viewSetBackageImag(live.getPhoto(),llInfo);
        tvInfo.setTexts(live.getNickName());

        tvLoveCount.setText(live.getLoveCount());
        String hei = live.getHeight() == null ? "165 cm  " : live.getHeight() + "cm  ";
        String wei = live.getWeight() == null ? "4 5 kg  " : live.getWeight() + "kg  ";
        String bwh = live.getBwh() == null ? "    " : "三围:  " + live.getBwh();
        tvWeight.setText(hei + wei + bwh);
        tvAddress.setText(live.getCity() == null ? "" : live.getCity());
        loadMsg(live.getLid());
    }

    public AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            ForwardUtils.target(getActivity(), Constant.START_LIVE, null);
        }

    };


}
