package com.huixiangtv.liveshow.pay.alipay;

/**
 * Created by Stone on 16/5/26.
 */

public interface OnPayListener {
    void onSucceed(PayResult payResult);

    void onWait(PayResult payResult);

    void onFailed(PayResult payResult);
}