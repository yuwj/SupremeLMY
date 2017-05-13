package com.zondy.jwt.jwtmobile.model.impl;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zondy.jwt.jwtmobile.base.MyApplication;
import com.zondy.jwt.jwtmobile.callback.IGPSUploadCallback;
import com.zondy.jwt.jwtmobile.callback.ILoginCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryUnacceptBufbkIdsCallback;
import com.zondy.jwt.jwtmobile.callback.IUpdateDLSSXXCallback;
import com.zondy.jwt.jwtmobile.callback.IUpdatePasswordCallback;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.manager.UrlManager;
import com.zondy.jwt.jwtmobile.model.ILoginModel;
import com.zondy.jwt.jwtmobile.util.GsonUtil;
import com.zondy.jwt.jwtmobile.util.SDCardUtil;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;


/**
 * Created by sheep on 2016/12/23.
 */

public class LoginModelImpl implements ILoginModel {

    @Override
    public void login(String username, final String password, final String simid, final ILoginCallback loginCallback) {

        if(MyApplication.IS_Test_json){
            UrlManager.LOGIN = UrlManager.LOGIN.replace("/","");
        }
        String url = UrlManager.getSERVER() + UrlManager.LOGIN;
        JSONObject param=new JSONObject();
        try {
            param.put("userName",username);
            param.put("password",password);
            param.put("simid",simid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build().execute(new Callback() {
                @Override
                public Object parseNetworkResponse(Response response, int id) throws Exception {



                    String string=response.body().string();
                    if(MyApplication.IS_Test_json){
                        string = string.substring(1,string.length()-1);
                        string = string.replace("\\","");
                    }

                    JSONObject object=new JSONObject(string);

                    int resultCode=object.optInt("result");
                    String msg=object.optString("message");
                    switch (resultCode){
                        case 1:
                            EntityUser entityUser=new Gson().fromJson(string,EntityUser.class);
                            return entityUser;
                        default:
                            return msg;
                    }
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    loginCallback.loginFailed();
                }

                @Override
                public void onResponse(Object response, int id) {
                    if(response instanceof EntityUser){
                        EntityUser entityUser= (EntityUser) response;
                        entityUser.setPassword(password);
                        loginCallback.loginSuccess(entityUser);
                    }else {
                        loginCallback.loginUnSuccessed((String) response);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadGPS(String username, String simid, double longitude, double latitude, final IGPSUploadCallback gpsUploadCallback) {
        final StringBuffer sb = new StringBuffer();
        final String url = UrlManager.getSERVER() + UrlManager.uploadGPS;
        sb.append("\n\nurl:"+url);
        JSONObject param=new JSONObject();
        try {
            param.put("jh",username);
            param.put("simid",simid);
            param.put("x",longitude);
            param.put("y",latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\nparam:"+param);
        try {
            OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build().execute(new Callback<String>() {
                @Override
                public String parseNetworkResponse(Response response, int id) throws Exception {


                    String string=response.body().string();
                    sb.append("\n\nresponse:"+string);
                    JSONObject object=new JSONObject(string);
                    int resultCode=object.optInt("result");
                    String msg=object.optString("message");
                    switch (resultCode){
                        case 1:
                            return msg;
                        default:
                            throw new Exception(msg);
                    }
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    sb.append("\n\nresponse error:"+e.getMessage());
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    gpsUploadCallback.uploadFail(e);
                }

                @Override
                public void onResponse(String response, int id) {
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    gpsUploadCallback.uploadSuccess();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDLSSXX(String username, String simid, final IUpdateDLSSXXCallback updateDLSSXXCallback) {
        final StringBuffer sb = new StringBuffer();
        final String url = UrlManager.getSERVER() + UrlManager.uploadDlxx;
        sb.append("\n\nurl:"+url);
        JSONObject param=new JSONObject();
        try {
            param.put("jh",username);
            param.put("simid",simid);
        } catch (JSONException e) {
            e.printStackTrace();
        }sb.append("\n\n param:"+param.toString());
        try {
            OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build().execute(new Callback<String>() {
                @Override
                public String parseNetworkResponse(Response response, int id) throws Exception {
                    String string=response.body().string();
                    sb.append("\n\n resp:"+string);
                    JSONObject object=new JSONObject(string);
                    int resultCode=object.optInt("result");
                    String msg=object.optString("message");
                    switch (resultCode){
                        case 1:
                            return msg;
                        default:
                            throw new Exception(msg);
                    }
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    sb.append("\n\n resp error:"+e.getMessage());
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    updateDLSSXXCallback.updateFail(e);
                }

                @Override
                public void onResponse(String response, int id) {
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    updateDLSSXXCallback.updateSuccess();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void queryUnacceptBufbkIds(String jh,String simid,String xingm, final IQueryUnacceptBufbkIdsCallback queryUnacceptBufbkIdsCallback) {
        final StringBuffer sb = new StringBuffer();
        final String url = UrlManager.getSERVER() + UrlManager.queryUnacceptBufbkIds;
        sb.append("\n\n url:"+url);
        final JSONObject param=new JSONObject();
        try {
            param.put("jh",jh);
            param.put("simid",simid);
            param.put("xingm",xingm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n param:"+param.toString());
        try {
            OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build().execute(new Callback<List<String>>() {
                @Override
                public List<String> parseNetworkResponse(Response response, int id) throws Exception {
                    String string=response.body().string();
                    sb.append("\n\n resp:"+string);
                    JSONObject object=new JSONObject(string);
                    int resultCode=object.optInt("result");
                    String msg=object.optString("message");
                    switch (resultCode){
                        case 1:
                            List<String> list = new ArrayList<String>();
                            String res = object.optString("unaccptBufbkIds");
                            if(!TextUtils.isEmpty(res)){
                                String[] ids = res.split(",");
                                list = Arrays.asList(ids);
                            }
                            return list;
                        default:
                            throw new Exception(msg);
                    }
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    sb.append("\n\n resp error:"+e.getMessage());
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    queryUnacceptBufbkIdsCallback.queryUnacceptBufbkIdsFail(e);
                }

                @Override
                public void onResponse(List<String> response, int id) {
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    queryUnacceptBufbkIdsCallback.queryUnacceptBufbkIdsSuccess(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}