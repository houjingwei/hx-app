package com.huixiangtv.liveshow.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.huixiangtv.liveshow.Api;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.NewApplyFriendAdapter;
import com.huixiangtv.liveshow.adapter.NewApplyGroupdAdapter;
import com.huixiangtv.liveshow.model.Friend;
import com.huixiangtv.liveshow.service.RequestUtils;
import com.huixiangtv.liveshow.service.ResponseCallBack;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;
import com.huixiangtv.liveshow.utils.widget.WidgetUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewApplyGroupActivity extends AppCompatActivity {
    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.listView)
    private SwipeMenuListView mListView;

    NewApplyGroupdAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_apply_group);
        x.view().inject(this);
        initView();
        loadData();
    }

    String page = "1";
    private void loadData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page",page);
        params.put("pageSize","10000");
        RequestUtils.sendPostRequest(Api.APPLY_ADD_GROUP_LIST, params, new ResponseCallBack<Friend>() {
            @Override
            public void onSuccessList(List<Friend> data) {
                super.onSuccessList(data);
                if(page.equals("1")){
                    adapter.clear();
                }
                adapter.addList(data);
            }

            @Override
            public void onFailure(ServiceException e) {
                CommonHelper.showTip(NewApplyGroupActivity.this,e.getMessage());
            }
        }, Friend.class);
    }


    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.newGroup));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ForwardUtils.target(NewApplyGroupActivity.this, Constant.CHAT_MSG,null);
            }
        });

        adapter = new NewApplyGroupdAdapter(NewApplyGroupActivity.this);
        mListView.setAdapter(adapter);



        //滑动菜单
        initSwipeMenu();
    }


    /**
     * 滑动菜单
     */
    private void initSwipeMenu() {
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(NewApplyGroupActivity.this);
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(248,
                        79, 130)));
                // set item width
                deleteItem.setWidth(WidgetUtil.dip2px(NewApplyGroupActivity.this,60));
                // set a icon
                deleteItem.setIcon(R.mipmap.v3_item_remove);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Friend f = (Friend) adapter.getItem(position);
                switch (index) {
                    case 0:
                        CommonHelper.showTip(NewApplyGroupActivity.this,"delete"+f.getNickName());
                        break;
                }
                return false;
            }
        });


    }


    public void refresh() {
        loadData();
    }
}
