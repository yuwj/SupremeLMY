package com.zondy.jwt.jwtmobile.entity;

import com.zondy.jwt.jwtmobile.util.GsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通知公告反馈信息实体
 * Created by ywj on 2017/3/23 0023.
 */

public class EntityNoticeFank implements Serializable {
    public String noticeid;// '推送消息记录id';
    public String fkxx;// '反馈信息';
    public String dmtlj;// '反馈信息的多媒体路径,多个路径用逗号分隔';
    public String fkrjh;// '反馈人警号';
    public String fksj;// '反馈时间';
    List<String> dmtljList;//'反馈信息的多媒体路径,通过dmtlj属性用逗号分隔得来.

    public String getDmtlj() {
        return dmtlj;
    }

    public void setDmtlj(String dmtlj) {
        this.dmtlj = dmtlj;
    }

    public String getFkrjh() {
        return fkrjh;
    }

    public void setFkrjh(String fkrjh) {
        this.fkrjh = fkrjh;
    }

    public String getFksj() {
        return fksj;
    }

    public void setFksj(String fksj) {
        this.fksj = fksj;
    }

    public String getFkxx() {
        return fkxx;
    }

    public void setFkxx(String fkxx) {
        this.fkxx = fkxx;
    }

    public List<String> getDmtljList() {
        if (dmtljList == null) {
            dmtljList = new ArrayList<>();
        }
        String[] dmtljs = dmtlj.split(",");
        dmtljList.addAll(Arrays.asList(dmtljs));
        return dmtljList;

    }

    public void setDmtljList(List<String> dmtljList) {
        this.dmtljList = dmtljList;
    }

    public String getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
    }

    public String toJsonStr() {
        return GsonUtil.bean2Json(this);
    }
}
