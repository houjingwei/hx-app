package com.huixiangtv.live.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
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
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.ui.LiveView;
import com.huixiangtv.live.ui.StartLiveView;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.KeyBoardUtils;
import com.huixiangtv.live.utils.MeizuSmartBarUtils;
import com.huixiangtv.live.utils.StringUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class LiveActivity extends BaseBackActivity implements View.OnClickListener ,LiveRecorderManager.OnStatusCallback{

    private static final String TAG = "LiveActivity";
    @ViewInject(R.id.flCover)
    FrameLayout flCover;
    @ViewInject(R.id.flPlayView)
    FrameLayout flPlayView;



    TextView tvTheme;
    TextView tvStart;
    StartLiveView startLiveView;

    LiveView liveView;



    private String isPlay = "false";
    private String playUrl = "";
    private String lid = "";
    private IjkVideoView mVideoView;
    private ColaProgress  copro = null;

    private View recordView;


    private Live live;

    private int sharePlat = 0;
    private String pushUrl;
    private String isRecord = "false";

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

    private void startPush() {
        if (null != Constant.accessToken){
            try {
                LiveService.getInstance().createLive(Constant.accessToken, Constant.SPACE, Constant.LIVE_URL);
                LiveService.getInstance().setCreateLiveListener(new CreateLiveListener() {
                    @Override
                    public void onCreateLiveError(int errorCode, String message) {
                        Log.e("live", "errorCode:" + errorCode + "message" + message);
                    }

                    @Override
                    public void onCreateLiveSuccess(String pushUrl, String playUrl) {
                        pushUrl = pushUrl;
                        startRecorder(live.getPushUrl(), playUrl);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e(TAG, ex.getMessage().toString());
            }
        }
    }

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

        final View playView = LayoutInflater.from(LiveActivity.this).inflate(R.layout.play_view, null, false);
        mVideoView = (IjkVideoView) playView.findViewById(R.id.video_view);
        mVideoView.setRender(2);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mVideoView.setVideoPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");
        mVideoView.setVideoURI(Uri.parse(playUrl));
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                copro = ColaProgress.show(LiveActivity.this, "连线中", false, true, null);
            }
        });
        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                CommonHelper.showTip(LiveActivity.this,"播放完成");
            }
        });
        mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                if(null!=copro){
                    copro.dismiss();
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


    private void initStartView() {
        startLiveView = new StartLiveView(this);
        startLiveView.setActivity(this);
        flCover.addView(startLiveView);
        tvTheme = startLiveView.getTvTheme();
        tvStart = startLiveView.getTvStart();
        tvTheme.setOnClickListener(this);
        tvStart.setOnClickListener(this);
        KeyBoardUtils.closeKeybord(startLiveView.getEtTitle(), this);
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
     * 请求开始直播
     */
    private ColaProgress cp = null;
    private void toLive() {
        final Map<String, String> params = new HashMap<String, String>();
        if (TextUtils.isEmpty(startLiveView.getEtTitle().getText().toString())) {
            CommonHelper.showTip(LiveActivity.this, "请输入直播标题");
            startLiveView.getEtTitle().requestFocus();
            return;
        } else if (startLiveView.getTvTheme().getText().toString().equals(R.string.selTheme)) {
            CommonHelper.showTip(LiveActivity.this, "请选择一个话题");
            return;
        }
        params.put("title", startLiveView.getEtTitle().getText().toString());
        params.put("topic", startLiveView.getTvTheme().getText().toString());
        String[] jwd = startLiveView.getJwd();
        params.put("lon", jwd[0]);
        params.put("lat", jwd[1]);
        params.put("address", jwd[2]);
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

        //初始化美颜
        beautyRender = BeautyRender.getInstance();
        beautyRender.initRenderer(getAssets(),_Client);
        beautyRender.switchBeauty(true);

        flPlayView.addView(recordView);
        if(null!=recordView){
            //开始预览
            _Client.startPreview();
            if (_SurfaceControl != null) {
                _SurfaceControl.setVisible(true);
            }
        }


    }


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
    private final CompoundButton.OnCheckedChangeListener _SwitchBeautyOnCheckedChange =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    beautyRender.switchBeauty(isChecked);
                }
            };

    private final CompoundButton.OnCheckedChangeListener _CameraOnCheckedChange =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
            };

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


    @Override
    public void onNetworkStatusChange(int status) {
        switch (status) {
            case LiveStreamStatus.CONNECTION_START:
                Log.i(TAG, "Start live stream connection!");
                break;
            case LiveStreamStatus.CONNECTION_ESTABLISHED:
                Log.i(TAG, "Live stream connection is established!");
                break;
            case LiveStreamStatus.CONNECTION_CLOSED:
                Log.i(TAG, "Live stream connection is closed!");
                mLiveRecorder.release();
                mLiveRecorder = null;
                break;
            case LiveStreamStatus.CONNECTION_ERROR_IO:
            case LiveStreamStatus.CONNECTION_ERROR_MEM:
            case LiveStreamStatus.CONNECTION_ERROR_TIMEOUT:
            case LiveStreamStatus.CONNECTION_ERROR_BROKENPIPE:
            case LiveStreamStatus.CONNECTION_ERROR_INVALIDARGUMENT:
            case LiveStreamStatus.CONNECTION_ERROR_NETWORKUNREACHABLE:
                Log.i(TAG, "Live stream connection error-->" + status);
                if (mIsRecording) {
                    mLiveRecorder.reconnect(null);
                } else {
                    mLiveRecorder.release();
                    mLiveRecorder = null;
                }
                break;
            case LiveStreamStatus.CONNECTION_ERROR_AUTH:
                Log.i(TAG, "Live stream connection auth failure!");
                if (mIsRecording) {
                    stopRecorder();
                }
                mLiveRecorder.release();
                mLiveRecorder = null;

                /* You can fetch another auth and start new live recorder here */
                break;
            default:
                Log.i(TAG, "Live stream connection unexpected error-->" + status);
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
    private void stopRecorder() {
        if (mIsRecording) {
            Log.i(TAG, "Stop live stream stopRecord!");
            mLiveRecorder.stop();
            mIsRecording = false;
        }
    }
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
            _Client = null;
        }
    };

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
            CommonHelper.share(LiveActivity.this,live.getNickName()+live.getTitle(),live.getTopic()+"正在回响直播，赶紧来捧场吧",SHARE_MEDIA.SMS,live.getPhoto(),"http://h5.huixiangtv.com/live/"+live.getLid(),back);
        }else if(sharePlat==2){
            CommonHelper.share(LiveActivity.this,live.getNickName()+live.getTitle(),live.getTopic()+"正在回响直播，赶紧来捧场吧",SHARE_MEDIA.QQ,live.getPhoto(),"http://h5.huixiangtv.com/live/"+live.getLid(),back);
        }else if(sharePlat==3){
            CommonHelper.share(LiveActivity.this,live.getNickName()+live.getTitle(),live.getTopic()+"正在回响直播，赶紧来捧场吧",SHARE_MEDIA.QZONE,live.getPhoto(),"http://h5.huixiangtv.com/live/"+live.getLid(),back);
        }else if(sharePlat==4){
            CommonHelper.share(LiveActivity.this,live.getNickName()+live.getTitle(),live.getTopic()+"正在回响直播，赶紧来捧场吧",SHARE_MEDIA.WEIXIN,live.getPhoto(),"http://h5.huixiangtv.com/live/"+live.getLid(),back);
        }else if(sharePlat==5){
            CommonHelper.share(LiveActivity.this,live.getNickName()+live.getTitle(),live.getTopic()+"正在回响直播，赶紧来捧场吧",SHARE_MEDIA.WEIXIN_CIRCLE,live.getPhoto(),"http://h5.huixiangtv.com/live/"+live.getLid(),back);
        }else if(sharePlat==6){
            CommonHelper.share(LiveActivity.this,live.getNickName()+live.getTitle(),live.getTopic()+"正在回响直播，赶紧来捧场吧",SHARE_MEDIA.SINA,live.getPhoto(),"http://h5.huixiangtv.com/live/"+live.getLid(),back);
        }

    }

    ApiCallback back = new ApiCallback<String>(){

        @Override
        public void onSuccess(String data) {
            startPush();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && requestCode == 1) {
                    String tid = data.getStringExtra("tid");
                    String topic = data.getStringExtra("topic");
                    tvTheme.setText("# " + topic + " #");
                }
                break;

            default:
                break;
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != liveView) {
            liveView.removeGlobalListener();
            liveView.removeMsgListener();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        
        if (StringUtil.isNotEmpty(isRecord) && isRecord.equals("true")) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //人气，爱心，在线人数，播放时长
                    String[] closeInfo = liveView.getCloseInfo();
                    closeLiving(closeInfo);
                }
            }, 1000);


        }
        if(null!=recordView){
            //停止推流
            stopRecorder();
            _Client.stopPreview();
            _Client.onDestroy();

            //停止直播
            stopLive();

        }

        flPlayView.removeAllViews();
        if (null != mVideoView) {
            if (!mVideoView.isBackgroundPlayEnabled()) {
                mVideoView.stopPlayback();
                mVideoView.stopBackgroundPlay();
            } else {
                mVideoView.enterBackground();
            }

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
        RequestUtils.sendPostRequest(Api.LIVEING_CLOSE, params, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String str) {
                super.onSuccess(str);

            }


            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, String.class);
    }

    private void stopLive() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mVideoView) {
            if (!mVideoView.isBackgroundPlayEnabled()) {
                mVideoView.stopPlayback();
                mVideoView.stopBackgroundPlay();
            } else {
                mVideoView.enterBackground();
            }
        }

        //停止推流
        stopRecorder();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mVideoView) {
            mVideoView.start();
        }

    }




}
