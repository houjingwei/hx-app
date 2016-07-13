package com.huixiangtv.liveshow.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.model.Dynamic;
import com.huixiangtv.liveshow.model.DynamicImage;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CenterLoadingView;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.StringUtil;
import com.huixiangtv.liveshow.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjw on 16/5/13.
 */
public class MyCircleAdapter extends BaseAdapter {




    Activity activity;
    Context context;
    List<Dynamic> voList = new ArrayList<Dynamic>();

    private int photoWidth;




    public MyCircleAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public MyCircleAdapter() {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        final Dynamic dn = (Dynamic) getItem(position);
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.my_circle_item, parent, false);
            holder.llRootView = (LinearLayout) convertView.findViewById(R.id.llRootView);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            holder.tvMonth = (TextView) convertView.findViewById(R.id.tvMonth);
            holder.tvDay = (TextView) convertView.findViewById(R.id.tvDay);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            holder.tvImgCount = (TextView) convertView.findViewById(R.id.tvImgCount);



            holder.oneImg = (ImageView) convertView.findViewById(R.id.oneImg);
            holder.llImg2 = (LinearLayout) convertView.findViewById(R.id.llImg2);
            holder.twoImg1 = (ImageView) convertView.findViewById(R.id.twoImg1);
            holder.twoImg2 = (ImageView) convertView.findViewById(R.id.twoImg2);

            holder.llImg3 = (LinearLayout) convertView.findViewById(R.id.llImg3);
            holder.threeImg1 = (ImageView) convertView.findViewById(R.id.threeImg1);
            holder.threeImg2 = (ImageView) convertView.findViewById(R.id.threeImg2);
            holder.threeImg3 = (ImageView) convertView.findViewById(R.id.threeImg3);

            holder.llImg4 = (LinearLayout) convertView.findViewById(R.id.llImg4);
            holder.fourImg1 = (ImageView) convertView.findViewById(R.id.fourImg1);
            holder.fourImg2 = (ImageView) convertView.findViewById(R.id.fourImg2);
            holder.fourImg3 = (ImageView) convertView.findViewById(R.id.fourImg3);
            holder.fourImg4 = (ImageView) convertView.findViewById(R.id.fourImg4);

            holder.rlVideo = (RelativeLayout) convertView.findViewById(R.id.rlVideo);
            holder.ivVideo = (ImageView) convertView.findViewById(R.id.ivVideo);




            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tvDate.setText(dn.getLastDate());
        holder.tvMonth.setText(dn.getMonth());
        holder.tvDay.setText(dn.getDay());
        if(StringUtil.isNotEmpty(dn.getContent())){
            if(dn.getContent().length()>30){
                holder.tvContent.setText(dn.getContent().substring(0,30)+"...");
            }else{
                holder.tvContent.setText(dn.getContent());
            }
        }


        if(dn.getType().equals("0")){
            holder.tvImgCount.setVisibility(View.GONE);
            holder.rlVideo.setVisibility(View.GONE);
            hideImages(holder);
        }else if(dn.getType().equals("1")){
            holder.rlVideo.setVisibility(View.GONE);
            holder.tvImgCount.setText("共"+dn.getImages().size()+"张");
            setImages(holder,dn.getImages());
        }else if(dn.getType().equals("2")){
            holder.rlVideo.setVisibility(View.VISIBLE);
            holder.tvImgCount.setVisibility(View.GONE);
            if(StringUtil.isNotNull(dn.getVideoCover())) {

                ImageUtils.display(holder.ivVideo, dn.getVideoCover());
            }else{

                holder.ivVideo.setImageDrawable(activity.getResources().getDrawable(R.drawable.default_43));
                //ImageUtils.display(holder.ivVideo, "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=4159758596,1465917751&fm=58");
            }
            hideImages(holder);
        }


//        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                delDynamic(dn,position);
//            }
//        });



        return convertView;
    }

    CenterLoadingView loadingDialog = null;
    private void delDynamic(Dynamic dn, final int position) {
        Map<String,String> params = new HashMap<>();
        params.put("dynamicId",dn.getDynamicId());

        RequestUtils.sendPostRequest(Api.DELETE_DYNAMIC, params, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);

                if(null!=loadingDialog){
                    loadingDialog.dismiss();
                }
                voList.remove(position);
                notifyDataSetChanged();
                CommonHelper.showTip(activity,"动态删除成功");


            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                if(null!=loadingDialog){
                    loadingDialog.dismiss();
                }
                CommonHelper.showTip(activity,e.getMessage());
            }
        },String.class);
    }

    private void hideImages(ViewHolder holder) {
        holder.llImg4.setVisibility(View.GONE);
        holder.oneImg.setVisibility(View.GONE);
        holder.llImg2.setVisibility(View.GONE);
        holder.llImg3.setVisibility(View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setImages(ViewHolder holder, List<DynamicImage> images) {
        if(images.size()>3){
            holder.llImg4.setVisibility(View.VISIBLE);
            holder.oneImg.setVisibility(View.GONE);
            holder.llImg2.setVisibility(View.GONE);
            holder.llImg3.setVisibility(View.GONE);

            ImageUtils.display(holder.fourImg1,images.get(0).getSmall());
            ImageUtils.display(holder.fourImg2,images.get(1).getSmall());
            ImageUtils.display(holder.fourImg3,images.get(2).getSmall());
            ImageUtils.display(holder.fourImg4,images.get(3).getSmall());

        }else if(images.size()==3){
            holder.llImg3.setVisibility(View.VISIBLE);
            holder.oneImg.setVisibility(View.GONE);
            holder.llImg2.setVisibility(View.GONE);
            holder.llImg4.setVisibility(View.GONE);

            ImageUtils.display(holder.threeImg1,images.get(0).getSmall());
            ImageUtils.display(holder.threeImg2,images.get(1).getSmall());
            ImageUtils.display(holder.threeImg3,images.get(2).getSmall());
        }else if(images.size()==2){
            holder.llImg2.setVisibility(View.VISIBLE);
            holder.oneImg.setVisibility(View.GONE);
            holder.llImg3.setVisibility(View.GONE);
            holder.llImg4.setVisibility(View.GONE);


            ImageUtils.display(holder.twoImg1,images.get(0).getSmall());
            ImageUtils.display(holder.twoImg2,images.get(1).getSmall());


        }else if(images.size()==1){
            holder.oneImg.setVisibility(View.VISIBLE);
            holder.llImg2.setVisibility(View.GONE);
            holder.llImg3.setVisibility(View.GONE);
            holder.llImg4.setVisibility(View.GONE);

            ImageUtils.display(holder.oneImg,images.get(0).getSmall());
        }
    }

    public void clear() {
        voList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<Dynamic> ls) {
        if (ls != null) {
            voList.addAll(ls);
        }
        notifyDataSetChanged();
    }

    public void setPhotoWidth(int photoWidth) {
        this.photoWidth = photoWidth;
    }


    static class ViewHolder
    {
        LinearLayout llRootView;

        RelativeLayout rlVideo;
        ImageView ivVideo;

        TextView tvDate ;
        TextView tvMonth ;
        TextView tvDay;
        TextView tvContent;
        TextView tvImgCount;
        TextView tvDelete;

        ImageView oneImg;

        LinearLayout llImg2;
        ImageView twoImg1;
        ImageView twoImg2;

        LinearLayout llImg3;
        ImageView threeImg1;
        ImageView threeImg2;
        ImageView threeImg3;

        LinearLayout llImg4;
        ImageView fourImg1;
        ImageView fourImg2;
        ImageView fourImg3;
        ImageView fourImg4;

    }

}