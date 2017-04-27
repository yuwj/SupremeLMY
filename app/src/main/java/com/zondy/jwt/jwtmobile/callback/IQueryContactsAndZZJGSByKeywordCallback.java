package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityContactsAndZZJGS;

import java.util.List;

/**
 * Created by ywj on 2017/4/26 0026.
 */

public interface IQueryContactsAndZZJGSByKeywordCallback {
    public void onQueryContactsAndZZJGsSuccess(EntityContactsAndZZJGS result);
    public void onQueryContactsAndZZJGsFail(Exception e);
}
