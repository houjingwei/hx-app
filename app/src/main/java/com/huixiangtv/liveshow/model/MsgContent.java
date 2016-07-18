package com.huixiangtv.liveshow.model;

import android.os.Parcel;

import io.rong.imlib.model.MessageContent;

/**
 * Created by hjw on 16/7/18.
 */
public class MsgContent extends MessageContent {
    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
