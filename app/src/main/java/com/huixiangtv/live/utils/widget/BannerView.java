package com.huixiangtv.live.utils.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.WebActivity;
import com.huixiangtv.live.model.BannerModel;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.ImageUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by apple on 16/5/13.
 */
public class BannerView extends FrameLayout implements Runnable {
    public final int FAKE_BANNER_SIZE = 100;

    private List<BannerModel> bannerModel;
    private GuideImgViewPagerAdapter guideImgViewPagerAdapter;
    private Context context;
    private PictureViewPager viewPager;
    private Handler mHandler;
    private int mBannerPosition = 0;

    private boolean mIsUserTouched = false;
    private Timer mTimer;

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (!mIsUserTouched) {
                mBannerPosition = (mBannerPosition + 1) % FAKE_BANNER_SIZE;
                mHandler.post(BannerView.this);
            }
        }
    };

    @Override
    public void run() {
        if (viewPager != null) {
            viewPager.setCurrentItem(mBannerPosition);
        }
    }

    public BannerView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public BannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(final Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.banner_view, this);

        mHandler = new Handler(Looper.getMainLooper());
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTimer();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();
    }

    private void startTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }

        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 5000, 5000);
    }

    private void stopTimer() {
        mTimer.cancel();
        mTimer = null;
    }

    public void setPositionAdvertBO(List<BannerModel> bannerModel) {
        this.bannerModel = bannerModel;
        bannerShow(context);
    }

    private void bannerShow(final Context context) {
        viewPager = (PictureViewPager) findViewById(R.id.pic_vp);
        final LinearLayout index_ll = (LinearLayout) findViewById(R.id.index_ll);
        viewPager.removeAllViewsInLayout();
        viewPager.removeAllViews();

        // 记清理掉之前数据，避免反复加载造成点数无限增多
        index_ll.removeAllViews();
        if (bannerModel != null) {
            for (int i = 0; i < bannerModel.size(); i++) {
                ImageView iv = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT); // , 1是可选写的
                lp.setMargins(8, 0, 8, 0);
                iv.setLayoutParams(lp);
                if (i == 0) {
                    iv.setBackgroundResource(R.drawable.home_circle_sel);
                } else {
                    iv.setBackgroundResource(R.drawable.home_circle_unsel);
                }
                index_ll.addView(iv);
            }
        }

        guideImgViewPagerAdapter = new GuideImgViewPagerAdapter(context, viewPager, bannerModel);
        viewPager.setAdapter(guideImgViewPagerAdapter);

        // 切换监听
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int pos) {
                mBannerPosition = pos;
                int childCount = index_ll.getChildCount();
                pos %= childCount;
                for (int i = 0; i < childCount; i++) {
                    View view = index_ll.getChildAt(i);
                    if (i == pos) {
                        view.setBackgroundResource(R.drawable.home_circle_sel);
                    } else {
                        view.setBackgroundResource(R.drawable.home_circle_unsel);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN
                        || action == MotionEvent.ACTION_MOVE) {
                    mIsUserTouched = true;
                } else if (action == MotionEvent.ACTION_UP) {
                    mIsUserTouched = false;
                }
                return false;
            }
        });

        viewPager.setFocusable(true);
        viewPager.setFocusableInTouchMode(true);
        viewPager.requestFocus();
    }

    class GuideImgViewPagerAdapter extends PagerAdapter {
        private LayoutInflater mInflater;
        private ViewPager mViewPager;
        private Context mContext;

        private int mBannerCount = 1;

        private List<BannerModel> listbannerModel;

        public GuideImgViewPagerAdapter(Context context, ViewPager viewPager, List<BannerModel> bannerModel) {
            mInflater = LayoutInflater.from(context);
            mViewPager = viewPager;
            mContext = context;

            refreshData(bannerModel);
        }

        public void refreshData(List<BannerModel> bannerModel) {
            if (bannerModel != null) {
                this.listbannerModel = bannerModel;
                mBannerCount = listbannerModel.size();
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return FAKE_BANNER_SIZE;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= mBannerCount;

            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(720, 360));
            imageView.setScaleType(ScaleType.FIT_XY);
            final BannerModel ps = listbannerModel.get(position);
            ImageUtils.display(imageView,ps.image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoWebActivity(context,ps.target);
                }
            });
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            int position = mViewPager.getCurrentItem();
            if (position == 0) {
                position = mBannerCount;
                mViewPager.setCurrentItem(position, false);
            } else if (position == FAKE_BANNER_SIZE - 1) {
                position = mBannerCount - 1;
                mViewPager.setCurrentItem(position, false);
            }
        }
    }


    private void gotoWebActivity(Context mContext,String url)
    {
        Map<String,String> param = new HashMap<>();
        param.put("url",url);
        ForwardUtils.target((Activity)mContext, Constant.BINNER_URL, param);
    }
}