package com.huixiangtv.liveshow.model;

/**
 * Created by Stone on 16/7/1.
 */
public class Praise {
    private String uid;

    public long getDynamicid() {
        return dynamicid;
    }

    public void setDynamicid(long dynamicid) {
        this.dynamicid = dynamicid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    private long dynamicid;
    private String photo;
    private String nickName;

}
