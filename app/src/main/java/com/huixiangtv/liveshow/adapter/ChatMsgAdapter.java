package com.huixiangtv.liveshow.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.ChatMessage;
import com.huixiangtv.liveshow.model.MsgExt;
import com.huixiangtv.liveshow.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjw on 16/5/13.
 */
public class ChatMsgAdapter extends BaseAdapter {

    private int TYPE_COUNT = 2;
    private int LEFT = 0;
    private int RIGHT = 1;

    Activity activity;
    Context context;
    List<ChatMessage> voList = new ArrayList<ChatMessage>();



    public ChatMsgAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public ChatMsgAdapter() {
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
    public int getItemViewType(int position) {
        ChatMessage msg = (ChatMessage) getItem(position);
        if(null!=msg && msg.getExt().getMsgType().equals("0")){
                return LEFT;
        }
        return RIGHT;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        ChatMessage msg = (ChatMessage) getItem(position);

        if(getItemViewType(position)==LEFT){
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_chat_msg_item_left, parent, false);

                holder.tvMsg = (TextView) convertView.findViewById(R.id.tvMsg);
                holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
                holder.tvTime =(TextView) convertView.findViewById(R.id.tvTime);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
        }else{
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_chat_msg_item_right, parent, false);
                holder.tvMsg = (TextView) convertView.findViewById(R.id.tvMsg);
                holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
                holder.tvTime =(TextView) convertView.findViewById(R.id.tvTime);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
        }
        holder.tvMsg.setText(msg.getContent());
        MsgExt ext = msg.getExt();
        if(null!=ext){
            ImageUtils.displayAvator(holder.ivPhoto,ext.getPhoto());
        }


        return convertView;
    }

    public void clear() {
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<ChatMessage> ls) {
        if (ls != null) {
            voList.addAll(ls);
        }
        notifyDataSetChanged();
    }



    static class ViewHolder
    {

        public TextView tvTime;
        public ImageView ivPhoto;
        public TextView tvMsg;
        public LinearLayout llTime;
    }
}