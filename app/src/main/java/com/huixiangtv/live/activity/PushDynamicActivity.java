package com.huixiangtv.live.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.service.LoginCallBack;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.widget.WidgetUtil;

import java.util.ArrayList;
import java.util.List;

public class PushDynamicActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout llContent;
    private RelativeLayout switchWrapper;
    private RelativeLayout switchTrigger;
    private TextView switchLabel;



    int width = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_dynamic);

        initView();

        initImagePicker();//最好放到 Application oncreate执行
        
    }

    private void initImagePicker() {

    }

    private void initView() {

        width = (App.screenWidth - WidgetUtil.dip2px(this,40))/4;

        llContent = (LinearLayout) findViewById(R.id.llContent);
        LinearLayout.LayoutParams photoParams = new LinearLayout.LayoutParams(width,width);
        photoParams.leftMargin= WidgetUtil.dip2px(this,5);


        ImageView addPhotoImageView = new ImageView(this);
        addPhotoImageView.setImageDrawable(getResources().getDrawable(R.mipmap.icon_dynamic_photo));
        addPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choisePhoto();
            }
        });
        llContent.addView(addPhotoImageView,photoParams);

        ImageView addVideoImageView = new ImageView(this);
        addVideoImageView.setImageDrawable(getResources().getDrawable(R.mipmap.icon_dynamic_video));
        addVideoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiseVideo();
            }
        });
        llContent.addView(addVideoImageView,photoParams);



        switchWrapper = (RelativeLayout) findViewById(R.id.switchWrapper);
        switchTrigger = (RelativeLayout) findViewById(R.id.switchTrigger);
        switchLabel = (TextView) findViewById(R.id.switchLabel);
        switchWrapper.setOnClickListener(this);//添加switch喊话事件


    }

    private void choiseVideo() {
        Log.i("choise","video");
    }

    public static final int REQUEST_CODE_SELECT = 100;
    private void choisePhoto() {
        Log.i("choise","photo");

    }


    List selImageList = new ArrayList<>();
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.switchWrapper:
                if (null == App.getLoginUser()) {
                    CommonHelper.showLoginPopWindow(PushDynamicActivity.this, R.id.liveMain, new LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            localOpen();
                        }
                    });
                    return;
                }
                localOpen();
                break;

        }
    }


    /**
     * 定位开关切换
     */
    private void localOpen() {
        GradientDrawable gd = (GradientDrawable) switchWrapper.getBackground();
        String triggerTag = switchTrigger.getTag().toString();
        float offset = WidgetUtil.dip2px(this,12);

        if ("open_yes".equals(triggerTag)) {
            //开关状态
            gd.setColor(getResources().getColor(R.color.gray));
            switchLabel.setTextColor(getResources().getColor(R.color.gray));
            switchTrigger.setTag("open_no");
            switchLabel.setText("关");
            ObjectAnimator anim = ObjectAnimator.ofFloat(switchTrigger, "translationX",0.0f,-offset);
            anim.setDuration(300l);
            anim.start();

        } else {
            //关闭状态
            gd.setColor(getResources().getColor(R.color.mainColor));
            switchLabel.setTextColor(getResources().getColor(R.color.mainColor));
            switchTrigger.setTag("open_yes");
            switchLabel.setText("开");
            ObjectAnimator anim = ObjectAnimator.ofFloat(switchTrigger, "translationX",-offset,0.0f);
            anim.setDuration(300l);
            anim.start();

        }
    }

}