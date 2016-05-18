package com.huixiangtv.live.model;

/**
 * Created by hjw on 16/5/17.
 */
public class LiveChatMsg {

    private String userName;
    private String msg;
    private String type;

    public LiveChatMsg(String userName, String msg, String type) {
        this.userName = userName;
        this.msg = msg;
        this.type = type;
    }

    public LiveChatMsg() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
