package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.AccountBindInfoModel;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.ForwardUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends BaseBackActivity implements View.OnClickListener {


    private ColaProgress cp = null;

    //@ViewInject(R.id.setback)
    //ImageView back;

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.qqbind)
    TextView qqbind;

    @ViewInject(R.id.weibobind)
    TextView weibobind;

    @ViewInject(R.id.weixinbind)
    TextView weixinbind;

    @ViewInject(R.id.helpcentre)
    RelativeLayout helpcentre;

    @ViewInject(R.id.tvLoginOut)
    TextView tvLoginOut;

    @ViewInject(R.id.rlPhoenBind)
    RelativeLayout rlPhoenBind;

    @ViewInject(R.id.rlUpdatePwd)
    RelativeLayout rlUpdatePwd;

    @ViewInject(R.id.rlQqBind)
    RelativeLayout rlQqBind;

    @ViewInject(R.id.rlWxBind)
    RelativeLayout rlWxBind;

    @ViewInject(R.id.rlSinaBind)
    RelativeLayout rlSinaBind;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        x.view().inject(this);
        initview();

        loadData();
    }

    private void loadData() {
        auth_Getaccount_Bindinfo();
    }

    private void initview() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.set));


        helpcentre.setOnClickListener(this);
        tvLoginOut.setOnClickListener(this);
        rlPhoenBind.setOnClickListener(this);
        rlUpdatePwd.setOnClickListener(this);
        rlQqBind.setOnClickListener(this);
        rlWxBind.setOnClickListener(this);
        rlSinaBind.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLoginOut:
                App.clearLoginUser();
                onBackPressed();
                break;
            case R.id.helpcentre:
                ForwardUtils.target(this, Constant.HELP, null);
                break;
            case R.id.rlPhoenBind:
                Map<String,String> params = new HashMap<String,String>();
                ForwardUtils.target(SettingActivity.this,Constant.PHONE_BIND,params);
                break;
            case R.id.rlUpdatePwd:
                break;
            case R.id.rlQqBind:

                break;
            case R.id.rlWxBind:

                break;
            case R.id.rlSinaBind:

                break;
        }
    }


    private void auth_Getaccount_Bindinfo() {
        String token = App.getPreferencesValue("token");
        String uid = App.getPreferencesValue("uid");
        cp = ColaProgress.show(SettingActivity.this, "正在加载...", false, true, null);
        Map<String, String> params = new HashMap<String, String>();

        RequestUtils.sendPostRequest(Api.AUTH_GETACCOUNT_BINDINFO, params, new ResponseCallBack<AccountBindInfoModel>() {
            @Override
            public void onSuccess(AccountBindInfoModel data) {
                super.onSuccess(data);
                if (data != null) {
                    setBindandUnbandGB("phone", data.phone);
                    setBindandUnbandGB("qq", data.qq);
                    setBindandUnbandGB("wx", data.wx);
                    setBindandUnbandGB("wb", data.wb);
                    Toast.makeText(SettingActivity.this, "Loading BindInfo is Successfully", Toast.LENGTH_LONG).show();
                }

                if (null != cp) {
                    cp.dismiss();
                }

            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                if (null != cp) {
                    cp.dismiss();
                }
                Toast.makeText(SettingActivity.this, "Loading BindInfo is Failed", Toast.LENGTH_LONG).show();
            }
        }, AccountBindInfoModel.class);

    }


    private void Auth_Account_Bind(String platform, String assessToken) {
        String token = App.getPreferencesValue("token");
        String uid = App.getPreferencesValue("uid");
        cp = ColaProgress.show(SettingActivity.this, "正在加载...", false, true, null);
        cp.setCancelable(false);
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("uid", uid);
        params.put("platform", platform);
        params.put("verifyCode", "");
        params.put("phone", "");
        params.put("code", assessToken);


        RequestUtils.sendPostRequest(Api.AUTH_ACCOUNTBIND, params, new ResponseCallBack<AccountBindInfoModel>() {
            @Override
            public void onSuccess(AccountBindInfoModel data) {
                super.onSuccess(data);
                if (data != null) {
                    setBindandUnbandGB("phone", data.phone);
                    setBindandUnbandGB("qq", data.qq);
                    setBindandUnbandGB("wx", data.wx);
                    setBindandUnbandGB("wb", data.wb);
                    Toast.makeText(SettingActivity.this, "Bind is Successfully", Toast.LENGTH_LONG).show();
                }

                if (null != cp) {
                    cp.dismiss();
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                if (null != cp) {
                    cp.dismiss();
                }
                Toast.makeText(SettingActivity.this, "Bind is Failed", Toast.LENGTH_LONG).show();
            }
        }, AccountBindInfoModel.class);
    }


    /**
     * 绑定的时候调用
     *
     * @param platform
     */
    private void login_Platform(SHARE_MEDIA platform) {
        App.mShareAPI.doOauthVerify(SettingActivity.this, platform, umAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            App.mShareAPI.getPlatformInfo(SettingActivity.this, platform, umAuthListener);
            //App.mShareAPI.deleteOauth(getActivity(), platform, umAuthListener);
            if (null != data && data.size() > 0) {
                String refresh_token = data.get("refresh_token");
                String access_token = data.get("access_token");
                String uid = data.get("uid");
                //授权成功，下一步 绑定账号
                Auth_Account_Bind(platform.toString(), access_token);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(SettingActivity.this, "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(SettingActivity.this, "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };


    /**
     * 设置绑定图标
     * @param type
     * @param status
     */
    private void setBindandUnbandGB(String type, String status) {
        switch (type) {
            case "qq":
                setTVBg(qqbind, status,rlQqBind);
                break;
            case "wb":
                setTVBg(weibobind, status,rlSinaBind);
                break;
            case "wx":
                setTVBg(weixinbind, status,rlSinaBind);
                break;
        }
    }


    private void setTVBg(View view, String tag, RelativeLayout rootView) {
        if (tag == "1") {
            rootView.setClickable(false);
            view.setClickable(false);
            view.setBackgroundResource(R.drawable.set_bind);
        } else {
            view.setBackgroundResource(R.drawable.set_qqbind);
        }
    }


}
