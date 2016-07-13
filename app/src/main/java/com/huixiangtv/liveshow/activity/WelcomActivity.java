package com.huixiangtv.liveshow.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.Ad;
import com.huixiangtv.liveshow.model.Getglobalconfig;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.utils.image.ImageUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stone on 16/6/24.
 */
public class WelcomActivity extends Activity{

    private TextView tv_oncie;

    private boolean isyzm = true;
    private MyCount mc;
    private ImageView imUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        tv_oncie = (TextView) findViewById(R.id.tv_oncie);
        imUrl = (ImageView) findViewById(R.id.ivUrl);
        //getGlobalConfig();
         String type =  getIntent().getStringExtra("type");
        if(null!=type && type.trim().length()>0)
        {

        }

    }


    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

            isyzm = true;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int i = (int) millisUntilFinished / 1000;
            tv_oncie.setText(  i + " 跳过 >)");

        }
    }

    private void getGlobalConfig() {

        try {
            final String version = App.getVersionCode(WelcomActivity.this);
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
                            ImageUtils.display(imUrl,adEntity.getImage());
                            imUrl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //doing
                                }
                            });
                            if (mc == null) {
                                mc = new MyCount(adEntity.getShowtime(), 1000);
                            }
                            mc.start();
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
            Toast.makeText(WelcomActivity.this, "加载异常", Toast.LENGTH_LONG).show();
        }
    }


    private void gotoMain(){

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(WelcomActivity.this, MainActivity.class);
                WelcomActivity.this.startActivity(intent);
                WelcomActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                WelcomActivity.this.finish();
            }
        }, 2000);
    }


}
