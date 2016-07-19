package com.huixiangtv.liveshow.model;

/**
 * Created by hjw on 16/7/19.
 */
public class Friend {

    private String uid;
    private String nickName;
    private String photo;
    private String sex;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Friend(String uid, String nickName, String photo, String sex) {
        this.uid = uid;
        this.nickName = nickName;
        this.photo = photo;
        this.sex = sex;
    }

    public Friend() {
    }
}
