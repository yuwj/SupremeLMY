package com.zondy.jwt.jwtmobile.model;

import com.zondy.jwt.jwtmobile.callback.IXunlpcQueryGuijWithLvgCallBack;
import com.zondy.jwt.jwtmobile.callback.IXunlpcQueryGuijWithWangbCallBack;

/**
 * Created by ywj on 2017/3/16 0016.
 */

public interface IXunlpcModel {
    public void queryGuijWithLvg(String userId, String startTime, String endTime, IXunlpcQueryGuijWithLvgCallBack xunlpcQueryGuijWithLvgCallBack);
    public void queryGuijWithWangb(String userId, String startTime, String endTime, IXunlpcQueryGuijWithWangbCallBack xunlpcQueryGuijWithWangbCallBack);
}
