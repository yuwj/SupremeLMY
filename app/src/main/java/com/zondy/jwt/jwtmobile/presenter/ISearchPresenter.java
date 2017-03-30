package com.zondy.jwt.jwtmobile.presenter;

import com.zondy.jwt.jwtmobile.callback.IQueryPoiTypesCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryPoisCallback;
import com.zondy.jwt.jwtmobile.entity.EntityPoiType;

import static android.R.attr.radius;

/**
 * Created by sheep on 2017/1/18.
 */

public interface ISearchPresenter {
    void queryZHCXList(String layerid,String layername,int orderType,String keyword,double radius,double longitude,double latitude,int nowpage,int pagesize);
    void queryTCFZList(String jh,String simid);

    void queryPoiTypes(String jh, String simid);
    void queryPois(String jh,String simid,String keyword,EntityPoiType poiType,int pageNo,int pageSize);
}
