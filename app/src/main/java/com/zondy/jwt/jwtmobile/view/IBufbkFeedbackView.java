package com.zondy.jwt.jwtmobile.view;

import com.zondy.jwt.jwtmobile.entity.EntityBufbkFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityFeedback;

/**
 * Created by ywj on 2017/1/12 0012.
 */

public interface IBufbkFeedbackView {
    public void feedbackSuccess();
    public void feedbackFali(Exception e);
}
