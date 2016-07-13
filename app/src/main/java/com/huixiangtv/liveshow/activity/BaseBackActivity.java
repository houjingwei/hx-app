package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.view.View;

import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.utils.MeizuSmartBarUtils;

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
