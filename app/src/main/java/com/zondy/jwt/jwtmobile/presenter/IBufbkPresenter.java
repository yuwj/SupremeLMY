package com.zondy.jwt.jwtmobile.presenter;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IBufbkPresenter {
    public void queryBufbkList( String jh, String simid, String xingm, int pageSize, int pageNo);
    public void queryBufbkFeedbackDatas(String jh, String simid, String noticeId);

    public void feedback(String bufbkId, String jh, String simid,String xingm, String mediaPaths, String feedbackStrInfo);
}
