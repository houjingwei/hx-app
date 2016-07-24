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
import com.huixiangtv.liveshow.model.DynamicImage;
import com.huixiangtv.liveshow.ui.ZJImageView;
import com.huixiangtv.liveshow.utils.StringUtil;
import com.huixiangtv.liveshow.utils.image.ImageUtils;
import com.huixiangtv.liveshow.utils.image.RoundImageView;
import com.huixiangtv.liveshow.utils.widget.SquareLayout;
import com.huixiangtv.liveshow.utils.widget.WidgetUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview2;

/**
 * Created by Stone on 16/7/1.
 */
public class GridViewGroupInfoAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<DynamicImage> images;
    private Context context;
    private int videoWidth = 0;

    public GridViewGroupInfoAdapter(Context context, List<DynamicImage> images) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.images = images;
    }

    @Override
    public int getCount() {
            return images.size();
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

        private ZJImageView mImageView;
        private TextView tvName;
        private SquareLayout rlSL;


    }

    @Override
    public View getView(final int arg0, View convertView, ViewGroup arg2) {

        DynamicImage dynamicImage = (DynamicImage) getItem(arg0);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.img_gridview_group_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ZJImageView) convertView
                    .findViewById(R.id.iv_gridview_img);
            viewHolder.rlSL = (SquareLayout) convertView.findViewById(R.id.rlSL);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        double height = 0;

        height = 0.2;

        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) viewHolder.mImageView.getLayoutParams();
        layoutParams.width = App.screenWidth;
        layoutParams.height = (int) (App.screenWidth * height);
        viewHolder.mImageView.setLayoutParams(layoutParams);

        if(images.size()-1 == arg0)
        {
            viewHolder.tvName.setText("邀请成员");
            viewHolder.mImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.v3_group_chat_defind));
        }
        else {
            ImageUtils.display(viewHolder.mImageView, dynamicImage.getSmall());
            viewHolder.tvName.setText("刘德华");
        }

        return convertView;
    }


}
