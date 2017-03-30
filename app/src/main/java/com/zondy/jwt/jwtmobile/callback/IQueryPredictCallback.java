package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityPredict;

/**
 * Created by ywj on 2017/1/12 0012.
 */

public interface IQueryPredictCallback {
    public void queryPredictSuccess(EntityPredict predict);
    public void queryPredictFailed(Exception exception);
}
