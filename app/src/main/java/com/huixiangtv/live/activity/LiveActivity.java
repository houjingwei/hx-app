package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.ui.LiveView;
import com.umeng.socialize.Config;

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
        Config.OpenEditor = true;
        x.view().inject(this);
        platform = getIntent().getStringExtra("platform");
        local = getIntent().getStringExtra("local");
        Live live = new Live();

        LiveView liveView = new LiveView(this);
        flCover.addView(liveView);
        liveView.loadLive(live);
    }
}
