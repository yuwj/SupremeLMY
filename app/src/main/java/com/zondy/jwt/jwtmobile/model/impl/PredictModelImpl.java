package com.zondy.jwt.jwtmobile.model.impl;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zondy.jwt.jwtmobile.callback.IQueryPredictCallback;
import com.zondy.jwt.jwtmobile.entity.EntityPredict;
import com.zondy.jwt.jwtmobile.manager.UrlManager;
import com.zondy.jwt.jwtmobile.model.IPredictModel;
import com.zondy.jwt.jwtmobile.util.GsonUtil;
import com.zondy.jwt.jwtmobile.util.SDCardUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * Created by ywj on 2017/3/29 0029.
 */

public class PredictModelImpl implements IPredictModel {

    @Override
    public void queryPredict(String jh, String simid, String predictId, final IQueryPredictCallback queryPredictCallback) {
        final String url = UrlManager.getSERVER() + UrlManager.queryPredict;
        final StringBuffer sb = new StringBuffer();
        sb.append("\n\nurl:" + url);
        JSONObject param = new JSONObject();
        try {
            param.put("jh", jh);
            param.put("simid", simid);
            param.put("predictId", predictId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("\n\n param:" + param.toString());
        try {
            OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build().execute(new Callback<EntityPredict>() {
                @Override
                public EntityPredict parseNetworkResponse(Response response, int id) throws Exception {
                    String string = response.body().string();
                    sb.append("\n\n response:" + string);
                    JSONObject object = new JSONObject(string);
                    int resultCode = object.optInt("result");
                    String msg = object.optString("message");
                    switch (resultCode) {
                        case 1:

                            String predictResult = object.optString("predictResult");
                            EntityPredict data = GsonUtil.json2Bean(predictResult, EntityPredict.class);
                            return data;
                        default:
                            throw new Exception(msg);
                    }
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    sb.append("\n\n response:" + e);

                    SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
//                    if (isTestPredict) {
//                        String s = "{\"deptName\":\"浦楼 \",\"predResults\":[{\"realDate\":20170228,\"gridNum\":1144,\"patrolPeroidId\":0,\"pdId\":91,\"intervalUpper\":\"0000\",\"intervalLower\":\"2400\",\"crimeGroupId\":null,\"gridInfo\":{\"pdId\":91,\"gridNum\":1144,\"p1X\":119.000694,\"p1Y\":33.579838,\"p2X\":119.00177,\"p2Y\":33.57894,\"centX\":119.00123,\"centY\":33.579388,\"subDistrictId\":0},\"duration\":null,\"checked\":false,\"predProb\":0.1054},{\"realDate\":20170228,\"gridNum\":1375,\"patrolPeroidId\":0,\"pdId\":91,\"intervalUpper\":\"0000\",\"intervalLower\":\"2400\",\"crimeGroupId\":null,\"gridInfo\":{\"pdId\":91,\"gridNum\":1375,\"p1X\":119.00932,\"p1Y\":33.580738,\"p2X\":119.01041,\"p2Y\":33.579838,\"centX\":119.009865,\"centY\":33.580288,\"subDistrictId\":0},\"duration\":null,\"checked\":false,\"predProb\":0.093994856},{\"realDate\":20170228,\"gridNum\":1143,\"patrolPeroidId\":0,\"pdId\":91,\"intervalUpper\":\"0000\",\"intervalLower\":\"2400\",\"crimeGroupId\":null,\"gridInfo\":{\"pdId\":91,\"gridNum\":1143,\"p1X\":119.000694,\"p1Y\":33.580738,\"p2X\":119.00177,\"p2Y\":33.579838,\"centX\":119.00123,\"centY\":33.580288,\"subDistrictId\":0},\"duration\":null,\"checked\":false,\"predProb\":0.04595056},{\"realDate\":20170228,\"gridNum\":1173,\"patrolPeroidId\":0,\"pdId\":91,\"intervalUpper\":\"0000\",\"intervalLower\":\"2400\",\"crimeGroupId\":null,\"gridInfo\":{\"pdId\":91,\"gridNum\":1173,\"p1X\":119.00177,\"p1Y\":33.579838,\"p2X\":119.00285,\"p2Y\":33.57894,\"centX\":119.00231,\"centY\":33.579388,\"subDistrictId\":0},\"duration\":null,\"checked\":false,\"predProb\":0.034297008},{\"realDate\":20170228,\"gridNum\":1289,\"patrolPeroidId\":0,\"pdId\":91,\"intervalUpper\":\"0000\",\"intervalLower\":\"2400\",\"crimeGroupId\":null,\"gridInfo\":{\"pdId\":91,\"gridNum\":1289,\"p1X\":119.00609,\"p1Y\":33.579838,\"p2X\":119.00717,\"p2Y\":33.57894,\"centX\":119.00663,\"centY\":33.579388,\"subDistrictId\":0},\"duration\":null,\"checked\":false,\"predProb\":0.02798841}],\"date\":1488211200000}";
//                        EntityPredict p = GsonUtil.json2Bean(s, EntityPredict.class);
//                        queryPredictCallback.queryPredictSuccess(p);
//                    } else {
                        queryPredictCallback.queryPredictFailed(e);
//                    }
                }

                @Override
                public void onResponse(EntityPredict response, int id) {
                    SDCardUtil.saveHttpRequestInfo2File(url, sb.toString());
                    queryPredictCallback.queryPredictSuccess(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
