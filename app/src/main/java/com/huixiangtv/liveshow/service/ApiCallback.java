package com.huixiangtv.liveshow.service;

/**
 * Created by Johnny on 2015/12/17.
 */
public abstract class ApiCallback<T> {

    public abstract void onSuccess(T data);

    public void onFailure(ServiceException e) {

    }
}
