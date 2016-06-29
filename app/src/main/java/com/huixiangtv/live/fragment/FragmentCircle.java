package com.huixiangtv.live.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.CircleAdapter;
import com.huixiangtv.live.utils.ForwardUtils;


public class FragmentCircle extends Fragment implements View.OnClickListener {

	View mRootView;


	ListView mListView;
	CircleAdapter adapter ;

	int page = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_circle, container, false);
		initView();
		return mRootView;
	}



	private void initView() {

		adapter = new CircleAdapter(getActivity());
		mListView.setAdapter(adapter);
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_circle_head, null, false);
		mListView.addHeaderView(view);

	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.rlUserInfo:
				if (null != App.getLoginUser()) {
					ForwardUtils.target(getActivity(), Constant.USERINFO, null);
				} else {
					ForwardUtils.target(getActivity(), Constant.LOGIN, null);
				}
				break;
			case R.id.rlAccount:
				if (null != App.getLoginUser()) {
					ForwardUtils.target(getActivity(), Constant.ACCOUNT, null);
				} else {
					ForwardUtils.target(getActivity(), Constant.LOGIN, null);
				}

				break;
			case R.id.rlLove:
				if (null != App.getLoginUser()) {
					ForwardUtils.target(getActivity(), Constant.MY_LOVES, null);
				} else {
					ForwardUtils.target(getActivity(), Constant.LOGIN, null);
				}

				break;
			case R.id.rlSetting:
				if (null != App.getLoginUser()) {
					ForwardUtils.target(getActivity(), Constant.SETINT, null);
				} else {
					ForwardUtils.target(getActivity(), Constant.LOGIN, null);
				}
				break;
			case R.id.rlFeedBack:
				ForwardUtils.target(getActivity(), Constant.FEEDBACK, null);
				break;
			case R.id.tvFans:
				if (null != App.getLoginUser()) {
					ForwardUtils.target(getActivity(), Constant.MY_FANS, null);
				} else {
					ForwardUtils.target(getActivity(), Constant.LOGIN, null);
				}

				break;
			case R.id.tvHots:
				if (null != App.getLoginUser()) {
					ForwardUtils.target(getActivity(), Constant.MY_HOTS, null);
				} else {
					ForwardUtils.target(getActivity(), Constant.LOGIN, null);
				}

				break;
		}
	}


	@Override
	public void onResume() {
		super.onResume();
	}
}
