package com.huixiangtv.live.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Other;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.ImageUtils;


public class FragmentMe extends Fragment implements View.OnClickListener {

    View mRootView;

    TextView tvNickName;
    TextView tvHots;
    TextView tvFans;
    ImageView ivPhoto;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_me, container, false);
        initView();
        initData();
        return mRootView;
    }

    private void initData() {
        User user = App.getLoginUser();
        if (user != null) {
            tvNickName.setText(user.getNickName());
            if (null != user.getPhoto() && user.getPhoto().length() > 0) {
                ImageUtils.displayAvator(ivPhoto, user.getPhoto());
            }


            CommonHelper.myFansCount(new ApiCallback<Other>() {
                @Override
                public void onSuccess(Other data) {
                    if (null != data) {
                        tvFans.setText(data.getFans());
                        tvHots.setText(data.getHots());
                    }
                }

                @Override
                public void onFailure(ServiceException e) {
                    super.onFailure(e);

                }
            });
        } else {
            tvHots.setText("0");
            tvFans.setText("0");
            ivPhoto.setImageResource(R.drawable.default_head);
            tvNickName.setText("未登录");

        }
    }

    private void initView() {
        tvNickName = (TextView) mRootView.findViewById(R.id.tvNickName);
        tvHots = (TextView) mRootView.findViewById(R.id.tvHots);
        tvFans = (TextView) mRootView.findViewById(R.id.tvFans);
        ivPhoto = (ImageView) mRootView.findViewById(R.id.ivPhoto);
        tvHots.setOnClickListener(this);
        tvFans.setOnClickListener(this);

        mRootView.findViewById(R.id.rlUserInfo).setOnClickListener(this);
        mRootView.findViewById(R.id.rlAccount).setOnClickListener(this);
        mRootView.findViewById(R.id.rlLove).setOnClickListener(this);
        mRootView.findViewById(R.id.rlCard).setOnClickListener(this);
        mRootView.findViewById(R.id.rlAuth).setOnClickListener(this);
        mRootView.findViewById(R.id.rlSetting).setOnClickListener(this);
        mRootView.findViewById(R.id.rlFeedBack).setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlUserInfo:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.USERINFO, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }
                break;
            case R.id.rlAccount:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.ACCOUNT, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }

                break;
            case R.id.rlLove:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.MY_LOVES, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }

                break;
            case R.id.rlSetting:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.SETINT, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }
                break;
            case R.id.rlFeedBack:
                ForwardUtils.target(getActivity(), Constant.FEEDBACK, null);
                break;
            case R.id.tvFans:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.MY_FANS, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }

                break;
            case R.id.tvHots:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.MY_HOTS, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }

                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
