package com.huixiangtv.live.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
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
import com.huixiangtv.live.model.BasePayent;
import com.huixiangtv.live.model.ChatMessage;
import com.huixiangtv.live.model.HistoryMsg;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.model.LiveMsg;
import com.huixiangtv.live.model.LoveGift;
import com.huixiangtv.live.model.MsgExt;
import com.huixiangtv.live.model.Other;
import com.huixiangtv.live.model.ShoutGift;
import com.huixiangtv.live.model.User;
import com.huixiangtv.live.pop.CameraWindow;
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
import com.huixiangtv.live.utils.KeyBoardUtils;
import com.huixiangtv.live.utils.RongyunUtils;
import com.huixiangtv.live.utils.StringUtil;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.WidgetUtil;
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


    FrameLayout flLive;
    LinearLayout LlUser;

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

    private void initView() {

        LlUser = (LinearLayout) findViewById(R.id.LlUser);
        ivAddFen = (ImageView) findViewById(R.id.ivAddFen);

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
        findViewById(R.id.tvSendMsg).setOnClickListener(this);

        flLive = (FrameLayout) findViewById(R.id.flLive);
        flLive.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        flLive.setOnClickListener(this);


        rlGift = (RelativeLayout) findViewById(R.id.rlGift);


        ivLove.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] locations = new int[2];
                ivLove.getLocationInWindow(locations);
                int x = locations[0];
                if (x > 0) {
                    if(bubbview==null){
                        bubbview = new BubbView(activity, ivLove, flLive, false);
                    }
                    bubbview.bubble();
                    ivLove.getViewTreeObserver().removeGlobalOnLayoutListener(this);
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

        //初始化礼物面板
        initGift();

        //初始化喊话礼物
        initShoutGift(new ApiCallback<ShoutGift>() {
            @Override
            public void onSuccess(ShoutGift data) {
                shoutGift = data;
            }
        });
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

    private void initShoutGift(final ApiCallback call) {

        //发送喊话消息
        RequestUtils.sendPostRequest(Api.SHOUT_GIFT, null, new ResponseCallBack<ShoutGift>() {
            @Override
            public void onSuccess(ShoutGift data) {
                super.onSuccess(data);
                if (null != data) {
                    call.onSuccess(data);
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(activity, e.getMessage());
            }
        }, ShoutGift.class);
    }


    /**
     * 初始化礼物面板
     */
    private void initGift() {
        giftView = new GiftView(activity);
        giftView.setHotsView(tvHot);
        giftView.setActivity(activity);
        giftView.initView();
        giftView.setLiveInfo(live);
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
                App.imClient.setOnReceiveMessageListener(new MyReceiveMessageListener());
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

    private void loadOnlineUser() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("chatroom", live.getChatroom());
        RequestUtils.sendPostRequest(Api.ONLINE_USER, params, new ResponseCallBack<User>() {
            @Override
            public void onSuccessList(List<User> data) {
                super.onSuccessList(data);
                if (null != data && data.size()>0) {
                    List<User> list = removalUser(data);

                    mAdapter.addData(list);
                    mRecyclerView.setAdapter(mAdapter);

                    onLineNum = data.size();
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
                            showChatInputView();
                        }

                    });
                    return;
                }
                addFend();
                break;
            case R.id.ivMsg:
                if (null == App.getLoginUser()) {
                    CommonHelper.showLoginPopWindow(activity, R.id.liveMain, new LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            showChatInputView();
                        }

                    });
                    return;
                }
                showChatInputView();
                break;
            case R.id.tvSendMsg:
                sendMessage();
                break;
            case R.id.switchWrapper:
                if (null == App.getLoginUser()) {
                    CommonHelper.showLoginPopWindow(activity, R.id.liveMain, new LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            changeHanhua();
                        }
                    });
                    return;
                }
                changeHanhua();
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
                if (null == App.getLoginUser()) {
                    CommonHelper.showLoginPopWindow(activity, R.id.liveMain, new LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            showGift();
                        }
                    });
                    return;
                }
                showGift();
                break;
            case R.id.flLive:
                if (giftShow) {
                    hideGift();
                }
                hideKeyBoard();
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
                Map<String,String> params = new HashMap<String,String>();
                params.put("uid",live.getUid());
                if (null != App.getLoginUser() && !live.getUid().equals(App.getLoginUser().getUid())) {
                    ForwardUtils.target(activity, Constant.PIC_LIST, params);
                }else if(null==App.getLoginUser()){
                    ForwardUtils.target(activity, Constant.PIC_LIST, params);
                }
                break;
        }
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
                    int count = Integer.parseInt(gift.getLoves());
                    String loves = old+count+"";
                    tvLove.setText(loves);
                }
            }


            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, LoveGift.class);
    }

    /**
     * 发送消息
     */
    private void sendMessage() {


        if (TextUtils.isEmpty(etChatMsg.getText().toString())) {
            CommonHelper.showTip(activity, "不可以发送空消息哦~");
            etChatMsg.requestFocus();
            return;
        }
        hideKeyBoard();
        if ("shouting_yes".equals(switchTrigger.getTag().toString())) {
            if (null == shoutGift) {
                initShoutGift(new ApiCallback<ShoutGift>() {
                    @Override
                    public void onSuccess(ShoutGift data) {
                        sendShoutGift(data);
                    }

                    @Override
                    public void onFailure(ServiceException e) {
                        super.onFailure(e);
                    }
                });
            } else {
                sendShoutGift(shoutGift);
            }


        } else {
            //发送普通消息
            Map<String, String> params = new HashMap<String, String>();
            params.put("chatroom", live.getChatroom());
            params.put("content", etChatMsg.getText().toString());
            params.put("nickName", App.getLoginUser().getNickName());
            params.put("photo", App.getLoginUser().getPhoto());
            RequestUtils.sendPostRequest(Api.SEND_MSG, params, new ResponseCallBack<String>() {
                @Override
                public void onSuccess(String str) {
                    super.onSuccess(str);
                    LiveMsg msg = new LiveMsg();
                    msg.setContent(etChatMsg.getText().toString());
                    msg.setPhoto(App.getLoginUser().getPhoto());
                    msg.setNickName(App.getLoginUser().getNickName());
                    msgAdapter.add(msg);
                    msgListView.setSelection(msgAdapter.getCount() - 1);
                    etChatMsg.setText("");
                }

                @Override
                public void onFailure(ServiceException e) {
                    super.onFailure(e);
                    CommonHelper.showTip(activity, e.getMessage());
                }
            }, String.class);
        }
    }

    private void sendShoutGift(ShoutGift gift) {
        //发送喊话消息
        Map<String, String> params = new HashMap<String, String>();
        params.put("chatroom", live.getChatroom());
        params.put("amount", gift.getCoin());
        params.put("content", etChatMsg.getText().toString());
        params.put("buid", App.getLoginUser().getUid());
        params.put("nickName", App.getLoginUser().getNickName());
        params.put("photo", App.getLoginUser().getPhoto());
        params.put("cuid", live.getUid());
        RequestUtils.sendPostRequest(Api.GIFT_PAYMENT, params, new ResponseCallBack<BasePayent>() {
            @Override
            public void onSuccess(BasePayent data) {
                super.onSuccess(data);
                hideKeyBoard();
                if (null != data) {
                    etChatMsg.setText("");
                    //更新金币数量
                    App.userCoin = data.getAmount()+"";

                    int old = Integer.parseInt(tvHot.getText().toString());
                    int addhot = Integer.parseInt(data.getHots());
                    String loves = old+addhot+"";
                    tvHot.setText(loves);
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(activity, e.getMessage());
            }
        }, BasePayent.class);
    }

    /**
     * 隐藏礼物面板
     */
    boolean giftShow = true;

    private void hideGift() {
        giftShow = false;
        rlMenu.setVisibility(View.VISIBLE);
        AnimHelper.viewDownToBottom(rlGift, WidgetUtil.dip2px(activity, 150), 300);
    }

    /**
     * 显示礼物面板
     */
    private void showGift() {
        giftShow = true;
        rlGift.setVisibility(VISIBLE);
        AnimHelper.viewDownToBottom(rlMenu, WidgetUtil.dip2px(activity, 60), 300);
        AnimHelper.viewUpToMiddle(rlGift, WidgetUtil.dip2px(activity, 150), 500);
    }


    /**
     * 相机面板
     */
    private void showCameraWin() {
        CommonHelper.showCameraPopWindow(activity, R.id.liveMain, new CameraWindow.SelectListener() {
            @Override
            public void select(int flag) {
                super.select(flag);
                if(activity instanceof LiveRecordActivity){
                    //1:切换像头 2：切图 3：美颜
                    if(flag==1){
                       ((LiveRecordActivity)activity).changeCamera();
                    }else if(flag==2){
                        ((LiveRecordActivity)activity).cutScreen();
                    }else if(flag==3){
                        ((LiveRecordActivity)activity).changeBeau();
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
                ShareModel model = new ShareModel();
                UMImage image = new UMImage(activity, live.getPhoto());
                model.setTitle(live.getNickName()+live.getTitle());
                model.setTargetUrl(Api.SHARE_URL+live.getLid());
                model.setImageMedia(image);
                model.setContent(live.getNickName()+live.getTitle());
                CommonHelper.share(activity,model,platForm,0,new ApiCallback(){
                    @Override
                    public void onSuccess(Object data) {

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

    /**
     * 喊话切换
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
            etChatMsg.setHint("和大家聊聊吧~");

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
            if (null != shoutGift) {
                etChatMsg.setHint(shoutGift.getTips());
            } else {
                initShoutGift(new ApiCallback<ShoutGift>() {
                    @Override
                    public void onSuccess(ShoutGift data) {
                        etChatMsg.setHint(shoutGift.getTips());
                    }
                });
            }
        }
    }

    /**
     * 显示聊天区域
     */
    private void showChatInputView() {
        KeyBoardUtils.openKeybord(etChatMsg, ct);
    }


    public void removeGlobalListener() {
        if (null != globalLayoutListener) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                flLive.getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
            } else {
                flLive.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
            }
        }
    }


    // 状态栏的高度
    private int statusBarHeight = 0;

    private int myStautHeight() {
        Rect rectangle= new Rect();
        Window window= activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight= rectangle.top;
        return statusBarHeight;
    }

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
            if(statusBarHeight==0) {
                statusBarHeight = myStautHeight();
            }
            if (keyboardHeight == 0 && heightDiff > statusBarHeight) {
                keyboardHeight = heightDiff - statusBarHeight;
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
        Log.i("rinima", App.screenHeight - keyboardHeight+ "");
        margin.setMargins(0, App.screenHeight - keyboardHeight - WidgetUtil.dip2px(ct, 40), 0, 0);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        rlChatView.setLayoutParams(layoutParams);
        etChatMsg.requestFocus();
    }


    private void onHideKeyboard() {
        rlChatView.setVisibility(GONE);
//        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(rlChatView.getLayoutParams());
//        //左上右下
//        margin.setMargins(0,0, 0, 0);
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
//        rlChatView.setLayoutParams(layoutParams);
    }

    public LiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    public void removeMsgListener() {
        App.imClient.setOnReceiveMessageListener(null);
        App.imClient.quitChatRoom("123456", new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Log.i("outout", "ok");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
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




    /**
     * 隐藏键盘
     */
    private void hideKeyBoard() {
        KeyBoardUtils.closeKeybord(etChatMsg, ct);
    }




    //喊话弹幕
    private final int BARRAGE = 11;
    private final int BARRAGE_FINISH = 12;

    //礼物接收
    private final int GIFT_ANIM = 13;
    private final int GIFT_FINISH =14;

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
                    User user = new User();
                    user.setPhoto(msg.getPhoto());
                    user.setNickName(msg.getNickName());
                    mAdapter.addData(user);
                    mRecyclerView.setAdapter(mAdapter);
                    onLineNum++;


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
