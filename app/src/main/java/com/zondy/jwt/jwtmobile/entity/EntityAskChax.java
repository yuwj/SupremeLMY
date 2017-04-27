package com.zondy.jwt.jwtmobile.entity;


import com.zondy.jwt.jwtmobile.util.GsonUtil;

/**
 * 请求布控
 * Created by ywj on 2017/4/12 0012.
 */

public class EntityAskChax extends EntityAskService {
    private String id;//'id';
    private String jh;//'请求人警号';
    private String jqbh;//'关联的警情编号';
    private String x;//'请求布控的经度';
    private String y;//'请求布控的纬度';
    private String address;//'请求布控的地址描述';
    private String zzjgdm;//'请求人单位编码';
    private String cxyy;//查询原因
    private String cxnr;//查询内容

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



	public String getCxyy() {
		return cxyy;
	}



	public void setCxyy(String cxyy) {
		this.cxyy = cxyy;
	}



	public String getCxnr() {
		return cxnr;
	}



	public void setCxnr(String cxnr) {
		this.cxnr = cxnr;
	}



	public String toJsonStr(){
        return GsonUtil.bean2Json(this);
    }
}
