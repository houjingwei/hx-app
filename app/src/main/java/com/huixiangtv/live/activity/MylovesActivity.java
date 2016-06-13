package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.MyLovesAdapter;
import com.huixiangtv.live.model.Love;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.StringUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MylovesActivity extends BaseBackActivity {

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;
    PtrClassicFrameLayout ptrClassicFrameLayout;
    ListView mListView;



    int page = 1;
    MyLovesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myloves);
        x.view().inject(this);
        initview();
    }

    private void initview() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.myLoves));

        adapter = new MyLovesAdapter(this);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) this.findViewById(R.id.test_list_view_frame);
        mListView = (ListView) this.findViewById(R.id.test_list_view);
        mListView.setAdapter(adapter);
        View view = LayoutInflater.from(MylovesActivity.this).inflate(R.layout.activity_myloves_head, null, false);
        mListView.addHeaderView(view);

        TextView tvMyLoves = (TextView) view.findViewById(R.id.tvMyLoves);
        if(null!=App.getLoginUser()) {
            tvMyLoves.setText(StringUtil.isNotEmpty(App.getLoginUser().getLoves())?App.getLoginUser().getLoves():0+"个");
        }else{
            tvMyLoves.setText("0个");
        }

        loadData();
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                loadData();
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                loadData();
            }

        });

    }

    private void loadData() {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", page + "");
        paramsMap.put("pageSize", "10");
        RequestUtils.sendPostRequest(Api.LOVE_DETAIL, paramsMap, new ResponseCallBack<Love>() {
            @Override
            public void onSuccessList(List<Love> data) {

                if (data != null && data.size() > 0) {
                    if(page==1){
                        adapter.clear();
                    }
                    adapter.addList(data);
                }
                ptrClassicFrameLayout.loadComplete(true);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(MylovesActivity.this, e.getMessage());
                ptrClassicFrameLayout.loadComplete(false);
            }
        }, Love.class);
    }


}
