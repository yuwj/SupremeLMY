package com.zondy.jwt.jwtmobile.model.impl;

import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.listener.UploadListener;
import com.lzy.okserver.upload.UploadInfo;
import com.lzy.okserver.upload.UploadManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zondy.jwt.jwtmobile.base.MyApplication;
import com.zondy.jwt.jwtmobile.callback.IAcceptJingqCallback;
import com.zondy.jwt.jwtmobile.callback.IArriveConfirmCallback;
import com.zondy.jwt.jwtmobile.callback.IJingqHandleCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryAllJingqKuaisclTypesCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryAllJingqTypesCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryJinqDatasCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryUnacceptJingqCountCallback;
import com.zondy.jwt.jwtmobile.callback.IReloadJingqCallback;
import com.zondy.jwt.jwtmobile.callback.IRollbackJingqCallback;
import com.zondy.jwt.jwtmobile.entity.EntityJingq;
import com.zondy.jwt.jwtmobile.entity.EntityZD;
import com.zondy.jwt.jwtmobile.manager.UrlManager;
import com.zondy.jwt.jwtmobile.model.IJingqHandleModel;
import com.zondy.jwt.jwtmobile.util.GsonUtil;
import com.zondy.jwt.jwtmobile.util.SDCardUtil;
import com.zondy.jwt.jwtmobile.util.ToastTool;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * Created by ywj on 2017/1/12 0012.
 */

public class JingqHandleModelImpl implements IJingqHandleModel {

    String msg = "";
    private static final String TAG = "JingqHandleModelImpl";


    @Override
    public void queryJingqDatas(String jh, String simid, final IQueryJinqDatasCallback queryJingqDatasCallback) {

        final StringBuffer sb = new StringBuffer();


        if(MyApplication.IS_Test_json){
            UrlManager.queryJingqList = UrlManager.queryJingqList.replace("/","");
        }
        msg = "";
        final String url = UrlManager.getSERVER() + UrlManager.queryJingqList;
        sb.append("\n\nurl:" + url);
        JSONObject param = new JSONObject();
        try {
            param.put("jh", jh);
            param.put("simid", simid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n" + param.toString());
        try {
            OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build().execute(new Callback<List<EntityJingq>>() {

                @Override
                public void inProgress(float progress, long total, int id) {
                    super.inProgress(progress, total, id);
                    queryJingqDatasCallback.inProgress(progress, total, id);
                }

                @Override
                public List<EntityJingq> parseNetworkResponse(Response response, int id) throws Exception {
                    String string = response.body().string();
                    if(MyApplication.IS_Test_json){
                        string = string.substring(1,string.length()-1);
                        string = string.replace("\\","");
                    }
                    sb.append("\n\n resp:" + string);
                    JSONObject object = new JSONObject(string);
                    int resultCode = object.optInt("result");
                    msg = object.optString("message");
                    String dataStr = object.optString("jingqList");
                    switch (resultCode) {
                        case 1:
                            List<EntityJingq> jignqDatas = GsonUtil.json2BeanList(dataStr, EntityJingq.class);
                            return jignqDatas;
                        default:
                            return null;
                    }
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    sb.append("\n\n" + e.getMessage());
                    queryJingqDatasCallback.onFailed(e);
                    SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                }

                @Override
                public void onResponse(List<EntityJingq> response, int id) {
                    SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                    if (response != null) {
                        queryJingqDatasCallback.onSuccess(response);
                    } else {
                        if (MyApplication.IS_TEST_JINGQLIST) {
                            String value = "[{\"baojdz\":\"板湖镇南郭村南郭大桥北侧50米处\",\"baojnr\":\"我的女儿步行至此处，被一辆电动三轮车撞伤，现人受伤我将其先送往医院，请派人处理。\",\"baojr\":\"汤如宝\",\"baojrdh\":\"13770176910\",\"baojsj\":\"2017-01-13 07:31:48\",\"bjlb\":null,\"bjlbdm\":\"210000\",\"bjlx\":null,\"bjlxdm\":\"210100\",\"bjxl\":null,\"bjxldm\":\"210105\",\"chujsj\":null,\"daodsj\":null,\"fanksj\":null,\"filesPath\":\"\",\"fknr\":\"电动三轮车撞到行人，致行人受伤。\",\"gxdwdm\":null,\"jiejh\":\"1046\",\"jingqid\":\"JJ320700_130185367\",\"jingqzt\":\"2\",\"latitude\":\"30.493347107757284\",\"longitude\":\"114.40758042680389\",\"zhipsj\":null,\"zjg_cjjg\":null,\"zjg_cjlbbm\":null,\"zjg_cjlbmc\":null,\"zjg_cjr\":null,\"zjg_cjsxbm\":null,\"zjg_cjsxmc\":null,\"zjg_fsdd\":null,\"zjg_sfcsbm\":null,\"zjg_sfcsmc\":null,\"zjg_sfsjsx\":null,\"zjg_sfsjxx\":null,\"zjg_ssqk\":null,\"zjg_tqqkbm\":null,\"zjg_tqqkmc\":null},{\"baojdz\":\"县城澳门路与上海路交汇处\",\"baojnr\":\"大约三天前的上午，我父亲骑电动车至这里时被一辆拖拉机撞伤了，当时没有报警，现双方在县医院因为治疗事情发生矛盾。请派人处理。\",\"baojr\":\"高德军\",\"baojrdh\":\"13814321112\",\"baojsj\":\"2017-01-13 07:31:48\",\"bjlb\":null,\"bjlbdm\":\"210000\",\"bjlx\":null,\"bjlxdm\":\"210100\",\"bjxl\":null,\"bjxldm\":\"210102\",\"chujsj\":null,\"daodsj\":null,\"fanksj\":null,\"filesPath\":\"\",\"fknr\":\"一辆电动车与一辆拖拉机相撞，电动车驾驶人受伤，伤者已送医院，事故待处理。\",\"gxdwdm\":null,\"jiejh\":\"1001\",\"jingqid\":\"20160809090638000005\",\"jingqzt\":\"0\",\"latitude\":\"30.493347107757284\",\"longitude\":\"114.40758042680389\",\"zhipsj\":null,\"zjg_cjjg\":null,\"zjg_cjlbbm\":null,\"zjg_cjlbmc\":null,\"zjg_cjr\":null,\"zjg_cjsxbm\":null,\"zjg_cjsxmc\":null,\"zjg_fsdd\":null,\"zjg_sfcsbm\":null,\"zjg_sfcsmc\":null,\"zjg_sfsjsx\":null,\"zjg_sfsjxx\":null,\"zjg_ssqk\":null,\"zjg_tqqkbm\":null,\"zjg_tqqkmc\":null}]";
                            response = GsonUtil.json2BeanList(value, EntityJingq.class);
                            queryJingqDatasCallback.onSuccess(response);
                        } else {
                            queryJingqDatasCallback.onFailed(new Exception("暂无数据"));
                        }
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void reloadJingq(String jingqid, String jh, String simid, final IReloadJingqCallback reloadJingqCallback) {

        final StringBuffer sb = new StringBuffer();

        if(MyApplication.IS_Test_json){
            UrlManager.queryJingqByJingqid = UrlManager.queryJingqByJingqid.replace("/","");
        }
        final String url = UrlManager.getSERVER() + UrlManager.queryJingqByJingqid;
        sb.append("\n\nurl:" + url);
        msg = "";
        JSONObject param = new JSONObject();
        try {
            param.put("jingqid", jingqid);
            param.put("jh", jh);
            param.put("simid", simid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n" + param.toString());
        OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new Callback<EntityJingq>() {
            @Override
            public EntityJingq parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                if(MyApplication.IS_Test_json){
                    string = string.substring(1,string.length()-1);
                    string = string.replace("\\","");
                }
                sb.append("\n\nresp:"+string);
                JSONObject object = new JSONObject(string);
                int resultCode = object.optInt("result");
                msg = object.optString("message");
                String jingqStr = object.optString("jingqInfo");
                EntityJingq jingq = null;
                switch (resultCode) {
                    case 1:
                        jingq = GsonUtil.json2Bean(jingqStr, EntityJingq.class);
                        break;
                    default:
                        break;
                }
                return jingq;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                sb.append("\n\n" + e.getMessage());
                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                reloadJingqCallback.loadJingqFailed(e);
            }

            @Override
            public void onResponse(EntityJingq response, int id) {
                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                if (response != null) {
                    reloadJingqCallback.loadJingqSuccess(response);
                } else {
                    reloadJingqCallback.loadJingqFailed(new Exception(msg));
                }
            }
        });

    }

    @Override
    public void acceptJingq(String jingqid, String carid, String jh, String simid, final IAcceptJingqCallback acceptJingqCallback) {

        final StringBuffer sb = new StringBuffer();


        final String url = UrlManager.getSERVER() + UrlManager.receiveJingqingConfirm;
        sb.append("\n\nurl:" + url);
        JSONObject param = new JSONObject();
        msg = "";
        try {
            param.put("jingqid", jingqid);
            param.put("carid", carid);
            param.put("jh", jh);
            param.put("simid", simid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n" + param.toString());
        OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new Callback<Boolean>() {
            @Override
            public Boolean parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                JSONObject object = new JSONObject(string);
                int resultCode = object.optInt("result");
                msg = object.optString("message");
                switch (resultCode) {
                    case 1:
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {

                sb.append("\n\n" + e.getMessage());
                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                acceptJingqCallback.acceptJingqFailed(e);
            }

            @Override
            public void onResponse(Boolean response, int id) {
                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                if (response) {
                    acceptJingqCallback.acceptJingqSuccess();
                } else {
                    acceptJingqCallback.acceptJingqFailed(new Exception(msg));

                }
            }
        });


    }

    @Override
    public void rollbackJingq(String jingqid, String jh, String simid, final IRollbackJingqCallback rollbackJingqCallback) {

        final StringBuffer sb = new StringBuffer();


        final String url = UrlManager.getSERVER() + UrlManager.receiveJingqingReBack;
        sb.append("\n\nurl:" + url);
        JSONObject param = new JSONObject();
        msg = "";
        try {
            param.put("jingqid", jingqid);
            param.put("jh", jh);
            param.put("simid", simid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n" + param.toString());
        OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new Callback<Boolean>() {
            @Override
            public Boolean parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                JSONObject object = new JSONObject(string);
                int resultCode = object.optInt("result");
                msg = object.optString("message");
                switch (resultCode) {
                    case 1:
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {

                sb.append("\n\n" + e.getMessage());
                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                rollbackJingqCallback.rollbackJingqFailed(e);
            }

            @Override
            public void onResponse(Boolean response, int id) {
                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                if (response) {
                    rollbackJingqCallback.rollbackJingqSuccess();
                } else {
                    rollbackJingqCallback.rollbackJingqFailed(new Exception(msg));

                }
            }
        });

    }

    @Override
    public void arriveConfirm(String jingyid, String jingqid, String longitude, String latitude, String jh, String simid, final IArriveConfirmCallback arriveConfirmCallback) {

        final StringBuffer sb = new StringBuffer();


        final String url = UrlManager.getSERVER() + UrlManager.reachConfirm;
        sb.append("\n\nurl:" + url);
        JSONObject param = new JSONObject();
        msg = "";
        try {
            param.put("jingyid", jingyid);
            param.put("jingqid", jingqid);
            param.put("longitude", longitude);
            param.put("latitude", latitude);
            param.put("jh", jh);
            param.put("simid", simid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n" + param.toString());
        OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new Callback<Boolean>() {
            @Override
            public Boolean parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                JSONObject object = new JSONObject(string);
                int resultCode = object.optInt("result");
                msg = object.optString("message");
                String dataStr = object.optString("jingqList");
                switch (resultCode) {
                    case 1:
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {

                sb.append("\n\n" + e.getMessage());
                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                arriveConfirmCallback.arriveFailed(e);
            }

            @Override
            public void onResponse(Boolean response, int id) {
                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                if (response) {
                    arriveConfirmCallback.arriveSuccess();
                } else {
                    arriveConfirmCallback.arriveFailed(new Exception(msg));

                }
            }
        });


    }


    @Override
    public void submitJingq(String jingyid, String jingqid, String chuljg, String bjlb, String bjlx, String bjxl, String chuljgms, String filesPath, String jh, String simid, final IJingqHandleCallback jingqHandleCallback) {

        final StringBuffer sb = new StringBuffer();


        final String url = UrlManager.getSERVER() + UrlManager.handleJingqing;
        sb.append("\n\nurl:" + url);
        List<File> files = new ArrayList<>();
        if (!TextUtils.isEmpty(filesPath)) {
            String[] ss = filesPath.split(",");

            for (int i = 0; i < ss.length; i++) {

                files.add(new File(ss[i]));
            }
        }

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("jingyid", jingyid);
            jsonParam.put("jingqid", jingqid);
            jsonParam.put("chuljg", chuljg);
            jsonParam.put("bjlb", bjlb);
            jsonParam.put("bjlx", bjlx);
            jsonParam.put("bjxl", bjxl);
            jsonParam.put("chuljgms", chuljgms);
            jsonParam.put("filesPath", filesPath);
            jsonParam.put("jh", jh);
            jsonParam.put("simid", simid);
        } catch (Exception e) {
            Log.e(TAG, "submitJingq: ");
        }
        sb.append("\n\n" + jsonParam.toString());
        Map<String, String> param = new HashMap<>();
        param.put("strBody", jsonParam.toString() + " \n" + filesPath);


        OkGo.post(url).tag(this)//
                .isMultipart(true)       // 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params("strBody", jsonParam.toString())        // 这里可以上传参数
//                .params("file1", new File("filepath1"))   // 可以添加文件上传
//                .params("file2", new File("filepath2"))     // 支持多文件同时添加上传
                .addFileParams("images", files) // 这里支持一个key传多个文件
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        sb.append("\n\nresponse:" + s);
                        SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                        //上传成功
                        Log.d("submitJingq", response.body().toString());
                        jingqHandleCallback.jingqHandleSuccess();
                    }


                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        Log.d("submitJingq", "进度" + progress);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        sb.append("\n\n" + e.getMessage());
                        SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                        super.onError(call, response, e);
                        jingqHandleCallback.jingqHandleFailed(e);
                        Log.d("submitJingq", e.getMessage());
                    }
                });

        ////////////////////
//        String uploadUrl = "http://server.jeasonlzy.com/OkHttpUtils/" + "upload";
//        PostRequest postRequest = OkGo.post(url)
////                .isMultipart(true)
//                //
//                .headers("headerKey1", "headerValue1")//
//                .headers("headerKey2", "headerValue2")//
//                .params("jingyid", jingyid)
//                .params("jingqid", jingqid)
//                .params("chuljg", chuljg)
//                .params("bjlb", bjlb)
//                .params("bjlx", bjlx)
//                .params("bjxl", bjxl)
//                .params("chuljgms", chuljgms)
//                .params("filesPath", filesPath)
//                .params("jh", jh)
//                .params("simid", simid);
//        String[] s = filesPath.split(",");
//        for (int i = 0; i < s.length; i++) {
//            postRequest.params("fileKey" + i, new File(s[i]));
//        }
//
//        UploadListener listener = new UploadListener() {
//            @Override
//            public void onProgress(UploadInfo uploadInfo) {
//
//                if (uploadInfo.getState() == DownloadManager.NONE) {
////                                tvProgress.setText("请上传");
////                    civ.setText("请上传");
//
//                } else if (uploadInfo.getState() == UploadManager.ERROR) {
////                                tvProgress.setText("上传出错");
////                    civ.setText("错误");
//                } else if (uploadInfo.getState() == UploadManager.WAITING) {
////                                tvProgress.setText("等待中");
////                    civ.setText("等待");
//                } else if (uploadInfo.getState() == UploadManager.FINISH) {
////                                tvProgress.setText("上传成功");
////                    civ.setText("成功");
//                } else if (uploadInfo.getState() == UploadManager.UPLOADING) {
////                                tvProgress.setText("上传中");
////                    civ.setProgress((int) (uploadInfo.getProgress() * 100));
////                    civ.setText((Math.round(uploadInfo.getProgress() * 10000) * 1.0f / 100) + "%");
//                }
//            }
//
//            @Override
//            public void onFinish(Object o) {
////                            tvProgress.setText("上传成功");
//                Log.i("xx", "上传成功");
//            }
//
//            @Override
//            public void onError(UploadInfo uploadInfo, String errorMsg, Exception e) {
//                Log.i("xx", e.getMessage());
//            }
//
//            @Override
//            public Object parseNetworkResponse(Response response) throws Exception {
//                return null;
//            }
//        };
//
//        UploadManager uploadManager = UploadManager.getInstance();
//        uploadManager.getThreadPool().setCorePoolSize(1);
//        uploadManager.addTask(url, postRequest, listener);
        ///////////////////
//        msg = "";
//        JSONObject jsonParam = new JSONObject();
//        try {
//            jsonParam.put("jingyid", jingyid);
//            jsonParam.put("jingqid", jingqid);
//            jsonParam.put("chuljg", chuljg);
//            jsonParam.put("bjlb", bjlb);
//            jsonParam.put("bjlx", bjlx);
//            jsonParam.put("bjxl", bjxl);
//            jsonParam.put("chuljgms", chuljgms);
//            jsonParam.put("filesPath", filesPath);
//            jsonParam.put("jh", jh);
//            jsonParam.put("simid", simid);
//        } catch (Exception e) {
//            Log.e(TAG, "submitJingq: ");
//        }
//        Map<String,String> param = new HashMap<>();
//        param.put("strBody",jsonParam.toString());
//
//        PostFormBuilder builder = OkHttpUtils.post();
//        String[] files = filesPath.split(",");
//        Log.i(TAG, "submitJingq: " + files.length);
//        if (files != null && files.length > 0) {
//            for (int i = 0; i < files.length; i++) {
//                if (!TextUtils.isEmpty(files[i])) {
//                    File file = new File(files[i]);
//                    String fileName = file.getName();
//                    String fileId = "mFile";
//                    builder.addFile(fileId, fileName, file);
//                }
//
//            }
//        }
//
//        builder.url(url).params(param)
//                .build().execute(new Callback<Boolean>() {
//
//            @Override
//            public Boolean parseNetworkResponse(Response response, int id) throws Exception {
//                String string = response.body().string();
//                JSONObject object = new JSONObject(string);
//                int resultCode = object.optInt("result");
//                msg = object.optString("message");
//                boolean result = false;
//                switch (resultCode) {
//                    case 1:
//                        result = true;
//                        break;
//                    default:
//                        break;
//                }
//                return result;
//            }
//
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//                jingqHandleCallback.jingqHandleFailed(e);
//            }
//
//            @Override
//            public void onResponse(Boolean response, int id) {
//
//                if (response) {
//                    jingqHandleCallback.jingqHandleSuccess();
//                } else {
//                    jingqHandleCallback.jingqHandleFailed(new Exception(msg));
//
//                }
//            }
//        });
    }

    @Override
    public void queryAllJingqTypes(String jh, String simid, final IQueryAllJingqTypesCallback queryAllJingqTypesCallback) {

        final StringBuffer sb = new StringBuffer();


        final String url = UrlManager.getSERVER() + UrlManager.queryJingqTypeZD;
        sb.append("\n\nurl:" + url);
        JSONObject param = new JSONObject();
        msg = "";
        try {
            param.put("jh", jh);
            param.put("simid", simid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n" + param.toString());
        OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new Callback<List<EntityZD>>() {
            @Override
            public List<EntityZD> parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                sb.append("\n\n" + string);
                JSONObject object = new JSONObject(string);
                int resultCode = object.optInt("result");
                msg = object.optString("message");
                String resultStr = object.optString("list");
                List<EntityZD> result = null;
                switch (resultCode) {
                    case 1:
                        result = GsonUtil.json2BeanList(resultStr, EntityZD.class);
                        break;
                    default:
                        break;
                }
                return result;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

                sb.append("\n\n" + e.getMessage());
                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                queryAllJingqTypesCallback.jingqHandleFailed(e);
            }

            @Override
            public void onResponse(List<EntityZD> response, int id) {

                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                if (response != null) {
                    queryAllJingqTypesCallback.jingqHandleSuccess(response);
                } else {
                    queryAllJingqTypesCallback.jingqHandleFailed(new Exception(msg));

                }
            }
        });
    }

    @Override
    public void queryAllJingqKuaisclTypes(String jh, String simid, final IQueryAllJingqKuaisclTypesCallback queryAllJingqKuaisclTypesCallback) {

        final StringBuffer sb = new StringBuffer();


        final String url = UrlManager.getSERVER() + UrlManager.jingqingksxz;
        sb.append("\n\nurl:" + url);
        JSONObject param = new JSONObject();
        msg = "";
        try {
            param.put("jh", jh);
            param.put("simid", simid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n" + param.toString());
        OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new Callback<List<EntityZD>>() {
            @Override
            public List<EntityZD> parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                sb.append("\n\n" + string);
                JSONObject object = new JSONObject(string);
                int resultCode = object.optInt("result");
                msg = object.optString("message");
                String resultStr = object.optString("list");
                List<EntityZD> result = null;
                switch (resultCode) {
                    case 1:
                        result = GsonUtil.json2BeanList(resultStr, EntityZD.class);
                        break;
                    default:
                        break;
                }
                return result;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

                sb.append("\n\n" + e.getMessage());
                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                queryAllJingqKuaisclTypesCallback.queryJingqKuaisclFailed(e);
            }

            @Override
            public void onResponse(List<EntityZD> response, int id) {

                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                if (response != null) {
                    queryAllJingqKuaisclTypesCallback.queryJingqKuaisclSuccess(response);
                } else {
                    queryAllJingqKuaisclTypesCallback.queryJingqKuaisclFailed(new Exception(msg));

                }
            }
        });
    }

    @Override
    public void queryUnacceptJingqCount(String jh, String simid, String zzjgdm, final IQueryUnacceptJingqCountCallback queryUnacceptJingqCountCallback) {

        final StringBuffer sb = new StringBuffer();
        final String url = UrlManager.getSERVER() + UrlManager.queryUnacceptJingqCount;
        sb.append("\n\nurl:" + url);
        JSONObject param = new JSONObject();
        msg = "";
        try {
            param.put("jh", jh);
            param.put("simid", simid);
            param.put("zzjgdm", zzjgdm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n" + param.toString());
        OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new Callback<Integer>() {
            @Override
            public Integer parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                sb.append("\n\n" + string);
                JSONObject object = new JSONObject(string);
                int resultCode = object.optInt("result");
                int count = 0;
                msg = object.optString("message");
                switch (resultCode) {
                    case 1:
                        count = object.optInt("unacceptJingqCount", 0);
                        break;
                    default:
                        throw new Exception(msg);

                }
                return count;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

                sb.append("\n\n" + e.getMessage());
                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                queryUnacceptJingqCountCallback.queryUnacceptJingqCountFail(e);
            }

            @Override
            public void onResponse(Integer response, int id) {

                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());

                queryUnacceptJingqCountCallback.queryUnacceptJingqCountSuccess(response);
            }
        });
    }

}
