package com.huixiangtv.live.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.MainActivity;
import com.huixiangtv.live.model.TabEntity;

import java.util.ArrayList;


public class FragmentTabFour extends Fragment {
	private View mRootView;
	MainActivity activity;
	private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
	private ArrayList<Fragment> mMyFragments = new ArrayList<>();
	private View mDecorView;
	private ViewPager mViewPager;
	private CommonTabLayout mTabLayout;
	private String[] mTitles = new String[]{"圈子","账户"};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_tab_four, container, false);
		activity = (MainActivity) getActivity();
		initView();
		initData();
		return mRootView;
	}

	private void initData() {
	}

	private void initView() {
		Window window = activity.getWindow();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			window.setStatusBarColor(getResources().getColor(R.color.mainColor));
		}


		FragmentCircle circle = new FragmentCircle();
		FragmentMe me = new FragmentMe();
		mMyFragments.add(circle);
		mMyFragments.add(me);
		mTabEntities.add(new TabEntity(mTitles[0], 0, 0));
		mTabEntities.add(new TabEntity(mTitles[1], 0, 0));

		mDecorView = activity.getWindow().getDecorView();
		mViewPager = (ViewPager) mRootView.findViewById(R.id.vp);
		mViewPager.setOffscreenPageLimit(1);

		mViewPager.setAdapter(new MyPagerAdapter(activity.getSupportFragmentManager()));
		mTabLayout = (CommonTabLayout) mRootView.findViewById(R.id.tl);
		tl();
	}


	private void tl() {

		mTabLayout.setTabData(mTabEntities);
		mTabLayout.setCurrentTab(1);
		mViewPager.setCurrentItem(1);
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				FragmentCircle.clearViode();
				mTabLayout.setCurrentTab(position);

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});


		mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
			@Override
			public void onTabSelect(int position) {

				mViewPager.setCurrentItem(position);
			}

			@Override
			public void onTabReselect(int position) {
			}
		});


	}


	private class MyPagerAdapter extends FragmentPagerAdapter {
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public int getCount() {
			return mMyFragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTitles[position];
		}

		@Override
		public Fragment getItem(int position) {
			return mMyFragments.get(position);
		}
	}

}
