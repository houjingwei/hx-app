package com.huixiang.live.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.huixiang.live.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class MyFansActivity extends BaseBackActivity implements View.OnClickListener{

    @ViewInject(R.id.myfansback)
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans);
        x.view().inject(this);
        initview();
    }

    private void initview() {
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myfansback:
                onBackPressed();
                break;
        }
    }
}
