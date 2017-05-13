package com.zondy.jwt.jwtmobile.entity;

import com.zondy.jwt.jwtmobile.util.GsonUtil;

/**
 * Created by ywj on 2017/4/1 0001.
 */

public class EntityPred {

    String realDate;//20170228
    String gridNum;//1144
    String patrolPeroidId;//0
    String pdId;//91
    String intervalUpper;//"0000"
    String intervalLower;//"2400"
    String crimeGroupId;//null
    EntityPredictGridInfo gridInfo;
    String duration;//null
    String checked;//false
    String predProb;//0.105

    public String isChecked() {
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

    public EntityPredictGridInfo getGridInfo() {
        return gridInfo;
    }

    public void setGridInfo(EntityPredictGridInfo gridInfo) {
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

    public String getJsonStr(){return GsonUtil.bean2Json(this);}
}
