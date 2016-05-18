package com.huixiangtv.live.ui;


/**
 *
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.R;


public class ColaProgress extends Dialog {

    public ColaProgress(Context context, int theme) {
        super(context, theme);
    }

    //焦点在window上的时候开始实例化，给图片加入动画效果
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        spinner.start();
    }

    //设置文字
    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    public static ColaProgress show(Context context, CharSequence message, boolean cancelable) {
        return show(context, message, cancelable, false, null);
    }

    //展示
    public static ColaProgress show(Context context, CharSequence message, boolean cancelable, boolean cancelableOutsite, OnCancelListener onCancelListener) {
        //设置主题
        ColaProgress colaProgress = new ColaProgress(context, R.style.ColaProgress);
        cancelSelf();//先取消界面上已有进度框。
        _instance = colaProgress;
        colaProgress.setTitle("");
        colaProgress.setContentView(R.layout.layout_colaprogress);

        if (message.length() == 0 || message == null) {
            colaProgress.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txtMessage = (TextView) colaProgress.findViewById(R.id.message);
            txtMessage.setText(message);
        }
        colaProgress.setCancelable(cancelable);
        colaProgress.setCanceledOnTouchOutside(cancelableOutsite);
        colaProgress.setOnCancelListener(onCancelListener);
        WindowManager.LayoutParams lp = colaProgress.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.2f;
        colaProgress.getWindow().setAttributes(lp);
        colaProgress.show();
        return colaProgress;
    }

    //增加一个静态方法可能直接cancel当前进度框。
    private static ColaProgress _instance = null;

    public static void cancelSelf() {
        if (_instance != null) {
            _instance.dismiss();
            _instance = null;
        }
    }
}



