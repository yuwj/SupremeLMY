package com.zondy.jwt.jwtmobile.presenter.impl;

import com.zondy.jwt.jwtmobile.callback.INoticeFeedbackCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryNoticeDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryNoticeListCallback;
import com.zondy.jwt.jwtmobile.entity.EntityFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;
import com.zondy.jwt.jwtmobile.model.INoticeModel;
import com.zondy.jwt.jwtmobile.model.impl.NoticeModelImpl;
import com.zondy.jwt.jwtmobile.presenter.INoticePresenter;
import com.zondy.jwt.jwtmobile.view.INoticeView;
import com.zondy.jwt.jwtmobile.view.INoticeFeedbackView;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public class NoticePresenter implements INoticePresenter {
    private INoticeView voticeView;
    private INoticeModel noticeModel;
    private INoticeFeedbackView tuisxxFeedbackView;

    public NoticePresenter(INoticeView voticeView) {
        super();
        this.voticeView = voticeView;
        noticeModel = new NoticeModelImpl();
    }

    public NoticePresenter(INoticeFeedbackView tuisxxFeedbackView) {
        super();
        this.tuisxxFeedbackView = tuisxxFeedbackView;
        noticeModel = new NoticeModelImpl();
    }


    @Override
    public void queryNoticeList(int noticeType, String jh, String simid,String zzjgdm,int pageSize,int pageNo) {
        noticeModel.queryNoticeList(noticeType, jh, simid,zzjgdm,pageSize,pageNo, new IQueryNoticeListCallback() {
            @Override
            public void queryNoticeListSuccess(List<EntityNotice> notices) {
                voticeView.queryNoticeListSuccess(notices);
            }

            @Override
            public void queryNoticeListFail(Exception e) {
                voticeView.queryNoticeListFail(e);
            }
        });
    }

    @Override
    public void queryNoticeDetail(String jh, String simid, String noticeId) {
        noticeModel.queryNoticeDetail(jh, simid, noticeId, new IQueryNoticeDetailCallback() {
            @Override
            public void onQueryNoticeDetailSuccess(List<EntityFeedback> entityNotice) {
                voticeView.queryNoticeDetailSuccess(entityNotice);
            }

            @Override
            public void onQueryNoticeDetailFail(Exception e) {
                voticeView.queryNoticeDetailFail(e);
            }
        });
    }

    @Override
    public void feedback(String tuisxxId, String jh, String simid, String mediaPaths, String feedbackStrInfo) {
        noticeModel.feedbackNotice(tuisxxId, jh, simid, feedbackStrInfo, mediaPaths, new INoticeFeedbackCallback() {
            @Override
            public void noticeFeedbackSuccess(EntityFeedback entityFeedback) {
                tuisxxFeedbackView.feedbackSuccess(entityFeedback);
            }

            @Override
            public void noticeFeedbackFail(Exception e) {

                tuisxxFeedbackView.feedbackFalied(e);
            }
        });
    }
}
