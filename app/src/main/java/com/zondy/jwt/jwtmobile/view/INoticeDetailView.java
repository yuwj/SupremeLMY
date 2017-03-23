package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityNotice;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface INoticeDetailView {
    void queryNoticeDetailSuccess(EntityNotice entityNotice);
    void queryNoticeDetailFail(Exception e);

}
