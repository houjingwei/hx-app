package com.huixiangtv.liveshow.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.MsgExt;
import com.huixiangtv.liveshow.utils.DateUtils;
import com.huixiangtv.liveshow.utils.StringUtil;
import com.huixiangtv.liveshow.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * Created by hjw on 16/5/13.
 */
public class ChatAdapter extends BaseAdapter {


    Activity activity;
    Context context;
    List<Conversation> voList = new ArrayList<Conversation>();


    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public ChatAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public ChatAdapter() {
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
        Conversation conver = (Conversation) getItem(position);
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_chatitem, parent, false);
            holder.tvUnRead = (TextView) convertView.findViewById(R.id.tvUnRead);
            holder.tvNickName = (TextView) convertView.findViewById(R.id.tvNickName);
            holder.tvMsg = (TextView) convertView.findViewById(R.id.tvMsg);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            holder.ivFlag =(ImageView) convertView.findViewById(R.id.ivFlag);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        if(conver.getLatestMessage() instanceof TextMessage){
            TextMessage tm = (TextMessage) conver.getLatestMessage();

            String content = "";
            if(StringUtil.isNotNull(tm.getContent())){
                if(tm.getContent().length()>15){
                    content = tm.getContent().substring(0,15)+"...";
                }else{
                    content = tm.getContent();
                }
            }

            holder.tvMsg.setText(content);
        }


        holder.tvTime.setText(DateUtils.msgTime("yyyy-MM-dd HH:mm:ss",conver.getReceivedTime()));
        MessageContent msgContent = conver.getLatestMessage();


        //设置未读消息
        if(conver.getUnreadMessageCount()>0) {
            holder.tvUnRead.setVisibility(View.VISIBLE);
            holder.tvUnRead.setText(conver.getUnreadMessageCount()+"");
        }else{
            holder.tvUnRead.setVisibility(View.GONE);
        }


        //设置消息用户信息
        if(conver.getConversationType().equals(Conversation.ConversationType.GROUP)){
            holder.ivPhoto.setImageDrawable(activity.getResources().getDrawable(R.mipmap.v3_group_caht));
            if(null!=msgContent){
                if(msgContent instanceof TextMessage){
                    TextMessage tm = (TextMessage) msgContent;
                    if(StringUtil.isNotNull(tm.getExtra())){
                        MsgExt msgExt = JSON.parseObject(String.valueOf(tm.getExtra()), MsgExt.class);
                        if(null!=msgExt){
                            holder.tvNickName.setText(msgExt.getNickName());
                        }
                    }
                }
            }
        }else{
            if(null!=msgContent){
                if(msgContent instanceof TextMessage){
                    TextMessage tm = (TextMessage) msgContent;
                    if(StringUtil.isNotNull(tm.getExtra())){
                        MsgExt msgExt = JSON.parseObject(String.valueOf(tm.getExtra()), MsgExt.class);
                        if(null!=msgExt){
                            holder.tvNickName.setText(msgExt.getToNickName());
                            ImageUtils.displayAvator(holder.ivPhoto, msgExt.getToPhoto());

                        }
                    }


                }
            }
        }


        //如果群聊显示群聊图标，否则隐藏
        if(conver.getConversationType() == Conversation.ConversationType.GROUP){
            holder.ivFlag.setVisibility(View.VISIBLE);
        }else if(conver.getConversationType() == Conversation.ConversationType.PRIVATE){
            holder.ivFlag.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void clear() {
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<Conversation> ls) {
        if (ls != null) {
            voList.addAll(ls);
        }
        notifyDataSetChanged();
    }



    static class ViewHolder
    {
        public TextView tvUnRead;
        public TextView tvNickName;
        public TextView tvMsg;
        public TextView tvTime;
        public ImageView ivPhoto;
        public ImageView ivFlag;
    }
}