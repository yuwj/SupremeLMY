package com.zondy.jwt.jwtmobile.callback;

import com.zondy.jwt.jwtmobile.entity.EntityBufbkFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityFeedback;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IQueryBufbkDetailCallback {
    void onQueryBufbkDetailSuccess(List<EntityBufbkFeedback> feedbackList);
    void onQueryBufbkDetailFail(Exception e);

}
