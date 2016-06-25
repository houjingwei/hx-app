package com.huixiangtv.live.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.MainActivity;
import com.huixiangtv.live.adapter.LiveAdapter;
import com.huixiangtv.live.common.CommonUtil;
import com.huixiangtv.live.model.BannerModel;
import com.huixiangtv.live.model.HistoryMsg;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.model.PlayUrl;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CenterLoadingView;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.DepthPageTransformer;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.BannerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentTabOne extends Fragment {


    private LinearLayout main;
    private int page = 1;
    private final int pageSize = 5;
    private BannerView bannerView;
    private List<BannerModel> guangGao = new ArrayList<BannerModel>();
    private View mRootView;
    MainActivity activity;


    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private ListView listview;
    private LiveAdapter adapter;

    private LiveListBroadcast receiver;

    private String ACTION = "com.android.broadcast.RECEIVER_ACTION";


    private int bigPage = 1;


    private static final int BIG_UPDATE = -10;
    LinearLayout llTop;
    FrameLayout flBottom;
    private ViewPager mViewPager;
    private List<Live> liveList = new ArrayList<>();
    private List<LinearLayout> pagerViews = new ArrayList<LinearLayout>();
    private PagerAdapter pageAdapter;


    private boolean left = false;
    private boolean right = false;
    boolean knowBg2 = false;
    private boolean isChange = false;
    private int oldIndex = 0;
    private int lastValue = -1;
    private int currIndex = 0;


    ImageView bg2;
    ImageView bg3;


    private String newImage;


    CenterLoadingView loadingDialog = null;
    private boolean hasLoadBig = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tab_one, container, false);
        activity = (MainActivity) getActivity();
        receiver = new LiveListBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        //动态注册BroadcastReceiver
        getActivity().registerReceiver(receiver, filter);


        initLayout();
        initData();
        setGuide();


        return mRootView;
    }


    protected void initLayout() {
        ptrClassicFrameLayout = (PtrClassicFrameLayout) mRootView.findViewById(R.id.test_list_view_frame);
        main = (LinearLayout) mRootView.findViewById(R.id.main);
        listview = (ListView) mRootView.findViewById(R.id.test_list_view);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Live live = (Live) adapter.getItem((position - 1));
                toPlayActivity(live);
            }
        });

        //添加banner并设置banner高度
        View bannView = LayoutInflater.from(getActivity()).inflate(R.layout.live_banner, null, false);
        listview.addHeaderView(bannView);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (App.screenWidth * 0.28));
        bannerView = (BannerView) bannView.findViewById(R.id.banner);
        bannerView.setLayoutParams(layoutParams);


        //大图
        llTop = (LinearLayout) mRootView.findViewById(R.id.llTop);



        flBottom = (FrameLayout) mRootView.findViewById(R.id.flBottom);
        FrameLayout.LayoutParams llParams = (FrameLayout.LayoutParams)flBottom.getLayoutParams();
        llParams.height = (int) (App.screenHeight*0.32);
        llParams.width = App.screenWidth;
        flBottom.setLayoutParams(llParams);

        bg2 = (ImageView) mRootView.findViewById(R.id.iv2);
        bg3 = (ImageView) mRootView.findViewById(R.id.iv3);

    }


    /**
     * 查询播放信息，并跳转到播放页
     *
     * @param live
     */
    private void toPlayActivity(final Live live) {
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

    protected void setGuide() {
        CommonUtil.setGuidImage(getActivity(), R.id.main, R.drawable.index_up_down, "guide1", new ApiCallback() {

            @Override
            public void onSuccess(Object data) {

            }
        });
    }

    protected void initData() {
        getBanner();
        adapter = new LiveAdapter(getActivity());
        listview.setAdapter(adapter);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                loadData();
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                loadData();
            }

        });

        loadData();



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
     * 加载列表数据
     */
    private void loadData() {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", page + "");
        paramsMap.put("pageSize", pageSize + "");
        paramsMap.put("cNo", "TJ");


        RequestUtils.sendPostRequest(Api.LIVE_LIST, paramsMap, new ResponseCallBack<Live>() {
            @Override
            public void onSuccessList(List<Live> data) {
                if (data != null && data.size() > 0) {
                    if (page == 1) {
                        adapter.clear();
                        adapter.addList(data);
                    } else {
                        adapter.addList(data);
                    }

                } else {
                    if (page == 1) {
                        CommonHelper.noData("暂无人气贡献记录哦", listview, getActivity());
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
                    mRootView.findViewById(R.id.rotRl).setVisibility(View.GONE);
                    if(!hasLoadBig) {
                        loadBigViewData();
                    }

                } else if (intent.getStringExtra("type").toString().equals("1")) {
                    mRootView.findViewById(R.id.rotRl).setVisibility(View.VISIBLE);
                    ptrClassicFrameLayout.setVisibility(View.VISIBLE);


                }
            }

        }
    }

    /**
     * load  more data
     */
    private void loadBigViewData() {
        //put params
        if (null == loadingDialog) {
            loadingDialog = new CenterLoadingView(getActivity());
        }
        loadingDialog.setCancelable(true);
        loadingDialog.setTitle("正在连接");
        loadingDialog.show();
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", bigPage + "");
        paramsMap.put("pageSize", pageSize + "");
        paramsMap.put("cNo", "TJ");

        RequestUtils.sendPostRequest(Api.LIVE_LIST, paramsMap, new ResponseCallBack<Live>() {
            @Override
            public void onSuccessList(List<Live> data) {
                if (data != null && data.size() > 0) {
                    resetPageviewParams();
                    if(null!=pageAdapter){
                        liveList.clear();
                        pageAdapter = null;
                        mViewPager = null;
                        llTop.removeAllViews();
                    }
                    liveList = data;

                    android.os.Message msg = new android.os.Message();
                    msg.what = BIG_UPDATE;
                    msg.obj = msg;
                    myHandle.sendMessage(msg);
                }else{
                    bigPage--;
                    if (null != loadingDialog) {
                        loadingDialog.dismiss();
                    }

                }

            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);

            }
        }, Live.class);
    }


    private Handler myHandle = new Handler() {
        @Override
        public void handleMessage(android.os.Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case BIG_UPDATE:
                    hasLoadBig = true;
                    initPagerViewData();
                    break;

            }
        }
    };

    private void initPagerViewData() {

        mViewPager = new ViewPager(getActivity());
        llTop.addView(mViewPager);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mViewPager.getLayoutParams();
        params.height = App.screenHeight;
        params.width = App.screenWidth;
        mViewPager.setLayoutParams(params);

        final DepthPageTransformer mPageTransformer = new DepthPageTransformer();
        mViewPager.setPageTransformer(true, mPageTransformer);

        pageAdapter = new myAdapter();
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                int flag = ev.getAction();
                switch (flag) {
                    case MotionEvent.ACTION_DOWN:
                        oldIndex = mViewPager.getCurrentItem();

                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                }
                return false;
            }
        });

        mViewPager.addOnPageChangeListener(changeListener);
        initPagerData();
    }

    private void initPagerData() {


        String bg3ImagePath = "";
        if(null!=liveList && liveList.size()>0){
            bg3ImagePath = liveList.get(0).getBlur();
            bottomInfo(liveList.get(0));
        }
        bg2.setAlpha(0.0f);
        bg3.setAlpha(0.5f);
        ImageUtils.display(bg3, bg3ImagePath);
        if(null!=liveList && liveList.size()>0){
            for (int i=0;i<liveList.size();i++){
                Live live = liveList.get(i);
                LinearLayout view = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.view_pager_content_view, null, false);
                LinearLayout lLayout = (LinearLayout) view.findViewById(R.id.llRoot);
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                String img = live.getImg1();
                Log.i("myImagePath",img);
                ImageUtils.display(imageView,img);
                lLayout.addView(imageView);
                LinearLayout.LayoutParams imageParams = (LinearLayout.LayoutParams)imageView.getLayoutParams();
                imageParams.height = (int) (App.screenHeight*0.68);
                imageParams.width = App.screenWidth;
                imageView.setLayoutParams(imageParams);

                pagerViews.add(view);
            }
        }

        adapter.notifyDataSetChanged();



        if (null != loadingDialog) {
            loadingDialog.dismiss();
        }

    }

    /**
     * 设置底部的信息
     * @param live
     */
    private void bottomInfo(Live live) {
        Log.i("liveInfo",live.getNickName());
    }

    List<Integer> offsetList = new ArrayList<Integer>();
    List<Integer> oldIndexList = new ArrayList<Integer>();
    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.i("offset", positionOffsetPixels + "***" + position);

            offsetList.add(positionOffsetPixels);
            oldIndexList.add(position);
            if (offsetList.size() > 2) {
                int num1 = offsetList.get(0);
                int num2 = offsetList.get(1);

                if (isChange) {
                    if (knowBg2) {
                        if (num1 > num2) {
                            twoToLeftAlpha(positionOffset / 2);
                        } else if (num1 < num2) {
                            twoToRightAlpha(positionOffset / 2);
                        }
                    }
                } else {
                    if (knowBg2) {

                        if (num1 > num2) {

                            oneToLeftAlpha(positionOffset / 2);
                        } else if (num1 < num2) {

                            oneToRightAlpha(positionOffset / 2);
                        }
                    }
                }

            }


            if (offsetList.size() > 3) {
                if (!knowBg2) {
                    knowBg2 = true;
                    if (offsetList.get(offsetList.size() - 2) > offsetList.get(offsetList.size() - 3)) {
                        //向右
                        if(oldIndex<liveList.size()-1){
                            newImage = liveList.get(oldIndex+1).getBlur();
                        }else{
                            newImage = liveList.get(liveList.size()-1).getBlur();
                        }

                    } else if (offsetList.get(offsetList.size() - 2) < offsetList.get(offsetList.size() - 3)) {
                        //向左
                        if(oldIndex>0){
                            newImage = liveList.get(oldIndex-1).getBlur();
                        }else{
                            newImage = liveList.get(0).getBlur();
                        }
                    }
                    if (isChange) {
                        ImageUtils.display(bg3, newImage);
                    } else {

                        ImageUtils.display(bg2, newImage);
                    }

                }

                lastValue = offsetList.get(offsetList.size() - 2);
            } else {
                lastValue = -1;
            }
        }

        @Override
        public void onPageSelected(int position) {
            currIndex = position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

            if (state == 0) {
                knowBg2 = false;
                if (offsetList.size() > 3) {
                    if (offsetList.get(offsetList.size() - 2) > offsetList.get(offsetList.size() - 3)) {
                        right = true;
                        left = false;
                    } else if (offsetList.get(offsetList.size() - 2) < offsetList.get(offsetList.size() - 3)) {
                        right = false;
                        left = true;
                    } else if (currIndex == pageAdapter.getCount() - 1 && offsetList.get(offsetList.size() - 2) == 0) {
                        left = false;
                        right = true;
                    } else if (currIndex == 0 && offsetList.get(offsetList.size() - 2) == 0) {
                        left = true;
                        right = false;
                    }
                    lastValue = offsetList.get(offsetList.size() - 2);
                } else {
                    lastValue = -1;
                }
                if (lastValue == 0 && currIndex == pageAdapter.getCount() - 1 && right) {
                    toLoadNext();
                } else if (lastValue == 0 && currIndex == 0 && left) {
                    toRefresh();
                }
                offsetList.clear();

                if (oldIndex != currIndex) {
                    isChange = !isChange;
                    bottomInfo(liveList.get(currIndex));
                } else {
                    if (isChange) {
                        bg2.setAlpha(0.5f);
                        bg3.setAlpha(0.0f);
                    } else {
                        bg2.setAlpha(0.0f);
                        bg3.setAlpha(0.5f);
                    }
                }

            }
        }
    };

    private void toRefresh() {
        bigPage=1;
        loadBigViewData();
    }

    private void toLoadNext() {
        bigPage ++;
        loadBigViewData();
    }

    private void resetPageviewParams(){
        pagerViews.clear();
        left = false;
        right = false;
        knowBg2 = false;
        isChange = false;
        oldIndex = 0;
        lastValue = -1;
        currIndex = 0;
    }


    private void oneToRightAlpha(float positionOffset) {
        if (positionOffset != 0.0) {
            float ff = formartAlpha(positionOffset);
            bg2.setAlpha(ff);
            bg3.setAlpha(0.5f - ff);
        }
    }

    private void twoToRightAlpha(float positionOffset) {
        if (positionOffset != 0.0) {
            float ff = formartAlpha(positionOffset);
            Log.i("bg2 alpha 减至 ", (0.5f - ff) + "***");
            bg2.setAlpha(0.5f - ff);
            bg3.setAlpha(ff);
        }
    }

    private void oneToLeftAlpha(float positionOffset) {
        if (positionOffset != 0.0) {
            float ff = formartAlpha(positionOffset);
            bg2.setAlpha(0.5f - ff);
            bg3.setAlpha(ff);
        }

    }


    private void twoToLeftAlpha(float positionOffset) {
        if (positionOffset != 0.0) {
            float ff = formartAlpha(positionOffset);
            bg2.setAlpha(ff);
            bg3.setAlpha(0.5f - ff);
        }

    }

    private float formartAlpha(float positionOffset) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(positionOffset);//format 返回的是字符串
        return Float.parseFloat(p);
    }


    public class myAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pagerViews.get(position));
            return pagerViews.get(position);
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object obj) {

            ((ViewPager) container).removeView((LinearLayout) obj);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return liveList.size();
        }
    }


    private void loadMsg(String lid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("chatroom", lid);
        RequestUtils.sendPostRequest(Api.HISTORY_MSG, params, new ResponseCallBack<HistoryMsg>() {
            @Override
            public void onSuccess(HistoryMsg data) {
                super.onSuccess(data);

            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, HistoryMsg.class);
    }

}

