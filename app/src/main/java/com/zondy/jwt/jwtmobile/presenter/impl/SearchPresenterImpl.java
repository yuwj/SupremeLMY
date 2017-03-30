package com.zondy.jwt.jwtmobile.presenter.impl;

import android.content.Context;

import com.zondy.jwt.jwtmobile.callback.IQueryPoiTypesCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryPoisCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryTCFZListCallback;
import com.zondy.jwt.jwtmobile.callback.IQueryZHCXListCallback;
import com.zondy.jwt.jwtmobile.entity.EntityPoi;
import com.zondy.jwt.jwtmobile.entity.EntityPoiType;
import com.zondy.jwt.jwtmobile.entity.EntitySearchResult;
import com.zondy.jwt.jwtmobile.entity.EntityTCFL;
import com.zondy.jwt.jwtmobile.model.ISearchModel;
import com.zondy.jwt.jwtmobile.model.impl.SearchModelImpl;
import com.zondy.jwt.jwtmobile.presenter.ISearchPresenter;
import com.zondy.jwt.jwtmobile.view.ICompositeSearchInputView;
import com.zondy.jwt.jwtmobile.view.ICompositeSearchResultListView;
import com.zondy.jwt.jwtmobile.view.ISearchTCFLView;
import com.zondy.jwt.jwtmobile.view.ISearchZHCXListView;

import java.util.List;

/**
 * Created by sheep on 2017/1/18.
 */

public class SearchPresenterImpl implements ISearchPresenter{
    private ISearchTCFLView searchTCFLView;
    private ISearchZHCXListView searchZHCXListView;
    ICompositeSearchInputView compositeSearchInputView;
    ICompositeSearchResultListView compositeSearchResultListView;
    private ISearchModel searchModel;
    Context context;
    public SearchPresenterImpl(Context context,ISearchTCFLView searchTCFLView){
        super();
        this.searchTCFLView=searchTCFLView;
        this.context=context;
        searchModel=new SearchModelImpl();
    }
    public SearchPresenterImpl(Context context, ISearchZHCXListView searchZHCXListView){
        super();
        this.searchZHCXListView=searchZHCXListView;
        this.context=context;
        searchModel=new SearchModelImpl();
    }

    public SearchPresenterImpl(Context context, ICompositeSearchInputView compositeSearchInputView){
        super();
        this.compositeSearchInputView=compositeSearchInputView;
        this.context=context;
        searchModel=new SearchModelImpl();
    }
    public SearchPresenterImpl(Context context, ICompositeSearchResultListView compositeSearchResultListView){
        super();
        this.compositeSearchResultListView=compositeSearchResultListView;
        this.context=context;
        searchModel=new SearchModelImpl();
    }
    /**
     * 通过图层名直接获取查询结果
     * @param keyword
     * @param radius
     * @param longitude
     * @param latitude
     * @param nowpage
     * @param pagesize
     */
    @Override
    public void queryZHCXList(String layerid,String layername,int orderType,String keyword, double radius, double longitude, double latitude, int nowpage, int pagesize) {
        searchModel.queryZHCXList(context,layerid,layername,orderType,keyword, radius, longitude, latitude, nowpage, pagesize, new IQueryZHCXListCallback() {
            @Override
            public void querySuccessed(List<EntitySearchResult> allEntitys,int allpages) {
                searchZHCXListView.queryZHCXListSuccessed(allEntitys,allpages);
            }

            @Override
            public void queryUnSuccessed(String msg) {
                searchZHCXListView.queryZHCXListUnSuccessed(msg);
            }
        });
    }

    @Override
    public void queryTCFZList(String jh,String simid) {
        searchModel.queryTCFZList(context, new IQueryTCFZListCallback() {

            @Override
            public void querySuccessed(List<EntityTCFL> poiTypeList) {
                searchTCFLView.queryTCFLSuccessed(poiTypeList);
            }

            @Override
            public void queryUnSuccessed(Exception e) {
                searchTCFLView.queryTCFLUnSuccessed(e);
            }
        });
    }

    @Override
    public void queryPoiTypes(String jh, String simid) {
        searchModel.queryPoiTypes(jh, simid, new IQueryPoiTypesCallback() {
            @Override
            public void queryPoiTypesSuccess(List<EntityPoiType> poiTypes) {
                compositeSearchInputView.queryPoiTypesSuccess(poiTypes);
            }

            @Override
            public void queryPoiTypesFail(Exception e) {
                compositeSearchInputView.queryPoiTypesFail(e);
            }
        });
    }

    @Override
    public void queryPois(String jh, String simid, String keyword,EntityPoiType poiType, int pageNo, int pageSize) {
        if(poiType == null){
            searchModel.queryPois(jh, simid, keyword, null, pageNo, pageSize, new IQueryPoisCallback() {
                @Override
                public void queryPoisSuccess(List<EntityPoi> pois) {
                    compositeSearchResultListView.queryPoisSuccess(pois);
                }

                @Override
                public void queryPoisFail(Exception e) {
                    compositeSearchResultListView.queryPoisFail(e);

                }
            });
        }else{
            searchModel.queryPois(jh, simid, keyword, poiType.getClassCode(), pageNo, pageSize, new IQueryPoisCallback() {
                @Override
                public void queryPoisSuccess(List<EntityPoi> pois) {
                    compositeSearchResultListView.queryPoisSuccess(pois);
                }

                @Override
                public void queryPoisFail(Exception e) {
                    compositeSearchResultListView.queryPoisFail(e);

                }
            });
        }


    }


}
