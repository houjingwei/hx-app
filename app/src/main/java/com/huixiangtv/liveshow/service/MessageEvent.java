package com.huixiangtv.liveshow.service;

import com.huixiangtv.liveshow.model.LiveMsg;

/**
 * Created by hjw on 16/7/21.
 */
public class MessageEvent {

    private LiveMsg msg;

    public MessageEvent(LiveMsg msg) {
        this.msg = msg;
    }

    public MessageEvent() {
    }

    public LiveMsg getMsg() {
        return msg;
    }

    public void setMsg(LiveMsg msg) {
        this.msg = msg;
    }
}
