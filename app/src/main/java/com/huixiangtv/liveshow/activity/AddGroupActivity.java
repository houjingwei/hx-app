package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.ClearEditText;
import com.huixiangtv.liveshow.ui.ColaProgress;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stone on 16/7/22.
 */
public class AddGroupActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.tvGroupName)
    ClearEditText tvGroupName;
    private ColaProgress cp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_friends);
        x.view().inject(this);
        initView();
    }

    private void initView() {

        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.create_group));
        commonTitle.setSaveText("完成");
        commonTitle.saveShow(View.VISIBLE);
        commonTitle.getSave().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null != tvGroupName && tvGroupName.getText().toString().trim().length() > 0) {

                    createGroup(tvGroupName.getText().toString());

                } else {

                    CommonHelper.showTip(AddGroupActivity.this, "请填写群名称");
                }

            }
        });


    }


    /**
     *
     * @param gName
     */
    private void createGroup(String gName) {
        cp = ColaProgress.show(AddGroupActivity.this, "正在保存", true, false, null);
        Map<String, String> params = new HashMap<String, String>();
        params.put("gName", gName);
        RequestUtils.sendPostRequest(Api.CREATE_GROUP, params, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                if(cp.isShowing())
                    cp.dismiss();

                CommonHelper.showTip(AddGroupActivity.this, "创建群组成功");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        App.refreshGrouplist = true;
                        onBackPressed();
                    }
                }, 1000);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                if(cp.isShowing())
                    cp.dismiss();
                CommonHelper.showTip(AddGroupActivity.this, "创建群组失败: "+e.getMessage());
            }
        }, String.class);
    }

}
