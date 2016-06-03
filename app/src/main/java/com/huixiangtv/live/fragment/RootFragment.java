package com.huixiangtv.live.fragment;

import android.app.Activity;
import android.view.View;

import com.huixiangtv.live.utils.CommonHelper;

import java.util.Calendar;

/**
 * Created by Stone on 16/5/15.  先放着可能到时候扩展用到
 */
public abstract  class RootFragment extends LoadingFragment implements View.OnClickListener {



    public void finishFragment(){
        if(getActivity() != null){
            getActivity().finish();
        }
    }


    protected void finishActivityAttached() {
        Activity activity = getActivity();
        if(activity != null) {
            activity.finish();
        }
    }

    public void requestDone() {
        hideLoading();
    }


    public void showToast(String text) {
        CommonHelper.showTip(getActivity(),text);
    }

    public void showToast(int resId) {
        showToast(resId+"");
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
