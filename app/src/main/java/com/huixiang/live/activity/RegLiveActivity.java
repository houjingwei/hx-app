package com.huixiang.live.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huixiang.live.Constant;
import com.huixiang.live.R;
import com.huixiang.live.ui.CommonTitle;
import com.huixiang.live.utils.ForwardUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Stone on 16/5/17.
 */
public class RegLiveActivity extends BaseBackActivity {


    @ViewInject(R.id.back)
    ImageView ivBack;

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.tvRegTitle)
    TextView tvRegTitle;

    @ViewInject(R.id.tvNext)
    TextView tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_live);
        x.view().inject(this);
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.txt_authentication));
        Drawable drawable = getResources().getDrawable(R.drawable.res_ioc);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvRegTitle.setCompoundDrawables(drawable, null, null, null);
        tvNext.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void onNoDoubleClick(View view) {
        super.onNoDoubleClick(view);

        switch (view.getId()) {

            case R.id.back:
                onBackPressed();
                break;

            case R.id.tvNext:
                ForwardUtils.target(RegLiveActivity.this, Constant.REG_LIVE_NEXT, null);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

}
