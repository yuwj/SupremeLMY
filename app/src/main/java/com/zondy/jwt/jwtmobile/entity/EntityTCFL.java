package com.zondy.jwt.jwtmobile.entity;

import java.io.Serializable;

/**
 * Created by sheep on 2017/1/19.
 */

public class EntityTCFL implements Serializable {
    private int id;//classCode
    private int layerid;//classCode
    private String layername;//largeClass
    private String mc;//largeClass
    private int parentid;//partentClassCode

//    String largeClass;
//    String middleClass;
//    String tinyClass;
//    String classCode;
//    String partentClassCode;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public int getLayerid() {
        return layerid;
    }

    public void setLayerid(int layerid) {
        this.layerid = layerid;
    }

    public String getLayername() {
        return layername;
    }

    public void setLayername(String layername) {
        this.layername = layername;
    }
}
