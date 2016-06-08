package com.chanven.lib.cptr;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chanven.lib.cptr.indicator.PtrIndicator;

/**
 * Created by houjingwei on 2016/6/7.
 */
public class CustomHeader extends FrameLayout implements PtrUIHandler {

    private ImageView imageView;
    private AnimationDrawable animationDrawable;

    public CustomHeader(Context context) {
        super(context);
        initViews();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initViews() {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.custom_header, this);
        imageView = (ImageView) header.findViewById(R.id.ptr_classic_header_rotate_view);
        imageView.setImageResource(R.drawable.animation);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onUIReset(PtrFrameLayout frame) {
        animationDrawable.stop();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        animationDrawable.start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        animationDrawable.stop();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

    }
}
