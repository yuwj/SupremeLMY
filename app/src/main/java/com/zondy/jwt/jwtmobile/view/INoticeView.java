package com.zondy.jwt.jwtmobile.view;

import android.content.Entity;

import com.zondy.jwt.jwtmobile.entity.EntityFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface INoticeView {
    void queryNoticeListSuccess(List<EntityNotice> entityNoticeList);
    void queryNoticeListFail(Exception e);
    void queryNoticeDetailSuccess(List<EntityFeedback> entityNotice);
    void queryNoticeDetailFail(Exception se);

}
