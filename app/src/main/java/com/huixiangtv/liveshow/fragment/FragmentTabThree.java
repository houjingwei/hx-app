package com.huixiangtv.liveshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.activity.MainActivity;
import com.huixiangtv.liveshow.model.Other;
import com.huixiangtv.liveshow.model.User;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;
import com.huixiangtv.liveshow.utils.image.ImageUtils;

import java.util.HashMap;
import java.util.Map;


public class FragmentTabThree extends Fragment implements View.OnClickListener{


    private View mRootView;
    MainActivity activity;

    ImageView ivPhoto;
    TextView tvUserName;
    TextView tvHot;
    TextView tvFans;
    TextView tvArtist;
    TextView haveFans;
    TextView tvAccount;
    TextView tvLoves;
    RelativeLayout llUserTop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tab_three, container, false);
        activity = (MainActivity) getActivity();
        initView();
        initData();
        return mRootView;
    }



    protected void initData() {
        //getUserStatus();
        ArtistCardInfoStatus();
    }


    private void initView() {
        mRootView.findViewById(R.id.ivPhoto).setOnClickListener(this);
        mRootView.findViewById(R.id.llAccount).setOnClickListener(this);
        mRootView.findViewById(R.id.llLoves).setOnClickListener(this);
        mRootView.findViewById(R.id.tvUserName).setOnClickListener(this);
        mRootView.findViewById(R.id.setting).setOnClickListener(this);
        mRootView.findViewById(R.id.rlFeedBack).setOnClickListener(this);
        mRootView.findViewById(R.id.llTitle).setOnClickListener(this);
        mRootView.findViewById(R.id.llMyfans).setOnClickListener(this);
        mRootView.findViewById(R.id.llfaned).setOnClickListener(this);
        mRootView.findViewById(R.id.llHot).setOnClickListener(this);

        tvArtist = (TextView) mRootView.findViewById(R.id.artist);
        ivPhoto = (ImageView) mRootView.findViewById(R.id.ivPhoto);
        tvUserName = (TextView) mRootView.findViewById(R.id.tvUserName);
        tvHot = (TextView) mRootView.findViewById(R.id.tvHot);
        tvFans = (TextView) mRootView.findViewById(R.id.tvFans);
        haveFans = (TextView) mRootView.findViewById(R.id.haveFans);
        tvAccount = (TextView) mRootView.findViewById(R.id.tvAccount);
        tvLoves = (TextView) mRootView.findViewById(R.id.tvLoves);
        llUserTop = (RelativeLayout) mRootView.findViewById(R.id.llUserTop);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPhoto:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.USERINFO, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }
                break;
            case R.id.llAccount:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.ACCOUNT, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }

                break;
            case R.id.llLoves:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.MY_LOVES, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }

                break;
            case R.id.tvUserName:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.USERINFO, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }
                break;
            case R.id.setting:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.SETINT, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }
                break;
            case R.id.rlFeedBack:
                ForwardUtils.target(getActivity(), Constant.FEEDBACK, null);
                break;
            case R.id.llTitle:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.PIC_LIST, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }

                break;
            case R.id.llMyfans:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.MY_FANS, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }

                break;
            case R.id.llfaned:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.FANED_ME, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }

                break;
            case R.id.llHot:
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.MY_HOTS, null);
                } else {
                    ForwardUtils.target(getActivity(), Constant.LOGIN, null);
                }

                break;

        }
    }


    public void onDelayLoad() {
        User user = App.getLoginUser();
        if (user != null) {
            tvUserName.setText(user.getNickName());
            if (null != user.getPhoto() && user.getPhoto().length() > 0) {
                ImageUtils.displayAvator(ivPhoto, user.getPhoto());
                CommonHelper.viewSetBackageImag(user.getPhoto(), llUserTop);
            }


            CommonHelper.myFansCount(new ApiCallback<Other>() {
                @Override
                public void onSuccess(Other data) {
                    if (null != data) {
                        haveFans.setText(data.getCollects());
                        tvFans.setText(data.getFans());
                        tvHot.setText(data.getHots());
                    }

                }

                @Override
                public void onFailure(ServiceException e) {
                    super.onFailure(e);

                }
            });


            CommonHelper.myAccount(new ApiCallback<Other>() {
                @Override
                public void onSuccess(Other data) {
                    if (null != data) {
                        tvAccount.setText(data.getBalance());
                        tvLoves.setText(data.getLoves());

                        App.upUserLove(data.getLoves());
                        App.upUserBalance(data.getBalance());
                    }

                }

                @Override
                public void onFailure(ServiceException e) {
                    super.onFailure(e);

                }
            });
        } else {
            tvHot.setText("0");
            tvFans.setText("0");
            haveFans.setText("0");
            tvAccount.setText("0");
            tvLoves.setText("0");
            ivPhoto.setImageResource(R.drawable.default_head);
            tvUserName.setText("未登录");
            llUserTop.setBackgroundColor(getActivity().getResources().getColor(R.color.mainColor));

        }
    }




    @Override
    public void onResume() {
        super.onResume();
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                onDelayLoad();
            }
        }, 300);
    }


    private void ArtistCardInfoStatus() {
        RequestUtils.sendPostRequest(Api.GET_USER_ARTISTCARD_STATUS, null, new ResponseCallBack<User>() {
            @Override
            public void onSuccess(User data) {
                if (data != null) {

                    if (data.getStatus().equals("1")) //status
                    {
                        tvArtist.setText("我的艺人卡");
                    }
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, User.class);
    }


    /**
     * get user status by auth
     */
    public void getUserStatus() {
        try {
            String token = App.getPreferencesValue("token");
            String uid = App.getPreferencesValue("uid");
            Map<String, String> params = new HashMap<String, String>();
            params.put("uid", uid);
            params.put("token", token);
            RequestUtils.sendPostRequest(Api.USER_GETAUTHSTATUS, params, new ResponseCallBack<String>() {
                @Override
                public void onSuccess(String str) {
                    super.onSuccess(str);
                    Log.i("xxxxxxxxxx",str);
                    int flag = Integer.parseInt(str);
                    switch (flag) {
                        case 0:
                            Log.i("xxxxxxxxxx","成为艺人");
                            tvArtist.setText("成为艺人");
                            break;
                        case 1:
                            tvArtist.setText("我的艺人卡");
                            break;
                        case 2:
                            tvArtist.setText("艺人卡认证中");
                            break;
                        case -1:
                            tvArtist.setText("艺人卡认证不通过");
                            break;
                    }

                }

                @Override
                public void onFailure(ServiceException e) {
                    super.onFailure(e);
                }
            }, String.class);

        } catch (Exception ex) {

        }
    }


}