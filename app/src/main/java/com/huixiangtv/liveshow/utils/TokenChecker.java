package com.huixiangtv.liveshow.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Constant;

/**
 * Created by Stone on 16/5/19.
 */
public class TokenChecker {


    /**
     * 检查是否有登录
     * @param context
     * @return
     */
    public static boolean checkToken(Activity context) {
        String token = App.getPreferencesValue("token");
        if(TextUtils.isEmpty(token)) {
            popupLoginNotice(context);
            return false;
        }
        return true;
    }


    public static void popupLoginNotice(final Activity context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle("").setMessage("该功能需要登陆后使用，是否马上登录")
                .setPositiveButton("现在登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ForwardUtils.target(context, Constant.LOGIN,null);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();
        alertDialog.show();
    }
}
