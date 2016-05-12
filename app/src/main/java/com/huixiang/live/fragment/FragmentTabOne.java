package com.huixiang.live.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huixiang.live.R;
import com.huixiang.live.activity.MainActivity;


public class FragmentTabOne extends Fragment {




	private View mRootView;
	MainActivity activity ;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_tab_one, container, false);

		activity = (MainActivity)getActivity();
		activity.setTitleBar(getString(R.string.tabone_title));
		activity.hideTitle(false);
		return mRootView;
	}






}
