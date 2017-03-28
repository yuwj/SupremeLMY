package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityBufbk;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IQueryUnacceptBufbkIdsCallback {

    public void queryUnacceptBufbkIdsSuccess(List<String> bufbkIds);
    public void queryUnacceptBufbkIdsFail(Exception e);
}
