package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.PayCoinAdapter;
import com.huixiangtv.live.model.Coin;
import com.huixiangtv.live.model.PayMode;
import com.huixiangtv.live.pop.PayModeWindow;
import com.huixiangtv.live.pop.UpdateSexWindow;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountActivity extends BaseBackActivity implements View.OnClickListener{




    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.tvBalance)
    TextView tvBalance;

    @ViewInject(R.id.tvXieyi)
    TextView tvXieyi;


    @ViewInject(R.id.listView)
    ListView listView;


    PayCoinAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        x.view().inject(this);
        initView();
        initData();
    }

    private void initData() {
        if(null!= App.getLoginUser()){
            tvBalance.setText(App.userCoin+"");
        }else{
            tvBalance.setText("0");
        }

        adapter = new PayCoinAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Coin coin = (Coin) adapter.getItem(position);
                choisePayWay(coin);


            }
        });
        Map params = new HashMap<>();
        params.put("platform", Constant.PLATFORM);
        RequestUtils.sendPostRequest(Api.COIN_LIST, params, new ResponseCallBack<Coin>() {
            @Override
            public void onSuccessList(List<Coin> data) {
                super.onSuccessList(data);
                adapter.addList(data);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, Coin.class);




    }


    private ColaProgress cp = null;
    private void choisePayWay(final Coin coin) {
        //cp = ColaProgress.show(AccountActivity.this, "加载支付方式", false, true, null);
        //加载支付方式
        Map<String, String> map = new HashMap<String, String>();
        map.put("platform", "Android");
        map.put("version", App.deviceVersion);
        RequestUtils.sendPostRequest(Api.PAY_MODE, map, new ResponseCallBack<PayMode>() {
            @Override
            public void onSuccessList(List<PayMode> data) {
                super.onSuccessList(data);
                if(null!=cp){
                    cp.dismiss();
                }
                showPayPop(coin,data);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                if(null!=cp){
                    cp.dismiss();
                }
                List<PayMode> data = new ArrayList<PayMode>();
                for (int i=0;i<3;i++){
                    PayMode m = new PayMode();
                    m.setCode(i+1+"");
                    m.setName("支付宝");
                    m.setPid(i+1+"");
                    data.add(m);
                }
                showPayPop(coin,data);
            }
        },PayMode.class);



    }

    private void showPayPop(final Coin coin, List<PayMode> data) {
        PayModeWindow payModeWin = new PayModeWindow(AccountActivity.this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,data,coin);
        payModeWin.showAtLocation(findViewById(R.id.main), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        payModeWin.update();
        payModeWin.setListener(new PayModeWindow.SelectPayModeListener() {
            @Override
            public void select(final PayMode payMode) {
                super.select(payMode);
                toPay(coin,payMode);
            }
        });
    }

    private void toPay(Coin coin, PayMode payMode) {
        CommonHelper.showTip(AccountActivity.this,coin.getName()+payMode.getName());
    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.moneyBag));
        tvXieyi.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvXieyi:
                ForwardUtils.target(AccountActivity.this,"http://www.baidu.com?title=支付协议",null);
                break;


        }
    }


}