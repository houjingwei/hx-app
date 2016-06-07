package com.huixiangtv.live.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.Fans;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjw on 16/5/13.
 */
public class MyFansAdapter extends RecyclerView.Adapter<MyFansAdapter.StarViewHolder> implements View.OnClickListener {


    private RecyclerviewListener listener;

    private List<Fans> datas = new ArrayList<Fans>();

    private Context context;

    public MyFansAdapter(List<Fans> ls) {
        if (null != ls) {
            this.datas = ls;
        }
    }

    public MyFansAdapter(Context ct) {
        this.context = ct;
    }


    @Override
    public StarViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fans_item, viewGroup, false);
        StarViewHolder vh = new StarViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(StarViewHolder holder, int position) {
        Fans star = datas.get(position);
        holder.tvRank.setText(position + 1 + "");
        holder.tvHots.setText(star.getHots());
        holder.tvNickName.setText(star.getNickName());
        ImageUtils.displayAvator(holder.ivPhoto, star.getPhoto());
        holder.itemView.setTag(star);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public void addData(Fans user) {
        datas.add(0, user);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        datas.remove(position);
        notifyDataSetChanged();
    }

    public int getDataSize() {
        return datas.size();
    }


    public void setOnItemClickListener(RecyclerviewListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            //注意这里使用getTag方法获取数据
            listener.onItemClick(view, view.getTag());
        }
    }

    public void addList(List<Fans> fansList) {
        if(null!=datas && datas.size()>0){
            datas.addAll(datas.size()-1,fansList);
        }else{
            datas.addAll(fansList);
        }

        notifyDataSetChanged();
    }

    public class StarViewHolder extends RecyclerView.ViewHolder{
        public TextView tvRank;
        public TextView tvNickName;
        public TextView tvHots;
        public ImageView ivPhoto;
        public StarViewHolder(View itemView) {
            super(itemView);
            tvRank = (TextView) itemView.findViewById(R.id.tvRank);
            tvNickName = (TextView) itemView.findViewById(R.id.tvNickName);
            tvHots = (TextView) itemView.findViewById(R.id.tvHots);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
        }

    }
}