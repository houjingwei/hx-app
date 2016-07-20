package com.huixiangtv.liveshow.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.Friend;
import com.huixiangtv.liveshow.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjw on 16/5/13.
 */
public class FriendAdapter extends BaseAdapter {


    Activity activity;
    Context context;
    List<Friend> voList = new ArrayList<Friend>();


    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public FriendAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public FriendAdapter() {
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
        Friend friend = (Friend) getItem(position);
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_friend_item, parent, false);

            holder.tvNickName = (TextView) convertView.findViewById(R.id.tvNickName);
            holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            holder.ivFlag =(ImageView) convertView.findViewById(R.id.ivFlag);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }


        //设置消息用户信息
        holder.tvNickName.setText(friend.getNickName());
        ImageUtils.displayAvator(holder.ivPhoto,friend.getPhoto());


//        //如果群聊显示群聊图标，否则隐藏
//        if(conver.getConversationType() == Conversation.ConversationType.GROUP){
//            holder.ivFlag.setVisibility(View.VISIBLE);
//        }else if(conver.getConversationType() == Conversation.ConversationType.PRIVATE){
//            holder.ivFlag.setVisibility(View.GONE);
//        }

        return convertView;
    }

    public void clear() {
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<Friend> ls) {
        if (ls != null) {
            voList.addAll(ls);
        }
        notifyDataSetChanged();
    }



    static class ViewHolder
    {

        public TextView tvNickName;
        public ImageView ivPhoto;
        public ImageView ivFlag;
    }
}