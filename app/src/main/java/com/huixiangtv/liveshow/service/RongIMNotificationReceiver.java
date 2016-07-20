package com.huixiangtv.liveshow.service;

import android.content.Context;
import android.util.Log;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by hjw on 16/7/20.
 */
public class RongIMNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        Log.i("消息",pushNotificationMessage.getPushContent());
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        Log.i("消息",pushNotificationMessage.getPushContent());
        return false;
    }
}
