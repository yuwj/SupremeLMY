package com.zondy.jwt.jwtmobile.entity;


import com.zondy.jwt.jwtmobile.util.GsonUtil;

/**
 * 请求增援
 * Created by ywj on 2017/4/12 0012.
 */

public class EntityAskZengy extends EntityAskService {
    private String id;//'id';
    private String jh;//'请求人警号';
    private String jqbh;//'关联的警情编号';
    private String x;//'增援的经度';
    private String y;//'增援的纬度';
    private String address;//'请求增援的地址描述';
    private String zzjgdm;//'请求人单位编码';
    private String dmtlj;//'多媒体路径';
    private String description;//'增援描述';
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZzjgdm() {
		return zzjgdm;
	}
	public void setZzjgdm(String zzjgdm) {
		this.zzjgdm = zzjgdm;
	}
	public String getDmtlj() {
		return dmtlj;
	}
	public void setDmtlj(String dmtlj) {
		this.dmtlj = dmtlj;
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
