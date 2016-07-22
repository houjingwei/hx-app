package com.huixiangtv.liveshow.service;

import android.content.Context;
import android.util.Log;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by hjw on 16/7/20.
 */
public class RongIMNotificationReceiver extends PushMessageReceiver {

    /**
     * 用来接收服务器发来的通知栏消息(消息到达客户端时触发)
     * @param context
     * @param pushNotificationMessage
     * @return
     */
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        Log.i("pushMsg",pushNotificationMessage.getPushContent());
        return true;
    }


    /**
     * 是在⽤户点击通知栏消息时触发
     * @param context
     * @param pushNotificationMessage
     * @return
     */
    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        Log.i("pushMsg",pushNotificationMessage.getPushContent());
        return true;
    }
}
