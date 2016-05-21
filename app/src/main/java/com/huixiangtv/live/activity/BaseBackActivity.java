package com.huixiangtv.live.activity;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.huixiangtv.live.R;
import com.huixiangtv.live.utils.MeizuSmartBarUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Calendar;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class BaseBackActivity extends SwipeBackActivity implements View.OnClickListener{

    private SwipeBackLayout mSwipeBackLayout;

    public int color = R.color.mainColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        if(MeizuSmartBarUtils.hasSmartBar()){
            View decorView = getWindow().getDecorView();
            MeizuSmartBarUtils.hide(decorView);
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        finish();
    }

    /**
     * 事件处理
     */
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    protected  void onNoDoubleClick(View view){

    }
    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }





}