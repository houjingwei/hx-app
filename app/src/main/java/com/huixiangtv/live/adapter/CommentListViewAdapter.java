package com.huixiangtv.live.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Cm;

import java.util.List;

/**
 * Created by Stone on 16/7/1.
 */
public class CommentListViewAdapter  extends BaseAdapter {

    private static final String TAG = "CommentListViewAdapter";
    private LayoutInflater inflater;
    private Context context;
    private String text;
    private int number;
    private List<Cm> cmList;
    private String str;

    public CommentListViewAdapter(Context context, List<Cm> cmList) {
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

    public class ViewHolder {

        private TextView mTextView;

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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        text = cmList.get(arg0).getNickName() + cmList.get(arg0).getContent();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            str = String.valueOf(ch);
            if (str.equals(":")) {
                number = i;
                break;
            }
        }

        SpannableStringBuilder builder = new SpannableStringBuilder(text);

        // ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan = new ForegroundColorSpan(
                Color.parseColor("#323232"));
        builder.setSpan(redSpan, 0, number, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.mTextView.setText(builder+" ");
        return convertView;
    }
}
