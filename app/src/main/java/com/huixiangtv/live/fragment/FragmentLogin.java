package com.huixiangtv.live.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.utils.CommonHelper;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;


public class FragmentLogin extends Fragment implements View.OnClickListener {

	private View mRootView;
	EditText etAccount;
	EditText etPwd;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_login, container, false);
		initView();
		return mRootView;
	}

	private void initView() {

		etAccount = (EditText) mRootView.findViewById(R.id.etAccount);
		etPwd = (EditText) mRootView.findViewById(R.id.etPwd);
		mRootView.findViewById(R.id.tvLoginBtn).setOnClickListener(this);

		mRootView.findViewById(R.id.llQQ).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				login(SHARE_MEDIA.QQ);
			}
		});
		mRootView.findViewById(R.id.llWchat).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				login(SHARE_MEDIA.WEIXIN);
			}
		});
		mRootView.findViewById(R.id.llSina).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				login(SHARE_MEDIA.SINA);
			}
		});
	}


	private void login(SHARE_MEDIA platform) {
		App.mShareAPI.doOauthVerify(getActivity(), platform, umAuthListener);
	}


	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			//App.mShareAPI.deleteOauth(getActivity(), platform, umAuthListener);
			if( null != data && data.size()>0) {
				String refresh_token = data.get("refresh_token");
				String access_token = data.get("access_token");
				String uid = data.get("uid");
				AuthThirdlogin(access_token, platform.toString());
			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			Toast.makeText( getActivity(), "Authorize fail", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			Toast.makeText( getActivity(), "Authorize cancel", Toast.LENGTH_SHORT).show();
		}
	};


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tvLoginBtn:
				accountLogin();
				break;
		}
	}

	/**
	 * 账号登录
	 */
	private ColaProgress cp = null;
	private void accountLogin() {
		if(TextUtils.isEmpty(etAccount.getText().toString())){
			CommonHelper.showTip(getActivity(), "账号为空，请输入登录账号");
			etAccount.requestFocus();
			return;
		}else if(TextUtils.isEmpty(etPwd.getText().toString())){
			CommonHelper.showTip(getActivity(), "密码为空，请输入密码");
			etPwd.requestFocus();
			return;
		}
		cp = ColaProgress.show(getActivity(), "登录中", false, true, null);
		Map<String,String> giftParams = new HashMap<String, String>();
		giftParams.put("uid","001");
		giftParams.put("type","1");
		RequestUtils.sendPostRequest(Api.LOGIN, giftParams, new ResponseCallBack<User>() {
			@Override
			public void onSuccess(User data) {
				super.onSuccess(data);
				saveLoginInfo(data);
				if(null!=cp){
					cp.dismiss();
				}
			}

			@Override
			public void onFailure(ServiceException e) {
				super.onFailure(e);
				if(null!=cp){
					cp.dismiss();
				}
			}
		},User.class);
	}

	private void saveLoginInfo(User data) {
		App.saveLoginUser(data);
		getActivity().finish();
	}

	private void AuthThirdlogin(String code,String platform){
		cp = ColaProgress.show(getActivity(), "第三方登录中...", false, true, null);
		cp.setCancelable(true);
		Map<String,String> params = new HashMap<String, String>();
		params.put("code",code);
		params.put("platform",platform);
		RequestUtils.sendPostRequest(Api.AUTH_THIRDLOGIN, params, new ResponseCallBack<User>() {
			@Override
			public void onSuccess(User data) {
				super.onSuccess(data);
				Toast.makeText(getActivity(),"Successfully",Toast.LENGTH_LONG).show();
				saveLoginInfo(data);
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
			}
		}, User.class);

	}


}
