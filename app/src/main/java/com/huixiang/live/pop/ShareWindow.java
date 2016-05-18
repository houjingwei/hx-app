package com.huixiang.live.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.huixiang.live.R;
import com.umeng.socialize.bean.SHARE_MEDIA;


public class ShareWindow extends BasePopupWindow implements OnClickListener {

	private Activity context;
	private View view;
	ScrollView bottom;
	SelectShareListener listener;
	private ImageView shareGo;

	private LinearLayout shareQQ;
	private LinearLayout shareWeixin;
	private LinearLayout shareWxfriend;
	private LinearLayout shareSina;
	private LinearLayout shareCopy;

	private Animation mShowAnimation;
	private Animation mHideAnimation;

	private void initAnimation() {
		mShowAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
		mHideAnimation = AnimationUtils.loadAnimation(context,R.anim.bottom_down);
	}

	public ShareWindow(Activity context, int width, int height) {
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
			view = RelativeLayout.inflate(context, R.layout.pop_share, null);
			bottom = (ScrollView) view.findViewById(R.id.bottom);
			view.findViewById(R.id.pop_layout).setOnClickListener(this);

			shareQQ = (LinearLayout) view.findViewById(R.id.shareQQ);
			shareCopy = (LinearLayout) view.findViewById(R.id.shareCopy);
			shareWeixin = (LinearLayout) view.findViewById(R.id.shareWeixin);
			shareWxfriend = (LinearLayout) view.findViewById(R.id.shareWxfriend);
			shareSina = (LinearLayout) view.findViewById(R.id.shareSina);

			shareGo = (ImageView) view.findViewById(R.id.iv_share_go);

			super.setFocusable(true);
			super.setOutsideTouchable(true);
			super.setBackgroundDrawable(new BitmapDrawable());
			this.setContentView(view);
			this.setWidth(LayoutParams.FILL_PARENT);
			this.setHeight(LayoutParams.FILL_PARENT);
			this.setFocusable(true);
			this.setAnimationStyle(R.style.popupAnimation);

			shareGo.setOnClickListener(this);
			shareQQ.setOnClickListener(this);
			shareWeixin.setOnClickListener(this);
			shareWxfriend.setOnClickListener(this);
			shareSina.setOnClickListener(this);
			shareCopy.setOnClickListener(this);
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
		case R.id.shareQQ:
			if(listener!=null){
				listener.select(SHARE_MEDIA.QQ);
			}
			dismiss();
			break;
		case R.id.shareCopy:
			if(listener!=null){
				listener.selectCopy();
			}
			dismiss();
			break;
		case R.id.shareWeixin:
			if(listener!=null){
				listener.select(SHARE_MEDIA.WEIXIN);
			}
			dismiss();
			break;
		case R.id.shareWxfriend:
			if(listener!=null){
				listener.select(SHARE_MEDIA.WEIXIN_CIRCLE);
			}
			dismiss();
			break;
		case R.id.shareSina:
			if(listener!=null){
				listener.select(SHARE_MEDIA.SINA);
			}
			dismiss();
			break;
		case R.id.iv_share_go:
			dismiss();
			break;
		default:
			break;
		}
	}

	public static class SelectShareListener {
		public void select(SHARE_MEDIA platForm) {

		}

		public void selectCopy(){

		}
	}




	public SelectShareListener getListener() {
		return listener;
	}

	public void setListener(SelectShareListener listener) {
		this.listener = listener;
	}
	
}
