package com.zondy.jwt.jwtmobile.presenter;

import android.content.Context;

/**
 * Created by sheep on 2016/12/23.
 */

public interface ILoginPresenter {
    void login(Context context,String username,String password,String simid);
    void uploadGPS(String username,String simid,double longitude,double latitude);
    void updateDLSSXX(String username,String simid);
    void queryUnacceptBufbkIds(Context context, String jh,String simid,String xingm);
}
