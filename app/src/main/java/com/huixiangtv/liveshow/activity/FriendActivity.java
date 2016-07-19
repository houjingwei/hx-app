package com.huixiangtv.liveshow.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.FriendAdapter;
import com.huixiangtv.liveshow.model.Friend;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

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

            }
        });
        //群聊
        view.findViewById(R.id.llGroupChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //邀请的朋友
        view.findViewById(R.id.llInvite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        mListView.addHeaderView(view);

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
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(14);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

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
                        CommonHelper.showTip(FriendActivity.this,"open");
                        break;
                    case 1:
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
                testData();
            }
        });
    }

    private void testData() {
        List<Friend> friendList = new ArrayList<Friend>();

        Friend f = new Friend("1","lele","https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=4040074045,1463637776&fm=21&gp=0.jpg","0");

        Friend f2 = new Friend("2","dingding","http://img5.imgtn.bdimg.com/it/u=117954092,421964103&fm=23&gp=0.jpg","1");
        friendList.add(f);
        friendList.add(f2);
        adapter.addList(friendList);
    }
}
