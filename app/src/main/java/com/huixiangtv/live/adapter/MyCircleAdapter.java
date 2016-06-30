package com.huixiangtv.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Love;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjw on 16/5/13.
 */
public class MyCircleAdapter extends BaseAdapter {


    Activity activity;
    Context context;
    List<Love> voList = new ArrayList<Love>();


    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public MyCircleAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public MyCircleAdapter() {
    }


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
        View rowView = convertView;
        if (null == rowView) {
            final Love love = (Love) getItem(position);
            rowView = LayoutInflater.from(context).inflate(R.layout.loves_item, parent, false);
            TextView tvTitle = (TextView) rowView.findViewById(R.id.tvTitle);
            TextView tvTime = (TextView) rowView.findViewById(R.id.tvTime);
            TextView tvContent = (TextView) rowView.findViewById(R.id.tvContent);
            TextView tvLoveCount = (TextView) rowView.findViewById(R.id.tvLoveCount);
            tvTime.setText(love.getDateTime());
            tvTitle.setText(love.getSource());
            tvLoveCount.setText(love.getCount());
            tvContent.setText(love.getDesc());
        }
        return rowView;
    }

    public void clear() {
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<Love> ls) {
        if (ls != null) {
            voList.addAll(ls);
        }
        notifyDataSetChanged();
    }
}