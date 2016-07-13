package com.huixiangtv.liveshow.model;

import java.util.List;

/**
 * Created by hjw on 16/5/17.
 */
public class HistoryMsg {
    private String online;
    private List<ChatMessage> lastMsg;

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public List<ChatMessage> getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(List<ChatMessage> lastMsg) {
        this.lastMsg = lastMsg;
    }
}
