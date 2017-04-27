package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityAskService;

import java.util.List;

/**
 * 查询请求列表回调接口
 * Created by ywj on 2017/4/13 0013.
 */

public interface IQueryAskServiceListCallback {
    void onQueryAskServiceListSuccess(List<EntityAskService> entityAskForServiceList);
    void onQueryAskServiceListFailed(Exception e);
}
