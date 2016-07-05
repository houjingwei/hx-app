package com.huixiangtv.live.activity;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.DynamicCommentAdapter;
import com.huixiangtv.live.model.Dynamic;
import com.huixiangtv.live.model.DynamicComment;
import com.huixiangtv.live.model.DynamicImage;
import com.huixiangtv.live.service.LoginCallBack;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.ui.HuixiangLoadingLayout;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.DateUtils;
import com.huixiangtv.live.utils.KeyBoardUtils;
import com.huixiangtv.live.utils.StringUtil;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.ActionItem;
import com.huixiangtv.live.utils.widget.TitlePopup;
import com.huixiangtv.live.utils.widget.WidgetUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            ll1.setLayoutParams(llViewParams);
            llImgRoot.addView(ll1);


            //设置图片1张时图片的布局宽度
            photoParams = new LinearLayout.LayoutParams(imgTotalWidth, (int) (imgTotalWidth*0.5));

            addOneImgToLl(images, photoParams, ll1,0);

        }else if(rowNum==1 && consNum==2){
            //设置图片2张时llImgRoot子布局宽度
            llViewParams.height = imgTotalWidth/2;
            LinearLayout ll1 = new LinearLayout(DynamicDetialActivity.this);
            ll1.setLayoutParams(llViewParams);
            llImgRoot.addView(ll1);


            //设置图片2张时图片的布局宽度
            int width = (int) (imgTotalWidth*0.5-WidgetUtil.dip2px(DynamicDetialActivity.this,1));
            photoParams = new LinearLayout.LayoutParams(width,width);
            photoParams.rightMargin = WidgetUtil.dip2px(DynamicDetialActivity.this,1);

            addOneImgToLl(images, photoParams, ll1,0);
            addOneImgToLl(images, photoParams, ll1,1);

        }else if(rowNum==1 && consNum==3){
            int width = imgTotalWidth/3-WidgetUtil.dip2px(DynamicDetialActivity.this,1);
            photoParams = new LinearLayout.LayoutParams(width,width);
            photoParams.rightMargin = WidgetUtil.dip2px(DynamicDetialActivity.this,1);

            addllToRootLl(0,images, llViewParams, photoParams);


        }else{

            int width = imgTotalWidth/3-WidgetUtil.dip2px(DynamicDetialActivity.this,1);
            photoParams = new LinearLayout.LayoutParams(width,width);
            photoParams.rightMargin = WidgetUtil.dip2px(DynamicDetialActivity.this,1);


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
        ImageUtils.display(imageView,images.get(position).getSmall());
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

        llCommentView = (LinearLayout) findViewById(R.id.llCommentView);
        etComment = (EditText) findViewById(R.id.etComment);
        ffRoot.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

        adapter = new DynamicCommentAdapter(this);
        refreshView = (PullToRefreshListView) findViewById(R.id.refreshView);
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshView.setHeaderLayout(new HuixiangLoadingLayout(this));
        refreshView.setFooterLayout(new HuixiangLoadingLayout(this));
        refreshView.setAdapter(adapter);
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
        RequestUtils.sendPostRequest(Api.DYNAMIC_COMMENT, map, new ResponseCallBack<DynamicComment>() {
            @Override
            public void onSuccessList(List<DynamicComment> data) {
                super.onSuccessList(data);
                if (data != null && data.size() > 0) {
                    if(page==1){
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

    ImageView ivZanAndComm;
    private void addHeadView() {
        View view = LayoutInflater.from(DynamicDetialActivity.this).inflate(R.layout.activity_dynamic_detial_head, null, false);
        refreshView.getRefreshableView().addHeaderView(view);
        ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        mRecylerView = (RecyclerView) view.findViewById(R.id.mRecylerView);
        llImgRoot = (LinearLayout) view.findViewById(R.id.llImgRoot);
        ivZanAndComm = (ImageView) view.findViewById(R.id.ivZanAndComm);
        ivZanAndComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showZanAndCommPop(view);
            }
        });
    }


    private void showZanAndCommPop(View v) {

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
                    }
                    if (item.mTitle.equals("取消")) {
                        titlePopup.setComment("赞");
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
}
