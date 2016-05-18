package com.huixiangtv.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.model.LiveChatMsg;

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
    List<LiveChatMsg> voList = new ArrayList<LiveChatMsg>();


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
    public LiveChatMsg getItem(int position) {
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
            final LiveChatMsg message = (LiveChatMsg) getItem(position);
            rowView = LayoutInflater.from(context).inflate(R.layout.live_msg_item, parent, false);
            TextView msg = (TextView) rowView.findViewById(R.id.tvMsg);

            SpannableString ss = new SpannableString(message.getUserName()+": "+message.getMsg());
            ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, message.getUserName().length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //ss.setSpan(new ForegroundColorSpan(Color.GREEN), message.getUserName().length()+1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msg.setText(ss);
        }
        return rowView;
    }

    public void clear() {
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<LiveChatMsg> ls) {
        if (ls != null) {
            voList.addAll(ls);
        }
        notifyDataSetChanged();
    }

    public void add(LiveChatMsg msg) {
        voList.add(msg);
        notifyDataSetChanged();

    }
}