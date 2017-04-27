package com.zondy.jwt.jwtmobile.model;

import com.zondy.jwt.jwtmobile.callback.INoticeFankCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryNoticeDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryNoticeListCallback;

/**
 * 通知公告处理模块
 * Created by ywj on 2017/3/23 0023.
 */

public interface INoticeModel {

    public void queryNoticeList(int noticeType, String jh, String simid,String zzjgdm,int pageSize,int pageNo, IQueryNoticeListCallback queryNoticeListCallback);
    public void queryNoticeDetail(String jh,String simid,String noticeId,IQueryNoticeDetailCallback queryNoticeDetailCallback);
    public void noticeFank(String noticeId, String jh, String simid, String feedbackStrInfo, String mediaPaths, INoticeFankCallback noticeFeedback);
}
