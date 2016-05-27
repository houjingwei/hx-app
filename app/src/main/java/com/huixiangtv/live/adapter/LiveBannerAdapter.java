package com.huixiangtv.live.adapter;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Live;
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
            appItem.tvInfo = (TextView) convertView.findViewById(R.id.tvInfo);
            appItem.sigImg = (ImageView) convertView.findViewById(R.id.ivIcon);
            appItem.iv_goto_live = (ImageView) convertView.findViewById(R.id.iv_goto_live);
            appItem.llInfo = (LinearLayout) convertView.findViewById(R.id.llInfo);
            appItem.rlpp = (RelativeLayout) convertView.findViewById(R.id.rlpp);
            convertView.setTag(appItem);
        } else {
            appItem = (AppItem) convertView.getTag();
        }


        ImageView ivIcon = appItem.sigImg;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(App.screenWidth, App.screenHeight);
        params.height = (int) (App.screenHeight);
        LinearLayout llInfo = appItem.llInfo;
        llInfo.setBackgroundResource(R.color.black_01);
        llInfo.getBackground().mutate().setAlpha(255);
        appItem.tvInfo.setText(mList.get(position).getNickName());
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
        LinearLayout llInfo;
        TextView tvInfo;
        ImageView iv_goto_live;
        RelativeLayout rlpp;
    }
}