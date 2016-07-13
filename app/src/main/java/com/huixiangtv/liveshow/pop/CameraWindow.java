package com.huixiangtv.liveshow.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.huixiangtv.liveshow.R;


public class CameraWindow extends BasePopupWindow implements OnClickListener {

    private Activity context;
    private View view;
    ScrollView bottom;
    SelectListener listener;


    private LinearLayout llCamera;
    private LinearLayout llCut;
    private LinearLayout llMeiyan;

    private Animation mShowAnimation;
    private Animation mHideAnimation;

    private void initAnimation() {
        mShowAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
        mHideAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_down);
    }

    public CameraWindow(Activity context, int width, int height) {
        this.context = context;
        super.setWidth(width);
        super.setHeight(height);
        initPopUpWindow();
        initAnimation();
        show();
    }

    private void show() {
        bottom.setAnimation(mShowAnimation);
        bottom.setVisibility(View.VISIBLE);
    }


    public void initPopUpWindow() {

        try {
            view = RelativeLayout.inflate(context, R.layout.pop_camera, null);
            bottom = (ScrollView) view.findViewById(R.id.bottom);
            view.findViewById(R.id.pop_layout).setOnClickListener(this);

            llCamera = (LinearLayout) view.findViewById(R.id.llCamera);
            llCut = (LinearLayout) view.findViewById(R.id.llCut);
            llMeiyan = (LinearLayout) view.findViewById(R.id.llMeiyan);

            super.setFocusable(true);
            super.setOutsideTouchable(true);
            super.setBackgroundDrawable(new BitmapDrawable());
            this.setContentView(view);
            this.setWidth(LayoutParams.FILL_PARENT);
            this.setHeight(LayoutParams.FILL_PARENT);
            this.setFocusable(true);
            this.setAnimationStyle(R.style.popupAnimation);

            llCamera.setOnClickListener(this);
            llCut.setOnClickListener(this);
            llMeiyan.setOnClickListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_layout:
                dismiss();
                break;
            case R.id.llCamera:
                if (listener != null) {
                    listener.select(1);
                }
                dismiss();
                break;
            case R.id.llCut:
                if (listener != null) {
                    listener.select(2);
                }
                dismiss();
                break;
            case R.id.llMeiyan:
                if (listener != null) {
                    listener.select(3);
                }
                dismiss();
                break;

            default:
                break;
        }
    }

    public static class SelectListener {
        public void select(int flag) {

        }

    }


    public SelectListener getListener() {
        return listener;
    }

    public void setListener(SelectListener listener) {
        this.listener = listener;
    }

}
