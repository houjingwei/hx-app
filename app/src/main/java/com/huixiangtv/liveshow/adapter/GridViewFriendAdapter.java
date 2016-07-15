package com.huixiangtv.liveshow.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.DynamicImage;
import com.huixiangtv.liveshow.utils.StringUtil;
import com.huixiangtv.liveshow.utils.image.ImageUtils;
import com.huixiangtv.liveshow.utils.widget.SquareLayout;
import com.huixiangtv.liveshow.utils.widget.WidgetUtil;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview2;

/**
 * Created by Stone on 16/7/1.
 */
public class GridViewFriendAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<DynamicImage> images;
    private Context context;
    private int size = 0;
    private int videoWidth = 0;

    public GridViewFriendAdapter(Context context, List<DynamicImage> images, int size) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.images = images;
        this.size = size;
    }

    @Override
    public int getCount() {
        if (images.size() > 9) {
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
        private SquareLayout rlSL;


    }

    @Override
    public View getView(final int arg0, View convertView, ViewGroup arg2) {

        DynamicImage dynamicImage = (DynamicImage) getItem(arg0);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.img_gridview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) convertView
                    .findViewById(R.id.iv_gridview_img);
            viewHolder.rlSL = (SquareLayout) convertView.findViewById(R.id.rlSL);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ArrayList<String> showImgList = new ArrayList<String>();
        //所有大图图片
        if (null != images) {
            for (DynamicImage img : images) {
                showImgList.add(img.getBig());
            }
        }


        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewImg(arg0, showImgList);
            }
        });
        ViewGroup.LayoutParams  layoutParams =  viewHolder.mImageView.getLayoutParams();


        videoWidth = App.screenWidth - WidgetUtil.dip2px(context, 80);
        double height = 0;
        if (size == 1) {
            if(StringUtil.isNotNull(dynamicImage.getRate())){
                height = (int) (videoWidth/Float.parseFloat(images.get(0).getRate()));
            }else{
                height = (int) (videoWidth*0.75);
            }
            layoutParams.width = videoWidth;
            layoutParams.height =(int) height;
        } else if (size == 2) {

            int width = (int) (videoWidth*0.5-WidgetUtil.dip2px(context,4));
            //layoutParams = new LinearLayout.LayoutParams(width,width);
            //layoutParams.rightMargin = WidgetUtil.dip2px(context,2);

            layoutParams.width = width;
            layoutParams.height =width;


        } else {
            int width = videoWidth/3-WidgetUtil.dip2px(context,4);
            layoutParams.width = width;
            layoutParams.height =width;
            //layoutParams.rightMargin = WidgetUtil.dip2px(context,2);

        }

        viewHolder.mImageView.setLayoutParams(layoutParams);
        ImageUtils.display(viewHolder.mImageView, dynamicImage.getSmall());

        return convertView;
    }

    /**
     * 图片预览
     *
     * @param curPosition
     */
    private void previewImg(int curPosition, ArrayList<String> showImgList) {
        PhotoPreview2.builder().setPhotos(showImgList).setCurrentItem(curPosition).setShowDeleteButton(false).start((Activity) context, PhotoPicker.REQUEST_CODE);
    }

}
