package com.huixiangtv.live.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.MainActivity;
import com.huixiangtv.live.utils.ForwardUtils;


public class FragmentTabTwo extends RootFragment {


	private View mRootView;
	private TextView tvLoad;
	MainActivity activity ;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_tab_two, container, false);

		activity = (MainActivity)getActivity();

		activity.hideTitle(false);
		tvLoad = (TextView) mRootView.findViewById(R.id.load);
		tvLoad.setOnClickListener(this);
		return mRootView;
	}

	@Override
	protected void onNoDoubleClick(View view) {

		switch (view.getId())
		{
			case R.id.load:
				ForwardUtils.target(getActivity(),"huixiang://refresh",null);
				break;
		}
	}



}
