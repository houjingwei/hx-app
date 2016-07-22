package com.huixiangtv.liveshow.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.duanqu.qupai.android.camera.AutoFocusCallback;
import com.duanqu.qupai.android.camera.CameraClient;
import com.duanqu.qupai.android.camera.CameraDevice;
import com.duanqu.qupai.android.camera.CameraSurfaceController;
import com.duanqu.qupai.android.camera.SessionRequest;
import com.duanqu.qupai.live.CreateLiveListener;
import com.duanqu.qupai.live.LiveAudioStream;
import com.duanqu.qupai.live.LiveRecorderManager;
import com.duanqu.qupai.live.LiveService;
import com.duanqu.qupai.live.LiveStreamStatus;
import com.duanqu.qupai.live.LiveVideoStream;
import com.duanqu.qupai.utils.MathUtil;
import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Beauty.BeautyRender;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.Live;
import com.huixiangtv.liveshow.model.LiveMsg;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CenterLoadingView;
import com.huixiangtv.liveshow.ui.ColaProgress;
import com.huixiangtv.liveshow.ui.LiveView;
import com.huixiangtv.liveshow.ui.StartLiveView;
import com.huixiangtv.liveshow.utils.ForwardUtils;
import com.huixiangtv.liveshow.utils.KeyBoardUtils;
import com.huixiangtv.liveshow.utils.MeizuSmartBarUtils;
import com.huixiangtv.liveshow.utils.StringUtil;
import com.huixiangtv.liveshow.utils.image.ImageUtils;
import com.umeng.socialize.UMShareAPI;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import io.rong.imlib.RongIMClient;


public class LiveRecordActivity extends Activity implements View.OnClickListener ,LiveRecorderManager.OnStatusCallback{



    private static final String TAG = "LiveActivity";

    @ViewInject(R.id.flCover)
    FrameLayout flCover;
    @ViewInject(R.id.flPlayView)
    FrameLayout flPlayView;
    private View recordView;



    TextView tvTheme;
    TextView tvStart;
    TextView tvDynamic;
    StartLiveView startLiveView;

    LiveView liveView;

    private Live live;
    private int sharePlat = 0;
    private int isLocal = 1;

    private int startRecord = 0;

    CenterLoadingView loadingDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        x.view().inject(this);
        //注册eventBus
        EventBus.getDefault().register(this);
        Log.i("eventBus","注册eventBus");
        if (MeizuSmartBarUtils.hasSmartBar()) {
            View decorView = getWindow().getDecorView();
            MeizuSmartBarUtils.hide(decorView);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }



        addRecordView();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                initStartView();
            }
        }, 1000);

    }



    @Subscriber(tag = "live_tag", mode = ThreadMode.MAIN)
    public void reciveLive(LiveMsg msg) {
        if(null!=liveView){
            liveView.msgRecive(msg);
        }

    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }



    private void initStartView() {
        startLiveView = new StartLiveView(this);
        startLiveView.setActivity(this);
        flCover.addView(startLiveView);
        startLiveView.findViewById(R.id.ivCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCamera();
            }
        });
        tvTheme = startLiveView.getTvTheme();
        tvStart = startLiveView.getTvStart();
        tvDynamic = startLiveView.getTvDynamic();
        tvTheme.setOnClickListener(this);
        tvStart.setOnClickListener(this);
        tvDynamic.setOnClickListener(this);
        KeyBoardUtils.closeKeybord(startLiveView.getEtTitle(), this);
    }


    private SurfaceView _CameraSurface;
    private CameraClient _Client;
    private CameraSurfaceController _SurfaceControl;

    private LiveRecorderManager mLiveRecorder;
    private LiveVideoStream mVideoStream;
    private LiveAudioStream mAudioStream;

    private boolean mIsRecording = false;

    private final float mMaxZoomLevel = 3;
    private BeautyRender beautyRender;

    private void addRecordView() {


        recordView = LayoutInflater.from(LiveRecordActivity.this).inflate(R.layout.record_view, null, false);
        flPlayView.addView(recordView);


        _CameraSurface = (SurfaceView) recordView.findViewById(R.id.camera_surface);
        _CameraSurface.getHolder().addCallback(_CameraSurfaceCallback);
        _CameraSurface.setOnTouchListener(mOnTouchListener);

        //对焦
        mDetector = new GestureDetector(_CameraSurface.getContext(), mGestureDetector);
        //缩放
        mScaleDetector = new ScaleGestureDetector(_CameraSurface.getContext(), mScaleGestureListener);

        _Client = new CameraClient();
        _Client.setOnErrorListener(new CameraClient.OnErrorListener() {
            @Override
            public void onError(CameraClient cameraClient, int i) {
                Log.i("cameraError",i+"");
            }
        });
        _Client.setAutoFocusCallback(new AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean b, CameraDevice cameraDevice) {

            }

            @Override
            public void onAutoFocusMoving(boolean b, CameraDevice cameraDevice) {

            }
        });


        _Client.setCallback(mCameraClientCallback);//设置摄像头开启的回调
        _Client.setCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT);//设置摄像头为前置摄像头
        _Client.setContentSize(384, 640);//设置摄像头分辨率，这里建议与VideoStream设置同样尺寸，否则会产生黑边。VideoStream设置参考【视频流设置】


        //初始化美颜
        beautyRender = BeautyRender.getInstance();
        beautyRender.initRenderer(getAssets(),_Client);
        beautyRender.switchBeauty(true);


        if(null!=recordView){
            //开始预览
            _Client.startPreview();
            if (_SurfaceControl != null) {
                _SurfaceControl.setVisible(true);
            }
        }
    }

    private CameraClient.Callback mCameraClientCallback = new CameraClient.Callback() {
        @Override
        public void onDeviceAttach(CameraClient client) {
            //摄像头开启成功
            SessionRequest request = client.getSessionRequest();
            request.mExposureCompensation = client.getCharacteristics().getMaxExposureCompensation() / 3;
            request.previewFrameRate = 20;
        }

        @Override
        public void onSessionAttach(CameraClient client) {
            //成功开启预览
            _Client.autoFocus(0.5f, 0.5f, _SurfaceControl);
        }

        @Override
        public void onCaptureUpdate(CameraClient client) {
            //暂时用不到
        }

        @Override
        public void onSessionDetach(CameraClient client) {
            //关闭预览成功

        }

        @Override
        public void onDeviceDetach(CameraClient client) {
            //关闭摄像头成功
        }
    };

    private ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {

            float zoom = _Client.zoomRatio * scaleGestureDetector.getScaleFactor();
            _Client.setZoom(MathUtil.clamp(zoom, 1, mMaxZoomLevel), null);

            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

        }
    };


    private GestureDetector mDetector;
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector.OnGestureListener mGestureDetector = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            float x = motionEvent.getX() / _SurfaceControl.getWidth();
            float y = motionEvent.getY() / _SurfaceControl.getHeight();

            return _Client.autoFocus(x, y, _SurfaceControl);

        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
    };

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mDetector.onTouchEvent(motionEvent);
            mScaleDetector.onTouchEvent(motionEvent);
            return true;
        }
    };

    private final SurfaceHolder.Callback _CameraSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            holder.setKeepScreenOn(true);

            _SurfaceControl = _Client.addSurface(holder);
            _SurfaceControl.setVisible(true);
            _SurfaceControl.setDisplayMethod(CameraSurfaceController.FullScreen | CameraSurfaceController.ScaleEnabled);

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            _SurfaceControl.setResolution(width, height);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            _Client.removeSurface(holder);
            _SurfaceControl = null;
        }
    };





    int lineCount = 0;
    @Override
    public void onNetworkStatusChange(int status) {
        Message msg = new Message();
        switch (status) {
            case LiveStreamStatus.CONNECTION_START:
                break;
            case LiveStreamStatus.CONNECTION_ESTABLISHED:
                Log.i("living","living");
                msg.what=NET_OK;
                pushHandler.sendMessage(msg);
                break;
            case LiveStreamStatus.CONNECTION_CLOSED:
                mLiveRecorder.release();
                mLiveRecorder = null;
                msg.what=NET_CLOSE;
                pushHandler.sendMessage(msg);
                break;
            case LiveStreamStatus.CONNECTION_ERROR_IO:
            case LiveStreamStatus.CONNECTION_ERROR_MEM:
            case LiveStreamStatus.CONNECTION_ERROR_TIMEOUT:
            case LiveStreamStatus.CONNECTION_ERROR_BROKENPIPE:
            case LiveStreamStatus.CONNECTION_ERROR_INVALIDARGUMENT:
            case LiveStreamStatus.CONNECTION_ERROR_NETWORKUNREACHABLE:
                if (mIsRecording) {
                    mLiveRecorder.reconnect();
                } else {
                    mLiveRecorder.release();
                    mLiveRecorder = null;
                }
                msg.what=NET_CLOSE;
                pushHandler.sendMessage(msg);
                break;
            case LiveStreamStatus.CONNECTION_ERROR_AUTH:
                if (mIsRecording) {
                    stopRecorder();
                }
                mLiveRecorder.release();
                mLiveRecorder = null;
                msg.what=NET_CLOSE;
                pushHandler.sendMessage(msg);
                break;
            default:
                if (mIsRecording) {
                    mLiveRecorder.reconnect();
                } else {
                    stopRecorder();
                    mLiveRecorder.release();
                    mLiveRecorder = null;
                }
                break;
        }
    }



    private static final int NET_CLOSE = -22;
    private static final int NET_OK = -23;
    Handler pushHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NET_CLOSE:
                    if(lineCount==0 && !isFinish){
                        lineCount++;
                        if(null==loadingDialog ){
                            loadingDialog = new CenterLoadingView(LiveRecordActivity.this);
                        }
                        loadingDialog.setCancelable(true);
                        loadingDialog.setTitle("等待网络恢复");
                        loadingDialog.show();

                    }
                    break;
                case NET_OK:
                    lineCount = 0;
                    if(null!=loadingDialog){
                        loadingDialog.dismiss();
                    }
                    break;
            }
        }
    };


    private final int CHANGE_CAMERA = 100;
    private final int CHANGE_MEIYAN = 101;
    private Handler liveViewHandle = new Handler() {
        @Override
        public void handleMessage(android.os.Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case CHANGE_CAMERA:
                    changeCamera();
                    break;
                case CHANGE_MEIYAN:
                    changeBeau();
                    break;

            }
        }
    };





    private void stopRecorder() {
        if (mIsRecording) {

            Log.i(TAG, "Stop live stream stopRecord!");
            mLiveRecorder.stop();
            mIsRecording = false;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTheme:
                //选择主题
                ForwardUtils.target(LiveRecordActivity.this, Constant.LIVE_TOPIC, null);
                break;
            case R.id.tvStart:
                Map<String, String> params = new HashMap<String, String>();
                params.put("local", startLiveView.getLocal() + "");
                params.put("platform", startLiveView.getPlatform() + "");
                //请求开始直播
                toLive();
                break;
            case R.id.tvDynamic:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(LiveRecordActivity.this, Constant.PUSH_DYNAMIC, null);
                } else {
                    ForwardUtils.target(LiveRecordActivity.this, Constant.LOGIN, null);
                }
                break;

        }
    }





    /**
     * 请求开始直播
     */
    private ColaProgress cp = null;
    private void toLive() {
        final Map<String, String> params = new HashMap<String, String>();

        params.put("title", startLiveView.getEtTitle().getText().toString());
        if(StringUtil.isEmpty(startLiveView.getTvTheme().getText().toString())){
            params.put("topic", "念念不忘 必有回响");
        }else{
            params.put("topic", startLiveView.getTvTheme().getText().toString());
        }

        Log.i("isLocal",isLocal+"");
        Log.i("isLocal",params.toString());
        if(isLocal==1){
            String[] jwd = startLiveView.getJwd();
            params.put("lon", jwd[0]);
            params.put("lat", jwd[1]);
            params.put("address", jwd[2]);
            Log.i("isLocal",jwd.toString());
        }else{
            params.put("lon", "");
            params.put("lat", "");
            params.put("address", "");
        }

        ObjectAnimator anim = ObjectAnimator.ofFloat(startLiveView, "alpha", 0f);
        anim.setDuration(1000);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }
            @Override
            public void onAnimationEnd(Animator animator) {
                sharePlat = startLiveView.platform;
                if (null != startLiveView) {
                    startRecord = 2;
                    flCover.removeView(startLiveView);
                    startLiveView = null;
                }
                showLive(params);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }

        });
        anim.start();
    }



    private void showLive(Map<String, String> params) {
        cp = ColaProgress.show(LiveRecordActivity.this, "准备直播", false, true, null);
        RequestUtils.sendPostRequest(Api.LIVE_SHOW, params, new ResponseCallBack<Live>() {
            @Override
            public void onSuccess(Live data) {
                super.onSuccess(data);
                if (null != cp) {
                    cp.dismiss();
                }
                live =data;
                outhAndStartPush();
                living(data);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                if (null != cp) {
                    cp.dismiss();
                }
            }
        }, Live.class);
    }



    private void living(Live data) {
        liveView = new LiveView(LiveRecordActivity.this);
        liveView.setInfo(data);
        flCover.addView(liveView);
        liveView.setHandle(liveViewHandle);
        liveView.loadLive();
        liveView.shareWin();
//        ShareModel model = new ShareModel();
//        UMImage image = new UMImage(LiveRecordActivity.this, live.getPhoto());
//        model.setTitle(live.getNickName()+live.getTitle());
//        model.setTargetUrl(Api.SHARE_URL+live.getLid());
//        model.setImageMedia(image);
//        model.setContent(live.getNickName()+live.getTitle());
//        if(sharePlat==1){
//            CommonHelper.share(LiveRecordActivity.this,model,SHARE_MEDIA.SMS,0,null);
//        }else if(sharePlat==2){
//            CommonHelper.share(LiveRecordActivity.this,model,SHARE_MEDIA.QQ,0,null);
//        }else if(sharePlat==3){
//            CommonHelper.share(LiveRecordActivity.this,model,SHARE_MEDIA.QZONE,0,null);
//        }else if(sharePlat==4){
//            CommonHelper.share(LiveRecordActivity.this,model,SHARE_MEDIA.WEIXIN,0,null);
//        }else if(sharePlat==5){
//            CommonHelper.share(LiveRecordActivity.this,model,SHARE_MEDIA.WEIXIN_CIRCLE,0,null);
//        }else if(sharePlat==6){
//            CommonHelper.share(LiveRecordActivity.this,model,SHARE_MEDIA.SINA,0,null);
//        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }catch(Exception e){}
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && requestCode == 1) {
                    String topic = data.getStringExtra("topic");
                    tvTheme.setText(topic);
                }
                break;
            default:
                break;
        }
    }


    private void outhAndStartPush(){
        if (null != Constant.accessToken){
            startPush();
        }else{
            App.qupaiAuth(new ApiCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    startPush();
                }
            });
        }
    }

    private void startPush() {
        try {
            if(null!=live && StringUtil.isNotEmpty(live.getPushUrl())) {
                LiveService.getInstance().createLive(Constant.accessToken, Constant.SPACE, Constant.LIVE_URL);
                LiveService.getInstance().setCreateLiveListener(new CreateLiveListener() {
                    @Override
                    public void onCreateLiveError(int errorCode, String message) {
                    }

                    @Override
                    public void onCreateLiveSuccess(String pushUrl, String playUrl) {
                        startRecorder(live.getPushUrl(), playUrl);
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void startRecorder(String pushUrl,String playUrl) {
        try{
            if (mLiveRecorder != null) {
                return;
            }
            initLiveRecord(pushUrl);
            //ip_address这里修改为可以直接ip推流.就近原则。建议使用httpDNS得到最优ip.直接推流.不用即直接传null
            mLiveRecorder.start(this);
            mIsRecording = true;
            Log.i(TAG, "************** Starting live stream! **************");
        }catch(Exception e){
            Log.i("huixiangLive_Error",e.getMessage());
        }
    }

    private void initLiveRecord(String pushUrl) {
        mLiveRecorder = new LiveRecorderManager();
        mLiveRecorder.init(pushUrl, "flv");//flv为推流文件格式，目前仅支持flv
        mLiveRecorder.setNetworkThreshHold(90);//设置网络最大buffer阈值.可不设，默认为90
        mVideoStream = mLiveRecorder.addVideoStream();
        mVideoStream.init(384, 640, 400000, 20, 3);//参数分别为：宽、高、码率、帧数、帧数间隔；宽384为推荐设置，可解决部分手机不支持16倍数的问题
        mVideoStream.setInput(_Client);
        mVideoStream.setMirrored(_Client.isFrontCamera());
        mVideoStream.setBitRateRange(400000,800000);//设置最小码率和最大码率，可根据网络状况自动调节码率.会自动调节码率
        mAudioStream = mLiveRecorder.addAudioStream();
        mAudioStream.init(44100, 32000);//音频采样率、码率
        mLiveRecorder.setOnStatusCallback(this);
    }




    /**
     * 切换相机
     */
    public void changeCamera() {
        if (_Client != null && _Client.hasSession()) {
            if (mIsRecording) {
                mVideoStream.stopMediaCodec();
            }
            _Client.nextCamera();
            if (mIsRecording) {
                mVideoStream.setMirrored(_Client.isFrontCamera());
                mVideoStream.startMediaCodec();
            }
        }
    }

    /**
     * 屏幕截图保存
     */
    public void cutScreen() {
        ImageUtils.catImage(LiveRecordActivity.this, flCover);
    }


    /**
     * 美颜开启关闭
     */
    boolean isBeauty = true;
    public void changeBeau() {
        if(null!=beautyRender) {
            isBeauty = !isBeauty;
            beautyRender.switchBeauty(isBeauty);
        }
    }






    /**
     * 直播结束
     * @param closeInfo
     */

    public boolean isFinish = false;
    private void closeLiving(String[] closeInfo) {
        isFinish = true;
        Map<String, String> params = new HashMap<String, String>();
        params.put("lid", live.getLid());
        params.put("online", closeInfo[2]);
        params.put("hots", ""+(Integer.parseInt(closeInfo[0])+addHotByMe));
        params.put("liveTime", closeInfo[3]);
        params.put("loves", ""+(Integer.parseInt(closeInfo[1])+addLoveByMe));

        params.put("type", "0");
        params.put("chatroom", live.getChatroom());
        params.put("nickName", null!=App.getLoginUser()?App.getLoginUser().getNickName():"");

        params.put("photo", null!=App.getLoginUser()?App.getLoginUser().getPhoto():"");

        final LiveMsg msg = new LiveMsg();
        msg.setOnline(closeInfo[2]);
        msg.setAddhot(""+(Integer.parseInt(closeInfo[0])+addHotByMe));
        msg.setLove(""+(Integer.parseInt(closeInfo[1])+addLoveByMe));
        msg.setLiveTime(closeInfo[3]);
        msg.setPhoto(null!=App.getLoginUser()?App.getLoginUser().getPhoto():"");
        msg.setNickName(null!=App.getLoginUser()?App.getLoginUser().getNickName():"");

        RequestUtils.sendPostRequest(Api.LIVEING_CLOSE, params, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String str) {
                super.onSuccess(str);
                showCloseInfo(msg);

            }


            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, String.class);
    }

    public void showCloseInfo(LiveMsg msg) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("onLine",msg.getOnline());
        map.put("addhot",msg.getAddhot());
        map.put("love",msg.getLove());
        map.put("liveTime",msg.getLiveTime());
        map.put("photo",msg.getPhoto());
        map.put("nickName",msg.getNickName());
        ForwardUtils.target(LiveRecordActivity.this,Constant.LIVERECORD_FINISH,map);
    }






    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog();
            return false;
        }
        return false;

    }


    public void closeLiving() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LiveRecordActivity.this);
        builder.setMessage("确定要退出直播吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("退出",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        stopRecorder();
                        //人气，爱心，在线人数，播放时长
                        String[] closeInfo = liveView.getCloseInfo();
                        closeLiving(closeInfo);
                    }
                });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }


    protected void dialog() {

        if(startRecord==2 && showFinishWindow==false){
            AlertDialog.Builder builder = new AlertDialog.Builder(LiveRecordActivity.this);
            builder.setMessage("确定要退出直播吗?");
            builder.setTitle("提示");
            builder.setPositiveButton("退出",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            stopRecorder();
                            //人气，爱心，在线人数，播放时长
                            String[] closeInfo = liveView.getCloseInfo();
                            closeLiving(closeInfo);
                        }
                    });
            builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }else if(showFinishWindow){

        }else{
            onBackPressed();
        }

    }

    private boolean showFinishWindow = false;


    @Override
    public void onBackPressed() {
        if(null!=live){
            App.imClient.quitChatRoom(live.getChatroom(),new RongIMClient.OperationCallback(){

                @Override
                public void onSuccess() {
                    Log.i("rongyun","exit"+live.getChatroom()+"");
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }

        if(null!=_Client){
            _Client.stopPreview();
            _Client.onDestroy();
        }

        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        finish();
    }


    @Override
    protected void onRestart() {
        Log.i("myTest","onRestart");
        super.onRestart();
        outhAndStartPush();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRecorder();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _Client.stopPreview();
        _Client = null;
        EventBus.getDefault().unregister(this);
        Log.i("eventBus","反注册eventBus");
    }

    public void setIsLocal(int isLocal) {
        this.isLocal = isLocal;
    }


    private int addHotByMe = 0;
    public void updateHotsByMeSend(String hots) {
        addHotByMe= addHotByMe+Integer.parseInt(hots);
    }
    private int addLoveByMe = 0;
    public void updateLovesByMeSend(String loves) {
        addLoveByMe = addLoveByMe+Integer.parseInt(loves);
    }
}