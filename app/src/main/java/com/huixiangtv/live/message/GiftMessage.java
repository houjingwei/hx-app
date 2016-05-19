package com.huixiangtv.live.message;


import com.huixiangtv.live.model.Gift;

/**
 * 礼物消息(type==3)
 * <pre>
 * 人气值计算方法：
 *
 * 1、进入直播间 从后台载入各艺人人气 n
 * 2、收到礼物消息后，根据productId、count、targetUuid进行动态累加
 * n += product.人气值 * count
 * 3、定时执行步骤1，对人气值进行核对
 *
 * </pre>
 * Created by Johnny on 2015/11/30.
 */
public class GiftMessage extends MessageBase {
    private Integer productId;//int 礼物id
    private Integer count;//int 礼物个数，如果是守护表示几个月
    private String tip;//string 如果是喊话表示喊话内容
    private String targetUuid;//string, 接收礼物的用户（艺人）id
    private boolean isBarrage = false;//扩展字段(是否为喊话消息，默认为否)
    private Gift product;//扩展字段(对应礼物)

    public GiftMessage() {
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTargetUuid() {
        return targetUuid;
    }

    public void setTargetUuid(String targetUuid) {
        this.targetUuid = targetUuid;
    }

    public boolean isBarrage() {
        return isBarrage;
    }

    public void setBarrage(boolean barrage) {
        isBarrage = barrage;
    }

    public Gift getProduct() {
        return product;
    }

    public void setProduct(Gift product) {
        this.product = product;
    }
}
