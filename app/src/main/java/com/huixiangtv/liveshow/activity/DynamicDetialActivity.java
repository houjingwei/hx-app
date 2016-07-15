package com.huixiangtv.liveshow.activity;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.DynamicCommentAdapter;
import com.huixiangtv.liveshow.adapter.PraiseAdapter;
import com.huixiangtv.liveshow.model.Dynamic;
import com.huixiangtv.liveshow.model.DynamicComment;
import com.huixiangtv.liveshow.model.DynamicImage;
import com.huixiangtv.liveshow.model.DynamicpPraise;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.service.LoginCallBack;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CenterLoadingView;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.ui.HuixiangLoadingLayout;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.DateUtils;
import com.huixiangtv.liveshow.utils.ForwardUtils;
import com.huixiangtv.liveshow.utils.KeyBoardUtils;
import com.huixiangtv.liveshow.utils.StringUtil;
import com.huixiangtv.liveshow.utils.image.ImageUtils;
import com.huixiangtv.liveshow.utils.widget.ActionItem;
import com.huixiangtv.liveshow.utils.widget.TitlePopup;
import com.huixiangtv.liveshow.utils.widget.WidgetUtil;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview2;

public class DynamicDetialActivity extends BaseBackActivity {

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.ffRoot)
    FrameLayout ffRoot;

    private PullToRefreshListView refreshView;
    int page = 1;
    DynamicCommentAdapter adapter;


    LinearLayout llCommentView;
    EditText etComment;

    String did;
    Dynamic dn;
    ArrayList<String> showImgList = new ArrayList<String>();

    int imgTotalWidth = 0;

    int videoWidth = 0;
    int videoHeight = 0;

    PraiseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detial);
        x.view().inject(this);

        did = getIntent().getStringExtra("did");

        if (StringUtil.isNotNull(did)) {
            intitView();

        } else {
            onBackPressed();
        }
    }


    private void upViewData(Dynamic data) {

        dn = data;

        //设置动态删除按钮是否显示
        if(null!=App.getLoginUser() && App.getLoginUser().getUid().equals(dn.getUid())){
            commonTitle.saveShow(View.VISIBLE);
        }

        //所有大图图片
        if(null!=data.getImages()){
            for (DynamicImage img :data.getImages()) {
                showImgList.add(img.getBig());
            }
        }
        

        
        ImageUtils.display(ivPhoto,data.getPhoto());
        tvName.setText(data.getNickName());
        tvContent.setText(data.getContent());
        tvTime.setText(DateUtils.formatDisplayTime(data.getDate(),"yyyy-MM-dd HH:mm:ss"));

        //动态计算图片行列
        if(data.getType().equals("1")){
            int imgCount = data.getImages().size();
            //行
            int rowNum = 0;
            //列
            int consNum = 0;

            if(imgCount==1){
                rowNum = 1;
                consNum = 1;
            }else if(imgCount==2){
                rowNum = 1;
                consNum = 2;
            }else if(imgCount==3){
                rowNum = 1;
                consNum = 3;
            }else if(imgCount>3){
                if(imgCount%3==0){
                    rowNum = imgCount/3;
                }else{
                    rowNum = imgCount/3+1;
                }
                consNum = 3;
            }

            addImgAndShow(data.getImages(),rowNum,consNum);
        }

        //动态添加视频
        if(data.getType().equals("2")){
            videoWidth = App.screenWidth - WidgetUtil.dip2px(DynamicDetialActivity.this,80);
            if(StringUtil.isNotNull(data.getRate())){
                videoHeight = (int) (videoWidth/Float.parseFloat(data.getRate()));
                Log.i("videoHeight",videoWidth+"*******"+videoHeight);
            }else{
                videoHeight = (int) (videoWidth*0.75);
            }

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)rlVideo.getLayoutParams();
            params.width = videoWidth;
            params.height = videoHeight;
            rlVideo.setLayoutParams(params);
            ImageUtils.display(ivVideo,dn.getVideoCover());
            if(StringUtil.isNotNull(playUrl)){
                toPlay();
            }else{
                loadPlayUrlAndPlay();
            }
        }

        //赞数据
        mAdapter = new PraiseAdapter(null);
        mRecylerView.setAdapter(mAdapter);
        if(null!=data.getPraises() && data.getPraises().size()>0){
            rlZan.setVisibility(View.VISIBLE);
            mAdapter.addAll(data.getPraises());
            for (DynamicpPraise dynamicpPraise : dn.getPraises()) {
                if (dynamicpPraise.getUid().equals(App.getLoginUser().getUid())) {
                    dn.setIsZan(true);
                }
            }

        }
    }


    //每一行的图片集合
    List<DynamicImage> imgList = new ArrayList<>();
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void addImgAndShow(List<DynamicImage> images, int rowNum, int consNum) {
        imgTotalWidth = App.screenWidth - WidgetUtil.dip2px(DynamicDetialActivity.this,80);

        LinearLayout.LayoutParams llViewParams = new LinearLayout.LayoutParams(imgTotalWidth,imgTotalWidth/3);
        llViewParams.setLayoutDirection(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams photoParams = null;

        if(rowNum==1 && consNum==1){
            //设置图片1张时llImgRoot子布局宽度
            LinearLayout ll1 = new LinearLayout(DynamicDetialActivity.this);
            if(StringUtil.isNotNull(images.get(0).getRate())){
                llViewParams.height = (int) (imgTotalWidth/Float.parseFloat(images.get(0).getRate()));
            }else{
                llViewParams.height = (int) (imgTotalWidth*0.75);
            }

            ll1.setLayoutParams(llViewParams);
            llImgRoot.addView(ll1);


            //设置图片1张时图片的布局宽度
            photoParams = new LinearLayout.LayoutParams(imgTotalWidth, imgTotalWidth/2);
            if(StringUtil.isNotNull(images.get(0).getRate())){
                photoParams.height = (int) (imgTotalWidth/Float.parseFloat(images.get(0).getRate()));
            }else{
                photoParams.height = (int) (imgTotalWidth*0.75);
            }
            addOneImgToLl(images, photoParams, ll1,0);

        }else if(rowNum==1 && consNum==2){
            //设置图片2张时llImgRoot子布局宽度
            llViewParams.height = imgTotalWidth/2;
            LinearLayout ll1 = new LinearLayout(DynamicDetialActivity.this);
            ll1.setLayoutParams(llViewParams);
            llImgRoot.addView(ll1);


            //设置图片2张时图片的布局宽度
            int width = (int) (imgTotalWidth*0.5-WidgetUtil.dip2px(DynamicDetialActivity.this,4));
            photoParams = new LinearLayout.LayoutParams(width,width);
            photoParams.rightMargin = WidgetUtil.dip2px(DynamicDetialActivity.this,2);

            addOneImgToLl(images, photoParams, ll1,0);
            addOneImgToLl(images, photoParams, ll1,1);

        }else if(rowNum==1 && consNum==3){
            int width = imgTotalWidth/3-WidgetUtil.dip2px(DynamicDetialActivity.this,6);
            photoParams = new LinearLayout.LayoutParams(width,width);
            photoParams.rightMargin = WidgetUtil.dip2px(DynamicDetialActivity.this,2);

            addllToRootLl(0,images, llViewParams, photoParams);


        }else{

            int width = imgTotalWidth/3-WidgetUtil.dip2px(DynamicDetialActivity.this,6);
            photoParams = new LinearLayout.LayoutParams(width,width);
            photoParams.rightMargin = WidgetUtil.dip2px(DynamicDetialActivity.this,2);
            llViewParams.height = imgTotalWidth/3-WidgetUtil.dip2px(DynamicDetialActivity.this,4);

            addllToRootLl(0,images, llViewParams, photoParams);
            if(rowNum==2){
                addllToRootLl(1,images, llViewParams, photoParams);
            }else{

                addllToRootLl(1,images, llViewParams, photoParams);
                addllToRootLl(2,images, llViewParams, photoParams);


            }

        }
    }


    /**
     * 只有一张图或两张图添加图片
     * @param images
     * @param params
     * @param ll
     * @param position
     */
    private void addOneImgToLl(List<DynamicImage> images, LinearLayout.LayoutParams params, LinearLayout ll, final int position) {
        ImageView imageView = (ImageView) LayoutInflater.from(DynamicDetialActivity.this).inflate(R.layout.activity_dynamic_image, null, false);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewImg(position);
            }
        });
        ll.addView(imageView,params);
        ImageUtils.display(imageView,images.get(position).getBig());
    }


    /**
     * 图片大于或等于3张时，动态给图片添加一个横向的布局
     * @param startIndex
     * @param images
     * @param rootViewParams
     * @param params
     */
    private void addllToRootLl(int startIndex,List<DynamicImage> images, LinearLayout.LayoutParams rootViewParams, LinearLayout.LayoutParams params) {
        imgList = getCurrLineImgs(startIndex, images);
        LinearLayout ll1 = new LinearLayout(DynamicDetialActivity.this);
        ll1.setLayoutParams(rootViewParams);
        llImgRoot.addView(ll1);
        addllToRootLlAndShow(params, ll1,startIndex);
    }


    /**
     * 每一行图片布局添加到父布局，并且显示每一行的图片
     * @param params
     * @param ll
     * @param startIndex
     */
    private void addllToRootLlAndShow(LinearLayout.LayoutParams params, LinearLayout ll,int startIndex) {
        int imgPosition = startIndex*3;
        for (DynamicImage img: imgList) {
            final int currPosition = imgPosition;
            ImageView imageView = (ImageView) LayoutInflater.from(DynamicDetialActivity.this).inflate(R.layout.activity_dynamic_image, null, false);
            ll.addView(imageView,params);
            ImageUtils.display(imageView,img.getSmall());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    previewImg(currPosition);
                }
            });
            imgPosition++;
        }
    }


    /**
     * 图片预览
     *
     * @param curPosition
     */
    private void previewImg(int curPosition) {
        PhotoPreview2.builder().setPhotos(showImgList).setCurrentItem(curPosition).setShowDeleteButton(false).start(this, PhotoPicker.REQUEST_CODE);
    }


    /**
     * 截取每一行的图片集合
     * @param num
     * @param photos
     * @return
     */
    private List<DynamicImage> getCurrLineImgs(int num, List photos) {
        List<DynamicImage> list = null;
        int min = num * 3;
        int max = (num + 1) * 3;
        if (photos.size() > max) {
            list = photos.subList(min, max);
        } else {
            list = photos.subList(min, photos.size());
        }
        return list;

    }



    /**
     * view设置
     */
    private void intitView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.dynamic_detial));
        commonTitle.setSaveText("删除");
        commonTitle.getSave().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDynamic();
            }
        });

        llCommentView = (LinearLayout) findViewById(R.id.llCommentView);
        etComment = (EditText) findViewById(R.id.etComment);
        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== 4 )
                {
                    KeyBoardUtils.closeKeybord(etComment,DynamicDetialActivity.this);
                    reComment();
                }
                return false;
            }
        });


        ffRoot.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        ffRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHideKeyboard();
            }
        });
        adapter = new DynamicCommentAdapter(this);
        refreshView = (PullToRefreshListView) findViewById(R.id.refreshView);
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshView.setHeaderLayout(new HuixiangLoadingLayout(this));
        refreshView.setFooterLayout(new HuixiangLoadingLayout(this));
        refreshView.setAdapter(adapter);
        refreshView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onHideKeyboard();

            }
        });
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHideKeyboard();
            }
        });
        addHeadView();
        loadDetialData();
        loadData();

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
    }


    CenterLoadingView loadingDialog = null;
    private void deleteDynamic() {
        onHideKeyboard();
        final MaterialDialog mMaterialDialog = new MaterialDialog(this);
        mMaterialDialog.setPositiveButton("删除", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        doDeleteDynamic();
                    }
                })
                .setNegativeButton("放弃", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.setTitle("回想提示");
        mMaterialDialog.setMessage("确定要删除动态吗");
        mMaterialDialog.show();
    }

    private void doDeleteDynamic() {
        if(null!=dn){
            if(null!=loadingDialog){
                loadingDialog = null;
            }
            loadingDialog = new CenterLoadingView(DynamicDetialActivity.this);
            loadingDialog.setCancelable(true);
            loadingDialog.setTitle("正在删除");
            loadingDialog.show();
            Map<String,String> params = new HashMap<>();
            params.put("dynamicId",dn.getDynamicId());

            RequestUtils.sendPostRequest(Api.DELETE_DYNAMIC, params, new ResponseCallBack<String>() {
                @Override
                public void onSuccess(String data) {
                    super.onSuccess(data);
                    App.refreshMyCircleActivity = true;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if(null!=loadingDialog){
                                loadingDialog.dismiss();
                                CommonHelper.showTip(DynamicDetialActivity.this,"动态删除成功");
                            }
                        }
                    }, 800);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            onBackPressed();
                        }
                    }, 1000);
                }

                @Override
                public void onFailure(ServiceException e) {
                    super.onFailure(e);
                    if(null!=loadingDialog){ CommonHelper.showTip(DynamicDetialActivity.this,e.getMessage());
                        loadingDialog.dismiss();
                    }

                }
            },String.class);
        }else{
            CommonHelper.showTip(DynamicDetialActivity.this,"动态删除异常");
        }
    }


    // 软键盘的高度
    private int keyboardHeight = 0;
    // 软键盘的显示状态
    private boolean isShowKeyboard = false;
    // 状态栏的高度
    private int statusBarHeight = App.statuBarHeight;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            // 应用可以显示的区域。此处包括应用占用的区域，
            // 以及ActionBar和状态栏，但不含设备底部的虚拟按键。
            Rect r = new Rect();
            ffRoot.getWindowVisibleDisplayFrame(r);

            // 屏幕高度。这个高度不含虚拟按键的高度
            int screenHeight = ffRoot.getRootView().getHeight();

            int heightDiff = screenHeight - (r.bottom - r.top);

            // 在不显示软键盘时，heightDiff等于状态栏的高度
            // 在显示软键盘时，heightDiff会变大，等于软键盘加状态栏的高度。
            // 所以heightDiff大于状态栏高度时表示软键盘出现了，
            // 这时可算出软键盘的高度，即heightDiff减去状态栏的高度
            if (keyboardHeight == 0 && heightDiff > statusBarHeight) {
                keyboardHeight = heightDiff - statusBarHeight;
            }

            if (isShowKeyboard) {
                // 如果软键盘是弹出的状态，并且heightDiff小于等于状态栏高度，
                // 说明这时软键盘已经收起
                if (heightDiff <= statusBarHeight) {
                    isShowKeyboard = false;
                    onHideKeyboard();
                }
            } else {
                // 如果软键盘是收起的状态，并且heightDiff大于状态栏高度，
                // 说明这时软键盘已经弹出
                if (heightDiff > statusBarHeight) {
                    isShowKeyboard = true;
                    onShowKeyboard();
                }
            }
        }
    };





    private void onShowKeyboard() {
        llCommentView.setVisibility(View.VISIBLE);
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(llCommentView.getLayoutParams());
        //左上右下
        Log.i("rinima", App.screenHeight - keyboardHeight+ "");
        int topY = App.screenHeight - keyboardHeight - WidgetUtil.dip2px(DynamicDetialActivity.this,50) - statusBarHeight;
        margin.setMargins(0, topY, 0, 0);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        llCommentView.setLayoutParams(layoutParams);
        etComment.requestFocus();
    }


    private void onHideKeyboard() {
        KeyBoardUtils.closeKeybord(etComment,DynamicDetialActivity.this);
        llCommentView.setVisibility(View.GONE);

    }


    /**
     * 显示聊天区域
     */
    private void showChatInputView() {

        if (null != App.getLoginUser()) {
            KeyBoardUtils.openKeybord(etComment, DynamicDetialActivity.this);
        }else{
            CommonHelper.showLoginPopWindow(DynamicDetialActivity.this, R.id.ffRoot, new LoginCallBack() {
                @Override
                public void loginSuccess() {
                    KeyBoardUtils.openKeybord(etComment, DynamicDetialActivity.this);
                }
            });
        }
    }



    private void loadDetialData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("did", did);
        RequestUtils.sendPostRequest(Api.DYNAMIC_DETIAL, map, new ResponseCallBack<Dynamic>() {
            @Override
            public void onSuccess(Dynamic data) {
                super.onSuccess(data);
                upViewData(data);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);

            }
        }, Dynamic.class);
    }

    private void loadData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("dynamicId", did);
        map.put("page", page+"");
        map.put("pageSize", Constant.PAGE_SIZE);
        RequestUtils.sendPostRequest(Api.DYNAMIC_COMMENT, map, new ResponseCallBack<DynamicComment>() {
            @Override
            public void onSuccessList(List<DynamicComment> data) {
                super.onSuccessList(data);
                if (data != null && data.size() > 0) {
                    if(page==1){
                        data.get(0).setShowIcon(true);
                        adapter.clear();
                    }
                    adapter.addList(data);
                }
                refreshView.onRefreshComplete();
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);

            }
        }, DynamicComment.class);
    }


    ImageView ivPhoto;
    TextView tvName;
    TextView tvContent;
    TextView tvTime;
    RecyclerView mRecylerView;
    LinearLayout llImgRoot;
    RelativeLayout rlVideo;
    ImageView ivVideo;
    ImageView ivPlay;
    LinearLayout llVideoView;
    RelativeLayout rlPlay;
    RelativeLayout rlZan;

    ImageView ivZanAndComm;
    boolean isPlay = false;
    private void addHeadView() {
        View view = LayoutInflater.from(DynamicDetialActivity.this).inflate(R.layout.activity_dynamic_detial_head, null, false);
        refreshView.getRefreshableView().addHeaderView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHideKeyboard();
            }
        });
        ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        rlZan = (RelativeLayout) view.findViewById(R.id.rlZan);
        mRecylerView = (RecyclerView) view.findViewById(R.id.mRecylerView);
        mRecylerView.setHasFixedSize(true);
        mRecylerView.setLayoutManager(new LinearLayoutManager(DynamicDetialActivity.this, LinearLayoutManager.HORIZONTAL, false));

        llImgRoot = (LinearLayout) view.findViewById(R.id.llImgRoot);
        ivZanAndComm = (ImageView) view.findViewById(R.id.ivZanAndComm);
        ivZanAndComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showZanAndCommPop(view);
            }
        });



        rlVideo = (RelativeLayout) view.findViewById(R.id.rlVideo);
        llVideoView = (LinearLayout) view.findViewById(R.id.llVideoView);
        ivVideo= (ImageView) view.findViewById(R.id.ivVideo);
        ivPlay= (ImageView) view.findViewById(R.id.ivPlay);
        ivPlay.setVisibility(View.GONE);
        rlPlay = (RelativeLayout) view.findViewById(R.id.rlPlay);
        rlPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHideKeyboard();
                if(!isPlay){
                    if(StringUtil.isNotNull(playUrl)){
                        play();
                    }else{
                        loadPlayUrlAndPlay();
                    }
                }else{
                    toPause();
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(null!=mVideoView){
            mVideoView.release();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(null!=mVideoView && StringUtil.isNotEmpty(playUrl)){
            mVideoView.start();

        }
    }

    /**
     * 获取点播视频地址并播放
     */
    private void loadPlayUrlAndPlay() {
        rlVideo.setVisibility(View.VISIBLE);
        Map<String,String> params = new HashMap<String,String>();
        params.put("key",dn.getVideoURL());
        params.put("type","1");
        CommonHelper.videoPlayUrl(params,new ApiCallback<String>(){

            @Override
            public void onSuccess(String data) {
                playUrl = data;
                toPlay();
            }
        });
    }

    int currPosition = 0;
    private void toPause() {
        ivPlay.setVisibility(View.VISIBLE);
        isPlay = false;
        mVideoView.pause();
        currPosition = mVideoView.getCurrentPosition();

    }

    private void play() {
        if(null!=mVideoView){
            if(mVideoView.isPlaying()){
                mVideoView.seekTo(currPosition);
            }else{
                mVideoView.start();
            }

            isPlay = true;
            ivPlay.setVisibility(View.GONE);
        }
    }

    ScalableVideoView mVideoView;
    private String playUrl = "";
    private void toPlay() {
        try{
            mVideoView = new ScalableVideoView(DynamicDetialActivity.this);
            llVideoView.addView(mVideoView);
            mVideoView.setDataSource(playUrl);
            //mVideoView.setVolume(50, 100);
            mVideoView.setLooping(true);
            mVideoView.prepareAsync(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mVideoView.setScalableType(ScalableType.CENTER_CROP);
                    mVideoView.start();
                    isPlay = true;
                    ivPlay.setVisibility(View.GONE);
                }
            });
            mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                    return false;
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private void showZanAndCommPop(View v) {
        onHideKeyboard();
        if(null!=dn){
            final TitlePopup titlePopup = new TitlePopup(DynamicDetialActivity.this, CommonHelper.dip2px(DynamicDetialActivity.this, 110), CommonHelper.dip2px(DynamicDetialActivity.this, 30));
            titlePopup.addAction(new ActionItem(DynamicDetialActivity.this, "评论", dn.getDynamicId(), R.mipmap.v2_dynamic_zan));
            if(!dn.isZan()){
                titlePopup.setComment("赞");
                titlePopup.addAction(new ActionItem(DynamicDetialActivity.this, "赞", dn.getDynamicId(),R.mipmap.v2_dynamic_comm));
            }else{
                titlePopup.setComment("取消");
                titlePopup.addAction(new ActionItem(DynamicDetialActivity.this, "取消", dn.getDynamicId(),R.mipmap.v2_dynamic_comm));
            }


            titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
                @Override
                public void onItemClick(ActionItem item, int position) {
                    if (item.mTitle.equals("赞")) {
                        Log.i("popup","赞");
                        titlePopup.setComment("取消");
                        zan();
                    }
                    if (item.mTitle.equals("取消")) {
                        titlePopup.setComment("赞");
                        cancelZan();
                    }
                    if (item.mTitle.equals("评论")) {
                        Log.i("popup","评论");
                        showChatInputView();
                    }
                }
            });

            titlePopup.setAnimationStyle(R.style.cricleBottomAnimation);
            titlePopup.show(v);
        }

    }

    private void cancelZan() {
        if(null==App.getLoginUser()){
            ForwardUtils.target(DynamicDetialActivity.this, Constant.LOGIN, null);
            return;
        }
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("dynamicId", did);

        RequestUtils.sendPostRequest(Api.DYNAMIC_REMOVEPRAISE, paramsMap, new ResponseCallBack<DynamicpPraise>() {
            @Override
            public void onSuccess(DynamicpPraise data) {
                mAdapter.removeData(0);
                dn.setIsZan(false);
                if(mAdapter.getDataSize()==0){
                    rlZan.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(DynamicDetialActivity.this, e.getMessage());
            }
        }, DynamicpPraise.class);
    }

    private void zan() {
        if(null==App.getLoginUser()){
            ForwardUtils.target(DynamicDetialActivity.this, Constant.LOGIN, null);
            return;
        }
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("dynamicId", did);

        RequestUtils.sendPostRequest(Api.DYNAMIC_ADDPRAISE, paramsMap, new ResponseCallBack<DynamicpPraise>() {
            @Override
            public void onSuccess(DynamicpPraise data) {
                if (data != null) {
                    rlZan.setVisibility(View.VISIBLE);
                    data.setNickName(App.getLoginUser().getNickName());
                    data.setPhoto(App.getLoginUser().getPhoto());
                    mAdapter.addData(data);
                    dn.setIsZan(true);
                }else{
                    if(mAdapter.getDataSize()==0){
                        rlZan.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(DynamicDetialActivity.this,e.getMessage());
            }
        }, DynamicpPraise.class);
    }

    private void reComment() {
        Log.i("rinima",etComment.getText().toString());
        String comment = etComment.getText().toString();
        String did = dn.getDynamicId();
        final DynamicComment dc = new DynamicComment();
        dc.setContent(comment);
        dc.setDynamicId(did);
        dc.setNickName(App.getLoginUser().getNickName());
        dc.setPhoto(App.getLoginUser().getPhoto());
        dc.setDate(DateUtils.dateToString(new Date(),"yyyy-MM-dd HH:mm:ss"));
        CommonHelper.reComment(dc,new ApiCallback<String>(){
            @Override
            public void onSuccess(String data) {
                etComment.setText("");
                dc.setCommentId(data);
                if(adapter.getCount()==0){
                    dc.setShowIcon(true);
                }
                adapter.add(dc);
            }
        });
    }
}
