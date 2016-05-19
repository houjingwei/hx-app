package com.huixiangtv.live.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.huixiangtv.live.R;

/**
 * Created by hjw on 16/5/19.
 */
public class PointImageView extends RelativeLayout {
    public PointImageView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.point_image_view,null);
    }

    public PointImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
