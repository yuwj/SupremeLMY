package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityNotice;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IQueryNoticeListCallback {

    public void queryNoticeListSuccess(List<EntityNotice> notices);
    public void queryNoticeListFail(Exception e);
}
