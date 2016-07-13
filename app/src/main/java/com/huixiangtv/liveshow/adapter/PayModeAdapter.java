package com.huixiangtv.liveshow.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.PayMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by houjingwei on 15/11/4.
 */
public class PayModeAdapter extends BaseAdapter{


    Activity activity;
    Context context;
    List<PayMode> voList = new ArrayList<PayMode>();

    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public PayModeAdapter(Activity activity){
        this.activity = activity;
        this.context = activity;
    }
    public PayModeAdapter(){}


    @Override
    public int getCount() {
        return voList.size();
    }

    @Override
    public Object getItem(int position) {
        return voList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = this.viewMap.get(position);
        if(null == rowView){
            final PayMode vo = (PayMode) getItem(position);
            rowView = LayoutInflater.from(activity).inflate(R.layout.pay_mode_list_item, null);
            TextView tvPay= (TextView) rowView.findViewById(R.id.tvPay);
            tvPay.setText(vo.getName());
            viewMap.put(position,rowView);
        }
        return rowView;
    }



    public void clear() {
        viewMap.clear();
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<PayMode> ls) {
        if (ls != null) {
            voList.addAll(ls);

        }
        notifyDataSetChanged();
    }
}
