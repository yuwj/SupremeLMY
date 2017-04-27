package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityAskBuk;
import com.zondy.jwt.jwtmobile.entity.EntityAskBukfk;
import com.zondy.jwt.jwtmobile.entity.EntityAskZengy;
import com.zondy.jwt.jwtmobile.entity.EntityJingq;

import java.util.List;

/**
 * 请求增援服务详情界面
 * Created by ywj on 2017/1/12 0012.
 */

public interface IAskZengyDetailView {
    /**
     * 查询请求增援详情成功
     */
    public void onQueryAskZengyDetailSuccess(EntityAskZengy entityAskZengy);
    /**
     * 查询请求增援详情失败
     */
    public void onQueryAskZengyDetailFailed(Exception exception);
}
