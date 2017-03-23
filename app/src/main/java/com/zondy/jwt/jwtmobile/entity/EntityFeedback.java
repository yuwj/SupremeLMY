package com.zondy.jwt.jwtmobile.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 反馈信息实体,用在通知公告模块.
 * Created by ywj on 2017/3/23 0023.
 */

public class EntityFeedback implements Serializable {
    String fbcUser;
    String fbcTime;
    double fbcLongitude;
    double fbcLatitude;
    List<String> fbcMediaPaths;

    public double getFbcLatitude() {
        return fbcLatitude;
    }

    public void setFbcLatitude(double fbcLatitude) {
        this.fbcLatitude = fbcLatitude;
    }

    public double getFbcLongitude() {
        return fbcLongitude;
    }

    public void setFbcLongitude(double fbcLongitude) {
        this.fbcLongitude = fbcLongitude;
    }

    public List<String> getFbcMediaPaths() {
        return fbcMediaPaths;
    }

    public void setFbcMediaPaths(List<String> fbcMediaPaths) {
        this.fbcMediaPaths = fbcMediaPaths;
    }

    public String getFbcTime() {
        return fbcTime;
    }

    public void setFbcTime(String fbcTime) {
        this.fbcTime = fbcTime;
    }

    public String getFbcUser() {
        return fbcUser;
    }

    public void setFbcUser(String fbcUser) {
        this.fbcUser = fbcUser;
    }
}
