package com.zondy.jwt.jwtmobile.presenter.impl;

import com.zondy.jwt.jwtmobile.callback.IXunlpcQueryGuijWithLvgCallBack;
import com.zondy.jwt.jwtmobile.entity.EntityGuijWithLvg;
import com.zondy.jwt.jwtmobile.model.IXunlpcModel;
import com.zondy.jwt.jwtmobile.model.impl.XunlpcModelImpl;
import com.zondy.jwt.jwtmobile.presenter.IXunlpcPresenter;
import com.zondy.jwt.jwtmobile.view.IXunlpcView;

import java.util.List;

/**
 * Created by ywj on 2017/3/16 0016.
 */

public class XunlpcPresenter implements IXunlpcPresenter {
    IXunlpcView xunlpcView;
    IXunlpcModel xunlpcModel;

    public XunlpcPresenter(IXunlpcView xunlpcView){
        this.xunlpcView = xunlpcView;
        xunlpcModel = new XunlpcModelImpl();
    }

    @Override
    public void searchGuijWithLvg(String startTime, String endTime, String userId) {
        xunlpcModel.queryGuijWithLvg(userId, startTime, endTime, new IXunlpcQueryGuijWithLvgCallBack() {
            @Override
            public void queryLvgSuccess(List<EntityGuijWithLvg> guijDatas) {
                xunlpcView.showGuijWithLvgInMap(guijDatas);
            }

            @Override
            public void queryLvgFail(Exception e) {

            }
        });
    }

    @Override
    public void searchGuijWithWangb(String startTime, String endTime, String userId) {

    }
}
