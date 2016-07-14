package com.huixiangtv.liveshow.model;

/**
 * Created by houjingwei on 2016/7/14.
 */
public class Image {

    private String path;
    private String rate;

    public Image(String img, String rate) {
        this.path = img;
        this.rate = rate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
