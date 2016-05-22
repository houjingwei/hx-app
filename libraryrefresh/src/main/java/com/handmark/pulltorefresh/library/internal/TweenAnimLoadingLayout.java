package com.handmark.pulltorefresh.library.internal;



import com.handmark.pulltorefresh.library.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Stone on 16/5/20.
 */
public class TweenAnimLoadingLayout extends LoadingLayout {

    private AnimationDrawable animationDrawable;

    public TweenAnimLoadingLayout(Context context, Mode mode,
                                  Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        // 初始化
        mHeaderImage.setImageResource(R.anim.loading);
        animationDrawable = (AnimationDrawable) mHeaderImage.getDrawable();
    }
    // 默认图片
    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.progress_loading_image_01;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {
        // NO-OP
    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
        // NO-OP
    }
    // 下拉以刷新
    @Override
    protected void pullToRefreshImpl() {
        // NO-OP
    }
    // 正在刷新时回调
    @Override
    protected void refreshingImpl() {
        // 播放帧动画
        animationDrawable.start();
    }
    // 释放以刷新
    @Override
    protected void releaseToRefreshImpl() {
        // NO-OP
    }
    // 重新设置
    @Override
    protected void resetImpl() {
        mHeaderImage.setVisibility(View.VISIBLE);
        mHeaderImage.clearAnimation();
    }

}