package com.huixiangtv.liveshow.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.huixiangtv.liveshow.R;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;


public class ShareTwoWindow extends BasePopupWindow implements OnClickListener {

    private Activity context;
    private View view;
    ScrollView bottom;
    SelectShareListener listener;

    private LinearLayout shareSms;
    private LinearLayout shareQQ;
    private LinearLayout shareWeixin;
    private LinearLayout shareWxfriend;
    private LinearLayout shareSina;
    private LinearLayout shareCopy;
    private LinearLayout shareQQZone;

    private Animation mShowAnimation;


    private void initAnimation() {
        mShowAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_up);

    }

    public ShareTwoWindow(Activity context, int width, int height) {
        this.context = context;
        super.setWidth(width);
        super.setHeight(height);
        initPopUpWindow();
        initAnimation();
        show();
    }

    private void show() {
        bottom.setAnimation(mShowAnimation);
        bottom.setVisibility(View.VISIBLE);
    }



    private ViewPager viewPager;
    private ArrayList<View> shareViews;
    private RelativeLayout[] pointRootViews;
    // 包裹滑动图片LinearLayout
    private ViewGroup main;
    private LinearLayout llPointView;

    public void initPopUpWindow() {
        try {
            view = RelativeLayout.inflate(context, R.layout.pop_two_share, null);
            bottom = (ScrollView) view.findViewById(R.id.bottom);
            view.findViewById(R.id.pop_layout).setOnClickListener(this);
            super.setFocusable(true);
            super.setOutsideTouchable(true);
            super.setBackgroundDrawable(new BitmapDrawable());
            this.setContentView(view);
            this.setWidth(LayoutParams.FILL_PARENT);
            this.setHeight(LayoutParams.FILL_PARENT);
            this.setFocusable(true);
            this.setAnimationStyle(R.style.popupAnimation);

            initView();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initView() {
        shareViews = new ArrayList<View>();
        View shareViewOne = LayoutInflater.from(context).inflate(R.layout.share_one_view, null);
        shareViews.add(shareViewOne);
        View shareViewTwo = LayoutInflater.from(context).inflate(R.layout.share_two_view, null);
        shareViews.add(shareViewTwo);

        shareWxfriend = (LinearLayout) shareViewOne.findViewById(R.id.shareWxfriend);
        shareWeixin = (LinearLayout) shareViewOne.findViewById(R.id.shareWeixin);
        shareSina = (LinearLayout) shareViewOne.findViewById(R.id.shareSina);
        shareQQ = (LinearLayout) shareViewOne.findViewById(R.id.shareQQ);
        shareQQZone = (LinearLayout) shareViewOne.findViewById(R.id.shareQQZone);

        shareCopy = (LinearLayout) shareViewTwo.findViewById(R.id.shareCopy);
        shareSms = (LinearLayout) shareViewTwo.findViewById(R.id.shareSms);

        shareSms.setOnClickListener(this);
        shareQQ.setOnClickListener(this);
        shareWeixin.setOnClickListener(this);
        shareWxfriend.setOnClickListener(this);
        shareSina.setOnClickListener(this);
        shareCopy.setOnClickListener(this);
        shareQQZone.setOnClickListener(this);


        llPointView = (LinearLayout) view.findViewById(R.id.llPointView);
        viewPager = (ViewPager)view.findViewById(R.id.guidePages);


        /**
         * 有几张图片下面就显示几个小圆点
         */
        pointRootViews = new RelativeLayout[shareViews.size()];
        // 设置每个小圆点距离左边的间距
        for (int i = 0; i < shareViews.size(); i++) {
            RelativeLayout pointRootView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.point_image_view,null);
            pointRootViews[i] = pointRootView;
            ImageView imageView = (ImageView) pointRootViews[i].findViewById(R.id.pointImage);
            if (i == 0) {
                // 默认选中第一张图片
                imageView.setImageResource(R.mipmap.point_check);
            } else {
                // 其他图片都设置未选中状态
                imageView.setImageResource(R.mipmap.point_uncheck);

            }

            llPointView.addView(pointRootView);
        }

        // 给viewpager设置适配器
        viewPager.setAdapter(new GuidePageAdapter());

        // 给viewpager设置监听事件
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());

    }


    // 指引页面更改事件监听器
    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) { }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) { }

        @Override
        public void onPageSelected(int arg0) {
            // 遍历数组让当前选中图片下的小圆点设置颜色
            for (int i = 0; i < pointRootViews.length; i++) {
                ImageView imageView = (ImageView) pointRootViews[arg0].findViewById(R.id.pointImage);
                imageView.setImageResource(R.mipmap.point_check);
                if (arg0 != i) {
                    ImageView others = (ImageView) pointRootViews[i].findViewById(R.id.pointImage);
                    others.setImageResource(R.mipmap.point_uncheck);

                }
            }

        }

    }



    class GuidePageAdapter extends PagerAdapter {
        @Override

        public int getCount() {

            return shareViews.size();

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager)arg0).removeView(shareViews.get(arg1));
        }
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager)arg0).addView(shareViews.get(arg1));
            return shareViews.get(arg1);
        }

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_layout:
                dismiss();
                break;
            case R.id.shareQQ:
                if (listener != null) {
                    listener.select(SHARE_MEDIA.QQ);
                }
                dismiss();
                break;
            case R.id.shareCopy:
                if (listener != null) {
                    listener.selectCopy();
                }
                dismiss();
                break;
            case R.id.shareWeixin:
                if (listener != null) {
                    listener.select(SHARE_MEDIA.WEIXIN);
                }
                dismiss();
                break;
            case R.id.shareWxfriend:
                if (listener != null) {
                    listener.select(SHARE_MEDIA.WEIXIN_CIRCLE);
                }
                dismiss();
                break;
            case R.id.shareQQZone:
                if (listener != null) {
                    listener.select(SHARE_MEDIA.QZONE);
                }
                dismiss();
                break;
            case R.id.shareSina:
                if (listener != null) {
                    listener.select(SHARE_MEDIA.SINA);
                }
                dismiss();
                break;
            case R.id.shareSms:
                if (listener != null) {
                    listener.select(SHARE_MEDIA.SMS);
                }
                dismiss();
                break;

            default:
                break;
        }
    }

    public static class SelectShareListener {
        public void select(SHARE_MEDIA platForm) {

        }

        public void selectCopy() {

        }
    }


    public SelectShareListener getListener() {
        return listener;
    }

    public void setListener(SelectShareListener listener) {
        this.listener = listener;
    }

}
