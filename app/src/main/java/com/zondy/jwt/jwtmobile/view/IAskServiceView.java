package com.zondy.jwt.jwtmobile.view;

import android.location.Location;

import com.zondy.jwt.jwtmobile.entity.EntityLocation;

/**
 * 已请求服务列表界面
 * Created by ywj on 2017/1/12 0012.
 */

public interface IAskServiceView {
    /**
     * 请求成功
     */
    public void onAskSuccess();

    /**
     * 请求失败
     * @param exception
     */
    public void onAskFail(Exception exception);

    /**
     * 发起请求
     */
    public void launchAsk();

//    /**
//     * 更新在自己的位置
//     */
//    public void updatePosition(EntityLocation entityLocation);
}
