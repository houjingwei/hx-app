package com.huixiangtv.liveshow.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.User;
import com.huixiangtv.liveshow.service.LoginCallBack;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.ColaProgress;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;


public class LoginWindow extends BasePopupWindow implements OnClickListener {

	private Activity context;
	private View view;
	ScrollView bottom;
	LoginCallBack callBack;


	private LinearLayout loginPhone;
	private LinearLayout loginQq;
	private LinearLayout loginWeixin;
	private LinearLayout loginSina;


	private Animation mShowAnimation;
	private Animation mHideAnimation;

	private void initAnimation() {
		mShowAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
		mHideAnimation = AnimationUtils.loadAnimation(context,R.anim.bottom_down);
	}

	public LoginWindow(Activity context, int width, int height) {
		this.context = context;
		super.setWidth(width);
		super.setHeight(height);
		initPopUpWindow();
		initAnimation();
		show();
	}

	private void show() {
		bottom.setAnimation(mShowAnimation);
		bottom.setVisibility(View.VISIBLE);
	}

	

	public void initPopUpWindow() {

		try {
			view = RelativeLayout.inflate(context, R.layout.pop_login, null);
			bottom = (ScrollView) view.findViewById(R.id.bottom);
			view.findViewById(R.id.pop_layout).setOnClickListener(this);

			loginPhone = (LinearLayout) view.findViewById(R.id.loginPhone);
			loginQq = (LinearLayout) view.findViewById(R.id.loginQq);
			loginSina = (LinearLayout) view.findViewById(R.id.loginSina);
			loginWeixin = (LinearLayout) view.findViewById(R.id.loginWeixin);


			super.setFocusable(true);
			super.setOutsideTouchable(true);
			super.setBackgroundDrawable(new BitmapDrawable());
			this.setContentView(view);
			this.setWidth(LayoutParams.FILL_PARENT);
			this.setHeight(LayoutParams.FILL_PARENT);
			this.setFocusable(true);
			this.setAnimationStyle(R.style.popupAnimation);

			loginPhone.setOnClickListener(this);
			loginQq.setOnClickListener(this);
			loginWeixin.setOnClickListener(this);
			loginSina.setOnClickListener(this);
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_layout:
			dismiss();
			break;
		case R.id.loginQq:
			login(SHARE_MEDIA.QQ);
			dismiss();
			break;
		case R.id.loginPhone:
			ForwardUtils.target(context, Constant.LOGIN,null);
			dismiss();
			break;
		case R.id.loginWeixin:
			login(SHARE_MEDIA.WEIXIN);
			dismiss();
			break;
		case R.id.loginSina:
			login(SHARE_MEDIA.SINA);
			dismiss();
			break;

		default:
			break;
		}
	}

	private void login(SHARE_MEDIA platform) {
		cp = ColaProgress.show(context, "正在授权", false, true, null);
		App.mShareAPI.doOauthVerify(context, platform, umAuthListener);
	}

	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			if(null!=cp){
				cp.dismiss();
			}
			if( null != data && data.size()>0) {
				String access_token = data.get("access_token");
				String openId = "";
				String flag = "";
				if(platform==SHARE_MEDIA.QQ){
					flag="2";
				}else if(platform==SHARE_MEDIA.WEIXIN){
					flag="3";
					openId = data.get("openid");
				}else if(platform==SHARE_MEDIA.SINA){
					flag="4";
				}
				AuthThirdlogin(access_token, flag,openId);
			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			if(null!=cp){
				cp.dismiss();
			}
			CommonHelper.showTip(context,"授权失败");
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			if(null!=cp){
				cp.dismiss();
			}
			CommonHelper.showTip(context,"取消登录");
		}
	};

	private ColaProgress cp = null;
	private void AuthThirdlogin(String code,String platform,String openid){
		cp = ColaProgress.show(context, "登录中...", false, true, null);
		Map<String,String> params = new HashMap<String, String>();
		params.put("code",code);
		params.put("platform",platform);
		params.put("openid",openid);
		RequestUtils.sendPostRequest(Api.AUTH_THIRDLOGIN, params, new ResponseCallBack<User>() {
			@Override
			public void onSuccess(User data) {
				super.onSuccess(data);
				saveLoginInfo(data);
				if (null != cp) {
					cp.dismiss();
				}
				if(null!=callBack){
					callBack.loginSuccess();
				}
			}

			@Override
			public void onFailure(ServiceException e) {
				super.onFailure(e);
				if(null!=cp){
					cp.dismiss();
				}
				CommonHelper.showTip(context,"登录失败");
			}
		}, User.class);
	}

	private void saveLoginInfo(User data) {
		App.saveLoginUser(data);
	}


	public void setListener(LoginCallBack callBack) {
		this.callBack = callBack;
	}
}
