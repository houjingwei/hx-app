package com.huixiangtv.live.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.model.LiveMsg;
import com.huixiangtv.live.utils.image.ImageUtils;


public class LivingFinishWindow extends BasePopupWindow implements
		OnClickListener {

	private Activity context;
	private View view;
	ScrollView bottom;
	CloseListener listener;

	private LiveMsg msg;
	private Live live;

	private Animation mShowAnimation;

	private void initAnimation() {
		mShowAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
	}

	public LivingFinishWindow(Activity context, int width, int height,LiveMsg msg,Live live) {
		this.context = context;
		this.msg = msg;
		this.live =  live;
		super.setWidth(width);
		super.setHeight(height);
		initPopUpWindow(msg,live);
		initAnimation();
		show();
	}

	private void show() {
		bottom.setAnimation(mShowAnimation);
		bottom.setVisibility(View.VISIBLE);
	}

	

	public void initPopUpWindow(LiveMsg msg, Live live) {
		view = RelativeLayout.inflate(context, R.layout.living_finish_view, null);
		bottom = (ScrollView) view.findViewById(R.id.bottom);
		view.findViewById(R.id.pop_layout).setOnClickListener(this);
		super.setFocusable(true);
		super.setOutsideTouchable(true);
		super.setBackgroundDrawable(new BitmapDrawable());
		this.setContentView(view);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.FILL_PARENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.popupAnimation);

		//设置直播信息
		TextView tvNickname = (TextView) view.findViewById(R.id.tvNickname);
		TextView tvOnlineNum = (TextView) view.findViewById(R.id.tvOnlineNum);
		TextView tvHot = (TextView) view.findViewById(R.id.tvHots);
		TextView tvLove = (TextView) view.findViewById(R.id.tvLoves);
		TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
		ImageView ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);

		tvNickname.setText(this.msg.getNickName());
		tvOnlineNum.setText(this.msg.getOnline());
		tvHot.setText(this.msg.getAddhot());
		tvLove.setText(this.msg.getLove());
		tvTime.setText(this.msg.getLiveTime());
		ImageUtils.displayAvator(ivPhoto, this.live.getPhoto());

		view.findViewById(R.id.tvGohome).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(listener!=null){
					dismiss();
					listener.select();
				}

			}
		});

		view.findViewById(R.id.btnClosed).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(listener!=null){
					dismiss();
					listener.select();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_layout:
			if(listener!=null){
				dismiss();
				listener.select();
			}
			break;

		default:
			break;
		}
	}
	

	public static abstract class CloseListener {
		public void select() {

		}
	}


	public CloseListener getListener() {
		return listener;
	}

	public void setListener(CloseListener listener) {
		this.listener = listener;
	}
	
}
