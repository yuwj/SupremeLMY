package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityBufbk;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IBufbkDetailView {
    void queryBufbkDetailSuccess(EntityBufbk entityBufbk);
    void queryBufbkDetailFail(Exception e);

}
