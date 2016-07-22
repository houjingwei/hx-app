package com.huixiangtv.liveshow.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.utils.StringUtil;


/**
 * Created by hjw on 16/6/16.
 */
public class CenterMsgView extends Dialog {



    private ImageView ivImage;
    private TextView tvMsg;

    private AnimationDrawable animationDrawable;



    public CenterMsgView(Context context) {
        super(context, R.style.dialog_loading);
        init();
    }

    private void init() {
        setContentView(R.layout.center_msg_view);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        tvMsg = (TextView) findViewById(R.id.tvMsg);

    }






    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    @Override
    public void setTitle(CharSequence title) {
        if (StringUtil.isNotEmpty(title.toString()) && null!=tvMsg) {
            tvMsg.setText(title);
        }
    }


    public static void dismissDialog(CenterMsgView loadingDialog) {
        if (null == loadingDialog) { return; }
        loadingDialog.dismiss();
    }

}