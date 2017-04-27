package com.zondy.jwt.jwtmobile.entity;

import com.zondy.jwt.jwtmobile.util.GsonUtil;

import java.io.Serializable;

/**
 * 请求布控的布控车辆特征信息
 * Created by ywj on 2017/4/12 0012.
 */

public class EntityAskForBukcl implements Serializable {
    private String cx; //车型(小轿车,卡车等)
    private String cp;//车牌
    private String csys; //车身颜色
    private String description;  //车辆描述
    private String id;  //id
    private String bkid;//对应的布控请求id


    public String getBkid() {
        return bkid;
    }

    public void setBkid(String bkid) {
        this.bkid = bkid;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCsys() {
        return csys;
    }

    public void setCsys(String csys) {
        this.csys = csys;
    }

    public String getCx() {
        return cx;
    }

    public void setCx(String cx) {
        this.cx = cx;
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

    public String toJsonStr(){
        return GsonUtil.bean2Json(this);
    }
}
