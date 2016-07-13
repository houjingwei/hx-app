package com.huixiangtv.liveshow.model;

/**
 * Created by Stone on 16/6/27.
 */
public class Ad {

    private String type;
    private String url;
    private float width;
    private float height;
    private String image;
    private int showtime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getShowtime() {
        return showtime;
    }

    public void setShowtime(int showtime) {
        this.showtime = showtime;
    }
}
