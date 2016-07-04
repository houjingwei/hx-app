package com.huixiangtv.live.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.AttentionMeAdapter;
import com.huixiangtv.live.adapter.DynamicCommentAdapter;
import com.huixiangtv.live.model.Dynamic;
import com.huixiangtv.live.model.DynamicImage;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.ui.HuixiangLoadingLayout;
import com.huixiangtv.live.ui.MLImageView;
import com.huixiangtv.live.utils.DateUtils;
import com.huixiangtv.live.utils.StringUtil;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.WidgetUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicDetialActivity extends BaseBackActivity {

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    private PullToRefreshListView refreshView;
    int page = 1;
    DynamicCommentAdapter adapter;

    String did;

    int imgTotalWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detial);
        x.view().inject(this);
        did = getIntent().getStringExtra("did");
        intitView();
        if (StringUtil.isNotNull(did)) {
            initData();
        } else {
            onBackPressed();
        }
    }

    /**
     * 加载数据
     */
    private void initData() {
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

    private void upViewData(Dynamic data) {
        ImageUtils.display(ivPhoto,data.getPhoto());
        tvName.setText(data.getNickName());
        tvContent.setText(data.getContent());
        tvTime.setText(DateUtils.formatDisplayTime(data.getDate(),"yyyy-MM-dd HH:mm:ss"));

        //动态添加图片
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

        LinearLayout.LayoutParams rootViewParams = new LinearLayout.LayoutParams(imgTotalWidth,imgTotalWidth/3);
        rootViewParams.setLayoutDirection(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = null;
        if(rowNum==1 && consNum==1){
            LinearLayout ll1 = new LinearLayout(DynamicDetialActivity.this);
            ll1.setLayoutParams(rootViewParams);
            llImgRoot.addView(ll1);

            params = new LinearLayout.LayoutParams(imgTotalWidth, (int) (imgTotalWidth*0.5));
            MLImageView imageView = (MLImageView) LayoutInflater.from(DynamicDetialActivity.this).inflate(R.layout.activity_dynamic_image, null, false);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageUtils.display(imageView,images.get(0).getSmall());
            ll1.addView(imageView,params);

        }else if(rowNum==1 && consNum==2){


            rootViewParams.height = imgTotalWidth/2;
            LinearLayout ll1 = new LinearLayout(DynamicDetialActivity.this);
            ll1.setLayoutParams(rootViewParams);
            llImgRoot.addView(ll1);


            int width = (int) (imgTotalWidth*0.5-WidgetUtil.dip2px(DynamicDetialActivity.this,1));
            params = new LinearLayout.LayoutParams(width,width);
            params.rightMargin = WidgetUtil.dip2px(DynamicDetialActivity.this,1);

            MLImageView imageView = (MLImageView) LayoutInflater.from(DynamicDetialActivity.this).inflate(R.layout.activity_dynamic_image, null, false);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ll1.addView(imageView,params);
            ImageUtils.display(imageView,images.get(0).getSmall());

            MLImageView imageView2 = (MLImageView) LayoutInflater.from(DynamicDetialActivity.this).inflate(R.layout.activity_dynamic_image, null, false);
            imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ll1.addView(imageView2,params);
            ImageUtils.display(imageView2,images.get(1).getSmall());

        }else if(rowNum==1 && consNum==3){

            int width = (int) (imgTotalWidth/3-WidgetUtil.dip2px(DynamicDetialActivity.this,1));
            params = new LinearLayout.LayoutParams(width,width);
            params.rightMargin = WidgetUtil.dip2px(DynamicDetialActivity.this,1);

            addllToRootLl(0,images, rootViewParams, params);



        }else{

            int width = (int) (imgTotalWidth/3-WidgetUtil.dip2px(DynamicDetialActivity.this,1));
            params = new LinearLayout.LayoutParams(width,width);
            params.rightMargin = WidgetUtil.dip2px(DynamicDetialActivity.this,1);

            if(rowNum==2){

                addllToRootLl(0,images, rootViewParams, params);

                addllToRootLl(1,images, rootViewParams, params);

            }else{

                addllToRootLl(0,images, rootViewParams, params);

                addllToRootLl(1,images, rootViewParams, params);

                addllToRootLl(2,images, rootViewParams, params);


            }

        }
    }

    private void addllToRootLl(int startIndex,List<DynamicImage> images, LinearLayout.LayoutParams rootViewParams, LinearLayout.LayoutParams params) {
        imgList = getCurrLineImgs(startIndex, images);
        LinearLayout ll1 = new LinearLayout(DynamicDetialActivity.this);
        ll1.setLayoutParams(rootViewParams);
        llImgRoot.addView(ll1);
        addllToRootLlAndShow(params, ll1);
    }

    private void addllToRootLlAndShow(LinearLayout.LayoutParams params, LinearLayout ll) {
        for (DynamicImage img: imgList) {
            MLImageView imageView = (MLImageView) LayoutInflater.from(DynamicDetialActivity.this).inflate(R.layout.activity_dynamic_image, null, false);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ll.addView(imageView,params);
            ImageUtils.display(imageView,img.getSmall());
        }
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

        adapter = new DynamicCommentAdapter(this);
        refreshView = (PullToRefreshListView) findViewById(R.id.refreshView);
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshView.setHeaderLayout(new HuixiangLoadingLayout(this));
        refreshView.setFooterLayout(new HuixiangLoadingLayout(this));
        refreshView.setAdapter(adapter);
        addHeadView();


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

    private void loadData() {

    }


    ImageView ivPhoto;
    TextView tvName;
    TextView tvContent;
    TextView tvTime;
    RecyclerView mRecylerView;
    LinearLayout llImgRoot;
    private void addHeadView() {
        View view = LayoutInflater.from(DynamicDetialActivity.this).inflate(R.layout.activity_dynamic_detial_head, null, false);
        refreshView.getRefreshableView().addHeaderView(view);
        ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        mRecylerView = (RecyclerView) view.findViewById(R.id.mRecylerView);
        llImgRoot = (LinearLayout) view.findViewById(R.id.llImgRoot);
    }
}
