package com.huixiangtv.live.utils.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by stone on 16/5/13.
 */
public class PictureViewPager extends ViewPager {
    PointF downP = new PointF();
    PointF curP = new PointF();
    OnSingleTouchListener onSingleTouchListener;

    public PictureViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PictureViewPager(Context context) {
        super(context);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // 不拦截事件让其下传
        return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        curP.x = arg0.getX();
        curP.y = arg0.getY();

        if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
            downP.x = arg0.getX();
            downP.y = arg0.getY();
        }

        if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }

        if (arg0.getAction() == MotionEvent.ACTION_UP) {
            if (Math.abs(downP.x - curP.x) < 5
                    && Math.abs(downP.y - curP.y) < 5) {
                onSingleTouch();
                return true;
            }
        }

        return super.onTouchEvent(arg0);
    }

    /**
     * click
     */
    public void onSingleTouch() {
        if (onSingleTouchListener != null) {
            onSingleTouchListener.onSingleTouch();
        }
    }

    /**
     * create click interface
     *
     */
    public interface OnSingleTouchListener {
        public void onSingleTouch();
    }

    public void setOnSingleTouchListener(
            OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }
}