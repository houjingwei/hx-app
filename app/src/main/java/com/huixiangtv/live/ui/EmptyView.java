package com.huixiangtv.live.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.utils.widget.WidgetUtil;

/**
 * Created by hjw on 16/6/14.
 */
public class EmptyView extends LinearLayout {

    Context ct;
    TextView tvMsg;

    public EmptyView(Context context) {
        super(context);
        ct = context;
        LayoutInflater.from(context).inflate(R.layout.empty_data, this);
        RelativeLayout rlRoot = (RelativeLayout) findViewById(R.id.rlRoot);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)rlRoot.getLayoutParams();
        params.height = App.screenHeight - WidgetUtil.dip2px(ct,50)-App.statuBarHeight;
        params.width =  ViewGroup.LayoutParams.MATCH_PARENT;
        rlRoot.setLayoutParams(params);
        tvMsg = (TextView)findViewById(R.id.tvMsg);

    }



    public void setMsg(String msg) {
        tvMsg.setText(""+msg);
    }


}