package com.zondy.jwt.jwtmobile.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.zondy.jwt.jwtmobile.callback.IGPSUploadCallback;
import com.zondy.jwt.jwtmobile.callback.ILoginCallback;
import com.zondy.jwt.jwtmobile.callback.ILogoutCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryUnacceptBufbkIdsCallback;
import com.zondy.jwt.jwtmobile.callback.IUpdateDLSSXXCallback;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.manager.JWTNotifyManager;
import com.zondy.jwt.jwtmobile.model.ILoginModel;
import com.zondy.jwt.jwtmobile.model.impl.LoginModelImpl;
import com.zondy.jwt.jwtmobile.presenter.ILoginPresenter;
import com.zondy.jwt.jwtmobile.service.PollService;
import com.zondy.jwt.jwtmobile.view.ILoginView;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by sheep on 2016/12/23.
 */

public class LoginPresenterImpl implements ILoginPresenter{
    String tag = getClass().getSimpleName();
    private ILoginView loginView;
    private ILoginModel loginModel;
    public LoginPresenterImpl(ILoginView loginView){
        super();
        this.loginView=loginView;
        loginModel=new LoginModelImpl();
    }

    public LoginPresenterImpl(){
        super();
        loginModel=new LoginModelImpl();
    }

    /**
     * 登录
     * @param username
     * @param password
     * @param simid
     */
    @Override
    public void login(final Context context, String username, String password, String simid) {
        loginModel.login(username, password, simid, new ILoginCallback() {
            @Override
            public void loginSuccess(EntityUser entityUser) {
                loginView.loginSuccessed(entityUser);
                context.startService(PollService.createIntent(context,30,true));
            }

            @Override
            public void loginUnSuccessed(String msg) {
                loginView.loginUnSuccessed(msg);
            }

            @Override
            public void loginFailed() {
                loginView.loginFailed();
            }
        });
    }

    @Override
    public void uploadGPS(String username, String simid, double longitude, double latitude) {
        loginModel.uploadGPS(username, simid, longitude, latitude, new IGPSUploadCallback() {
            @Override
            public void uploadSuccess() {
                Log.d(tag, "uploadSuccess: ");
            }

            @Override
            public void uploadFail(Exception e) {

                Log.d(tag, "uploadFail: "+e.getMessage());
            }
        });
    }

    @Override
    public void updateDLSSXX(String username, String simid) {
        loginModel.updateDLSSXX(username, simid, new IUpdateDLSSXXCallback() {
            @Override
            public void updateSuccess() {

                Log.d(tag, "updateDLSSXXSuccess: ");
            }

            @Override
            public void updateFail(Exception e) {

                Log.d(tag, "updateDLSSXXFail: "+e.getMessage());
            }
        });
    }

    @Override
    public void queryUnacceptBufbkIds(final Context context,String jh,String simid, String xingm) {
        loginModel.queryUnacceptBufbkIds(jh, simid,xingm, new IQueryUnacceptBufbkIdsCallback() {
            @Override
            public void queryUnacceptBufbkIdsSuccess(List<String> bufbkIds) {
                Log.d(tag, "updateDLSSXXSuccess: ");
                //TODO 给手机发送通知
                JWTNotifyManager.getInstance(context).notifyUnacceptBufbkIds(bufbkIds);
            }

            @Override
            public void queryUnacceptBufbkIdsFail(Exception e) {

            }
        });
    }
}
