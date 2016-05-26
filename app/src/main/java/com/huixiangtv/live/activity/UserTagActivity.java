package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.R;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.ui.ColaProgressTip;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.ShowUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserTagActivity extends BaseBackActivity implements View.OnClickListener {

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;
    private TextView tvSave;


    @ViewInject(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;
    TagAdapter<String> adapter;


    String userTags = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tag);
        x.view().inject(this);
        initView();
        userTags = getIntent().getStringExtra("tags");
        initData();


    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText("标签搜索");
        commonTitle.saveShow(View.VISIBLE);
        tvSave = commonTitle.getSave();
        tvSave.setOnClickListener(this);



    }

    String[] tags = null;

    private void initData() {
        Set<Integer> selectedSet = new HashSet<Integer>();
        tags = new String[50];
        for (int i = 0; i < 50; i++) {
            tags[i] = "标签" + (i + 1);
            for (String tag:userTags.split(",")){
                if(tag.equals(tags[i])){
                   selectedSet.add(i);
                }
            }
        }
        final LayoutInflater mInflater = LayoutInflater.from(UserTagActivity.this);
        mFlowLayout.setMaxSelectCount(4);
        adapter = new TagAdapter<String>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag, mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        };
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (mFlowLayout.getSelectedList().size() == 4) {
                    ShowUtils.showTip(UserTagActivity.this, "最多选择四个标签~");
                }
                return false;
            }
        });
        mFlowLayout.setAdapter(adapter);
        if(null!=selectedSet && selectedSet.size()>0){
            adapter.setSelectedList(selectedSet);
        }
    }


    private ColaProgress cp = null;
    ColaProgressTip cpTip = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                if (mFlowLayout.getSelectedList().size() == 0) {
                    ShowUtils.showTip(UserTagActivity.this, "请选择标签~");
                }else{
                    Map<String,String> params = new HashMap<String,String>();
                    String tags = getTag();
                    ShowUtils.showTip(UserTagActivity.this, "等待提供接口~"+tags);
                    //RequestUtils.sendPostRequest(Api.SAVE_USER_TAG,params,);
                }
                break;
        }
    }


    /**
     * 用户标签字符串
     */
    private String getTag(){
        String selectTagStr = "";
        StringBuffer sb = new StringBuffer("");
        for(int key:mFlowLayout.getSelectedList()){
            sb.append(tags[key]+",");
        }
        if(null!=sb && sb.length()>1){
            selectTagStr = (String) sb.subSequence(0,sb.length()-1);
        }
        return selectTagStr;
    }



    @Override
    protected void onDestroy() {
        if (cp != null && cp.isShowing()) {
            cp.dismiss();
        }
        if (cpTip != null && cpTip.isShowing()) {
            cpTip.dismiss();
        }
        super.onDestroy();
    }




}
