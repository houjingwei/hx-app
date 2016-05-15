package com.huixiang.live.fragment;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Stone on 16/5/15.  先放着可能到时候扩展用到
 */
public abstract  class RootFragment extends LoadingFragment {



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
        Activity activity = getActivity();
        if(activity != null) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(int resId) {
        Activity activity = getActivity();
        if(activity != null) {
            Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
        }
    }

}
