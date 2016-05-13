package com.huixiang.live.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.huixiang.live.Constant;
import com.huixiang.live.R;
import com.huixiang.live.utils.ForwardUtils;
import com.huixiang.live.utils.KeyBoardUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class StartLiveActivity extends BaseBackActivity implements View.OnClickListener {


    @ViewInject(R.id.llRoot)
    LinearLayout llRoot;

    @ViewInject(R.id.etTitle)
    EditText etTitle;

    @ViewInject(R.id.ivClose)
    ImageView ivClose;

    @ViewInject(R.id.tvTheme)
    TextView tvTheme;


    @ViewInject(R.id.rbPhone)
    RadioButton rbPhone;
    @ViewInject(R.id.rbWx)
    RadioButton rbwx;
    @ViewInject(R.id.rbCircle)
    RadioButton rbCircle;
    @ViewInject(R.id.rbSina)
    RadioButton rbSina;
    @ViewInject(R.id.rbQq)
    RadioButton rbQq;
    @ViewInject(R.id.rbQzone)
    RadioButton rbQzone;

    LinkedList<RadioButton> buttons;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_live);
        x.view().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        initView();

    }

    private void initView() {
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
            case R.id.tvTheme:
                hideKeyBoard();
                choiseTheme();
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
        }
    }

    int platform = 1;
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




    /**
     * 选择主题
     */
    private void choiseTheme() {
        ForwardUtils.target(StartLiveActivity.this, Constant.LIVE_TOPIC);
    }


    /**
     * 隐藏键盘
     */
    private void hideKeyBoard() {
        KeyBoardUtils.closeKeybord(etTitle,StartLiveActivity.this);
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && requestCode==1) {
                    String tid = data.getStringExtra("tid");
                    String topic= data.getStringExtra("topic");
                    tvTheme.setText("# "+topic+" #");
                }
                break;

            default:
                break;
        }
    }
}
