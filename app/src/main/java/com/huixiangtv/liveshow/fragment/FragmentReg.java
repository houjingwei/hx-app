package com.huixiangtv.liveshow.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.callback.CodeCallBack;
import com.huixiangtv.liveshow.common.CommonUtil;
import com.huixiangtv.liveshow.model.User;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.ColaProgress;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;
import com.huixiangtv.liveshow.utils.KeyBoardUtils;
import com.huixiangtv.liveshow.utils.RSAUtils;

import java.util.HashMap;
import java.util.Map;


public class FragmentReg extends Fragment implements View.OnClickListener {




	private View mRootView;

	EditText etAccount;
	EditText etPwd;
	EditText etCode;

	TextView tvXieyi;

	private TextView tvGetCode;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_reg, container, false);
		initView();
		return mRootView;
	}

	private void initView() {

		etAccount = (EditText) mRootView.findViewById(R.id.etAccount);
		etCode = (EditText) mRootView.findViewById(R.id.etCode);
		etPwd = (EditText) mRootView.findViewById(R.id.etPwd);
		mRootView.findViewById(R.id.tvRegBtn).setOnClickListener(this);

		tvGetCode = (TextView) mRootView.findViewById(R.id.tvGetCode);
		tvGetCode.setOnClickListener(this);

		tvXieyi = (TextView) mRootView.findViewById(R.id.tvXieyi);
		tvXieyi.setOnClickListener(this);

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tvRegBtn:
				accountReg();
				break;
			case R.id.tvGetCode:
				if(TextUtils.isEmpty(etAccount.getText().toString())){
					CommonHelper.showTip(getActivity(),"请输入手机号码");
					etAccount.requestFocus();
					return;
				}
				KeyBoardUtils.closeKeybord(etAccount,getActivity());
				tvGetCode.setEnabled(false);
				cp = ColaProgress.show(getActivity(), "正在获取", false, true, null);
				CommonUtil.getMsgCode(etAccount.getText().toString(),new CodeCallBack(){
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
								CommonHelper.showTip(getActivity(),"验证码发送失败，失败原因："+msg);
							}
						}

				);
				break;
			case R.id.tvXieyi:
				ForwardUtils.target(getActivity(), Constant.REG_PRO,null);
				break;

		}
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


	/**
	 * 账号注册
	 */
	private ColaProgress cp = null;
	private void accountReg() {
		if(TextUtils.isEmpty(etAccount.getText().toString())){
			CommonHelper.showTip(getActivity(), "账号为空，请输入登录账号");
			etAccount.requestFocus();
			return;
		}else if(TextUtils.isEmpty(etPwd.getText().toString())){
			CommonHelper.showTip(getActivity(), "密码为空，请输入密码");
			etPwd.requestFocus();
			return;
		}else if(TextUtils.isEmpty(etCode.getText().toString())){
			CommonHelper.showTip(getActivity(), "验证码为空，请输入验证码");
			etCode.requestFocus();
			return;
		}
		cp = ColaProgress.show(getActivity(), "注册中", false, true, null);
		Map<String,String> giftParams = new HashMap<String, String>();
		giftParams.put("phone",etAccount.getText().toString());
		String pwd = RSAUtils.rsaPwd(etPwd.getText().toString());
		giftParams.put("password",etPwd.getText().toString());
		giftParams.put("code",etCode.getText().toString());
		RequestUtils.sendPostRequest(Api.REG, giftParams, new ResponseCallBack<User>() {

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
}