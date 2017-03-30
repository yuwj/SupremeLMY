package com.zondy.jwt.jwtmobile.entity;

import com.zondy.jwt.jwtmobile.util.GsonUtil;

import java.util.List;

/**
 * 预测方格
 * Created by ywj on 2017/3/29 0029.
 */

public class EntityPredict {
    String deptName;
    String date;
    List<EntityPred> predResults;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<EntityPred> getPredResults() {
        return predResults;
    }

    public void setPredResults(List<EntityPred> predResults) {
        this.predResults = predResults;
    }


public class EntityPred {

    String realDate;//20170228
    String gridNum;//1144
    String patrolPeroidId;//0
    String pdId;//91
    String intervalUpper;//"0000"
    String intervalLower;//"2400"
    String crimeGroupId;//null
    EntityGridInfo gridInfo;
    String duration;//null
    String checked;//false
    String predProb;//0.105

    public String getJsonStr(){
        return GsonUtil.bean2Json(this);
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getCrimeGroupId() {
        return crimeGroupId;
    }

    public void setCrimeGroupId(String crimeGroupId) {
        this.crimeGroupId = crimeGroupId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public EntityGridInfo getGridInfo() {
        return gridInfo;
    }

    public void setGridInfo(EntityGridInfo gridInfo) {
        this.gridInfo = gridInfo;
    }

    public String getGridNum() {
        return gridNum;
    }

    public void setGridNum(String gridNum) {
        this.gridNum = gridNum;
    }

    public String getIntervalLower() {
        return intervalLower;
    }

    public void setIntervalLower(String intervalLower) {
        this.intervalLower = intervalLower;
    }

    public String getIntervalUpper() {
        return intervalUpper;
    }

    public void setIntervalUpper(String intervalUpper) {
        this.intervalUpper = intervalUpper;
    }

    public String getPatrolPeroidId() {
        return patrolPeroidId;
    }

    public void setPatrolPeroidId(String patrolPeroidId) {
        this.patrolPeroidId = patrolPeroidId;
    }

    public String getPdId() {
        return pdId;
    }

    public void setPdId(String pdId) {
        this.pdId = pdId;
    }

    public String getPredProb() {
        return predProb;
    }

    public void setPredProb(String predProb) {
        this.predProb = predProb;
    }

    public String getRealDate() {
        return realDate;
    }

    public void setRealDate(String realDate) {
        this.realDate = realDate;
    }
}

public class EntityGridInfo {
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
}}