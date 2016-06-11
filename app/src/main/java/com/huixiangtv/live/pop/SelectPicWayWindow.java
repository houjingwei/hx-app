package com.huixiangtv.live.pop;

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

import com.huixiangtv.live.R;


public class SelectPicWayWindow extends BasePopupWindow implements
		OnClickListener {

	private Activity context;
	private View view;
	ScrollView bottom;
	LinearLayout llAlbum,llCapture;
	SelectPicListener listener;

	private Animation mShowAnimation;

	private void initAnimation() {
		mShowAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
	}

	public SelectPicWayWindow(Activity context, int width, int height) {
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
		view = RelativeLayout.inflate(context, R.layout.pop_select_pic, null);
		bottom = (ScrollView) view.findViewById(R.id.bottom);
		view.findViewById(R.id.pplayout).setOnClickListener(this);
		llAlbum=(LinearLayout) view.findViewById(R.id.ll_album);
		llCapture=(LinearLayout) view.findViewById(R.id.ll_capture);
		super.setFocusable(true);
		super.setOutsideTouchable(true);
		super.setBackgroundDrawable(new BitmapDrawable());
		this.setContentView(view);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.FILL_PARENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.popupAnimation);
		llAlbum.setOnClickListener(this);
		llCapture.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_layout:
			dismiss();
			break;
		case R.id.ll_album:
			if(listener!=null){
				listener.select(0);
			}
			dismiss();
			break;
		case R.id.ll_capture:
			if(listener!=null){
				listener.select(1);
			}
			dismiss();
			break;
		default:
			break;
		}
	}
	
	
	public static abstract class SelectPicListener {
		public void select(int select) {

		}
	}


	public SelectPicListener getListener() {
		return listener;
	}

	public void setListener(SelectPicListener listener) {
		this.listener = listener;
	}
	
}
