package com.huixiangtv.live.model;


import java.io.Serializable;

public class Tag implements Serializable {
    private String tId;
    private String name;

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
