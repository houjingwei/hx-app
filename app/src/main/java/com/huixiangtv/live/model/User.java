package com.huixiangtv.live.model;


import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class User implements Serializable {
    private String token;//令牌
    private String uid;//用户id
    private String nickName;//昵称
    private String sex;//性别，0女，1男
    private String photo;//头像
    private String birthday;//生日
    private String hots;//人气
    private String loves;//爱心
    private String fans;//粉丝数
    private String orders;
    private String lives;
    private String coins = "0";
    private String signature;
    private String tags;
    private String phoneNum;
    private String status;


    public String getBust() {
        return bust;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBust(String bust) {
        this.bust = bust;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getHip() {
        return hip;
    }

    public void setHip(String hip) {
        this.hip = hip;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public String getImg5() {
        return img5;
    }

    public void setImg5(String img5) {
        this.img5 = img5;
    }

    private String bust;  //胸围
    private String waist; //腰部
    private String hip;  //臀部
    private String height;
    private String weight;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;

    public String getImgLoc1() {
        return imgLoc1;
    }

    public void setImgLoc1(String imgLoc1) {
        this.imgLoc1 = imgLoc1;
    }

    public String getImgLoc2() {
        return imgLoc2;
    }

    public void setImgLoc2(String imgLoc2) {
        this.imgLoc2 = imgLoc2;
    }

    public String getImgLoc3() {
        return imgLoc3;
    }

    public void setImgLoc3(String imgLoc3) {
        this.imgLoc3 = imgLoc3;
    }

    public String getImgLoc4() {
        return imgLoc4;
    }

    public void setImgLoc4(String imgLoc4) {
        this.imgLoc4 = imgLoc4;
    }

    public String getImgLoc5() {
        return imgLoc5;
    }

    public void setImgLoc5(String imgLoc5) {
        this.imgLoc5 = imgLoc5;
    }

    private String imgLoc1;
    private String imgLoc2;
    private String imgLoc3;
    private String imgLoc4;
    private String imgLoc5;

    private Drawable drawableImg1;
    private Drawable drawableImg2;

    public Drawable getDrawableImg3() {
        return drawableImg3;
    }

    public void setDrawableImg3(Drawable drawableImg3) {
        this.drawableImg3 = drawableImg3;
    }

    public Drawable getDrawableImg1() {
        return drawableImg1;
    }

    public void setDrawableImg1(Drawable drawableImg1) {
        this.drawableImg1 = drawableImg1;
    }

    public Drawable getDrawableImg2() {
        return drawableImg2;
    }

    public void setDrawableImg2(Drawable drawableImg2) {
        this.drawableImg2 = drawableImg2;
    }

    public Drawable getDrawableImg4() {
        return drawableImg4;
    }

    public void setDrawableImg4(Drawable drawableImg4) {
        this.drawableImg4 = drawableImg4;
    }

    public Drawable getDrawableImg5() {
        return drawableImg5;
    }

    public void setDrawableImg5(Drawable drawableImg5) {
        this.drawableImg5 = drawableImg5;
    }

    private Drawable drawableImg3;
    private Drawable drawableImg4;
    private Drawable drawableImg5;




    public User() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }



    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getHots() {
        return hots;
    }

    public void setHots(String hots) {
        this.hots = hots;
    }

    public String getLoves() {
        return loves;
    }

    public void setLoves(String loves) {
        this.loves = loves;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getLives() {
        return lives;
    }

    public void setLives(String lives) {
        this.lives = lives;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
