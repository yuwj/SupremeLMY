package com.zondy.jwt.jwtmobile.callback;

/**
 * 请求布控回调接口
 * Created by ywj on 2017/4/13 0013.
 */

public interface IAskZengyCallback {
    void onAskZengySuccess();
    void onAskZengyFail(Exception e);
}
