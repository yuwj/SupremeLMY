package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityAskChax;

/**
 * 请求查询服务详情界面
 * Created by ywj on 2017/1/12 0012.
 */

public interface IAskChaxDetailView {
    /**
     * 查询请求增援详情成功
     */
    public void onQueryAskChaxDetailSuccess(EntityAskChax entityAskChax);
    /**
     * 查询请求增援详情失败
     */
    public void onQueryAskChaxDetailFailed(Exception exception);
}
