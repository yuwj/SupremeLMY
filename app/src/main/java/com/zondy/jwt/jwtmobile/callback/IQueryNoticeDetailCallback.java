package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityNoticeFank;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IQueryNoticeDetailCallback {
    void onQueryNoticeDetailSuccess(List<EntityNoticeFank> feedbackList);
    void onQueryNoticeDetailFail(Exception e);

}
