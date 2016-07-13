package com.huixiangtv.liveshow.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.huixiangtv.liveshow.utils.CommonHelper;

import java.util.Calendar;

/**
 * Created by Stone on 16/5/15.  先放着可能到时候扩展用到
 */
public abstract  class RootFragment extends Fragment implements View.OnClickListener {




    public void showToast(String text) {
        CommonHelper.showTip(getActivity(), text);
    }


    /**
     * 事件处理
     */
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    protected abstract void onNoDoubleClick(View view);
    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

}
