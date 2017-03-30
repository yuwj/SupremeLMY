package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityPoiType;

import java.util.List;

/**
 * Created by ywj on 2017/3/29 0029.
 */

public interface ICompositeSearchInputView {
    void queryPoiTypesSuccess(List<EntityPoiType> poiTypes);

    void queryPoiTypesFail(Exception e);
}
