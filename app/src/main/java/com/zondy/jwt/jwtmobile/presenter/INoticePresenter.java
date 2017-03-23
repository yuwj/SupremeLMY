package com.zondy.jwt.jwtmobile.presenter;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface INoticePresenter {
    public void queryNoticeList(int noticeType, String jh, String simid);
    public void queryNoticeDetail(String jh,String simid,String noticeId);
}
