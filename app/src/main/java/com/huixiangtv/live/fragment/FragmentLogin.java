package com.huixiangtv.live.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.callback.CodeCallBack;
import com.huixiangtv.live.common.CommonUtil;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.KeyBoardUtils;
import com.huixiangtv.live.utils.RSAUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;


public class FragmentLogin extends Fragment implements View.OnClickListener {

	private View mRootView;
	EditText etAccount;
	EditText etPwd;

	LinearLayout llLogin;



	LinearLayout llForgot;
	EditText etPhone;
	EditText etCode;
	EditText etForgotPwd;
	TextView tvGetCode;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_login, container, false);
		initView();
		return mRootView;
	}

	private void initView() {
		mRootView.findViewById(R.id.tvForgotPwd).setOnClickListener(this);
		etAccount = (EditText) mRootView.findViewById(R.id.etAccount);
		etPwd = (EditText) mRootView.findViewById(R.id.etPwd);
		mRootView.findViewById(R.id.tvLoginBtn).setOnClickListener(this);
		llLogin = (LinearLayout) mRootView.findViewById(R.id.llLogin);
		llForgot = (LinearLayout) mRootView.findViewById(R.id.llForgot);
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



		//找回密码相关
		etPhone = (EditText) mRootView.findViewById(R.id.etPhone);
		etCode = (EditText) mRootView.findViewById(R.id.etCode);
		etForgotPwd = (EditText) mRootView.findViewById(R.id.etForgotPwd);
		tvGetCode = (TextView) mRootView.findViewById(R.id.tvGetCode);
		tvGetCode.setOnClickListener(this);
		mRootView.findViewById(R.id.tvCommitBtn).setOnClickListener(this);
		mRootView.findViewById(R.id.tvHaveAccount).setOnClickListener(this);




	}


	private void login(SHARE_MEDIA platform) {
//		App.mShareAPI.getPlatformInfo(getActivity(), platform, umAuthListener);
		App.mShareAPI.doOauthVerify(getActivity(), platform, umAuthListener);
	}


	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			//App.mShareAPI.deleteOauth(getActivity(), platform, umAuthListener);
			if( null != data && data.size()>0) {
				String access_token = data.get("access_token");
				String openId = "";
				String flag = "";
				if(platform==SHARE_MEDIA.QQ){
					flag="2";
				}else if(platform==SHARE_MEDIA.WEIXIN){
					flag="3";
					openId = data.get("openid");
				}else if(platform==SHARE_MEDIA.SINA){
					flag="4";
				}
				AuthThirdlogin(access_token, flag,openId);
			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			CommonHelper.showTip(getActivity(),"授权失败："+t.getMessage());
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			CommonHelper.showTip(getActivity(),"取消登录");
		}
	};


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tvLoginBtn:
				accountLogin();
				break;
			case R.id.tvForgotPwd:
				showForgotPwd(true);
				break;
			case R.id.tvHaveAccount:
				showForgotPwd(false);
				break;
			case R.id.tvCommitBtn:
				updatePwd();
				break;
			case R.id.tvGetCode:
				getCode();
				break;

		}
	}

	private void updatePwd() {
		if(TextUtils.isEmpty(etPhone.getText().toString())){
			CommonHelper.showTip(getActivity(), "账号为空，请输入登录账号");
			etPhone.requestFocus();
			return;
		}else if(TextUtils.isEmpty(etCode.getText().toString())){
			CommonHelper.showTip(getActivity(), "验证码为空，请输入密码");
			etCode.requestFocus();
			return;
		}else if(TextUtils.isEmpty(etForgotPwd.getText().toString())){
			CommonHelper.showTip(getActivity(), "密码为空，请输入密码");
			etForgotPwd.requestFocus();
			return;
		}
		cp = ColaProgress.show(getActivity(), "登录中", false, true, null);
		Map<String,String> params = new HashMap<String, String>();
		String pwd = RSAUtils.rsaPwd(etForgotPwd.getText().toString());
		params.put("phone",etPhone.getText().toString());
		params.put("password",etForgotPwd.getText().toString());
		params.put("code",etCode.getText().toString());

		RequestUtils.sendPostRequest(Api.UP_PWD, params, new ResponseCallBack<String>() {
			@Override
			public void onSuccess(String data) {
				super.onSuccess(data);
				if(null!=cp){
					cp.dismiss();
				}
				CommonHelper.showTip(getActivity(),"密码重置成功，请登录");
				showForgotPwd(false);
			}

			@Override
			public void onFailure(ServiceException e) {
				super.onFailure(e);
				if(null!=cp){
					cp.dismiss();
				}
				CommonHelper.showTip(getActivity(),e.getMessage());
			}
		},String.class);
	}

	private void showForgotPwd(boolean bool) {
		if(bool){
			llLogin.setVisibility(View.GONE);
			llForgot.setVisibility(View.VISIBLE);
		}else{
			llLogin.setVisibility(View.VISIBLE);
			llForgot.setVisibility(View.GONE);
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
		Map<String,String> params = new HashMap<String, String>();
		String pwd = RSAUtils.rsaPwd(etPwd.getText().toString());
		params.put("phone",etAccount.getText().toString());
		params.put("password", etPwd.getText().toString());

		RequestUtils.sendPostRequest(Api.LOGIN, params, new ResponseCallBack<User>() {
			@Override
			public void onSuccess(User data) {
				super.onSuccess(data);
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
				CommonHelper.showTip(getActivity(), e.getMessage());
			}
		}, User.class);
	}

	private void saveLoginInfo(User data) {
		App.saveLoginUser(data);
		getActivity().finish();
	}

	private void AuthThirdlogin(String code,String platform,String openid){
		cp = ColaProgress.show(getActivity(), "第三方登录中", false, true, null);
		cp.setCancelable(true);
		Map<String,String> params = new HashMap<String, String>();
		params.put("code",code);
		params.put("platform",platform);
		params.put("openid", openid);
		RequestUtils.sendPostRequest(Api.AUTH_THIRDLOGIN, params, new ResponseCallBack<User>() {
			@Override
			public void onSuccess(User data) {
				super.onSuccess(data);
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
				CommonHelper.showTip(getActivity(), e.getMessage());
			}
		}, User.class);

	}




	public void getCode() {
		if(TextUtils.isEmpty(etPhone.getText().toString())){
			CommonHelper.showTip(getActivity(),"请输入手机号码");
			etPhone.requestFocus();
			return;
		}
		KeyBoardUtils.closeKeybord(etPhone,getActivity());
		tvGetCode.setEnabled(false);
		cp = ColaProgress.show(getActivity(), "正在获取", false, true, null);
		CommonUtil.getMsgCode(etCode.getText().toString(),new CodeCallBack(){
					@Override
					public void sendSuccess() {
						if(null!=cp){
							cp.dismiss();
						}
						CommonHelper.showTip(getActivity(),"验证码发送成功");
						myThread = new MyThread();
						myThread.run();

					}

					@Override
					public void sendError(String msg) {
						if(null!=cp){
							cp.dismiss();
						}
						tvGetCode.setEnabled(true);
						CommonHelper.showTip(getActivity(),"验证码发送失败："+msg);
					}
				}

		);
	}


	public int state = 1; //状态 1 表示未启动线程或正在运行线程。0 停止线程
	private MyThread myThread;
	class MyThread extends Thread {
		@Override
		public void run() {
			super.run();
			new AsyncTask<Object, Object, Object>() {
				@Override
				protected Object doInBackground(Object... arg0) {
					for (int i = 60; i >= 0; i--) {
						if (state == 0) { // 停止线程
							return null;
						}
						if (i == 0) {
							publishProgress("获取验证码");
						} else {
							// 剩余多少秒
							publishProgress("剩余" + (i - 1) + "秒");
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(Object result) {
					super.onPostExecute(result);
				}

				@Override
				protected void onProgressUpdate(Object... values) {
					super.onProgressUpdate(values);
//					GradientDrawable myGrad = (GradientDrawable) tvGetCode.getBackground();
					if (values.length > 0 && values[0] != null) {
						if ("剩余59秒".equals(values[0])) {
//							myGrad.setColor(getActivity().getResources().getColor(R.color.gray));
						} else if ("获取验证码".equals(values[0])) {
							tvGetCode.setEnabled(true);
//							myGrad.setColor(getActivity().getResources().getColor(R.color.orange));
						}
						tvGetCode.setText(values[0].toString());
					}
				}
			}.execute();
		}
	}
}
