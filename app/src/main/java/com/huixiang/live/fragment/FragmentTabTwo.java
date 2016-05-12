package com.huixiang.live.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huixiang.live.R;
import com.huixiang.live.activity.MainActivity;
import com.huixiang.live.utils.ForwardUtils;


public class FragmentTabTwo extends Fragment {


	private View mRootView;
	private TextView tvLoad;
	MainActivity activity ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_tab_two, container, false);

		activity = (MainActivity)getActivity();
		activity.setTitleBar(getString(R.string.tabtwo_title));
		activity.hideTitle(false);
		tvLoad = (TextView) mRootView.findViewById(R.id.load);

		tvLoad.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ForwardUtils.target(getActivity(),"huixiang://refresh");
			}
		});
		return mRootView;
	}

}
