package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityPoi;
import com.zondy.jwt.jwtmobile.entity.EntityPoiType;

import java.util.List;

/**
 * Created by ywj on 2017/3/29 0029.
 */

public interface ICompositeSearchResultListView {
    void queryPoisSuccess(List<EntityPoi> pois);

    void queryPoisFail(Exception e);
}
