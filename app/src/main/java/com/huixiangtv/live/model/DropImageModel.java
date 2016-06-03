package com.huixiangtv.live.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Stone on 16/5/31.
 */
public class DropImageModel implements Serializable {
    private Drawable iconId;
    private String name;

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    private  int isFinish;

    public DropImageModel() {

    }

    public DropImageModel(DropImageModel bean) {
        this.iconId = bean.getIconId();
        this.name = bean.getName();
    }

    /**
     * @return the iconId
     */
    public Drawable  getIconId() {
        return iconId;
    }

    /**
     * @param iconId
     *            the iconId to set
     */
    public void setIconId(Drawable iconId) {
        this.iconId = iconId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }





}
