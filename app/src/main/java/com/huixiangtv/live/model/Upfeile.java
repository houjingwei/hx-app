package com.huixiangtv.live.model;

/**
 * Created by hjw on 2016/5/25.
 */
public class Upfeile {

    private String bucket;
    private String sig;
    private String filaName;
    private String appId;
    private String persistenceId;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getFilaName() {
        return filaName;
    }

    public void setFilaName(String filaName) {
        this.filaName = filaName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPersistenceId() {
        return persistenceId;
    }

    public void setPersistenceId(String persistenceId) {
        this.persistenceId = persistenceId;
    }
}