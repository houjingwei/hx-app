package com.huixiangtv.live.utils.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Stone on 16/7/1.
 */
public class ListViewCircle extends ListView {

    public ListViewCircle(Context context) {
        super(context);
    }

    public ListViewCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewCircle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
