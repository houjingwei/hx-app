package com.huixiangtv.live.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.activity.LiveRecordActivity;
import com.huixiangtv.live.adapter.LiveMsgAdapter;
import com.huixiangtv.live.adapter.LiveOnlineUsersAdapter;
import com.huixiangtv.live.adapter.RecyclerviewListener;
import com.huixiangtv.live.model.ChatMessage;
import com.huixiangtv.live.model.HistoryMsg;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.model.LiveMsg;
import com.huixiangtv.live.model.LoveGift;
import com.huixiangtv.live.model.MsgExt;
import com.huixiangtv.live.model.Other;
import com.huixiangtv.live.model.Share;
import com.huixiangtv.live.model.ShoutGift;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.pop.CameraWindow;
import com.huixiangtv.live.pop.GiftWindow;
import com.huixiangtv.live.pop.InputWindow;
import com.huixiangtv.live.pop.ShareWindow;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.ChatTokenCallBack;
import com.huixiangtv.live.service.LoginCallBack;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.utils.AnimHelper;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.utils.RongyunUtils;
import com.huixiangtv.live.utils.StringUtil;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import simbest.com.sharelib.ShareModel;

/**
 * Created by hjw on 16/5/17.
 */
public class LiveView extends RelativeLayout implements View.OnClickListener {

    Context ct;
    Activity activity;
    Handler handler;


    FrameLayout flLive;
    LinearLayout LlUser;

    LinearLayout llLoves;
    LinearLayout llOffline;
    LinearLayout llOnline;
    LinearLayout llDynamicAndCard;
    ImageView ivCard;
    ImageView ivDynamic;

    protected RecyclerView mRecyclerView;
    LiveOnlineUsersAdapter mAdapter;


    ImageView ivAddFen;


    ListView msgListView;
    List<LiveMsg> msgList;
    LiveMsgAdapter msgAdapter;

    ImageView ivMsg;
    ImageView ivShare;
    ImageView ivCamera;
    ImageView ivLove;
    ImageView ivGift;

    ImageView liveClose;





    RelativeLayout rlMenu;


    ImageView ivPhoto;
    TextView tvNickName;
    TextView tvHot;
    TextView tvLove;
    TextView tvOnline;

    private Live live;

    private ShoutGift shoutGift;


    BubbView bubbview = null;



    private int startHot = 0;
    private int startLove = 0;
    private long startTime;
    private int onLineNum = 0;


    public LiveView(Activity context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.live_view, this);
        ct = context;
        this.activity = context;
        initView();


    }



    /**
     * 艺人不在线时显示的view
     */
    public void showOfflineView() {
        llLoves.setVisibility(GONE);
        llOffline.setVisibility(VISIBLE);
        llOnline.setVisibility(GONE);
        llDynamicAndCard.setVisibility(VISIBLE);

    }

    public void hideOffline() {
        llLoves.setVisibility(VISIBLE);
        llOffline.setVisibility(GONE);
        llOnline.setVisibility(VISIBLE);
        llDynamicAndCard.setVisibility(GONE);
    }


    private void initView() {

        LlUser = (LinearLayout) findViewById(R.id.LlUser);
        ivAddFen = (ImageView) findViewById(R.id.ivAddFen);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        tvNickName = (TextView) findViewById(R.id.tvNickName);
        tvHot = (TextView) findViewById(R.id.tvHot);
        tvLove = (TextView) findViewById(R.id.tvLove);
        tvOnline = (TextView) findViewById(R.id.tvOnline);


        llLoves = (LinearLayout) findViewById(R.id.llLoves);
        llOffline = (LinearLayout) findViewById(R.id.llOffline);
        llOnline = (LinearLayout) findViewById(R.id.llOnline);
        llDynamicAndCard = (LinearLayout) findViewById(R.id.llDynamicAndCard);
        ivCard = (ImageView) findViewById(R.id.ivCard);
        ivDynamic = (ImageView) findViewById(R.id.ivDynamic);
        ivCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,String> params = new HashMap<String,String>();
                params.put("uid",live.getUid());
                ForwardUtils.target(activity, Constant.PIC_LIST, params);

            }
        });
        ivDynamic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != App.getLoginUser()) {
                    ForwardUtils.target(activity, Constant.OWN_CIRCLE, null);
                }else{
                    CommonHelper.showLoginPopWindow(activity, R.id.liveMain, new LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            ForwardUtils.target(activity, Constant.OWN_CIRCLE, null);
                        }
                    });
                }
            }
        });



        rlMenu = (RelativeLayout) findViewById(R.id.rlMenu);

        ivMsg = (ImageView) findViewById(R.id.ivMsg);
        ivShare = (ImageView) findViewById(R.id.ivShare);
        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        ivLove = (ImageView) findViewById(R.id.ivLove);
        ivGift = (ImageView) findViewById(R.id.ivGift);


        liveClose = (ImageView) findViewById(R.id.liveClose);

        LlUser.setOnClickListener(this);
        ivAddFen.setOnClickListener(this);
        ivMsg.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivLove.setOnClickListener(this);
        ivGift.setOnClickListener(this);
        liveClose.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);


        flLive = (FrameLayout) findViewById(R.id.flLive);
        flLive.setOnClickListener(this);


        ivLove.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] locations = new int[2];
                ivLove.getLocationInWindow(locations);
                int x = locations[0];
                if (x > 0) {
                    if(bubbview==null){
                        Log.i("errorMsg","123");
                        bubbview = new BubbView(activity, ivLove, flLive, false);
                        bubbview.bubble();
                        ivLove.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }


                }
            }
        });
    }


    public void setInfo(Live info) {
        if (null != info) {
            live = info;
            ImageUtils.displayAvator(ivPhoto, info.getPhoto());
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
        FendStatus();//是否关注
    }

    public void loadLive() {

        //检测当前登录用户是否已经关注了艺人
        if(null!=App.getLoginUser()) {
            fendStatus();
        }

        //开始时间
        startTime = System.currentTimeMillis();

        //加载在线用户
        initLoadOnlineUsers();

        //初始化消息
        initMsg();



    }


    /**
     * 关注状态
     */
    private void fendStatus() {
        CommonHelper.fenStatus(live.getUid(), new ApiCallback<Other>() {
            @Override
            public void onSuccess(Other data) {
                if(data.getIsFollowed().equals("1")){
                    ivAddFen.setVisibility(GONE);
                }
            }
        });
    }





    /**
     * 初始化消息
     */
    private void initMsg() {
        msgListView = (ListView) findViewById(R.id.msgList);
        msgAdapter = new LiveMsgAdapter(activity);
        msgList = new ArrayList<LiveMsg>();
        msgListView.setAdapter(msgAdapter);
        loadMsg();

        checkRongyunConnectionAndJoinRoom();
    }


    private void checkRongyunConnectionAndJoinRoom() {
        int flag = App.imClient.getCurrentConnectionStatus().getValue();
        if(null!=App.getLoginUser()){
            if (flag == 0 || flag == 1) {
                joinRoom();
            } else {
                reJoinRoom();
            }
        }else{
            reJoinRoom();
        }

    }



    private void reJoinRoom() {
        if(null!=live){
            App.imClient.quitChatRoom(live.getChatroom(),new RongIMClient.OperationCallback(){

                @Override
                public void onSuccess() {
                    Log.i("rongyun","exit"+live.getChatroom()+"");
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
        App.imClient.logout();
        final RongyunUtils utils = new RongyunUtils(App.getContext());
        utils.chatToken(new ChatTokenCallBack() {
            @Override
            public void getTokenSuccess(String token) {
                utils.connect(token, new ChatTokenCallBack() {
                    @Override
                    public void getTokenSuccess(String token) {

                        joinRoom();
                    }
                });
            }
        });
    }

    private void joinRoom() {
        App.imClient.joinChatRoom(live.getChatroom(), -1, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                RongIMClient.setOnReceiveMessageListener(new MyReceiveMessageListener());
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    /**
     * 发送入场消息
     */
    public boolean isVisitor = false;
    public void sendIntoRoomMsg() {
        isVisitor = true;
        if(null!=App.getLoginUser()){
            Map<String, String> params = new HashMap<String, String>();
            params.put("chatroom", live.getChatroom());
            params.put("nickName",App.getLoginUser().getNickName());
            params.put("photo", App.getLoginUser().getPhoto());
            RequestUtils.sendPostRequest(Api.INTO_ROOM, params, new ResponseCallBack<String>() {
                @Override
                public void onSuccess(String str) {
                    super.onSuccess(str);
                }


                @Override
                public void onFailure(ServiceException e) {
                    super.onFailure(e);
                }
            }, String.class);
        }

    }


    private void loadMsg() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("chatroom", live.getChatroom());
        RequestUtils.sendPostRequest(Api.HISTORY_MSG, params, new ResponseCallBack<HistoryMsg>() {
            @Override
            public void onSuccess(HistoryMsg data) {
                super.onSuccess(data);
                if (null != data.getLastMsg() && data.getLastMsg().size() > 0) {
                    List<LiveMsg> ll = new ArrayList<LiveMsg>();
                    for (ChatMessage message : data.getLastMsg()) {

                        LiveMsg msg = new LiveMsg();
                        MsgExt ext = message.getExt();
                        if (null != ext) {
                            msg.setMsgType(ext.getMsgType());
                            msg.setNickName(ext.getNickName());
                            msg.setUid(ext.getUid());
                            msg.setPhoto(ext.getPhoto());
                            msg.setRole(ext.getRole());
                            msg.setOnline(ext.getOnline());
                        }

                        msg.setContent(message.getContent().toString());
                        if (msg.getMsgType().equals(Constant.MSG_TYPE_BASE)) {
                            ll.add(msg);
                        }


                    }
                    if (ll.size() > 0) {
                        msgAdapter.addList(ll);
                        msgListView.setSelection(msgAdapter.getCount() - 1);
                    }
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, HistoryMsg.class);
    }


    public void initLoadOnlineUsers() {
        mAdapter = new LiveOnlineUsersAdapter(null);
        mAdapter.setOnItemClickListener(new RecyclerviewListener() {
            @Override
            public void onItemClick(View view, Object data) {
                CommonHelper.showTip(activity, ((User) data).getNickName());
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecylerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        loadOnlineUser();
    }

    Map<String,User> userMap = new HashMap<>();
    private void loadOnlineUser() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("chatroom", live.getChatroom());
        RequestUtils.sendPostRequest(Api.ONLINE_USER, params, new ResponseCallBack<User>() {
            @Override
            public void onSuccessList(List<User> data) {
                super.onSuccessList(data);
                if (null != data && data.size()>0) {
                    List<User> list = removalUser(data);
                    rootUserMap(list);
                    mAdapter.addData(list);
                    mRecyclerView.setAdapter(mAdapter);
                    onLineNum = data.size();
                    tvOnline.setText(onLineNum+"");
                }

            }

            @Override
            public void onSuccess(User data) {
                super.onSuccess(data);

            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, User.class);
    }

    private void rootUserMap(List<User> list) {
        for (User user : list) {
            userMap.put(user.getUid(),user);
        }
    }

    /**
     * 去除重复在线用户
     * @param data
     * @return
     */
    private List<User> removalUser(List<User> data) {

        List<User> list = new ArrayList<User>();
        Set<String> uids = new HashSet<String>();
        for (User user : data) {
            uids.add(user.getUid());
        }



        for (String uid : uids) {
            for (User user : data) {
                if(uid.equals(user.getUid())){
                    list.add(user);
                    break;
                }
            }
        }

        return list;

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivAddFen:
                if (null == App.getLoginUser()) {
                    CommonHelper.showLoginPopWindow(activity, R.id.liveMain, new LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            addFend();
                        }

                    });
                    return;
                }
                addFend();
                break;
            case R.id.ivMsg:
                if (null == App.getLoginUser()) {
                    CommonHelper.showLoginPopWindow(activity, R.id.liveMain,null);
                    return;
                }
                showChatInputView();
                break;
            case R.id.ivShare:
                if (null == App.getLoginUser()) {
                    CommonHelper.showLoginPopWindow(activity, R.id.liveMain, new LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            shareWin();
                        }
                    });
                    return;
                }
                shareWin();
                break;
            case R.id.ivCamera:
                if(isVisitor){
                    ImageUtils.catImage(activity);
                }else{
                    if (null == App.getLoginUser()) {
                        CommonHelper.showLoginPopWindow(activity, R.id.liveMain, new LoginCallBack() {
                            @Override
                            public void loginSuccess() {
                                showCameraWin();
                            }
                        });
                        return;
                    }
                    showCameraWin();
                }

                break;
            case R.id.ivGift:
//                if (null == App.getLoginUser()) {
//                    CommonHelper.showLoginPopWindow(activity, R.id.liveMain, new LoginCallBack() {
//                        @Override
//                        public void loginSuccess() {
//                            //showGift();
//                        }
//                    });
//                    return;
//                }
//                showGift();
                GiftWindow pop = new GiftWindow(activity, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,live,tvHot);
                pop.showAtLocation(activity.findViewById(R.id.liveMain), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                pop.update();

                break;
            case R.id.ivLove:
                if(bubbview==null){
                    bubbview = new BubbView(activity, ivLove, flLive, true);
                }
                if (null == App.getLoginUser()) {
                    CommonHelper.showLoginPopWindow(activity, R.id.liveMain, new LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            bubbview.oneBubble();
                        }
                    });
                    return;
                }
                bubbview.oneBubble();
                sendLove();
                break;
            case R.id.liveClose:
                if(activity instanceof LiveRecordActivity){
                    ((LiveRecordActivity) activity).closeLiving();
                }else{
                    activity.onBackPressed();
                }
                break;
            case R.id.LlUser:

                CommonHelper.showUserPopWindow(activity, R.id.liveMain, live);
                break;
        }
    }

    private void showChatInputView() {
        InputWindow pop = new InputWindow(activity, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,live,tvHot,msgAdapter,msgListView);
        pop.showAtLocation(activity.findViewById(R.id.liveMain), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        pop.update();
        pop.showKeyBoard();
    }


    private void FendStatus(){

        CommonHelper.addFen(live.getUid(),false,new ApiCallback<String>(){

            @Override
            public void onSuccess(String data) {
                ivAddFen.setVisibility(GONE);

            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                ivAddFen.setVisibility(GONE);
            }
        });

    }
    /**
     * 加关注
     */
    private void addFend() {

        if(null!=App.getLoginUser() && App.getLoginUser().equals(live.getUid())){
            CommonHelper.showTip(activity,"不可以关注自己哦");
            return;
        }
        CommonHelper.addFen(live.getUid(),true,new ApiCallback<String>(){

            @Override
            public void onSuccess(String data) {
                ivAddFen.setVisibility(GONE);
                CommonHelper.showTip(activity,"关注成功");
                ivAddFen.setVisibility(GONE);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(activity,e.getMessage());
            }
        });

    }


    /**
     * 送爱心
     */
    private void sendLove() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("chatroom", live.getChatroom());
        params.put("nickName", null!=App.getLoginUser()?App.getLoginUser().getNickName():"");
        params.put("photo", null!=App.getLoginUser()?App.getLoginUser().getPhoto():"");
        params.put("buid", null!=App.getLoginUser()?App.getLoginUser().getUid():"");
        params.put("cuid", live.getUid());
        params.put("nickName", null!=App.getLoginUser()?App.getLoginUser().getNickName():"");
        params.put("photo", null!=App.getLoginUser()?App.getLoginUser().getPhoto():"");
        RequestUtils.sendPostRequest(Api.LOVEPAYMENT, params, new ResponseCallBack<LoveGift>() {
            @Override
            public void onSuccess(LoveGift gift) {
                super.onSuccess(gift);
                if(StringUtil.isNotEmpty(gift.getLoves())){
                    int old = Integer.parseInt(tvLove.getText().toString());

                    String loves = ""+(old+1);
                    tvLove.setText(loves);
                    if(activity instanceof LiveRecordActivity){
                        ((LiveRecordActivity)activity).updateLovesByMeSend("1");
                    }
                }
            }


            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, LoveGift.class);
    }






    /**
     * 相机面板
     */
    private final int CHANGE_CAMERA = 100;
    private final int CHANGE_MEIYAN = 101;
    private void showCameraWin() {
        CommonHelper.showCameraPopWindow(activity, R.id.liveMain, new CameraWindow.SelectListener() {
            @Override
            public void select(int flag) {
                super.select(flag);
                if(activity instanceof LiveRecordActivity){
                    //1:切换像头 2：切图 3：美颜
                    if(flag==1){
                        android.os.Message msg = new android.os.Message();
                        msg.what = CHANGE_CAMERA;
                        msg.obj = msg;
                        handler.sendMessage(msg);
                    }else if(flag==2){
                        ((LiveRecordActivity)activity).cutScreen();
                    }else if(flag==3){
                        android.os.Message msg = new android.os.Message();
                        msg.what = CHANGE_MEIYAN;
                        msg.obj = msg;
                        handler.sendMessage(msg);
                    }
                }
            }
        });
    }


    /**
     * 分享面板
     */
    public void shareWin() {
        CommonHelper.showSharePopWindow(activity, R.id.liveMain, new ShareWindow.SelectShareListener() {
            @Override
            public void select(SHARE_MEDIA platForm) {
                super.select(platForm);
                if(platForm.equals(SHARE_MEDIA.SMS)){
                    platForm = SHARE_MEDIA.WEIXIN;
                }
                final SHARE_MEDIA pt = platForm;
                CommonHelper.shareInfo(platForm,"0", live.getLid(), new ApiCallback<Share>() {
                    @Override
                    public void onSuccess(Share data) {

                        android.os.Message msg = new android.os.Message();
                        data.setPlatForm(pt);
                        msg.what=SHARE;
                        msg.obj = data;
                        liveHandler.sendMessage(msg);

                    }
                });





            }

            @Override
            public void selectCopy() {
                super.selectCopy();
                ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(Api.SHARE_URL+live.getLid());
                CommonHelper.showTip(activity, "链接复制成功");
            }
        });

    }






    public LiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    public String[] getCloseInfo() {
        String[] array = new String[4];
        array[0] = startHot+"";
        array[1] = startLove+"";
        array[2] = onLineNum+"";
        array[3] = getPlayTime();
        return array;
    }

    /**
     * 获取直播市场
     * @return
     */
    public String getPlayTime() {
        long nowDate = System.currentTimeMillis();
        long diff = nowDate -startTime;
        long day = diff / (24 * 60 * 60 * 1000);
        long hour = (diff / (60 * 60 * 1000) - day * 24);
        long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long sec = (diff/1000-day*24*60*60-hour*60*60-min*60);

        if(day>1){
            return (day<10?"0":day)+"天 "+(hour<10?"0":hour)+":"+(min<10?"0":min)+":"+(sec<10?"0":sec)+":";
        }else{
            return (hour<10?"0":hour)+":"+(min<10?"0":min)+":"+(sec<10?"0":sec);
        }

    }




    //喊话弹幕
    private final int BARRAGE = 11;
    private final int BARRAGE_FINISH = 12;

    //礼物接收
    private final int GIFT_ANIM = 13;
    private final int GIFT_FINISH =14;


    private final int SHARE = 18;

    public void setHandle(Handler handle) {
        this.handler = handle;
    }




    private class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
        @Override
        public boolean onReceived(Message message, int i) {
            if (message.getContent() instanceof TextMessage) {
                TextMessage tm = (TextMessage) message.getContent();
                final LiveMsg msg = JSON.parseObject(String.valueOf(tm.getExtra()), LiveMsg.class);
                Log.i("myCloseclose",msg.getMsgType()+"");
                msg.setContent(tm.getContent().toString());
                if(null!=App.getLoginUser() && !msg.getUid().equals(App.getLoginUser().getUid())){
                    msgRecive(msg);
                }else if(null==App.getLoginUser()){
                    msgRecive(msg);
                }


            }
            return false;
        }
    }

    private void msgRecive(final LiveMsg msg) {
        if (msg.getMsgType().equals(Constant.MSG_TYPE_BASE)) {

            msgListView.post(new Runnable() {

                public void run() {
                    msgAdapter.add(msg);
                    msgListView.setSelection(msgAdapter.getCount()-1);

                }

            });

        }else if(msg.getMsgType().equals(Constant.MSG_TYPE_ENTER)){

            msgListView.post(new Runnable() {

                public void run() {
                    msgAdapter.add(msg);
                    msgListView.setSelection(msgAdapter.getCount()-1);
                    if(null!=userMap && userMap.size()>0){
                        User user = userMap.get(msg.getUid());
                        if(null==userMap.get(msg.getUid())){
                            user = new User();
                            user.setPhoto(msg.getPhoto());
                            user.setNickName(msg.getNickName());
                            mAdapter.addData(user);
                            mRecyclerView.setAdapter(mAdapter);
                            onLineNum++;
                        }
                    }else{
                        User user = new User();
                        user.setPhoto(msg.getPhoto());
                        user.setNickName(msg.getNickName());
                        mAdapter.addData(user);
                        mRecyclerView.setAdapter(mAdapter);
                        onLineNum++;
                    }
                    tvOnline.setText(onLineNum+"");



                }

            });

        } else if(msg.getMsgType().equals(Constant.MSG_TYPE_BARRAGE)){

            msgListView.post(new Runnable() {
                public void run() {

                    android.os.Message barrageMsg = new android.os.Message();
                    barrageMsg.what=BARRAGE;
                    barrageMsg.obj = msg;
                    liveHandler.sendMessage(barrageMsg);

                }

            });

        }else if(msg.getMsgType().equals(Constant.MSG_TYPE_GIFT)){
            msgListView.post(new Runnable() {
                public void run() {
                    if(StringUtil.isNotEmpty(msg.getAddhot())){
                        int old = Integer.parseInt(tvHot.getText().toString());
                        int addhot = Integer.parseInt(msg.getAddhot());
                        String loves = old+addhot+"";
                        tvHot.setText(loves);
                        startHot = startHot + addhot;
                        android.os.Message barrageMsg = new android.os.Message();
                        barrageMsg.what=GIFT_ANIM;
                        barrageMsg.obj = msg;
                        liveHandler.sendMessage(barrageMsg);
                    }

                }

            });

        }else if(msg.getMsgType().equals(Constant.MSG_TYPE_LOVE)){
            msgListView.post(new Runnable() {
                public void run() {
                    if(StringUtil.isNotEmpty(msg.getAddhot())){
                        int old = Integer.parseInt(tvLove.getText().toString());
                        int addhot = Integer.parseInt(msg.getCount());
                        String loves = old+addhot+"";
                        tvLove.setText(loves);

                        startLove = startLove + addhot;
                    }

                }

            });

        }else if(msg.getMsgType().equals(Constant.LIVING_CLOSE)){
            msgListView.post(new Runnable() {
                public void run() {
                    showCloseInfo(msg);

                }
            });
        }
    }


    private void showCloseInfo(LiveMsg msg) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("onLine",msg.getOnline());
        map.put("addhot",msg.getAddhot());
        map.put("love",msg.getLove());
        map.put("liveTime",msg.getLiveTime());
        map.put("photo",live.getPhoto());
        map.put("nickName",msg.getNickName());
        ForwardUtils.target(activity,Constant.LIVERECORD_FINISH,map);

    }


    private void showGiftAni(List<LiveMsg> ls, Handler liveHandler, int road) {
        AnimHelper.showGiftAni(activity,flLive,ls,liveHandler,road);
    }

    private void showBarrageAni(final LiveMsg msg, final Handler liveHandler, final int road) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                AnimHelper.showBarrageAni(activity,flLive,msg,road,liveHandler);
            }
        }, getRandomNum(1200));

    }

    private long getRandomNum(int time) {java.util.Random random=new java.util.Random();
        long result=random.nextInt(time);
        return result;
    }



    private int  firstGiftAnimRun = 0;
    private int firstBarrageAnimRun = 0;


    private List<LiveMsg> giftMsgs = new ArrayList<>();
    private List<LiveMsg> allGiftMsgs = new ArrayList<>();


    private List<LiveMsg> barrageMsgs = new ArrayList<>();

    private Handler liveHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case BARRAGE:
                    LiveMsg msg = (LiveMsg) message.obj;
                    if(firstBarrageAnimRun<2){
                        firstBarrageAnimRun++;
                        showBarrageAni(msg,liveHandler,firstBarrageAnimRun);
                    }else{
                        barrageMsgs.add(msg);
                    }

                    break;
                case BARRAGE_FINISH:
                    int currRoad = (int) message.obj;
                    if(null!=barrageMsgs && barrageMsgs.size()>0){
                        LiveMsg m = barrageMsgs.get(0);
                        barrageMsgs.remove(0);
                        showBarrageAni(m, liveHandler,currRoad);
                    }else{
                        firstBarrageAnimRun=0;
                    }
                    break;
                case GIFT_ANIM:
                    LiveMsg giftMsg = (LiveMsg) message.obj;
                    if(firstGiftAnimRun<1){
                        firstGiftAnimRun++;
                        List<LiveMsg> ls = new ArrayList<LiveMsg>();
                        ls.add(giftMsg);
                        showGiftAni(ls,liveHandler, firstGiftAnimRun);
                    }else{
                        giftMsgs.add(giftMsg);
                    }

                    break;
                case GIFT_FINISH:

                    int road = (int) message.obj;

                    allGiftMsgs.addAll(giftMsgs);

                    giftMsgs.clear();

                    if(null!=allGiftMsgs && allGiftMsgs.size()>0){
                        top10GiftMsgAndAnim(road,allGiftMsgs);

                    }else{
                        firstGiftAnimRun=0;
                    }
                    break;
                case SHARE:
                    Share data = (Share) message.obj;

                    ShareModel model = new ShareModel();
                    UMImage image;
                    if(StringUtil.isNotEmpty(data.getCover())){
                        image = new UMImage(activity, data.getCover());
                    }else{
                        image = new UMImage(activity, live.getPhoto());
                    }
                    model.setTitle(data.getTitle());
                    model.setTargetUrl(data.getUrl());
                    model.setImageMedia(image);
                    if(StringUtil.isNotEmpty(data.getDec())){
                        model.setContent(data.getDec());
                    }else{
                        model.setContent(" ");
                    }

                    CommonHelper.share(activity,model,data.getPlatForm(),0,new ApiCallback(){
                        @Override
                        public void onSuccess(Object data) {

                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };



    private void top10GiftMsgAndAnim(final int road, final List<LiveMsg> allGiftMsgs) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                List<LiveMsg> top10 = new ArrayList<LiveMsg>();
                List<LiveMsg> sameList = new ArrayList<LiveMsg>();
                LiveMsg msg = null;


                if(null!=allGiftMsgs && allGiftMsgs.size()>0){
                    msg = allGiftMsgs.get(0);
                    if(allGiftMsgs.size()>10){
                        try{
                            top10.addAll(allGiftMsgs.subList(0,10));
                        }catch (Exception e){

                        }
                    }else{
                        top10.addAll(allGiftMsgs);
                    }
                    Log.i("msgType","top10:"+top10.size());
                    allGiftMsgs.removeAll(top10);
                }
                if(top10.size()>0){

                    for (int i=0;i<top10.size();i++){

                        if(msg.getUid().equals(top10.get(i).getUid()) && msg.getGid().equals(top10.get(i).getGid())){
                            sameList.add(top10.get(i));
                        }
                    }

                }

                if(sameList.size()==0){
                    sameList.add(msg);
                }

                showGiftAni(sameList,liveHandler, road);
            }
        }, 100);
    }


}
