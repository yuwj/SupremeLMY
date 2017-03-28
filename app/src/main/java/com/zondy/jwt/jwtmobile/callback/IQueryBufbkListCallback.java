package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityBufbk;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IQueryBufbkListCallback {

    public void queryBufbkListSuccess(List<EntityBufbk> bufbks);
    public void queryBufbkListFail(Exception e);
}
