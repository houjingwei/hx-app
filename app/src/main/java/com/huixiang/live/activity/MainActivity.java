package com.huixiang.live.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiang.live.App;
import com.huixiang.live.Constant;
import com.huixiang.live.R;
import com.huixiang.live.fragment.FragmentTabOne;
import com.huixiang.live.fragment.FragmentTabThree;
import com.huixiang.live.fragment.FragmentTabTwo;
import com.huixiang.live.utils.ForwardUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class MainActivity extends BaseActivity implements View.OnClickListener{

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


    FragmentTabOne fragmentOne;
    FragmentTabTwo fragmentTwo;
    FragmentTabThree fragmentThree;


    FragmentTransaction trx = null;



    TextView txTitle;
    ImageView ivBack;
    LinearLayout llTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);
        initWindow();
        App.getContext().addActivity(this);
        initView();
    }

    private void initWindow() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//           setTranslucentStatus(true);
//        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
    }

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


    /**
     * 初始化组件
     */
    private void initView() {
        llTitle = (LinearLayout) findViewById(R.id.llTitle);
        txTitle = (TextView) findViewById(R.id.title);
        ivBack = (ImageView) findViewById(R.id.back);

        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        iv2.setOnClickListener(this);
        initFragment();

    }

    private void initFragment() {
        fragmentOne = new FragmentTabOne();
        fragmentTwo = new FragmentTabTwo();
        fragmentThree = new FragmentTabThree();

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
            hideTitle(false);
            iv1.setImageResource(R.mipmap.tab1_pressed);
            iv3.setImageResource(R.mipmap.tab3);

        } else if (index == 1) {
            hideTitle(true);
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



    public void setTitleBar(String title){
        txTitle.setText(title);
        ivBack.setVisibility(View.GONE);
    }


    private long lastTipTimeMills = 0l;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTipTimeMills > 1000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            lastTipTimeMills = System.currentTimeMillis();
        } else {
            finish();
            App.getContext().finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    public void hideTitle(boolean bool) {
        Window window = getWindow();
        if(bool){
            llTitle.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(R.color.mainColor));
            }
        }else{
            llTitle.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1:
                setTabSelection(0);
                break;
            case R.id.tab2:
                startLive();
                break;
            case R.id.iv2:
                startLive();
                break;
            case R.id.tab3:
                setTabSelection(1);
                break;
            default:
                break;
        }
    }


    /**
     * 开启直播
     */
    private void startLive() {
        ForwardUtils.target(MainActivity.this, Constant.START_LIVE,null);
    }



    @Override
    public void onAttachFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        super.onAttachFragment(fragment);


        Log.d(TAG,"onAttachFragment");

        if (fragmentOne == null && fragment instanceof FragmentTabOne) {
            fragmentOne = (FragmentTabOne)fragment;
        }else if (fragmentThree == null && fragment instanceof FragmentTabThree) {
            fragmentThree = (FragmentTabThree)fragment;
        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        //阻止activity保存fragment的状态
//        //super.onSaveInstanceState(outState, outPersistentState);
//    }
}
