package com.huixiangtv.live.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.MainActivity;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.StringUtil;
import com.huixiangtv.live.utils.image.ImageUtils;


public class FragmentTabThree extends RootFragment{


	private View mRootView,llTitle;
	MainActivity activity ;

	ImageView ivPhoto;
	TextView tvUserName;
	TextView tvMySj;
	TextView tvFans;
	TextView haveFans;
	TextView tvAccount;
	TextView tvLoves;
	LinearLayout llUserTop;

	@Override
	protected View getLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView =  inflater.inflate(R.layout.fragment_tab_three, container, false);
		activity = (MainActivity)getActivity();
		activity.hideTitle(true);
		initView();
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
		mRootView.findViewById(R.id.tvUserName).setOnClickListener(this);
		mRootView.findViewById(R.id.setting).setOnClickListener(this);
		mRootView.findViewById(R.id.llTitle).setOnClickListener(this);


		ivPhoto = (ImageView) mRootView.findViewById(R.id.ivPhoto);
		tvUserName = (TextView) mRootView.findViewById(R.id.tvUserName);
		tvMySj = (TextView) mRootView.findViewById(R.id.tvMySj);
		tvFans = (TextView) mRootView.findViewById(R.id.tvFans);
		haveFans = (TextView) mRootView.findViewById(R.id.haveFans);
		tvAccount = (TextView) mRootView.findViewById(R.id.tvAccount);
		tvLoves = (TextView) mRootView.findViewById(R.id.tvLoves);
		llUserTop = (LinearLayout) mRootView.findViewById(R.id.llUserTop);
	}

	@Override
	protected void onNoDoubleClick(View view) {
		switch (view.getId()){
			case R.id.ivPhoto:
				ForwardUtils.target(getActivity(),"huixiang://userinfo",null);
				break;
			case R.id.llAccount:
				ForwardUtils.target(getActivity(), Constant.ACCOUNT,null);
				break;
			case R.id.tvUserName:
				ForwardUtils.target(getActivity(), Constant.LOGIN,null);
				break;
			case R.id.setting:
				ForwardUtils.target(getActivity(), Constant.SETINT,null);
				break;
			case R.id.llTitle:
				ForwardUtils.target(getActivity(), Constant.REG_LIVE_MAIN,null);
				break;
		}
	}


	public void onDelayLoad() {
		User user = App.getLoginUser();
		if (user != null) {
			tvUserName.setText(user.getNickName());
			if (null != user.getPhoto() && user.getPhoto().length() > 0) {
				ImageUtils.displayAvator(ivPhoto, user.getPhoto());
				//CommonHelper.viewSetBackage(user.getPhoto(),llUserTop);
			}

			tvMySj.setText(user.getCoins());
			tvFans.setText(StringUtil.isNotEmpty(user.getFans())?user.getFans():"100");
			haveFans.setText(StringUtil.isNotEmpty(user.getFans())?user.getFans():"3298");
			tvAccount.setText(user.getCoins());
			tvLoves.setText(user.getLoves());
		} else {
			tvMySj.setText("0");
			tvFans.setText("0");
			haveFans.setText("0");
			tvAccount.setText("0");
			tvLoves.setText("0");
			llUserTop.setBackgroundColor(getActivity().getResources().getColor(R.color.mainColor));

		}
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



}