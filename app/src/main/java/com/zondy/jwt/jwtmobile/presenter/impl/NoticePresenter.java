package com.zondy.jwt.jwtmobile.presenter.impl;

import com.zondy.jwt.jwtmobile.callback.INoticeFankCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryNoticeDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryNoticeListCallback;
import com.zondy.jwt.jwtmobile.entity.EntityNoticeFank;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;
import com.zondy.jwt.jwtmobile.model.INoticeModel;
import com.zondy.jwt.jwtmobile.model.impl.NoticeModelImpl;
import com.zondy.jwt.jwtmobile.presenter.INoticePresenter;
import com.zondy.jwt.jwtmobile.view.INoticeView;
import com.zondy.jwt.jwtmobile.view.INoticeFankView;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public class NoticePresenter implements INoticePresenter {
    private INoticeView noticeView;
    private INoticeModel noticeModel;
    private INoticeFankView noticeFankView;

    public NoticePresenter(INoticeView voticeView) {
        super();
        this.noticeView = voticeView;
        noticeModel = new NoticeModelImpl();
    }

    public NoticePresenter(INoticeFankView tuisxxFeedbackView) {
        super();
        this.noticeFankView = tuisxxFeedbackView;
        noticeModel = new NoticeModelImpl();
    }


    @Override
    public void queryNoticeList(int noticeType, String jh, String simid,String zzjgdm,int pageSize,int pageNo) {
        noticeModel.queryNoticeList(noticeType, jh, simid,zzjgdm,pageSize,pageNo, new IQueryNoticeListCallback() {
            @Override
            public void queryNoticeListSuccess(List<EntityNotice> notices) {
                noticeView.queryNoticeListSuccess(notices);
            }

            @Override
            public void queryNoticeListFail(Exception e) {
                noticeView.queryNoticeListFail(e);
            }
        });
    }

    @Override
    public void queryNoticeDetail(String jh, String simid, String noticeId) {
        noticeModel.queryNoticeDetail(jh, simid, noticeId, new IQueryNoticeDetailCallback() {
            @Override
            public void onQueryNoticeDetailSuccess(List<EntityNoticeFank> entityNotice) {
                noticeView.queryNoticeDetailSuccess(entityNotice);
            }

            @Override
            public void onQueryNoticeDetailFail(Exception e) {
                noticeView.queryNoticeDetailFail(e);
            }
        });
    }

    @Override
    public void fank(String tuisxxId, String jh, String simid, String mediaPaths, String feedbackStrInfo) {
        noticeModel.noticeFank(tuisxxId, jh, simid, feedbackStrInfo, mediaPaths, new INoticeFankCallback() {
            @Override
            public void noticeFankSuccess(EntityNoticeFank entityNoticeFank) {
                noticeFankView.noticeFankSuccess(entityNoticeFank);
            }

            @Override
            public void noticeFankFail(Exception e) {

                noticeFankView.noticeFankFail(e);
            }
        });
    }
}
