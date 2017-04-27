package com.zondy.jwt.jwtmobile.model.impl;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.zondy.jwt.jwtmobile.base.MyApplication;
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
import com.zondy.jwt.jwtmobile.entity.EntityAskBukfk;
import com.zondy.jwt.jwtmobile.entity.EntityAskChax;
import com.zondy.jwt.jwtmobile.entity.EntityAskQit;
import com.zondy.jwt.jwtmobile.entity.EntityAskService;
import com.zondy.jwt.jwtmobile.entity.EntityAskZengy;
import com.zondy.jwt.jwtmobile.entity.EntityAskZous;
import com.zondy.jwt.jwtmobile.manager.UrlManager;
import com.zondy.jwt.jwtmobile.model.IAskForServiceModel;
import com.zondy.jwt.jwtmobile.util.GsonUtil;
import com.zondy.jwt.jwtmobile.util.SDCardUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by ywj on 2017/4/13 0013.
 */

public class AskForServiceModelImpl extends BaseModelImpl implements IAskForServiceModel {
    @Override
    public void queryAskServiceList(String jh, String simid, String zzjgdm, int pageSize, int pageNo, final IQueryAskServiceListCallback queryAskServiceListCallback) {

        final StringBuffer sb = new StringBuffer();
        final String url = UrlManager.getSERVER() + UrlManager.queryAskServiceList;
        sb.append("\n\nurl:" + url);
        Map<String, String> params = new HashMap<>();
        params.put("jh",jh);
        params.put("simid",simid);
        params.put("zzjgdm",zzjgdm);
        params.put("pageSize",pageSize+"");
        params.put("pageNo",pageNo+"");

        sb.append("\n\n params:" + params.toString());
        try {
            OkGo.post(url).params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            sb.append("\n\n resp" + s);

                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                            try {

                                JSONObject respObj = new JSONObject(s);
                                int result = respObj.optInt("resultCode",0);
                                String message = respObj.optString("resultMessage","服务端未返回错误信息");
                                switch (result){
                                    case 0:
                                        queryAskServiceListCallback.onQueryAskServiceListFailed(new Exception(message));
                                        break;
                                    case 1:
                                        String respStr = respObj.optString("resultData","");
                                        List<EntityAskService> askServiceList = GsonUtil.json2BeanList(respStr,EntityAskService.class);
                                        if(askServiceList != null && askServiceList.size() > 0){
                                            queryAskServiceListCallback.onQueryAskServiceListSuccess(askServiceList);
                                        }else{
                                            queryAskServiceListCallback.onQueryAskServiceListFailed(new Exception("暂无数据"));
                                        }

                                        break;
                                    case 2:
                                        queryAskServiceListCallback.onQueryAskServiceListFailed(new Exception(message));
                                        break;

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                queryAskServiceListCallback.onQueryAskServiceListFailed(e);
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            sb.append("\n\n resp" + e.getMessage());
                            super.onError(call, response, e);
                            queryAskServiceListCallback.onQueryAskServiceListFailed(e);
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("\n\n req error:" + e.getMessage());
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
            queryAskServiceListCallback.onQueryAskServiceListFailed(e);
        }


    }


    @Override
    public void queryAskBukDetail(String jh, String simid, String askBukServiceId, final IQueryAskBukDetailCallback queryAskBukDetailCallback) {

        final StringBuffer sb = new StringBuffer();
        final String url = UrlManager.getSERVER() + UrlManager.queryAskBukDetail;
        sb.append("\n\nurl:" + url);
        Map<String, String> params = new HashMap<>();
        params.put("jh",jh);
        params.put("simid",simid);
        params.put("askBukServiceId",askBukServiceId);

        sb.append("\n\n params:" + params.toString());
        try {
            OkGo.post(url).params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            sb.append("\n\n resp" + s);
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                            try {
                                EntityAskBuk askedBukDetail = (EntityAskBuk) PaseData(s, EntityAskBuk.class);
                                if (queryAskBukDetailCallback != null) {
                                    queryAskBukDetailCallback.onQueryAskBukDetailSuccess(askedBukDetail);
                                }

                                JSONObject respObj = new JSONObject(s);
                                int result = respObj.optInt("resultCode",0);
                                String message = respObj.optString("resultMessage","服务端未返回错误信息");
                                switch (result){
                                    case 0:
                                        queryAskBukDetailCallback.onQueryAskBukDetailFail(new Exception(message));
                                        break;
                                    case 1:
                                        String respStr = respObj.optString("resultData","");
                                        EntityAskBuk askBuk = GsonUtil.json2Bean(respStr,EntityAskBuk.class);
                                        if(askBuk != null){
                                            queryAskBukDetailCallback.onQueryAskBukDetailSuccess(askBuk);
                                        }else{
                                            queryAskBukDetailCallback.onQueryAskBukDetailFail(new Exception("暂无数据"));
                                        }

                                        break;
                                    case 2:
                                        queryAskBukDetailCallback.onQueryAskBukDetailFail(new Exception(message));
                                        break;

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (queryAskBukDetailCallback != null) {
                                    queryAskBukDetailCallback.onQueryAskBukDetailFail(e);
                                }
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            sb.append("\n\n resp" + e.getMessage());
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                            super.onError(call, response, e);
                            if (queryAskBukDetailCallback != null) {
                                queryAskBukDetailCallback.onQueryAskBukDetailFail(e);
                            }
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("\n\n req error:" + e.getMessage());
            queryAskBukDetailCallback.onQueryAskBukDetailFail(e);
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
        }
    }

    @Override
    public void queryAskZengyDetail(String jh, String simid, String askZengyId, final IQueryAskZengyDetailCallback queryAskZengyDetailCallback) {

        final StringBuffer sb = new StringBuffer();
        final String url = UrlManager.getSERVER() + UrlManager.queryAskZengyDetail;
        sb.append("\n\nurl:" + url);
        Map<String, String> params = new HashMap<>();
        params.put("jh",jh);
        params.put("simid",simid);
        params.put("askZengyId",askZengyId);

        sb.append("\n\n params:" + params.toString());
        try {
            OkGo.post(url).params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            sb.append("\n\n resp" + s);
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                            try {
                                EntityAskZengy askZengyDetail = (EntityAskZengy) PaseData(s, EntityAskZengy.class);
                                if (queryAskZengyDetailCallback != null) {
                                    queryAskZengyDetailCallback.onQueryAskZengyDetailSuccess(askZengyDetail);
                                }

                                JSONObject respObj = new JSONObject(s);
                                int result = respObj.optInt("resultCode",0);
                                String message = respObj.optString("resultMessage","服务端未返回错误信息");
                                switch (result){
                                    case 0:
                                        queryAskZengyDetailCallback.onQueryAskZengyDetailFail(new Exception(message));
                                        break;
                                    case 1:
                                        String respStr = respObj.optString("resultData","");
                                        EntityAskZengy askZengy = GsonUtil.json2Bean(respStr,EntityAskZengy.class);
                                        if(askZengy != null){
                                            queryAskZengyDetailCallback.onQueryAskZengyDetailSuccess(askZengy);
                                        }else{
                                            queryAskZengyDetailCallback.onQueryAskZengyDetailFail(new Exception("暂无数据"));
                                        }

                                        break;
                                    case 2:
                                        queryAskZengyDetailCallback.onQueryAskZengyDetailFail(new Exception(message));
                                        break;

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (queryAskZengyDetailCallback != null) {
                                    queryAskZengyDetailCallback.onQueryAskZengyDetailFail(e);
                                }
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            sb.append("\n\n resp" + e.getMessage());
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                            super.onError(call, response, e);
                            if (queryAskZengyDetailCallback != null) {
                                queryAskZengyDetailCallback.onQueryAskZengyDetailFail(e);
                            }
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("\n\n req error:" + e.getMessage());
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
            queryAskZengyDetailCallback.onQueryAskZengyDetailFail(e);
        }
    }

    @Override
    public void queryAskChaxDetail(String jh, String simid, String askChaxId, final IQueryAskChaxDetailCallback queryAskChaxDetailCallback) {

        final StringBuffer sb = new StringBuffer();
        final String url = UrlManager.getSERVER() + UrlManager.queryAskChaxDetail;
        sb.append("\n\nurl:" + url);
        Map<String, String> params = new HashMap<>();
        params.put("jh",jh);
        params.put("simid",simid);
        params.put("askChaxId",askChaxId);

        sb.append("\n\n params:" + params.toString());
        try {
            OkGo.post(url).params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            sb.append("\n\n resp" + s);
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                            try {
                                EntityAskChax askChaxDetail = (EntityAskChax) PaseData(s, EntityAskChax.class);
                                if (queryAskChaxDetailCallback != null) {
                                    queryAskChaxDetailCallback.onQueryAskChaxDetailSuccess(askChaxDetail);
                                }

                                JSONObject respObj = new JSONObject(s);
                                int result = respObj.optInt("resultCode",0);
                                String message = respObj.optString("resultMessage","服务端未返回错误信息");
                                switch (result){
                                    case 0:
                                        queryAskChaxDetailCallback.onQueryAskChaxDetailFail(new Exception(message));
                                        break;
                                    case 1:
                                        String respStr = respObj.optString("resultData","");
                                        EntityAskChax askChax = GsonUtil.json2Bean(respStr,EntityAskChax.class);
                                        if(askChax != null){
                                            queryAskChaxDetailCallback.onQueryAskChaxDetailSuccess(askChax);
                                        }else{
                                            queryAskChaxDetailCallback.onQueryAskChaxDetailFail(new Exception("暂无数据"));
                                        }

                                        break;
                                    case 2:
                                        queryAskChaxDetailCallback.onQueryAskChaxDetailFail(new Exception(message));
                                        break;

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (queryAskChaxDetailCallback != null) {
                                    queryAskChaxDetailCallback.onQueryAskChaxDetailFail(e);
                                }
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            sb.append("\n\n resp" + e.getMessage());
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                            super.onError(call, response, e);
                            if (queryAskChaxDetailCallback != null) {
                                queryAskChaxDetailCallback.onQueryAskChaxDetailFail(e);
                            }
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("\n\n req error:" + e.getMessage());
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
            queryAskChaxDetailCallback.onQueryAskChaxDetailFail(e);
        }
    }

    @Override
    public void queryAskZousDetail(String jh, String simid, String askZousId, final IQueryAskZousDetailCallback queryAskZousDetailCallback) {

        final StringBuffer sb = new StringBuffer();
        final String url = UrlManager.getSERVER() + UrlManager.queryAskZousDetail;
        sb.append("\n\nurl:" + url);
        Map<String, String> params = new HashMap<>();
        params.put("jh",jh);
        params.put("simid",simid);
        params.put("askZousId",askZousId);

        sb.append("\n\n params:" + params.toString());
        try {
            OkGo.post(url).params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            sb.append("\n\n resp" + s);
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                            try {
                                EntityAskZous askZousDetail = (EntityAskZous) PaseData(s, EntityAskZous.class);
                                if (queryAskZousDetailCallback != null) {
                                    queryAskZousDetailCallback.onQueryAskZousDetailSuccess(askZousDetail);
                                }

                                JSONObject respObj = new JSONObject(s);
                                int result = respObj.optInt("resultCode",0);
                                String message = respObj.optString("resultMessage","服务端未返回错误信息");
                                switch (result){
                                    case 0:
                                        queryAskZousDetailCallback.onQueryAskZousDetailFail(new Exception(message));
                                        break;
                                    case 1:
                                        String respStr = respObj.optString("resultData","");
                                        EntityAskZous askZous = GsonUtil.json2Bean(respStr,EntityAskZous.class);
                                        if(askZous != null){
                                            queryAskZousDetailCallback.onQueryAskZousDetailSuccess(askZous);
                                        }else{
                                            queryAskZousDetailCallback.onQueryAskZousDetailFail(new Exception("暂无数据"));
                                        }

                                        break;
                                    case 2:
                                        queryAskZousDetailCallback.onQueryAskZousDetailFail(new Exception(message));
                                        break;

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (queryAskZousDetailCallback != null) {
                                    queryAskZousDetailCallback.onQueryAskZousDetailFail(e);
                                }
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            sb.append("\n\n resp" + e.getMessage());
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                            super.onError(call, response, e);
                            if (queryAskZousDetailCallback != null) {
                                queryAskZousDetailCallback.onQueryAskZousDetailFail(e);
                            }
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("\n\n req error:" + e.getMessage());
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
            queryAskZousDetailCallback.onQueryAskZousDetailFail(e);
        }
    }

    @Override
    public void queryAskQitDetail(String jh, String simid, String askQitId, final IQueryAskQitDetailCallback queryAskQitDetailCallback) {

        final StringBuffer sb = new StringBuffer();
        final String url = UrlManager.getSERVER() + UrlManager.queryAskQitDetail;
        sb.append("\n\nurl:" + url);
        Map<String, String> params = new HashMap<>();
        params.put("jh",jh);
        params.put("simid",simid);
        params.put("askQitId",askQitId);

        sb.append("\n\n params:" + params.toString());
        try {
            OkGo.post(url).params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            sb.append("\n\n resp" + s);
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                            try {
                                EntityAskQit askQitDetail = (EntityAskQit) PaseData(s, EntityAskQit.class);
                                if (queryAskQitDetailCallback != null) {
                                    queryAskQitDetailCallback.onQueryAskQitDetailSuccess(askQitDetail);
                                }

                                JSONObject respObj = new JSONObject(s);
                                int result = respObj.optInt("resultCode",0);
                                String message = respObj.optString("resultMessage","服务端未返回错误信息");
                                switch (result){
                                    case 0:
                                        queryAskQitDetailCallback.onQueryAskQitDetailFail(new Exception(message));
                                        break;
                                    case 1:
                                        String respStr = respObj.optString("resultData","");
                                        EntityAskQit askQit = GsonUtil.json2Bean(respStr,EntityAskQit.class);
                                        if(askQit != null){
                                            queryAskQitDetailCallback.onQueryAskQitDetailSuccess(askQit);
                                        }else{
                                            queryAskQitDetailCallback.onQueryAskQitDetailFail(new Exception("暂无数据"));
                                        }

                                        break;
                                    case 2:
                                        queryAskQitDetailCallback.onQueryAskQitDetailFail(new Exception(message));
                                        break;

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (queryAskQitDetailCallback != null) {
                                    queryAskQitDetailCallback.onQueryAskQitDetailFail(e);
                                }
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            sb.append("\n\n resp" + e.getMessage());
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                            super.onError(call, response, e);
                            if (queryAskQitDetailCallback != null) {
                                queryAskQitDetailCallback.onQueryAskQitDetailFail(e);
                            }
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("\n\n req error:" + e.getMessage());
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
            queryAskQitDetailCallback.onQueryAskQitDetailFail(e);
        }
    }


    @Override
    public void askBuk(String jh,String simid,EntityAskBuk entityAskBuk, final IAskBukCallback askBukCallback) {
        final StringBuffer sb = new StringBuffer();
        if(MyApplication.IS_Test_json){
            UrlManager.askBuk = UrlManager.askBuk.replace("/","");
        }
        final String url = UrlManager.getSERVER() + UrlManager.askBuk;
        sb.append("\n\nurl:" + url);
        Map<String, String> params = new HashMap<>();
        params.put("jh", jh);
        params.put("simid", simid);
        params.put("entityAskBuk", GsonUtil.bean2Json(entityAskBuk));
        sb.append("\n\n params:" + params.toString());
        try {
            OkGo.post(url).params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                            try {

                                    if(MyApplication.IS_Test_json){
                                        s= s.substring(1,s.length()-1);
                                        s = s.replace("\\","");
                                    }
                                JSONObject respObj = new JSONObject(s);
                                int result = respObj.optInt("result",0);
                                String message = respObj.optString("message","服务端未返回错误信息");
                                switch (result){
                                    case 0:
                                        askBukCallback.onAskBukFail(new Exception(message));
                                        break;
                                    case 1:
                                        askBukCallback.onAskBukSuccess();
                                        break;
                                    case 2:
                                        askBukCallback.onAskBukFail(new Exception(message));
                                        break;

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                askBukCallback.onAskBukFail(e);
                            }


                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            askBukCallback.onAskBukFail(e);
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("\n\n" + e.getMessage());
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());

            askBukCallback.onAskBukFail(e);
        }


    }

    @Override
    public void askZengy(String jh, String simid, EntityAskZengy entityAskZengy, final IAskZengyCallback askZengyCallback) {
        final StringBuffer sb = new StringBuffer();
        if(MyApplication.IS_Test_json){
            UrlManager.askZengy = UrlManager.askZengy.replace("/","");
        }
        final String url = UrlManager.getSERVER() + UrlManager.askZengy;
        sb.append("\n\nurl:" + url);
        Map<String, String> params = new HashMap<>();
        params.put("jh", jh);
        params.put("simid", simid);
        params.put("entityAskZengy", GsonUtil.bean2Json(entityAskZengy));
        sb.append("\n\n params:" + params.toString());
        try {
            OkGo.post(url).params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                            try {
                                if(MyApplication.IS_Test_json){
                                     s= s.substring(1,s.length()-1);
                                    s = s.replace("\\","");
                                }
                                JSONObject respObj = new JSONObject(s);
                                int result = respObj.optInt("result",0);
                                String message = respObj.optString("message","服务端未返回错误信息");
                                switch (result){
                                    case 0:
                                        askZengyCallback.onAskZengyFail(new Exception(message));
                                        break;
                                    case 1:
                                        askZengyCallback.onAskZengySuccess();
                                        break;
                                    case 2:
                                        askZengyCallback.onAskZengyFail(new Exception(message));
                                        break;

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                askZengyCallback.onAskZengyFail(e);
                            }


                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            if (askZengyCallback != null) {
                                askZengyCallback.onAskZengyFail(e);
                            }
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
            askZengyCallback.onAskZengyFail(e);
            sb.append("\n\n" + e.getMessage());
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
        }


    }

    @Override
    public void askChax(String jh, String simid, EntityAskChax entityAskChax, final IAskChaxCallback askChaxCallback) {
        final StringBuffer sb = new StringBuffer();

        if(MyApplication.IS_Test_json){
            UrlManager.askChax = UrlManager.askChax.replace("/","");
        }
        final String url = UrlManager.getSERVER() + UrlManager.askChax;
        sb.append("\n\nurl:" + url);
        Map<String, String> params = new HashMap<>();
        params.put("jh", jh);
        params.put("simid", simid);
        params.put("entityAskChax", GsonUtil.bean2Json(entityAskChax));
        sb.append("\n\n params:" + params.toString());
        try {
            OkGo.post(url).params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                            try {
                                if(MyApplication.IS_Test_json){
                                    s= s.substring(1,s.length()-1);
                                    s = s.replace("\\","");
                                }
                                JSONObject respObj = new JSONObject(s);
                                int result = respObj.optInt("result",0);
                                String message = respObj.optString("message","服务端未返回错误信息");
                                switch (result){
                                    case 0:
                                        askChaxCallback.onAskChaxFail(new Exception(message));
                                        break;
                                    case 1:
                                        askChaxCallback.onAskChaxSuccess();
                                        break;
                                    case 2:
                                        askChaxCallback.onAskChaxFail(new Exception(message));
                                        break;

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                if (askChaxCallback != null) {
                                    askChaxCallback.onAskChaxFail(e);
                                }
                            }


                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            if (askChaxCallback != null) {
                                askChaxCallback.onAskChaxFail(e);
                            }
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
            askChaxCallback.onAskChaxFail(e);
            sb.append("\n\n" + e.getMessage());
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
        }


    }

    @Override
    public void askZous(String jh, String simid, EntityAskZous entityAskZous, final IAskZousCallback askZousCallback) {
        final StringBuffer sb = new StringBuffer();

        if(MyApplication.IS_Test_json){
            UrlManager.askZous = UrlManager.askZous.replace("/","");
        }
        final String url = UrlManager.getSERVER() + UrlManager.askZous;
        sb.append("\n\nurl:" + url);
        Map<String, String> params = new HashMap<>();
        params.put("jh", jh);
        params.put("simid", simid);
        params.put("entityAskZous", GsonUtil.bean2Json(entityAskZous));
        sb.append("\n\n params:" + params.toString());
        try {
            OkGo.post(url).params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                            try {
                                if(MyApplication.IS_Test_json){
                                    s= s.substring(1,s.length()-1);
                                    s = s.replace("\\","");
                                }
                                JSONObject respObj = new JSONObject(s);
                                int result = respObj.optInt("result",0);
                                String message = respObj.optString("message","服务端未返回错误信息");
                                switch (result){
                                    case 0:
                                        askZousCallback.onAskZousFail(new Exception(message));
                                        break;
                                    case 1:
                                        askZousCallback.onAskZousSuccess();
                                        break;
                                    case 2:
                                        askZousCallback.onAskZousFail(new Exception(message));
                                        break;

                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                                askZousCallback.onAskZousFail(e);
                            }


                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);

                            askZousCallback.onAskZousFail(e);
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
            askZousCallback.onAskZousFail(e);
            sb.append("\n\n" + e.getMessage());
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
        }


    }

    @Override
    public void askQit(String jh, String simid, EntityAskQit entityAskQit, final IAskQitCallback askQitCallback) {
        final StringBuffer sb = new StringBuffer();

        if(MyApplication.IS_Test_json){
            UrlManager.askQit = UrlManager.askQit.replace("/","");
        }
        final String url = UrlManager.getSERVER() + UrlManager.askQit;
        sb.append("\n\nurl:" + url);
        Map<String, String> params = new HashMap<>();
        params.put("jh", jh);
        params.put("simid", simid);
        params.put("entityAskQit", GsonUtil.bean2Json(entityAskQit));
        sb.append("\n\n params:" + params.toString());
        try {
            OkGo.post(url).params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                            try {
                                if(MyApplication.IS_Test_json){
                                    s= s.substring(1,s.length()-1);
                                    s = s.replace("\\","");
                                }
                                JSONObject respObj = new JSONObject(s);
                                int result = respObj.optInt("result",0);
                                String message = respObj.optString("message","服务端未返回错误信息");
                                switch (result){
                                    case 0:
                                        askQitCallback.onAskQitFail(new Exception(message));
                                        break;
                                    case 1:
                                        askQitCallback.onAskQitSuccess();
                                        break;
                                    case 2:
                                        askQitCallback.onAskQitFail(new Exception(message));
                                        break;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                                askQitCallback.onAskQitFail(e);
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            askQitCallback.onAskQitFail(e);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            askQitCallback.onAskQitFail(e);
            sb.append("\n\n" + e.getMessage());
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
        }
    }
}
