package com.huixiangtv.live.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.ImageUtils;

import org.w3c.dom.Text;

/**
 * Created by Stone on 16/5/24.
 */
public class LiveBannerAdapter extends BaseAdapter {
    private Context con;
    public List<Live> mType = null;

    public LiveBannerAdapter(Context con, List<Live> mType) {
        this.con = con;
        this.mType = mType;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mType.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return pos;
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return pos;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        final myV holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(con).inflate(R.layout.index_single,
                    parent, false);
            holder = new myV();
            holder.tvInfo = (TextView) convertView.findViewById(R.id.tvInfo);
            holder.sigImg = (ImageView) convertView.findViewById(R.id.ivIcon);
            holder.iv_goto_live = (ImageView) convertView.findViewById(R.id.iv_goto_live);
            holder.llInfo = (LinearLayout) convertView.findViewById(R.id.llInfo);
            holder.rlpp = (RelativeLayout) convertView.findViewById(R.id.rlpp);
            convertView.setTag(holder);
        } else {
            holder = (myV) convertView.getTag();
        }
        ImageView ivIcon = holder.sigImg;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(App.screenWidth,App.screenHeight);
        params.height = (int) (App.screenHeight);
       // holder.rlpp.setLayoutParams(params);
        LinearLayout llInfo = holder.llInfo;
        llInfo.setBackgroundResource(R.color.black_01);
        llInfo.getBackground().mutate().setAlpha(255);
        holder.tvInfo.setText(mType.get(pos).getNickName());
        ImageUtils.display(ivIcon, mType.get(pos).getPhoto());
        holder.iv_goto_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return convertView;
    }

    static class myV {
        ImageView sigImg;
        LinearLayout llInfo;
        TextView tvInfo;
        ImageView iv_goto_live;
        RelativeLayout rlpp;

    }

}
