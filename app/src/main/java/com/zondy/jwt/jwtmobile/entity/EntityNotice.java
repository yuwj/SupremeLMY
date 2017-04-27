package com.zondy.jwt.jwtmobile.entity;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.zondy.jwt.jwtmobile.util.GsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通知公告实体类
 * Created by ywj on 2017/3/23 0023.
 */

public class EntityNotice implements Serializable {
    public static final int TYPE_NOTICE = 1;//通知公告实体

    public String id;// 'OID';
    public String fbdw;// '发布单位';
    public String qh;// '期号';
    public String fbrq;// '发布日期';
    public String bt;// '标题';
    public String content;// '通知内容';
    public String cbr;// '承办人';
    public String qfr;// '签发人';
    public String fbsj;// '发布日期';
    public String dmtlj;//通知的多媒体路径,多个文件用逗号分隔.
    List<EntityNoticeFank> fankList;//反馈信息集合.
    List<String> dmtljList;//通知的多媒体路径,通过dmtlj属性用逗号分隔得来.

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public String getCbr() {
        return cbr;
    }

    public void setCbr(String cbr) {
        this.cbr = cbr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDmtlj() {
        return dmtlj;
    }

    public void setDmtlj(String dmtlj) {
        this.dmtlj = dmtlj;
    }


    public String getFbdw() {
        return fbdw;
    }

    public void setFbdw(String fbdw) {
        this.fbdw = fbdw;
    }

    public String getFbrq() {
        return fbrq;
    }

    public void setFbrq(String fbrq) {
        this.fbrq = fbrq;
    }

    public String getFbsj() {
        return fbsj;
    }

    public void setFbsj(String fbsj) {
        this.fbsj = fbsj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQfr() {
        return qfr;
    }

    public void setQfr(String qfr) {
        this.qfr = qfr;
    }

    public String getQh() {
        return qh;
    }

    public void setQh(String qh) {
        this.qh = qh;
    }

    public List<EntityNoticeFank> getFankList() {
        return fankList;
    }

    public void setFankList(List<EntityNoticeFank> fankList) {
        this.fankList = fankList;
    }

    public List<String> getDmtljList() {
        if(dmtljList == null){
            dmtljList = new ArrayList<>();
        }
        String[] dmtljs = dmtlj.split(",");
        dmtljList.addAll(Arrays.asList(dmtljs));
        return dmtljList;
    }

    public void setDmtljList(List<String> dmtljList) {
        this.dmtljList = dmtljList;
    }

    public String toJsonStr(){
        return GsonUtil.bean2Json(this);
    }
}

