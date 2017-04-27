package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityJingq;

/**
 * Created by ywj on 2017/4/14 0014.
 */

public interface IAskServiceMainView {
    void onQueryJingqSuccess(EntityJingq entityJingq);
    void onQueryJingqFail(Exception e);

}
