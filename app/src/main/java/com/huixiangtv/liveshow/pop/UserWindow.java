package com.huixiangtv.liveshow.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.Live;
import com.huixiangtv.liveshow.model.Other;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.service.LoginCallBack;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;
import com.huixiangtv.liveshow.utils.StringUtil;
import com.huixiangtv.liveshow.utils.image.ImageUtils;

import java.util.HashMap;
import java.util.Map;


public class UserWindow extends BasePopupWindow implements OnClickListener {

    private Activity context;
    private View view;
    private Live live;
    ScrollView bottom;

    private ImageView ivPhoto;
    private TextView tvName;
    private TextView tvAddress;
    private TextView tvHeight;
    private TextView tvWeight;
    private TextView tvSanwei;

    private LinearLayout llReport;
    private LinearLayout llFans;
    private LinearLayout llDynamic;
    private LinearLayout llCard;
    private LinearLayout llTags;



    private Animation mShowAnimation;
    private Animation mHideAnimation;

    private void initAnimation() {
        mShowAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
        mHideAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_down);
    }

    public UserWindow(Activity context, int width, int height,Live live) {
        this.context = context;
        super.setWidth(width);
        super.setHeight(height);
        this.live = live;
        initPopUpWindow(live);
        initAnimation();
        show();
    }

    private void show() {
        bottom.setAnimation(mShowAnimation);
        bottom.setVisibility(View.VISIBLE);
    }


    public void initPopUpWindow(Live live) {

        try {
            view = RelativeLayout.inflate(context, R.layout.pop_user, null);
            bottom = (ScrollView) view.findViewById(R.id.bottom);
            view.findViewById(R.id.pop_layout).setOnClickListener(this);


            ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvAddress = (TextView) view.findViewById(R.id.tvAddress);
            tvHeight = (TextView) view.findViewById(R.id.tvHeight);
            tvWeight = (TextView) view.findViewById(R.id.tvWeight);
            tvSanwei = (TextView) view.findViewById(R.id.tvSanwei);

            llReport = (LinearLayout) view.findViewById(R.id.llReport);
            llFans = (LinearLayout) view.findViewById(R.id.llFans);
            llDynamic = (LinearLayout) view.findViewById(R.id.llDynamic);
            llCard = (LinearLayout) view.findViewById(R.id.llCard);

            llTags = (LinearLayout) view.findViewById(R.id.llTags);

            if(null!=live){
                ImageUtils.displayAvator(ivPhoto,live.getPhoto());
                tvName.setText(live.getNickName());
                tvAddress.setText(live.getCity());

                tvHeight.setText(StringUtil.isNotNull(live.getHeight())?live.getHeight()+"cm":"163cm");
                tvWeight.setText(StringUtil.isNotNull(live.getWeight())?live.getWeight()+"kg":"45kg");
                tvSanwei.setText(StringUtil.isNotNull(live.getBwh())?live.getBwh():"89-63-94");
                if(StringUtil.isNotNull(live.getTags())){
                    String[] tags = live.getTags().split(",");
                    for (String tag : tags) {
                        View tagView = LayoutInflater.from(context).inflate(R.layout.pop_user_tag_view, null, false);
                        TextView tvTag = (TextView) tagView.findViewById(R.id.tvTag);
                        tvTag.setText(tag);
                        llTags.addView(tagView);

                    }

                }
            }

            llReport.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    jubao();
                }
            });
            llFans.setOnClickListener(this);
            llDynamic.setOnClickListener(this);
            llCard.setOnClickListener(this);
            
            
            super.setFocusable(true);
            super.setOutsideTouchable(true);
            super.setBackgroundDrawable(new BitmapDrawable());
            this.setContentView(view);
            this.setWidth(LayoutParams.FILL_PARENT);
            this.setHeight(LayoutParams.FILL_PARENT);
            this.setFocusable(true);
            this.setAnimationStyle(R.style.popupAnimation);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 举报
     */
    private void jubao() {
        CommonHelper.jubao("0", "", live.getLid(), new ApiCallback<Other>() {
            @Override
            public void onSuccess(Other data) {
                CommonHelper.showTip(context,"举报成功");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_layout:
                dismiss();
                break;
            case R.id.llFans:
                dismiss();
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(context, Constant.MY_FANS, null);
                } else {
                    CommonHelper.showLoginPopWindow(context, R.id.liveMain, new LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            ForwardUtils.target(context, Constant.MY_FANS, null);
                        }
                    });
                }
                break;
            case R.id.llDynamic:
                dismiss();
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(context, Constant.OWN_CIRCLE, null);
                }else{
                    CommonHelper.showLoginPopWindow(context, R.id.liveMain, new LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            ForwardUtils.target(context, Constant.OWN_CIRCLE, null);
                        }
                    });
                }
                break;
            case R.id.llCard:
                dismiss();
                Map<String,String> params = new HashMap<String,String>();
                params.put("uid",live.getUid());
                if (null != App.getLoginUser() && !live.getUid().equals(App.getLoginUser().getUid())) {
                    ForwardUtils.target(context, Constant.PIC_LIST, params);
                }else if(null==App.getLoginUser()){
                    ForwardUtils.target(context, Constant.PIC_LIST, params);
                }
                break;
            default:
                break;
        }
    }


}
