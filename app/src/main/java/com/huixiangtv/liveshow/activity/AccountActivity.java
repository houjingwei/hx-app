package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.PayCoinAdapter;
import com.huixiangtv.liveshow.model.Coin;
import com.huixiangtv.liveshow.model.PayMode;
import com.huixiangtv.liveshow.pop.PayModeWindow;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.ColaProgress;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;

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
        cp = ColaProgress.show(AccountActivity.this, "加载支付方式", false, true, null);
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
        if(payMode.getPid().equals("alipay")){

        }else if(payMode.getPid().equals("wx")){

        }
        CommonHelper.showTip(AccountActivity.this,coin.getName()+payMode.getName());
    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.moneyBag));

        commonTitle.saveShow(View.VISIBLE);
        commonTitle.getSave().setText("充值记录");
        commonTitle.getSave().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForwardUtils.target(AccountActivity.this, Constant.ORDER,null);
            }
        });

        tvXieyi.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvXieyi:
                ForwardUtils.target(AccountActivity.this, Constant.PAY_PRO,null);
                break;


        }
    }


}
