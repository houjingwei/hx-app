package com.huixiangtv.live.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.common.CommonUtil;
import com.huixiangtv.live.pay.weichat.WeiChatConstants;
import com.huixiangtv.live.pay.weichat.WxPayUtils;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.ForwardUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Stone on 16/5/18.
 */
public class RegLiveMainActivity extends BaseBackActivity {


    @ViewInject(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;
    TagAdapter<String> adapter ;

    @ViewInject(R.id.tvRegTitle)
    TextView tvRegTitle;

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;


    @ViewInject(R.id.txtHqyzm)
    TextView hqyzm;

    @ViewInject(R.id.txtPhone)
    TextView phone;

    @ViewInject(R.id.etJob)
    EditText etJob;


    @ViewInject(R.id.tvNext)
    TextView tvNext;


    private boolean isyzm = true;
    private MyCount mc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_main);
        x.view().inject(this);
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.txt_authentication));
        Drawable drawable = getResources().getDrawable(R.drawable.res_ioc);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvRegTitle.setCompoundDrawables(drawable, null, null, null);

        tvNext.setOnClickListener(this);
        hqyzm.setOnClickListener(this);
        initTags();

    }

    @Override
    protected void onNoDoubleClick(View view) {
        super.onNoDoubleClick(view);

        switch (view.getId()) {


            case R.id.tvNext:
                ForwardUtils.target(RegLiveMainActivity.this, Constant.REG_LIVE, null);
                break;

            case R.id.txtHqyzm:
                if (CommonUtil.isMobileNum(phone.getText().toString()) && isyzm) {
                    isyzm = false;
                    hqyzm.setEnabled(false);
                    if (mc == null) {
                        // 第一参数是总的时间，第二个是间隔时间 都是毫秒为单位
                        mc = new MyCount(60 * 1000, 1000);
                    }
                    mc.start();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    String[] tags = null;
    private void initTags() {
        tags = new String[6];
        for (int i = 0; i < 6; i++) {
            tags[i] = "标签"+(i+1);
        }
        final LayoutInflater mInflater = LayoutInflater.from(RegLiveMainActivity.this);
        mFlowLayout.setMaxSelectCount(1);
        adapter = new TagAdapter<String>(tags) {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                final Button tv = (Button) mInflater.inflate(R.layout.tag_button,mFlowLayout, false);
                tv.setText(s);
                tv.setBackground(getResources().getDrawable(R.drawable.tag_bg));
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // etJob.setText(tv.getText());
                    }
                });
                return tv;
            }
        };

        mFlowLayout.setAdapter(adapter);


    }


    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            hqyzm.setText("重新获取");
            hqyzm.setTextColor(Color.parseColor("#FFFFFF"));
            hqyzm.setBackgroundResource(R.drawable.button_radius);
            isyzm = true;
            hqyzm.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int i = (int) millisUntilFinished / 1000;
            hqyzm.setText("(" + i + "秒)");
            hqyzm.setTextColor(Color.parseColor("#bcbcbc"));
            hqyzm.setBackgroundColor(getResources().getColor(R.color.white));

        }
    }


//    private void startWxPay(String appId, String prepayId, String nonceStr, String partnerId,
//                            String timeStamp, String packageValue, String paySignature) {
//        WeiChatConstants.APP_ID = appId;
//        WxPayUtils.pay(RegLiveMainActivity.this, appId, prepayId, nonceStr, partnerId, timeStamp, packageValue, paySignature);
//    }


}
