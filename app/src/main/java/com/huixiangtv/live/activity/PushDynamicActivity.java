package com.huixiangtv.live.activity;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.duanqu.qupai.engine.session.MovieExportOptions;
import com.duanqu.qupai.engine.session.ProjectOptions;
import com.duanqu.qupai.engine.session.ThumbnailExportOptions;
import com.duanqu.qupai.engine.session.UISettings;
import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
import com.duanqu.qupai.sdk.android.QupaiManager;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.common.CommonUtil;
import com.huixiangtv.live.model.RecordResult;
import com.huixiangtv.live.model.Upfile;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.LoginCallBack;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CenterLoadingView;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.Utils;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.WidgetUtil;
import com.tencent.upload.task.data.FileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPicker2;
import me.iwf.photopicker.PhotoPreview2;

public class PushDynamicActivity extends BaseActivity implements AMapLocationListener {


    private static final int RECORD_CODE = 10001;

    //没张小图片宽度和高度
    int width = 0;

    /***** 获取地址参数 ******/
    private RelativeLayout switchWrapper;
    private RelativeLayout switchTrigger;
    private TextView switchLabel;
    private TextView tvLocal;
    private String jd;
    private String wd;

    /***** 录制参数******/
    private float mDurationLimit;
    private float mMinDurationLimit;
    private int mVideoBitrate;
    private int mWaterMark = -1;
    private int beautySkinProgress;
    private String waterMarkPath;

    /***** 添加图片和视频的父容器及子容器布局 ******/
    LinearLayout llContent;

    LinearLayout ll1;
    LinearLayout ll2;
    LinearLayout ll3;
    RelativeLayout videoRoot;

    LinearLayout.LayoutParams photoParams;
    LinearLayout.LayoutParams llRootParams;
    RelativeLayout.LayoutParams rlVideoRootParams;
    RelativeLayout.LayoutParams rlVideoPhotoParams;
    RelativeLayout.LayoutParams rlVideoRemoveParams;


    /***** 要发布的动态的类型和选择的图片或视频******/
    int type = 0;
    //选择的所有的图片
    ArrayList<String> photos = null;
    //每一行的图片集合
    List<String> imgList = new ArrayList<>();

    String videoFile = "";
    String thum = "";

    TextView etShareContent;



    CenterLoadingView loadingDialog = null;




    /********  定位  ********/
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_dynamic);

        initLocal();

        initView();

        toLocal(true);



    }

    private void initLocal() {
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为低功耗模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 设置定位监听
        locationClient.setLocationListener(this);
    }

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            msg.what = Utils.MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
        }
    }


    Handler mHandler = new Handler(){
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                //定位完成
                case Utils.MSG_LOCATION_FINISH:
                    AMapLocation loc = (AMapLocation)msg.obj;
                    jd = loc.getLongitude()+"";
                    wd  = loc.getLatitude()+"";
                    String cityArea = loc.getCity()+loc.getDistrict()+"";
                    tvLocal.setText(cityArea);
                    break;
                default:
                    break;
            }
        };
    };



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initView() {


        etShareContent = (TextView) findViewById(R.id.etShareContent);

        width = App.screenWidth / 4 - WidgetUtil.dip2px(PushDynamicActivity.this, 6);

        llContent = (LinearLayout) findViewById(R.id.llContent);

        llRootParams = new LinearLayout.LayoutParams(App.screenWidth, App.screenWidth / 4);
        llRootParams.setLayoutDirection(LinearLayout.HORIZONTAL);

        rlVideoRootParams = new RelativeLayout.LayoutParams(App.screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlVideoPhotoParams = new RelativeLayout.LayoutParams((int) (width * 1.25), width);
        rlVideoPhotoParams.leftMargin = WidgetUtil.dip2px(this, 5);
        rlVideoPhotoParams.topMargin = WidgetUtil.dip2px(this, 5);
        int deleteWidth = WidgetUtil.dip2px(PushDynamicActivity.this, 20);
        rlVideoRemoveParams = new RelativeLayout.LayoutParams(deleteWidth, deleteWidth);
        rlVideoRemoveParams.topMargin = 0;
        rlVideoRemoveParams.leftMargin = (int) (width * 1.25 - deleteWidth / 2);


        photoParams = new LinearLayout.LayoutParams(width, width);
        photoParams.leftMargin = WidgetUtil.dip2px(this, 5);
        photoParams.topMargin = WidgetUtil.dip2px(this, 5);


        initAddPhotoAndVideoImageBth();

        switchWrapper = (RelativeLayout) findViewById(R.id.switchWrapper);
        switchTrigger = (RelativeLayout) findViewById(R.id.switchTrigger);
        switchLabel = (TextView) findViewById(R.id.switchLabel);
        tvLocal = (TextView) findViewById(R.id.tvLocal);
        //添加定位切换事件
        switchWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == App.getLoginUser()) {
                    CommonHelper.showLoginPopWindow(PushDynamicActivity.this, R.id.liveMain, new LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            isLocalChange();
                        }
                    });
                    return;
                }
                isLocalChange();
            }
        });


        /**
         * 取消事件
         */
        findViewById(R.id.tvCancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        /**
         * 发布事件
         */
        findViewById(R.id.tvSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushDynamic();
            }
        });


    }


    /**
     * 初始化添加图片和添加视频的2个view
     */
    private void initAddPhotoAndVideoImageBth() {
        type = 0;
        ll1 = new LinearLayout(PushDynamicActivity.this);
        llContent.addView(ll1, llRootParams);
        addPicBtn(ll1);
        addVideoBtn();
    }


    /**
     * 发布动态
     */

    private void pushDynamic() {
        Log.i("dynamicType",type+"");

        if(type==0){
            //文本
            Log.i("dynamicType","文本");
            if(TextUtils.isEmpty(etShareContent.getText().toString())){
                CommonHelper.showTip(PushDynamicActivity.this, "动态内容为空");
                etShareContent.requestFocus();
                return;
            }
            doPushText();
        }else if(type==1){
            //图片
            Log.i("dynamicType","图片");
            if(TextUtils.isEmpty(etShareContent.getText().toString())){
                CommonHelper.showTip(PushDynamicActivity.this, "动态内容为空");
                etShareContent.requestFocus();
                return;
            }
            pushImage();
        }else if(type==2){
            //视频
            Log.i("dynamicType","视频");
            if(TextUtils.isEmpty(etShareContent.getText().toString())){
                CommonHelper.showTip(PushDynamicActivity.this, "动态内容为空");
                etShareContent.requestFocus();
                return;
            }
            pushVideo();
        }
    }

    private void pushVideo() {
        if(null==loadingDialog){
            loadingDialog = new CenterLoadingView(PushDynamicActivity.this);
        }
        loadingDialog.setCancelable(true);
        loadingDialog.setTitle("发布中");
        loadingDialog.show();
        //获取网络图片地址
        Map<String, String> upParams = new HashMap<String, String>();
        upParams.put("type", "1");
        ImageUtils.upVideoInfo(upParams, new ApiCallback<Upfile>() {
            @Override
            public void onSuccess(Upfile data) {
                ImageUtils.upVideo(PushDynamicActivity.this, data, videoFile, new ApiCallback<FileInfo>() {
                    @Override
                    public void onSuccess(FileInfo file) {
                        Log.i("dynamicType",file.url);
                        doPushVideo(file.url);
                    }

                });
            }
        });
    }


    /**
     * 处理视频发布
     * @param url
     */
    private void doPushVideo(String url) {
        Map<String,String> params = new HashMap<>();
        params.put("type",type+"");
        params.put("content",etShareContent.getText().toString());
        params.put("images", "");
        params.put("video", url);
        RequestUtils.sendPostRequest(Api.CREATE_DYNAMIC, params, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                if(null!=loadingDialog){
                    loadingDialog.dismiss();
                }
                CommonHelper.showTip(PushDynamicActivity.this,"动态发布成功");
                App.createDynamic = true;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        onBackPressed();
                    }
                }, 1000);

            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                if(null!=loadingDialog){
                    loadingDialog.dismiss();
                }
                CommonHelper.showTip(PushDynamicActivity.this,e.getMessage());
            }
        },String.class);
    }


    private void doPushText() {
        Map<String,String> params = new HashMap<>();
        params.put("type",type+"");
        params.put("content",etShareContent.getText().toString());
        params.put("images", "");
        params.put("video", "");
        RequestUtils.sendPostRequest(Api.CREATE_DYNAMIC, params, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                if(null!=loadingDialog){
                    loadingDialog.dismiss();
                }
                CommonHelper.showTip(PushDynamicActivity.this,"动态发布成功");
                App.createDynamic = true;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        onBackPressed();
                    }
                }, 1000);

            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                if(null!=loadingDialog){
                    loadingDialog.dismiss();
                }
                CommonHelper.showTip(PushDynamicActivity.this,e.getMessage());
            }
        },String.class);
    }


    /**
     * 发布图片动态,首先上传图片获取地址
     */
    private void pushImage() {
        if(null==loadingDialog){
            loadingDialog = new CenterLoadingView(PushDynamicActivity.this);
        }
        loadingDialog.setCancelable(true);
        loadingDialog.setTitle("发布中");
        loadingDialog.show();
        //获取网络图片地址
        imgPaths.clear();
        upImages();
    }

    final List<String> imgPaths = new ArrayList<>();
    int i = 0;
    private void upImages() {
        Log.i("dynamicType","upImage");
        Map<String, String> upParams = new HashMap<String, String>();
        upParams.put("type", "3");

        ImageUtils.upFileInfo(upParams, new ApiCallback<Upfile>() {
            @Override
            public void onSuccess(Upfile data) {
                ImageUtils.upFile(PushDynamicActivity.this, data, photos.get(i), new ApiCallback<FileInfo>() {
                    @Override
                    public void onSuccess(FileInfo file) {
                        Log.i("dynamicType",file.url);
                        imgPaths.add(file.url);
                        if(i==(photos.size()-1)){
                            doPushImage(imgPaths);
                        }else{
                            upImages();
                            i++;
                        }

                    }
                });
            }
        });
    }

    /**
     * 处理图片上传
     * @param imgPaths
     */
    private void doPushImage(final List<String> imgPaths) {
        Log.i("myImgPath",imgPaths.toString());
        Map<String,String> params = new HashMap<>();
        params.put("type",type+"");
        params.put("content",etShareContent.getText().toString());
        params.put("images", CommonUtil.listToString(imgPaths));
        params.put("video", "");
        RequestUtils.sendPostRequest(Api.CREATE_DYNAMIC, params, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                for (String path : imgPaths) {
                    File f = new File(path);
                    if(f.exists()){
                        Log.i("myImgPath","删除"+path);
                        f.delete();
                    }
                }
                if(null!=loadingDialog){
                    loadingDialog.dismiss();
                }
                CommonHelper.showTip(PushDynamicActivity.this,"动态发布成功");
                App.createDynamic = true;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        onBackPressed();
                    }
                }, 1000);

            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                if(null!=loadingDialog){
                    loadingDialog.dismiss();
                }
                CommonHelper.showTip(PushDynamicActivity.this,e.getMessage());
            }
        },String.class);

    }


    /**
     * 拍摄视频或选择本地视频
     */
    private void choiseVideo() {
        //判断鉴权，如果鉴权过，直接录制
        if (null != Constant.accessToken) {
            toRecord();
        } else {
            App.qupaiAuth(new ApiCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    toRecord();
                }
            });
        }
    }


    /**
     * 跳转到录制页面
     */
    private void toRecord() {
        QupaiService qupaiService = QupaiManager.getQupaiService(PushDynamicActivity.this);
        if (qupaiService == null) {
            Toast.makeText(PushDynamicActivity.this, "插件没有初始化，无法获取 QupaiService",Toast.LENGTH_LONG).show();
            return;
        }

        //视频时长
        mDurationLimit = 60f;
        //默认最小时长
        mMinDurationLimit = 12f;
        //视频码率
        mVideoBitrate = 2;
        //是否需要水印
        mWaterMark = 0;
        //水印存储的目录
        waterMarkPath = "assets://Qupai/watermark/qupai-logo.png";
        //美颜参数:1-100.这里不设指定为80,这个值只在第一次设置，之后在录制界面滑动美颜参数之后系统会记住上一次滑动的状态
        beautySkinProgress = 60;
        UISettings _UISettings = new UISettings() {
            @Override
            public boolean hasEditor() { return false; }

            @Override
            public boolean hasImporter() { return true; }

            @Override
            public boolean hasGuide() { return false; }

            @Override
            public boolean hasSkinBeautifer() { return false; }
        };

        MovieExportOptions movie_options = new MovieExportOptions.Builder().setVideoBitrate(mVideoBitrate).configureMuxer("movflags", "+faststart").build();

        ProjectOptions projectOptions = new ProjectOptions.Builder().setVideoSize(480, 360).setVideoFrameRate(30).setDurationRange(mMinDurationLimit, mDurationLimit).get();

        //设置只要一张略缩图
        ThumbnailExportOptions thumbnailExportOptions = new ThumbnailExportOptions.Builder().setCount(1).get();

        VideoSessionCreateInfo info = new VideoSessionCreateInfo.Builder()
                .setWaterMarkPath(waterMarkPath)
                .setWaterMarkPosition(mWaterMark)
                .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_BACK)
                .setBeautyProgress(beautySkinProgress)
                .setBeautySkinOn(true)
                .setMovieExportOptions(movie_options)
                .setThumbnailExportOptions(thumbnailExportOptions)
                .build();

        qupaiService.initRecord(info, projectOptions, _UISettings);
        qupaiService.showRecordPage(PushDynamicActivity.this, RECORD_CODE, false);
    }


    /**
     * 选择图片
     */
    private void choisePhoto() {
        PhotoPicker2.builder()
                .setPhotoCount(9)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            //选择图片
            if (data != null) {
                if (null != photos) {
                    photos.clear();
                }
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                for (String url : photos) {
                    Log.i("imgUrl", url);
                }
                resetImage(photos);
            }
        }else if (resultCode == RESULT_OK && requestCode == RECORD_CODE) {
            //选择视频
            RecordResult result = new RecordResult(data);
            //得到视频地址，和缩略图地址的数组，返回十张缩略图
            videoFile = result.getPath();
            thum = result.getThumbnail()[0];
            result.getDuration();
            resetVideo();
        } else {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(PushDynamicActivity.this, "RESULT_CANCELED", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * 视频选择刷新重置
     * @param
     */
    private void resetVideo() {
        type = 2;
        resetView();
        videoRoot = new RelativeLayout(PushDynamicActivity.this);
        llContent.addView(videoRoot, rlVideoRootParams);

        ImageView imgView = new ImageView(this);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageUtils.display(imgView, thum);
        videoRoot.addView(imgView, rlVideoPhotoParams);

        ImageView delImg = new ImageView(this);
        delImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initAddPhotoAndVideoImageBth();
            }
        });
        delImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        delImg.setImageDrawable(getResources().getDrawable(R.mipmap.icon_record_live_delete));
        videoRoot.addView(delImg, rlVideoRemoveParams);
    }


    /**
     * 图片选择刷新重置
     * @param photos
     */
    private void resetImage(ArrayList<String> photos) {
        resetView();
        //图片总数
        int num = 0;
        //行数
        int lineNum = 0;
        //添加图片按钮的行数
        int addPhotoLineNum = 0;

        if (null != photos && photos.size() > 0) {
            type = 1;

            num = photos.size();
            if (num % 4 != 0) {
                lineNum = num / 4 + 1;
                addPhotoLineNum = lineNum;
            } else {
                lineNum = num / 4;
                addPhotoLineNum = lineNum + 1;
            }


            if (lineNum == 1) {
                addL1(photos, 0);
            } else if (lineNum == 2) {
                addL1(photos, 0);
                addL2(photos, 1);
            } else if (lineNum == 3) {
                addL1(photos, 0);
                addL2(photos, 1);
                addL3(photos, 2);
            }


            if (photos.size() > 0 && photos.size() < 9) {
                if (addPhotoLineNum == 1) {
                    addPicBtn(ll1);
                } else if (addPhotoLineNum == 2) {
                    if (null == ll2) {
                        ll2 = new LinearLayout(PushDynamicActivity.this);
                        llContent.addView(ll2, llRootParams);
                    }
                    addPicBtn(ll2);
                } else if (addPhotoLineNum == 3) {
                    if (null == ll3) {
                        ll3 = new LinearLayout(PushDynamicActivity.this);
                        llContent.addView(ll3, llRootParams);
                    }
                    addPicBtn(ll3);
                }
            }
        } else {
            initAddPhotoAndVideoImageBth();
        }

    }


    /**
     * 重置时清空所有图片和视频view
     */
    private void resetView() {
        llContent.removeAllViews();
        ll3 = null;
        ll2 = null;
        ll1 = null;
    }


    /**
     * 动态创建添加视频的图片按钮
     */
    private void addVideoBtn() {
        ImageView addVideoImageView = new ImageView(this);
        addVideoImageView.setImageDrawable(getResources().getDrawable(R.mipmap.icon_dynamic_video));
        addVideoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiseVideo();
            }
        });
        ll1.addView(addVideoImageView, photoParams);
    }


    /**
     * 添加选择图片的按钮
     *
     * @param ll
     */
    private void addPicBtn(LinearLayout ll) {
        ImageView addPhotoImageView = new ImageView(PushDynamicActivity.this);
        addPhotoImageView.setImageDrawable(getResources().getDrawable(R.mipmap.icon_dynamic_photo));
        addPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choisePhoto();
            }
        });
        ll.addView(addPhotoImageView, photoParams);
    }


    /**
     * 动态第三行添加图片
     * @param photos
     * @param line
     */
    private void addL3(ArrayList<String> photos, int line) {
        ll3 = new LinearLayout(PushDynamicActivity.this);
        llContent.addView(ll3, llRootParams);
        imgList = getCurrLineImgs(2, photos);
        addImageToLl(ll3, imgList, line);
    }

    /**
     * 动态第二行添加图片
     * @param photos
     * @param line
     */
    private void addL2(ArrayList<String> photos, int line) {
        ll2 = new LinearLayout(PushDynamicActivity.this);
        llContent.addView(ll2, llRootParams);
        imgList = getCurrLineImgs(1, photos);
        addImageToLl(ll2, imgList, line);
    }


    /**
     * 动态第一行添加图片
     * @param photos
     * @param line
     */
    private void addL1(ArrayList<String> photos, int line) {
        ll1 = new LinearLayout(PushDynamicActivity.this);
        llContent.addView(ll1, llRootParams);
        imgList = getCurrLineImgs(0, photos);
        addImageToLl(ll1, imgList, line);
    }


    /**
     * 添加图片到对应的linearlayout
     * @param ll
     * @param imgList
     * @param line
     */
    private void addImageToLl(LinearLayout ll, List<String> imgList, int line) {
        int position = line * 4;
        for (String img : imgList) {
            final int curPosition = position;
            ImageView imgView = new ImageView(this);
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    previewImg(curPosition);
                }
            });
            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageUtils.display(imgView, img);
            ll.addView(imgView, photoParams);
            position++;
        }

    }

    /**
     * 图片预览
     *
     * @param curPosition
     */
    private void previewImg(int curPosition) {
        PhotoPreview2.builder().setPhotos(photos).setCurrentItem(curPosition).start(this, PhotoPicker.REQUEST_CODE);
    }

    /**
     * 截取每一行的图片集合
     * @param num
     * @param photos
     * @return
     */
    private List<String> getCurrLineImgs(int num, ArrayList<String> photos) {
        List<String> list = null;
        int min = num * 4;
        int max = (num + 1) * 4;
        if (photos.size() > max) {
            list = photos.subList(min, max);
        } else {
            list = photos.subList(min, photos.size());
        }
        return list;

    }


    private void toLocal(boolean bool){
        if(bool){

            // 设置是否需要显示地址信息
            locationOption.setNeedAddress(true);
            // 设置是否开启缓存
            locationOption.setLocationCacheEnable(true);
            //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
            locationOption.setOnceLocationLatest(true);
            locationOption.setInterval(Long.valueOf(2000l));

            // 设置定位参数
            locationClient.setLocationOption(locationOption);
            // 启动定位
            locationClient.startLocation();
        }else{
            tvLocal.setText("");
            jd = null;
            wd = null;
        }

    }



    /**
     * 定位开关切换
     */
    private void isLocalChange() {
        GradientDrawable gd = (GradientDrawable) switchWrapper.getBackground();
        String triggerTag = switchTrigger.getTag().toString();
        float offset = WidgetUtil.dip2px(this, 12);

        if ("open_yes".equals(triggerTag)) {
            //开关状态
            gd.setColor(getResources().getColor(R.color.gray));
            switchLabel.setTextColor(getResources().getColor(R.color.gray));
            switchTrigger.setTag("open_no");
            switchLabel.setText("关");
            ObjectAnimator anim = ObjectAnimator.ofFloat(switchTrigger, "translationX", 0.0f, -offset);
            anim.setDuration(300l);
            anim.start();
            toLocal(false);

        } else {
            //关闭状态
            gd.setColor(getResources().getColor(R.color.mainColor));
            switchLabel.setTextColor(getResources().getColor(R.color.mainColor));
            switchTrigger.setTag("open_yes");
            switchLabel.setText("开");
            ObjectAnimator anim = ObjectAnimator.ofFloat(switchTrigger, "translationX", -offset, 0.0f);
            anim.setDuration(300l);
            anim.start();
            toLocal(true);


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
}
