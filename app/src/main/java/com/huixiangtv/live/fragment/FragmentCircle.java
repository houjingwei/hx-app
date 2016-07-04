package com.huixiangtv.live.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.FriendCircleAdapter;
import com.huixiangtv.live.model.Cm;
import com.huixiangtv.live.model.Dynamic;
import com.huixiangtv.live.model.DynamicComment;
import com.huixiangtv.live.model.DynamicImage;
import com.huixiangtv.live.model.DynamicpPraise;
import com.huixiangtv.live.model.Images;
import com.huixiangtv.live.model.Praise;
import com.huixiangtv.live.ui.HuixiangLoadingLayout;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.ActionItem;
import com.huixiangtv.live.utils.widget.TitlePopup;
import com.huixiangtv.live.utils.widget.TitlePopup.OnItemOnClickListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentCircle extends Fragment implements OnItemOnClickListener {

    private final String PAGESIZE = "10";
    View mRootView;
    private PullToRefreshListView refreshView;
    private FriendCircleAdapter adapter;
    int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_circle, container, false);
        initView();
        return mRootView;
    }


    private TitlePopup titlePopup;

    private void initView() {
        refreshView = (PullToRefreshListView) mRootView.findViewById(R.id.refreshView);
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshView.setHeaderLayout(new HuixiangLoadingLayout(getActivity()));
        refreshView.setFooterLayout(new HuixiangLoadingLayout(getActivity()));
        titlePopup = new TitlePopup(getActivity(), CommonHelper.dip2px(getActivity(), 165), CommonHelper.dip2px(
                getActivity(), 40));
        titlePopup
                .addAction(new ActionItem(getActivity(), "评论", R.drawable.point_zan));
        titlePopup.addAction(new ActionItem(getActivity(), "赞",
                R.drawable.point_comm));


        titlePopup.setItemOnClickListener(this);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_circle_head, null, false);
        refreshView.getRefreshableView().addHeaderView(view);

        //点击进入自己的相册圈
        view.findViewById(R.id.ivPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(getActivity(), Constant.OWN_CIRCLE, null);
                }
            }
        });
        initHeadInfo(view);
        adapter = new FriendCircleAdapter(titlePopup, getActivity());
        refreshView.setAdapter(adapter);

        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        loadData();
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        loadData();
                    }
                }, 1000);

            }
        });
        loadData();

    }

    private void loadData() {

        bindDynamicInfo();

    }


    private void initHeadInfo(View view) {
        ImageView ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        TextView tvNickName = (TextView) view.findViewById(R.id.tvNickName);
        if (null != App.getLoginUser()) {
            tvNickName.setText(App.getLoginUser().getNickName());
            ImageUtils.displayAvator(ivPhoto, App.getLoginUser().getPhoto());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(ActionItem item, int position) {

        CommonHelper.showTip(getActivity(), item.mTitle.toString());
    }

    private List<Dynamic> getData(){

        List<Dynamic> dynamics = new ArrayList<>();

        Dynamic dynamic1 = new Dynamic();
        dynamic1.setCommentCount("5");
        DynamicComment cm = new DynamicComment();
        cm.setContent("hahahaha");
        cm.setNickName("szj:");

        DynamicComment cm1 = new DynamicComment();
        cm1.setContent("今天在干嘛？");
        cm1.setNickName("兰艳:");

        DynamicComment cm2 = new DynamicComment();
        cm2.setContent("？");
        cm2.setNickName("兰艳:");


        DynamicpPraise praise = new DynamicpPraise();
        praise.setPhoto("http://c.hiphotos.baidu.com/image/h%3D200/sign=b7453bef44166d222777129476220945/b3b7d0a20cf431ad2951ff1d4c36acaf2edd9817.jpg");


        DynamicpPraise praise1 = new DynamicpPraise();
        praise1.setPhoto("http://c.hiphotos.baidu.com/image/h%3D200/sign=b7453bef44166d222777129476220945/b3b7d0a20cf431ad2951ff1d4c36acaf2edd9817.jpg");

        List<DynamicpPraise> praiseList= new ArrayList<>();
        praiseList.add(praise); praiseList.add(praise1);praiseList.add(praise1);praiseList.add(praise1);praiseList.add(praise1);praiseList.add(praise1);praiseList.add(praise1);praiseList.add(praise1);praiseList.add(praise1);praiseList.add(praise1);
        dynamic1.setPraises(praiseList);

        List<DynamicImage> imagesList = new ArrayList<>();
        DynamicImage images = new DynamicImage();
        images.setBig("http://c.hiphotos.baidu.com/image/h%3D200/sign=b7453bef44166d222777129476220945/b3b7d0a20cf431ad2951ff1d4c36acaf2edd9817.jpg");
        images.setSmall("http://c.hiphotos.baidu.com/image/h%3D200/sign=b7453bef44166d222777129476220945/b3b7d0a20cf431ad2951ff1d4c36acaf2edd9817.jpg");
        imagesList.add(images);
        dynamic1.setImages(imagesList);


        List<DynamicComment> cmList= new ArrayList<>();
        cmList.add(cm);
        cmList.add(cm1);  cmList.add(cm2);
        dynamic1.setContent("xixixi");
        dynamic1.setComments(cmList);
        dynamic1.setNickName("ly");
        dynamic1.setPhoto("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=413574418,3633313838&fm=80");

        dynamics.add(dynamic1);



        Dynamic dynamic2 = new Dynamic();
        dynamic2.setCommentCount("5");
        Cm c3 = new Cm();
        c3.setContent("xxxxx");
        c3.setNickName("Andy:");

        Cm cm4 = new Cm();
        cm4.setContent("今天在干嘛？");
        cm4.setNickName("艳子:");

        Cm cm5 = new Cm();
        cm5.setContent("我回家了");
        cm5.setNickName("艳子:");






        List<DynamicComment> cmList1= new ArrayList<>();
        cmList1.add(cm);
        cmList1.add(cm1);  cmList1.add(cm2);
        dynamic2.setContent("xixixi");
        dynamic2.setComments(cmList);
        dynamic2.setNickName("Andy");
        dynamic2.setPhoto("http://c.hiphotos.baidu.com/image/h%3D200/sign=b7453bef44166d222777129476220945/b3b7d0a20cf431ad2951ff1d4c36acaf2edd9817.jpg");
        List<DynamicpPraise> praiseList1= new ArrayList<>();
        praiseList1.add(praise); praiseList1.add(praise1);praiseList1.add(praise1);
        dynamic2.setPraises(praiseList1);
        dynamics.add(dynamic2);


        return dynamics;

    }

    /**
     * @param
     */
    private void bindDynamicInfo() {

        adapter.clear();

        adapter.addList(getData());

        refreshView.onRefreshComplete();

//        Map<String, String> paramsMap = new HashMap<String, String>();
//        paramsMap.put("page", page + "");
//        paramsMap.put("pageSize", PAGESIZE);
//
//        RequestUtils.sendPostRequest(Api.DYNAMIC_OWNDYNAMIC, paramsMap, new ResponseCallBack<Dynamic>() {
//            @Override
//            public void onSuccessList(List<Dynamic> data) {
//                if (data != null && data.size() > 0) {
//                    if (page == 1) {
//                        adapter.clear();
//                        adapter.addList(data);
//                    } else {
//                        adapter.addList(data);
//                    }
//                } else {
//                    if (page == 1) {
//                        CommonHelper.noData("暂无动态", refreshView.getRefreshableView(), getActivity(), 2);
//                    }
//                }
//                refreshView.onRefreshComplete();
//            }
//
//            @Override
//            public void onFailure(ServiceException e) {
//                super.onFailure(e);
//                CommonHelper.showTip(getActivity(), "暂无动态:" + e.getMessage());
//                refreshView.onRefreshComplete();
//
//            }
//        }, Dynamic.class);
    }

}
