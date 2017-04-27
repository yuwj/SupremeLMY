package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityAskQit;

/**
 * 请求其他服务详情界面
 * Created by ywj on 2017/1/12 0012.
 */

public interface IAskQitDetailView {
    /**
     * 查询请求增援详情成功
     */
    public void onQueryAskQitDetailSuccess(EntityAskQit entityAskQit);
    /**
     * 查询请求增援详情失败
     */
    public void onQueryAskQitDetailFailed(Exception exception);
}
