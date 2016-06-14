package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.MyFansAdapter;
import com.huixiangtv.live.model.Fans;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.CommonHelper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttentionMeActivity extends BaseBackActivity   {


    @ViewInject(R.id.tv_list_empty)
    TextView tv_list_empty;

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    PtrClassicFrameLayout ptrClassicFrameLayout;
    ListView mListView;



    int page = 1;


    MyFansAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_me);
        x.view().inject(this);
        initview();


    }

    private void initview() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.myfans));

        adapter = new MyFansAdapter(this);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) this.findViewById(R.id.test_list_view_frame);
        mListView = (ListView) this.findViewById(R.id.test_list_view);
        mListView.setAdapter(adapter);
        loadMyFans();
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                loadMyFans();
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                loadMyFans();
            }

        });




    }

    private void loadMyFans(){

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", page + "");
        paramsMap.put("pageSize", "10");
        RequestUtils.sendPostRequest(Api.GETOWNFANS, paramsMap, new ResponseCallBack<Fans>() {
            @Override
            public void onSuccessList(List<Fans> data) {

                if (data != null && data.size() > 0) {
                    if(page==1){
                        adapter.clear();
                    }
                    adapter.addList(data);
                }else{
                    if (page == 1) {
                        CommonHelper.noData("还没有粉丝哦",mListView,AttentionMeActivity.this);
                    }
                }

                ptrClassicFrameLayout.loadComplete(true);

            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(AttentionMeActivity.this, e.getMessage());
                ptrClassicFrameLayout.loadComplete(false);
            }
        }, Fans.class);
    }

}