package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityPoi;
import com.zondy.jwt.jwtmobile.entity.EntityPoiType;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IQueryPoisCallback {

    public void queryPoisSuccess(List<EntityPoi> pois);
    public void queryPoisFail(Exception e);
}
