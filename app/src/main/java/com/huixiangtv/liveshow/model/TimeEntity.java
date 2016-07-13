package com.huixiangtv.liveshow.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 16/5/14.
 */
public class TimeEntity extends Model implements Parcelable {
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time="";

    public TimeEntity() {
        super();
    }

    protected TimeEntity(Parcel in) {
        super(in);
        time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TimeEntity> CREATOR = new Creator<TimeEntity>() {
        @Override
        public TimeEntity createFromParcel(Parcel in) {
            return new TimeEntity(in);
        }

        @Override
        public TimeEntity[] newArray(int size) {
            return new TimeEntity[size];
        }
    };
}
