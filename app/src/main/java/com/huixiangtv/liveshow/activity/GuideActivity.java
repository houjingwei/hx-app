package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.ui.GiftView;

public class GuideActivity extends AppCompatActivity {

    RelativeLayout main;
    GiftView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        main = (RelativeLayout) findViewById(R.id.main);
        view = new GiftView(this);
        view.setActivity(this);
        view.initView();
        main.addView(view);

    }
}
