package com.huixiangtv.live.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.ui.LiveView;
import com.huixiangtv.live.ui.StartLiveView;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.KeyBoardUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

public class StartLiveActivity extends BaseBackActivity implements View.OnClickListener{

    @ViewInject(R.id.flCover)
    FrameLayout flCover;

    TextView tvTheme;
    TextView tvStart;
    StartLiveView startLiveView;

    LiveView liveView;

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
        KeyBoardUtils.closeKeybord(startLiveView.getEtTitle(),this);

    }

    private void initView() {
        tvTheme =startLiveView.getTvTheme();
        tvStart = startLiveView.getTvStart();
        tvTheme.setOnClickListener(this);
        tvStart.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTheme:
                //选择主题
                ForwardUtils.target(StartLiveActivity.this, Constant.LIVE_TOPIC,null);
                break;
            case R.id.tvStart:
                Map<String,String> params = new HashMap<String,String>();
                params.put("local",startLiveView.getLocal()+"");
                params.put("platform",startLiveView.getPlatform()+"");
//                ForwardUtils.target(StartLiveActivity.this, Constant.LIVE,params);
                //切换视图
                changeToLive();
                break;

        }
    }

    private ColaProgress cp = null;
    private void changeToLive() {
        cp = ColaProgress.show(StartLiveActivity.this, "准备直播", false, true, null);
        ObjectAnimator anim = ObjectAnimator.ofFloat(startLiveView, "alpha", 0f);
        anim.setDuration(1000);
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(null!=startLiveView){
                    flCover.removeView(startLiveView);
                }
                if(null!=cp){
                    cp.dismiss();
                }
                liveView = new LiveView(StartLiveActivity.this);
                liveView.setActivity(StartLiveActivity.this);
                flCover.addView(liveView);
                liveView.loadLive(null);
                ObjectAnimator animIn = ObjectAnimator.ofFloat(startLiveView, "alpha", 1f);
                animIn.setDuration(500);
                animIn.start();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }

        });
        anim.start();



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
    protected void onDestroy() {
        super.onDestroy();
        liveView.removeGlobalListener();

    }
}
