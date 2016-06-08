package com.huixiangtv.live.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.MyFansAdapter;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.ui.CommonTitle;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.EnumUpdateTag;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stone on 16/6/8.
 */
public class PopularityActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    int page = 1;


    private PullToRefreshScrollView mPullToRefreshScrollView;
    private ListView mDataLv;

    MyFansAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faned_me);
        x.view().inject(this);
        initview();


    }

    private void initview() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.myconcern));

        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshLayout);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mDataLv = (ListView) findViewById(R.id.data);
        View view = LayoutInflater.from(PopularityActivity.this).inflate(R.layout.search_view, null, false);
        //mDataLv.addHeaderView(view);
        adapter = new MyFansAdapter(this);
        mDataLv.setAdapter(adapter);

        mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                loadFanedMe(EnumUpdateTag.UPDATE);
                mPullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                loadFanedMe(EnumUpdateTag.MORE);
                mPullToRefreshScrollView.onRefreshComplete();
            }
        });
        loadFanedMe(EnumUpdateTag.UPDATE);
    }


    private void loadFanedMe(final EnumUpdateTag enumUpdateTag){

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", page + "");
        paramsMap.put("pageSize", "120");


        RequestUtils.sendPostRequest(Api.GETPOPULARITYRANK, paramsMap, new ResponseCallBack<Fans>() {
            @Override
            public void onSuccessList(List<Fans> data) {

                if (data != null && data.size() > 0) {
                    if (enumUpdateTag == EnumUpdateTag.UPDATE) {

                        mDataLv.removeAllViews();
                    }
                    Long totalCount = Long.parseLong(data.size() + "");
                    if (0 == totalCount) {
                        Toast.makeText(PopularityActivity.this, "已经没有更多内容了", Toast.LENGTH_LONG).show();
                    } else {
                        adapter = new MyFansAdapter();
                        mDataLv.setAdapter(adapter);
                    }
                } else {
                }
                mPullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                mPullToRefreshScrollView.onRefreshComplete();
                CommonHelper.showTip(PopularityActivity.this, e.getMessage());
            }
        }, Fans.class);
    }


}