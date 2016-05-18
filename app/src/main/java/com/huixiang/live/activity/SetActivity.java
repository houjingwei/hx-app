package com.huixiang.live.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.huixiang.live.Constant;

import com.huixiang.live.utils.ForwardUtils;
import com.huixiang.live.R;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class SetActivity extends BaseBackActivity implements View.OnClickListener{


    @ViewInject(R.id.setback)
    ImageView back;
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
     back.setOnClickListener(this);
        helpcentre.setOnClickListener(this);
        setexit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setback:
               onBackPressed();
                break;
            case R.id.setexit:
                onBackPressed();
                break;
            case R.id.helpcentre:
                ForwardUtils.target(this, Constant.HELP,null);
                break;
        }
    }
}
