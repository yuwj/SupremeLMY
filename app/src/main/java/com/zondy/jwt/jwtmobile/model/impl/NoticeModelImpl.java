package com.zondy.jwt.jwtmobile.model.impl;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zondy.jwt.jwtmobile.callback.IQueryNoticeDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryNoticeListCallback;
import com.zondy.jwt.jwtmobile.entity.EntityBaseResponse;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.manager.UrlManager;
import com.zondy.jwt.jwtmobile.model.INoticeModel;
import com.zondy.jwt.jwtmobile.util.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public class NoticeModelImpl implements INoticeModel {

    @Override
    public void queryNoticeList(int noticeType, String jh, String simid, final IQueryNoticeListCallback queryNoticeListCallback) {
        String url = UrlManager.getSERVER() + UrlManager.queryXiectsxxList;
        JSONObject param=new JSONObject();
        try {
            param.put("jh",jh);
            param.put("simid",simid);
            param.put("noticeType",noticeType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build().execute(new Callback<List<EntityNotice>>() {
                @Override
                public List<EntityNotice> parseNetworkResponse(Response response, int id) throws Exception {
                    String string=response.body().string();
                    JSONObject object=new JSONObject(string);
                    int resultCode=object.optInt("result");
                    String msg=object.optString("message");
                    String noticeListStr = object.optString("noticeListStr");
                    switch (resultCode){
                        case 1:
                            List<EntityNotice> datas = GsonUtil.json2BeanList(noticeListStr, EntityNotice.class);
                            return  datas;
                        default:
                            throw new Exception(msg);
                    }
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    queryNoticeListCallback.queryNoticeListFail(e);
                }

                @Override
                public void onResponse(List<EntityNotice> response, int id) {
                    queryNoticeListCallback.queryNoticeListSuccess(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void queryNoticeDetail(String jh, String simid, String noticeId, final IQueryNoticeDetailCallback queryNoticeDetailCallback) {
        String url = UrlManager.getSERVER() + UrlManager.queryXiectsxxDetailByID;
        JSONObject param=new JSONObject();
        try {
            param.put("jh",jh);
            param.put("simid",simid);
            param.put("noticeId",noticeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build().execute(new Callback<EntityNotice>() {
                @Override
                public EntityNotice parseNetworkResponse(Response response, int id) throws Exception {
                    String string=response.body().string();
                    JSONObject object=new JSONObject(string);
                    int resultCode=object.optInt("result");
                    String msg=object.optString("message");
                    String noticeDetailStr = object.optString("noticeDetailStr");
                    switch (resultCode){
                        case 1:
                            EntityNotice data = GsonUtil.json2Bean(noticeDetailStr, EntityNotice.class);
                            return  data;
                        default:
                            throw new Exception(msg);
                    }
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    queryNoticeDetailCallback.onQueryNoticeDetailFail(e);
                }

                @Override
                public void onResponse(EntityNotice response, int id) {
                    queryNoticeDetailCallback.onQueryNoticeDetailSuccess(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
