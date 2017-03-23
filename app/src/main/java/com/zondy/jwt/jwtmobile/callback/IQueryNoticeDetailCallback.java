package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityNotice;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IQueryNoticeDetailCallback {
    void onQueryNoticeDetailSuccess(EntityNotice entityNotice);
    void onQueryNoticeDetailFail(Exception e);

}
