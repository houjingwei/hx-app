package com.huixiangtv.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.model.DynamicComment;
import com.huixiangtv.live.utils.DateUtils;
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
        Log.i("position_me",position+"");
        ViewHolder holder;
        DynamicComment dc = (DynamicComment) getItem(position);
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.dynamic_comment_item, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tvName.setText(dc.getNickName());
        holder.tvTime.setText(DateUtils.formatDisplayTime(dc.getDate(),"yyyy-MM-dd HH:mm:ss"));
        holder.tvContent.setText(dc.getContent());
        ImageUtils.displayAvator(holder.ivPhoto,dc.getPhoto());

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
        public TextView tvContent;
        public TextView tvTime;
        public TextView tvName;
        public ImageView ivPhoto;
        public ImageView viIcon;
    }
}