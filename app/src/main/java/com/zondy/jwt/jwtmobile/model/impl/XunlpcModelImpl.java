package com.zondy.jwt.jwtmobile.model.impl;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zondy.jwt.jwtmobile.callback.IXunlpcQueryGuijWithLvgCallBack;
import com.zondy.jwt.jwtmobile.entity.EntityGuijWithLvg;
import com.zondy.jwt.jwtmobile.manager.UrlManager;
import com.zondy.jwt.jwtmobile.model.IXunlpcModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * Created by ywj on 2017/3/16 0016.
 */

public class XunlpcModelImpl implements IXunlpcModel {

    @Override
    public void queryGuijWithLvg(String userId, String startTime, final String endTime, final IXunlpcQueryGuijWithLvgCallBack xunlpcQueryGuijWithLvgCallBack) {
        String url = UrlManager.getSERVER()+UrlManager.guijWithLvg;
        JSONObject param = new JSONObject();
        try {
            param.put("userId",userId);
            param.put("startTime",startTime);
            param.put("endTime",endTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(url).content(param.toString()).mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new Callback<List<EntityGuijWithLvg>>() {
            @Override
            public List<EntityGuijWithLvg> parseNetworkResponse(Response response, int id) throws Exception {

                List<EntityGuijWithLvg> lvgDatas = new ArrayList<EntityGuijWithLvg>();
                for(int i = 0;i < 10;i++){
                    EntityGuijWithLvg entityGuijWithLvg = new EntityGuijWithLvg();
                    entityGuijWithLvg.setStartTime("2016-07-0"+i+" 13:23:34");
                    entityGuijWithLvg.setEndTime("2016-07-0"+i+" 13:23:34");
                    entityGuijWithLvg.setAddress("地址"+i);
                    entityGuijWithLvg.setPositionName(i+"旅馆");
                    entityGuijWithLvg.setStarCount(i);
                    entityGuijWithLvg.setLatitude(30.4+0.01*i);
                    entityGuijWithLvg.setLongitude(114.4+0.01*i);
                    lvgDatas.add(entityGuijWithLvg);
                }
                return lvgDatas;

            }

            @Override
            public void onError(Call call, Exception e, int id) {

                xunlpcQueryGuijWithLvgCallBack.queryLvgFail(e);
            }

            @Override
            public void onResponse(List<EntityGuijWithLvg> response, int id) {
                xunlpcQueryGuijWithLvgCallBack.queryLvgSuccess(response);
            }
        });
    }

    @Override
    public void queryGuijWithWangb(String userId, String startTime, String endTime, IXunlpcQueryGuijWithLvgCallBack xunlpcQueryGuijWithWangbCallBack) {

    }
}
