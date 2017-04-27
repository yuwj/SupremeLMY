package com.zondy.jwt.jwtmobile.entity;

import com.zondy.jwt.jwtmobile.util.GsonUtil;

import java.io.Serializable;


/**
 * 请求布控的布控人员特征信息
 * Created by ywj on 2017/4/12 0012.
 */

public class EntityAskZousry implements Serializable {

    private String id;// 'id';
    private String name;//走失人员名字
    private String missdate;//走失日期
    private String height;// '身高';
    private String sex;// '性别';
    private String age;// '年龄';
    private String description;// '特征描述';
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    
    
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMissdate() {
		return missdate;
	}

	public void setMissdate(String missdate) {
		this.missdate = missdate;
	}
	
	

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String toJsonStr(){
        return GsonUtil.bean2Json(this);
    }
}
