package com.huixiang.live.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by STONE on 16/5/14.
 */
public class Model implements Parcelable {
    public Model() {
        id = 0L;
        title = "";
    }

    public Model(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Model(Long id, String title, int tag) {
        this.id = id;
        this.title = title;
        this.tag = tag;
    }

    public Model(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Long id;
    public String title;
    public String description;
    public int tag;

    protected Model(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        tag = in.readInt();
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    @Override
    public String toString() {
        return "id:" + id + " title:" + title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeInt(tag);
    }
}