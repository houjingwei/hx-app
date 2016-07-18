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
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.Live;
import com.huixiangtv.liveshow.utils.image.ImageUtils;
import com.huixiangtv.liveshow.utils.widget.LinearLayoutForListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjw on 16/5/13.
 */
public class GroupListAdapter extends BaseAdapter {


    Activity activity;
    Context context;
    List<Live> voList = new ArrayList<Live>();


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
        Live live = (Live) getItem(position);
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.group_list_item, parent, false);
            holder.rlRoot = (LinearLayout) convertView.findViewById(R.id.llroot);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            holder.tvContext = (TextView) convertView.findViewById(R.id.tvContext);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        String city = live.getCity()==null || live.getCity().trim().length()==0?"":"."+live.getCity();
        String title = live.getTitle()==null || live.getTitle().trim().length()==0?"":"."+live.getTitle();
        holder.tvTitle.setText(live.getNickName()+" "+city+title);
        holder.tvContext.setText(live.getNickName());
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.rlRoot.getLayoutParams();
        params.height = (int) (App.screenWidth * 0.2);
        holder.rlRoot.setLayoutParams(params);
        ImageUtils.display(holder.ivPhoto, live.getImg2());

        return convertView;
    }

    public void clear() {
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<Live> ls) {
        if (ls != null) {
            voList.addAll(ls);
        }
        notifyDataSetChanged();
    }



    static class ViewHolder
    {

        public LinearLayout rlRoot;
        public TextView tvTitle;
        public TextView tvContext;
        public ImageView ivPhoto;






    }
}