package com.huixiangtv.liveshow.activity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.Other;
import com.huixiangtv.liveshow.model.User;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.StringUtil;
import com.huixiangtv.liveshow.utils.image.ImageUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class UserDetialActivity extends BaseBackActivity {

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;


    @ViewInject(R.id.ivPhoto)
    ImageView ivPhoto;
    @ViewInject(R.id.tvNickName)
    TextView tvNickName;
    @ViewInject(R.id.tvArtist)
    TextView tvArtist;

    @ViewInject(R.id.tvAttention)
    TextView tvAttention;
    @ViewInject(R.id.tvFans)
    TextView tvFans;
    @ViewInject(R.id.tvHots)
    TextView tvHots;
    @ViewInject(R.id.tvAddFriend)
    TextView tvAddFriend;



    String uid;

    private String isFriend = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        x.view().inject(this);
        uid = getIntent().getStringExtra("uid");
        if(StringUtil.isNotNull(uid)){
            initView();
            loadData();
        }else{
            CommonHelper.showTip(UserDetialActivity.this, "访问用户信息出错");
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    onBackPressed();
                }
            }, 1000);
        }

    }

    private void loadData() {
        //用户信息
        CommonHelper.userInfo(uid, new ApiCallback<User>() {
            @Override
            public void onSuccess(User data) {
                tvNickName.setText(data.getNickName());
                ImageUtils.displayAvator(ivPhoto,data.getPhoto());
            }
        });
        //艺人卡设置状态
        CommonHelper.cardStatus(uid,new ApiCallback<Other>() {
            @Override
            public void onSuccess(Other data) {
                if (data.getStatus().equals("1")) {
                    tvArtist.setText("形象卡");
                    GradientDrawable gd = (GradientDrawable) tvArtist.getBackground();
                    gd.setColor(UserDetialActivity.this.getResources().getColor(R.color.card_blue));
                    tvArtist.setBackgroundDrawable(gd);
                }
            }
        });
        //人气，贡献榜，粉丝
        CommonHelper.myFansCount(uid, new ApiCallback<Other>() {
            @Override
            public void onSuccess(Other data) {
                tvAttention.setText(data.getCollects());
                tvHots.setText(data.getHots());
                tvFans.setText(data.getFans());
            }
        });
        //是否加为好友
        CommonHelper.isFriend(uid, new ApiCallback<Other>() {
            @Override
            public void onSuccess(Other data) {
                if(StringUtil.isNotNull(data.getResult())){
                    isFriend = data.getResult();
                    if(data.getResult().equals("0")){
                        tvAddFriend.setText("加为好友");
                    }else{
                        tvAddFriend.setText("发消息");
                    }
                }
            }
        });
    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.userInfo));


    }
}
