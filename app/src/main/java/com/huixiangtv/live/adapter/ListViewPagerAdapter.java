package com.huixiangtv.live.adapter;


import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.utils.widget.CustomIndicator;

/**
 * Created by Stone on 16/5/24.
 */
public class ListViewPagerAdapter extends PagerAdapter implements OnItemClickListener{
    Context context;

    public static List<View> mListViewPager = new ArrayList<View>(); // ViewPager对象的内容
    public static List<Live> list;
    public static List<List<Live>> lcontant = null;
    int pageNum = 1;
    CustomIndicator mCustomIndicator;
    int pageRows=1;

    /**
     *
     * @param context 活动窗体
     * @param kf 数据
     * @param rows 每页显示多少条数据
     */
    public ListViewPagerAdapter(final Context context, List<Live> kf,int rows) {
        //this.mCustomIndicator=customIndicator;
        this.pageRows=rows;
        int count = 0;  //循环次数
        int pos = 0;		//当前位置

        this.context = context;
        list = new ArrayList<Live>();
        this.list = kf;
        //计算页数
        pageNum = (int) Math.ceil(list.size() / pageRows);
        int a=list.size() % pageRows;
        if (a>0) {
            pageNum=pageNum+1;
        }
        Log.d("hx2", String.valueOf(pageNum));
        if (Math.ceil(kf.size() / pageRows) == 0) {
            pageNum = 1;
        }
        lcontant = new ArrayList<List<Live>>();
        for (int i = 0; i < pageNum; i++) {
            Log.d("hx2", String.valueOf(i));
            List<Live> item = new ArrayList<Live>();
            for(int k = pos;k<kf.size();k++){
                count++;
                pos = k;
                item.add(kf.get(k));
                //每个List六条记录，存满N个跳出
                if(count == pageRows){
                    count = 0;
                    pos = pos+1;
                    break;
                }
            }
            lcontant.add(item);
        }

        for (int j = 0; j < pageNum; j++) {
            View viewPager = LayoutInflater.from(context).inflate(
                    R.layout.list, null);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)viewPager.getLayoutParams();
//        params.height = (int) (App.screenHeight);
//            viewPager.setLayoutParams(params);
            ListView mList = (ListView) viewPager.findViewById(R.id.view_list);
            final LiveBannerAdapter myadapter=new LiveBannerAdapter(context, lcontant.get(j));
            mList.setAdapter(myadapter);

            mList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String aaa = "";
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    String aaa = "";
                }
            });

            mList.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    String aaa = "";
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    String aaa = "";
                }
            });


//            mList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                    String sss= "";
//                }
//            });



            mListViewPager.add(viewPager);
        }

    }





    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return mListViewPager.size();
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(mListViewPager.get(position));
        return mListViewPager.get(position);

    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }

    @Override
    public void destroyItem(View container, int position, Object arg2) {
        ViewPager pViewPager = ((ViewPager) container);
        pViewPager.removeView(mListViewPager.get(position));
    }

    @Override
    public void finishUpdate(View arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

    }




}