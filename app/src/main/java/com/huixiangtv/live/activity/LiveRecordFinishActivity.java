package com.huixiangtv.live.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.utils.MeizuSmartBarUtils;
import com.huixiangtv.live.utils.image.ImageUtils;

public class LiveRecordFinishActivity extends BaseBackActivity{

    TextView tvNickname;
    TextView tvOnlineNum;
    TextView tvHot;
    TextView tvLove;
    TextView tvTime;
    ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_record_finish);
        if (MeizuSmartBarUtils.hasSmartBar()) {
            View decorView = getWindow().getDecorView();
            MeizuSmartBarUtils.hide(decorView);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        initView();
        initData();
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

    private void initData() {
        String nickName = getIntent().getStringExtra("nickName");
        String online = getIntent().getStringExtra("onLine");
        String addhot = getIntent().getStringExtra("addhot");
        String photo = getIntent().getStringExtra("photo");
        String love = getIntent().getStringExtra("love");
        String liveTime = getIntent().getStringExtra("liveTime");


        tvNickname.setText(nickName);
        tvOnlineNum.setText(online);
        tvHot.setText(addhot);
        tvLove.setText(love);
        tvTime.setText(liveTime);
        ImageUtils.displayAvator(ivPhoto,photo);
    }

    private void initView() {
        //设置直播信息
        tvNickname = (TextView) findViewById(R.id.tvNickname);
        tvOnlineNum = (TextView) findViewById(R.id.tvOnlineNum);
        tvHot = (TextView) findViewById(R.id.tvHots);
        tvLove = (TextView) findViewById(R.id.tvLoves);
        tvTime = (TextView) findViewById(R.id.tvTime);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);


        findViewById(R.id.tvGohome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.btnClosed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
