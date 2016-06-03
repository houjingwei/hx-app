package com.huixiangtv.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.model.LiveMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjw on 16/5/13.
 */
public class LiveMsgAdapter extends BaseAdapter {


    Activity activity;
    Context context;
    List<LiveMsg> voList = new ArrayList<LiveMsg>();


    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public LiveMsgAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public LiveMsgAdapter() {
    }


    @Override
    public int getCount() {
        return voList.size();
    }

    @Override
    public LiveMsg getItem(int position) {
        return voList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        final LiveMsg message = (LiveMsg) getItem(position);
        if (null == rowView) {
            rowView = LayoutInflater.from(context).inflate(R.layout.live_msg_item, parent, false);
        }
        TextView msg = (TextView) rowView.findViewById(R.id.tvMsg);
        Log.i("rinima","<<"+message.getNickName()+">>");
//        SpannableString ss = new SpannableString(message.getNickName()+": "+message.getContent());
//
//        ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.mainColor)), 0, message.getNickName().length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msg.setText(message.getContent());
        return rowView;
    }

    public void clear() {
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<LiveMsg> ls) {
        if (ls != null) {
            voList.addAll(ls);
        }
        notifyDataSetChanged();
    }

    public void add(LiveMsg msg) {
        voList.add(msg);
        notifyDataSetChanged();

    }
}