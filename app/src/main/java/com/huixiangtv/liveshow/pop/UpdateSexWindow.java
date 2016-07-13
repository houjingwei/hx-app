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
import com.huixiangtv.liveshow.R;


public class UpdateSexWindow extends BasePopupWindow implements
		OnClickListener {

	private Activity context;
	private View view;
	ScrollView bottom;
	LinearLayout nan,nv;
	SelectSexListener listener;

	private Animation mShowAnimation;
	private Animation mHideAnimation;

	private void initAnimation() {
		mShowAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
		mHideAnimation = AnimationUtils.loadAnimation(context,R.anim.bottom_down);
	}

	public UpdateSexWindow(Activity context, int width, int height) {
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
		view = RelativeLayout.inflate(context, R.layout.pop_sex, null);
		bottom = (ScrollView) view.findViewById(R.id.bottom);
		view.findViewById(R.id.pop_layout).setOnClickListener(this);
		nan=(LinearLayout) view.findViewById(R.id.nan);
		nv=(LinearLayout) view.findViewById(R.id.nv);
		super.setFocusable(true);
		super.setOutsideTouchable(true);
		super.setBackgroundDrawable(new BitmapDrawable());
		this.setContentView(view);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.FILL_PARENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.popupAnimation);
		nan.setOnClickListener(this);
		nv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_layout:
			dismiss();
			break;
		case R.id.nv:
			if(listener!=null){
				listener.select(0);
			}
			dismiss();
			break;
		case R.id.nan:
			if(listener!=null){
				listener.select(1);
			}
			dismiss();
			break;
		default:
			break;
		}
	}
	
	
	public static abstract class SelectSexListener {
		public void select(int sex) {

		}
	}


	public SelectSexListener getListener() {
		return listener;
	}

	public void setListener(SelectSexListener listener) {
		this.listener = listener;
	}
	
}
