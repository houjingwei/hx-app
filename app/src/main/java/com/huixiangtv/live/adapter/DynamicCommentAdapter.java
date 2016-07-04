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
import com.huixiangtv.live.model.DynamicComment;
import com.huixiangtv.live.model.Fans;
import com.huixiangtv.live.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjw on 16/5/13.
 */
public class DynamicCommentAdapter extends BaseAdapter {


    Activity activity;
    Context context;
    List<DynamicComment> voList = new ArrayList<DynamicComment>();


    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public DynamicCommentAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public DynamicCommentAdapter() {
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

        ViewHolder holder;
        Fans fans = (Fans) getItem(position);
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.fans_item, parent, false);
            holder.tvRank = (TextView) convertView.findViewById(R.id.tvRank);
            holder.tvNickName = (TextView) convertView.findViewById(R.id.tvNickName);
            holder.tvHots = (TextView) convertView.findViewById(R.id.tvHots);
            holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tvRank.setText(position+1+"");
        holder.tvHots.setText(fans.getHots());
        holder.tvNickName.setText(fans.getNickName());
        ImageUtils.displayAvator(holder.ivPhoto,fans.getPhoto());

        return convertView;
    }

    public void clear() {
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<DynamicComment> ls) {
        if (ls != null) {
            voList.addAll(ls);
        }
        notifyDataSetChanged();
    }



    static class ViewHolder
    {
        public TextView msg;
        public TextView tvRank;
        public TextView tvNickName;
        public TextView tvHots;
        public ImageView ivPhoto;
    }
}