package com.zondy.jwt.jwtmobile.entity;

import com.zondy.jwt.jwtmobile.util.GsonUtil;

import java.io.Serializable;

/**
 * 请求布控的反馈信息
 * Created by ywj on 2017/4/12 0012.
 */

public class EntityAskBukfk implements Serializable{
    private String id;
    private String time;

    private String jh;
    private String zzjgdm;
    private String bkid;
    private String description;

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

    public String getJh() {
        return jh;
    }

    public void setJh(String jh) {
        this.jh = jh;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getZzjgdm() {
        return zzjgdm;
    }

    public void setZzjgdm(String zzjgdm) {
        this.zzjgdm = zzjgdm;
    }
    public String toJsonStr(){
        return GsonUtil.bean2Json(this);
    }
}
