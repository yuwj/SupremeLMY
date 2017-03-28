package com.zondy.jwt.jwtmobile.entity;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 反馈信息实体,用在通知公告模块.
 * Created by ywj on 2017/3/23 0023.
 */

public class EntityBufbkFeedback implements Serializable {
    String id;
    String bkid;
    String fkrjh;
    String fkrxm;
    String fkdmtlj;
    String fksj;
    String fkwbxx;
    List<String> fbcMediaPaths;


    public List<String> getFbcMediaPaths() {
        List<String> datas = new ArrayList<>();
        if (!TextUtils.isEmpty(fkdmtlj)) {
            String[] d = fkdmtlj.split(",");
            if (d != null && d.length > 0) {
                for (String s : d) {
                    datas.add(s);
                }
            }
        }
        return datas;
    }

    public void setFbcMediaPaths(List<String> fbcMediaPaths) {
        this.fbcMediaPaths = fbcMediaPaths;
    }

    public String getBkid() {
        return bkid;
    }

    public void setBkid(String bkid) {
        this.bkid = bkid;
    }

    public String getFkdmtlj() {
        return fkdmtlj;
    }

    public void setFkdmtlj(String fkdmtlj) {
        this.fkdmtlj = fkdmtlj;
    }

    public String getFkrjh() {
        return fkrjh;
    }

    public void setFkrjh(String fkrjh) {
        this.fkrjh = fkrjh;
    }

    public String getFkrxm() {
        return fkrxm;
    }

    public void setFkrxm(String fkrxm) {
        this.fkrxm = fkrxm;
    }

    public String getFksj() {
        return fksj;
    }

    public void setFksj(String fksj) {
        this.fksj = fksj;
    }

    public String getFkwbxx() {
        return fkwbxx;
    }

    public void setFkwbxx(String fkwbxx) {
        this.fkwbxx = fkwbxx;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
