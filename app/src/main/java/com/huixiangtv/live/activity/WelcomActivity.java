package com.huixiangtv.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Ad;
import com.huixiangtv.live.model.Getglobalconfig;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.utils.image.ImageUtils;

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
        getGlobalConfig();
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

                            Intent intent = new Intent(WelcomActivity.this, MainActivity.class);
                            WelcomActivity.this.startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(ServiceException e) {
                    super.onFailure(e);
                    Toast.makeText(WelcomActivity.this, "当有网络不可用，加载信息失败", Toast.LENGTH_LONG).show();

                }
            }, Getglobalconfig.class);
        } catch (Exception ex) {
            Toast.makeText(WelcomActivity.this, "加载异常", Toast.LENGTH_LONG).show();
        }
    }



}
