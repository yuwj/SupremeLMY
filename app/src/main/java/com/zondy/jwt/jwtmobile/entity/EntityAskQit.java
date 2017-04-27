package com.zondy.jwt.jwtmobile.entity;


import com.zondy.jwt.jwtmobile.util.GsonUtil;

/**
 * 请求其他
 * Created by ywj on 2017/4/12 0012.
 */

public class EntityAskQit extends EntityAskService {
    private String id;//'id';
    private String jh;//'请求人警号';
    private String jqbh;//'关联的警情编号';
    private String x;//'请求其他的经度';
    private String y;//'请求其他的纬度';
    private String address;//'请求其他的地址描述';
    private String zzjgdm;//'请求人单位编码';
    String description;//其他信息描述

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getJqbh() {
        return jqbh;
    }

    public void setJqbh(String jqbh) {
        this.jqbh = jqbh;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZzjgdm() {
        return zzjgdm;
    }

    public void setZzjgdm(String zzjgdm) {
        this.zzjgdm = zzjgdm;
    }

    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toJsonStr(){
        return GsonUtil.bean2Json(this);
    }
}
