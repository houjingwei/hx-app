package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Dynamic;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.DateUtils;
import com.huixiangtv.live.utils.StringUtil;
import com.huixiangtv.live.utils.image.ImageUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

public class DynamicDetialActivity extends BaseBackActivity {

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.ivPhoto)
    ImageView ivPhoto;

    @ViewInject(R.id.tvName)
    TextView tvName;

    @ViewInject(R.id.tvContent)
    TextView tvContent;

    @ViewInject(R.id.tvTime)
    TextView tvTime;

    @ViewInject(R.id.mRecylerView)
    RecyclerView mRecylerView;

    String did;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detial);
        x.view().inject(this);
        did = getIntent().getStringExtra("did");
        intitView();
        if (StringUtil.isNotNull(did)) {
            initData();
        } else {
            onBackPressed();
        }
    }

    /**
     * 加载数据
     */
    private void initData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("did", did);
        RequestUtils.sendPostRequest(Api.DYNAMIC_DETIAL, map, new ResponseCallBack<Dynamic>() {
            @Override
            public void onSuccess(Dynamic data) {
                super.onSuccess(data);
                upViewData(data);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);

            }
        }, Dynamic.class);
    }

    private void upViewData(Dynamic data) {
        ImageUtils.display(ivPhoto,data.getPhoto());
        tvName.setText(data.getNickName());
        tvContent.setText(data.getContent());
        tvTime.setText(DateUtils.formatDisplayTime(data.getDate(),"yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * view设置
     */
    private void intitView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.dynamic_detial));
    }
}
