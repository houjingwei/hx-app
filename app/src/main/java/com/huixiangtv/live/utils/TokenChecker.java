package com.huixiangtv.live.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

/**
 * Created by Stone on 16/5/19.
 */
public class TokenChecker {


    /**
     * 检查是否有登录
     * @param context
     * @return
     */
    public static boolean checkToken(Context context) {
//        String token ="";
//        if(TextUtils.isEmpty(token)) {
//            popupLoginNotice(context);
//            return false;
//        }
        return true;
    }


    public static void popupLoginNotice(final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle("").setMessage("该功能需要登陆后使用，是否马上登录")
                .setPositiveButton("现在登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //gotoLoginPage
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();
        alertDialog.show();
    }
}
