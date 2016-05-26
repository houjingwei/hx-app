package com.huixiangtv.live.pay.alipay;

/**
 * Created by Stone on 16/5/26.
 */

public interface OnPayListener {
    public void onSucceed(PayResult payResult);

    public void onWait(PayResult payResult);

    public void onFailed(PayResult payResult);
}