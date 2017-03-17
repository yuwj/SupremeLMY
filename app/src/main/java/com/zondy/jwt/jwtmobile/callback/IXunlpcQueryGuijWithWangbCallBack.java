package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityBaseGuij;
import com.zondy.jwt.jwtmobile.entity.EntityGuijWithWangb;

import java.util.List;

/**
 * 巡逻盘查查询上网轨迹的回调
 * Created by ywj on 2017/3/16 0016.
 */

public interface IXunlpcQueryGuijWithWangbCallBack {
    public void queryWangbSuccess(List<EntityGuijWithWangb> guijDatas);
    public void queryWangbFail(Exception e);
}
