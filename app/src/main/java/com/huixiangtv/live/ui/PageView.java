package com.huixiangtv.live.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.huixiangtv.live.App;

/**
 * Created by hjw on 16/6/22.
 */
public class PageView extends HorizontalScrollView {

    private int mBaseScrollX;//滑动基线。也就是点击并滑动之前的x值，以此值计算相对滑动距离。
    private int mScreenWidth;
    private int mScreenHeight;

    private FrameLayout mContainer;
    private boolean flag;
    private int mPageCount;//页面数量
    private int flX = 0;

    private int mScrollX = 500;//滑动多长距离翻页

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenWidth = App.screenWidth;
        mScreenHeight = App.screenHeight;

    }

    /**
     * 添加一个页面到最后。
     * @param page
     */
    public void addPage(View page) {
        addPage(page, -1);
    }

    /**
     * 添加一个页面。
     * @param page
     */
    public void addPage(View page, int index) {
        if(!flag) {
            mContainer = (FrameLayout) getChildAt(0);
            flag = true;
        }
        flX = getPageCount()* mScreenWidth;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mScreenWidth, mScreenHeight);
        params.leftMargin = flX;
        if(index == -1) {
            mContainer.addView(page, params);
        } else {
            mContainer.addView(page, index, params);
        }
        mPageCount++;
    }


    public void currPage(){
        View view ;
        Log.i("PageView","***"+mBaseScrollX);
    }

    /**
     * 移除一个页面。
     * @param index
     */
    public void removePage(int index) {
        if(mPageCount < 1) {
            return;
        }
        if(index<0 || index>mPageCount-1) {
            return;
        }
        mContainer.removeViewAt(index);
        mPageCount--;
    }

    /**
     * 移除所有的页面
     */
    public void removeAllPages() {
        if(mPageCount > 0) {
            mContainer.removeAllViews();
        }
    }

    /**
     * 获取页面数量
     * @return
     */
    public int getPageCount() {
        return mPageCount;
    }

    /**
     * 获取相对滑动位置。由右向左滑动，返回正值；由左向右滑动，返回负值。
     * @return
     */
    private int getBaseScrollX() {
        return getScrollX() - mBaseScrollX;
    }

    /**
     * 使相对于基线移动x距离。
     * @param x x为正值时右移；为负值时左移。
     */
    private void baseSmoothScrollTo(int x) {
        smoothScrollTo(x + mBaseScrollX, 0);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                int scrollX = getBaseScrollX();
                //左滑，大于一半，移到下一页
                if (scrollX > mScrollX) {
                    baseSmoothScrollTo(mScreenWidth);
                    mBaseScrollX += mScreenWidth;
                    Log.i("PageView","***"+mBaseScrollX);
                }
                //左滑，不到一半，返回原位
                else if (scrollX > 0) {
                    baseSmoothScrollTo(0);
                }
                //右滑，不到一半，返回原位
                else if(scrollX > -mScrollX) {
                    baseSmoothScrollTo(0);
                }
                //右滑，大于一半，移到下一页
                else {
                    baseSmoothScrollTo(-mScreenWidth);
                    mBaseScrollX -= mScreenWidth;
                    Log.i("PageView","---"+mBaseScrollX);
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }
}