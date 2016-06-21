package com.huixiangtv.live.adapter;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.model.Live;

/**
 * Created by Stone on 16/6/20.
 */
public class ListViewPagerAdapter extends PagerAdapter implements OnItemClickListener{
    Context context;
    Activity activity;

    public static List<View> mListViewPager = new ArrayList<View>(); // ViewPager对象的内容
    public static List<Live> list = new ArrayList<Live>();
    public static List<List<Live>> lcontant = null;
    public static List<List<Live>> lcontants = null;
    int pageNum = 1;
    int pageRows=5;

    public ListViewPagerAdapter(final Activity activity,final Context context, List<Live> kf,int rows) {
        //this.mCustomIndicator=customIndicator;
        this.pageRows=rows;
        int count = 0;
        int pos = 0;

        this.context = context;
        this.activity = activity;
        this.list = kf;
        pageNum = (int) Math.ceil(list.size() / pageRows);
        int a=list.size() % pageRows;
        if (a>0) {
            pageNum=pageNum+1;
        }

        lcontant = new ArrayList<List<Live>>();
        lcontants = new ArrayList<List<Live>>();
        for (int i = 0; i < pageNum; i++) {
            Log.d("hx2", String.valueOf(i));
            List<Live> item = new ArrayList<Live>();
            for(int k = pos;k<kf.size();k++){
                count++;
                pos = k;
                item.add(kf.get(k));
                if(count == pageRows){
                    count = 0;
                    pos = pos+1;
                    break;
                }
            }

            lcontant.add(item);
            lcontants.add(item);
        }

        for (int j = 0; j < pageNum; j++) {
            View viewPager = LayoutInflater.from(context).inflate(
                    R.layout.list, null);
            ListView mList = (ListView) viewPager.findViewById(R.id.view_list);
            final LiveBannerAdapter myadapter=new LiveBannerAdapter(activity,context, lcontant.get(j),1);
            mList.setAdapter(myadapter);
            mListViewPager.add(viewPager);
            mList.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {



                }
            });
        }

    }


    public void ListViewPagerAdapterLoadMore(final Context context, List<Live> kf,int rows) {
        //this.mCustomIndicator=customIndicator;
        this.pageRows=rows;
        int count = 0;  //循环次数
        int pos = 0;		//当前位置

        this.context = context;
//        this.list.addAll(kf);
        this.list =  kf;
        //计算页数
        pageNum = (int) Math.ceil(list.size() / pageRows);
        int a=list.size() % pageRows;
        if (a>0) {
            pageNum=pageNum+1;
        }
        lcontants = new ArrayList<List<Live>>();
        for (int i = 0; i < pageNum; i++) {
            List<Live> item = new ArrayList<Live>();
            for(int k = pos;k<kf.size();k++){
                count++;
                pos = k;
                item.add(kf.get(k));
                if(count == 1){
                    count = 0;
                    pos = pos+1;
                    break;
                }
            }

            lcontant.add(item);
            lcontants.add(item);
        }

        for (int j = 0; j < pageNum; j++) {
            View viewPager = LayoutInflater.from(context).inflate(
                    R.layout.list, null);
            ListView mList = (ListView) viewPager.findViewById(R.id.view_list);
            final LiveBannerAdapter myadapter=new LiveBannerAdapter(activity,context, lcontants.get(j),1);
            mList.setAdapter(myadapter);
            mListViewPager.add(viewPager);
            mList.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                }
            });
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