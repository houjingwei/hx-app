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

import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Dynamic;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.ActionItem;
import com.huixiangtv.live.utils.widget.ListViewCircle;
import com.huixiangtv.live.utils.widget.TitlePopup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stone on 16/7/1.
 */
public class FriendCircleAdapter extends BaseAdapter {

    private TitlePopup titlePopup;
    private Context context;
    private List<Dynamic> list = new ArrayList<>();


    public FriendCircleAdapter(TitlePopup titlePopup, Context context) {
        this.context = context;
        this.titlePopup = titlePopup;
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

    }

    @Override
    public View getView(int current, View convertView, ViewGroup arg2) {

        Dynamic dynamic = (Dynamic) list.get(current);
        ViewHolder viewHolder;
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
//        viewHolder.tv_location.setText(list.get(current));
        viewHolder.tv_context.setText(dynamic.getContent());
        viewHolder.mGridView.setVisibility(View.VISIBLE);
        viewHolder.tvNickName.setText(dynamic.getNickName());
        viewHolder.mImgGridView.setVisibility(View.VISIBLE);

        if (null != dynamic.getPraises() && dynamic.getPraises().size() > 0) {
            viewHolder.ll_comment_head.setVisibility(View.VISIBLE);
            viewHolder.mGridView
                    .setAdapter(new HeadCommentGridViewAdapter(context, dynamic.getPraises()));
        }
        if (null != dynamic.getImage() && dynamic.getImage().size() > 0) {
            viewHolder.mImgGridView.setAdapter(new GridViewFriendAdapter(context, dynamic.getImage()));
        }

        if (null != dynamic.getComments() && dynamic.getComments().size() > 0) {
            viewHolder.mListView.setAdapter(new CommentListViewAdapter(context, dynamic.getComments()));
        }
        viewHolder.ivPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titlePopup.setAnimationStyle(R.style.cricleBottomAnimation);
                titlePopup.show(v);

            }
        });

        return convertView;
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

}

