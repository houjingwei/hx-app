package com.huixiangtv.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Ad;
import com.huixiangtv.live.model.Getglobalconfig;
import com.huixiangtv.live.model.UpgradeLevel;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.UpdateApp;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.oneapm.agent.android.OneApmAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends Activity{


    ImageView rlSplash = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rlSplash = (ImageView) findViewById(R.id.rlSplash);
        OneApmAgent.init(this.getApplicationContext()).setToken("2E961902283DF7ACC50EE66AFA93699B27").start();
        getGlobalConfig();
    }


    private void getGlobalConfig() {
        try {
            final String version = App.getVersionCode(SplashActivity.this);
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("version", version);
            RequestUtils.sendPostRequest(Api.GETGLOBALCONFIG, paramsMap, new ResponseCallBack<Getglobalconfig>() {

                public void onSuccess(Getglobalconfig data) {

                    if (data != null) {
                        App.saveIndexStyle(data.getIndexStyle());

                        ArrayList<Ad> adArrayList =  data.getList_ad();
                        if(null!= adArrayList && adArrayList.size()>0)
                        {
                            Ad adEntity  = adArrayList.get(0);
                            Intent intent = new Intent(SplashActivity.this, WelcomActivity.class);
                            intent.putExtra("type",adEntity.getType());
                            intent.putExtra("url", adEntity.getUrl());
                            intent.putExtra("width",adEntity.getWidth());
                            intent.putExtra("height",adEntity.getHeight());
                            intent.putExtra("image",adEntity.getImage());
                            intent.putExtra("showtime",adEntity.getShowtime());
                            startActivity(intent);
                        }
                        else
                        {
                            gotoMain();
                        }
                    }
                }

                @Override
                public void onFailure(ServiceException e) {
                    super.onFailure(e);
                    gotoMain();
                }
            }, Getglobalconfig.class);
        } catch (Exception ex) {
            gotoMain();
        }
    }


    private void gotoMain(){

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                SplashActivity.this.finish();
            }
        }, 2000);
    }

}
