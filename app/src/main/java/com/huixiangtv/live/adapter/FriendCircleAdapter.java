package com.huixiangtv.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.fragment.FragmentCircle;
import com.huixiangtv.live.model.Dynamic;
import com.huixiangtv.live.model.DynamicComment;
import com.huixiangtv.live.model.DynamicImage;
import com.huixiangtv.live.model.DynamicpPraise;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.DateUtils;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.ActionItem;
import com.huixiangtv.live.utils.widget.ListViewCircle;
import com.huixiangtv.live.utils.widget.TitlePopup;
import com.huixiangtv.live.utils.widget.WidgetUtil;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Stone on 16/7/1.
 */
public class FriendCircleAdapter extends BaseAdapter {

    private Context context;
    private FragmentCircle fragmentCircle;
    private List<Dynamic> list = new ArrayList<>();
    private Handler handler;
    int videoWidth = 0;
    private ImageView imagePlay;
    private static int currentId = -1;
    int videoHeight = 0;
    private static ScalableVideoView scalableVideoView;
    private int currTag = 100000;


    /**
     * 上一个播放的视频的position
     */
    int videoIndex = 10000;

    public FriendCircleAdapter(FragmentCircle fragmentCircle, Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.fragmentCircle = fragmentCircle;
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

    public void refreshCommAdapter(DynamicComment dc) {
        List<DynamicComment> dynamicComments = list.get(dc.getCurrentIndex()).getComments();
        dynamicComments.add(0, dc);

        notifyDataSetChanged();
    }

    public class ViewHolder {
        private GridView mGridView;
        private GridView mImgGridView;
        private ListViewCircle mListView;
        private ImageView ivPoint;
        private TextView tvNickName;
        private TextView tv_context;
        private TextView tv_location;
        private TextView tv_time;
        private ImageView ivDmPic;
        private LinearLayout ll_comment_head;
        private HeadCommentGridViewAdapter headCommentGridViewAdapter;
        private CommentListViewAdapter commentListViewAdapter;

        private RelativeLayout rlVideo;
        private ImageView ivVideo;
        private LinearLayout llVideoView;
        private RelativeLayout rlPlay;
        private ImageView ivPlay;
        private LinearLayout lltoDetails;
        private ScalableVideoView mVideoView;
        private String playUrl;
        private boolean isPlay = false;


    }


    @Override
    public View getView(final int current, View convertView, ViewGroup arg2) {
        final Dynamic dynamic = (Dynamic) getItem(current);
        final ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_circle_item, null);
            viewHolder.ll_comment_head = (LinearLayout) convertView.findViewById(R.id.ll_comment_head);
            viewHolder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            viewHolder.tv_context = (TextView) convertView.findViewById(R.id.tv_context);
            viewHolder.tvNickName = (TextView) convertView.findViewById(R.id.tvNickName);
            viewHolder.ivPoint = (ImageView) convertView.findViewById(R.id.ivPoint);
            viewHolder.mGridView = (GridView) convertView.findViewById(R.id.gv_comment_head);
            viewHolder.ivDmPic = (ImageView) convertView.findViewById(R.id.ivDmPic);
            viewHolder.mImgGridView = (GridView) convertView.findViewById(R.id.gv_listView_main_gridView);
            viewHolder.mListView = (ListViewCircle) convertView.findViewById(R.id.lv_item_listView);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.lltoDetails = (LinearLayout) convertView.findViewById(R.id.ll_toDetails);

            //视频
            viewHolder.rlVideo = (RelativeLayout) convertView.findViewById(R.id.rlVideo);
            viewHolder.ivVideo = (ImageView) convertView.findViewById(R.id.ivVideo);
            viewHolder.ivVideo.setVisibility(View.GONE);
            //播放器容器
            viewHolder.llVideoView = (LinearLayout) convertView.findViewById(R.id.llVideoView);
            //播放控制view
            viewHolder.rlPlay = (RelativeLayout) convertView.findViewById(R.id.rlPlay);
            viewHolder.ivPlay = (ImageView) convertView.findViewById(R.id.ivPlay);
            viewHolder.mVideoView = null;
            viewHolder.isPlay =  false;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //convertView.setTag("wode tag"+current);


        //动态详情
        viewHolder.lltoDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> params = new HashMap<String, String>();
                params.put("did", dynamic.getDynamicId());
                ForwardUtils.target((Activity) context, Constant.DYNAMIC_DETAIL, params);
            }
        });

        dynamic.setIsZan(false);
        ImageUtils.display(viewHolder.ivDmPic, dynamic.getPhoto()); //pic
        viewHolder.ll_comment_head.setVisibility(View.GONE);
        if (null != dynamic.getAddress() && dynamic.getAddress().trim().length() > 0) {
            viewHolder.tv_location.setText(dynamic.getAddress());
        } else {
            viewHolder.tv_location.setVisibility(View.GONE);
        }

        viewHolder.tv_time.setText(DateUtils.formatDisplayTime(dynamic.getDate(), "yyyy-MM-dd HH:mm:ss"));
        viewHolder.tv_context.setText(dynamic.getContent());
        viewHolder.mGridView.setVisibility(View.VISIBLE);
        viewHolder.tvNickName.setText(dynamic.getNickName());
        viewHolder.mImgGridView.setVisibility(View.VISIBLE);

        if (null != dynamic.getPraises() && dynamic.getPraises().size() > 0) {
            for (DynamicpPraise dynamicpPraise : dynamic.getPraises()) {
                if (dynamicpPraise.getUid().equals(App.getLoginUser().getUid())) {
                    dynamic.setIsZan(true);
                }
            }
            viewHolder.headCommentGridViewAdapter = new HeadCommentGridViewAdapter(context, dynamic.getPraises());
            viewHolder.ll_comment_head.setVisibility(View.VISIBLE);
            viewHolder.mGridView.setAdapter(viewHolder.headCommentGridViewAdapter);
        } else {
            List<DynamicpPraise> dynamicpPraiseList = new ArrayList<>();
            viewHolder.headCommentGridViewAdapter = new HeadCommentGridViewAdapter(context, dynamicpPraiseList);
            viewHolder.ll_comment_head.setVisibility(View.GONE);
            viewHolder.mGridView.setAdapter(viewHolder.headCommentGridViewAdapter);
        }

        if (dynamic.getType().equals("0")) {  //文本
            viewHolder.rlVideo.setVisibility(View.GONE);
            viewHolder.mImgGridView.setVisibility(View.GONE);
        }


        if (dynamic.getType().equals("1")) {  //图片
            viewHolder.mImgGridView.setVisibility(View.VISIBLE);
            viewHolder.rlVideo.setVisibility(View.GONE);

            if (null != dynamic.getImages() && dynamic.getImages().size() > 0) {
                viewHolder.mImgGridView.setNumColumns(3);
                int size = dynamic.getImages().size();
                if (size == 1) {
                    viewHolder.mImgGridView.setNumColumns(1);
                } else if (size == 2) {
                    viewHolder.mImgGridView.setNumColumns(2);
                } else if (size > 2) {
                    viewHolder.mImgGridView.setNumColumns(3);
                }
                GridViewFriendAdapter gridViewFriendAdapter = new GridViewFriendAdapter(context, dynamic.getImages(), size);
                viewHolder.mImgGridView.setAdapter(gridViewFriendAdapter);
            } else {
                List<DynamicImage> dynamicImages = new ArrayList<>();
                viewHolder.mImgGridView.setAdapter(new GridViewFriendAdapter(context, dynamicImages, 0));
            }
        }

        if (dynamic.getType().equals("2")) {   //视频
            viewHolder.mImgGridView.setVisibility(View.GONE);
            viewHolder.rlVideo.setVisibility(View.VISIBLE);
            viewHolder.rlPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currTag != current) {

                        try {
                            if (list.get(currTag).getScalableVideoView().isPlaying()) {
                                list.get(currTag).getScalableVideoView().pause();
                            }

                        }catch (Exception ex)
                        {

                        }



                        if (null != viewHolder.mVideoView && viewHolder.playUrl.length()>0) {
                            //LinearLayout ll = (LinearLayout) viewHolder.mVideoView.getParent();
                            if (viewHolder.mVideoView.isPlaying()) {
                                viewHolder.mVideoView.pause();
                                //viewHolder.mVideoView.stop();
                            }
                            else {
                                //loadPlayUrlAndPlay(viewHolder, dynamic.getVideoURL(), current);
                                viewHolder.mVideoView.start();
                            }


//                            viewHolder.mVideoView.release();
//                            ll.removeView(viewHolder.mVideoView);


//                            View v = getView(videoIndex,null,null);
                              //Log.i("wodeTag", ll.getId() + "");
//                            LinearLayout ll = (LinearLayout) v.findViewById(R.id.llVideoView);
//                            ll.removeView(mVideoView);
//                            RelativeLayout rl = (RelativeLayout) ll.getParent();
//                            rl.findViewById(R.id.ivPlay).setVisibility(View.VISIBLE);

                        }
                        else
                        {
                            loadPlayUrlAndPlay(viewHolder, dynamic.getVideoURL(), current);
                        }
                        currTag = current;

                    } else {
                        if (viewHolder.isPlay) {
                            toPause(viewHolder);
                        } else {
                            play(viewHolder);
                        }
                        currTag = current;
                    }
                }
            });
            videoWidth = App.screenWidth - WidgetUtil.dip2px(context, 80);
            videoHeight = (int) (videoWidth * 0.75);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.rlVideo.getLayoutParams();
            params.width = videoWidth;
            params.height = videoHeight;
            viewHolder.rlVideo.setLayoutParams(params);
            viewHolder.ivVideo.setVisibility(View.VISIBLE);
            ImageUtils.display(viewHolder.ivVideo, dynamic.getVideoCover());

        }
        else
        {
            viewHolder.mImgGridView.setVisibility(View.VISIBLE);
            viewHolder.rlVideo.setVisibility(View.GONE);
        }


        if (null != dynamic.getComments() && dynamic.getComments().size() > 0) {
            viewHolder.commentListViewAdapter = new CommentListViewAdapter(context, dynamic.getComments());
            viewHolder.mListView.setAdapter(viewHolder.commentListViewAdapter);
        } else {
            List<DynamicComment> dynamicComments = new ArrayList<>();
            viewHolder.mListView.setAdapter(new CommentListViewAdapter(context, dynamicComments));
        }


        viewHolder.ivPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dynamic.setCurrentIndex(current);
                getTitlePopup(v, dynamic, viewHolder.headCommentGridViewAdapter, viewHolder.ll_comment_head);

            }
        });

        return convertView;
    }

    private void getTitlePopup(View v, final Dynamic dynamic, final HeadCommentGridViewAdapter headCommentGridViewAdapter, final LinearLayout ll_comment_head) {

        final TitlePopup titlePopup = new TitlePopup(context, CommonHelper.dip2px(context, 110), CommonHelper.dip2px(
                context, 30));
        titlePopup
                .addAction(new ActionItem(context, "评论", dynamic.getDynamicId(), R.mipmap.v2_dynamic_zan));
        if (!dynamic.isZan()) {
            titlePopup.setComment("赞");
            titlePopup.addAction(new ActionItem(context, "赞", dynamic.getDynamicId(),
                    R.mipmap.v2_dynamic_comm));

        } else {
            titlePopup.setComment("取消");
            titlePopup.addAction(new ActionItem(context, "取消", dynamic.getDynamicId(),
                    R.mipmap.v2_dynamic_comm));
        }


        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {


                if (item.mTitle.equals("赞")) {
                    dynamic.setIsZan(true);
                    titlePopup.setComment("取消");
                    addPraise(ll_comment_head, headCommentGridViewAdapter, item.dynamicId + "");

                }
                if (item.mTitle.equals("取消")) {
                    titlePopup.setComment("赞");
                    dynamic.setIsZan(false);
                    removePraise(ll_comment_head, headCommentGridViewAdapter, item.dynamicId + "");
                }

                if (item.mTitle.equals("评论")) {
                    Message msg = new Message();
                    msg.what = 10;
                    msg.obj = dynamic;
                    handler.sendMessage(msg);
                }
            }
        });

        titlePopup.setAnimationStyle(R.style.cricleBottomAnimation);
        titlePopup.show(v);
    }

    private void unZan(final LinearLayout ll_comment_head, HeadCommentGridViewAdapter headCommentGridViewAdapter) {
        List<DynamicpPraise> dynamicpPraiseList = headCommentGridViewAdapter.getList();
        for (int i = 0; i < dynamicpPraiseList.size(); i++) {
            if (dynamicpPraiseList.get(i).getUid().equals(App.getLoginUser().getUid())) {
                dynamicpPraiseList.remove(i);
            }
        }
        inVisiblePraiseUI(ll_comment_head, headCommentGridViewAdapter);
        headCommentGridViewAdapter.notifyDataSetChanged();
    }

    private void inVisiblePraiseUI(LinearLayout ll_comment_head, HeadCommentGridViewAdapter headCommentGridViewAdapter) {
        if (headCommentGridViewAdapter.getList().size() == 0) {
            ll_comment_head.setVisibility(View.GONE);
        } else {
            ll_comment_head.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 点赞
     *
     * @param headCommentGridViewAdapter
     */
    private void zanBinderUi(final LinearLayout ll_comment_head, HeadCommentGridViewAdapter headCommentGridViewAdapter) {
        List<DynamicpPraise> dynamicpPraiseList = headCommentGridViewAdapter.getList();

        boolean isHasObject = false;

        for (DynamicpPraise dynamicpPraise : dynamicpPraiseList) {
            if (dynamicpPraise.getUid().equals(App.getLoginUser().getUid())) {
                isHasObject = true;
            }
        }
        if (!isHasObject) {

            DynamicpPraise dynamicpPraise = new DynamicpPraise();
            dynamicpPraise.setPhoto(App.getLoginUser().getPhoto());
            dynamicpPraise.setNickName(App.getLoginUser().getNickName());
            dynamicpPraise.setUid(App.getLoginUser().getUid());
            dynamicpPraiseList.add(0, dynamicpPraise);
            headCommentGridViewAdapter.notifyDataSetChanged();
        }
        inVisiblePraiseUI(ll_comment_head, headCommentGridViewAdapter);

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
     * 点赞
     */
    private void addPraise(final LinearLayout ll_comment_head, final HeadCommentGridViewAdapter headCommentGridViewAdapter, String dynamicId) {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("dynamicId", dynamicId);

        RequestUtils.sendPostRequest(Api.DYNAMIC_ADDPRAISE, paramsMap, new ResponseCallBack<Dynamic>() {
            @Override
            public void onSuccess(Dynamic data) {
                if (data != null) {
                    zanBinderUi(ll_comment_head, headCommentGridViewAdapter);
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


    private void removePraise(final LinearLayout ll_comment_head, final HeadCommentGridViewAdapter headCommentGridViewAdapter, String dynamicId) {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("dynamicId", dynamicId);

        RequestUtils.sendPostRequest(Api.DYNAMIC_REMOVEPRAISE, paramsMap, new ResponseCallBack<Dynamic>() {
            @Override
            public void onSuccess(Dynamic data) {
                unZan(ll_comment_head, headCommentGridViewAdapter);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(context, "取消点赞:" + e.getMessage());
            }
        }, Dynamic.class);
    }


    public List<Dynamic> getList() {
        return list;
    }


    /**
     * 获取点播视频地址并播放
     */
    private void loadPlayUrlAndPlay(final ViewHolder viewHolder, String videoUrl, final int current) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", videoUrl);
        params.put("type", "1");
        CommonHelper.videoPlayUrl(params, new ApiCallback<String>() {

            @Override
            public void onSuccess(String data) {
                viewHolder.playUrl = data;

                toPlay(viewHolder, data, current);
            }
        });
    }


    private void play(ViewHolder viewHolder) {
        if (null != viewHolder.mVideoView) {
            viewHolder.isPlay = true;
            viewHolder.mVideoView.start();
            //mVideoView.seekTo(mVideoView.getCurrentPosition());
            viewHolder.ivPlay.setVisibility(View.GONE);
        }
    }

    private void toPause(ViewHolder viewHolder) {

        if (viewHolder.mVideoView.isPlaying()) {
            viewHolder.isPlay = false;
            viewHolder.ivPlay.setVisibility(View.VISIBLE);
            viewHolder.mVideoView.pause();

        } else {
            play(viewHolder);
        }

    }


    private void toPlay(final ViewHolder viewHolder, String playUrl, final int current) {
        try {

            viewHolder.mVideoView = new ScalableVideoView(context);
            viewHolder.mVideoView.setDataSource(playUrl);
            viewHolder.llVideoView.addView(viewHolder.mVideoView);
            viewHolder.mVideoView.setLooping(true);
            list.get(current).setScalableVideoView(viewHolder.mVideoView);
            viewHolder.mVideoView.prepareAsync(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    currTag = current;
                    viewHolder.isPlay = true;
                    viewHolder.mVideoView.setScalableType(ScalableType.CENTER_CROP);
                    viewHolder.mVideoView.start();
                    viewHolder.ivPlay.setVisibility(View.GONE);
                }
            });
//            viewHolder.mVideoView.prepare(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private class MyViode extends AsyncTask<Object, Integer, String>
    {

        @Override
        protected String doInBackground(Object... params) {
            ViewHolder viewHolder = (ViewHolder) params[0];
            Dynamic dynamic = (Dynamic) params[1];
            int current = (int) params[2];
            loadPlayUrlAndPlay(viewHolder, dynamic.getVideoURL(), current);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

}