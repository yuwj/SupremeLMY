package com.zondy.jwt.jwtmobile.callback;

/**
 * 实时在线信息更新回调
 * Created by sheep on 2017/1/9.
 */

public interface IUpdateDLSSXXCallback {
    void updateSuccess();
    void updateFail(Exception e);
}
