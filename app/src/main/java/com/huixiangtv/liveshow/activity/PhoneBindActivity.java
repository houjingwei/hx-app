package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.ui.CommonTitle;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class PhoneBindActivity extends BaseBackActivity {

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_bind);
        x.view().inject(this);
        initView();
    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.newPhoneBind));
    }
}
