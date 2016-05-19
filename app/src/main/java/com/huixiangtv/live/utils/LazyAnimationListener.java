package com.huixiangtv.live.utils;

import android.view.animation.Animation;

/**
 * Created by Johnny on 2015/11/30.
 */
public class LazyAnimationListener implements Animation.AnimationListener {
    private float toCurrX;
    private float toCurrY;

    public LazyAnimationListener(){
    }

    public LazyAnimationListener(float toCurrX, float toCurrY){
        this.toCurrX = toCurrX;
        this.toCurrY = toCurrY;
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public float getToCurrY() {
        return toCurrY;
    }

    public void setToCurrY(float toCurrY) {
        this.toCurrY = toCurrY;
    }

    public float getToCurrX() {
        return toCurrX;
    }

    public void setToCurrX(float toCurrX) {
        this.toCurrX = toCurrX;
    }
}
