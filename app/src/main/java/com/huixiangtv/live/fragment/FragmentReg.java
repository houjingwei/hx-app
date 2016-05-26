package com.huixiangtv.live.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.common.CommonUtil;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.ColaProgress;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.KeyBoardUtils;

import java.util.HashMap;
import java.util.Map;


public class FragmentReg extends Fragment implements View.OnClickListener {




	private View mRootView;

	EditText etAccount;
	EditText etPwd;
	EditText etCode;

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
				CommonUtil.getMsgCode(etAccount.getText().toString(),getActivity());
				break;

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
		cp = ColaProgress.show(getActivity(), "登录中", false, true, null);
		Map<String,String> giftParams = new HashMap<String, String>();
		giftParams.put("phone",etAccount.getText().toString());
		giftParams.put("password",etPwd.getText().toString());
		giftParams.put("code",etCode.getText().toString());
		RequestUtils.sendPostRequest(Api.REG, giftParams, new ResponseCallBack<User>() {

			@Override
			public void onSuccess(User data) {
				super.onSuccess(data);
				saveLoginInfo(data);
				if(null!=cp){
					cp.dismiss();;
				}
			}

			@Override
			public void onFailure(ServiceException e) {
				super.onFailure(e);
				if(null!=cp){
					cp.dismiss();;
				}
			}
		},User.class);
	}

	private void saveLoginInfo(User data) {
		App.saveLoginUser(data);
		getActivity().finish();
	}
}
