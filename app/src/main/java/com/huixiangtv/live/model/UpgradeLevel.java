package com.huixiangtv.live.model;

import android.os.Parcel;

/**
 * Created by Stone on 16/5/23.
 */
public class UpgradeLevel extends ImageModel {

    public String alert;
    public String desc;
    public String appUrl;
    public String review;

    @Override
    public String toString() {
        return "UpgradeLevel{" +
                "alert='" + alert + '\'' +
                ", desc='" + desc + '\'' +
                ", appUrl='" + appUrl + '\'' +
                ", review='" + review + '\'' +
                '}';
    }

    public UpgradeLevel() {


    }


    protected UpgradeLevel(Parcel in) {
        super(in);

        alert = in.readString();
        desc = in.readString();
        appUrl = in.readString();
        review = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(alert);
        dest.writeString(appUrl);
        dest.writeString(desc);
        dest.writeString(review);


    }

}
