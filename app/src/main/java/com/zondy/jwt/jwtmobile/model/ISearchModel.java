package com.zondy.jwt.jwtmobile.model;

import android.content.Context;

import com.zondy.jwt.jwtmobile.callback.IQueryPoiTypesCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryPoisCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryTCFZListCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryZHCXListCallback;

/**
 * Created by sheep on 2017/1/18.
 */

public interface ISearchModel {
    void queryZHCXList(Context context,String layerid,String layername,int orderType,String keyword, double radius, double longitude, double latitude, int nowpage, int pagesize,
                       IQueryZHCXListCallback queryZHCXListCallback);
    void queryTCFZList(Context context, IQueryTCFZListCallback queryTCFZListCallback);

    void queryPoiTypes(String jh, String simid, IQueryPoiTypesCallback queryPoiTypesCallback);
    void queryPois(String jh,String simid,String keyword,String poiType,int pageNo,int pageSize,IQueryPoisCallback queryPoisCallback);
}
