package com.huixiangtv.live.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hjw on 16/5/17.
 */
public class LiveOnlineUsersAdapter extends RecyclerView.Adapter<LiveOnlineUsersAdapter.StarViewHolder> implements View.OnClickListener{

    private RecyclerviewListener listener;

    private List<User> datas = new ArrayList<User>();

    public LiveOnlineUsersAdapter(List<User> ls) {
        if(null!=ls) {
            this.datas = ls;
        }
    }


    @Override
    public StarViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.live_online_users,viewGroup,false);
        StarViewHolder vh = new StarViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(StarViewHolder holder, int position) {
        User star = datas.get(position);
        ImageUtils.display(holder.imageView,star.getPhoto());
        holder.itemView.setTag(star);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public void addData(User user) {
        datas.add(0,user);
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
            listener.onItemClick(view,view.getTag());
        }
    }



    public class StarViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public StarViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ivPhoto);
        }
    }
}