package com.huixiangtv.liveshow.model;

/**
 * Created by Administrator on 2016/7/25.
 */
public class UnreadCount {

    private int totalCount;
    private int groupUnReadCount;
    private int newFriendUnReadCount;


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getGroupUnReadCount() {
        return groupUnReadCount;
    }

    public void setGroupUnReadCount(int groupUnReadCount) {
        this.groupUnReadCount = groupUnReadCount;
    }

    public int getNewFriendUnReadCount() {
        return newFriendUnReadCount;
    }

    public void setNewFriendUnReadCount(int newFriendUnReadCount) {
        this.newFriendUnReadCount = newFriendUnReadCount;
    }
}
