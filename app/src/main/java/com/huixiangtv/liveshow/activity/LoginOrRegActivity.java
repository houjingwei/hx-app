package com.huixiangtv.liveshow.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.fragment.FragmentLogin;
import com.huixiangtv.liveshow.fragment.FragmentReg;
import com.huixiangtv.liveshow.model.TabEntity;
import com.huixiangtv.liveshow.utils.ViewFindUtils;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;

public class LoginOrRegActivity extends BaseBackActivity implements View.OnClickListener{

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mMyFragments = new ArrayList<>();
    private View mDecorView;
    private ViewPager mViewPager;
    private CommonTabLayout mTabLayout;
    private String[] mTitles = new String[]{"登录","注册"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_reg);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.mainColor));
        }

        findViewById(R.id.back).setOnClickListener(this);

        FragmentLogin login = new FragmentLogin();
        FragmentReg reg = new FragmentReg();
        mMyFragments.add(login);
        mMyFragments.add(reg);
        mTabEntities.add(new TabEntity(mTitles[0], 0, 0));
        mTabEntities.add(new TabEntity(mTitles[1], 0, 0));

        mDecorView = getWindow().getDecorView();
        mViewPager = ViewFindUtils.find(mDecorView, R.id.vp);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabLayout = ViewFindUtils.find(mDecorView, R.id.tl);
        tl();
    }

    private void tl() {
        mTabLayout.setTabData(mTabEntities);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);


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
