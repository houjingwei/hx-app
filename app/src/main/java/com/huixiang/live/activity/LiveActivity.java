package com.huixiang.live.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.huixiang.live.R;
import com.huixiang.live.model.Live;
import com.huixiang.live.ui.LiveView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class LiveActivity extends BaseBackActivity {

    private String platform;
    private String local;
    @ViewInject(R.id.flCover)
    FrameLayout flCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        x.view().inject(this);
        platform = getIntent().getStringExtra("platform");
        local = getIntent().getStringExtra("local");
        Live live = new Live();

        LiveView liveView = new LiveView(this);
        flCover.addView(liveView);
        liveView.loadLive(live);
    }
}
