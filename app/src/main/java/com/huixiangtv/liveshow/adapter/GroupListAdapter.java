package com.huixiangtv.liveshow.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.ChatGroup;
import com.huixiangtv.liveshow.model.Live;
import com.huixiangtv.liveshow.utils.DateUtils;
import com.huixiangtv.liveshow.utils.ForwardUtils;
import com.huixiangtv.liveshow.utils.image.ImageUtils;
import com.huixiangtv.liveshow.utils.widget.LinearLayoutForListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;

/**
 * Created by hjw on 16/5/13.
 */
public class GroupListAdapter extends BaseAdapter {


    Activity activity;
    Context context;
    List<ChatGroup> voList = new ArrayList<ChatGroup>();


    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public GroupListAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public GroupListAdapter() {
    }


    @Override
    public int getCount() {
        return voList.size();
    }

    @Override
    public ChatGroup getItem(int position) {
        return voList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        final ChatGroup group = (ChatGroup) getItem(position);
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.group_list_item, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            holder.ivFlag =(ImageView) convertView.findViewById(R.id.ivFlag);
            holder.llChat = (LinearLayout) convertView.findViewById(R.id.llChat);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tvName.setText(group.getgName());
        ImageUtils.displayAvator(holder.ivPhoto,group.getImage());

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toGroupDetial(group.getGid());
            }
        });
        holder.llChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Map<String,String> map = new HashMap<String,String>();
                map.put("targetId",group.getGid());
                map.put("type","2");
                map.put("userName",group.getgName());
                ForwardUtils.target(activity, Constant.CHAT_MSG, map);
            }
        });




        return convertView;
    }

    private void toGroupDetial(String gid) {
        Map<String,String> map = new HashMap<>();
        map.put("groupId",gid);
        ForwardUtils.target(activity, Constant.GROUP_CHAT_INFO, map);
    }

    public void clear() {
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<ChatGroup> ls) {
        if (ls != null) {
            voList.addAll(ls);
        }
        notifyDataSetChanged();
    }



    static class ViewHolder
    {
        public TextView tvName;
        public ImageView ivPhoto;
        public ImageView ivFlag;
        public LinearLayout llChat;
    }
}