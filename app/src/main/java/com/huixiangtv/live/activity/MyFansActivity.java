package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.MyFansAdapter;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.EnumUpdateTag;
import com.huixiangtv.live.utils.image.ImageUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFansActivity extends BaseBackActivity {

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    PullToRefreshScrollView mRefreshLayout;


    ListView listView;
    int page = 1;

    List<Fans> fansList ;
    MyFansAdapter adapter;


    ImageView ivPhoto1;
    ImageView ivPhoto2;
    ImageView ivPhoto3;
    TextView tvNickName1;
    TextView tvNickName2;
    TextView tvNickName3;
    TextView tvHot1;
    TextView tvHot2;
    TextView tvHot3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans);
        x.view().inject(this);
        initview();
    }

    private void initview() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.myfans));
        mRefreshLayout = (PullToRefreshScrollView) findViewById(R.id.refreshLayout);
        mRefreshLayout.setMode(PullToRefreshBase.Mode.BOTH);
        listView = (ListView) findViewById(R.id.listview);

        ivPhoto1 = (ImageView) findViewById(R.id.ivPhoto1);
        ivPhoto2 = (ImageView) findViewById(R.id.ivPhoto2);
        ivPhoto3 = (ImageView) findViewById(R.id.ivPhoto3);
        tvNickName1 = (TextView) findViewById(R.id.tvNickName1);
        tvNickName2 = (TextView) findViewById(R.id.tvNickName2);
        tvNickName3 = (TextView) findViewById(R.id.tvNickName3);
        tvHot1 = (TextView) findViewById(R.id.tvHot1);
        tvHot2 = (TextView) findViewById(R.id.tvHot2);
        tvHot3 = (TextView) findViewById(R.id.tvHot3);

        mRefreshLayout.setMode(PullToRefreshBase.Mode.BOTH);

        mRefreshLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                loadFanedMe(EnumUpdateTag.UPDATE);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                loadFanedMe(EnumUpdateTag.MORE);
            }
        });

        loadFanedMe(EnumUpdateTag.UPDATE);

    }


    private void loadData() {
//        Map<String,String> params  = new HashMap<String,String>();
//        params.put("page", page+"");
//        params.put("pageSize", Constant.PAGE_SIZE);
//        RequestUtils.sendPostRequest(Api.MY_FANS, params, new ResponseCallBack<Fans>() {
//            @Override
//            public void onSuccessList(List<Fans> data) {
//                super.onSuccessList(data);
//
//            }
//
//            @Override
//            public void onFailure(ServiceException e) {
//                super.onFailure(e);
//            }
//        },Fans.class);


        loadFanedMe(EnumUpdateTag.UPDATE);
    }

    private void setTop3AndFanslist() {
        List<Fans> top3 = null;
        if(null!=fansList && fansList.size()>3){
            top3 = fansList.subList(0,3);
            fansList = fansList.subList(3,fansList.size());
        }else{
            top3 = fansList;
            fansList = null;
        }
        if(null!=top3){
            for (int i=0;i<top3.size();i++){
                Fans fans = top3.get(i);
                if(i==0){
                    ImageUtils.displayAvator(ivPhoto1,fans.getPhoto());
                    tvNickName1.setText(fans.getNickName());
                    tvHot1.setText(fans.getHots());
                }else if(i==1){
                    ImageUtils.displayAvator(ivPhoto2,fans.getPhoto());
                    tvNickName2.setText(fans.getNickName());
                    tvHot2.setText(fans.getHots());
                }else if(i==2){
                    ImageUtils.displayAvator(ivPhoto3,fans.getPhoto());
                    tvNickName3.setText(fans.getNickName());
                    tvHot3.setText(fans.getHots());
                }
            }
        }
    }


    int totalNum = 0;
    private void addData(List<Fans> fansList) {
        for (Fans fans : fansList) {

            View view = LayoutInflater.from(MyFansActivity.this).inflate(R.layout.fans_item, null, false);
            TextView tvRank = (TextView) view.findViewById(R.id.tvRank);
            TextView tvNickName = (TextView) view.findViewById(R.id.tvNickName);
            TextView tvHots = (TextView) view.findViewById(R.id.tvHots);
            ImageView ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
            tvRank.setText(totalNum+4+"");
            tvHots.setText(fans.getHots());
            tvNickName.setText(fans.getNickName());
            ImageUtils.displayAvator(ivPhoto,fans.getPhoto());
            listView.addView(view);
            totalNum+=1;
        }
    }


    public List<Fans> getData() {
        List<Fans> ls = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open("myFans.json"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            ls = JSON.parseArray(stringBuilder.toString(),Fans.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ls;
    }


    private void loadFanedMe(final EnumUpdateTag enumUpdateTag){

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", page + "");
        paramsMap.put("pageSize", "120");


        RequestUtils.sendPostRequest(Api.GETOWNFANS, paramsMap, new ResponseCallBack<Fans>() {
            @Override
            public void onSuccessList(List<Fans> data) {

                if (data != null && data.size() > 0) {

                    fansList = data;
                    if(page ==1 )
                    {
                       // setTop3AndFanslist();

                    }

                    if (enumUpdateTag == EnumUpdateTag.UPDATE) {

                        listView.removeAllViews();
                    }
                    Long totalCount = Long.parseLong(data.size() + "");
                    if (0 == totalCount) {
                        Toast.makeText(MyFansActivity.this, "已经没有更多内容了", Toast.LENGTH_LONG).show();
                    } else {
                        adapter = new MyFansAdapter();
                        listView.setAdapter(adapter);
                    }
                } else {
                }
                mRefreshLayout.onRefreshComplete();
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                mRefreshLayout.onRefreshComplete();
                CommonHelper.showTip(MyFansActivity.this, e.getMessage());
            }
        }, Fans.class);
    }
}
