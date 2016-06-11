package com.huixiangtv.live.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.model.PlayUrl;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.ImageGlideUtils;

/**
 * Created by Stone on 16/5/27.
 */
public class LiveBannerAdapter extends BaseAdapter {
    private List<Live> mList;
    private Context mContext;
    private Activity activity;
    public static final int APP_PAGE_SIZE = 1;

    public LiveBannerAdapter(Activity activity,Context context, List<Live> list, int page) {
        mContext = context;
        this.activity = activity;
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

    public View getView(final int position, View convertView, ViewGroup parent) {
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
                loadUrlAndShow(activity,mList.get(position));
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

    private void loadUrlAndShow(final Activity activity, final Live live) {
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("lid",live.getLid());
        RequestUtils.sendPostRequest(Api.PLAY_URL, params, new ResponseCallBack<PlayUrl>() {
            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }

            @Override
            public void onSuccess(PlayUrl data) {
                super.onSuccess(data);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("isPlay", "true");
                map.put("lid", live.getLid());
                map.put("playUrl", data.getUrl());
                ForwardUtils.target(activity, Constant.LIVE, map);
            }

            @Override
            public void onSuccessList(List<PlayUrl> datas) {
                super.onSuccessList(datas);
                if (null != datas && datas.size() > 0) {
                    PlayUrl data = datas.get(0);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("isPlay", "true");
                    map.put("playUrl", data.getUrl());
                    map.put("lid", live.getLid());
                    ForwardUtils.target(activity, Constant.LIVE, map);
                }
            }
        }, PlayUrl.class);
    }
}