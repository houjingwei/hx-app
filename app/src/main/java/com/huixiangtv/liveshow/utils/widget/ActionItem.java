package com.huixiangtv.liveshow.utils.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Stone on 16/7/1.
 */
public class ActionItem {
    // 定义图片对象
    public Drawable mDrawable;
    // 定义文本对象
    public CharSequence mTitle;

    public CharSequence dynamicId;

    public ActionItem(Drawable drawable, CharSequence title) {
        this.mDrawable = drawable;
        this.mTitle = title;
    }

    public ActionItem(CharSequence title) {
        this.mDrawable = null;
        this.mTitle = title;
    }

    public ActionItem(Context context, int titleId, int drawableId) {
        this.mTitle = context.getResources().getText(titleId);
        this.mDrawable = context.getResources().getDrawable(drawableId);
    }

    public ActionItem(Context context, CharSequence title, int drawableId) {
        this.mTitle = title;
        this.mDrawable = context.getResources().getDrawable(drawableId);
    }

    public ActionItem(Context context, CharSequence title,CharSequence dynamicId, int drawableId) {
        this.mTitle = title;
        this.dynamicId = dynamicId;
        this.mDrawable = context.getResources().getDrawable(drawableId);
    }

    public void setItemTv(CharSequence tv) {
        mTitle = tv;
    }
}

