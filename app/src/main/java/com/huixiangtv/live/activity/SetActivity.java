package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.BaseBackActivity;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.ForwardUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class SetActivity extends BaseBackActivity implements View.OnClickListener{


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;
    @ViewInject(R.id.qqbind)
    TextView qqbind;
    @ViewInject(R.id.helpcentre)
    RelativeLayout helpcentre;
    @ViewInject(R.id.setexit)
    Button setexit;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        x.view().inject(this);
        initview();
    }

    private void initview() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.set));
        helpcentre.setOnClickListener(this);
        setexit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setexit:
                onBackPressed();
                break;
            case R.id.helpcentre:
                ForwardUtils.target(this, Constant.HELP,null);
                break;
        }
    }
}
