package com.zondy.jwt.jwtmobile.model;

import com.zondy.jwt.jwtmobile.callback.IAcceptBufbkCallback;
import com.zondy.jwt.jwtmobile.callback.IBufbkFeedbackCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryBufbkDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryBufbkListCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryUnacceptBufbkCountCallback;

/**
 * 推送通知处理模块
 * Created by ywj on 2017/3/23 0023.
 */

public interface IBufbkModel {

    public void acceptBufbk(String jh, String simid, String xingm, IAcceptBufbkCallback acceptBufbkCallback);
    public void queryBufbkList(String jh, String simid, String xingm, int pageSize, int pageNo, IQueryBufbkListCallback queryBufbkListCallback);
    public void queryBufbkFeedbacks(String jh, String simid, String BufbkId, IQueryBufbkDetailCallback queryBufbkDetailCallback);
    public void feedbackBufbk(String BufbkId, String jh, String simid,String xingm, String feedbackStrInfo, String mediaPaths, IBufbkFeedbackCallback BufbkFeedback);
    public void queryUnacceptBufbkCount(String jh, String simid, String xingm, IQueryUnacceptBufbkCountCallback queryUnacceptBufbkCountCallback);
}
