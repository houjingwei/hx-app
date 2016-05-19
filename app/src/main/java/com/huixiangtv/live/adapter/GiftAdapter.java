package com.huixiangtv.live.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Gift;
import com.huixiangtv.live.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by houjingwei on 15/11/4.
 */
public class GiftAdapter extends BaseAdapter {
    private Integer itemBackgroundColor=null;
    Activity activity;
    Context context;
    List<Gift> voList = new ArrayList<Gift>();

    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public GiftAdapter(Activity activity){
        this.activity = activity;
        this.context = activity;
    }

    public GiftAdapter(){}

    @Override
    public int getCount() {
        return voList.size();
    }

    @Override
    public Object getItem(int position) {
        return voList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = this.viewMap.get(position);
        if(null == rowView){
            Gift gift = (Gift) getItem(position);
            rowView = LayoutInflater.from(context).inflate(R.layout.gift_list_item, null);
            ImageView icon = (ImageView) rowView.findViewById(R.id.ivIcon);
            TextView tvTitle = (TextView) rowView.findViewById(R.id.tvTitle);
            TextView tvCoin = (TextView) rowView.findViewById(R.id.tvMangoCoin);
            RelativeLayout giftRoot = (RelativeLayout) rowView.findViewById(R.id.giftItem);


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)giftRoot.getLayoutParams();
            params.width = App.screenWidth/4;
            params.height = App.screenWidth/4;
            giftRoot.setLayoutParams(params);

            tvTitle.setText(gift.getName());
            tvCoin.setText(gift.getPrice());
            tvCoin.setTextColor(context.getResources().getColor(R.color.orange));
            icon.setTag(5);


            RelativeLayout.LayoutParams iconParams =  (RelativeLayout.LayoutParams)icon.getLayoutParams();
            iconParams.width = (int) (App.screenWidth/5/1.5);
            iconParams.height = (int) (App.screenWidth/5/1.5);
            icon.setLayoutParams(iconParams);



            ImageUtils.display(icon, gift.getIcon());
            if(itemBackgroundColor!=null){
                giftRoot.setBackgroundColor(itemBackgroundColor);
            }


            viewMap.put(position,rowView);
        }

        return rowView;
    }

    public void clear() {
        viewMap.clear();
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<Gift> ls) {
        if (ls != null) {
            voList.addAll(ls);

        }
        notifyDataSetChanged();
    }

    public void setItemBackgroundColor(int itemBackgroundColor) {
        this.itemBackgroundColor = itemBackgroundColor;
    }


}
