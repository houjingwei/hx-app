package com.huixiangtv.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.model.UpgradeLevel;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.UpdateApp;
import com.oneapm.agent.android.OneApmAgent;

import com.huixiangtv.live.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends Activity implements Animation.AnimationListener {


    RelativeLayout rlSplash = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rlSplash = (RelativeLayout) findViewById(R.id.rlSplash);
        startAnim();
        OneApmAgent.init(this.getApplicationContext()).setToken("B3747B7350941C879DB5765C388AA59D73").start();
    }

    private void startAnim() {
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setAnimationListener(this);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,1);
        alphaAnimation.setDuration(3000);
        //保持动画状态
        alphaAnimation.setFillAfter(true);
        animationSet.addAnimation(alphaAnimation);
        rlSplash.startAnimation(animationSet);
    }





    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        SplashActivity.this.finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


}
