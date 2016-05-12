package com.huixiang.live.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiang.live.R;
import com.huixiang.live.ui.ColaProgress;
import com.huixiang.live.ui.ColaProgressTip;
import com.huixiang.live.utils.ShowUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class UserTagActivity extends BaseBackActivity implements View.OnClickListener {

    @ViewInject(R.id.title)
    TextView tvTitle;
    @ViewInject(R.id.back)
    ImageView ivBack;
    @ViewInject(R.id.save)
    TextView tvSave;



    @ViewInject(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;
    TagAdapter<String> adapter ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tag);
        x.view().inject(this);
        initView();
        initData();
    }

    private void initView() {
        ivBack.setOnClickListener(this);
        tvTitle.setText(R.string.tagSearch);
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);


    }

    String[] tags = null;
    private void initData() {
        tags = new String[50];
        for (int i = 0; i < 50; i++) {
            tags[i] = "标签"+(i+1);
        }
        final LayoutInflater mInflater = LayoutInflater.from(UserTagActivity.this);
        mFlowLayout.setMaxSelectCount(4);
        adapter = new TagAdapter<String>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag,mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        };
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if(mFlowLayout.getSelectedList().size()==4){
                    ShowUtils.showTip(UserTagActivity.this, "最多选择四个标签~");
                }
                return false;
            }
        });

        mFlowLayout.setAdapter(adapter);


    }


    private ColaProgress cp = null;
    ColaProgressTip cpTip = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.save:
                for(int key:mFlowLayout.getSelectedList()){
                    Toast.makeText(UserTagActivity.this ,tags[key], Toast.LENGTH_SHORT).show();
                }

                cp = ColaProgress.show(UserTagActivity.this, "保存印象", false, true, null);

                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if(cp!=null && cp.isShowing()){
            cp.dismiss();
        }
        if(cpTip!=null && cpTip.isShowing()){
            cpTip.dismiss();
        }
        super.onDestroy();
    }
}
