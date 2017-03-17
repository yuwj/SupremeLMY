package com.zondy.jwt.jwtmobile.entity;

import com.zondy.jwt.jwtmobile.util.GsonUtil;

import java.io.Serializable;

/**
 * 轨迹的基础类
 * Created by ywj on 2017/3/16 0016.
 */

public class EntityBaseGuij implements Serializable{


    public static final int GUIJI_LVG = 1;//旅馆轨迹
    public static final int GUIJI_WANGB = 2;//网吧轨迹

    String startTime;
    String endTime;
    String positionName;
    String address;
    double longitude;
    double latitude;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getJsonStr(){
        return GsonUtil.bean2Json(this);
    }

}
