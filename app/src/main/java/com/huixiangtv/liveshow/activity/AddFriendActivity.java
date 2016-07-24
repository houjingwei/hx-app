package com.huixiangtv.liveshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

public class AddFriendActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.tvApplyMsg)
    EditText tvApplyMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        x.view().inject(this);
        initView();
    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText("新的朋友");
        commonTitle.saveShow(View.VISIBLE);
        commonTitle.setSaveText("发送");
        commonTitle.getSave().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });
    }


    /**
     * 添加朋友
     */
    private void addFriend() {

        Intent intent=new Intent();
        intent.putExtra("msg",tvApplyMsg.getText().toString());
        setResult(RESULT_OK, intent);
        onBackPressed();

    }
}
