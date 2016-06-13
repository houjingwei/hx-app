package com.huixiangtv.live.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.callback.CodeCallBack;
import com.huixiangtv.live.service.ApiCallback;
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
     * @param codeCallBack
     */
    public static void getMsgCode(String phoneNum,final CodeCallBack codeCallBack) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("phone",phoneNum);
        RequestUtils.sendPostRequest(Api.MSG_CODE, params, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String str) {
                super.onSuccess(str);
                codeCallBack.sendSuccess();
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                codeCallBack.sendError(e.getMessage());
            }
        },String.class);
    }


    public static void setGuidImage(Activity act,int viewId, int imageId, String preferenceName,final ApiCallback<Object> apiCallback ) {
        @SuppressWarnings("static-access")
        SharedPreferences preferences = act.getSharedPreferences(preferenceName, act.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        final String key = act.getClass().getName() + "_firstLogin";
        if (!preferences.contains(key)) {
            editor.putBoolean(key, true);
            editor.commit();
        }

        //判断是否首次登陆
        if (!preferences.getBoolean(key, true)) {
            apiCallback.onSuccess("no");
            return;
        }
        View view = act.getWindow().getDecorView().findViewById(viewId);
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof FrameLayout) {
            final FrameLayout frameLayout = (FrameLayout) viewParent;
            final ImageView guideImage = new ImageView(act.getApplication());

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.RIGHT;
            guideImage.setLayoutParams(params);

            guideImage.setScaleType(ImageView.ScaleType.FIT_XY);
            guideImage.setImageResource(imageId);
            guideImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frameLayout.removeView(guideImage);
                    editor.putBoolean(key, false);
                    editor.commit();
                    apiCallback.onSuccess("ok");
                }
            });
            frameLayout.addView(guideImage);//添加引导图片

        }
    }

}
