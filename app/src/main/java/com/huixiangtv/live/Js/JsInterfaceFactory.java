package com.huixiangtv.live.Js;

import android.content.Context;

/**
 * Created by Stone on 16/6/13.
 */
public class JsInterfaceFactory {
    private static int currentapiVersion = android.os.Build.VERSION.SDK_INT;

    public static IJsInterface createJsInterface(Context context) {
        IJsInterface jsInterface;
        if (currentapiVersion >= 17) {
            jsInterface = new Hight17Js(context);
        } else {
            jsInterface = new Low17Js(context);
        }
        return jsInterface;
    }
}
