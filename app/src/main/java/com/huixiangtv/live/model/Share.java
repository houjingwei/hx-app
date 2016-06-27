package com.huixiangtv.live.model;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by Stone on 16/6/23.
 */
public class Share {

    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    private String title;
    private String dec;
    private String cover;
    private SHARE_MEDIA platForm;

    public SHARE_MEDIA getPlatForm() {
        return platForm;
    }

    public void setPlatForm(SHARE_MEDIA platForm) {
        this.platForm = platForm;
    }
}
