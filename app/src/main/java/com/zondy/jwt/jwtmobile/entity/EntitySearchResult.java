package com.zondy.jwt.jwtmobile.entity;

import com.google.gson.annotations.SerializedName;
import com.zondy.jwt.jwtmobile.util.GsonUtil;

import java.io.Serializable;

/**
 * Created by sheep on 2016/12/28.
 */

public class EntitySearchResult implements Serializable {
    private int imageResourceID;
    private String mc;
    private String dz;
    private String distance;
    private String rs;
    private String dmtlj;
    @SerializedName("x")
    private double longitude;
    @SerializedName("y")
    private double latitude;

    public EntitySearchResult() {

    }
    public EntitySearchResult(String mc, int imageResourceID, String dz, String jl, String rs) {
        this.mc = mc;
        this.imageResourceID = imageResourceID;
        this.dz = dz;
        this.distance = jl;
        this.rs = rs;
    }

    public String getDmtlj() {
        return dmtlj;
    }

    public void setDmtlj(String dmtlj) {
        this.dmtlj = dmtlj;
    }

    public int getImageResourceID() {
        return imageResourceID;
    }

    public void setImageResourceID(int imageResourceID) {
        this.imageResourceID = imageResourceID;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
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

    public String getJsonStr(){
        return GsonUtil.bean2Json(this);
    }
}
