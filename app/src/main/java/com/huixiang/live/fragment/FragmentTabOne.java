package com.huixiang.live.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.huixiang.live.R;
import com.huixiang.live.activity.MainActivity;
import com.huixiang.live.model.PositionAdvertBO;
import com.huixiang.live.utils.widget.BannerView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


public class FragmentTabOne extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

	private BannerView bannerView;
	private BGARefreshLayout mRefreshLayout;
	private List<PositionAdvertBO> guangGao = new ArrayList<PositionAdvertBO>();
	private View mRootView;
	MainActivity activity ;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_tab_one, container, false);
		activity = (MainActivity)getActivity();
		activity.setTitleBar(getString(R.string.today_rm));
		activity.hideTitle(false);

		findView();
		initRefreshLayout();
		BindData();
		return mRootView;
	}


	private void BindData(){

		PositionAdvertBO positionAdvertBO = new PositionAdvertBO();
		positionAdvertBO.setAdImgPath("http://h.hiphotos.baidu.com/image/h%3D200/sign=ae4df4ec878ba61ec0eecf2f713497cc/43a7d933c895d143b233160576f082025aaf074a.jpg");
		guangGao.add(positionAdvertBO);
		bannerView.setPositionAdvertBO(guangGao);
	}

	private void findView()
	{
		bannerView = (BannerView) mRootView.findViewById(R.id.banner);
		mRefreshLayout = (BGARefreshLayout) mRootView.findViewById(R.id.mRefreshLayout);
	}


	@Override
	public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
		Toast.makeText(getContext(),"onBGARefreshLayoutBeginRefreshing",Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
		 Toast.makeText(getContext(),"onBGARefreshLayoutBeginLoadingMore",Toast.LENGTH_LONG).show();
		return false;
	}

	public void beginRefreshing() {
		mRefreshLayout.beginRefreshing();
	}

	// 通过代码方式控制进入加载更多状态
	public void beginLoadingMore() {
		mRefreshLayout.beginLoadingMore();
	}


	private void initRefreshLayout() {


	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		return false;
	}


//	private class MyBGARefreshViewHolder extends BGARefreshViewHolder
//	{
//
//
//		/**
//		 * @param context
//		 * @param isLoadingMoreEnabled 上拉加载更多是否可用
//		 */
//		public MyBGARefreshViewHolder(Context context, boolean isLoadingMoreEnabled) {
//			super(context, isLoadingMoreEnabled);
//		}
//
//		@Override
//		public View getRefreshHeaderView() {
//			return null;
//		}
//
//		@Override
//		public void handleScale(float scale, int moveYDistance) {
//
//		}
//
//		@Override
//		public void changeToIdle() {
//
//		}
//
//		@Override
//		public void changeToPullDown() {
//
//		}
//
//		@Override
//		public void changeToReleaseRefresh() {
//
//		}
//
//		@Override
//		public void changeToRefreshing() {
//
//		}
//
//		@Override
//		public void onEndRefreshing() {
//
//		}
//
//	}
}
