package com.huixiangtv.liveshow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.DynamicComment;

import java.util.List;

/**
 * Created by Stone on 16/7/1.
 */
public class CommentListViewAdapter extends BaseAdapter {

    private static final String TAG = "CommentListViewAdapter";
    private LayoutInflater inflater;
    private Context context;
    private String text;
    private int number;
    private List<DynamicComment> cmList;
    private String str;

    public CommentListViewAdapter(Context context, List<DynamicComment> cmList) {
        this.context = context;
        this.cmList = cmList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return cmList.size();
    }

    @Override
    public Object getItem(int arg0) {

        return cmList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {

        return arg0;
    }

    public void add(DynamicComment dc) {
        cmList.add(dc);
        notifyDataSetChanged();
    }

    public class ViewHolder {

        private TextView mTextView;
        private TextView tv_comment_key;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater
                    .inflate(R.layout.listview_comment_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView
                    .findViewById(R.id.tv_comment_text);
            viewHolder.tv_comment_key = (TextView) convertView.findViewById(R.id.tv_comment_key);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_comment_key.setText(cmList.get(arg0).getNickName() + ":  ");
        viewHolder.mTextView.setText(cmList.get(arg0).getContent());
        return convertView;
    }

    public void clear() {
        cmList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<DynamicComment> ls) {
        if (ls != null) {
            cmList.addAll(ls);
        }
        notifyDataSetChanged();
    }

    public List<DynamicComment> getList() {
        return cmList;
    }

}
