package com.huixiangtv.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Dynamic;
import com.huixiangtv.live.model.DynamicComment;
import com.huixiangtv.live.model.DynamicImage;
import com.huixiangtv.live.model.DynamicpPraise;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.ActionItem;
import com.huixiangtv.live.utils.widget.ListViewCircle;
import com.huixiangtv.live.utils.widget.TitlePopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stone on 16/7/1.
 */
public class FriendCircleAdapter extends BaseAdapter {

    private Context context;
    private List<Dynamic> list = new ArrayList<>();


    public FriendCircleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int arg0) {

        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {

        return arg0;
    }



    public class ViewHolder {

        private GridView mGridView;
        private GridView mImgGridView;
        private ListViewCircle mListView;
        private ImageView ivPoint;
        private TextView tvNickName;
        private TextView tv_context;
        private TextView tv_location;
        private ImageView ivDmPic;
        private LinearLayout ll_comment_head;
        private HeadCommentGridViewAdapter headCommentGridViewAdapter;

    }

    @Override
    public View getView(int current, View convertView, ViewGroup arg2) {

       final Dynamic dynamic = (Dynamic) list.get(current);
        dynamic.setIsZan(false);

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_circle_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ll_comment_head = (LinearLayout) convertView.findViewById(R.id.ll_comment_head);
            viewHolder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            viewHolder.tv_context = (TextView) convertView.findViewById(R.id.tv_context);
            viewHolder.tvNickName = (TextView) convertView.findViewById(R.id.tvNickName);
            viewHolder.ivPoint = (ImageView) convertView.findViewById(R.id.ivPoint);
            viewHolder.mGridView = (GridView) convertView
                    .findViewById(R.id.gv_comment_head);
            viewHolder.ivDmPic = (ImageView) convertView.findViewById(R.id.ivDmPic);
            viewHolder.mImgGridView = (GridView) convertView
                    .findViewById(R.id.gv_listView_main_gridView);
            viewHolder.mListView = (ListViewCircle) convertView
                    .findViewById(R.id.lv_item_listView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        ImageUtils.display(viewHolder.ivDmPic, dynamic.getPhoto()); //pic

        viewHolder.ll_comment_head.setVisibility(View.GONE);
        if(null!= dynamic.getAddress() && dynamic.getAddress().trim().length()>0) {
            viewHolder.tv_location.setText(dynamic.getAddress());
        }
        else
        {
            viewHolder.tv_location.setVisibility(View.GONE);
        }

        viewHolder.tv_context.setText(dynamic.getContent());
        viewHolder.mGridView.setVisibility(View.VISIBLE);
        viewHolder.tvNickName.setText(dynamic.getNickName());
        viewHolder.mImgGridView.setVisibility(View.VISIBLE);

        if (null != dynamic.getPraises() && dynamic.getPraises().size() > 0) {

            for(DynamicpPraise dynamicpPraise :dynamic.getPraises())
            {
              if(dynamicpPraise.getUid().equals(App.getLoginUser().getUid()))
              {
                  dynamic.setIsZan(true);
              }
            }

            viewHolder.headCommentGridViewAdapter = new HeadCommentGridViewAdapter(context, dynamic.getPraises());
            viewHolder.ll_comment_head.setVisibility(View.VISIBLE);
            viewHolder.mGridView
                    .setAdapter(viewHolder.headCommentGridViewAdapter);
        }
        else
        {
            List<DynamicpPraise> dynamicpPraiseList  =new ArrayList<>();
            viewHolder.headCommentGridViewAdapter = new HeadCommentGridViewAdapter(context,dynamicpPraiseList);
            viewHolder.ll_comment_head.setVisibility(View.GONE);
            viewHolder.mGridView
                    .setAdapter(viewHolder.headCommentGridViewAdapter);
        }



        if (null != dynamic.getImages() && dynamic.getImages().size() > 0) {
            viewHolder.mImgGridView.setNumColumns(3);

            GridViewFriendAdapter gridViewFriendAdapter = new GridViewFriendAdapter(context, dynamic.getImages());
            viewHolder.mImgGridView.setAdapter(gridViewFriendAdapter);



            int size = dynamic.getImages().size();
            if (size==1){
                viewHolder.mImgGridView.setNumColumns(1);
            }
            else if (size==2){
                viewHolder.mImgGridView.setNumColumns(2);
            }
            else if (size>2){
                viewHolder.mImgGridView.setNumColumns(3);
            }
            gridViewFriendAdapter.notifyDataSetChanged();
        } else
        {
            List<DynamicImage> dynamicImages = new ArrayList<>();
            viewHolder.mImgGridView.setAdapter(new GridViewFriendAdapter(context, dynamicImages));
        }

        if (null != dynamic.getComments() && dynamic.getComments().size() > 0) {
            viewHolder.mListView.setAdapter(new CommentListViewAdapter(context, dynamic.getComments()));
        }
        else
        {
            List<DynamicComment> dynamicComments = new ArrayList<>();
            viewHolder.mListView.setAdapter(new CommentListViewAdapter(context, dynamicComments));
        }

        viewHolder.ivPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTitlePopup(v,dynamic);

            }
        });





        return convertView;
    }

    private void getTitlePopup(View v,Dynamic dynamic) {

        final TitlePopup titlePopup = new TitlePopup(context, CommonHelper.dip2px(context, 165), CommonHelper.dip2px(
                context, 40));
        titlePopup
                .addAction(new ActionItem(context, "评论", dynamic.getDynamicId(), R.drawable.point_zan));
        if(!dynamic.isZan())
        {
            titlePopup.setComment("赞");
            titlePopup.addAction(new ActionItem(context, "赞", dynamic.getDynamicId(),
                    R.drawable.point_comm));
        }
        else
        {
            titlePopup.setComment("取消");
            titlePopup.addAction(new ActionItem(context, "取消", dynamic.getDynamicId(),
                    R.drawable.point_comm));
        }


        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                if (item.mTitle.equals("赞")) {

                    addPraise(item.dynamicId + "");
                    titlePopup.setComment("取消");


                }
                if (item.mTitle.equals("取消")) {

                    removePraise(item.dynamicId + "");
                    titlePopup.setComment("赞");
                }

                if (item.mTitle.equals("评论")) {

                }
            }
        });

        titlePopup.setAnimationStyle(R.style.cricleBottomAnimation);
        titlePopup.show(v);
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addList(List<Dynamic> ls) {
        if (ls != null) {
            list.addAll(ls);
        }
        notifyDataSetChanged();
    }




    /**
     *  点赞
     */
    private void addPraise(String dynamicId) {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("dynamicId", dynamicId);

        RequestUtils.sendPostRequest(Api.DYNAMIC_ADDPRAISE, paramsMap, new ResponseCallBack<Dynamic>() {
            @Override
            public void onSuccess(Dynamic data) {
                if (data != null) {

                    getPraise(data.getDynamicId());
                    CommonHelper.showTip(context, "点赞成功");
                } else {
                    CommonHelper.showTip(context, "点赞失败");
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(context, "点赞:" + e.getMessage());
            }
        }, Dynamic.class);
    }


    private void removePraise(String dynamicId) {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("dynamicId", dynamicId);

        RequestUtils.sendPostRequest(Api.DYNAMIC_REMOVEPRAISE, paramsMap, new ResponseCallBack<Dynamic>() {
            @Override
            public void onSuccess(Dynamic data) {

            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(context, "取消点赞:" + e.getMessage());
            }
        }, Dynamic.class);
    }


    private void getPraise(String dynamicId) {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", "1");
        paramsMap.put("pageSize","30");
        paramsMap.put("dynamicId", dynamicId);

        RequestUtils.sendPostRequest(Api.DYNAMIC_GETPRAISES, paramsMap, new ResponseCallBack<Dynamic>() {
            @Override
            public void onSuccess(Dynamic data) {

                String dd = data.getAddress();

            }



            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(context, "点赞列表:" + e.getMessage());
            }
        }, Dynamic.class);
    }

}

