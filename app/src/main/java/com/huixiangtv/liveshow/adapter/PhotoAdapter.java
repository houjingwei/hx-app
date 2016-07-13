package com.huixiangtv.liveshow.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Stone on 16/5/31.
 */
public class PhotoAdapter extends PagerAdapter {
    private ImageView[] mImageViews;
    private Context mContext;
    public static final int APP_PAGE_SIZE = 1;

    public PhotoAdapter(Context context, ImageView[] mImageViews) {
        mContext = context;
        this.mImageViews =mImageViews;



    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
        return mImageViews[position % mImageViews.length];
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }


    @Override
    public void startUpdate(View arg0) {
    }
    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);

    }

    @Override
    public void finishUpdate(View arg0) {
        // TODO Auto-generated method stub
    }


}