package com.zondy.jwt.jwtmobile.entity;

/**
 * 请求服务
 * Created by ywj on 2017/4/12 0012.
 */

public class EntityAskService {
    public static final int ASK_FOR_SERVICE_TYPE_BUK = 1;
    public static final int ASK_FOR_SERVICE_TYPE_ZENGY = 2;
    public static final int ASK_FOR_SERVICE_TYPE_CHAX = 3;
    public static final int ASK_FOR_SERVICE_TYPE_ZOUS = 4;
    public static final int ASK_FOR_SERVICE_TYPE_QIT = 5;

    String jingqID;//关联的警情ID
    String serviceDescription;//服务描述
    String time;//请求服务的时间
    int type;//请求服务的类型 ASK_FOR_SERVICE_TYPE_BUK-布控,ASK_FOR_SERVICE_TYPE_ZENGY-增援,ASK_FOR_SERVICE_TYPE_CHAX-查询,ASK_FOR_SERVICE_TYPE_ZOUS-走失,ASK_FOR_SERVICE_TYPE_QIT-其他
    int state;//状态,是否有反馈
    String askServiceID;//请求服务的ID

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAskServiceID() {
        return askServiceID;
    }

    public String getJingqID() {
        return jingqID;
    }

    public void setJingqID(String jingqID) {
        this.jingqID = jingqID;
    }

    public void setAskServiceID(String askServiceID) {
        this.askServiceID = askServiceID;
    }
}
