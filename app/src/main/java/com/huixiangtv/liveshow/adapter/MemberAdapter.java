package com.huixiangtv.liveshow.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.Live;
import com.huixiangtv.liveshow.model.Member;
import com.huixiangtv.liveshow.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjw on 16/5/13.
 */
public class MemberAdapter extends BaseAdapter {


    Activity activity;
    Context context;
    List<Member> voList = new ArrayList<Member>();


    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public MemberAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public MemberAdapter() {
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
        Member member = (Member) getItem(position);
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.group_member_item, parent, false);
            holder.rlRoot = (RelativeLayout) convertView.findViewById(R.id.rlRoot);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvGX = (TextView) convertView.findViewById(R.id.tvGX);
            holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }


        ImageUtils.display(holder.ivPhoto,member.ivPhone);

        holder.tvGX.setText(member.gx);
        holder.tvTitle.setText(member.title);


        return convertView;
    }

    public void clear() {
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<Member> ls) {
        if (ls != null) {
            voList.addAll(ls);
        }
        notifyDataSetChanged();
    }



    static class ViewHolder
    {

        public RelativeLayout rlRoot;
        public TextView tvGX;
        public ImageView ivPhoto;
        public TextView tvTitle;






    }
}