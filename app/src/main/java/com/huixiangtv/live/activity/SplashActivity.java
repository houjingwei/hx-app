package com.huixiangtv.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.UpgradeLevel;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.UpdateApp;
import com.oneapm.agent.android.OneApmAgent;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends Activity{


    ImageView rlSplash = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        rlSplash = (ImageView) findViewById(R.id.rlSplash);
        CheckVersion();
        OneApmAgent.init(this.getApplicationContext()).setToken("2E961902283DF7ACC50EE66AFA93699B27").start();


    }


    public void gotoMainActivity()
    {

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                SplashActivity.this.finish();
            }
        }, 2000);

    }

    public void updateClose(){
        finish();
        App.getContext().finishAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());

    }

    /**
     * 检查新版本
     */
    private void CheckVersion() {
        try {
            final String version = App.getVersionCode(SplashActivity.this);
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("osType", "1");
            paramsMap.put("appVersion", version );

            RequestUtils.sendPostRequest(Api.UPGRADE_LEVEL, paramsMap, new ResponseCallBack<UpgradeLevel>() {

                public void onSuccess(UpgradeLevel data) {

                    if (data != null) {

                        UpgradeLevel upgradeLevel = data;

                        UpdateApp updateApp = new UpdateApp(SplashActivity.this, SplashActivity.this);
                        if (updateApp
                                .judgeVersion(upgradeLevel.alert, upgradeLevel.appUrl, upgradeLevel.desc)) {

                        }
                    }
                }

                
                @Override
                public void onFailure(ServiceException e) {
                    super.onFailure(e);
                    Toast.makeText(getBaseContext(), "当有网络不可用，检查更新失败", Toast.LENGTH_LONG).show();
                }
            }, UpgradeLevel.class);
        } catch (Exception ex) {
            Toast.makeText(SplashActivity.this,"更新异常",Toast.LENGTH_LONG).show();
        }

    }

}
