package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.GridViewGroupInfoAdapter;
import com.huixiangtv.liveshow.model.DynamicImage;
import com.huixiangtv.liveshow.model.Member;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;
import com.huixiangtv.liveshow.utils.widget.GridViewCircle;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stone on 16/7/18.
 */
public class GroupInfoActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.gv_group_info)
    GridViewCircle gridViewCircle;

    @ViewInject(R.id.rlAllMember)
    RelativeLayout rlAllMember;

    private GridViewGroupInfoAdapter adapter;
    private int page = 1;
    private String pageSize = "5";

    String groupId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_chat_info);
        x.view().inject(this);
        groupId = getIntent().getStringExtra("groupId");
        initView();
        initData();
    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.group_chat_info));

        rlAllMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map = new HashMap<>();
                map.put("groupId",groupId);
                ForwardUtils.target(GroupInfoActivity.this, Constant.GROUP_MEMBER_LIST_ACTIVITY, map);

            }
        });
    }



    protected void initData() {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", page + "");
        paramsMap.put("pageSize", pageSize + "");
        paramsMap.put("gid", groupId);


        RequestUtils.sendPostRequest(Api.GET_GROUP_MEMBER, paramsMap, new ResponseCallBack<Member>() {
            @Override
            public void onSuccessList(List<Member> data) {
                if(null==data) {
                    data = new ArrayList<Member>();
                }
                Member m = new Member();
                data.add(m);
                adapter = new GridViewGroupInfoAdapter(GroupInfoActivity.this,data);
                gridViewCircle.setAdapter(adapter);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, Member.class);




    }

}
