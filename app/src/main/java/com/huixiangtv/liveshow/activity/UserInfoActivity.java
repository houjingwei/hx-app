package com.huixiangtv.liveshow.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.Friend;
import com.huixiangtv.liveshow.model.Other;
import com.huixiangtv.liveshow.model.User;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;
import com.huixiangtv.liveshow.utils.StringUtil;
import com.huixiangtv.liveshow.utils.image.ImageUtils;
import com.tencent.connect.UserInfo;
import com.umeng.socialize.UMShareAPI;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends BaseBackActivity {

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
    @ViewInject(R.id.tvAddGroup)
    TextView tvAddGroup;



    String uid;
    private User user;

    private String isFriend = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        x.view().inject(this);
        uid = getIntent().getStringExtra("uid");
        if (StringUtil.isNotNull(uid)) {
            initView();
            loadData();
        } else {
            CommonHelper.showTip(UserInfoActivity.this, "访问用户信息出错");
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
                user = data;
                tvNickName.setText(data.getNickName());
                ImageUtils.displayAvator(ivPhoto, data.getPhoto());
            }
        });
        //艺人卡设置状态
        CommonHelper.cardStatus(uid, new ApiCallback<Other>() {
            @Override
            public void onSuccess(Other data) {
                if (data.getStatus().equals("1")) {
                    tvArtist.setText("形象卡");
                    GradientDrawable gd = (GradientDrawable) tvArtist.getBackground();
                    gd.setColor(UserInfoActivity.this.getResources().getColor(R.color.card_blue));
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
                if (StringUtil.isNotNull(data.getResult())) {
                    isFriend = data.getResult();
                    if (data.getResult().equals("0")) {
                        tvAddFriend.setText("加为好友");
                    } else {
                        tvAddFriend.setText("发消息");
                        //显示删除按钮
                        showDelete();
                    }
                }
            }
        });

        if (isFriend.equals("0")) {
            tvAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toAddFriend();
                }
            });
        } else {
            tvAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toChat();
                }
            });
        }

        //查询有没有加入粉丝群
        CommonHelper.joinFansGroup(uid, new ApiCallback<Other>() {
            @Override
            public void onSuccess(Other data) {
                if (StringUtil.isNotNull(data.getResult())) {
                    isFriend = data.getResult();
                    if (data.getResult().equals("0")) {
                        tvAddGroup.setText("加入粉丝群");
                        tvAddGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                applyJoinGroup();
                            }
                        });
                    } else {
                        tvAddGroup.setText("已加入粉丝群");
                    }
                }
            }
        });

    }



    /**
     * 显示删除按钮
     */
    private void showDelete() {
        commonTitle.saveShow(View.VISIBLE);
        commonTitle.setSaveText("删除");
        commonTitle.getSave().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFriend();
            }
        });
    }

    /**
     * 删除好友
     */
    private void deleteFriend() {
        Map<String,String> params = new HashMap<>();
        params.put("fid",uid);
        RequestUtils.sendPostRequest(Api.DEL_FRIEND, params, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(UserInfoActivity.this,e.getMessage());

            }
        }, String.class);
    }

    /**
     * 跳转到聊天页面
     */
    private void toChat() {
       if(null!=user){
           Map<String,String> map = new HashMap<String,String>();
           map.put("targetId",user.getUid());
           map.put("userName",user.getNickName());
           ForwardUtils.target(UserInfoActivity.this, Constant.CHAT_MSG,map);
       }


    }

    /**
     * 添加朋友
     */
    private void toAddFriend() {
        ForwardUtils.target(UserInfoActivity.this, Constant.ADD_FRIEND,null);
    }


    private void applyJoinGroup() {
        ForwardUtils.target(UserInfoActivity.this, Constant.APPLY_ADD_GROUP,null);
    }


    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.userInfo));


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && requestCode == 1) {
                    Map<String,String> params = new HashMap<>();
                    params.put("fid",uid);
                    params.put("content",getIntent().getStringExtra("msg"));
                    RequestUtils.sendPostRequest(Api.ADD_FRIEND, params, new ResponseCallBack<String>() {
                        @Override
                        public void onSuccess(String data) {
                            CommonHelper.showTip(UserInfoActivity.this,"请求已发出");
                        }

                        @Override
                        public void onFailure(ServiceException e) {
                            super.onFailure(e);
                            CommonHelper.showTip(UserInfoActivity.this,e.getMessage());

                        }
                    }, String.class);

                }
                break;
            case 2:
                if (resultCode == RESULT_OK && requestCode == 2) {
                    Map<String,String> params = new HashMap<>();
                    params.put("aid",uid);
                    params.put("content",getIntent().getStringExtra("msg"));
                    RequestUtils.sendPostRequest(Api.APPLY_ADD_GROUP, params, new ResponseCallBack<String>() {
                        @Override
                        public void onSuccess(String data) {
                            CommonHelper.showTip(UserInfoActivity.this,"请求已发出");
                        }

                        @Override
                        public void onFailure(ServiceException e) {
                            super.onFailure(e);
                            CommonHelper.showTip(UserInfoActivity.this,e.getMessage());

                        }
                    }, String.class);
                }
                break;
            default:
                break;
        }
    }



    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
