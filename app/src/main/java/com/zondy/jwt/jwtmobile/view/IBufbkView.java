package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityBufbk;
import com.zondy.jwt.jwtmobile.entity.EntityBufbkFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IBufbkView {
    void queryBufbkListSuccess(List<EntityBufbk> entityBufbkList);
    void queryBufbkListFail(Exception e);
    void queryBufbkDetailSuccess(List<EntityBufbkFeedback> entityBufbk);
    void queryBufbkDetailFail(Exception se);

}
