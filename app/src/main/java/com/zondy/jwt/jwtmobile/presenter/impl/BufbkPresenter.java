package com.zondy.jwt.jwtmobile.presenter.impl;

import com.zondy.jwt.jwtmobile.callback.IAcceptBufbkCallback;
import com.zondy.jwt.jwtmobile.callback.IBufbkFeedbackCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryBufbkDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryBufbkListCallback;
import com.zondy.jwt.jwtmobile.entity.EntityBufbkFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityBufbk;
import com.zondy.jwt.jwtmobile.model.IBufbkModel;
import com.zondy.jwt.jwtmobile.model.impl.BufbkModelImpl;
import com.zondy.jwt.jwtmobile.presenter.IBufbkPresenter;
import com.zondy.jwt.jwtmobile.view.IBufbkDetailView;
import com.zondy.jwt.jwtmobile.view.IBufbkFeedbackView;
import com.zondy.jwt.jwtmobile.view.IBufbkView;

import java.util.List;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public class BufbkPresenter implements IBufbkPresenter {
    private IBufbkView bufbkListView;
    private IBufbkModel bufbkModel;
    private IBufbkFeedbackView bufbkFeedbackView;
    private IBufbkDetailView bufbkDetailView;



    public BufbkPresenter(IBufbkDetailView bufbkDetailView) {
        super();
        this.bufbkDetailView = bufbkDetailView;
        bufbkModel = new BufbkModelImpl();
    }
    public BufbkPresenter(IBufbkView bufbkListView) {
        super();
        this.bufbkListView = bufbkListView;
        bufbkModel = new BufbkModelImpl();
    }

    public BufbkPresenter(IBufbkFeedbackView bufbkFeedbackView) {
        super();
        this.bufbkFeedbackView = bufbkFeedbackView;
        bufbkModel = new BufbkModelImpl();
    }


    @Override
    public void queryBufbkList(String jh, String simid,String xingm,int pageSize,int pageNo) {
        bufbkModel.queryBufbkList(jh, simid,xingm,pageSize,pageNo, new IQueryBufbkListCallback() {
            @Override
            public void queryBufbkListSuccess(List<EntityBufbk> bufbks) {
                bufbkListView.queryBufbkListSuccess(bufbks);
            }

            @Override
            public void queryBufbkListFail(Exception e) {
                bufbkListView.queryBufbkListFail(e);
            }
        });
    }

    @Override
    public void queryBufbkFeedbackDatas(String jh, String simid, String bufbkId) {
        bufbkModel.queryBufbkFeedbacks(jh, simid, bufbkId, new IQueryBufbkDetailCallback() {
            @Override
            public void onQueryBufbkDetailSuccess(List<EntityBufbkFeedback> entityBufbkFeedbacks) {
                bufbkDetailView.queryBufbkFeedbacksSuccess(entityBufbkFeedbacks);
            }

            @Override
            public void onQueryBufbkDetailFail(Exception e) {
                bufbkDetailView.queryBufbkFeedbacksFail(e);
            }
        });
    }

    @Override
    public void feedback(String bufbkId, String jh, String simid,String xingm, String mediaPaths, String feedbackStrInfo) {
        bufbkModel.feedbackBufbk(bufbkId, jh, simid,xingm, feedbackStrInfo, mediaPaths, new IBufbkFeedbackCallback() {
            @Override
            public void bufbkFeedbackSuccess() {
                bufbkFeedbackView.feedbackSuccess();
            }

            @Override
            public void bufbkFeedbackFail(Exception e) {

                bufbkFeedbackView.feedbackFali(e);
            }
        });
    }

    @Override
    public void acceptBufbk(String jh, String simid, String bufbkId,String xingm) {
        bufbkModel.acceptBufbk(jh, simid,bufbkId, xingm, new IAcceptBufbkCallback() {
            @Override
            public void acceptBufbkSuccess() {
                bufbkDetailView.acceptBufbkSuccess();
            }

            @Override
            public void acceptBufbkFailed(Exception exception) {
                bufbkDetailView.acceptBufbkFail();
            }
        });
    }
}
