package com.huixiangtv.live.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.LoadingLayoutBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.huixiangtv.live.R;

/**
 * create by hjw
 */
public class HuixiangLoadingLayout extends LoadingLayoutBase {

    static final String LOG_TAG = "PullToRefresh-HuixiangLoadingLayout";

    private FrameLayout flRefresh;
    private ImageView ivLoad;
    private AnimationDrawable animP;
    public HuixiangLoadingLayout(Context context) {
        this(context, PullToRefreshBase.Mode.PULL_FROM_START);
    }

    public HuixiangLoadingLayout(Context context, PullToRefreshBase.Mode mode) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.huixiang_loading_view, this);
        flRefresh = (FrameLayout) findViewById(R.id.flRefresh);
        ivLoad = (ImageView) flRefresh.findViewById(R.id.ivLoad);
        LayoutParams lp = (LayoutParams) flRefresh.getLayoutParams();
        lp.gravity = mode == PullToRefreshBase.Mode.PULL_FROM_END ? Gravity.TOP : Gravity.BOTTOM;
        reset();
    }

    @Override
    public final int getContentSize() {
        return flRefresh.getHeight();
    }

    @Override
    public final void pullToRefresh() {

    }

    @Override
    public final void onPull(float scaleOfLayout) {

    }

    @Override
    public final void refreshing() {
        if (animP == null) {
            ivLoad.setImageResource(R.drawable.animation);
            animP = (AnimationDrawable) ivLoad.getDrawable();
        }
        animP.start();
    }

    @Override
    public final void releaseToRefresh() {

    }

    @Override
    public final void reset() {
        if (animP != null) {
            animP.stop();
            animP = null;
        }
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {

    }

    @Override
    public void setPullLabel(CharSequence pullLabel) {

    }

    @Override
    public void setRefreshingLabel(CharSequence refreshingLabel) {

    }

    @Override
    public void setReleaseLabel(CharSequence releaseLabel) {

    }

    @Override
    public void setTextTypeface(Typeface tf) {

    }
}