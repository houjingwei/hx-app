package com.huixiang.live.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huixiang.live.Constant;
import com.huixiang.live.R;
import com.huixiang.live.ui.StartLiveView;
import com.huixiang.live.utils.ForwardUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class StartLiveActivity extends BaseBackActivity implements View.OnClickListener {

    @ViewInject(R.id.flCover)
    FrameLayout flCover;

    TextView tvTheme;
    StartLiveView startLiveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_live);
        x.view().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        startLiveView = new StartLiveView(this);
        startLiveView.setActivity(this);
        flCover.addView(startLiveView);
        initView();

    }

    private void initView() {
        tvTheme =startLiveView.getTvTheme();
        tvTheme.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvTheme:
                //KeyBoardUtils.closeKeybord(etTitle,activity);
                choiseTheme();
                break;


        }
    }









    /**
     * 选择主题
     */
    private void choiseTheme() {
        ForwardUtils.target(StartLiveActivity.this, Constant.LIVE_TOPIC,null);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && requestCode==1) {
                    String tid = data.getStringExtra("tid");
                    String topic= data.getStringExtra("topic");
                    tvTheme.setText("# "+topic+" #");
                }
                break;

            default:
                break;
        }
    }
}
