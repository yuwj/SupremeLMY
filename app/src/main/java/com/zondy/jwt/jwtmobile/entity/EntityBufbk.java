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
    public static final int jszt_unaccept = 0;
    public static final int jszt_accepted = 1;

    //布控基本信息
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

    //布控实时信息
    private String kdlx;//number,
    private String kdmc;//varchar2(50),
    private String cpz;// varchar2(50),
    private String sgsj;//date,
    private String ztlgsj;//varchar2(4000),
    private String ztdgsj;//varchar2(4000),
    private String flag;//number,
    private String x;//;//varchar2(20),
    private String y;//;//varchar2(20),
    private String kdlxr;// varchar2(20),//数据库字段 lxr
    private String kdlxdh;//varchar2(20),//数据库字段 lxdh
    private String dthm;//varchar2(20),
    private String sendflag;//varchar2(20),
    private String xtsj;//date,
    private String jszt;//number default 0


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

    public String getCpz() {
        return cpz;
    }

    public void setCpz(String cpz) {
        this.cpz = cpz;
    }

    public String getDthm() {
        return dthm;
    }

    public void setDthm(String dthm) {
        this.dthm = dthm;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getJszt() {
        return jszt;
    }

    public void setJszt(String jszt) {
        this.jszt = jszt;
    }

    public String getKdlx() {
        return kdlx;
    }

    public void setKdlx(String kdlx) {
        this.kdlx = kdlx;
    }

    public String getKdlxdh() {
        return kdlxdh;
    }

    public void setKdlxdh(String kdlxdh) {
        this.kdlxdh = kdlxdh;
    }

    public String getKdlxr() {
        return kdlxr;
    }

    public void setKdlxr(String kdlxr) {
        this.kdlxr = kdlxr;
    }

    public String getKdmc() {
        return kdmc;
    }

    public void setKdmc(String kdmc) {
        this.kdmc = kdmc;
    }

    public String getSendflag() {
        return sendflag;
    }

    public void setSendflag(String sendflag) {
        this.sendflag = sendflag;
    }

    public String getSgsj() {
        return sgsj;
    }

    public void setSgsj(String sgsj) {
        this.sgsj = sgsj;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getXtsj() {
        return xtsj;
    }

    public void setXtsj(String xtsj) {
        this.xtsj = xtsj;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZtdgsj() {
        return ztdgsj;
    }

    public void setZtdgsj(String ztdgsj) {
        this.ztdgsj = ztdgsj;
    }

    public String getZtlgsj() {
        return ztlgsj;
    }

    public void setZtlgsj(String ztlgsj) {
        this.ztlgsj = ztlgsj;
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

