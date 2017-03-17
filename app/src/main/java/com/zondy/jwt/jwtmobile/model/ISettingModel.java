package com.zondy.jwt.jwtmobile.model;

import com.zondy.jwt.jwtmobile.callback.ILogoutCallback;
import com.zondy.jwt.jwtmobile.callback.IUpdatePasswordCallback;

/**
 * Created by sheep on 2017/1/11.
 */

public interface ISettingModel {
    void logout(String jh, String simid, ILogoutCallback logoutCallback);

    void updatePassword(String userName, String oldPwd, String newPwd,String jh,String simid, IUpdatePasswordCallback updateCallback);
}
