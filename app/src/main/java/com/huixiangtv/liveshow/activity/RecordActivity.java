package com.huixiangtv.liveshow.activity;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.duanqu.qupai.android.camera.AutoFocusCallback;
import com.duanqu.qupai.android.camera.CameraClient;
import com.duanqu.qupai.android.camera.CameraDevice;
import com.duanqu.qupai.android.camera.CameraSurfaceController;
import com.duanqu.qupai.android.camera.SessionRequest;
import com.duanqu.qupai.live.LiveAudioStream;
import com.duanqu.qupai.live.LiveRecorderManager;
import com.duanqu.qupai.live.LiveVideoStream;
import com.duanqu.qupai.utils.MathUtil;
import com.huixiangtv.liveshow.Beauty.BeautyRender;
import com.huixiangtv.liveshow.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class RecordActivity extends AppCompatActivity {
    @ViewInject(R.id.flCover)
    FrameLayout flCover;
    @ViewInject(R.id.flPlayView)
    FrameLayout flPlayView;
    private View recordView;
    private SurfaceView _CameraSurface;
    private CameraClient _Client;
    private CameraSurfaceController _SurfaceControl;

    private LiveRecorderManager mLiveRecorder;
    private LiveVideoStream mVideoStream;
    private LiveAudioStream mAudioStream;

    private boolean mIsRecording = false;

    private final float mMaxZoomLevel = 3;
    private BeautyRender beautyRender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        x.view().inject(this);
        addRecordView();

        findViewById(R.id.tvXt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCamera();
            }
        });
        findViewById(R.id.tvMy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBeau();
            }
        });
    }

    /**
     * 切换相机
     */
    public void changeCamera() {
        if (_Client != null && _Client.hasSession()) {
            if (mIsRecording) {
                //mVideoStream.stopMediaCodec();
            }
            _Client.nextCamera();
            if (mIsRecording) {
                //mVideoStream.setMirrored(_Client.isFrontCamera());
                //mVideoStream.startMediaCodec();
            }
        }
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


    private void addRecordView() {
        recordView = LayoutInflater.from(RecordActivity.this).inflate(R.layout.record_view, null, false);
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
}
