package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityAskBuk;
import com.zondy.jwt.jwtmobile.entity.EntityAskBukfk;
import com.zondy.jwt.jwtmobile.entity.EntityJingq;

import java.util.List;

/**
 * 请求布控服务详情界面
 * Created by ywj on 2017/1/12 0012.
 */

public interface IAskBukDetailView {
    /**
     * 查询已请求布控详情成功
     */
    public void onQueryAskBukDetailSuccess(EntityAskBuk entityAskForBuk);
    /**
     * 查询已请求布控详情失败
     */
    public void onQueryAskBukDetailFailed(Exception exception);

    /**
     * 查询警情成功
     * @param entityJingq
     */
    public void onQueryJingqSuccess(EntityJingq entityJingq);

    /**
     * 查询警情失败
     * @param exception
     */
    public void onQueryJingqFail(Exception exception);

}
