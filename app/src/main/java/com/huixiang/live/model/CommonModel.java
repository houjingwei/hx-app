package com.huixiang.live.model;

import android.os.Parcel;

/**
 * Created by apple on 16/5/14.
 * 公共实体类 Parcelable
 */
public class CommonModel extends ImageModel{

     //do something

     public int img_id;
     public CommonModel()
     {


     }


     protected CommonModel(Parcel in) {
          super(in);
          img_id = in.readInt();
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
          super.writeToParcel(dest, flags);
          dest.writeInt(img_id);
     }

     @Override
     public String toString() {
          return super.toString() + " img_id:" + img_id;
     }

}
