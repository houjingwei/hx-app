package com.huixiangtv.live.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.MainActivity;
import com.huixiangtv.live.model.Other;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.StringUtil;
import com.huixiangtv.live.utils.TokenChecker;
import com.huixiangtv.live.utils.image.ImageUtils;

import java.util.HashMap;
import java.util.Map;


public class FragmentTabThree extends RootFragment{


	private View mRootView;
	MainActivity activity ;

	ImageView ivPhoto;
	TextView tvUserName;
	TextView tvHot;
	TextView tvFans;
	TextView haveFans;
	TextView tvAccount;
	TextView tvLoves;
	private RelativeLayout rlSign;
	RelativeLayout llUserTop;

	@Override
	protected View getLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView =  inflater.inflate(R.layout.fragment_tab_three, container, false);
		activity = (MainActivity)getActivity();
		activity.hideTitle(true);
		initView();
		getUserStatus();
		return mRootView;
	}

	@Override
	protected void initLayout(View view) {

	}

	@Override
	protected void initData() {

	}


	private void initView() {

		mRootView.findViewById(R.id.ivPhoto).setOnClickListener(this);
		mRootView.findViewById(R.id.llAccount).setOnClickListener(this);
		mRootView.findViewById(R.id.llLoves).setOnClickListener(this);
		mRootView.findViewById(R.id.tvUserName).setOnClickListener(this);
		mRootView.findViewById(R.id.setting).setOnClickListener(this);
		mRootView.findViewById(R.id.llTitle).setOnClickListener(this);
		mRootView.findViewById(R.id.llMyfans).setOnClickListener(this);
		mRootView.findViewById(R.id.llfaned).setOnClickListener(this);
		mRootView.findViewById(R.id.llHot).setOnClickListener(this);


		ivPhoto = (ImageView) mRootView.findViewById(R.id.ivPhoto);
		tvUserName = (TextView) mRootView.findViewById(R.id.tvUserName);
		tvHot = (TextView) mRootView.findViewById(R.id.tvHot);
		tvFans = (TextView) mRootView.findViewById(R.id.tvFans);
		haveFans = (TextView) mRootView.findViewById(R.id.haveFans);
		tvAccount = (TextView) mRootView.findViewById(R.id.tvAccount);
		tvLoves = (TextView) mRootView.findViewById(R.id.tvLoves);
		llUserTop = (RelativeLayout) mRootView.findViewById(R.id.llUserTop);



	}

	@Override
	protected void onNoDoubleClick(View view) {
		switch (view.getId()){
			case R.id.ivPhoto:
				if(null!=App.getLoginUser()){
					ForwardUtils.target(getActivity(),Constant.USERINFO,null);
				}else{
					ForwardUtils.target(getActivity(), Constant.LOGIN,null);
				}
				break;
			case R.id.llAccount:
				ForwardUtils.target(getActivity(), Constant.ACCOUNT,null);
				break;
			case R.id.llLoves:
				ForwardUtils.target(getActivity(), Constant.MY_LOVES,null);
				break;
			case R.id.tvUserName:
				if(null!=App.getLoginUser()){
					ForwardUtils.target(getActivity(),Constant.USERINFO,null);
				}else{
					ForwardUtils.target(getActivity(), Constant.LOGIN,null);
				}
				break;
			case R.id.setting:
				ForwardUtils.target(getActivity(), Constant.SETINT,null);
				break;
			case R.id.llTitle:
				if(TokenChecker.checkToken(getActivity()))
				   ForwardUtils.target(getActivity(), Constant.PIC_LIST,null);
				break;
			case R.id.llMyfans:
				ForwardUtils.target(getActivity(), Constant.MY_FANS,null);
				break;
			case R.id.llfaned:
				ForwardUtils.target(getActivity(), Constant.FANED_ME,null);
				break;
			case R.id.llHot:
				ForwardUtils.target(getActivity(), Constant.MY_HOTS,null);
				break;

		}
	}


	public void onDelayLoad() {




		User user = App.getLoginUser();
		if (user != null) {
			tvUserName.setText(user.getNickName());
			if (null != user.getPhoto() && user.getPhoto().length() > 0) {
				ImageUtils.displayAvator(ivPhoto, user.getPhoto());
				CommonHelper.viewSetBackageImag(user.getPhoto(),llUserTop);
			}


			CommonHelper.myFansCount(new ApiCallback<Other>() {
				@Override
				public void onSuccess(Other data) {
					if(null!=data){
						haveFans.setText(data.getCollects());
						tvFans.setText(data.getFans());
						tvHot.setText(data.getHots());
					}

				}

				@Override
				public void onFailure(ServiceException e) {
					super.onFailure(e);

				}
			});



			tvAccount.setText(user.getCoins());
			tvLoves.setText(user.getLoves());
//			Map<String, String> params = new HashMap<String, String>();
//			RequestUtils.sendPostRequest(Api.USER_INFO, params, new ResponseCallBack<User>() {
//				@Override
//				public void onSuccess(User user) {
//					super.onSuccess(user);
//					App.saveLoginUser(user);
//					resetUserInfo(user);
//				}
//
//				@Override
//				public void onFailure(ServiceException e) {
//					super.onFailure(e);
//				}
//			}, User.class);
		} else {
			tvHot.setText("0");
			tvFans.setText("0");
			haveFans.setText("0");
			tvAccount.setText("0");
			tvLoves.setText("0");
			ivPhoto.setImageResource(R.drawable.default_head);
			tvUserName.setText("未登录");
			llUserTop.setBackgroundColor(getActivity().getResources().getColor(R.color.mainColor));

		}
	}

	private void resetUserInfo(User user) {
		tvUserName.setText(user.getNickName());
		if (null != user.getPhoto() && user.getPhoto().length() > 0) {
			ImageUtils.displayAvator(ivPhoto, user.getPhoto());
			CommonHelper.viewSetBackageImag(user.getPhoto(),llUserTop);
		}



		CommonHelper.myFansCount(new ApiCallback<Other>() {
			@Override
			public void onSuccess(Other data) {
				if(null!=data){
					tvFans.setText(data.getCollects());
					haveFans.setText(data.getHots());
				}

			}

			@Override
			public void onFailure(ServiceException e) {
				super.onFailure(e);

			}
		});



		tvAccount.setText(user.getCoins());
		tvLoves.setText(user.getLoves());
	}


	@Override
	public void onResume() {
		super.onResume();
		getView().postDelayed(new Runnable() {
			@Override
			public void run() {
				onDelayLoad();
			}
		}, 300);
	}


	/**
	 * get user status by auth
	 */
	public   void getUserStatus() {
		try {
			String token = App.getPreferencesValue("token");
			String uid = App.getPreferencesValue("uid");

			Map<String, String> params = new HashMap<String, String>();
			params.put("uid", uid);
			params.put("token", token);

			RequestUtils.sendPostRequest(Api.USER_GETAUTHSTATUS, params, new ResponseCallBack<String>() {
				@Override
				public void onSuccess(String str) {
					super.onSuccess(str);
					switch (str)
					{
						case  "0":
							showToast("未认证");
							break;
						case "1":
							showToast("已认证");
							break;
						case "2":
							showToast("认证中");
							break;
						case "-1":
							showToast("认证不通过");
							break;
					}

				}

				@Override
				public void onFailure(ServiceException e) {
					super.onFailure(e);
				}
			}, String.class);

		}catch (Exception ex)
		{

		}
	}


}
