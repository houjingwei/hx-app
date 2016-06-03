package com.huixiangtv.live.model;

import android.os.Parcel;

/**
 * Created by Stone on 16/5/27.
 */
public class WxRequest extends ImageModel {

    public String appid;
    public String mch_id;
    public String device_info;
    public String nonce_str;
    public String sign;
    public String body;
    public String detail;
    public String attach;
    public String out_trade_no;
    public String fee_type;
    public String total_fee;
    public String spbill_create_ip;
    public String time_start;
    public String time_expire;
    public String goods_tag;
    public String notify_url;
    public String trade_type;
    public String limit_pay;

    public WxRequest(Parcel in) {
        super(in);
        this.appid = in.readString();;
        this.mch_id = in.readString();;
        this.device_info = in.readString();;
        this.nonce_str = in.readString();;
        this.sign = in.readString();;
        this.body = in.readString();;
        this.detail = in.readString();;
        this.attach = in.readString();;
        this.out_trade_no = in.readString();;
        this.fee_type = in.readString();;
        this.total_fee = in.readString();;
        this.spbill_create_ip = in.readString();;
        this.time_start = in.readString();;
        this.time_expire = in.readString();;
        this.goods_tag = in.readString();;
        this.notify_url = in.readString();;
        this.trade_type = in.readString();;
        this.limit_pay = in.readString();;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(appid);
        dest.writeString(mch_id);
        dest.writeString(device_info);
        dest.writeString(nonce_str);
        dest.writeString(sign);
        dest.writeString(body);
        dest.writeString(detail);
        dest.writeString(attach);
        dest.writeString(out_trade_no);
        dest.writeString(fee_type);
        dest.writeString(total_fee);
        dest.writeString(spbill_create_ip);
        dest.writeString(time_start);
        dest.writeString(time_expire);
        dest.writeString(goods_tag);
        dest.writeString(notify_url);
        dest.writeString(trade_type);
        dest.writeString(limit_pay);

    }

    @Override
    public String toString() {
        return "WxRequest{" +
                "appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", device_info='" + device_info + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", sign='" + sign + '\'' +
                ", body='" + body + '\'' +
                ", detail='" + detail + '\'' +
                ", attach='" + attach + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", fee_type='" + fee_type + '\'' +
                ", total_fee='" + total_fee + '\'' +
                ", spbill_create_ip='" + spbill_create_ip + '\'' +
                ", time_start='" + time_start + '\'' +
                ", time_expire='" + time_expire + '\'' +
                ", goods_tag='" + goods_tag + '\'' +
                ", notify_url='" + notify_url + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", limit_pay='" + limit_pay + '\'' +
                '}';
    }
}
