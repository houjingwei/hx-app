package com.huixiangtv.liveshow.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.huixiangtv.liveshow.adapter.NewFriendAdapter;
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

public class NewFriendActivity extends BaseBackActivity {

    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.listView)
    private SwipeMenuListView mListView;

    NewFriendAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        x.view().inject(this);
        initView();
        loadData();
    }

    private void loadData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page","1");
        params.put("pageSize","10000");
        RequestUtils.sendPostRequest(Api.APPLY_ADD_ME_LIST, params, new ResponseCallBack<Friend>() {
            @Override
            public void onSuccessList(List<Friend> data) {
                super.onSuccessList(data);
                adapter.addList(data);
            }

            @Override
            public void onFailure(ServiceException e) {
                CommonHelper.showTip(NewFriendActivity.this,e.getMessage());
            }
        }, Friend.class);
    }


    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.newFriend));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ForwardUtils.target(NewFriendActivity.this, Constant.CHAT_MSG,null);
            }
        });

        adapter = new NewFriendAdapter(NewFriendActivity.this);
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
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(WidgetUtil.dip2px(NewFriendActivity.this,90));
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

                switch (index) {
                    case 0:
                        CommonHelper.showTip(NewFriendActivity.this,"delete");
                        break;
                }
                return false;
            }
        });


    }






}