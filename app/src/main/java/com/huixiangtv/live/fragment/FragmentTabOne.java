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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.MainActivity;
import com.huixiangtv.live.adapter.LiveAdapter;
import com.huixiangtv.live.model.BannerModel;
import com.huixiangtv.live.model.ChatMessage;
import com.huixiangtv.live.model.HistoryMsg;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.model.MsgExt;
import com.huixiangtv.live.model.PlayUrl;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CenterLoadingView;
import com.huixiangtv.live.ui.HuixiangLoadingLayout;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.DepthPageTransformer;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.BannerView;
import com.huixiangtv.live.utils.widget.WidgetUtil;

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


    private PullToRefreshListView refreshView;
    private LiveAdapter adapter;

    private LiveListBroadcast receiver;

    private String ACTION = "com.android.broadcast.RECEIVER_ACTION";


    private int bigPage = 1;


    private static final int BIG_UPDATE = -10;
    LinearLayout llTop;
    ImageView ivPlay;
    FrameLayout flBottom;
    LinearLayout llLiveBottom;
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
    LinearLayout llMsg;


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

        setIndexStyle();

        return mRootView;
    }

    private void setIndexStyle() {
        if(null != App.getPreferencesValue("indexStyle") && App.getPreferencesValue("indexStyle").equals("1"))
        {

            refreshView.setVisibility(View.GONE);
            mRootView.findViewById(R.id.rotRl).setVisibility(View.GONE);
            if (!hasLoadBig) {
                loadBigViewData();
            }

        }

    }


    protected void initLayout() {
        refreshView = (PullToRefreshListView) mRootView.findViewById(R.id.refreshView);
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshView.setHeaderLayout(new HuixiangLoadingLayout(getActivity()));
        // 使用第二底部加载布局,要先禁止掉包含（Mode.PULL_FROM_END）的模式
        // 如修改（Mode.BOTH为Mode.PULL_FROM_START）
        // 修改（Mode.PULL_FROM_END 为Mode.DISABLE）
        refreshView.setFooterLayout(new HuixiangLoadingLayout(getActivity()));
        main = (LinearLayout) mRootView.findViewById(R.id.main);
        refreshView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Live live = (Live) adapter.getItem((position - 2));
                toPlayActivity(live);
            }
        });

        //添加banner并设置banner高度
        View bannView = LayoutInflater.from(getActivity()).inflate(R.layout.live_banner, null, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (App.screenWidth * 0.28));
        bannerView = (BannerView) bannView.findViewById(R.id.banner);
        bannerView.setLayoutParams(layoutParams);
        refreshView.getRefreshableView().addHeaderView(bannView);

        //大图
        llTop = (LinearLayout) mRootView.findViewById(R.id.llTop);
        ivPlay = (ImageView) mRootView.findViewById(R.id.ivPlay);
        playBtnLayout();
        //点击播放按钮
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPlay();
            }
        });


        flBottom = (FrameLayout) mRootView.findViewById(R.id.flBottom);
        llLiveBottom = (LinearLayout) mRootView.findViewById(R.id.llLiveBottom);
        FrameLayout.LayoutParams llParams = (FrameLayout.LayoutParams) flBottom.getLayoutParams();
        llParams.height = (int) (App.screenHeight * 0.32);
        llParams.width = App.screenWidth;
        flBottom.setLayoutParams(llParams);

        llMsg = (LinearLayout) mRootView.findViewById(R.id.llMsg);

        bg2 = (ImageView) mRootView.findViewById(R.id.iv2);
        bg3 = (ImageView) mRootView.findViewById(R.id.iv3);

    }

    private void toPlay() {
        if(null!=liveList && liveList.size()>0){
            Live live = liveList.get(currIndex);
            toPlayActivity(live);
        }
    }

    /**
     * 设置播放按钮的位置
     */
    private void playBtnLayout() {
        int topHeigth = (int) (App.screenHeight*0.68);
        int ivPlayX = (int) ((App.screenWidth- WidgetUtil.dip2px(getActivity(),50))*0.5);
        int ivPlayY = (int)((topHeigth-WidgetUtil.dip2px(getActivity(),50))*0.5);
        FrameLayout.LayoutParams marginParams=new FrameLayout.LayoutParams(ivPlay.getLayoutParams());
        marginParams.leftMargin = ivPlayX;
        marginParams.topMargin = ivPlayY;
        ivPlay.setLayoutParams(marginParams);
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


    protected void initData() {
        getBanner();
        adapter = new LiveAdapter(getActivity());
        refreshView.setAdapter(adapter);
        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page=1;
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
                        CommonHelper.noData("暂无直播", refreshView.getRefreshableView(), getActivity(),2);
                    }
                }
                refreshView.onRefreshComplete();
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                refreshView.onRefreshComplete();
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
                        refreshView.setVisibility(View.GONE);
                        mRootView.findViewById(R.id.rotRl).setVisibility(View.GONE);
                        if (!hasLoadBig) {
                            loadBigViewData();
                        }

                    } else if (intent.getStringExtra("type").toString().equals("1")) {
                        mRootView.findViewById(R.id.rotRl).setVisibility(View.VISIBLE);
                        refreshView.setVisibility(View.VISIBLE);

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
                    if (null != pageAdapter) {
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
                } else {
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
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewPager.getLayoutParams();
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
        if (null != liveList && liveList.size() > 0) {
            bg3ImagePath = liveList.get(0).getBlur();
            bottomInfo(liveList.get(0));
        }
        bg2.setAlpha(0.0f);
        bg3.setAlpha(0.5f);
        ImageUtils.display(bg3, bg3ImagePath);
        if (null != liveList && liveList.size() > 0) {
            for (int i = 0; i < liveList.size(); i++) {
                Live live = liveList.get(i);
                LinearLayout view = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.view_pager_content_view, null, false);
                LinearLayout lLayout = (LinearLayout) view.findViewById(R.id.llRoot);
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                String img = live.getImg1();
                Log.i("myImagePath", img);
                ImageUtils.display(imageView, img);
                lLayout.addView(imageView);
                LinearLayout.LayoutParams imageParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                imageParams.height = (int) (App.screenHeight * 0.68);
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


    List<Integer> offsetList = new ArrayList<Integer>();
    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.i("offset", positionOffsetPixels + "***" + position);

            offsetList.add(positionOffsetPixels);

            if (offsetList.size() > 2) {
                int num1 = offsetList.get(0);
                int num2 = offsetList.get(1);

                if (isChange) {
                    if (knowBg2) {
                        if (num1 > num2) {
                            twoToLeftAlpha(positionOffset / 2);
                            ivPlayAlpha(positionOffset, 1);
                        } else if (num1 < num2) {
                            twoToRightAlpha(positionOffset / 2);
                            ivPlayAlpha(positionOffset, 2);
                        }
                    }
                } else {
                    if (knowBg2) {
                        if (num1 > num2) {
                            oneToLeftAlpha(positionOffset / 2);
                            ivPlayAlpha(positionOffset, 1);
                        } else if (num1 < num2) {
                            oneToRightAlpha(positionOffset / 2);
                            ivPlayAlpha(positionOffset, 2);
                        }
                    }
                }

            }


            if (offsetList.size() > 3) {
                if (!knowBg2) {
                    knowBg2 = true;
                    if (offsetList.get(offsetList.size() - 2) > offsetList.get(offsetList.size() - 3)) {
                        //向右
                        if (oldIndex < liveList.size() - 1) {
                            newImage = liveList.get(oldIndex + 1).getBlur();
                        } else {
                            newImage = liveList.get(liveList.size() - 1).getBlur();
                        }

                    } else if (offsetList.get(offsetList.size() - 2) < offsetList.get(offsetList.size() - 3)) {
                        //向左
                        if (oldIndex > 0) {
                            newImage = liveList.get(oldIndex - 1).getBlur();
                        } else {
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


    /**
     * 根据偏移量改变播放按钮的透明度
     * @param offset
     * @param offset
     */
    private void ivPlayAlpha(float offset, int flag) {
        float def = offset*2;
        if(flag==1){
            //向左，降低
            if(def<1){
                ivPlay.setAlpha(1-def);
            }else{
                ivPlay.setAlpha(def-1);
            }
        }else if(flag==2){
            //向右，升高
            if(def>1){
                ivPlay.setAlpha(def-1);
            }else{
                ivPlay.setAlpha(1-def);
            }
        }

    }

    private void toRefresh() {
        bigPage = 1;
        loadBigViewData();
    }

    private void toLoadNext() {
        bigPage++;
        loadBigViewData();
    }

    private void resetPageviewParams() {
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


    /**
     * 设置底部的信息
     *
     * @param live
     */
    private void bottomInfo(Live live) {

        llMsg.removeAllViews();
        TextView tvName = (TextView) mRootView.findViewById(R.id.tvName);
        TextView tvAddress = (TextView) mRootView.findViewById(R.id.tvAddress);
        TextView tvLove = (TextView) mRootView.findViewById(R.id.tvLove);
        TextView tvSw = (TextView) mRootView.findViewById(R.id.tvSw);
        tvName.setText(live.getNickName());
        tvAddress.setText(live.getCity());
        tvLove.setText(live.getLoveCount());


        String hei = live.getHeight() == null ? "165 cm  " : live.getHeight() + "cm  ";
        String wei = live.getWeight() == null ? "4 5 kg  " : live.getWeight() + "kg  ";
        String bwh = live.getBwh() == null ? "    " : "三围:  " + live.getBwh();
        tvSw.setText(hei + wei + bwh);

        loadMsg(live);
    }

    private void loadMsg(Live live) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("chatroom", live.getUid());
        RequestUtils.sendPostRequest(Api.HISTORY_MSG, params, new ResponseCallBack<HistoryMsg>() {
            @Override
            public void onSuccess(HistoryMsg data) {
                super.onSuccess(data);
                if (null != data) {
                    int i = 0;
                    for (ChatMessage msg : data.getLastMsg()) {
                        MsgExt ext = msg.getExt();
                        LinearLayout ll = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.live_bottom_info_msg_item, null, false);
                        TextView tvMsg = (TextView) ll.findViewById(R.id.tvMsg);
                        String s = ext.getNickName() + ": " + msg.getContent();
                        if (s.length() > 25) {
                            s = s.substring(0, 25) + "...";
                        }
                        SpannableString ss = new SpannableString(s);
                        ss.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.yellow)), 0, ext.getNickName().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvMsg.setText(ss);
                        llMsg.addView(ll);
                        i++;
                        if (i == 5) {
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, HistoryMsg.class);
    }

}

