package com.huixiangtv.liveshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.Tag;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.ColaProgress;
import com.huixiangtv.liveshow.ui.ColaProgressTip;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ShowUtils;
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
        loadData();


    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText("标签搜索");
        commonTitle.saveShow(View.VISIBLE);
        tvSave = commonTitle.getSave();
        tvSave.setOnClickListener(this);



    }



    private void loadData() {

        Map<String,String> params = new HashMap<String,String>();
        params.put("type","1");
        RequestUtils.sendPostRequest(Api.USER_TAG, params, new ResponseCallBack<Tag>() {
            @Override
            public void onSuccessList(List<Tag> data) {
                super.onSuccessList(data);
                initTags(data);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(UserTagActivity.this,e.getMessage());
            }
        },Tag.class);


    }


    String[] tags = null;
    private void initTags(List<Tag> data) {
        Set<Integer> selectedSet = new HashSet<Integer>();
        if(null!=data && data.size()>0){
            tags = new String[data.size()];
            for (int i=0;i<data.size();i++){
                tags[i] = data.get(i).getName();
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
                    Intent intent=new Intent();
                    intent.putExtra("topic",tags);
                    setResult(RESULT_OK, intent);
                    onBackPressed();
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
