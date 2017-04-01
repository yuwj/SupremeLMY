package com.zondy.jwt.jwtmobile.entity;

import com.zondy.jwt.jwtmobile.util.GsonUtil;

/**
 * Created by ywj on 2017/4/1 0001.
 */

public
class EntityPredictGridInfo {
    String pdId;
    String gridNum;//1144
    String p1X;//119.000694
    String p1Y;//33.579838
    String p2X;//119.00177
    String p2Y;//33.57894
    String centX;//119.00123
    String centY;//33.579388
    String subDistrictId;//0

    public String getCentX() {
        return centX;
    }

    public void setCentX(String centX) {
        this.centX = centX;
    }

    public String getCentY() {
        return centY;
    }

    public void setCentY(String centY) {
        this.centY = centY;
    }

    public String getGridNum() {
        return gridNum;
    }

    public void setGridNum(String gridNum) {
        this.gridNum = gridNum;
    }

    public String getP1X() {
        return p1X;
    }

    public void setP1X(String p1X) {
        this.p1X = p1X;
    }

    public String getP1Y() {
        return p1Y;
    }

    public void setP1Y(String p1Y) {
        this.p1Y = p1Y;
    }

    public String getP2X() {
        return p2X;
    }

    public void setP2X(String p2X) {
        this.p2X = p2X;
    }

    public String getP2Y() {
        return p2Y;
    }

    public void setP2Y(String p2Y) {
        this.p2Y = p2Y;
    }

    public String getPdId() {
        return pdId;
    }

    public void setPdId(String pdId) {
        this.pdId = pdId;
    }

    public String getSubDistrictId() {
        return subDistrictId;
    }

    public void setSubDistrictId(String subDistrictId) {
        this.subDistrictId = subDistrictId;
    }

    public String getJsonStr(){
        return GsonUtil.bean2Json(this);
    }
}
