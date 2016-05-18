package com.huixiang.live.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huixiang.live.Constant;
import com.huixiang.live.R;
import com.huixiang.live.activity.MainActivity;
import com.huixiang.live.utils.ForwardUtils;


public class FragmentTabThree extends RootFragment{


	private View mRootView,llTitle;
	MainActivity activity ;

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
		mRootView.findViewById(R.id.rlAccount).setOnClickListener(this);
		mRootView.findViewById(R.id.tvNickName).setOnClickListener(this);
		mRootView.findViewById(R.id.setting).setOnClickListener(this);
		mRootView.findViewById(R.id.llTitle).setOnClickListener(this);
	}

	@Override
	protected void onNoDoubleClick(View view) {
		switch (view.getId()){
			case R.id.ivPhoto:
				ForwardUtils.target(getActivity(),"huixiang://userinfo",null);
				break;
			case R.id.rlAccount:
				ForwardUtils.target(getActivity(), Constant.ACCOUNT,null);
				break;
			case R.id.tvNickName:
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


}
