package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityBaseGuij;
import com.zondy.jwt.jwtmobile.entity.EntityGuijWithLvg;

import java.util.List;

/**
 * 巡逻盘查查询住宿轨迹的回调
 * Created by ywj on 2017/3/16 0016.
 */

public interface IXunlpcQueryGuijWithLvgCallBack {
    public void queryLvgSuccess(List<EntityGuijWithLvg> guijDatas);
    public void queryLvgFail(Exception e);
}
