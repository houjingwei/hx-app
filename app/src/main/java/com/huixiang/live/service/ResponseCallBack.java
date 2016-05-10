package com.huixiang.live.service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Johnny on 2015/11/19.
 */
public abstract class ResponseCallBack<T> implements Serializable {

    public ResponseCallBack() {
    }

    public void onSuccess(T data) {
    }

    public void onSuccessList(List<T> data) {
    }

    public void onFailure(ServiceException e) {
    }
}
