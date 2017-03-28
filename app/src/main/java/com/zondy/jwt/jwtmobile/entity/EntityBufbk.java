package com.zondy.jwt.jwtmobile.entity;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public class EntityBufbk implements Serializable {

    String id;
    String mc;
    String bksy;
    String qqbkdw;
    String lxr;
    String lxdh;
    String lx;
    String bkr;
    String bksj;
    String bkdwbm;
    String zt;
    String ckr;
    String cksj;
    String dmtlj;


    List<String> filePaths;//通知的多媒体信息
    List<EntityBufbkFeedback> entityBufbkFeedbacks;//反馈信息集合.

    public String getDmtlj() {
        return dmtlj;
    }

    public void setDmtlj(String dmtlj) {
        this.dmtlj = dmtlj;
    }

    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
    }

    public String getBkdwbm() {
        return bkdwbm;
    }

    public void setBkdwbm(String bkdwbm) {
        this.bkdwbm = bkdwbm;
    }

    public String getBkr() {
        return bkr;
    }

    public void setBkr(String bkr) {
        this.bkr = bkr;
    }

    public String getBksj() {
        return bksj;
    }

    public void setBksj(String bksj) {
        this.bksj = bksj;
    }

    public String getBksy() {
        return bksy;
    }

    public void setBksy(String bksy) {
        this.bksy = bksy;
    }

    public String getCkr() {
        return ckr;
    }

    public void setCkr(String ckr) {
        this.ckr = ckr;
    }

    public String getCksj() {
        return cksj;
    }

    public void setCksj(String cksj) {
        this.cksj = cksj;
    }

    public List<EntityBufbkFeedback> getEntityBufbkFeedbacks() {
        return entityBufbkFeedbacks;
    }

    public void setEntityBufbkFeedbacks(List<EntityBufbkFeedback> entityBufbkFeedbacks) {
        this.entityBufbkFeedbacks = entityBufbkFeedbacks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getQqbkdw() {
        return qqbkdw;
    }

    public void setQqbkdw(String qqbkdw) {
        this.qqbkdw = qqbkdw;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public List<EntityBufbkFeedback> getFeedbacks() {
        return entityBufbkFeedbacks;
    }

    public void setFeedbacks(List<EntityBufbkFeedback> entityBufbkFeedbacks) {
        this.entityBufbkFeedbacks = entityBufbkFeedbacks;
    }

    public List<String> getFilePaths() {

        List<String> datas = new ArrayList<>();
        if(!TextUtils.isEmpty(dmtlj)){


        String[] d = dmtlj.split(",");
        if (d != null && d.length > 0) {
            for (String s : d) {
                datas.add(s);
            }
        }}
        return datas;
    }


}

