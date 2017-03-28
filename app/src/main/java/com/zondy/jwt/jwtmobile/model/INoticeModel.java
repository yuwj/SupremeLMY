package com.zondy.jwt.jwtmobile.model;

import com.zondy.jwt.jwtmobile.callback.INoticeFeedbackCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryNoticeDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryNoticeListCallback;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;

import java.util.List;

/**
 * 推送通知处理模块
 * Created by ywj on 2017/3/23 0023.
 */

public interface INoticeModel {

    public void queryNoticeList(int noticeType, String jh, String simid,String zzjgdm,int pageSize,int pageNo, IQueryNoticeListCallback queryNoticeListCallback);
    public void queryNoticeDetail(String jh,String simid,String noticeId,IQueryNoticeDetailCallback queryNoticeDetailCallback);
    public void feedbackNotice(String noticeId,String jh,String simid,String feedbackStrInfo,String mediaPaths,INoticeFeedbackCallback noticeFeedback);
}
