package com.huixiangtv.liveshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.activity.MainActivity;
import com.huixiangtv.liveshow.utils.ForwardUtils;


public class FragmentTabTwo extends Fragment {


	private View mRootView;
	private TextView tvLoad;
	MainActivity activity ;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_tab_two, container, false);

		activity = (MainActivity)getActivity();
		tvLoad = (TextView) mRootView.findViewById(R.id.load);
		return mRootView;
	}




}
