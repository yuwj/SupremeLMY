package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityPoiType;
import com.zondy.jwt.jwtmobile.entity.EntityTCFL;

import java.util.List;

/**
 * Created by sheep on 2017/1/19.
 */

public interface IQueryTCFZListCallback {
    void querySuccessed(List<EntityTCFL> poiTypeList);
    void queryUnSuccessed(Exception e);
}
