package com.huixiangtv.liveshow.utils.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by houjingwei on 15/11/14.
 */
public class WidgetUtil {


        /*
        * 获取控件宽
        */
        public static int getWidth(View view)
        {
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            return (view.getMeasuredWidth());
        }
        /*
        * 获取控件高
        */
        public static int getHeight(View view)
        {
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            return (view.getMeasuredHeight());
        }

    /*
    * 设置控件所在的位置X，并且不改变宽高，
    * X为绝对位置，此时Y可能归0
    */
    public static void setLayoutX(View view, int x)
        {
            ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
            margin.setMargins(x,margin.topMargin, x+margin.width, margin.bottomMargin);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
            view.setLayoutParams(layoutParams);
        }
        /*
        * 设置控件所在的位置Y，并且不改变宽高，
        * Y为绝对位置，此时X可能归0
        */
        public static void setLayoutY(View view, int y)
        {
            ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
            margin.setMargins(margin.leftMargin,y, margin.rightMargin, y+margin.height);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
            view.setLayoutParams(layoutParams);
        }
        /*
        * 设置控件所在的位置YY，并且不改变宽高，
        * XY为绝对位置
        */
    public static void setLayout(View view, int x, int y)
    {
        ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x,y, x+margin.width, y+margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
