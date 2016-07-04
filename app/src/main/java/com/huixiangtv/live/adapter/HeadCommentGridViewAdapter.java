package com.huixiangtv.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Dynamic;
import com.huixiangtv.live.model.DynamicpPraise;
import com.huixiangtv.live.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stone on 16/7/1.
 */
public class HeadCommentGridViewAdapter extends BaseAdapter {

    private List<DynamicpPraise> list = new ArrayList<>();
    private boolean isHeadPic = false;
    private LayoutInflater inflater;
    private Context context;

    public HeadCommentGridViewAdapter(Context context, List<DynamicpPraise> list ) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int arg0) {

        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {

        return arg0;
    }

    public class ViewHolder {

        private com.github.siyamed.shapeimageview.CircularImageView mImageView;

    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.praise_gridview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (com.github.siyamed.shapeimageview.CircularImageView) convertView
                    .findViewById(R.id.iv_foot_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageUtils.displayAvator(viewHolder.mImageView, list.get(arg0).getPhoto()); //pic



        return convertView;
    }


    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addList(List<DynamicpPraise> ls) {
        if (ls != null) {
            list.addAll(ls);
        }
        notifyDataSetChanged();
    }

}