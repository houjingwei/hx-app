package com.huixiang.live.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;


import com.huixiang.live.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class SetActivity extends BaseActivity{

    @ViewInject(R.id.responseword)
    TextView response;
    @ViewInject(R.id.safe)
    TextView safe;
    @ViewInject(R.id.aboutus)
    TextView aboutus;
    @ViewInject(R.id.help)
    TextView help;
    @ViewInject(R.id.clearcache)
    TextView clearcache;
    @ViewInject(R.id.cachelarge)
    TextView cachelarge;
    @ViewInject(R.id.exitlogin)
    TextView exitlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        x.view().inject(this);
        response.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        safe.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        aboutus.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        help.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        clearcache.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        cachelarge.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        exitlogin.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
    }
}
