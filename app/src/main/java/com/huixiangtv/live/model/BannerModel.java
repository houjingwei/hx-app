package com.huixiangtv.live.model;

import android.os.Parcel;

/**
 * Created by Stone on 16/5/20.
 */
public class BannerModel extends ImageModel {


    public String title;
    public String image;
    public String target;
    public String tip1;
    public String tip2;
    public String tip3;

    public BannerModel
            () {
    }

    protected BannerModel(Parcel in) {
        super(in);
        title = in.readString();
        image = in.readString();
        target = in.readString();
        tip1 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(target);
        dest.writeString(tip1);
        dest.writeString(tip2);
        dest.writeString(tip3);

    }
}
