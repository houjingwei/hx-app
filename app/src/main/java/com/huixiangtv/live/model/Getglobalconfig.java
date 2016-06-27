package com.huixiangtv.live.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stone on 16/6/27.
 */
public class Getglobalconfig {

    private String version;
    private String indexStyle;

    public ArrayList<Ad> getList_ad() {
        return list_ad;
    }

    public void setList_ad(ArrayList<Ad> list_ad) {
        this.list_ad = list_ad;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIndexStyle() {
        return indexStyle;
    }

    public void setIndexStyle(String indexStyle) {
        this.indexStyle = indexStyle;
    }

    private ArrayList<Ad> list_ad;

}
