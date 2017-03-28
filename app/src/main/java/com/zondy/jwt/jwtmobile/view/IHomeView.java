package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityBufbk;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IHomeView {
    void queryUnacceptJingqCountSuccess(int count);
    void queryUnacceptJingqCountFail(Exception e);
    void queryUnacceptBufbkCountSuccess(int count);
    void queryUnacceptBufbkCountFail(Exception e);
    void queryUnacceptTongzggCountSuccess(int count);
    void queryUnacceptTongzggCountFail(Exception e);
    void logoutSuccess();
    void logoutFail(Exception e);

}
