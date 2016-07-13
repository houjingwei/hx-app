package com.huixiangtv.liveshow.Js;

import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * Created by Stone on 16/6/13.
 */
public class Hight17Js implements IJsInterface {
    private static final String TAG = "Hight17Js";
    private Context context;
    private Doing doing;

    public Hight17Js(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    @Override
    public void setDoing(Doing doing) {

    }
}
