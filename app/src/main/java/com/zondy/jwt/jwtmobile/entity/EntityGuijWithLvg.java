package com.zondy.jwt.jwtmobile.entity;

import com.zondy.jwt.jwtmobile.util.GsonUtil;

import java.io.Serializable;

/**
 * 轨迹实体之旅馆
 * Created by ywj on 2017/3/16 0016.
 */

public class EntityGuijWithLvg extends EntityBaseGuij implements Serializable {
    float starCount;

    public float getStartCount() {
        return starCount;
    }

    public void setStarCount(float starCount) {
        this.starCount = starCount;
    }
}
