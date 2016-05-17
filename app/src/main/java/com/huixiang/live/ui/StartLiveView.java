package com.huixiang.live.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.huixiang.live.R;
import com.huixiang.live.utils.KeyBoardUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hjw on 16/5/17.
 */
public class StartLiveView extends LinearLayout implements View.OnClickListener {



    LinearLayout llRoot;
    EditText etTitle;
    ImageView ivClose;
    TextView tvTheme;
    RadioButton rbPhone;
    RadioButton rbwx;
    RadioButton rbCircle;
    RadioButton rbSina;
    RadioButton rbQq;
    RadioButton rbQzone;
    LinearLayout llLocal;
    TextView tvLocal;
    TextView tvStart;


    LinkedList<RadioButton> buttons;
    Activity activity;


    //要分享的平台
    int platform = 0;
    //是否定位
    int local = 0;
    Context ct;
    public StartLiveView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.live_start_view, this);
        ct = context;
        initView();
    }

    public void setActivity(Activity ac){
        this.activity = ac;
    }

    private void initView() {
        llRoot = (LinearLayout) findViewById(R.id.llRoot);
        etTitle = (EditText) findViewById(R.id.etTitle);
        ivClose = (ImageView) findViewById(R.id.ivClose);
        tvTheme = (TextView) findViewById(R.id.tvTheme);

        rbPhone = (RadioButton) findViewById(R.id.rbPhone);
        rbwx = (RadioButton) findViewById(R.id.rbWx);
        rbCircle = (RadioButton) findViewById(R.id.rbCircle);
        rbSina = (RadioButton) findViewById(R.id.rbSina);
        rbQq = (RadioButton) findViewById(R.id.rbQq);
        rbQzone = (RadioButton) findViewById(R.id.rbQzone);


        llLocal = (LinearLayout) findViewById(R.id.llLocal);
        tvLocal = (TextView) findViewById(R.id.tvLocal);
        tvStart= (TextView) findViewById(R.id.tvStart);


        llRoot.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        tvTheme.setOnClickListener(this);

        rbPhone.setSelected(true);
        rbPhone.setOnClickListener(this);
        rbwx.setOnClickListener(this);
        rbCircle.setOnClickListener(this);
        rbSina.setOnClickListener(this);
        rbQq.setOnClickListener(this);
        rbQzone.setOnClickListener(this);
        llLocal.setOnClickListener(this);
        tvStart.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llRoot:
                hideKeyBoard();
                break;
            case R.id.ivClose:
                hideKeyBoard();
                onBackPressed();
                break;
            case R.id.rbPhone:
                hideKeyBoard();
                shareTo(1);
                break;
            case R.id.rbQq:
                hideKeyBoard();
                shareTo(2);
                break;
            case R.id.rbQzone:
                hideKeyBoard();
                shareTo(3);
                break;
            case R.id.rbWx:
                hideKeyBoard();
                shareTo(4);
                break;
            case R.id.rbCircle:
                hideKeyBoard();
                shareTo(5);
                break;
            case R.id.rbSina:
                hideKeyBoard();
                shareTo(6);
                break;
            case R.id.llLocal:
                if(local==1){
                    tvLocal.setText(R.string.localClose);
                    local = 0;
                }else{
                    tvLocal.setText(R.string.localOpen);
                    local = 1;
                }
                break;

        }
    }

    private void onBackPressed() {
        activity.onBackPressed();
    }


    /**
     * 隐藏键盘
     */
    private void hideKeyBoard() {
        KeyBoardUtils.closeKeybord(etTitle,ct);
    }


    private void shareTo(int flag) {

        buttons = new LinkedList<RadioButton>();
        RadioButton[] btns = new RadioButton[]{rbPhone,rbQq,rbQzone,rbwx,rbCircle,rbSina};
        for (int i=0;i<btns.length;i++){
            buttons.add(i,btns[i]);
        }
        platform = flag;
        if(platform==1){
            setSharePlatformStyle(buttons,0);
        }else if(platform==2){
            setSharePlatformStyle(buttons,1);
        }else if(platform==3){
            setSharePlatformStyle(buttons,2);
        }else if(platform==4){
            setSharePlatformStyle(buttons,3);
        }else if(platform==5){
            setSharePlatformStyle(buttons,4);
        }else if(platform==6){
            setSharePlatformStyle(buttons,5);
        }
    }



    /**
     * 设置radiobutton 选中或者不选中
     * @param buttons
     * @param flag
     */
    private void setSharePlatformStyle(List<RadioButton> buttons, int flag) {
        buttons.get(flag).setSelected(true);
        buttons.remove(flag);
        for (RadioButton rb : buttons) {
            rb.setSelected(false);
        }
    }

    public TextView getTvTheme() {
        return tvTheme;
    }

    public TextView getTvStart(){
        return tvStart;
    }


    public int getLocal() {
        return local;
    }

    public int getPlatform() {
        return platform;
    }
}
