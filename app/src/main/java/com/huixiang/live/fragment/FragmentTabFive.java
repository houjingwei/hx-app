package com.huixiang.live.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huixiang.live.R;


public class FragmentTabFive extends RootFragment {


	@Override
	protected void onNoDoubleClick(View view) {

	}

	@Override
	protected View getLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tab_five, container, false);
	}

	@Override
	protected void initLayout(View view) {

	}

	@Override
	protected void initData() {

	}
}
