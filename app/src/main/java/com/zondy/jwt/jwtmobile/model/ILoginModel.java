package com.zondy.jwt.jwtmobile.model;

import com.zondy.jwt.jwtmobile.callback.IGPSUploadCallback;
import com.zondy.jwt.jwtmobile.callback.ILoginCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryUnacceptBufbkIdsCallback;
import com.zondy.jwt.jwtmobile.callback.IUpdateDLSSXXCallback;
import com.zondy.jwt.jwtmobile.callback.IUpdatePasswordCallback;

/**
 * Created by sheep on 2016/12/23.
 */

public interface ILoginModel {
    /**
     * 登陆
     * @param username
     * @param password
     * @param simid
     * @param loginCallback
     */
    void login(String username, String password, String simid, ILoginCallback loginCallback);

    /**
     * 上传GPS信息
     * @param username
     * @param simid
     * @param longitude
     * @param latitude
     * @param gpsUploadCallback
     */
    void uploadGPS(String username, String simid, double longitude, double latitude, IGPSUploadCallback gpsUploadCallback);

    /**
     * 更新登陆实时信息
     * @param username
     * @param simid
     * @param updateDLSSXXCallback
     */
    void updateDLSSXX(String username, String simid, IUpdateDLSSXXCallback updateDLSSXXCallback);

    /**
     * 查询未接收的布防布控信息
     * @param xingm
     * @param queryUnacceptBufbkIdsCallback
     */
    void queryUnacceptBufbkIds(String jh,String simid,String xingm, IQueryUnacceptBufbkIdsCallback queryUnacceptBufbkIdsCallback);
}
