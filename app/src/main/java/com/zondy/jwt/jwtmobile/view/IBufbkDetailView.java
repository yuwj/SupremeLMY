package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityBufbk;
import com.zondy.jwt.jwtmobile.entity.EntityBufbkFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityFeedback;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IBufbkDetailView {
    void queryBufbkFeedbacksSuccess(List<EntityBufbkFeedback> entityBufbkFeedbacks);
    void queryBufbkFeedbacksFail(Exception e);
    void acceptBufbkSuccess();void acceptBufbkFail();

}
