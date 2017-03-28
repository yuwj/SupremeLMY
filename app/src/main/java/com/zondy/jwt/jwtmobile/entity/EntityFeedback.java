package com.zondy.jwt.jwtmobile.entity;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 反馈信息实体,用在通知公告模块.
 * Created by ywj on 2017/3/23 0023.
 */

public class EntityFeedback implements Serializable {
    @SerializedName("fkrjh")
    String fbcUser;
    @SerializedName("fksj")
    String fbcTime;
    @SerializedName("fkxx")
    String fbcStringInfo;
    double fbcLongitude;
    double fbcLatitude;
    @SerializedName("dmtlj")
    String fbcFilePaths;
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
        List<String> datas = new ArrayList<>();
        if (!TextUtils.isEmpty(fbcFilePaths)) {
            String[] d = fbcFilePaths.split(",");
            if (d != null && d.length > 0) {
                for (String s : d) {
                    datas.add(s);
                }
            }
        }
        return datas;
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

    public String getFbcStringInfo() {
        return fbcStringInfo;
    }

    public void setFbcStringInfo(String fbcStringInfo) {
        this.fbcStringInfo = fbcStringInfo;
    }
}
