package com.huixiangtv.live.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.BaseBackActivity;
import com.huixiangtv.live.model.AccountBindInfoModel;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.PreferencesHelper;
import com.huixiangtv.live.utils.StringUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SetActivity extends BaseBackActivity implements View.OnClickListener{


    private ColaProgress cp = null;

    @ViewInject(R.id.setback)
    ImageView back;
    @ViewInject(R.id.qqbind)
    TextView qqbind;

    @ViewInject(R.id.weibobind)
    TextView weibobind;

    @ViewInject(R.id.weixinbind)
    TextView weixinbind;

    @ViewInject(R.id.helpcentre)
    RelativeLayout helpcentre;
    @ViewInject(R.id.setexit)
    Button setexit;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        x.view().inject(this);
        initview();
    }

    private void initview() {
     back.setOnClickListener(this);
        helpcentre.setOnClickListener(this);
        setexit.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Auth_Getaccount_Bindinfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setback:
               onBackPressed();
                break;
            case R.id.setexit:
                onBackPressed();
                break;
            case R.id.helpcentre:
                ForwardUtils.target(this, Constant.HELP,null);
                break;
        }
    }


    private void Auth_Getaccount_Bindinfo(){

        String token = App.getPreferencesValue("token");
        String uid = App.getPreferencesValue("uid");
        cp = ColaProgress.show(SetActivity.this, "正在加载...", false, true, null);
        cp.setCancelable(false);
        Map<String,String> params = new HashMap<String, String>();
        params.put("token",token);
        params.put("uid",uid);
        RequestUtils.sendPostRequest(Api.AUTH_GETACCOUNT_BINDINFO, params, new ResponseCallBack<AccountBindInfoModel>() {
            @Override
            public void onSuccess(AccountBindInfoModel data) {
                super.onSuccess(data);
                if(data!=null)
                {
                    setBindandUnbandGB("phone",data.phone);
                    setBindandUnbandGB("qq",data.qq);
                    setBindandUnbandGB("wx",data.wx);
                    setBindandUnbandGB("wb",data.wb);
                    Toast.makeText(SetActivity.this, "Loading BindInfo is Successfully", Toast.LENGTH_LONG).show();
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
                Toast.makeText(SetActivity.this, "Loading BindInfo is Failed", Toast.LENGTH_LONG).show();
            }
        }, AccountBindInfoModel.class);

    }


    private void Auth_Account_Bind(String platform,String assessToken){


        String token = App.getPreferencesValue("token");
        String uid = App.getPreferencesValue("uid");
        cp = ColaProgress.show(SetActivity.this, "正在加载...", false, true, null);
        cp.setCancelable(false);
        Map<String,String> params = new HashMap<String, String>();
        params.put("token",token);
        params.put("uid",uid);
        params.put("platform", platform);
        params.put("verifyCode","");
        params.put("phone","");
        params.put("code",assessToken);


        RequestUtils.sendPostRequest(Api.AUTH_ACCOUNTBIND, params, new ResponseCallBack<AccountBindInfoModel>() {
            @Override
            public void onSuccess(AccountBindInfoModel data) {
                super.onSuccess(data);
                if(data!=null)
                {
                    setBindandUnbandGB("phone",data.phone);
                    setBindandUnbandGB("qq",data.qq);
                    setBindandUnbandGB("wx",data.wx);
                    setBindandUnbandGB("wb",data.wb);
                    Toast.makeText(SetActivity.this, "Bind is Successfully", Toast.LENGTH_LONG).show();
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
                Toast.makeText(SetActivity.this, "Bind is Failed", Toast.LENGTH_LONG).show();
            }
        }, AccountBindInfoModel.class);
    }


    /**
     * 绑定的时候调用
     * @param platform
     */
    private void login_Platform(SHARE_MEDIA platform) {
        App.mShareAPI.doOauthVerify(SetActivity.this, platform, umAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            App.mShareAPI.getPlatformInfo(SetActivity.this,platform,umAuthListener);
            //App.mShareAPI.deleteOauth(getActivity(), platform, umAuthListener);
            if( null != data && data.size()>0) {
                String refresh_token = data.get("refresh_token");
                String access_token = data.get("access_token");
                String uid = data.get("uid");
                //授权成功，下一步 绑定账号
                Auth_Account_Bind(platform.toString(),access_token);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(SetActivity.this, "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(SetActivity.this, "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

    private void setBindandUnbandGB(String type,String status)
    {

        switch (type)
        {
            case "phone":
                setTVBg(qqbind,status);
                break;
            case "qq":
                setTVBg(qqbind,status);
                break;
            case "wb":
                setTVBg(weibobind,status);
                break;
            case "wx":
                setTVBg(weixinbind,status);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setTVBg(View view,String tag)
    {
         if(tag == "1")
         {
             view.setBackground(getResources().getDrawable(R.drawable.set_bind));
         }
        else
         {
             view.setBackground(getResources().getDrawable(R.drawable.set_qqbind));
         }
    }



}
