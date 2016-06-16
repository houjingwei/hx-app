package com.huixiangtv.live.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.utils.StringUtil;


/**
 * Created by hjw on 16/6/16.
 */
public class CenterLoadingView  extends Dialog {



    private ImageView ivImage;
    private TextView tvMsg;

    private AnimationDrawable animationDrawable;



    public CenterLoadingView(Context context) {
        super(context, R.style.dialog_loading);
        init();
    }

    private void init() {
        setContentView(R.layout.common_dialog_loading_layout);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        tvMsg = (TextView) findViewById(R.id.tvMsg);
        initAnim();
//        getWindow().setWindowAnimations(R.anim.alpha_in);
    }

    private void initAnim() {
        ivImage.setImageResource(com.chanven.lib.cptr.R.drawable.animation);
        animationDrawable = (AnimationDrawable) ivImage.getDrawable();
    }




    @Override
    public void show() {
        super.show();
        animationDrawable.start();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        animationDrawable.stop();
    }


    @Override
    public void setTitle(CharSequence title) {
        if (StringUtil.isNotEmpty(title.toString()) && null!=tvMsg) {
            tvMsg.setText(title);
        }
    }


    public static void dismissDialog(CenterLoadingView loadingDialog) {
        if (null == loadingDialog) { return; }
        loadingDialog.dismiss();
    }

}
