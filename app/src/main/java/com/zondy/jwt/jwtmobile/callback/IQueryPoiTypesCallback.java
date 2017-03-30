package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityBufbk;
import com.zondy.jwt.jwtmobile.entity.EntityPoiType;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IQueryPoiTypesCallback {

    public void queryPoiTypesSuccess(List<EntityPoiType> poiTypes);
    public void queryPoiTypesFail(Exception e);
}
