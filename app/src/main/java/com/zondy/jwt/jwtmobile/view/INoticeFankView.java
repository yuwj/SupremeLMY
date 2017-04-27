package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityNoticeFank;

/**
 * Created by ywj on 2017/1/12 0012.
 */

public interface INoticeFankView {
    public void noticeFankSuccess(EntityNoticeFank entityNoticeFank);
    public void noticeFankFail(Exception e);
}
