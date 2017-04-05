package com.zondy.jwt.jwtmobile.model.impl;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zondy.jwt.jwtmobile.callback.INoticeFeedbackCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryNoticeDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryNoticeListCallback;
import com.zondy.jwt.jwtmobile.entity.EntityBaseResponse;
import com.zondy.jwt.jwtmobile.entity.EntityFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityLocation;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.manager.UrlManager;
import com.zondy.jwt.jwtmobile.model.INoticeModel;
import com.zondy.jwt.jwtmobile.util.GsonUtil;
import com.zondy.jwt.jwtmobile.util.SDCardUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * Created by ywj on 2017/3/23 0023.
 */

public class NoticeModelImpl implements INoticeModel {
    String tag = this.getClass().getSimpleName();

    @Override
    public void queryNoticeList(int noticeType, String jh, String simid,String zzjgdm,int pageSize,int pageNo, final IQueryNoticeListCallback queryNoticeListCallback) {
        final String url = UrlManager.getSERVER() + UrlManager.queryNoticeDatas;
        final StringBuffer sb = new StringBuffer();
        sb.append("\n\nurl:"+url);
        JSONObject param=new JSONObject();
        try {
            param.put("jh",jh);
            param.put("simid",simid);
            param.put("messageType",noticeType);
            param.put("zzjgdm",zzjgdm);
            param.put("pageSize",pageSize);
            param.put("pageNo",pageNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n param:"+param.toString());
        try {
            OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build().execute(new Callback<List<EntityNotice>>() {
                @Override
                public List<EntityNotice> parseNetworkResponse(Response response, int id) throws Exception {
                    String string=response.body().string();
                    sb.append("\n\n response:"+string);
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    JSONObject object=new JSONObject(string);
                    int resultCode=object.optInt("result");
                    String msg=object.optString("message");
                    String noticeListStr = object.optString("tuisxxList");
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
                    boolean isTest= true;
                    if(isTest){
                        List<EntityNotice> ns = new ArrayList<EntityNotice>();
                        for(int i = 0;i<20;i++){
                            EntityNotice n= new EntityNotice();
                            n.setContent("aaa");
                            ns.add(n);
                        }

                        queryNoticeListCallback.queryNoticeListSuccess(ns);
                        return;
                    }


                    queryNoticeListCallback.queryNoticeListFail(e);
                    sb.append("\n\n responese:"+e.getMessage());

                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                }

                @Override
                public void onResponse(List<EntityNotice> response, int id) {
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    queryNoticeListCallback.queryNoticeListSuccess(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void queryNoticeDetail(String jh, String simid, String noticeId, final IQueryNoticeDetailCallback queryNoticeDetailCallback) {
        final String url = UrlManager.getSERVER() + UrlManager.queryNoticeFeedbackDatas;
        final StringBuffer sb = new StringBuffer();
        sb.append("\n\nurl:"+url);
        JSONObject param=new JSONObject();
        try {
            param.put("jh",jh);
            param.put("simid",simid);
            param.put("tuisxxId",noticeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n param:"+sb.toString());
        try {
            OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build().execute(new Callback<List<EntityFeedback>>() {
                @Override
                public List<EntityFeedback> parseNetworkResponse(Response response, int id) throws Exception {
                    String string=response.body().string();
                    sb.append("\n\n response:"+string);
                    JSONObject object=new JSONObject(string);
                    int resultCode=object.optInt("result");
                    String msg=object.optString("message");
                    String noticeDetailStr = object.optString("noticeDetailStr");
                    switch (resultCode){
                        case 1:
                            List<EntityFeedback> data = GsonUtil.json2BeanList(noticeDetailStr, EntityFeedback.class);
                            return  data;
                        default:
                            throw new Exception(msg);
                    }
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    sb.append("\n\n response:"+e);

                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    queryNoticeDetailCallback.onQueryNoticeDetailFail(e);
                }

                @Override
                public void onResponse(List<EntityFeedback> response, int id) {
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    queryNoticeDetailCallback.onQueryNoticeDetailSuccess(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void feedbackNotice(String noticeId, String jh, String simid, String feedbackStrInfo, String filesPath, final INoticeFeedbackCallback noticeFeedback) {
        final String url = UrlManager.getSERVER() + UrlManager.NoticeFeedback;
        final StringBuffer sb = new StringBuffer();
        sb.append("\n\nurl:"+url);
        List<File> files = new ArrayList<>();
        if(!TextUtils.isEmpty(filesPath)){
            String[] ss = filesPath.split(",");

            for(int i = 0;i< ss.length;i++){

                files.add(new File(ss[i]));
            }
        }

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("tuisxxId", noticeId);
            jsonParam.put("jh", jh);
            jsonParam.put("simid", simid);
            jsonParam.put("fkxx", feedbackStrInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.append("\n\n param:"+jsonParam.toString()+"\n"+filesPath);
        Map<String,String> param = new HashMap<>();
        param.put("strBody",jsonParam.toString());


        OkGo.post(url).tag(this)//
                .isMultipart(true)       // 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params("strBody",jsonParam.toString())        // 这里可以上传参数
                .addFileParams("images", files) // 这里支持一个key传多个文件
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //上传成功
                        sb.append("\n\n response:"+s);
                        JSONObject object= null;
                        try {
                            object = new JSONObject(s);
                            int resultCode=object.optInt("result");
                            String msg=object.optString("message");
                            String noticeDetailStr = object.optString("tuisxxFKBean");
                            switch (resultCode){
                                case 1:
                                    EntityFeedback data = GsonUtil.json2Bean(noticeDetailStr, EntityFeedback.class);
                                    noticeFeedback.noticeFeedbackSuccess(data);
                                default:
                                    noticeFeedback.noticeFeedbackFail(new Exception(msg));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            sb.append("\n\n response:"+e.getMessage());
                            noticeFeedback.noticeFeedbackFail(e);
                        }

                        SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());

                    }


                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        Log.d(tag,"进度"+progress);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        noticeFeedback.noticeFeedbackFail(e);
                        Log.d(tag,e.getMessage());
                        sb.append("\n\n response:"+e.getMessage());
                        SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    }
                });

    }
}
