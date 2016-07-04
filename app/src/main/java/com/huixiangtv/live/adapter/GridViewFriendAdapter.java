package com.huixiangtv.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Images;
import com.huixiangtv.live.utils.image.ImageUtils;

import java.util.List;

/**
 * Created by Stone on 16/7/1.
 */
public class GridViewFriendAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Images> images;
    private Context context;

    public GridViewFriendAdapter(Context context,List<Images> images) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.images = images;
    }

    @Override
    public int getCount() {
        if (images.size()> 9) {
            return 9;
        } else {
            return images.size();
        }
    }

    @Override
    public Object getItem(int arg0) {
        return images.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return images.size();
    }

    public class ViewHolder {

        private ImageView mImageView;

    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.img_gridview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) convertView
                    .findViewById(R.id.iv_gridview_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageUtils.display(viewHolder.mImageView,images.get(arg0).getBig());

        return convertView;
    }

}
