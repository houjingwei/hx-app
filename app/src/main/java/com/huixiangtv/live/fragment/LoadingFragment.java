package com.huixiangtv.live.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huixiangtv.live.R;

/**
 * Created by Stone on 16/5/15. 先放着可能到时候扩展用到
 */
public abstract class LoadingFragment extends Fragment {
    private View mVLoadingContainer;
    private TextView mTvLoading;
    private boolean mIsInited = false;

    private boolean mIsLazyInitData = false;

    public LoadingFragment setLazyInitData(boolean isLazy) {
        mIsLazyInitData = true;
        return this;
    }

    @Override
    final public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_root_layout, null);
        mVLoadingContainer = rootView.findViewById(R.id.loading_container);
        mTvLoading = (TextView) rootView.findViewById(R.id.tv_loading);

        hideLoading();
        FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.fl_content);

        View view = getLayout(inflater, container, savedInstanceState);

        if(view != null) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            frameLayout.addView(view, lp);

            initLayout(view);

            if(!mIsLazyInitData) {
                callInitData();
            }
        }

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser && mIsLazyInitData) {
            callInitData();
        }
    }

    private void callInitData() {
        if(!mIsInited) {
            mIsInited = true;
            initData();
        }
    }

    protected void requestData() {
        initData();
    }

    protected void showLoading(String text) {
        if(mTvLoading != null) {
            mTvLoading.setText(text);
            mTvLoading.setVisibility(View.VISIBLE);
            mVLoadingContainer.setVisibility(View.VISIBLE);
        }
    }

    protected void hideLoading() {
        if(mTvLoading != null) {
            mTvLoading.setVisibility(View.GONE);
            mVLoadingContainer.setVisibility(View.GONE);
        }
    }


    protected abstract View getLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    protected abstract void initLayout(View view);
    protected abstract void initData();
}