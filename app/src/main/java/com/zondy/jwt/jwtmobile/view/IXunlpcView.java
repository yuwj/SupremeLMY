package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityBaseGuij;
import com.zondy.jwt.jwtmobile.entity.EntityGuijWithLvg;
import com.zondy.jwt.jwtmobile.entity.EntityGuijWithWangb;

import java.util.List;

/**
 * Created by ywj on 2017/3/16 0016.
 */

public interface IXunlpcView {
    public void showGuijWithLvgInMap(List<EntityGuijWithLvg> guijDatas);
    public void showGuijWithWangbInMap(List<EntityGuijWithWangb> guijDatas);
}
