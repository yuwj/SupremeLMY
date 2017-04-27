package com.zondy.jwt.jwtmobile.presenter.impl;

import com.zondy.jwt.jwtmobile.callback.IAskBukCallback;
import com.zondy.jwt.jwtmobile.callback.IAskChaxCallback;
import com.zondy.jwt.jwtmobile.callback.IAskQitCallback;
import com.zondy.jwt.jwtmobile.callback.IAskZengyCallback;
import com.zondy.jwt.jwtmobile.callback.IAskZousCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryAskBukDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryAskChaxDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryAskQitDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryAskServiceListCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryAskZengyDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryAskZousDetailCallback;
import com.zondy.jwt.jwtmobile.entity.EntityAskBuk;
import com.zondy.jwt.jwtmobile.entity.EntityAskChax;
import com.zondy.jwt.jwtmobile.entity.EntityAskQit;
import com.zondy.jwt.jwtmobile.entity.EntityAskService;
import com.zondy.jwt.jwtmobile.entity.EntityAskZengy;
import com.zondy.jwt.jwtmobile.entity.EntityAskZous;
import com.zondy.jwt.jwtmobile.model.IAskForServiceModel;
import com.zondy.jwt.jwtmobile.model.impl.AskForServiceModelImpl;
import com.zondy.jwt.jwtmobile.presenter.IAskServicePresenter;
import com.zondy.jwt.jwtmobile.view.IAskChaxDetailView;
import com.zondy.jwt.jwtmobile.view.IAskQitDetailView;
import com.zondy.jwt.jwtmobile.view.IAskServiceView;
import com.zondy.jwt.jwtmobile.view.IAskBukDetailView;
import com.zondy.jwt.jwtmobile.view.IAskServiceListView;
import com.zondy.jwt.jwtmobile.view.IAskZengyDetailView;
import com.zondy.jwt.jwtmobile.view.IAskZousDetailView;

import java.util.List;

/**
 * Created by ywj on 2017/4/13 0013.
 */

public class AskServicePresenterImpl implements IAskServicePresenter {
    public IAskServiceListView askedServiceListView;
    public IAskServiceView askServiceView;
    public IAskBukDetailView askBukDetailView;
    public IAskZengyDetailView askZengyDetailView;
    public IAskChaxDetailView askChaxDetailView;
    public IAskZousDetailView askZousDetailView;
    public IAskQitDetailView askQitDetailView;
    public IAskForServiceModel askForServiceModel;

    public AskServicePresenterImpl(IAskServiceListView askedServiceListView) {
        this.askedServiceListView = askedServiceListView;
        askForServiceModel = new AskForServiceModelImpl();
    }

    public AskServicePresenterImpl(IAskServiceView askServiceView) {
        this.askServiceView = askServiceView;
        askForServiceModel = new AskForServiceModelImpl();
    }

    public AskServicePresenterImpl(IAskBukDetailView askBukDetailView) {
        this.askBukDetailView = askBukDetailView;
        askForServiceModel = new AskForServiceModelImpl();
    }
    public AskServicePresenterImpl(IAskZengyDetailView askZengyDetailView) {
        this.askZengyDetailView = askZengyDetailView;
        askForServiceModel = new AskForServiceModelImpl();
    }
    public AskServicePresenterImpl(IAskChaxDetailView askChaxDetailView) {
        this.askChaxDetailView = askChaxDetailView;
        askForServiceModel = new AskForServiceModelImpl();
    }
    public AskServicePresenterImpl(IAskZousDetailView askZousDetailView) {
        this.askZousDetailView = askZousDetailView;
        askForServiceModel = new AskForServiceModelImpl();
    }
    public AskServicePresenterImpl(IAskQitDetailView askQitDetailView) {
        this.askQitDetailView = askQitDetailView;
        askForServiceModel = new AskForServiceModelImpl();
    }


    @Override
    public void queryAskServiceList(String jh, String simid, String xingm, int pageSize, int pageNo) {
        askForServiceModel.queryAskServiceList(jh, simid, xingm, pageSize, pageNo, new IQueryAskServiceListCallback() {

            @Override
            public void onQueryAskServiceListSuccess(List<EntityAskService> entityAskForServiceList) {
                askedServiceListView.onGetServiceDatasSuccess(entityAskForServiceList);
            }

            @Override
            public void onQueryAskServiceListFailed(Exception e) {
                askedServiceListView.onGetServiceDatasFailed(e);
            }
        });
    }

    @Override
    public void queryAskBukDetail(String jh, String simid, String askedServiceID) {
        askForServiceModel.queryAskBukDetail(jh, simid, askedServiceID, new IQueryAskBukDetailCallback() {
            @Override
            public void onQueryAskBukDetailSuccess(EntityAskBuk entityAskForBuk) {
                askBukDetailView.onQueryAskBukDetailSuccess(entityAskForBuk);
            }

            @Override
            public void onQueryAskBukDetailFail(Exception e) {
                askBukDetailView.onQueryAskBukDetailFailed(e);

            }
        });
    }

    @Override
    public void queryAskZengyDetail(String jh, String simid, String askZengyId) {
        askForServiceModel.queryAskZengyDetail(jh, simid, askZengyId, new IQueryAskZengyDetailCallback() {
            @Override
            public void onQueryAskZengyDetailSuccess(EntityAskZengy entityAskForZengy) {
                askZengyDetailView.onQueryAskZengyDetailSuccess(entityAskForZengy);
            }

            @Override
            public void onQueryAskZengyDetailFail(Exception e) {

                askZengyDetailView.onQueryAskZengyDetailFailed(e);
            }
        });
    }

    @Override
    public void queryAskChaxDetail(String jh, String simid, String askChaxId) {
        askForServiceModel.queryAskChaxDetail(jh, simid, askChaxId, new IQueryAskChaxDetailCallback() {
            @Override
            public void onQueryAskChaxDetailSuccess(EntityAskChax entityAskForChax) {
                askChaxDetailView.onQueryAskChaxDetailSuccess(entityAskForChax);
            }

            @Override
            public void onQueryAskChaxDetailFail(Exception e) {
                askChaxDetailView.onQueryAskChaxDetailFailed(e);

            }
        });
    }

    @Override
    public void queryAskZousDetail(String jh, String simid, String askZousId) {
        askForServiceModel.queryAskZousDetail(jh, simid, askZousId, new IQueryAskZousDetailCallback() {
            @Override
            public void onQueryAskZousDetailSuccess(EntityAskZous entityAskForZous) {
                askZousDetailView.onQueryAskZousDetailSuccess(entityAskForZous);
            }

            @Override
            public void onQueryAskZousDetailFail(Exception e) {
                askZousDetailView.onQueryAskZousDetailFailed(e);

            }
        });
    }

    @Override
    public void queryAskQitDetail(String jh, String simid, String askQitId) {
        askForServiceModel.queryAskQitDetail(jh, simid, askQitId, new IQueryAskQitDetailCallback() {
            @Override
            public void onQueryAskQitDetailSuccess(EntityAskQit entityAskForQit) {
                askQitDetailView.onQueryAskQitDetailSuccess(entityAskForQit);
            }

            @Override
            public void onQueryAskQitDetailFail(Exception e) {
                askQitDetailView.onQueryAskQitDetailFailed(e);

            }
        });
    }


    @Override
    public void askBuk(String jh, String simid, EntityAskBuk entityAskForBuk) {
        askForServiceModel.askBuk(jh, simid, entityAskForBuk, new IAskBukCallback() {
            @Override
            public void onAskBukSuccess() {
                askServiceView.onAskSuccess();
            }

            @Override
            public void onAskBukFail(Exception e) {
                askServiceView.onAskFail(e);
            }
        });
    }

    @Override
    public void askZengy(String jh, String simid, EntityAskZengy entityAskZengy) {
        askForServiceModel.askZengy(jh, simid, entityAskZengy, new IAskZengyCallback() {
            @Override
            public void onAskZengySuccess() {
                askServiceView.onAskSuccess();
            }

            @Override
            public void onAskZengyFail(Exception e) {
                askServiceView.onAskFail(e);
            }
        });
    }

    @Override
    public void askChax(String jh, String simid, EntityAskChax entityAskChax) {
        askForServiceModel.askChax(jh, simid, entityAskChax, new IAskChaxCallback() {
            @Override
            public void onAskChaxSuccess() {
                askServiceView.onAskSuccess();
            }

            @Override
            public void onAskChaxFail(Exception e) {
                askServiceView.onAskFail(e);
            }
        });
    }

    @Override
    public void askZous(String jh, String simid, EntityAskZous entityAskZous) {
        askForServiceModel.askZous(jh, simid, entityAskZous, new IAskZousCallback() {
            @Override
            public void onAskZousSuccess() {
                askServiceView.onAskSuccess();
            }

            @Override
            public void onAskZousFail(Exception e) {
                askServiceView.onAskFail(e);
            }
        });
    }

    @Override
    public void askQit(String jh, String simid, EntityAskQit entityAskQit) {
        askForServiceModel.askQit(jh, simid, entityAskQit, new IAskQitCallback() {
            @Override
            public void onAskQitSuccess() {
                askServiceView.onAskSuccess();
            }

            @Override
            public void onAskQitFail(Exception e) {
                askServiceView.onAskFail(e);
            }
        });
    }


}
