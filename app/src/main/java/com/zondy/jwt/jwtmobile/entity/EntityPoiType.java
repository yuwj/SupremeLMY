package com.zondy.jwt.jwtmobile.entity;

import java.io.Serializable;

/**
 * Created by ywj on 2017/3/24 0024.
 */

public class EntityPoiType implements Serializable {
    String largeClass;
    String middleClass;
    String tinyClass;
    String classCode;

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getLargeClass() {
        return largeClass;
    }

    public void setLargeClass(String largeClass) {
        this.largeClass = largeClass;
    }

    public String getMiddleClass() {
        return middleClass;
    }

    public void setMiddleClass(String middleClass) {
        this.middleClass = middleClass;
    }

    public String getTinyClass() {
        return tinyClass;
    }

    public void setTinyClass(String tinyClass) {
        this.tinyClass = tinyClass;
    }
}
