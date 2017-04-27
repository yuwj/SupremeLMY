package com.zondy.jwt.jwtmobile.model.impl;

import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zondy.jwt.jwtmobile.base.MyApplication;
import com.zondy.jwt.jwtmobile.callback.IAcceptBufbkCallback;
import com.zondy.jwt.jwtmobile.callback.IBufbkFeedbackCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryBufbkDetailCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryBufbkListCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryUnacceptBufbkCountCallback;
import com.zondy.jwt.jwtmobile.entity.EntityBufbkFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityBufbk;
import com.zondy.jwt.jwtmobile.manager.UrlManager;
import com.zondy.jwt.jwtmobile.model.IBufbkModel;
import com.zondy.jwt.jwtmobile.model.IBufbkModel;
import com.zondy.jwt.jwtmobile.util.GsonUtil;
import com.zondy.jwt.jwtmobile.util.SDCardUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

public class BufbkModelImpl implements IBufbkModel {
    String tag = this.getClass().getSimpleName();

    @Override
    public void acceptBufbk(String jh, String simid, String xingm, final IAcceptBufbkCallback acceptBufbkCallback) {
        final String url = UrlManager.getSERVER() + UrlManager.acceptBufbk;
        final StringBuffer sb = new StringBuffer();
        sb.append("\n\nurl:"+url);
        JSONObject param=new JSONObject();
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
                    .build().execute(new Callback<Boolean>() {
                @Override
                public Boolean parseNetworkResponse(Response response, int id) throws Exception {
                    String string=response.body().string();
                    sb.append("\n\n response:"+string);
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    JSONObject object=new JSONObject(string);
                    int resultCode=object.optInt("result");
                    String msg=object.optString("message");
                    switch (resultCode){
                        case 1:

                            boolean acceptBufbkResult = object.optBoolean("acceptBufbkResult");
                            return  acceptBufbkResult;
                        default:
                            throw new Exception(msg);
                    }
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    acceptBufbkCallback.acceptBufbkFailed(e);
                    sb.append("\n\n responese:"+e.getMessage());

                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                }

                @Override
                public void onResponse(Boolean response, int id) {
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    acceptBufbkCallback.acceptBufbkSuccess();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void queryBufbkList(String jh, String simid,String xingm,int pageSize,int pageNo, final IQueryBufbkListCallback queryBufbkListCallback) {
        if(MyApplication.IS_Test_json){
            UrlManager.queryBufbkDatas = UrlManager.queryBufbkDatas.replace("/","");
        }
        final String url = UrlManager.getSERVER() + UrlManager.queryBufbkDatas;
        final StringBuffer sb = new StringBuffer();
        sb.append("\n\nurl:"+url);
        JSONObject param=new JSONObject();
        try {
            param.put("jh",jh);
            param.put("simid",simid);
            param.put("xingm",xingm);
            param.put("pageSize",pageSize);
            param.put("pageNo",pageNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n param:"+param.toString());
        try {
            OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build().execute(new Callback<List<EntityBufbk>>() {
                @Override
                public List<EntityBufbk> parseNetworkResponse(Response response, int id) throws Exception {
                    String string=response.body().string();
                    if(MyApplication.IS_Test_json){
                        string = string.substring(1,string.length()-1);
                        string = string.replace("\\","");
                    }
                    sb.append("\n\n response:"+string);
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    JSONObject object=new JSONObject(string);
                    int resultCode=object.optInt("result");
                    String msg=object.optString("message");
                    String bufbkListStr = object.optString("bufbkDatas");
                    switch (resultCode){
                        case 1:
                            List<EntityBufbk> datas = GsonUtil.json2BeanList(bufbkListStr, EntityBufbk.class);
                            return  datas;
                        default:
                            throw new Exception(msg);
                    }
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    queryBufbkListCallback.queryBufbkListFail(e);
                    sb.append("\n\n responese:"+e.getMessage());

                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                }

                @Override
                public void onResponse(List<EntityBufbk> response, int id) {
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    queryBufbkListCallback.queryBufbkListSuccess(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void queryBufbkFeedbacks(String jh, String simid, String bufbkId, final IQueryBufbkDetailCallback queryBufbkDetailCallback) {

        if(MyApplication.IS_Test_json){
            UrlManager.queryBufbkFeedbackDatas = UrlManager.queryBufbkFeedbackDatas.replace("/","");
        }
        final String url = UrlManager.getSERVER() + UrlManager.queryBufbkFeedbackDatas;
        final StringBuffer sb = new StringBuffer();
        sb.append("\n\nurl:"+url);
        JSONObject param=new JSONObject();
        try {
            param.put("jh",jh);
            param.put("simid",simid);
            param.put("bufbkId",bufbkId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n param:"+sb.toString());
        try {
            OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build().execute(new Callback<List<EntityBufbkFeedback>>() {
                @Override
                public List<EntityBufbkFeedback> parseNetworkResponse(Response response, int id) throws Exception {
                    String string=response.body().string();

                    if(MyApplication.IS_Test_json){
                        string = string.substring(1,string.length()-1);
                        string = string.replace("\\","");
                    }
                    sb.append("\n\n response:"+string);
                    JSONObject object=new JSONObject(string);
                    int resultCode=object.optInt("result");
                    String msg=object.optString("message");
                    String bufbkFankxxDatas = object.optString("bufbkFankxxDatas");
                    switch (resultCode){
                        case 1:
                            List<EntityBufbkFeedback> datas = GsonUtil.json2BeanList(bufbkFankxxDatas, EntityBufbkFeedback.class);
                            return  datas;
                        default:
                            throw new Exception(msg);
                    }
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    sb.append("\n\n response:"+e);

                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    queryBufbkDetailCallback.onQueryBufbkDetailFail(e);
                }

                @Override
                public void onResponse(List<EntityBufbkFeedback> response, int id) {
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    queryBufbkDetailCallback.onQueryBufbkDetailSuccess(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void feedbackBufbk(String bufbkId, String jh, String simid,String xingm, String feedbackStrInfo, String filesPath, final IBufbkFeedbackCallback bufbkFeedback) {

        if(MyApplication.IS_Test_json){
            UrlManager.addBufbkFankxx = UrlManager.addBufbkFankxx.replace("/","");
        }
        final String url = UrlManager.getSERVER() + UrlManager.addBufbkFankxx;
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
            jsonParam.put("bufbkId", bufbkId);
            jsonParam.put("jh", jh);
            jsonParam.put("simid", simid);
            jsonParam.put("xingm", xingm);
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
                        if(MyApplication.IS_Test_json){
                            s = s.substring(1,s.length()-1);
                            s = s.replace("\\","");
                        }
                        JSONObject object= null;
                        try {
                            object = new JSONObject(s);
                            int resultCode=object.optInt("result");
                            String msg=object.optString("message");
                            switch (resultCode){
                                case 1:
                                    bufbkFeedback.bufbkFeedbackSuccess();
                                default:
                                    bufbkFeedback.bufbkFeedbackFail(new Exception(msg));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            sb.append("\n\n response:"+e.getMessage());
                            bufbkFeedback.bufbkFeedbackFail(e);
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
                        bufbkFeedback.bufbkFeedbackFail(e);
                        Log.d(tag,e.getMessage());
                        sb.append("\n\n response:"+e.getMessage());
                        SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    }
                });

    }

    @Override
    public void queryUnacceptBufbkCount(String jh, String simid, String xingm, final IQueryUnacceptBufbkCountCallback queryUnacceptBufbkCountCallback) {
        final String url = UrlManager.getSERVER() + UrlManager.queryUnacceptBufbkCount;
        final StringBuffer sb = new StringBuffer();
        sb.append("\n\nurl:"+url);

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("jh", jh);
            jsonParam.put("simid", simid);
            jsonParam.put("xingm", xingm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.append("\n\n param:"+jsonParam.toString());
        try {
            OkHttpUtils.postString().url(url).content(jsonParam.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build().execute(new Callback<Integer>() {
                @Override
                public Integer parseNetworkResponse(Response response, int id) throws Exception {
                    String string=response.body().string();
                    sb.append("\n\n response:"+string);
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    JSONObject object=new JSONObject(string);
                    int resultCode=object.optInt("result");
                    String msg=object.optString("message");
                    int count = object.optInt("unacceptBufbkCount");
                    switch (resultCode){
                        case 1:
                            return count;
                        default:
                            throw new Exception(msg);
                    }
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    queryUnacceptBufbkCountCallback.queryUnacceptBufbkCountFail(e);
                    sb.append("\n\n responese:"+e.getMessage());

                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                }

                @Override
                public void onResponse(Integer response, int id) {
                    SDCardUtil.saveHttpRequestInfo2File(url,sb.toString());
                    queryUnacceptBufbkCountCallback.queryUnacceptBufbkCountSuccess(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
