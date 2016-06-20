package com.huixiangtv.live.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
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
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.OauthHelper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SettingActivity extends BaseBackActivity implements View.OnClickListener {


    private ColaProgress cp = null;

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.qqbind)
    TextView qqbind;

    @ViewInject(R.id.weibobind)
    TextView weibobind;

    @ViewInject(R.id.weixinbind)
    TextView weixinbind;

    @ViewInject(R.id.rlHelp)
    RelativeLayout rlHelp;

    @ViewInject(R.id.rlUs)
    RelativeLayout rlUs;

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
        rlHelp.setOnClickListener(this);
        rlUs.setOnClickListener(this);
        tvLoginOut.setOnClickListener(this);
        rlPhoenBind.setOnClickListener(this);
        rlUpdatePwd.setOnClickListener(this);
        rlQqBind.setOnClickListener(this);
        rlWxBind.setOnClickListener(this);
        rlSinaBind.setOnClickListener(this);
        weixinbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_Platform(SHARE_MEDIA.WEIXIN, weixinbind);
            }
        });

        qqbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_Platform(SHARE_MEDIA.QQ, qqbind);
            }
        });

        weibobind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_Platform(SHARE_MEDIA.SINA, weibobind);
            }
        });
        if(null!=App.getLoginUser()){
            tvLoginOut.setVisibility(View.VISIBLE);
        }

        App.mShareAPI = UMShareAPI.get(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLoginOut:
                App.clearLoginUser();
                onBackPressed();
                ForwardUtils.target(SettingActivity.this, Constant.LOGIN, null);
                break;
            case R.id.rlHelp:
                ForwardUtils.target(SettingActivity.this, Constant.HELP, null);
                break;
            case R.id.rlUs:
                ForwardUtils.target(SettingActivity.this, Constant.GYWM, null);
                break;
            case R.id.rlPhoenBind:
                Map<String, String> params = new HashMap<String, String>();
                ForwardUtils.target(SettingActivity.this, Constant.PHONE_BIND, params);
                break;
            case R.id.rlUpdatePwd:
                break;
            case R.id.rlQqBind:
                login_Platform(SHARE_MEDIA.QQ, qqbind);
                break;
            case R.id.rlWxBind:
                login_Platform(SHARE_MEDIA.WEIXIN, weixinbind);
                break;
            case R.id.rlSinaBind:
                login_Platform(SHARE_MEDIA.SINA, weibobind);
                break;
        }
    }


    private void auth_Getaccount_Bindinfo() {
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
                Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, AccountBindInfoModel.class);

    }


    private void Auth_Account_Bind(final SHARE_MEDIA platform, String assessToken, final String actionType, String openid) {

        String flag = "";
        if (platform == SHARE_MEDIA.QQ) {
            flag = "2";
        } else if (platform == SHARE_MEDIA.WEIXIN) {
            flag = "3";
        } else if (platform == SHARE_MEDIA.SINA) {
            flag = "4";
        }
        cp = ColaProgress.show(SettingActivity.this, "正在加载...", false, true, null);
        cp.setCancelable(false);
        Map<String, String> params = new HashMap<String, String>();
        params.put("platform", flag);

        if (flag.equals("3"))
            params.put("openid", openid);

        params.put("code", assessToken);
        params.put("actionType", actionType);


        RequestUtils.sendPostRequest(Api.AUTH_ACCOUNTBIND, params, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                if (data != null) {

                    if (actionType.equals("1")) {
                        App.mShareAPI.deleteOauth(SettingActivity.this, platform, delumAuthListener);
                        if (platform == SHARE_MEDIA.QQ) {
                            qqbind.setBackgroundDrawable(getResources().getDrawable(R.drawable.set_bind));
                            qqbind.setTextAppearance(SettingActivity.this, R.style.white_normal_style);
                            qqbind.setText("绑定");
                        } else if (platform == SHARE_MEDIA.WEIXIN) {
                            weixinbind.setBackgroundDrawable(getResources().getDrawable(R.drawable.set_bind));
                            weixinbind.setTextAppearance(SettingActivity.this, R.style.white_normal_style);
                            weixinbind.setText("绑定");
                        } else if (platform == SHARE_MEDIA.SINA) {
                            weibobind.setBackgroundDrawable(getResources().getDrawable(R.drawable.set_bind));
                            weibobind.setTextAppearance(SettingActivity.this, R.style.white_normal_style);
                            weibobind.setText("绑定");
                        }
                    } else {

                        if (platform == SHARE_MEDIA.QQ) {
                            qqbind.setBackgroundDrawable(getResources().getDrawable(R.drawable.set_qqbind));
                            qqbind.setTextAppearance(SettingActivity.this, R.style.black_normal_style);
                            qqbind.setText("解除绑定");
                        } else if (platform == SHARE_MEDIA.WEIXIN) {
                            weixinbind.setBackgroundDrawable(getResources().getDrawable(R.drawable.set_qqbind));
                            weixinbind.setTextAppearance(SettingActivity.this, R.style.black_normal_style);
                            weixinbind.setText("解除绑定");
                        } else if (platform == SHARE_MEDIA.SINA) {
                            weibobind.setBackgroundDrawable(getResources().getDrawable(R.drawable.set_qqbind));
                            weibobind.setTextAppearance(SettingActivity.this, R.style.black_normal_style);
                            weibobind.setText("解除绑定");
                        }
                    }
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
                Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, String.class);
    }


    /**
     * 绑定的时候调用
     *
     * @param platform
     */
    private void login_Platform(SHARE_MEDIA platform, TextView view) {

        if (view.getText().equals("绑定")) {
            App.mShareAPI.doOauthVerify(this, platform, umAuthListener);

        } else {
            App.mShareAPI.doOauthVerify(this, platform, delumAuthListener);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        App.mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    private UMAuthListener delumAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (null != data && data.size() > 0) {
                String access_token = data.get("access_token");
                String openid = data.get("openid");
                String uid = data.get("uid");
                //授权成功，下一步 绑定账号
                Auth_Account_Bind(platform, access_token, "1", openid);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(SettingActivity.this, "del Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(SettingActivity.this, "del Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };


    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            if (null != data && data.size() > 0) {
                String access_token = data.get("access_token");
                String openid = data.get("openid");
                String uid = data.get("uid");
                //授权成功，下一步 绑定账号
                Auth_Account_Bind(platform, access_token, "0", openid);
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
     *
     * @param type
     * @param status
     */
    private void setBindandUnbandGB(String type, String status) {
        switch (type) {
            case "qq":
                setTVBg(qqbind, status, rlQqBind);
                break;
            case "wb":
                setTVBg(weibobind, status, rlSinaBind);
                break;
            case "wx":
                setTVBg(weixinbind, status, rlWxBind);
                break;
        }
    }


    private void setTVBg(TextView view, String tag, RelativeLayout rootView) {
        if (tag.equals("1")) {
//            rootView.setClickable(false);
//            view.setClickable(false);
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.set_qqbind));
            view.setTextAppearance(SettingActivity.this, R.style.black_normal_style);
            view.setText("解绑");
        }
        if (tag.equals("0")) {
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.set_bind));
            view.setTextAppearance(SettingActivity.this, R.style.white_normal_style);
            view.setText("绑定");

        }
    }


}
