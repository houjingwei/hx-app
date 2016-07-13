package com.huixiangtv.liveshow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stone on 16/5/14.  公共适配器
 */public abstract class CommonAdapter<B> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    /**
     * 实体bean容器
     */
    protected List<B> mDatas;
    /**
     * item模板布局id
     */
    protected final int mItemLayoutId;

    public CommonAdapter(Context context, int itemLayoutId) {
        this(context, null, itemLayoutId);
    }

    public CommonAdapter(Context context, List<B> listData, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = listData;
        this.mItemLayoutId = itemLayoutId;
    }

    public void setDataList(List<B> listData) {
        this.mDatas = listData;
        notifyDataSetChanged();
    }

    public void clearData() {
        if(mDatas != null) {
            mDatas.clear();
        }
    }

    public void clearDataAndNotifyDataSetChanged() {
        clearData();
        notifyDataSetChanged();
    }

    public void addDataListAndNotifyDataSetChanged(List<B> listData) {
        addDataList(listData);
        notifyDataSetChanged();
    }

    public void addDataList(List<B> listData) {
        if(mDatas == null) {
            mDatas = new ArrayList<B>();
        }
        mDatas.addAll(listData);
    }

    public List<B> getDatas() {
        return mDatas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public B getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        if(position<getCount()){
            convert(viewHolder, position, getItem(position));
        }
        return viewHolder.getConvertView();

    }

    /**
     * 子类覆写此方法进行item内控件的处理
     *
     * @param helper
     * @param item
     */
    public abstract void convert(ViewHolder helper, int position, B item);

    private ViewHolder getViewHolder(int position, View convertView,
                                     ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }

}
