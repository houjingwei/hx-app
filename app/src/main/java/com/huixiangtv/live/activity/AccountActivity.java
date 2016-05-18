package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.huixiangtv.live.R;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.widget.WidgetUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class AccountActivity extends BaseBackActivity implements View.OnClickListener{




    @ViewInject(R.id.llBuyCoin)
    LinearLayout llBuyCoin;

    @ViewInject(R.id.rbwx)
    RadioButton rbwx;
    @ViewInject(R.id.rbzfb)
    RadioButton rbzfb;
    @ViewInject(R.id.rbapple)
    RadioButton rbapple;


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        x.view().inject(this);
        initView();

        initData();
    }

    private void initData() {
        LayoutInflater inflater = LayoutInflater.from(AccountActivity.this);
        for (int i=0 ;i <10;i++){

            View view = inflater.inflate(R.layout.buy_coin_item,null);
            RelativeLayout rlBuy = (RelativeLayout) view.findViewById(R.id.rl_buy);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)rlBuy.getLayoutParams();
            params.height = WidgetUtil.dip2px(AccountActivity.this,45);
            rlBuy.setLayoutParams(params);
            llBuyCoin.addView(view);
        }

    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.moneyBag));


        rbwx.setSelected(true);
        rbwx.setOnClickListener(this);
        rbzfb.setOnClickListener(this);
        rbapple.setOnClickListener(this);
    }



    int payWay = 0;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.rbwx:
                choisePayWay(1);
                break;
            case R.id.rbzfb:
                choisePayWay(2);
                break;
            case R.id.rbapple:
                choisePayWay(3);
                break;
        }
    }

    private void choisePayWay(int way) {
        payWay = way;
        if(payWay==1){
            rbwx.setSelected(true);
            rbzfb.setSelected(false);
            rbapple.setSelected(false);
        }else if(payWay==2){
            rbwx.setSelected(false);
            rbzfb.setSelected(true);
            rbapple.setSelected(false);
        }else if(payWay==3){
            rbwx.setSelected(false);
            rbzfb.setSelected(false);
            rbapple.setSelected(true);
        }
    }

}
