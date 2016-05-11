package com.huixiang.live.ui;

/*
 * @Author yulongsheng
 * @Date 2014/9/11
 * @Email seafishyls@126.com
 * */

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiang.live.R;


public class ColaProgressTip extends Dialog {

	public ColaProgressTip(Context context, int theme) {
		super(context, theme);
	}
	

	
	//设置文字
	public void setMessage(CharSequence message) {
		if(message != null && message.length() > 0) {
			findViewById(R.id.message).setVisibility(View.VISIBLE);
			TextView txt = (TextView)findViewById(R.id.message);
			txt.setText(message);
			txt.invalidate();
		}
	}
	
	
	//展示
	public static ColaProgressTip show(Context context, CharSequence message, boolean cancelable, boolean cancelableOutsite, OnCancelListener onCancelListener, Drawable drawable){
		//设置主题
		ColaProgressTip colaProgress=new ColaProgressTip(context,R.style.ColaProgress);
		colaProgress.setTitle("");
		colaProgress.setContentView(R.layout.layout_colaprogress_tip);
		
		if(message.length()==0||message==null){
			colaProgress.findViewById(R.id.message).setVisibility(View.GONE);
		}else{
			TextView txtMessage=(TextView)colaProgress.findViewById(R.id.message);
			txtMessage.setText(message);
		}
		ImageView imageView = (ImageView) colaProgress.findViewById(R.id.imageView);
		if(null!=drawable){
			imageView.setImageDrawable(drawable);
		}else{
			imageView.setVisibility(ViewGroup.GONE);
		}

		colaProgress.setCancelable(cancelable);
		colaProgress.setCanceledOnTouchOutside(cancelableOutsite);
		colaProgress.setOnCancelListener(onCancelListener);
		WindowManager.LayoutParams lp=colaProgress.getWindow().getAttributes();
		lp.gravity= Gravity.CENTER;
		lp.dimAmount=0.2f;
		colaProgress.getWindow().setAttributes(lp);
		colaProgress.show();
		return colaProgress;
	}


	public static void showTip(final long time, final ColaProgressTip cp){
		new AsyncTask<Void, String, Void>() {
			@Override
			protected void onPreExecute() {

				super.onPreExecute();
			}
			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(String[] values) {
				super.onProgressUpdate(values);
			}
			@Override
			protected void onPostExecute(Void result) {
				//执行结束后关闭
				try{
					cp.dismiss();
				}catch(Exception e){

				}
				super.onPostExecute(result);
			}
		}.execute();
	}
}



