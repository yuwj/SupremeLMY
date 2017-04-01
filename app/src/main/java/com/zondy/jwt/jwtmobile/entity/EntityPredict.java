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



}


