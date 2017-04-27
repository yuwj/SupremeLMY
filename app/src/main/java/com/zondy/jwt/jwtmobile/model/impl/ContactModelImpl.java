package com.zondy.jwt.jwtmobile.model.impl;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zondy.jwt.jwtmobile.base.MyApplication;
import com.zondy.jwt.jwtmobile.callback.IQueryContactsAndZZJGSByKeywordCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryContactsByZZJGCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryZZJGCallback;
import com.zondy.jwt.jwtmobile.entity.EntityContact;
import com.zondy.jwt.jwtmobile.entity.EntityContactsAndZZJGS;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.entity.EntityZD;
import com.zondy.jwt.jwtmobile.manager.UrlManager;
import com.zondy.jwt.jwtmobile.model.IContactModel;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.GsonUtil;
import com.zondy.jwt.jwtmobile.util.SDCardUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * Created by sheep on 2017/1/17.
 */

public class ContactModelImpl implements IContactModel {

    @Override
    public void queryZZJG(Context context, String zdlx, final IQueryZZJGCallback queryZZJGCallback) {

        final StringBuffer sb = new StringBuffer();
        final String url = UrlManager.getSERVER() + UrlManager.queryZZJGZD;
        sb.append("\n\nurl:" + url);
        JSONObject param = new JSONObject();
        try {
            param.put("jh", SharedTool.getInstance().getUserInfo(context).getUserName());
            param.put("simid", CommonUtil.getDeviceId(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n" + param.toString());
        try {
            OkGo.post(url)
                    .upString(param.toString())
//                    .cacheKey("queryZZJG")
//                    .cacheMode(CacheMode.IF_NONE_CACHE_REQUEST)
                    .execute(new StringCallback() {
                        @Override
                        public void onCacheSuccess(String s, Call call) {
                            super.onCacheSuccess(s, call);
                            Log.i("sheep", "queryZZJG,读取的缓存");
                            Log.i("sheep", "s=" + s);
                            JSONObject object = null;
                            try {
                                object = new JSONObject(s);
                                int resultCode = object.optInt("result");
                                String msg = object.optString("message");
                                switch (resultCode) {
                                    case 1:
                                        List<EntityZD> allEntitys = new ArrayList<EntityZD>();
                                        JSONArray zdArray = null;
                                        zdArray = object.optJSONArray("list");
                                        if (zdArray != null) {
                                            for (int i = 0; i < zdArray.length(); i++) {
                                                JSONObject zzjgObj = zdArray.getJSONObject(i);
                                                String obj = zzjgObj.toString();
                                                EntityZD zzjg = new Gson().fromJson(obj, EntityZD.class);
                                                allEntitys.add(zzjg);
                                            }
                                        }
                                        queryZZJGCallback.querySuccessed(allEntitys);
                                        break;
                                    default:
                                        queryZZJGCallback.queryUnSuccessed(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.i("sheep", "response=" + response.toString());
                            Log.i("sheep", "s=" + s);

                            sb.append("\n\n" + s);
                            JSONObject object = null;
                            try {
                                object = new JSONObject(s);
                                int resultCode = object.optInt("result");
                                String msg = object.optString("message");
                                switch (resultCode) {
                                    case 1:
                                        List<EntityZD> allEntitys = new ArrayList<EntityZD>();
                                        JSONArray zdArray = null;
                                        zdArray = object.optJSONArray("list");
                                        if (zdArray != null) {
                                            for (int i = 0; i < zdArray.length(); i++) {
                                                JSONObject zzjgObj = zdArray.getJSONObject(i);
                                                String obj = zzjgObj.toString();
                                                EntityZD zzjg = new Gson().fromJson(obj, EntityZD.class);
                                                allEntitys.add(zzjg);
                                            }
                                        }
                                        queryZZJGCallback.querySuccessed(allEntitys);
                                        break;
                                    default:
                                        queryZZJGCallback.queryUnSuccessed(msg);
                                }
                            } catch (JSONException e) {
                                sb.append("\n\n" + e.getMessage());
                                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                                e.printStackTrace();
                            }

                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            sb.append("\n\n" + e.getMessage());
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                        }
                    });
        } catch (Exception e) {
            sb.append("\n\n" + e.getMessage());
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void queryZZJGByParentZZJG(Context context, String parentZZJG, final IQueryZZJGCallback queryZZJGCallback) {

        final StringBuffer sb = new StringBuffer();
        if (MyApplication.IS_Test_json) {
            UrlManager.queryZZJGZDByParentZZJG = UrlManager.queryZZJGZDByParentZZJG.replace("/", "");
        }
        final String url = UrlManager.getSERVER() + UrlManager.queryZZJGZDByParentZZJG;
        sb.append("\n\nurl:" + url);
        JSONObject param = new JSONObject();
        try {
            param.put("jh", SharedTool.getInstance().getUserInfo(context).getUserName());
            param.put("simid", CommonUtil.getDeviceId(context));
            param.put("parentZZJG", parentZZJG);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n" + param.toString());
        try {
            OkGo.post(url)
                    .upString(param.toString())
                    .execute(new StringCallback() {
                        @Override
                        public void onCacheSuccess(String s, Call call) {
                            super.onCacheSuccess(s, call);
                            Log.i("sheep", "queryZZJG,读取的缓存");
                            Log.i("sheep", "s=" + s);
                            JSONObject object = null;
                            try {

                                if (MyApplication.IS_Test_json) {
                                    s = s.substring(1, s.length() - 1);
                                    s = s.replace("\\", "");
                                }
                                object = new JSONObject(s);
                                int resultCode = object.optInt("result");
                                String msg = object.optString("message");
                                switch (resultCode) {
                                    case 1:
                                        List<EntityZD> allEntitys = new ArrayList<EntityZD>();
                                        JSONArray zdArray = null;
                                        zdArray = object.optJSONArray("list");
                                        if (zdArray != null) {
                                            for (int i = 0; i < zdArray.length(); i++) {
                                                JSONObject zzjgObj = zdArray.getJSONObject(i);
                                                String obj = zzjgObj.toString();
                                                EntityZD zzjg = new Gson().fromJson(obj, EntityZD.class);
                                                allEntitys.add(zzjg);
                                            }
                                        }
                                        queryZZJGCallback.querySuccessed(allEntitys);
                                        break;
                                    default:
                                        queryZZJGCallback.queryUnSuccessed(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.i("sheep", "response=" + response.toString());
                            Log.i("sheep", "s=" + s);

                            sb.append("\n\n" + s);
                            JSONObject object = null;
                            try {
                                if (MyApplication.IS_Test_json) {
                                    s = s.substring(1, s.length() - 1);
                                    s = s.replace("\\", "");
                                }
                                object = new JSONObject(s);
                                int resultCode = object.optInt("result");
                                String msg = object.optString("message");
                                switch (resultCode) {
                                    case 1:
                                        List<EntityZD> allEntitys = new ArrayList<EntityZD>();
                                        JSONArray zdArray = null;
                                        zdArray = object.optJSONArray("list");
                                        if (zdArray != null) {
                                            for (int i = 0; i < zdArray.length(); i++) {
                                                JSONObject zzjgObj = zdArray.getJSONObject(i);
                                                String obj = zzjgObj.toString();
                                                EntityZD zzjg = new Gson().fromJson(obj, EntityZD.class);
                                                allEntitys.add(zzjg);
                                            }
                                        }
                                        queryZZJGCallback.querySuccessed(allEntitys);
                                        break;
                                    default:
                                        queryZZJGCallback.queryUnSuccessed(msg);
                                }
                            } catch (JSONException e) {
                                sb.append("\n\n" + e.getMessage());
                                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                                e.printStackTrace();
                            }

                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            sb.append("\n\n" + e.getMessage());
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                        }
                    });
        } catch (Exception e) {
            sb.append("\n\n" + e.getMessage());
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void queryContactsByZZJG(Context context, final String zzjg, final IQueryContactsByZZJGCallback queryContactsByZZJGCallback) {

        final StringBuffer sb = new StringBuffer();


        if (MyApplication.IS_Test_json) {
            UrlManager.queryConnectionListByDwdm = UrlManager.queryConnectionListByDwdm.replace("/", "");
        }

        final String url = UrlManager.getSERVER() + UrlManager.queryConnectionListByDwdm;
        sb.append("\n\nurl:" + url);
        JSONObject param = new JSONObject();
        try {
            param.put("dwdm", zzjg);
            param.put("jh", SharedTool.getInstance().getUserInfo(context).getUserName());
            param.put("simid", CommonUtil.getDeviceId(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n" + param.toString());
        try {
            OkGo.post(url)
                    .upString(param.toString())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            sb.append("\n\n" + s);
                            Log.i("sheep", "response=" + response.toString());
                            Log.i("sheep", "s=" + s);
                            JSONObject object = null;
                            try {

                                if (MyApplication.IS_Test_json) {
                                    s = s.substring(1, s.length() - 1);
                                    s = s.replace("\\", "");
                                }
                                object = new JSONObject(s);
                                int resultCode = object.optInt("result");
                                String msg = object.optString("message");
                                switch (resultCode) {
                                    case 1:
                                        List<EntityContact> allEntitys = new ArrayList<EntityContact>();
                                        JSONArray zdArray = null;
                                        zdArray = object.optJSONArray("connectionList");
                                        if (zdArray != null) {
                                            for (int i = 0; i < zdArray.length(); i++) {
                                                JSONObject zzjgObj = zdArray.getJSONObject(i);
                                                String obj = zzjgObj.toString();
                                                EntityContact contact = new Gson().fromJson(obj, EntityContact.class);
                                                allEntitys.add(contact);
                                            }
                                        }
                                        queryContactsByZZJGCallback.querySuccessed(allEntitys);
                                        break;
                                    default:
                                        queryContactsByZZJGCallback.queryUnSuccessed(msg);
                                }
                            } catch (JSONException e) {
                                sb.append("\n\n" + e.getMessage());
                                e.printStackTrace();
                            }
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                        }

                        @Override
                        public void onCacheSuccess(String s, Call call) {
                            super.onCacheSuccess(s, call);
                            Log.i("sheep", "queryConnectionByDwdm：" + zzjg + ",读取了缓存");
                            Log.i("sheep", "s=" + s);
                            JSONObject object = null;
                            try {
                                object = new JSONObject(s);
                                int resultCode = object.optInt("result");
                                String msg = object.optString("message");
                                switch (resultCode) {
                                    case 1:
                                        List<EntityContact> allEntitys = new ArrayList<EntityContact>();
                                        JSONArray zdArray = null;
                                        zdArray = object.optJSONArray("connectionList");
                                        if (zdArray != null) {
                                            for (int i = 0; i < zdArray.length(); i++) {
                                                JSONObject zzjgObj = zdArray.getJSONObject(i);
                                                String obj = zzjgObj.toString();
                                                EntityContact contact = new Gson().fromJson(obj, EntityContact.class);
                                                allEntitys.add(contact);
                                            }
                                        }
                                        queryContactsByZZJGCallback.querySuccessed(allEntitys);
                                        break;
                                    default:
                                        queryContactsByZZJGCallback.queryUnSuccessed(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                sb.append("\n\n" + e.getMessage());
                                SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            sb.append("\n\n" + e.getMessage());
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("\n\n" + e.getMessage());
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
        }

    }

    @Override
    public void queryContactsAndZZJGSByKeyword(Context context, String keyword, final IQueryContactsAndZZJGSByKeywordCallback queryContactsAndZZJGSByKeywordCallback) {

        final StringBuffer sb = new StringBuffer();


        if (MyApplication.IS_Test_json) {
            UrlManager.queryContactsAndZZJGsByKeyword = UrlManager.queryContactsAndZZJGsByKeyword.replace("/", "");
        }

        final String url = UrlManager.getSERVER() + UrlManager.queryContactsAndZZJGsByKeyword;
        sb.append("\n\nurl:" + url);
        JSONObject param = new JSONObject();
        try {
            param.put("keyword", keyword);
            param.put("jh", SharedTool.getInstance().getUserInfo(context).getUserName());
            param.put("simid", CommonUtil.getDeviceId(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n" + param.toString());
        try {
            OkGo.post(url)
                    .upString(param.toString())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            sb.append("\n\n" + s);
                            Log.i("sheep", "response=" + response.toString());
                            Log.i("sheep", "s=" + s);
                            JSONObject object = null;
                            try {

                                if (MyApplication.IS_Test_json) {
                                    s = s.substring(1, s.length() - 1);
                                    s = s.replace("\\", "");
                                }
                                object = new JSONObject(s);
                                int resultCode = object.optInt("result");
                                String msg = object.optString("message");
                                switch (resultCode) {
                                    case 1:
                                        EntityContactsAndZZJGS entityContactsAndZZJGS = new EntityContactsAndZZJGS();
                                        String contactsStr = object.optString("connectionList");
                                        List<EntityContact> contactList = GsonUtil.json2BeanList(contactsStr, EntityContact.class);
                                        String zzjgsStr = object.optString("zzjgList");
                                        List<EntityZD> zzjgList = GsonUtil.json2BeanList(zzjgsStr, EntityZD.class);
                                        entityContactsAndZZJGS.setContactList(contactList);
                                        entityContactsAndZZJGS.setZzjgList(zzjgList);
                                        queryContactsAndZZJGSByKeywordCallback.onQueryContactsAndZZJGsSuccess(entityContactsAndZZJGS);
                                        break;
                                    default:
                                        queryContactsAndZZJGSByKeywordCallback.onQueryContactsAndZZJGsFail(new Exception(msg));
                                }
                            } catch (JSONException e) {
                                sb.append("\n\n" + e.getMessage());
                                e.printStackTrace();
                                queryContactsAndZZJGSByKeywordCallback.onQueryContactsAndZZJGsFail(e);
                            }
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            queryContactsAndZZJGSByKeywordCallback.onQueryContactsAndZZJGsFail(e);
                            sb.append("\n\n" + e.getMessage());
                            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("\n\n" + e.getMessage());
            SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
            queryContactsAndZZJGSByKeywordCallback.onQueryContactsAndZZJGsFail(e);
        }

    }
}
