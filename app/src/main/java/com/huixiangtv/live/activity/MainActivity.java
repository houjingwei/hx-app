package com.huixiangtv.live.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.common.CommonUtil;
import com.huixiangtv.live.fragment.FragmentCircle;
import com.huixiangtv.live.fragment.FragmentTabFour;
import com.huixiangtv.live.fragment.FragmentTabOne;
import com.huixiangtv.live.fragment.FragmentTabTwo;
import com.huixiangtv.live.model.UpgradeLevel;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.LoginCallBack;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.UpdateApp;
import com.huixiangtv.live.utils.BitmapHelper;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_ASK_CAMERA = 100;
    private static final int REQUEST_CODE_ASK_RECORD = 101;
    private final String TAG = "MainActivity";

    @ViewInject(R.id.tab1)
    RelativeLayout tab1;
    @ViewInject(R.id.iv1)
    ImageView iv1;

    @ViewInject(R.id.tab2)
    RelativeLayout tab2;
    @ViewInject(R.id.iv2)
    ImageView iv2;

    @ViewInject(R.id.tab3)
    RelativeLayout tab3;
    @ViewInject(R.id.iv3)
    ImageView iv3;

    @ViewInject(R.id.main)


    FragmentTabOne fragmentOne;
    FragmentTabTwo fragmentTwo;
    FragmentTabFour fragmentThree;


    FragmentTransaction trx = null;
    private boolean bigImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);
        initWindow();
        hideTitle();
        App.getContext().addActivity(this);
        initView();
        setGuidle();
        CheckVersion();
        isBigImage();
    }

    private void initWindow() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
    }


    /**
     * 初始化组件
     */
    private void initView() {

        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        iv2.setOnClickListener(this);
        initFragment();



    }

    private void initFragment() {
        fragmentOne = new FragmentTabOne();
        fragmentTwo = new FragmentTabTwo();
        fragmentThree = new FragmentTabFour();

        // 把第一个tab设为选中状态
        setTabSelection(0);
    }


    private void setTabSelection(int index) {
        trx = getSupportFragmentManager().beginTransaction();
        hideFragments(trx);
        switch (index) {
            case 0:
                if (!fragmentOne.isAdded()) {
                    trx.add(R.id.content, fragmentOne);
                }
                trx.show(fragmentOne);
                addSelection(0);
                break;
            case 1:
                if (!fragmentThree.isAdded()) {
                    trx.add(R.id.content, fragmentThree);
                }
                trx.show(fragmentThree);
                addSelection(1);
                break;
        }
        //防止一个状态丢失崩溃.
        trx.commitAllowingStateLoss();
    }


    private void addSelection(int index) {
        if (index == 0) {
            iv1.setImageResource(R.mipmap.tab1_pressed);
            iv3.setImageResource(R.mipmap.tab3);

        } else if (index == 1) {

            iv1.setImageResource(R.mipmap.tab1);
            iv3.setImageResource(R.mipmap.tab3_pressed);
        }
    }


    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (fragmentOne != null) {
            transaction.hide(fragmentOne);
        }
        if (fragmentThree != null) {
            transaction.hide(fragmentThree);
        }
    }




    private long lastTipTimeMills = 0l;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTipTimeMills > 1000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            lastTipTimeMills = System.currentTimeMillis();
        } else {
            finish();
            App.getContext().finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }



    public void hideTitle() {
        Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.mainColor));
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1:
                onDBClick();
                FragmentCircle.clearViode();
                break;
            case R.id.tab2:
                if(null!=App.getLoginUser()){
                    startLive();
                }else{
                    CommonHelper.showLoginPopWindow(MainActivity.this, R.id.main, new LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            startLive();
                        }
                    });
                }

                isSwitch = true;
                FragmentCircle.clearViode();
                break;
            case R.id.iv2:
                if(null!=App.getLoginUser()){
                    if (Build.VERSION.SDK_INT >= 23) {
                        int checkCallCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                        int checkCallAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
                        if(checkCallCameraPermission != PackageManager.PERMISSION_GRANTED || checkCallAudioPermission!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO},REQUEST_CODE_ASK_CAMERA);
                            return;
                        }else{
                            startLive();
                        }
                    } else {
                        startLive();
                    }
                }else{
                    CommonHelper.showLoginPopWindow(MainActivity.this, R.id.main, new LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            startLive();
                        }
                    });
                }
                isSwitch = true;
                FragmentCircle.clearViode();
                break;
            case R.id.tab3:
                if(null!=App.getLoginUser()) {
                    setTabSelection(1);

                }
                else
                {
                    ForwardUtils.target(MainActivity.this, Constant.LOGIN,null);
                }

                isSwitch = true;
                break;
            default:
                break;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i("version","gogogo123"+requestCode);
        if (requestCode == REQUEST_CODE_ASK_CAMERA)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startLive();
            } else{
                CommonHelper.showTip(MainActivity.this,"未允许方位相机");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private static boolean isSwitch = false;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onDBClick() {
        indexStyle();

    }

    private void indexStyle() {
        if (isSwitch) {
            setTabSelection(0);
            sendToOneFragment("1");
            isSwitch = false;
            changeIcon(true);
        } else {
            sendToOneFragment("0");
            iv1.setImageResource(R.mipmap.tab1);
            isSwitch = true;
            changeIcon(false);
        }
    }

    private void changeIcon(boolean bool) {
        if(bool){
            iv1.setImageResource(R.mipmap.tab1_pressed);
        }else{
            iv1.setImageResource(R.mipmap.tab1_big);
        }

    }

    /**
     * 事件处理
     */
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    /**
     * 开启直播
     */
    private void startLive() {

        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            Map<String, String> params = new HashMap<>();
            params.put("isRecord","true");
            ForwardUtils.target(MainActivity.this, Constant.START_LIVE, params);
        }

    }

    private void sendToOneFragment(String type) {
        Intent intent = new Intent("com.android.broadcast.RECEIVER_ACTION");
        intent.putExtra("type", type);
        sendBroadcast(intent);
    }


    /**
     * 设置新引导页
     */
    private void setGuidle()
    {

        SharedPreferences preferences = getSharedPreferences("indexsp", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        final String key = getClass().getName() + "_firstLogin";
        if (!preferences.contains(key)) {
            editor.putBoolean(key, true);
            editor.commit();
        }

        if (!preferences.getBoolean(key, true)) {
            return;
        }

        final AlertDialog dlg = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT).create();
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.index_guide);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width  =App.screenWidth;
        lp.height = (int) (App.screenHeight);
        window.setAttributes(lp);
        TranslateAnimation mAnimation = new TranslateAnimation(0,0,0,150);
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setDuration(1500);
        mAnimation.setFillAfter(true);
        window.findViewById(R.id.iv_hand).setAnimation(mAnimation);
        ImageView iv_hand_title = (ImageView) window.findViewById(R.id.iv_hand_title);
        iv_hand_title.setImageBitmap(BitmapHelper.zoomImg(BitmapHelper.readBitMap(MainActivity.this, R.drawable.index_hand_guide)));

        window.findViewById(R.id.fl_index).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(key, false);
                editor.commit();
                dlg.dismiss();
                setGuideNextLeft();
            }
        });

    }

    protected void setGuideNextLeft() {
        CommonUtil.setGuidImage(MainActivity.this, R.id.main, R.drawable.index_up_down, "guide1", new ApiCallback() {

            @Override
            public void onSuccess(Object data) {
                setGuideNextUp();
            }
        });
    }


    protected void setGuideNextUp() {
        CommonUtil.setGuidImage(MainActivity.this, R.id.main, R.drawable.index_hand_clear, "guide2", new ApiCallback() {

            @Override
            public void onSuccess(Object data) {

            }
        });
    }

    /**
     * 退出应用程序
     */
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
            final String version = App.getVersionCode(MainActivity.this);
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("osType", "1");
            paramsMap.put("appVersion", version );

            RequestUtils.sendPostRequest(Api.UPGRADE_LEVEL, paramsMap, new ResponseCallBack<UpgradeLevel>() {

                public void onSuccess(UpgradeLevel data) {

                    if (data != null) {

                        UpgradeLevel upgradeLevel = data;

                        UpdateApp updateApp = new UpdateApp(MainActivity.this);
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
            Toast.makeText(MainActivity.this,"更新异常",Toast.LENGTH_LONG).show();
        }


    }

    public boolean isBigImage() {
        if(null != App.getPreferencesValue("indexStyle") && App.getPreferencesValue("indexStyle").equals("1"))
        {
            isSwitch = true;
        }
        return isSwitch;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragmentOne == null && fragmentOne instanceof FragmentTabOne) {
            fragmentOne = (FragmentTabOne)fragment;
        }else if (fragmentThree == null && fragmentThree instanceof FragmentTabFour) {
            fragmentThree = (FragmentTabFour) fragment;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }


}
