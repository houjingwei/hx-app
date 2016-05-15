package com.huixiang.live.utils.widget;

import android.support.v4.view.PagerAdapter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.google.gson.annotations.SerializedName;
import com.huixiang.live.model.PositionAdvertBO;
import com.huixiang.live.utils.BitmapHelper;
import com.huixiang.live.utils.BitmapHelper.DefaultSize;
import com.huixiang.live.utils.BitmapHelper.ImageSize;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.ImageView.ScaleType;
import com.huixiang.live.R;

/**
 * Created by apple on 16/5/13.
 */
public class BannerView extends FrameLayout implements Runnable {
    public final int FAKE_BANNER_SIZE = 100;

    private List<PositionAdvertBO> positionAdvertBO;
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

    public void setPositionAdvertBO(List<PositionAdvertBO> PositionAdvertBO) {
        this.positionAdvertBO = PositionAdvertBO;
        bannerShow(context);
    }

    private void bannerShow(final Context context) {
        viewPager = (PictureViewPager) findViewById(R.id.pic_vp);
        final LinearLayout index_ll = (LinearLayout) findViewById(R.id.index_ll);
        viewPager.removeAllViewsInLayout();
        viewPager.removeAllViews();

        // 记清理掉之前数据，避免反复加载造成点数无限增多
        index_ll.removeAllViews();
        if (positionAdvertBO != null) {
            for (int i = 0; i < positionAdvertBO.size(); i++) {
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

        guideImgViewPagerAdapter = new GuideImgViewPagerAdapter(context, viewPager, positionAdvertBO);
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

        private List<PositionAdvertBO> listPositionAdvertBO;

        public GuideImgViewPagerAdapter(Context context, ViewPager viewPager, List<PositionAdvertBO> PositionAdvertBO) {
            mInflater = LayoutInflater.from(context);
            mViewPager = viewPager;
            mContext = context;

            refreshData(PositionAdvertBO);
        }

        public void refreshData(List<PositionAdvertBO> PositionAdvertBO) {
            if (PositionAdvertBO != null) {
                this.listPositionAdvertBO = PositionAdvertBO;
                mBannerCount = listPositionAdvertBO.size();
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
            final PositionAdvertBO ps = listPositionAdvertBO.get(position);
            BitmapHelper.getInstance(mContext).display(imageView, ps.getAdImgPath(), ImageSize.BANNER, DefaultSize.BIG);

            final int pos = position;

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(mContext,"doing", Toast.LENGTH_LONG).show();

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

}