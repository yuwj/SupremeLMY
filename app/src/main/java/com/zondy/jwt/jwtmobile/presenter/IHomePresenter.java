package com.zondy.jwt.jwtmobile.presenter;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public interface IHomePresenter {
    public void queryUnacceptJingqCount(String jh,String simid,String zzjgdm);
    public void queryUnacceptBufbkCount(String jh, String simid, String xingm);
    public void queryUnacceptTongzggCount(String jh, String simid, String xingm);
    public void logout(String jh, String simid);
}
