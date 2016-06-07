package com.huixiangtv.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.Fans;
import com.huixiangtv.live.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjw on 16/5/13.
 */
public class MyFansAdapter extends BaseAdapter {


    Activity activity;
    Context context;
    List<Fans> voList = new ArrayList<Fans>();


    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public MyFansAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public MyFansAdapter() {
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
            final Fans fans = (Fans) getItem(position);
            rowView = LayoutInflater.from(context).inflate(R.layout.fans_item, parent, false);
            TextView tvRank = (TextView) rowView.findViewById(R.id.tvRank);
            TextView tvNickName = (TextView) rowView.findViewById(R.id.tvNickName);
            TextView tvHots = (TextView) rowView.findViewById(R.id.tvHots);
            ImageView ivPhoto = (ImageView) rowView.findViewById(R.id.ivPhoto);
            tvRank.setText(position+1+"");
            tvHots.setText(fans.getHots());
            tvNickName.setText(fans.getNickName());
            ImageUtils.displayAvator(ivPhoto,fans.getPhoto());

        }
        return rowView;
    }

    public void clear() {
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<Fans> ls) {
        if (ls != null) {
            voList.addAll(ls);
        }
        notifyDataSetChanged();
    }
}