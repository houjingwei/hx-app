package com.huixiangtv.live.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Other;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.pop.InputWindow;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
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
        //ArtistCardInfoStatus();
        User user = App.getLoginUser();
        if (user != null) {
            tvNickName.setText(user.getNickName());
            tvNickName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInputWindow();
                }
            });
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

    InputWindow pop;
    private void showInputWindow() {


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
        mRootView.findViewById(R.id.rlCoupon).setOnClickListener(this);

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

            case R.id.rlCoupon:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.COUPON, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }
                break;

            case R.id.rlCard:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.PIC_LIST, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }
                break;


            case R.id.rlAuth:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.REG_LIVE_MAIN, null);
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



    private void ArtistCardInfoStatus() {
        RequestUtils.sendPostRequest(Api.GET_USER_ARTISTCARD_STATUS, null, new ResponseCallBack<User>() {
            @Override
            public void onSuccess(User data) {
                if (data != null) {

                    if (data.getStatus().equals("1")) //status
                    {
                        mRootView.findViewById(R.id.rlCard).setVisibility(View.VISIBLE);
                        mRootView.findViewById(R.id.rlAuth).setVisibility(View.GONE);
                    }
                    else
                    {
                        mRootView.findViewById(R.id.rlCard).setVisibility(View.GONE);
                        mRootView.findViewById(R.id.rlAuth).setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, User.class);
    }
}
