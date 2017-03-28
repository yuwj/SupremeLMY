package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityUser;

/**
 * GPS上传回调
 * Created by sheep on 2017/1/9.
 */

public interface IGPSUploadCallback {
    void uploadSuccess();
    void uploadFail(Exception e);
}
