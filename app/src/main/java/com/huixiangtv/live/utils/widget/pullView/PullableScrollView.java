package com.huixiangtv.live.utils.widget.pullView;

/**
 * Created by Stone on 16/6/6.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable
{

    public PullableScrollView(Context context)
    {
        super(context);
    }

    public PullableScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PullableScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown()
    {
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean canPullUp()
    {
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
        else
            return false;
    }

}
