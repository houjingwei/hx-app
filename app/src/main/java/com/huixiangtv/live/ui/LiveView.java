package com.huixiangtv.live.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import com.alibaba.fastjson.JSON;
import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.Constant;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.LiveMsgAdapter;
import com.huixiangtv.live.adapter.LiveOnlineUsersAdapter;
import com.huixiangtv.live.adapter.RecyclerviewListener;
import com.huixiangtv.live.model.BasePayent;
import com.huixiangtv.live.model.ChatMessage;
import com.huixiangtv.live.model.HistoryMsg;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.model.LiveMsg;
import com.huixiangtv.live.model.MsgExt;
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
import com.huixiangtv.live.utils.KeyBoardUtils;
import com.huixiangtv.live.utils.RongyunUtils;
import com.huixiangtv.live.utils.image.ImageUtils;
import com.huixiangtv.live.utils.widget.WidgetUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imlib.RongIMClient;
import io.rong.message.TextMessage;

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


        liveClose = (ImageView) findViewById(R.id.liveClose);

        ivMsg.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivLove.setOnClickListener(this);
        ivGift.setOnClickListener(this);
        liveClose.setOnClickListener(this);


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
                    new BubbView(activity, ivLove, flLive, false);
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

    }

    public void loadLive() {

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


        App.imClient.setOnReceiveMessageListener(new MyReceiveMessageListener());
        int flag = App.imClient.getCurrentConnectionStatus().getValue();
        if (flag == 0 || flag == 1) {
            joinRoom();
        } else {
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

    }

    private void joinRoom() {
        App.imClient.joinChatRoom(live.getChatroom(), -1, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                CommonHelper.showTip(activity, "进入聊天室成功");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }



    public LiveMsg getOneMsg() {
        if (null != msgList && msgList.size() > 0) {
            int index = (int) (Math.random() * msgList.size());
            return msgList.get(index);
        }

        return null;
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
                mAdapter.addData(data);
                mRecyclerView.setAdapter(mAdapter);
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
     * 发送进场消息
     */
    public void sendIntoRoomMsg() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("chatroom", live.getChatroom());
        params.put("nickName", null!=App.getLoginUser()?App.getLoginUser().getNickName():"");
        params.put("photo", null!=App.getLoginUser()?App.getLoginUser().getPhoto():"");
        RequestUtils.sendPostRequest(Api.INTO_ROOM, params, new ResponseCallBack<String>() {
            @Override
            public void onSuccessString(String str) {
                super.onSuccessString(str);
            }

            @Override
            public void onSuccessList(List<String> data) {
                super.onSuccessList(data);
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
            }
        }, String.class);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                if (null == App.getLoginUser()) {
                    CommonHelper.showLoginPopWindow(activity, R.id.liveMain, new LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            new BubbView(activity, ivLove, flLive, true);
                        }
                    });
                    return;
                }
                new BubbView(activity, ivLove, flLive, true);
                break;
            case R.id.liveClose:
                activity.onBackPressed();
                break;


        }
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
            params.put("nickName", live.getNickName());
            params.put("photo", live.getPhoto());
            RequestUtils.sendPostRequest(Api.SEND_MSG, params, new ResponseCallBack<String>() {
                @Override
                public void onSuccess(String str) {
                    super.onSuccess(str);
                    hideKeyBoard();
                    LiveMsg msg = new LiveMsg();
                    msg.setContent(etChatMsg.getText().toString());
                    msg.setPhoto(live.getPhoto());
                    msg.setNickName(live.getNickName());
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
                if (null != data) {
                    etChatMsg.setText("");
                    CommonHelper.showTip(activity, "喊话消息发送成功");
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
                CommonHelper.showTip(activity, "choise" + flag);
            }
        });
    }


    /**
     * 分享面板
     */
    private void shareWin() {
        CommonHelper.showSharePopWindow(activity, R.id.liveMain, new ShareWindow.SelectShareListener() {
            @Override
            public void select(SHARE_MEDIA platForm) {
                super.select(platForm);
                CommonHelper.share(activity, live.getNickName() + live.getTitle(), live.getTopic() + "正在回响直播，赶紧来捧场吧", platForm, live.getPhoto(), "http://h5.huixiangtv.com/live/" + live.getLid(), null);
            }

            @Override
            public void selectCopy() {
                super.selectCopy();
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
                } else {
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
        Log.i("rinima", App.screenHeight - keyboardHeight - WidgetUtil.dip2px(ct, 40) + "");
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

    public void setActivity(Activity activity) {
        this.activity = activity;
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





    /**
     * 接收消息监听
     */
    private class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
        @Override
        public boolean onReceived(io.rong.imlib.model.Message message, int i) {
            if (message.getContent() instanceof TextMessage) {
                TextMessage tm = (TextMessage) message.getContent();
                final LiveMsg msg = JSON.parseObject(String.valueOf(tm.getExtra()), LiveMsg.class);
                msg.setContent(tm.getContent().toString());
                if (msg.getMsgType().equals(Constant.MSG_TYPE_BASE)) {

                    msgListView.post(new Runnable() {

                        public void run() {
                            msgAdapter.add(msg);
                            msgListView.setSelection(msgAdapter.getCount()-1);

                        }

                    });

                }
                Log.i("msgmsg", msg.toString());
            }


            return false;
        }
    }


    /**
     * 隐藏键盘
     */
    private void hideKeyBoard() {
        KeyBoardUtils.closeKeybord(etChatMsg, ct);
    }
}
