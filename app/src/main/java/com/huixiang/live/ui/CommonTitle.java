package com.huixiang.live.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiang.live.R;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/5/17.
 */
public class CommonTitle extends RelativeLayout {

    TextView tvTitle;
    ImageView ivBack;
    TextView tvSave;

    public CommonTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.activity_base_title, this);
        tvTitle = (TextView) findViewById(R.id.title);
        ivBack = (ImageView) findViewById(R.id.back);
        tvSave = (TextView) findViewById(R.id.save);
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                ((Activity) getContext()).finish();
            }
        });
    }

    public void setTitleText(String str) {
        tvTitle.setText(str);
    }

    public void backShow(int flag) {
        ivBack.setVisibility(flag);
    }

    public void saveShow(int flag){
        tvSave.setVisibility(flag);
    }

    public TextView getSave(){
       return tvSave;
    }



}
