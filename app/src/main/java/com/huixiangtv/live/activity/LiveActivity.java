package com.huixiangtv.live.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.ijk.widget.media.IjkVideoView;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CenterLoadingView;
import com.huixiangtv.live.ui.LiveView;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.MeizuSmartBarUtils;
import com.huixiangtv.live.utils.ShareSdk;
import com.huixiangtv.live.utils.StringUtil;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import io.rong.imlib.RongIMClient;
import simbest.com.sharelib.IShareCallback;
import simbest.com.sharelib.ShareModel;
import simbest.com.sharelib.ShareUtils;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class LiveActivity extends BaseBackActivity{



    private static final String TAG = "LiveActivity";

    @ViewInject(R.id.flCover)
    FrameLayout flCover;
    @ViewInject(R.id.flPlayView)
    FrameLayout flPlayView;



    private String isPlay = "false";
    private String playUrl = "";
    private String lid = "";
    private IjkVideoView mVideoView;


    View playView;
    LiveView liveView;

    private Live live;
    private int sharePlat = 0;
    UMShareAPI mShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        x.view().inject(this);
        setStatusBar();
        mShareAPI = UMShareAPI.get(this);

        playUrl = getIntent().getStringExtra("playUrl");
        lid = getIntent().getStringExtra("lid");
        initPlayView();
    }

    private void setStatusBar() {
        if (MeizuSmartBarUtils.hasSmartBar()) {
            View decorView = getWindow().getDecorView();
            MeizuSmartBarUtils.hide(decorView);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
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
                if(null!=data){
                    live = data;
                    if(data.getLiveStatus().equals("1")){
                        living(data);
                        initPlayer();
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
            }
        }, Live.class);





    }


    private void initPlayer() {
        playView = LayoutInflater.from(LiveActivity.this).inflate(R.layout.play_view, null, false);

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
                switch (what) {
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //开始缓冲
                        if(null==loadingDialog){
                            loadingDialog = new CenterLoadingView(LiveActivity.this);
                        }
                        loadingDialog.setCancelable(true);
                        loadingDialog.setTitle("正在连接");
                        loadingDialog.show();

                        break;
                    case IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        //媒体视频开始渲染
                        break;
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        mVideoView.setAlpha(1f);
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
        mVideoView.setAlpha(0f);
        mVideoView.setBackgroundColor(R.color.mainColor);
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





    private void living(Live data) {
        liveView = new LiveView(LiveActivity.this);
        liveView.setActivity(LiveActivity.this);
        liveView.setInfo(data);
        if (StringUtil.isNotEmpty(isPlay) && isPlay.equals("true")) {
            liveView.isSendIntoRoomMsg(true);
        }
        flCover.addView(liveView);
        liveView.loadLive();



        ShareModel model = new ShareModel();
        UMImage image = new UMImage(LiveActivity.this, live.getPhoto());
        model.setTitle(live.getNickName()+live.getTitle());
        model.setTargetUrl(Api.SHARE_URL+live.getLid());
        model.setImageMedia(image);
        model.setContent(live.getNickName()+live.getTitle());
        if(sharePlat==1){
            CommonHelper.share(LiveActivity.this,model,SHARE_MEDIA.SMS,0,null);
        }else if(sharePlat==2){
            CommonHelper.share(LiveActivity.this,model,SHARE_MEDIA.QQ,0,null);
        }else if(sharePlat==3){
            CommonHelper.share(LiveActivity.this,model,SHARE_MEDIA.QZONE,0,null);
        }else if(sharePlat==4){
            CommonHelper.share(LiveActivity.this,model,SHARE_MEDIA.WEIXIN,0,null);
        }else if(sharePlat==5){
            CommonHelper.share(LiveActivity.this,model,SHARE_MEDIA.WEIXIN_CIRCLE,0,null);
        }else if(sharePlat==6){
            CommonHelper.share(LiveActivity.this,model,SHARE_MEDIA.SINA,0,null);
        }
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(LiveActivity.this,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LiveActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(LiveActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(LiveActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }


    /**
     * 屏幕截图保存
     */
    public void cutScreen() {
        ImageUtils.catImage(LiveActivity.this);
    }




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
        initPlayer();

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
        Log.i("myTest","onStop");
        super.onStop();
        if (null != mVideoView) {
            if (!mVideoView.isBackgroundPlayEnabled()) {
                mVideoView.stopPlayback();
                mVideoView.stopBackgroundPlay();
            } else {
                mVideoView.enterBackground();
            }
            flPlayView.removeView(playView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mVideoView) {
            if (!mVideoView.isBackgroundPlayEnabled()) {
                mVideoView.stopPlayback();
                mVideoView.stopBackgroundPlay();
            } else {
                mVideoView.enterBackground();
            }
            liveView.removeGlobalListener();
            liveView.removeMsgListener();
            flPlayView.removeView(playView);
            flCover.removeView(liveView);
        }
    }




    public void ijkFinish() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                onBackPressed();
            }
        }, 1000);
    }
}