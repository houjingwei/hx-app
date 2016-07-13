package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.ui.CenterLoadingView;
import com.huixiangtv.liveshow.utils.DepthPageTransformer;
import com.huixiangtv.liveshow.utils.image.ImageUtils;
import com.huixiangtv.liveshow.utils.widget.WidgetUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BigStyleActivity extends AppCompatActivity {
    LinearLayout llTop ;
    private ViewPager mViewPager;
    private int[] mImgIds = new int[] { R.drawable.v1,R.drawable.v2 };
    private int[] mImgIds2 = new int[] { R.drawable.v4,R.drawable.v5 };
    private List<LinearLayout> mImageViews = new ArrayList<LinearLayout>();
    private PagerAdapter adapter;
    public boolean toNext = false;

    private boolean left = false;
    private boolean right = false;

    private int lastValue = -1;
    private int currIndex = 0;


    ImageView bg2;
    ImageView bg3;

    boolean knowBg2 = false;
    private String newImage;
    private String oldImage;


    private boolean isChange = false;
    private int oldIndex =0;

    CenterLoadingView loadingDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_big_style);
        llTop = (LinearLayout) findViewById(R.id.llTop);
        bg2 = (ImageView) findViewById(R.id.iv2);
        bg3 = (ImageView) findViewById(R.id.iv3);
        loadOneData();
    }


    List<Integer> offsetList = new ArrayList<Integer>();
    List<Integer> oldIndexList = new ArrayList<Integer>();
    private void loadOneData() {
        mViewPager = new ViewPager(BigStyleActivity.this);
        llTop.addView(mViewPager);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mViewPager.getLayoutParams();
        params.width = App.screenWidth;
        params.height = WidgetUtil.dip2px(BigStyleActivity.this,350);
        mViewPager.setLayoutParams(params);
        final DepthPageTransformer mPageTransformer = new DepthPageTransformer();
        mViewPager.setPageTransformer(true, mPageTransformer);

        adapter = new myAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                int flag = ev.getAction();
                switch (flag){
                    case MotionEvent.ACTION_DOWN:
                        oldIndex = mViewPager.getCurrentItem();

                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                }
                return false;
            }
        });

        mViewPager.addOnPageChangeListener(changeListener);
        initData(mImgIds);
    }


    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.i("offset",positionOffsetPixels+"***"+position);

            offsetList.add(positionOffsetPixels);
            oldIndexList.add(position);
            if(offsetList.size()>2){
                int num1 = offsetList.get(0);
                int num2 = offsetList.get(1);

                if(isChange){
                    if(knowBg2) {
                        if(num1>num2){
                            twoToLeftAlpha(positionOffset/4);
                        }else if(num1<num2){
                            twoToRightAlpha(positionOffset/4);
                        }
                    }
                }else{
                    if(knowBg2) {

                        if(num1>num2){

                            oneToLeftAlpha(positionOffset/4);
                        }else if(num1<num2){

                            oneToRightAlpha(positionOffset/4);
                        }
                    }
                }

            }


            if(offsetList.size()>3){
                if(!knowBg2){
                    knowBg2 = true;
                    if(offsetList.get(offsetList.size()-2)>offsetList.get(offsetList.size()-3)){
                        //向右
                        newImage= "http://img3.imgtn.bdimg.com/it/u=1246108322,772622287&fm=21&gp=0.jpg";
                    }else if(offsetList.get(offsetList.size()-2)<offsetList.get(offsetList.size()-3)){
                        //向左
                        newImage = "http://i3.sinaimg.cn/hs/images/chenshu/08qiyeban/xiamen/dwj/002.jpg";
                    }
                    if(isChange){
                        ImageUtils.display(bg3,newImage);
                    }else{

                        ImageUtils.display(bg2,newImage);
                    }

                }

                lastValue = offsetList.get(offsetList.size()-2);
            }else{
                lastValue = -1;
            }
        }

        @Override
        public void onPageSelected(int position) {
            currIndex = position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

            if(state==0){
                knowBg2 = false;
                if(offsetList.size()>3){
                    if(offsetList.get(offsetList.size()-2)>offsetList.get(offsetList.size()-3)){
                        right = true;
                        left = false;
                    }else if(offsetList.get(offsetList.size()-2)<offsetList.get(offsetList.size()-3)){
                        right = false;
                        left = true;
                    }else if(currIndex==adapter.getCount()-1 && offsetList.get(offsetList.size()-2)==0){
                        left = false;
                        right = true;
                    }else if(currIndex==0 && offsetList.get(offsetList.size()-2)==0){
                        left = true;
                        right = false;
                    }
                    lastValue = offsetList.get(offsetList.size()-2);
                }else{
                    lastValue = -1;
                }
                if(lastValue==0 && currIndex==adapter.getCount()-1 && right){
                    Log.i("aaaaaa","去加载下一页");
                }else if(lastValue==0 && currIndex==0 && left){
                    Log.i("aaaaaa","重新加载加载");
                }
                offsetList.clear();

                if(oldIndex!=currIndex){
                    isChange =!isChange;
                    oldImage = newImage;
                }else{
                    if(isChange){
                        bg2.setAlpha(0.25f);
                        bg3.setAlpha(0.0f);
                    }else{
                        bg2.setAlpha(0.0f);
                        bg3.setAlpha(0.25f);
                    }
                }

            }
        }
    };


    private void oneToRightAlpha(float positionOffset) {
        if(positionOffset!=0.0) {
            float ff = formartAlpha(positionOffset);
            bg2.setAlpha(ff);
            bg3.setAlpha(0.25f-ff);
        }
    }

    private void twoToRightAlpha(float positionOffset) {
        if(positionOffset!=0.0) {
            float ff = formartAlpha(positionOffset);
            Log.i("bg2 alpha 减至 ",(0.25f-ff)+"***");
            bg2.setAlpha(0.25f-ff);
            bg3.setAlpha(ff);
        }
    }

    private void oneToLeftAlpha(float positionOffset) {
        if(positionOffset!=0.0) {
            float ff = formartAlpha(positionOffset);
            bg2.setAlpha(0.25f - ff);
            bg3.setAlpha(ff);
        }

    }





    private void twoToLeftAlpha(float positionOffset) {
        if(positionOffset!=0.0) {
            float ff = formartAlpha(positionOffset);
            bg2.setAlpha(ff);
            bg3.setAlpha(0.25f-ff);
        }

    }

    private float formartAlpha(float positionOffset) {
        DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p=decimalFormat.format(positionOffset);//format 返回的是字符串
        return Float.parseFloat(p);
    }


    private void initData(int[] mis)
    {
        if(null==loadingDialog){
            loadingDialog = new CenterLoadingView(BigStyleActivity.this);
        }
        loadingDialog.setCancelable(true);
        loadingDialog.setTitle("正在连接");
        loadingDialog.show();

        bg2.setAlpha(0.0f);


        bg3.setAlpha(0.25f);
        ImageUtils.display(bg3,"http://dl.bizhi.sogou.com/images/2012/03/07/254921.jpg");
        for (int imgId :mis)
        {
            LinearLayout view = (LinearLayout)LayoutInflater.from(BigStyleActivity.this).inflate(R.layout.view_pager_content_view, null, false);
            LinearLayout lLayout = (LinearLayout) view.findViewById(R.id.llRoot);
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(imgId);
            lLayout.addView(imageView);
            mImageViews.add(view);
        }
        adapter.notifyDataSetChanged();


        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(null!=loadingDialog){
                    loadingDialog.dismiss();
                }
            }
        }, 3000);
    }








    public class myAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object obj)
        {

            container.removeView((LinearLayout)obj);
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public int getCount()
        {
            return mImgIds.length;
        }
    }


}
