package com.huixiangtv.live.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.Fans;
import com.huixiangtv.live.adapter.LiveMsgAdapter;
import com.huixiangtv.live.adapter.LiveOnlineUsersAdapter;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.model.LiveMsg;
import com.huixiangtv.live.model.Star;
import com.huixiangtv.live.pop.CameraWindow;
import com.huixiangtv.live.pop.ShareWindow;
import com.huixiangtv.live.utils.AnimHelper;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.KeyBoardUtils;
import com.huixiangtv.live.utils.ShareSdk;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.WidgetUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hjw on 16/5/17.
 */
public class LiveView extends RelativeLayout implements View.OnClickListener {

    Context ct;
    Activity activity;


    FrameLayout flLive;

    protected RecyclerView mRecyclerView;
    LiveOnlineUsersAdapter mAdapter;


    ListView msgListView;
    List<LiveMsg> msgList;
    LiveMsgAdapter msgAdapter;

    ImageView ivMsg;
    ImageView ivShare;
    ImageView ivCamera;
    ImageView ivLove;
    ImageView ivGift;


    private RelativeLayout switchWrapper;
    private RelativeLayout switchTrigger;
    private TextView switchLabel;
    RelativeLayout rlChatView;
    EditText etChatMsg;


    //礼物面板
    RelativeLayout rlGift;
    GiftView giftView;

    RelativeLayout rlMenu;


    ImageView ivPhoto;
    TextView tvNickName;
    TextView tvHot;
    TextView tvLove;
    TextView tvOnline;




    public LiveView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.live_view, this);
        ct = context;

        initView();
    }

    private void initView() {

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        tvNickName = (TextView) findViewById(R.id.tvNickName);
        tvHot = (TextView) findViewById(R.id.tvHot);
        tvLove = (TextView) findViewById(R.id.tvLove);
        tvOnline = (TextView) findViewById(R.id.tvOnline);



        rlMenu = (RelativeLayout) findViewById(R.id.rlMenu);

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
        switchWrapper = (RelativeLayout) findViewById(R.id.switchWrapper);
        switchTrigger = (RelativeLayout) findViewById(R.id.switchTrigger);
        switchLabel = (TextView) findViewById(R.id.switchLabel);
        switchWrapper.setOnClickListener(this);//添加switch喊话事件
        //喊话状态
        GradientDrawable gd = (GradientDrawable) switchWrapper.getBackground();
        gd.setColor(getResources().getColor(R.color.gray));
        switchWrapper.setBackgroundDrawable(gd);
        switchLabel.setTextColor(getResources().getColor(R.color.gray));
        etChatMsg.setHint("和大家一起聊~");


        flLive = (FrameLayout) findViewById(R.id.flLive);
        flLive.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        flLive.setOnClickListener(this);


        rlGift = (RelativeLayout) findViewById(R.id.rlGift);


        ivLove.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int[] locations=new int[2];
                ivLove.getLocationInWindow(locations);
                int x = locations[0];
                if(x>0){
                    new BubbleView(activity,ivLove,flLive,false);
                    ivLove.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }


            }
        });


    }




    public void setInfo(Live info) {
        if(null!=info){
            ImageUtils.displayAvator(ivPhoto,info.getPhoto());
            tvNickName.setText(info.getNickName());
            tvHot.setText(info.getHotValue());
            tvLove.setText(info.getLoveCount());
            tvOnline.setText(info.getCount());
        }

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        tvNickName = (TextView) findViewById(R.id.tvNickName);
        tvHot = (TextView) findViewById(R.id.tvHot);
        tvLove = (TextView) findViewById(R.id.tvLove);
        tvOnline = (TextView) findViewById(R.id.tvOnline);

    }

    public void loadLive(Live live) {

        //加载在线用户
        initLoadOnlineUsers();

        //初始化消息
        initMsg();

        //初始化礼物面板
        initGift();








//        new Thread(new MyThread()).start();
    }


    /**
     * 初始化礼物面板
     */
    private void initGift() {
        giftView = new GiftView(activity);
        giftView.setActivity(activity);
        giftView.initView();
        giftView.setRootView(flLive);
        rlGift.addView(giftView);
    }



    /**
     * 初始化消息
     */
    private void initMsg() {
        msgListView = (ListView) findViewById(R.id.msgList);
        msgAdapter = new LiveMsgAdapter(activity);
        msgList = new ArrayList<LiveMsg>();
        loadMsg();
        msgAdapter.addList(msgList);
        msgListView.setAdapter(msgAdapter);

        //启动定时器
        timer.schedule(task, 0, 2000);

    }


    private Handler handler  = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.obj == 1){
                LiveMsg m = getOneMsg();
                if(null!=m){
                    msgAdapter.add(m);
                    msgListView.setSelection(msgAdapter.getCount()-1);
                }
            }
        }
    };

    public LiveMsg getOneMsg() {
        if(null!=msgList){
            int index=(int)(Math.random()*msgList.size());
            return msgList.get(index);
        }

        return null;
    }


    private Timer timer = new Timer(true);

    //任务
    private TimerTask task = new TimerTask() {
        public void run() {
            Message msg = new Message();
            msg.obj = 1;
            handler.sendMessage(msg);
        }
    };



    private void loadMsg() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(activity.getAssets().open("liveMsg.json"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            msgList = JSON.parseArray(stringBuilder.toString(),LiveMsg.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initLoadOnlineUsers() {
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecylerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        List<Star> list = new ArrayList<Star>();
        for (int i = 0; i < 5; i++) {
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
            case R.id.switchWrapper:
                changeHanhua();
                break;
            case R.id.ivShare:
                shareWin();
                break;
            case R.id.ivCamera:
                showCameraWin();
                break;
            case R.id.ivGift:
                showGift();
                break;
            case R.id.flLive:
                hideGift();
                break;
            case R.id.ivLove:
                new BubbleView(activity,ivLove,flLive,true);
                break;



        }
    }

    /**
     * 隐藏礼物面板
     */
    private void hideGift() {
        rlMenu.setVisibility(View.VISIBLE);
        AnimHelper.viewDownToBottom(rlGift,WidgetUtil.dip2px(activity,150),300);
    }

    /**
     * 显示礼物面板
     */
    private void showGift() {
        rlGift.setVisibility(VISIBLE);
        AnimHelper.viewDownToBottom(rlMenu, WidgetUtil.dip2px(activity,60),300);
        AnimHelper.viewUpToMiddle(rlGift, WidgetUtil.dip2px(activity,150),500);
    }


    /**
     * 相机面板
     */
    private void showCameraWin() {
        CommonHelper.showCameraPopWindow(activity, R.id.liveMain, new CameraWindow.SelectListener() {
            @Override
            public void select(int flag) {
                super.select(flag);
                CommonHelper.showTip(activity,"choise" + flag);
            }
        });
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(activity, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(activity,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(activity,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }

    };

    /**
     * 分享面板
     */
    private void shareWin() {
        CommonHelper.showSharePopWindow(activity, R.id.liveMain, new ShareWindow.SelectShareListener() {
            @Override
            public void select(SHARE_MEDIA platForm) {
                super.select(platForm);
                ShareSdk.startShare(activity, "title", "content", platForm, "http://www.umeng.com/images/pic/social/integrated_3.png", umShareListener);
                //CommonHelper.showTip(activity,"share to " + platForm);
            }

            @Override
            public void selectCopy() {
                super.selectCopy();
                CommonHelper.showTip(activity,"链接复制成功");
            }
        });
    }

    /**
     * 汉化切换
     */
    private void changeHanhua() {
        GradientDrawable gd = (GradientDrawable) switchWrapper.getBackground();
        String triggerTag = switchTrigger.getTag().toString();
        final int triggerWidth = switchTrigger.getWidth();
        final int wrapperWidth = switchWrapper.getWidth();
        final int parentPaddingRight = switchWrapper.getPaddingRight();
        final int offset = wrapperWidth - triggerWidth - parentPaddingRight * 2;

        if ("shouting_yes".equals(triggerTag)) {
            //喊话状态
            gd.setColor(getResources().getColor(R.color.gray));
            switchLabel.setTextColor(getResources().getColor(R.color.gray));
            switchTrigger.setTag("shouting_no");

            TranslateAnimation switchTrans = new TranslateAnimation(
                    Animation.ABSOLUTE, offset,
                    Animation.ABSOLUTE, 0,
                    Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0);
            switchTrans.setDuration(400);
            switchTrans.setFillAfter(true);
            switchTrigger.startAnimation(switchTrans);
            etChatMsg.setHint("和大家一起聊~");

        } else {
            //非喊话状态
            gd.setColor(getResources().getColor(R.color.mainColor));
            switchLabel.setTextColor(getResources().getColor(R.color.mainColor));
            switchTrigger.setTag("shouting_yes");

            TranslateAnimation switchTrans = new TranslateAnimation(
                    Animation.ABSOLUTE, (parentPaddingRight - switchTrigger.getLeft()),
                    Animation.ABSOLUTE, (offset + parentPaddingRight - switchTrigger.getLeft()),
                    Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0);
            switchTrans.setDuration(400);
            switchTrans.setFillAfter(true);
            switchTrigger.startAnimation(switchTrans);
            etChatMsg.setHint("喊话需20个金币/次");
        }
    }

    /**
     * 显示聊天区域
     */
    private void showChatInputView() {

        KeyBoardUtils.openKeybord(etChatMsg, ct);
    }


    public void removeGlobalListener() {
        if(null!=globalLayoutListener) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                flLive.getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
            } else {
                flLive.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
            }
        }
    }


    // 状态栏的高度
    private int statusBarHeight = App.statuBarHeight;
    // 软键盘的高度
    private int keyboardHeight = 0;
    // 软键盘的显示状态
    private boolean isShowKeyboard = false;

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            // 应用可以显示的区域。此处包括应用占用的区域，
            // 以及ActionBar和状态栏，但不含设备底部的虚拟按键。
            Rect r = new Rect();
            flLive.getWindowVisibleDisplayFrame(r);

            // 屏幕高度。这个高度不含虚拟按键的高度
            int screenHeight = flLive.getRootView().getHeight();

            int heightDiff = screenHeight - (r.bottom - r.top);

            // 在不显示软键盘时，heightDiff等于状态栏的高度
            // 在显示软键盘时，heightDiff会变大，等于软键盘加状态栏的高度。
            // 所以heightDiff大于状态栏高度时表示软键盘出现了，
            // 这时可算出软键盘的高度，即heightDiff减去状态栏的高度
            if (keyboardHeight == 0 && heightDiff > statusBarHeight) {

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    keyboardHeight = heightDiff - statusBarHeight;
                }else{
                    keyboardHeight = heightDiff;
                }
            }

            if (isShowKeyboard) {
                // 如果软键盘是弹出的状态，并且heightDiff小于等于状态栏高度，
                // 说明这时软键盘已经收起
                if (heightDiff <= statusBarHeight) {
                    isShowKeyboard = false;
                    onHideKeyboard();
                }
            } else {
                // 如果软键盘是收起的状态，并且heightDiff大于状态栏高度，
                // 说明这时软键盘已经弹出
                if (heightDiff > statusBarHeight) {
                    isShowKeyboard = true;
                    onShowKeyboard();
                }
            }
        }
    };

    private void onShowKeyboard() {
        rlChatView.setVisibility(VISIBLE);
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(rlChatView.getLayoutParams());
        //左上右下
        Log.i("rinima",App.screenHeight - keyboardHeight - WidgetUtil.dip2px(ct, 40)+"");
        margin.setMargins(0, App.screenHeight - keyboardHeight - WidgetUtil.dip2px(ct, 40), 0, 0);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        rlChatView.setLayoutParams(layoutParams);
    }


    private void onHideKeyboard() {
        rlChatView.setVisibility(GONE);
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(rlChatView.getLayoutParams());
        //左上右下
        margin.setMargins(0,0, 0, 0);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        rlChatView.setLayoutParams(layoutParams);
    }

    public LiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }



}
