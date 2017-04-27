package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityAskZous;

/**
 * 请求走失服务详情界面
 * Created by ywj on 2017/1/12 0012.
 */

public interface IAskZousDetailView {
    /**
     * 查询请求增援详情成功
     */
    public void onQueryAskZousDetailSuccess(EntityAskZous entityAskZous);
    /**
     * 查询请求增援详情失败
     */
    public void onQueryAskZousDetailFailed(Exception exception);
}
