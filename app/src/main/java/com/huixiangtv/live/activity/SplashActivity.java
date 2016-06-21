package com.huixiangtv.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.huixiangtv.live.R;
import com.oneapm.agent.android.OneApmAgent;

public class SplashActivity extends Activity{


    ImageView rlSplash = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        rlSplash = (ImageView) findViewById(R.id.rlSplash);

        OneApmAgent.init(this.getApplicationContext()).setToken("2E961902283DF7ACC50EE66AFA93699B27").start();

        new Handler().postDelayed(new Runnable(){
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                SplashActivity.this.finish();
            }
        }, 2000);
    }


}
