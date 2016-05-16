package com.huixiang.live.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.huixiang.live.Constant;
import com.huixiang.live.R;
import com.huixiang.live.utils.ForwardUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class SetActivity extends BaseBackActivity implements View.OnClickListener{


    @ViewInject(R.id.setback)
    ImageView back;
    @ViewInject(R.id.phonenumber)
    TextView phonenumber;
    @ViewInject(R.id.qqbind)
    TextView qqbind;
    @ViewInject(R.id.changebind)
    TextView chagebind;
    @ViewInject(R.id.helpcentre)
    RelativeLayout helpcentre;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        x.view().inject(this);
        initview();
    }

    private void initview() {
     back.setOnClickListener(this);
        helpcentre.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setback:
               onBackPressed();
                break;
            case R.id.helpcentre:
                Log.e("111111","22222222");
                ForwardUtils.target(this, Constant.HELP,null);
                Log.e("00000000","444444");
                break;
        }
    }
}
