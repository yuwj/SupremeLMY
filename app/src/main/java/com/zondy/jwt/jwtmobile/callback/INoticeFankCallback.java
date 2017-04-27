package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityNoticeFank;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface INoticeFankCallback {

    public void noticeFankSuccess(EntityNoticeFank entityNoticeFank);
    public void noticeFankFail(Exception e);
}
