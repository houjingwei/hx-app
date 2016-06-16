package com.huixiangtv.live.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.duanqu.qupai.android.camera.CameraClient;
import com.duanqu.qupai.android.camera.CameraSurfaceController;
import com.duanqu.qupai.android.camera.SessionRequest;
import com.duanqu.qupai.live.CreateLiveListener;
import com.duanqu.qupai.live.LiveAudioStream;
import com.duanqu.qupai.live.LiveRecorderManager;
import com.duanqu.qupai.live.LiveService;
import com.duanqu.qupai.live.LiveStreamStatus;
import com.duanqu.qupai.live.LiveVideoStream;
import com.duanqu.qupai.utils.MathUtil;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Beauty.BeautyRender;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.ijk.widget.media.IjkVideoView;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.model.LiveMsg;
import com.huixiangtv.live.pop.LivingFinishWindow;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CenterLoadingView;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.ui.LiveView;
import com.huixiangtv.live.ui.StartLiveView;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.KeyBoardUtils;
import com.huixiangtv.live.utils.MeizuSmartBarUtils;
import com.huixiangtv.live.utils.StringUtil;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class LiveActivity extends Activity implements View.OnClickListener ,LiveRecorderManager.OnStatusCallback{



    private static final String TAG = "LiveActivity";

    @ViewInject(R.id.flCover)
    FrameLayout flCover;
    @ViewInject(R.id.flPlayView)
    FrameLayout flPlayView;


    private View recordView;



    TextView tvTheme;
    TextView tvStart;
    StartLiveView startLiveView;

    private String isPlay = "false";
    private String playUrl = "";
    private String lid = "";
    private IjkVideoView mVideoView;
    private ColaProgress  copro = null;


    LiveView liveView;

    private Live live;
    private int sharePlat = 0;
    private int isLocal = 1;
    private String pushUrl;
    private String isRecord = "false";

    private int startRecord = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        x.view().inject(this);
        if (MeizuSmartBarUtils.hasSmartBar()) {
            View decorView = getWindow().getDecorView();
            MeizuSmartBarUtils.hide(decorView);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        isPlay = getIntent().getStringExtra("isPlay");
        isRecord = getIntent().getStringExtra("isRecord");
        if (StringUtil.isNotEmpty(isPlay) && isPlay.equals("true")) {
            playUrl = getIntent().getStringExtra("playUrl");
            lid = getIntent().getStringExtra("lid");
            initPlayView();
        } else {
            addRecordView();
            initStartView();

        }

    }


    CenterLoadingView loadingDialog = null;
    private void initPlayView() {
        final Map<String, String> param = new HashMap<String, String>();
        param.put("lid", lid);
        RequestUtils.sendPostRequest(Api.LIVEINFO, param, new ResponseCallBack<Live>() {
            @Override
            public void onSuccess(Live data) {
                super.onSuccess(data);
                if (null != cp) {
                    cp.dismiss();
                }
                if(null!=data){
                    if(data.getLiveStatus().equals("1")){
                        living(data);
                    }else if(data.getLiveStatus().equals("0")){
                        CommonHelper.showTip(LiveActivity.this,"直播已结束");
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                onBackPressed();
                            }
                        }, 1000);
                    }else if(data.getLiveStatus().equals("-1")){
                        CommonHelper.showTip(LiveActivity.this,"直播被禁止，不可观看");
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                onBackPressed();
                            }
                        }, 1000);
                    }

                }

            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                if (null != cp) {
                    cp.dismiss();
                }
            }
        }, Live.class);

        final View playView = LayoutInflater.from(LiveActivity.this).inflate(R.layout.play_view, null, false);
        mVideoView = (IjkVideoView) playView.findViewById(R.id.video_view);
        mVideoView.setActivity(LiveActivity.this);
        mVideoView.setRender(2);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mVideoView.setVideoPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");
        mVideoView.setVideoURI(Uri.parse(playUrl));
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {

            }
        });

        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                CommonHelper.showTip(LiveActivity.this,"播放完成");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        onBackPressed();
                    }
                }, 1000);
            }
        });
        mVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                CommonHelper.showTip(LiveActivity.this,"直播错误");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        onBackPressed();
                    }
                }, 1000);
                return false;
            }
        });
        mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                Log.i("ijkplayer_status","setOnInfoListener ****"+what+"*****");
                switch (what) {
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //开始缓冲
                        if(isFinish==false){
                            if(null==loadingDialog){
                                loadingDialog = new CenterLoadingView(LiveActivity.this);
                            }
                            loadingDialog.setCancelable(true);
                            loadingDialog.setTitle("正在连接");
                            loadingDialog.show();
                        }
                        break;
                    case IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        //媒体视频开始渲染
                        break;
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        if(null!=loadingDialog){
                            loadingDialog.dismiss();
                        }
                        //缓冲结束
                        break;
                }
                return false;
            }
        });

        mVideoView.start();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mVideoView.getLayoutParams();
        params.height = App.screenHeight;
        params.width = App.screenWidth;
        mVideoView.setLayoutParams(params);



        flPlayView.addView(playView);



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
        startRecord = 1;
        startLiveView = new StartLiveView(this);
        startLiveView.setActivity(this);
        flCover.addView(startLiveView);
        tvTheme = startLiveView.getTvTheme();
        tvStart = startLiveView.getTvStart();
        tvTheme.setOnClickListener(this);
        tvStart.setOnClickListener(this);
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
        recordView = LayoutInflater.from(LiveActivity.this).inflate(R.layout.record_view, null, false);
        _CameraSurface = (SurfaceView) recordView.findViewById(R.id.camera_surface);
        _CameraSurface.getHolder().addCallback(_CameraSurfaceCallback);
        _CameraSurface.setOnTouchListener(mOnTouchListener);

        //对焦
        mDetector = new GestureDetector(_CameraSurface.getContext(), mGestureDetector);
        //缩放
        mScaleDetector = new ScaleGestureDetector(_CameraSurface.getContext(), mScaleGestureListener);

        _Client = new CameraClient();
        _Client.setCallback(mCameraClientCallback);//设置摄像头开启的回调
        _Client.setCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT);//设置摄像头为前置摄像头
        _Client.setContentSize(384, 640);//设置摄像头分辨率，这里建议与VideoStream设置同样尺寸，否则会产生黑边。VideoStream设置参考【视频流设置】

        try{
            //初始化美颜
            beautyRender = BeautyRender.getInstance();
            beautyRender.initRenderer(getAssets(),_Client);
            beautyRender.switchBeauty(true);
        }catch(Exception e){
            e.printStackTrace();
        }

        flPlayView.addView(recordView);
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

            if (!_Client.autoFocus(x, y, _SurfaceControl)) {
                return false;
            }

            return true;
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
            Log.i("myTest","surfaceCreated");
            holder.setKeepScreenOn(true);

            _SurfaceControl = _Client.addSurface(holder);
            _SurfaceControl.setVisible(true);
            _SurfaceControl.setDisplayMethod(CameraSurfaceController.FullScreen | CameraSurfaceController.ScaleEnabled);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i("myTest","surfaceChanged");
            _SurfaceControl.setResolution(width, height);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.i("myTest","surfaceDestroyed");
            _Client.removeSurface(holder);
            _SurfaceControl = null;
        }
    };





    int lineCount = 0;
    @Override
    public void onNetworkStatusChange(int status) {
        android.os.Message msg = new android.os.Message();
        switch (status) {
            case LiveStreamStatus.CONNECTION_START:
                break;
            case LiveStreamStatus.CONNECTION_ESTABLISHED:
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
                    mLiveRecorder.reconnect(null);
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
                    mLiveRecorder.reconnect(null);
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
                    if(isFinish==false && lineCount==0){
                        lineCount++;
                        if(null==loadingDialog ){
                            loadingDialog = new CenterLoadingView(LiveActivity.this);
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
                ForwardUtils.target(LiveActivity.this, Constant.LIVE_TOPIC, null);
                break;
            case R.id.tvStart:
                Map<String, String> params = new HashMap<String, String>();
                params.put("local", startLiveView.getLocal() + "");
                params.put("platform", startLiveView.getPlatform() + "");
                //请求开始直播
                toLive();
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
        params.put("topic", startLiveView.getTvTheme().getText().toString());
        if(isLocal==1){
            String[] jwd = startLiveView.getJwd();
            params.put("lon", jwd[0]);
            params.put("lat", jwd[1]);
            params.put("address", jwd[2]);
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
        cp = ColaProgress.show(LiveActivity.this, "准备直播", false, true, null);
        RequestUtils.sendPostRequest(Api.LIVE_SHOW, params, new ResponseCallBack<Live>() {
            @Override
            public void onSuccess(Live data) {
                super.onSuccess(data);
                if (null != cp) {
                    cp.dismiss();
                }
                live =data;
                startPush();
                living(data);
                ObjectAnimator animIn = ObjectAnimator.ofFloat(startLiveView, "alpha", 1f);
                animIn.setDuration(500);
                animIn.start();
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
        liveView = new LiveView(LiveActivity.this);
        liveView.setActivity(LiveActivity.this);
        liveView.setInfo(data);
        if (StringUtil.isNotEmpty(isPlay) && isPlay.equals("true")) {
            liveView.isSendIntoRoomMsg(true);
        }
        flCover.addView(liveView);
        liveView.loadLive();
        if(sharePlat==1){
            CommonHelper.share(LiveActivity.this,live.getNickName()+live.getTitle(),live.getTopic()+"正在回响直播，赶紧来捧场吧",SHARE_MEDIA.SMS,live.getPhoto(),"http://119.29.94.122:8888/h5/index.html?uid=&lid="+live.getLid(),0,null);
        }else if(sharePlat==2){
            CommonHelper.share(LiveActivity.this,live.getNickName()+live.getTitle(),live.getTopic()+"正在回响直播，赶紧来捧场吧",SHARE_MEDIA.QQ,live.getPhoto(),"http://119.29.94.122:8888/h5/index.html?uid=&lid="+live.getLid(),0,null);
        }else if(sharePlat==3){
            CommonHelper.share(LiveActivity.this,live.getNickName()+live.getTitle(),live.getTopic()+"正在回响直播，赶紧来捧场吧",SHARE_MEDIA.QZONE,live.getPhoto(),"http://119.29.94.122:8888/h5/index.html?uid=&lid="+live.getLid(),0,null);
        }else if(sharePlat==4){
            CommonHelper.share(LiveActivity.this,live.getNickName()+live.getTitle(),live.getTopic()+"正在回响直播，赶紧来捧场吧",SHARE_MEDIA.WEIXIN,live.getPhoto(),"http://119.29.94.122:8888/h5/index.html?uid=&lid="+live.getLid(),0,null);
        }else if(sharePlat==5){
            CommonHelper.share(LiveActivity.this,live.getNickName()+live.getTitle(),live.getTopic()+"正在回响直播，赶紧来捧场吧",SHARE_MEDIA.WEIXIN_CIRCLE,live.getPhoto(),"http://119.29.94.122:8888/h5/index.html?uid=&lid="+live.getLid(),0,null);
        }else if(sharePlat==6){
            CommonHelper.share(LiveActivity.this,live.getNickName()+live.getTitle(),live.getTopic()+"正在回响直播，赶紧来捧场吧",SHARE_MEDIA.SINA,live.getPhoto(),"http://119.29.94.122:8888/h5/index.html?uid=&lid="+live.getLid(),0,null);
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && requestCode == 1) {
                    String tid = data.getStringExtra("tid");
                    String topic = data.getStringExtra("topic");
                    tvTheme.setText( topic);
                }
                break;

            default:
                break;
        }
    }


    private void startPush() {
        if (null != Constant.accessToken){
            try {
                if(null!=live && StringUtil.isNotEmpty(live.getPushUrl())) {
                    LiveService.getInstance().createLive(Constant.accessToken, Constant.SPACE, Constant.LIVE_URL);
                    LiveService.getInstance().setCreateLiveListener(new CreateLiveListener() {
                        @Override
                        public void onCreateLiveError(int errorCode, String message) {
                            Log.e("myTest", "errorCode:" + errorCode + "message" + message);
                        }

                        @Override
                        public void onCreateLiveSuccess(String pushUrl, String playUrl) {
                            pushUrl = pushUrl;
                            Log.e("myTest", "startPush");
                            startRecorder(live.getPushUrl(), playUrl);
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e(TAG, ex.getMessage().toString());
            }
        }
    }


    private void startRecorder(String pushUrl,String playUrl) {
        if (mLiveRecorder != null) {
            return;
        }

        Log.i(TAG, "************** Start live stream startRecord! ************** ");
        initLiveRecord();
        //ip_address这里修改为可以直接ip推流.就近原则。建议使用httpDNS得到最优ip.直接推流.不用即直接传null
        mLiveRecorder.start(this,null);
        mIsRecording = true;
        Log.i(TAG, "************** Starting live stream! **************");
    }

    private void initLiveRecord() {
        mLiveRecorder = new LiveRecorderManager();
        mLiveRecorder.init(live.getPushUrl(), "flv");//flv为推流文件格式，目前仅支持flv
        mLiveRecorder.setNetworkThreshHold(90);//设置网络最大buffer阈值.可不设，默认为90
        mVideoStream = mLiveRecorder.addVideoStream();
        mVideoStream.init(384, 640, 600000, 20, 3);//参数分别为：宽、高、码率、帧数、帧数间隔；宽384为推荐设置，可解决部分手机不支持16倍数的问题
        mVideoStream.setInput(_Client);
        mVideoStream.setMirrored(_Client.isFrontCamera());
        mVideoStream.setBitRateRange(600000,1000000);//设置最小码率和最大码率，可根据网络状况自动调节码率.会自动调节码率
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
        //savePic(takeScreenShot(LiveActivity.this), "sdcard/xx.png");

        ImageUtils.catImage(LiveActivity.this);
    }


    /**
     * 美颜开启关闭
     */
    boolean isBeauty = true;
    public void changeBeau() {
        if(null!=beautyRender) {
            isBeauty = !isBeauty;
            Log.i("qupai",isBeauty+"");
            beautyRender.switchBeauty(isBeauty);
        }
    }






    /**
     * 直播结束
     * @param closeInfo
     */
    private void closeLiving(String[] closeInfo) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("lid", live.getLid());
        params.put("online", closeInfo[2]);
        params.put("hots", closeInfo[0]);
        params.put("liveTime", closeInfo[3]);
        params.put("loves", closeInfo[1]);

        params.put("type", "0");
        params.put("chatroom", live.getChatroom());
        params.put("nickName", null!=App.getLoginUser()?App.getLoginUser().getNickName():"");

        params.put("photo", null!=App.getLoginUser()?App.getLoginUser().getPhoto():"");

        final LiveMsg msg = new LiveMsg();
        msg.setOnline(closeInfo[2]);
        msg.setAddhot(closeInfo[0]);
        msg.setLove(closeInfo[1]);
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
        Log.i("status_status",showFinishWindow+"");
        showFinishWindow = true;
        Log.i("status_status",showFinishWindow+"");
        LivingFinishWindow selectPicWayWindow = new LivingFinishWindow(LiveActivity.this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,msg,live);
        selectPicWayWindow.showAtLocation(LiveActivity.this.findViewById(R.id.liveMain), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        selectPicWayWindow.update();
        selectPicWayWindow.setListener(new LivingFinishWindow.CloseListener() {
            @Override
            public void select() {
                LiveActivity.this.onBackPressed();
            }
        });

    }






    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                isFinish = true;
                dialog();
                return false;
            }
        return false;

    }


    public void closeLiving() {
        isFinish = true;
        if(null!=loadingDialog){
            loadingDialog.dismiss();
        }
        if (StringUtil.isNotEmpty(isRecord) && isRecord.equals("true") && null!=liveView) {
            stopRecorder();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //人气，爱心，在线人数，播放时长
                    String[] closeInfo = liveView.getCloseInfo();
                    closeLiving(closeInfo);
                }
            }, 1000);
        }else{
            onBackPressed();
        }

    }


    protected void dialog() {

        if(StringUtil.isNotEmpty(isRecord) && isRecord.equals("true") && startRecord==2 && showFinishWindow==false){
            AlertDialog.Builder builder = new AlertDialog.Builder(LiveActivity.this);
            builder.setMessage("确定要退出直播吗?");
            builder.setTitle("提示");
            builder.setPositiveButton("退出",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            closeLiving();
                        }
                    });
            builder.setNegativeButton("取消",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            isFinish = false;
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
        if(null!=_Client){
            _Client.stopPreview();
            _Client.onDestroy();
        }

        if (null != mVideoView) {
            if (!mVideoView.isBackgroundPlayEnabled()) {
                mVideoView.stopPlayback();
                mVideoView.stopBackgroundPlay();
            } else {
                mVideoView.enterBackground();
            }
        }

        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        finish();
    }


    @Override
    protected void onRestart() {
        Log.i("myTest","onRestart");
        super.onRestart();
        if (null!=isRecord && isRecord.equals("true")) {
            startPush();
        }
    }

    @Override
    protected void onResume() {
        Log.i("myTest","onResume");
        super.onResume();
        if (null != mVideoView) {
            mVideoView.start();
        }
    }

    @Override
    protected void onPause() {
        Log.i("myTest","onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("myTest","onStop");
        super.onStop();
        stopRecorder();
        if (null != mVideoView) {
            if (!mVideoView.isBackgroundPlayEnabled()) {
                mVideoView.stopPlayback();
                mVideoView.stopBackgroundPlay();
            } else {
                mVideoView.enterBackground();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != liveView) {
            liveView.removeGlobalListener();
            liveView.removeMsgListener();
        }
    }


    public void setIsLocal(int isLocal) {
        this.isLocal = isLocal;
    }


    private boolean isFinish = false;
    public void setFinishLiving() {
        Log.i("myCloseclose","finish");
        if(null!=loadingDialog){
            loadingDialog.dismiss();
        }
        isFinish = true;
    }
}