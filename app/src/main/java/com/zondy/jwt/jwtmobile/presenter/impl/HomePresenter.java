package com.zondy.jwt.jwtmobile.presenter.impl;

import com.zondy.jwt.jwtmobile.callback.ILogoutCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryUnacceptBufbkCountCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryUnacceptJingqCountCallback;
import com.zondy.jwt.jwtmobile.model.IBufbkModel;
import com.zondy.jwt.jwtmobile.model.IJingqHandleModel;
import com.zondy.jwt.jwtmobile.model.ISettingModel;
import com.zondy.jwt.jwtmobile.model.impl.BufbkModelImpl;
import com.zondy.jwt.jwtmobile.model.impl.JingqHandleModelImpl;
import com.zondy.jwt.jwtmobile.model.impl.SettingModelImpl;
import com.zondy.jwt.jwtmobile.presenter.IHomePresenter;
import com.zondy.jwt.jwtmobile.view.IHomeView;

/**
 * Created by ywj on 2017/3/28 0028.
 */

public class HomePresenter implements IHomePresenter{
    ISettingModel settingModel;
    IBufbkModel bufbkModel;
    IJingqHandleModel jingqHandleModel;
    IHomeView homeView;


    public HomePresenter(IHomeView homeView){
        this.homeView = homeView;
        settingModel = new SettingModelImpl();
        bufbkModel = new BufbkModelImpl();
        jingqHandleModel = new JingqHandleModelImpl();
    }

    @Override
    public void queryUnacceptJingqCount(String jh, String simid, String zzjgdm) {
        jingqHandleModel.queryUnacceptJingqCount(jh,simid,zzjgdm,new IQueryUnacceptJingqCountCallback() {
            @Override
            public void queryUnacceptJingqCountSuccess(int count) {
                homeView.queryUnacceptJingqCountSuccess(count);
            }

            @Override
            public void queryUnacceptJingqCountFail(Exception e) {
                homeView.queryUnacceptJingqCountFail(e);
            }
        });
    }

    @Override
    public void queryUnacceptBufbkCount(String jh, String simid, String xingm) {
        bufbkModel.queryUnacceptBufbkCount(jh, simid, xingm, new IQueryUnacceptBufbkCountCallback() {
            @Override
            public void queryUnacceptBufbkCountSuccess(int count) {
                homeView.queryUnacceptBufbkCountSuccess(count);
            }

            @Override
            public void queryUnacceptBufbkCountFail(Exception e) {
                homeView.queryUnacceptBufbkCountFail(e);
            }
        });
    }

    @Override
    public void queryUnacceptTongzggCount(String jh, String simid, String xingm) {

    }

    @Override
    public void logout(String jh, String simid) {
        settingModel.logout(jh, simid, new ILogoutCallback() {
            @Override
            public void logoutSuccessed() {
                homeView.logoutSuccess();
            }

            @Override
            public void logoutUnSuccessed() {
                homeView.logoutFail(null);

            }
        });

    }
}
