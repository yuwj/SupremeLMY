package com.zondy.jwt.jwtmobile.presenter.impl;

import com.zondy.jwt.jwtmobile.callback.IQueryPredictCallback;
import com.zondy.jwt.jwtmobile.entity.EntityPredict;
import com.zondy.jwt.jwtmobile.model.IPredictModel;
import com.zondy.jwt.jwtmobile.model.impl.PredictModelImpl;
import com.zondy.jwt.jwtmobile.presenter.IPredictPresenter;
import com.zondy.jwt.jwtmobile.view.IPredictView;

/**
 * Created by ywj on 2017/3/29 0029.
 */

public class PredictPresenterImpl implements IPredictPresenter {
    IPredictModel predictModel;
    IPredictView predictView;

    public PredictPresenterImpl(IPredictView predictView) {
        this.predictView = predictView;
        predictModel = new PredictModelImpl();
    }

    @Override
    public void queryPredict(String jh, String simid, String preditId) {
        predictModel.queryPredict(jh, simid, preditId, new IQueryPredictCallback() {
            @Override
            public void queryPredictSuccess(EntityPredict predict) {
                predictView.queryPredictSuccess(predict);
            }

            @Override
            public void queryPredictFailed(Exception exception) {
                predictView.queryPredictFail(exception);
            }
        });
    }
}
