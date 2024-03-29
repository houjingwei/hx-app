package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.ui.CommonTitle;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Stone on 16/5/17.
 */
public class RegLiveSuccessActivity extends BaseBackActivity {

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;


    @ViewInject(R.id.tvMsg)
    TextView tvMsg;


    @ViewInject(R.id.tvNext)
    TextView tvNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_successfully);
        x.view().inject(this);
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.txt_authentication));
        tvNext.setOnClickListener(this);
        String exchange = getResources().getString(R.string.txt_reg_success_info);

        tvMsg.setText(Html.fromHtml(exchange));



    }

    @Override
    protected void onNoDoubleClick(View view) {
        super.onNoDoubleClick(view);

        switch (view.getId()) {
            case R.id.tvNext:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
