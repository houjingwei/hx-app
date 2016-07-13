package com.huixiangtv.liveshow.model;

import com.huixiangtv.liveshow.utils.PaymentType;

/**
 * Created by Stone on 16/5/26.
 */
public class PayResponseEntity {
    public PayResponseEntity() {
        wxPay = new WxPay();

        this.paymentMethod = PaymentType.none;
    }

    public boolean isPayCompleted;
    public PaymentType paymentMethod;

    public String paySignature;

    public WxPay wxPay;

    public class WxPay {
        public String appId;
        public String prepayId;
        public String nonceStr;
        public String partnerId;
        public String timeStamp;
        public String packageValue;
    }
}
