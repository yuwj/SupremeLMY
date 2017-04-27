package com.zondy.jwt.jwtmobile.entity;

import com.zondy.jwt.jwtmobile.util.GsonUtil;

import java.io.Serializable;

/**
 * 请求布控的布控人员特征信息
 * Created by ywj on 2017/4/12 0012.
 */

public class EntityAskForBukry implements Serializable {

    private String id;// 'id';
    private String minheight;// '最小身高';
    private String maxheight;// '最大身高';
    private String sex;// '性别';
    private String minage;// '最小年龄';
    private String maxage;// '最大年龄';
    private String description;// '布控人员描述';
    private String bkid;// '对应的布控id';

    public String getBkid() {
        return bkid;
    }

    public void setBkid(String bkid) {
        this.bkid = bkid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaxage() {
        return maxage;
    }

    public void setMaxage(String maxage) {
        this.maxage = maxage;
    }

    public String getMaxheight() {
        return maxheight;
    }

    public void setMaxheight(String maxheight) {
        this.maxheight = maxheight;
    }

    public String getMinage() {
        return minage;
    }

    public void setMinage(String minage) {
        this.minage = minage;
    }

    public String getMinheight() {
        return minheight;
    }

    public void setMinheight(String minheight) {
        this.minheight = minheight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String toJsonStr(){
        return GsonUtil.bean2Json(this);
    }
}
