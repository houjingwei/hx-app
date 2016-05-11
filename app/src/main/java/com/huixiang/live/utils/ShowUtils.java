package com.huixiang.live.utils;

import android.content.Context;

import com.huixiang.live.ui.ColaProgressTip;

/**
 * Created by hjw on 16/5/11.
 */
public class ShowUtils {


    public static void showTip(Context activity, String msg) {
        ColaProgressTip cpTip = ColaProgressTip.show(activity, msg, false, true, null, null);
        ColaProgressTip.showTip(800l, cpTip);
    }
}
