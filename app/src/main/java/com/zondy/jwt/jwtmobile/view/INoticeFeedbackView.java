package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityJingq;
import com.zondy.jwt.jwtmobile.entity.EntityZD;

import java.util.List;

/**
 * Created by ywj on 2017/1/12 0012.
 */

public interface INoticeFeedbackView {
    public void feedbackSuccess(EntityFeedback entityFeedback);
    public void feedbackFalied(Exception e);
}
