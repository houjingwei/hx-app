package com.huixiang.live.common;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by hjw on 16/5/10.
 */
public class CommonUtil {

    /**
     * 隐藏键盘
     * @param activity
     * @param et
     */
    public static void hideKeyBoard(Activity activity, EditText et) {

        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(),0);
    }


    
}
