package com.huixiangtv.liveshow.message;

/**
 * 基础消息实体
 */
public class MessageBase {

    protected Integer type;//int 消息类型(普通消息:1或null;进入直播间消息(type==2);礼物消息(type==3))
    protected String uuid;// string, 产生消息的用户id
    protected String nickName;//string, 产生消息的用户昵称
    protected String avatar;//string, 产生消息的用户头像

    public MessageBase() {
    }

    public Integer getType() {
        if(null==type){
            //表示普通消息
            type=1;
        }
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
