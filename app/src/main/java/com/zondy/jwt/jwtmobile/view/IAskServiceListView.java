package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityAskService;

import java.util.List;

/**
 * 已请求服务列表界面
 * Created by ywj on 2017/1/12 0012.
 */

public interface IAskServiceListView {
    public void onGetServiceDatasSuccess(List<EntityAskService> jingqDatas);
    public void onGetServiceDatasFailed(Exception exception);
}
