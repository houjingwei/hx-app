package com.huixiangtv.liveshow.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.huixiangtv.liveshow.App;
import com.huixiangtv.liveshow.Constant;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.adapter.FriendAdapter;
import com.huixiangtv.liveshow.model.Friend;
import com.huixiangtv.liveshow.model.UnreadCount;
import com.huixiangtv.liveshow.service.ApiCallback;
import com.huixiangtv.liveshow.service.ServiceException;
import com.huixiangtv.liveshow.ui.CommonTitle;
import com.huixiangtv.liveshow.utils.CommonHelper;
import com.huixiangtv.liveshow.utils.ForwardUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imlib.model.Message;

public class FriendActivity extends BaseBackActivity {


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.listView)
    private SwipeMenuListView mListView;

    FriendAdapter adapter ;


    TextView tvFriendUnRead;
    TextView tvGroupMsgUnRead;
    TextView tvInviteUnRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        x.view().inject(this);
        EventBus.getDefault().register(this);
        initView();
        loadData();
    }

    @Subscriber(tag = "new_friend", mode = ThreadMode.MAIN)
    public void friend(Message msg) {

        if(null!=tvFriendUnRead && null!=tvGroupMsgUnRead && null!=tvInviteUnRead){
            Log.i("qunimade","FriendActivity+friend");
            setCount();
        }
    }

    private void setCount() {
        App._myReceiveMessageListener.calcuCount(new ApiCallback<UnreadCount>() {
            @Override
            public void onSuccess(UnreadCount data) {
                if(data.getGroupUnReadCount()>0){
                    tvGroupMsgUnRead.setText(data.getGroupUnReadCount()+"");
                    tvGroupMsgUnRead.setVisibility(View.VISIBLE);
                }else{
                    tvGroupMsgUnRead.setVisibility(View.GONE);
                }

                if(data.getNewFriendUnReadCount()>0){
                    tvFriendUnRead.setText(data.getNewFriendUnReadCount()+"");
                    tvFriendUnRead.setVisibility(View.VISIBLE);
                }else{
                    tvFriendUnRead.setVisibility(View.GONE);
                }
            }
        });

        

    }


    private void initView() {
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.friend));


        View view = LayoutInflater.from(this).inflate(R.layout.activity_friend_head, null, false);
        tvFriendUnRead = (TextView) view.findViewById(R.id.tvFriendUnRead);
        tvGroupMsgUnRead = (TextView) view.findViewById(R.id.tvGroupMsgUnRead);
        tvInviteUnRead = (TextView) view.findViewById(R.id.tvInviteUnRead);
        //新朋友
        view.findViewById(R.id.llNewFriend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForwardUtils.target(FriendActivity.this,Constant.NEW_FRIEND,null);
                App._myReceiveMessageListener.setMsgRead(1);
            }
        });
        //群聊
        view.findViewById(R.id.llGroupChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForwardUtils.target(FriendActivity.this, Constant.GROUP_LIST, null);
                App._myReceiveMessageListener.setMsgRead(2);
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
                map.put("type","1");
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
                        FriendActivity.this);
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(248,
                        79, 130)));
                // set item width
                deleteItem.setWidth(dp2px(60));
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
                    adapter.clear();
                    adapter.addList(data);
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("qunimade","FriendActivity+onResume");
        setCount();
    }
}
