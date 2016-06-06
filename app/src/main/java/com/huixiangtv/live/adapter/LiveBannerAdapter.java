package com.huixiangtv.live.adapter;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.FastBlur;
import com.huixiangtv.live.utils.image.ImageGlideUtils;

/**
 * Created by Stone on 16/5/27.
 */
public class LiveBannerAdapter extends BaseAdapter {
    private List<Live> mList;
    private Context mContext;
    public static final int APP_PAGE_SIZE = 1;

    public LiveBannerAdapter(Context context, List<Live> list, int page) {
        mContext = context;
        mList = new ArrayList<Live>();
        int i = page * APP_PAGE_SIZE;
        int iEnd = i + APP_PAGE_SIZE;
        while ((i < list.size()) && (i < iEnd)) {
            mList.add(list.get(i));
            i++;
        }
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        AppItem appItem;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.index_single,
                    parent, false);
            appItem = new AppItem();
            appItem.sigImg = (ImageView) convertView.findViewById(R.id.ivIcon);
            appItem.iv_goto_live = (ImageView) convertView.findViewById(R.id.iv_goto_live);
            convertView.setTag(appItem);
        } else {
            appItem = (AppItem) convertView.getTag();
        }


        ImageView ivIcon = appItem.sigImg;
        ImageGlideUtils.display(mContext, mList.get(position).getPhoto(), ivIcon);
        appItem.iv_goto_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    private class AppItem {
        ImageView sigImg;
        ImageView sigImgs;
        TextView text;
        ImageView iv_goto_live;
    }
}