package com.zondy.jwt.jwtmobile.view;

import android.content.Entity;

import com.zondy.jwt.jwtmobile.entity.EntityPredict;

/**
 * Created by ywj on 2017/3/29 0029.
 */

public interface IPredictView {
    void queryPredictSuccess(EntityPredict predict);

    void queryPredictFail(Exception e);

}
