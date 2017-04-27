package com.zondy.jwt.jwtmobile.model.impl;

import com.zondy.jwt.jwtmobile.util.GsonUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

/**
 * Created by ywj on 2017/4/13 0013.
 */

public class BaseModelImpl<T> {

    public JSONObject chekResultCode(String resp) throws Exception {
        JSONObject object = null;
        try {
            object = new JSONObject(resp);
            int resultCode = object.optInt("result");
            String msg = object.optString("message");
            switch (resultCode) {
                case 1:
                    return object;
                case 2:
                    throw new Exception("" + msg);
                default:
                    throw new Exception("" + msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new Exception("服务端返回数据格式有误");
        }
    }

    public T PaseData(String s, Class objClass) throws Exception {
        StringBuffer sb = new StringBuffer();
        JSONObject object = chekResultCode(s);
        T entity = (T) GsonUtil.json2Bean(object.optString("resultData"), objClass);
        return entity;

    }

    public List<T> PaseDataList(String s, Class objClass) throws Exception {
        StringBuffer sb = new StringBuffer();
        JSONObject object = chekResultCode(s);
        try {
            object = new JSONObject(s);
        } catch (JSONException e) {
            sb.append("\n\n" + e.getMessage());
            e.printStackTrace();
        }
        List<T> entityList = (List<T>) GsonUtil.json2Bean(object.optString("resultData"), objClass);
        return entityList;
    }
}
