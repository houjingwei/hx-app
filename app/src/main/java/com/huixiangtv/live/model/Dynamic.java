package com.huixiangtv.live.model;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stone on 16/7/1.
 */
public class Dynamic {
    private String uid;
    private String dynamicId;
    private String nickName;
    private String photo;
    private String praiseCount;
    private String commentCount;
    private String date;
    private String content;
    private String videoCover;
    private String videoURL;
    private String type;
    private String lon;
    private String lat;
    private String address;




    private List<DynamicImage> images = new ArrayList<>();
    private List<DynamicComment> comments= new ArrayList<>();
    private List<DynamicpPraise> praises= new ArrayList<>();




    //用于计算的属性
    private String lastDate = "";
    private String month = "";
    private String day = "";
    private boolean marginTop = false;

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(boolean marginTop) {
        this.marginTop = marginTop;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(String praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<DynamicImage> getImages() {
        return images;
    }

    public void setImages(List<DynamicImage> images) {
        this.images = images;
    }

    public List<DynamicComment> getComments() {
        return comments;
    }

    public void setComments(List<DynamicComment> comments) {
        this.comments = comments;
    }

    public List<DynamicpPraise> getPraises() {
        return praises;
    }

    public void setPraises(List<DynamicpPraise> praises) {
        this.praises = praises;
    }


}