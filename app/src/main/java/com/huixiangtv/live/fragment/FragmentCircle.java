package com.huixiangtv.live.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.CircleAdapter;
import com.huixiangtv.live.ui.HuixiangLoadingLayout;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.ImageUtils;


public class FragmentCircle extends Fragment {

	View mRootView;


	private PullToRefreshListView refreshView;
	CircleAdapter adapter ;



	int page = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_circle, container, false);
		initView();
		return mRootView;
	}



	private void initView() {
		refreshView = (PullToRefreshListView) mRootView.findViewById(R.id.refreshView);
		refreshView.setMode(PullToRefreshBase.Mode.BOTH);
		refreshView.setHeaderLayout(new HuixiangLoadingLayout(getActivity()));
		refreshView.setFooterLayout(new HuixiangLoadingLayout(getActivity()));
		refreshView.setAdapter(adapter);
		adapter = new CircleAdapter(getActivity());
		refreshView.setAdapter(adapter);
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_circle_head, null, false);
		refreshView.getRefreshableView().addHeaderView(view);

		//点击进入自己的相册圈
		view.findViewById(R.id.ivPhoto).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (null != App.getLoginUser()) {
					ForwardUtils.target(getActivity(), Constant.OWN_CIRCLE, null);
				}
			}
		});
		initHeadInfo(view);

		loadData();
		refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						page = 1;
						loadData();
					}
				}, 1000);

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				new Handler().postDelayed(new Runnable() {
					public void run() {

						page++;
						loadData();
					}
				}, 1000);

			}
		});

	}

	private void loadData() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				refreshView.onRefreshComplete();
			}
		}, 1000);
	}


	private void initHeadInfo(View view) {
		ImageView ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
		TextView tvNickName = (TextView) view.findViewById(R.id.tvNickName);
		if(null!=App.getLoginUser()){
			tvNickName.setText(App.getLoginUser().getNickName());
			ImageUtils.displayAvator(ivPhoto,App.getLoginUser().getPhoto());
		}
	}




	@Override
	public void onResume() {
		super.onResume();
	}
}
