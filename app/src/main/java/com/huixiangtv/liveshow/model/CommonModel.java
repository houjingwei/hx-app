package com.huixiangtv.liveshow.model;

import android.os.Parcel;

/**
 * Created by apple on 16/5/14.
 * 公共实体类 Parcelable
 */
public class CommonModel extends ImageModel {

    //do something

    public int img_id;
    public String lId;
    public long count;
    public String uid;
    public String role;
    public String nickName;
    public String photo;
    public String hotValue;
    public String loveCount;
    public String height;
    public String weight;
    public String bwh;
    public String tags;

    public CommonModel() {


    }


    protected CommonModel(Parcel in) {
        super(in);
        img_id = in.readInt();
        lId = in.readString();
        count = in.readLong();
        uid = in.readString();
        role = in.readString();
        nickName = in.readString();
        photo = in.readString();
        loveCount = in.readString();
        hotValue = in.readString();
        weight = in.readString();
        height = in.readString();
        bwh = in.readString();
        tags = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(img_id);
        dest.writeLong(count);
        dest.writeString(uid);
        dest.writeString(role);
        dest.writeString(nickName);
        dest.writeString(photo);
        dest.writeString(loveCount);
        dest.writeString(height);
        dest.writeString(weight);
        dest.writeString(hotValue);
        dest.writeString(bwh);
        dest.writeString(tags);
    }

    @Override
    public String toString() {
        return "CommonModel{" +
                "img_id=" + img_id +
                ", lId='" + lId + '\'' +
                ", count=" + count +
                ", uid='" + uid + '\'' +
                ", role='" + role + '\'' +
                ", nickName='" + nickName + '\'' +
                ", photo='" + photo + '\'' +
                ", hotValue='" + hotValue + '\'' +
                ", loveCount='" + loveCount + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", bwh='" + bwh + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }

}
