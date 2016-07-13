package com.huixiangtv.liveshow.utils.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Stone on 16/7/1.
 */
public class GridViewCircle extends android.widget.GridView {

    public GridViewCircle(Context context) {
        super(context);
    }

    public GridViewCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewCircle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}