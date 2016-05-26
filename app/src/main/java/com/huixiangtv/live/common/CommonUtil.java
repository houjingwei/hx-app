package com.huixiangtv.live.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.utils.CommonHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * Validate mobile
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(14[0-9])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return true;
    }


    /**
     * 签名入口
     * @param context
     */
    public static void showSignAlert(final Context context) {

        Date currentDate = new Date();
        Date date = App.getDailyCheckInDate("time");
        int d = daysOfTwo(currentDate, date);
        if (d != 0) { // no sign the next

                final AlertDialog dlg = new AlertDialog.Builder(context).create();
                dlg.show();
                dlg.setCancelable(false);
                Window window = dlg.getWindow();
                window.setContentView(R.layout.activity_sign_in);

                ImageView btnClosed = (ImageView) window.findViewById(R.id.btnClosed);
                btnClosed.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dlg.dismiss();
                    }
                });

            //RelativeLayout

        }
        else
        {

             //no operator

        }

    }



    /**
     * Data Computer
     * @param fDate
     * @param oDate
     * @return
     */
    public static int daysOfTwo(Date fDate, Date oDate) {
        if (fDate == null || oDate == null) {
            return -1;
        }

        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;

    }


    /**
     * 获取验证码
     * @return
     * @param
     * @param ct
     */
    public static void getMsgCode(String phoneNum, final Context ct) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("phone",phoneNum);
        RequestUtils.sendPostRequest(Api.MSG_CODE, params, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String str) {
                super.onSuccess(str);
                Toast.makeText(App.getContext(), "\"验证码发送成功\"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(ct,e.getMessage());
            }
        },String.class);
    }
}
