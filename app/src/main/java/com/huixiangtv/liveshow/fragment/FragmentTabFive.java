package com.huixiangtv.liveshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.huixiangtv.liveshow.R;


public class FragmentTabFive extends RootFragment {


	@Override
	protected void onNoDoubleClick(View view) {

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tab_five, container, false);
	}

}
