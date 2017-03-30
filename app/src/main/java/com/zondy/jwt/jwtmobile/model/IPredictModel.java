package com.zondy.jwt.jwtmobile.model;

import com.zondy.jwt.jwtmobile.callback.IQueryPredictCallback;

/**
 * Created by ywj on 2017/3/29 0029.
 */

public interface IPredictModel {
    void queryPredict(String jh,String simid,String pdId, IQueryPredictCallback queryPredictCallback);
}
