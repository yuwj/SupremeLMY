package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityBufbkFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityFeedback;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IBufbkFeedbackCallback {

    public void bufbkFeedbackSuccess(EntityBufbkFeedback entityFeedback);
    public void bufbkFeedbackFail(Exception e);
}
