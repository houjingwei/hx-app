package com.huixiangtv.liveshow.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.FriendAdapter;
import com.huixiangtv.liveshow.model.Friend;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.listView)
    private SwipeMenuListView mListView;

    FriendAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        x.view().inject(this);
        initView();
        loadData();
    }



    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.friend));


        View view = LayoutInflater.from(this).inflate(R.layout.activity_friend_head, null, false);
        //新朋友
        view.findViewById(R.id.llNewFriend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForwardUtils.target(FriendActivity.this,Constant.NEW_FRIEND,null);
            }
        });
        //群聊
        view.findViewById(R.id.llGroupChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForwardUtils.target(FriendActivity.this, Constant.GROUP_LIST, null);
            }
        });
        //邀请的朋友
        view.findViewById(R.id.llInvite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        mListView.addHeaderView(view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Friend friend = (Friend) adapter.getItem(i-1);
                Map<String,String> map = new HashMap<String,String>();
                map.put("targetId",friend.getUid());
                map.put("userName",friend.getNickName());
                ForwardUtils.target(FriendActivity.this, Constant.CHAT_MSG,map);


            }
        });

        adapter = new FriendAdapter(FriendActivity.this);
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
                deleteItem.setWidth(dp2px(90));
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
                        CommonHelper.showTip(FriendActivity.this,"delete");
                        break;
                }
                return false;
            }
        });


    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void loadData() {
        CommonHelper.myFriend(new ApiCallback<List<Friend>>() {
            @Override
            public void onSuccess(List<Friend> data) {
                if(null!=data && data.size()>0){
                    adapter.addList(data);
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);

            }
        });
    }


}
