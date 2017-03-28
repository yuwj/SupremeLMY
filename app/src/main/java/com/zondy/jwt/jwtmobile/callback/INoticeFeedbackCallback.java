package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface INoticeFeedbackCallback {

    public void noticeFeedbackSuccess(EntityFeedback entityFeedback);
    public void noticeFeedbackFail(Exception e);
}
