package com.huixiangtv.live.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.huixiangtv.live.R;
import com.huixiangtv.live.ijk.widget.media.IjkVideoView;


import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class MyActivity extends AppCompatActivity{
    private String mVideoPath;
    private Uri    mVideoUri;
    private IjkVideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mVideoView = (IjkVideoView) findViewById(R.id.video_view);

        mVideoView.setVideoPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");
        mVideoView.setVideoURI(Uri.parse("rtmp://live.hkstv.hk.lxdns.com/live/hks"));
        mVideoView.start();
        mVideoView.toggleAspectRatio();


    }


}
