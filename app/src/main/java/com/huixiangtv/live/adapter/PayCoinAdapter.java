package com.huixiangtv.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Coin;
import com.huixiangtv.live.model.Topic;
import com.huixiangtv.live.utils.widget.WidgetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjw on 16/5/13.
 */
public class PayCoinAdapter extends BaseAdapter {


    Activity activity;
    Context context;
    List<Coin> voList = new ArrayList<Coin>();


    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public PayCoinAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public PayCoinAdapter() {
    }


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
        View rowView = convertView;
        if (null == rowView) {
            final Coin coin = (Coin) getItem(position);
            rowView = LayoutInflater.from(context).inflate(R.layout.buy_coin_item, parent, false);
            TextView tvCoinBuy = (TextView) rowView.findViewById(R.id.tvCoinBuy);
            TextView tvCoin = (TextView) rowView.findViewById(R.id.tvCoin);
            tvCoin.setText(coin.getName());
            tvCoinBuy.setText("￥"+coin.getPrice());

        }
        return rowView;
    }

    public void clear() {
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<Coin> ls) {
        if (ls != null) {
            voList.addAll(ls);
        }
        notifyDataSetChanged();
    }
}