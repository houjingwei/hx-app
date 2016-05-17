package com.huixiang.live.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.huixiang.live.R;
import com.huixiang.live.activity.StartLiveActivity;
import com.huixiang.live.adapter.LiveMsgAdapter;
import com.huixiang.live.adapter.LiveOnlineUsersAdapter;
import com.huixiang.live.model.Live;
import com.huixiang.live.model.LiveChatMsg;
import com.huixiang.live.model.Star;
import com.huixiang.live.utils.KeyBoardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjw on 16/5/17.
 */
public class LiveView extends RelativeLayout implements View.OnClickListener {

    Context ct;
    Activity activity;

    protected RecyclerView mRecyclerView;
    LiveOnlineUsersAdapter mAdapter;


    ListView msgListView;
    List<LiveChatMsg> msgList;
    LiveMsgAdapter msgAdapter;

    ImageView ivMsg;
    ImageView ivShare;
    ImageView ivCamera;
    ImageView ivLove;
    ImageView ivGift;

    RelativeLayout rlChatView;
    EditText etChatMsg;


    public LiveView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.live_view, this);
        ct = context;

        initView();
    }

    private void initView() {
        ivMsg = (ImageView) findViewById(R.id.ivMsg);
        ivShare = (ImageView) findViewById(R.id.ivShare);
        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        ivLove = (ImageView) findViewById(R.id.ivLove);
        ivGift = (ImageView) findViewById(R.id.ivGift);

        ivMsg.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivLove.setOnClickListener(this);
        ivGift.setOnClickListener(this);


        rlChatView = (RelativeLayout) findViewById(R.id.rlChatView);
        etChatMsg = (EditText) findViewById(R.id.etChatMsg);
    }

    public void loadLive(Live live) {

        //加载在线用户
        initLoadOnlineUsers();

        //初始化消息
        initMsg();


        new Thread(new MyThread()).start();
    }

    private void initMsg() {
        msgListView = (ListView) findViewById(R.id.msgList);
        msgAdapter = new LiveMsgAdapter(activity);
        msgList = new ArrayList<LiveChatMsg>();
        loadMsg();
        msgAdapter.addList(msgList);
        msgListView.setAdapter(msgAdapter);

    }

    private List loadMsg() {
        for (int i=1;i<10;i++){
            LiveChatMsg msg = new LiveChatMsg("doudou","今晚吃什么饭呀+"+i,"1");
            msgList.add(msg);
        }

        return msgList;
    }


    public void initLoadOnlineUsers(){
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecylerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        List<Star> list = new ArrayList<Star>();
        for (int i=0;i<5;i++){
            Star star = new Star("https://ss0.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2989677963,180662226&fm=21&gp=0.jpg");
            list.add(star);
        }

        mAdapter = new LiveOnlineUsersAdapter(list);

        mRecyclerView.setAdapter(mAdapter);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivMsg:
                showChatInputView();
                break;

        }
    }

    /**
     * 显示聊天区域
     */
    private void showChatInputView() {
        rlChatView.setVisibility(VISIBLE);
        KeyBoardUtils.openKeybord(etChatMsg,ct);
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    Thread.sleep(3000);// 线程暂停10秒，单位毫秒
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            mAdapter.addData(new Star("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3869864100,3728042084&fm=21&gp=0.jpg"));
            mAdapter.removeData(0);
            java.util.Random random=new java.util.Random();// 定义随机类

            msgAdapter.addList(loadMsg());
            msgListView.setSelection(msgAdapter.getCount()-1);
        }
    };



    public LiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setActivity(StartLiveActivity activity) {
        this.activity = activity;
    }
}
