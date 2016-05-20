package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.ui.CommonTitle;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class HelpActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        x.view().inject(this);
        initview();
    }

    private void initview() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.help));
    }


}
