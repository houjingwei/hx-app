package com.huixiang.live.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.huixiang.live.utils.widget.LinearLayoutForListView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huixiang.live.App;
import com.huixiang.live.R;
import com.huixiang.live.activity.MainActivity;
import com.huixiang.live.adapter.CommonAdapter;
import com.huixiang.live.adapter.ViewHolder;
import com.huixiang.live.model.CommonModel;
import com.huixiang.live.model.PositionAdvertBO;
import com.huixiang.live.utils.widget.BannerView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


public class FragmentTabOne extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,  View.OnClickListener {


    private BannerView bannerView;
    private BGARefreshLayout mRefreshLayout;
    private List<PositionAdvertBO> guangGao = new ArrayList<PositionAdvertBO>();
    private View mRootView;
    MainActivity activity;
    private BaseAdapter adapter;
    private List<CommonModel> commonModelList = new ArrayList<CommonModel>();
    private LinearLayoutForListView listview;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tab_one, container, false);
        activity = (MainActivity) getActivity();
        activity.setTitleBar(getString(R.string.today_rm));
        activity.hideTitle(false);

        findView();
        BindBinnerData();
        initAdapter();
        setListener();
        processLogic(savedInstanceState);
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    /**
     * loading and init list
     */
    private void initAdapter() {
        mRefreshLayout.setPullDownRefreshEnable(true);
        mRefreshLayout.endRefreshing();
        mRefreshLayout.endLoadingMore();
        int img_arr[] ={R.drawable.test_pic1,R.drawable.test_pic2,R.drawable.test_pic3,R.drawable.test_pic4,R.drawable.test_pic5};
        //模拟数据
        commonModelList.clear();
        for (int i = 0; i < 5; i++) {
            CommonModel commonModel1 = new CommonModel();
            //commonModel1.iconUrl = "http://f1.jgyes.com/4,013f52a5e0fd91";
            commonModel1.title = "超级女生2016排位赛"+i;
            commonModel1.setTime("5月30日 17：5"+i);
            commonModel1.img_id = img_arr[i];
            commonModelList.add(commonModel1);
        }
        adapter = new TabOneAdapter(getContext(), commonModelList, R.layout.index_list_pic);

        listview.setAdapter(adapter);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }


    private class TabOneAdapter extends CommonAdapter<CommonModel> {

        public TabOneAdapter(Context context, List<CommonModel> listData, int itemLayoutId) {
            super(context, listData, itemLayoutId);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void convert(ViewHolder helper, int position, CommonModel item) {

            TextView tvTitle = helper.getView(R.id.tvTitle);
            TextView tvTime = helper.getView(R.id.tvTime);
            tvTitle.setText(item.title);
            tvTime.setText(item.getTime());
            ImageView ivIcon = helper.getView(R.id.ivIcon);
            //BitmapHelper.getInstance(mContext).display(ivIcon, item.iconUrl, "" , BitmapHelper.DefaultSize.BIG);

            ivIcon.setBackground(getResources().getDrawable(item.img_id));
        }
    }


    private void BindBinnerData() {

        PositionAdvertBO positionAdvertBO = new PositionAdvertBO();
        positionAdvertBO.setAdImgPath("http://f1.jgyes.com/4,013f52a5e0fd91");
        PositionAdvertBO positionAdvertBO1 = new PositionAdvertBO();
        positionAdvertBO1.setAdImgPath("http://img3.imgtn.bdimg.com/it/u=1206514979,2546214886&fm=21&gp=0.jpg");
        PositionAdvertBO positionAdvertBO2 = new PositionAdvertBO();
        positionAdvertBO2.setAdImgPath("http://f1.jgyes.com/3,013e1fcbd9d368");


        List<PositionAdvertBO> positionAdvertBOList = new ArrayList<PositionAdvertBO>();
        positionAdvertBOList.add(positionAdvertBO);
        positionAdvertBOList.add(positionAdvertBO1);
        positionAdvertBOList.add(positionAdvertBO2);
        guangGao.clear();
        guangGao.addAll(positionAdvertBOList);
        bannerView.setPositionAdvertBO(guangGao);

    }

    private void findView() {
        bannerView = (BannerView) mRootView.findViewById(R.id.banner);
        mRefreshLayout = (BGARefreshLayout) mRootView.findViewById(R.id.mRefreshLayout);
        listview = (LinearLayoutForListView) mRootView.findViewById(R.id.listview);
        listview.setOnItemClickListener(new LinearLayoutForListView.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, Object item, int position) {
                Toast.makeText(getActivity(),"gotoLive",Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        //adapter.clearData();
        mRootView.findViewById(R.id.ll_search).setVisibility(View.VISIBLE);
        initAdapter();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        initAdapter();
        return false;
    }

    public void beginRefreshing() {
        mRefreshLayout.beginRefreshing();
    }

    public void beginLoadingMore() {
        mRefreshLayout.beginLoadingMore();
    }


    @Override
    public void onClick(View v) {

    }

    public void processLogic(Bundle savedInstanceState) {
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getContext(), true);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(App.getContext(), true));
        moocStyleRefreshViewHolder.setSpringDistanceScale(0.2f);
       // mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

    }

    public void setListener() {
        mRefreshLayout.setDelegate(FragmentTabOne.this);
        mRefreshLayout.setIsShowLoadingMoreView(true);
    }

}
