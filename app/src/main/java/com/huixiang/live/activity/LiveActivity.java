package com.huixiang.live.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.huixiang.live.R;

public class LiveActivity extends BaseBackActivity {

    private String platform;
    private String local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        platform = getIntent().getStringExtra("platform");
        local = getIntent().getStringExtra("local");
        Toast.makeText(LiveActivity.this, platform+"***"+local, Toast.LENGTH_SHORT).show();
    }
}
