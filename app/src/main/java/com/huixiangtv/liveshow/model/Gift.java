package com.huixiangtv.liveshow.model;

/**
 * Created by hjw on 16/5/19.
 */
public class Gift {

    private String gid;//礼物id
    private String code;//产品编码
    private String icon;//图标
    private String tag;//标签
    private String name;//名称
    private String price;//价格，20芒果币
    private String animType;//动画类别 为1的时候才显示右边的动画
    private String photo;//图片路径
    private String hots;//增加的人气值

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAnimType() {
        return animType;
    }

    public void setAnimType(String animType) {
        this.animType = animType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getHots() {
        return hots;
    }

    public void setHots(String hots) {
        this.hots = hots;
    }
}
