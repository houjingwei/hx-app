package com.huixiangtv.live.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stone on 16/5/14.
 */
public class ImageModel extends TimeEntity implements Parcelable {
    public ImageModel() {
        this(0L, null, null);
    }

    public ImageModel(String title, int tag) {
        this.title = title;
        this.tag = tag;
        imgUrlsList = new ArrayList<String>();
    }

    public ImageModel(Long id, String title, String iconUrl) {
        this.iconUrl = iconUrl;
        this.id = id;
        this.title = title;
        imgUrlsList = new ArrayList<String>();
    }

    public String iconUrl;

    public List<String> imgUrlsList;

    protected ImageModel(Parcel in) {
        super(in);
        iconUrl = in.readString();
        imgUrlsList = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(iconUrl);
        dest.writeStringList(imgUrlsList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    @Override
    public String toString() {
        return super.toString() + " iconUrl:" + iconUrl;
    }
}
