package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.huixiangtv.live.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class HelpActivity extends BaseBackActivity implements View.OnClickListener{

    @ViewInject(R.id.helpback)
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        x.view().inject(this);
        initview();
    }

    private void initview() {
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.helpback:
                onBackPressed();
                break;
        }
    }
}
