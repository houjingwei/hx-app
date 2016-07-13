package com.huixiangtv.live.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huixiangtv.live.Api;
import com.huixiangtv.live.App;
import com.huixiangtv.live.R;
import com.huixiangtv.live.adapter.LiveMsgAdapter;
import com.huixiangtv.live.model.BasePayent;
import com.huixiangtv.live.model.Live;
import com.huixiangtv.live.model.LiveMsg;
import com.huixiangtv.live.model.ShoutGift;
import com.huixiangtv.live.service.ApiCallback;
import com.huixiangtv.live.service.RequestUtils;
import com.huixiangtv.live.service.ResponseCallBack;
import com.huixiangtv.live.service.ServiceException;
import com.huixiangtv.live.utils.CommonHelper;
import com.huixiangtv.live.utils.KeyBoardUtils;

import java.util.HashMap;
import java.util.Map;


public class InputWindow extends BasePopupWindow implements OnClickListener {

    private static final String TAG = "InputWindow";
    private Activity context;
    private View view;
    ScrollView bottom;

    private TextView tvHot;
    private ListView msgListView;
    private Live live;
    private LiveMsgAdapter  msgAdapter;



    private RelativeLayout switchWrapper;
    private RelativeLayout switchTrigger;
    private TextView switchLabel;
    RelativeLayout rlChatView;
    EditText etChatMsg;
    private ShoutGift shoutGift;





    //判断键盘弹出或收回的高度值
    int viewHeight = 0;




    private Animation mShowAnimation;
    private Animation mHideAnimation;


    private void initAnimation() {
        mShowAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
        mHideAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_down);
    }

    public InputWindow(Activity context, int width, int height, Live live, TextView tvHot, LiveMsgAdapter msgAdapter, ListView msgListView) {
        this.context = context;
        this.tvHot = tvHot;
        this.live = live;
        this.msgListView = msgListView;
        this.msgAdapter = msgAdapter;
        super.setWidth(width);
        super.setHeight(height);
        initPopUpWindow();
        initData();
        initAnimation();
        show();
    }

    private void initData() {
        //初始化喊话礼物
        initShoutGift(new ApiCallback<ShoutGift>() {
            @Override
            public void onSuccess(ShoutGift data) {
                shoutGift = data;
            }
        });
    }

    private void show() {
        bottom.setAnimation(mShowAnimation);
        bottom.setVisibility(View.VISIBLE);
    }





    public void initPopUpWindow() {
        try {
            view = RelativeLayout.inflate(context, R.layout.pop_input, null);
            bottom = (ScrollView) view.findViewById(R.id.bottom);
            view.findViewById(R.id.pop_layout).setOnClickListener(this);
            rlChatView = (RelativeLayout) view.findViewById(R.id.rlChatView);
            etChatMsg = (EditText) view.findViewById(R.id.etChatMsg);
            switchWrapper = (RelativeLayout) view.findViewById(R.id.switchWrapper);
            switchTrigger = (RelativeLayout) view.findViewById(R.id.switchTrigger);
            switchLabel = (TextView) view.findViewById(R.id.switchLabel);
            switchWrapper.setOnClickListener(this);//添加switch喊话事件
            //喊话状态
            GradientDrawable gd = (GradientDrawable) switchWrapper.getBackground();
            gd.setColor(context.getResources().getColor(R.color.gray));
            switchWrapper.setBackgroundDrawable(gd);
            switchLabel.setTextColor(context.getResources().getColor(R.color.gray));
            etChatMsg.setHint("和大家一起聊~");
            view.findViewById(R.id.tvSendMsg).setOnClickListener(this);
            etChatMsg = (EditText) view.findViewById(R.id.etChatMsg);
            etChatMsg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId== 6 )
                    {
                        sendMessage();
                    }
                    return false;
                }
            });



            super.setFocusable(true);
            super.setOutsideTouchable(true);
            super.setBackgroundDrawable(new BitmapDrawable());
            this.setContentView(view);
            this.setWidth(LayoutParams.FILL_PARENT);
            this.setHeight(LayoutParams.FILL_PARENT);
            this.setFocusable(true);
            this.setAnimationStyle(R.style.popupAnimation);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_layout:
                dismiss();
                break;
            case R.id.tvSendMsg:
                sendMessage();
                break;
            case R.id.switchWrapper:
                if (null == App.getLoginUser()) {
                    CommonHelper.showLoginPopWindow(context, R.id.liveMain,null);
                    return;
                }
                changeHanhua();
                break;
            default:
                break;
        }
    }


    /**
     * 发送消息
     */
    private void sendMessage() {
        if (TextUtils.isEmpty(etChatMsg.getText().toString())) {
            CommonHelper.showTip(context, "不可以发送空消息哦~");
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
                    dismiss();
                }

                @Override
                public void onFailure(ServiceException e) {
                    super.onFailure(e);
                    CommonHelper.showTip(context, e.getMessage());
                }
            }, String.class);
        }
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
                CommonHelper.showTip(context, e.getMessage());
            }
        }, ShoutGift.class);
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
                    //更新金币数量
                    App.userCoin = data.getAmount()+"";

                    int old = Integer.parseInt(tvHot.getText().toString());
                    int addhot = Integer.parseInt(data.getHots());
                    String loves = old+addhot+"";
                    tvHot.setText(loves);
                    dismiss();
                }
            }

            @Override
            public void onFailure(ServiceException e) {
                super.onFailure(e);
                CommonHelper.showTip(context, e.getMessage());
            }
        }, BasePayent.class);
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
            gd.setColor(context.getResources().getColor(R.color.gray));
            switchLabel.setTextColor(context.getResources().getColor(R.color.gray));
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
            gd.setColor(context.getResources().getColor(R.color.mainColor));
            switchLabel.setTextColor(context.getResources().getColor(R.color.mainColor));
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




    boolean isFirst = true;
    public void showKeyBoard() {
        KeyBoardUtils.openKeybord(etChatMsg,context);

        etChatMsg.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener(){
                    @Override
                    public void onGlobalLayout()
                    {

                        int heightDiff = etChatMsg.getRootView().getHeight() - etChatMsg.getHeight();
                        if(viewHeight==0){
                            viewHeight = heightDiff;
                        }
                        Log.v(TAG, "heightDiff = " + heightDiff);


                        if(heightDiff!=viewHeight){
                            isFirst = !isFirst;
                            Log.v(TAG, "键盘弹出状态");
                        }else if(!isFirst){
                            Log.v(TAG, "键盘收起状态");
                            dismiss();
                        }
                    }
                });
    }
}
