package com.huixiangtv.live.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.ui.LiveView;
import com.huixiangtv.live.ui.StartLiveView;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.KeyBoardUtils;
import com.huixiangtv.live.utils.LocationTool;
import com.huixiangtv.live.utils.MeizuSmartBarUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

public class LiveActivity extends BaseBackActivity implements View.OnClickListener{

    @ViewInject(R.id.flCover)
    FrameLayout flCover;

    TextView tvTheme;
    TextView tvStart;
    StartLiveView startLiveView;

    LiveView liveView;

    String[] jwd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        x.view().inject(this);
        if (MeizuSmartBarUtils.hasSmartBar()) {
            View decorView = getWindow().getDecorView();
            MeizuSmartBarUtils.hide(decorView);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        LocationTool tool = new LocationTool(LiveActivity.this);
        jwd =  tool.jwd();
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
                ForwardUtils.target(LiveActivity.this, Constant.LIVE_TOPIC,null);
                break;
            case R.id.tvStart:
                Map<String,String> params = new HashMap<String,String>();
                params.put("local",startLiveView.getLocal()+"");
                params.put("platform",startLiveView.getPlatform()+"");
                //请求开始直播
                toLive();
                break;
        }
    }

    /**
     * 请求开始直播
     */
    private ColaProgress cp = null;
    private void toLive() {
        final Map<String,String> params = new HashMap<String,String>();
        if(TextUtils.isEmpty(startLiveView.getEtTitle().getText().toString())){
            CommonHelper.showTip(LiveActivity.this, "请输入直播标题");
            startLiveView.getEtTitle().requestFocus();
            return;
        }else if(startLiveView.getTvTheme().getText().toString().equals(R.string.selTheme)){
            CommonHelper.showTip(LiveActivity.this, "请选择一个话题");
            return;
        }
        params.put("title",startLiveView.getEtTitle().getText().toString());
        params.put("topic",startLiveView.getTvTheme().getText().toString());
        params.put("lon",jwd[0]);
        params.put("lat",jwd[1]);
        params.put("address",jwd[2]);
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
                showLive(params);


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

    private void showLive(Map<String, String> params) {
        cp = ColaProgress.show(LiveActivity.this, "准备直播", false, true, null);
        RequestUtils.sendPostRequest(Api.LIVE_SHOW, params, new ResponseCallBack<Live>() {
            @Override
            public void onSuccess(Live data) {
                super.onSuccess(data);
                if(null!=cp){
                    cp.dismiss();
                }
                liveView = new LiveView(LiveActivity.this);
                liveView.setActivity(LiveActivity.this);
                flCover.addView(liveView);
                liveView.loadLive(null);
                ObjectAnimator animIn = ObjectAnimator.ofFloat(startLiveView, "alpha", 1f);
                animIn.setDuration(500);
                animIn.start();
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                if(null!=cp){
                    cp.dismiss();
                }
            }
        },Live.class);
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
        if(null!=liveView) {
            liveView.removeGlobalListener();
        }

    }


}
