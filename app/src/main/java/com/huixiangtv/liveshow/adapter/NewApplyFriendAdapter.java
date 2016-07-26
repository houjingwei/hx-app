package com.huixiangtv.liveshow.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.activity.NewApplyFriendActivity;
import com.huixiangtv.liveshow.model.Friend;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjw on 16/5/13.
 */
public class NewApplyFriendAdapter extends BaseAdapter {

    private int TYPE_COUNT = 2;
    //有没有处理过
    private int NO = 0;
    private int YES = 1;


    Activity activity;
    Context context;
    List<Friend> voList = new ArrayList<Friend>();


    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public NewApplyFriendAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public NewApplyFriendAdapter() {
    }

    @Override
    public int getItemViewType(int position) {
        Friend friend = (Friend) getItem(position);
        if(friend.getStatus().equals("0")){
            return YES;
        }
        return NO;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
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

        ViewHolder holder = null;
        final Friend friend = (Friend) getItem(position);
        if(getItemViewType(position)==NO){
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_new_friend_item01, parent, false);
                holder.tvNickName = (TextView) convertView.findViewById(R.id.tvNickName);
                holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
                holder.tvStatus =(TextView) convertView.findViewById(R.id.tvStatus);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            if(friend.getStatus().equals("1")){
                holder.tvStatus.setText("拒绝");
            }else if(friend.getStatus().equals("2")){
                holder.tvStatus.setText("同意");
            }
        }else if(getItemViewType(position)==YES){
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_new_friend_item02, parent, false);
                holder.tvNickName = (TextView) convertView.findViewById(R.id.tvNickName);
                holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
                holder.tvValues =(TextView) convertView.findViewById(R.id.tvValues);
                holder.tvMsg =(TextView) convertView.findViewById(R.id.tvMsg);
                holder.rlAdd =(RelativeLayout) convertView.findViewById(R.id.rlAdd);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            holder.rlAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    replyAddGroup(friend);
                }
            });
            holder.tvValues.setText("贡献值:"+1987);
            holder.tvMsg.setText(friend.getReplyContent());
        }



        //设置消息用户信息
        holder.tvNickName.setText(friend.getNickName());
        ImageUtils.displayAvator(holder.ivPhoto,friend.getPhoto());


        return convertView;
    }

    private void replyAddGroup(final Friend friend) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type","2");
        params.put("content","");
        params.put("fid",friend.getUid());
        RequestUtils.sendPostRequest(Api.REPLY_ADD_FRIEND, params, new ResponseCallBack<Friend>() {
            @Override
            public void onSuccess(Friend data) {
                super.onSuccess(data);
                CommonHelper.showTip(context,"已添加");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ((NewApplyFriendActivity)activity).refresh();
                    }
                }, 1000);
            }

            @Override
            public void onFailure(ServiceException e) {
                CommonHelper.showTip(context,e.getMessage());
            }
        }, Friend.class);
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
        public TextView tvStatus;
        public RelativeLayout rlAdd;
        public TextView tvMsg;
        public TextView tvValues;
    }
}