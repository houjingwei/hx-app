package com.huixiang.live.utils.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Stone on 16/5/14.
 */
public class MyListView extends ListView {
    private float mLastx = 0;
    private float x = 0;

    private Activity mActivity;

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN :

                break;

            case MotionEvent.ACTION_MOVE :
                if (1==1) {
                // 下拉刷新逻辑...
                return true;//这里拦截了触摸事件
            }
            break;

            case MotionEvent.ACTION_UP :
                // ...
                break;

        }

        return true;
        //return super.onTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {


        return false;
    }
}