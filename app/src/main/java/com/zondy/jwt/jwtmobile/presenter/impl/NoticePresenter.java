package com.zondy.jwt.jwtmobile.presenter.impl;

import com.zondy.jwt.jwtmobile.callback.IQueryNoticeDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryNoticeListCallback;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;
import com.zondy.jwt.jwtmobile.model.ILoginModel;
import com.zondy.jwt.jwtmobile.model.INoticeModel;
import com.zondy.jwt.jwtmobile.model.impl.LoginModelImpl;
import com.zondy.jwt.jwtmobile.model.impl.NoticeModelImpl;
import com.zondy.jwt.jwtmobile.presenter.INoticePresenter;
import com.zondy.jwt.jwtmobile.view.ILoginView;
import com.zondy.jwt.jwtmobile.view.INoticeView;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public class NoticePresenter implements INoticePresenter {
    private INoticeView voticeView;
    private INoticeModel noticeModel;

    public NoticePresenter(INoticeView voticeView) {
        super();
        this.voticeView = voticeView;
        noticeModel = new NoticeModelImpl();
    }


    @Override
    public void queryNoticeList(int noticeType, String jh, String simid) {
        noticeModel.queryNoticeList(noticeType, jh, simid, new IQueryNoticeListCallback() {
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
            public void onQueryNoticeDetailSuccess(EntityNotice entityNotice) {
                voticeView.queryNoticeDetailSuccess(entityNotice);
            }

            @Override
            public void onQueryNoticeDetailFail(Exception e) {
                voticeView.queryNoticeDetailFail(e);
            }
        });
    }
}
