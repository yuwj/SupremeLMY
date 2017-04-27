package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityAskZengy;

/**
 * 查询请求布控详情回调接口
 * Created by ywj on 2017/4/13 0013.
 */

public interface IQueryAskZengyDetailCallback {
    void onQueryAskZengyDetailSuccess(EntityAskZengy entityAskZengy);
    void onQueryAskZengyDetailFail(Exception e);
}
