package com.huixiangtv.live.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.Fans;
import com.huixiangtv.live.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjw on 16/5/13.
 */
public class MyFansAdapter extends RecyclerView.Adapter<MyFansAdapter.FansViewHolder> implements View.OnClickListener{





    private RecyclerviewListener listener;

    private List<Fans> datas = new ArrayList<Fans>();

    public MyFansAdapter() {

    }


    @Override
    public FansViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fans_item, viewGroup, false);
        FansViewHolder vh = new FansViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(FansViewHolder holder, int position) {
        Fans fans = datas.get(position);
        holder.tvRank.setText(position+1+"");
        holder.tvHots.setText(fans.getHots());
        holder.tvNickName.setText(fans.getNickName());
        ImageUtils.displayAvator(holder.ivPhoto,fans.getPhoto());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public void addData(List<Fans> fans) {
        if(null!=datas && datas.size()>0){
            datas.addAll(datas.size()-1,fans);
        }else{
            datas.addAll(0,fans);
        }
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        datas.remove(position);
        notifyDataSetChanged();
    }

    public void clear() {
        datas.clear();
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
            listener.onItemClick(view,view.getTag());
        }
    }




    public class FansViewHolder extends RecyclerView.ViewHolder{

        public ImageView ivPhoto;
        public TextView tvRank ;
        public TextView tvNickName ;
        public TextView tvHots ;

        public FansViewHolder(View itemView) {
            super(itemView);
            tvRank = (TextView) itemView.findViewById(R.id.tvRank);
            tvNickName = (TextView) itemView.findViewById(R.id.tvNickName);
            tvHots = (TextView) itemView.findViewById(R.id.tvHots);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
        }
    }
}