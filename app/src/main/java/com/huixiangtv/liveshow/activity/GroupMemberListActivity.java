package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.MemberAdapter;
import com.huixiangtv.liveshow.model.Member;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.ui.HuixiangLoadingLayout;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Stone on 16/7/18.
 */
public class GroupMemberListActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.refreshView)
    PullToRefreshListView refreshView;
    private MemberAdapter adapter;
    private int page = 1;
    private String pageSize = "5";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmember);
        x.view().inject(this);
        initView();
        initLayout();
        initData();
    }

    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.group_member));
    }


    protected void initLayout() {

        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshView.setHeaderLayout(new HuixiangLoadingLayout(this));
        refreshView.setFooterLayout(new HuixiangLoadingLayout(this));

        refreshView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
    }



    protected void initData() {
        adapter = new MemberAdapter(this);
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

        List<Member> ccc = new ArrayList<Member>();
        Member cs = new Member();
        cs.ivPhone="http://img0.imgtn.bdimg.com/it/u=1850159850,51447102&fm=21&gp=0.jpg";
        cs.gx="99855327";
        cs.title="大头儿子";


        Member cs1 = new Member();
        cs1.ivPhone="http://img0.imgtn.bdimg.com/it/u=1850159850,51447102&fm=21&gp=0.jpg";
        cs1.gx="5533667";
        cs1.title="小头爸爸";

        ccc.add(cs);
        ccc.add(cs1);

        if(page ==1)
        adapter.clear();

        adapter.addList(ccc);
        refreshView.setAdapter(adapter);
        refreshView.onRefreshComplete();


    }

}
