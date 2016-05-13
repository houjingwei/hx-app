package com.huixiang.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huixiang.live.R;
import com.huixiang.live.model.Topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjw on 16/5/13.
 */
public class TopicAdapter extends BaseAdapter {


    Activity activity;
    Context context;
    List<Topic> voList = new ArrayList<Topic>();


    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public TopicAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public TopicAdapter() {
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
            final Topic t = (Topic) getItem(position);
            rowView = LayoutInflater.from(context).inflate(R.layout.topic_item, parent, false);
            TextView title = (TextView) rowView.findViewById(R.id.tvTitle);
            TextView count = (TextView) rowView.findViewById(R.id.tvCount);
            title.setText(t.getTopic());
            count.setText(t.getTopicCount());

        }
        return rowView;
    }

    public void clear() {
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<Topic> ls) {
        if (ls != null) {
            voList.addAll(ls);
        }
        notifyDataSetChanged();
    }
}