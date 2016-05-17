package com.huixiang.live.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.huixiang.live.R;
import com.huixiang.live.activity.StartLiveActivity;
import com.huixiang.live.model.Live;

/**
 * Created by hjw on 16/5/17.
 */
public class LiveView extends RelativeLayout {

    Context ct;
    Activity activity;


    public LiveView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.live_view, this);
        ct = context;
    }

    public LiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void loadLive(Live live) {

    }

public void setActivity(StartLiveActivity activity) {
        this.activity = activity;
    }
}