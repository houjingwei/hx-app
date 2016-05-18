package com.huixiangtv.live.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 16/5/13.
 */
public class PositionAdvertBO {

    public PositionAdvertBO() {
    }
    @SerializedName("adImgPath")
    private String adImgPath;
    public String getAdImgPath() {
        return this.adImgPath;
    }
    public void setAdImgPath(String adImgPath) {
        this.adImgPath = adImgPath;
    }

    public String toString() {
        return "PositionAdvertBO [adImgPath=" + this.adImgPath + "]";
    }
}
