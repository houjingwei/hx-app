package com.huixiangtv.live.model;

import android.os.Parcel;

/**
 * Created by Stone on 16/5/20.
 */
public class AccountBindInfoModel  extends ImageModel  {


    public String phone;
    public String qq;
    public String wx;
    public String wb;
    public AccountBindInfoModel(){}
    protected AccountBindInfoModel(Parcel in) {
        super(in);
        phone = in.readString();
        qq = in.readString();
        wx = in.readString();
        wb = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(phone);
        dest.writeString(qq);
        dest.writeString(wx);
        dest.writeString(wb);

    }
}
