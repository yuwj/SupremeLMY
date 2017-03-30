package com.zondy.jwt.jwtmobile.view.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yinglan.scrolllayout.ScrollLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityLocation;
import com.zondy.jwt.jwtmobile.entity.EntityPage;
import com.zondy.jwt.jwtmobile.entity.EntityPoi;
import com.zondy.jwt.jwtmobile.entity.EntityPoiType;
import com.zondy.jwt.jwtmobile.entity.EntitySearchResult;
import com.zondy.jwt.jwtmobile.entity.EntityTCFL;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.global.Constant;
import com.zondy.jwt.jwtmobile.manager.JWTLocationManager;
import com.zondy.jwt.jwtmobile.presenter.ISearchPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.SearchPresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.GsonUtil;
import com.zondy.jwt.jwtmobile.util.MapManager;
import com.zondy.jwt.jwtmobile.util.ScreenUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.ICompositeSearchResultListView;
import com.zondy.jwt.jwtmobile.view.ISearchZHCXListView;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.annotation.AnnotationsOverlay;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.core.geometry.Dot;
import com.zondy.mapgis.core.object.DoubleList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by yuwj on 2017年3月29日20:50:05
 */

public class CompositeSearchResultListActivity extends BaseActivity implements ICompositeSearchResultListView, View.OnClickListener {
    @BindView(R.id.ll_search_top)
    LinearLayout llSearchTop;
    @BindView(R.id.mapview)
    MapView mapView;
    @BindView(R.id.iv_search_result_title_back)
    ImageView ivSearchResultTitleBack;
    @BindView(R.id.tv_search_result_title_count)
    TextView tvSearchResultTitleCount;
    @BindView(R.id.ll_search_result_title)
    LinearLayout llSearchResultTitle;
    @BindView(R.id.rv_search_result)
    XRecyclerView rvSearchResult;
    @BindView(R.id.scroll_down_layout)
    ScrollLayout scrollDownLayout;
    @BindView(R.id.root)
    RelativeLayout root;

    boolean isRefresh = true;//加载时是否是刷新
    boolean isHasMore = true;//是否还有更多数据
    private MapManager mapManager;
    private List<Annotation> annotations;

    //搜索请求及参数
    private ISearchPresenter searchPresenter;
    private String inputKeyword;
    EntityPoiType entityTCFL;

    private double radius = 10;//半径，单位千米
    private double longitude = 114.3765164268;//经度
    private double latitude = 30.4933471077573;//纬度
    private int orderType = 1;//排序类型，1 代表距离排序，2 代表采集时间排序。 默认为距离排序
    EntityPage entityPage;//分页参数

    private List<EntityPoi> mDatas;
    private CommonAdapter<EntityPoi> adapter;

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener;
    EntityUser userInfo;

    BroadcastReceiver locationSuccessReceiver;//接收定位成功的广播

    public static Intent createIntent(Context context, String inputKeyword) {
        Intent intent = new Intent(context, CompositeSearchResultListActivity.class);
        intent.putExtra("inputKeyword", inputKeyword);
        return intent;
    }

    public static Intent createIntent(Context context, EntityPoiType entityPoiType) {
        Intent intent = new Intent(context, CompositeSearchResultListActivity.class);
        intent.putExtra("entityPoiType", entityPoiType);
        return intent;
    }


    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_search_result_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        initView();
        initOperator();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcast();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBroadcast();
    }


    public void registerBroadcast() {
        locationSuccessReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                EntityLocation location = (EntityLocation) intent.getSerializableExtra("entityLocation");
                if (mapManager != null) {
                    mapManager.updateUserLocation(location);
                }
            }
        };
        registerReceiver(locationSuccessReceiver, new IntentFilter(JWTLocationManager.LOCATION_SUCCESS_BROADCAST));
    }

    public void unregisterBroadcast() {
        if (locationSuccessReceiver != null) {
            unregisterReceiver(locationSuccessReceiver);
            locationSuccessReceiver = null;
        }
    }

    private void initParams() {
        userInfo = SharedTool.getInstance().getUserInfo(context);
        entityPage = new EntityPage();
        Intent intent = getIntent();
        inputKeyword = intent.getStringExtra("inputKeyword");
        entityTCFL = (EntityPoiType) intent.getSerializableExtra("entityPoiType");
        if (TextUtils.isEmpty(inputKeyword)) {
            inputKeyword = "";
        }

        mDatas = new ArrayList<>();
        adapter = new CommonAdapter<EntityPoi>(context, R.layout.item_zonghss_result, mDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityPoi entitySearchResult, int position) {
                String dmtlj = "xxx";
                Glide.with(CompositeSearchResultListActivity.this).load(dmtlj)
                        .placeholder(R.drawable.ic_img_loading)// 这行貌似是glide的bug,在部分机型上会导致第一次图片不在中间
                        .error(R.drawable.ic_img_load_fail)//
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//
                        .crossFade()
                        .into((ImageView) holder.getView(R.id.iv_item_scrollresults));
                holder.setText(R.id.tv_item_scrollresults_mc, entitySearchResult.getName());
                holder.setText(R.id.tv_item_scrollresults_dz, entitySearchResult.getAddress());
//                String distance = entitySearchResult.getDistance();
//                if (distance.length() > 4) {
//                    distance = distance.substring(0, 4);
//                }
//                holder.setText(R.id.tv_item_scrollresults_distance, distance + "km");
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                for (int i = 0; i < annotations.size(); i++) {
                    Annotation a = annotations.get(i);
                    Bitmap b = null;
                    if (i == position - 1) {
                        b = mapManager.createIndexAnnotationView(i, 1, true);
                        a.showAnnotationView();
                    } else {
                        b = mapManager.createIndexAnnotationView(i, 1, false);
                    }
                    a.setImage(b);
                }
                mapView.refresh();
                scrollDownLayout.setToExit();

                Intent intent = new Intent(CompositeSearchResultListActivity.this, SearchResultsItemActivity.class);
                intent.putExtra("NAME", mDatas.get(position - 1).getName());
                intent.putExtra("DZ", mDatas.get(position - 1).getAddress());
//                intent.putExtra("dmtlj", mDatas.get(position - 1).getDmtlj());
                startActivity(intent);


            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


        searchPresenter = new SearchPresenterImpl(this, this);

        mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
            @Override
            public void onScrollProgressChanged(float currentProgress) {
                Log.i("xxx", "onScrollProgressChanged: " + currentProgress);
            }

            @Override
            public void onScrollFinished(ScrollLayout.Status currentStatus) {
                //currentProgress exit：-1  中间open 1  顶部close 0
                // （  从底部到中间为 【-1，0）1  从中间到顶部  【1，0】  ）
                switch (currentStatus) {
                    case OPENED:

                        ivSearchResultTitleBack.setVisibility(View.INVISIBLE);
                        break;
                    case CLOSED:

                        ivSearchResultTitleBack.setVisibility(View.VISIBLE);
                        break;
                    case EXIT:

                        ivSearchResultTitleBack.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onChildScroll(int top) {

            }
        };
    }

    private void initView() {
        mapManager = new MapManager(mapView, context);
        mapManager.initMap(Constant.getMapPath(), new MapManager.MapLoadListner() {
            @Override
            public void onMapLoadSuccess() {
                EntityLocation entityLocation = SharedTool.getInstance().getLocationInfo(context);
                mapManager.updateUserLocation(entityLocation);
            }

            @Override
            public void onMapLoadFail() {

            }
        }, new MapManager.MapAnnotationListener() {
            @Override
            public AnnotationView createAnnotationView(final Annotation annotation) {

                for (int i = 0; i < annotations.size(); i++) {
                    Annotation a = annotations.get(i);
                    Bitmap b = null;
                    if (a.getUid().equals(annotation.getUid())) {
                        b = mapManager.createIndexAnnotationView(i, 1, true);
                    } else {
                        b = mapManager.createIndexAnnotationView(i, 1, false);
                    }
                    a.setImage(b);
                }
                AnnotationView annotationView = new AnnotationView(annotation, context);
                View contentView = LayoutInflater.from(context).inflate(R.layout.content_annotation_zonghss, null);

                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels / 2;
                View ll = contentView.findViewById(R.id.ll_xxx);
                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) ll.getLayoutParams();
                p.width = width;
                ll.setLayoutParams(p);
                TextView tv_annotation_dismiss = (TextView) contentView.findViewById(R.id.tv_annotation_dismiss);

                TextView tv_baojsj = (TextView) contentView.findViewById(R.id.tv_baojsj);
                TextView tv_baojnr = (TextView) contentView.findViewById(R.id.tv_baojnr);
                TextView tv_baojr = (TextView) contentView.findViewById(R.id.tv_baojr);
                EntityPoi jingq = GsonUtil.json2Bean(annotation.getDescription(), EntityPoi.class);
                tv_baojsj.setText("名称:" + jingq.getName());
                tv_baojnr.setText("地址:" + jingq.getAddress());
                tv_baojr.setText("电话:" + jingq.getTelephone());
                tv_annotation_dismiss.setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 annotation.hideAnnotationView();
                                                             }
                                                         }
                );

                annotationView.setCalloutView(contentView);
                AnnotationsOverlay annotationsOverlay = mapView.getAnnotationsOverlay();
                int index = annotationsOverlay.indexOf(annotation);
                //把annotation放到z轴最前面
                annotationsOverlay.moveAnnotation(index, -1);
                if (mapView.getResolution() > MapManager.goodResolution) {//缩放地图到最佳大小
                    mapView.zoomToCenter(annotation.getPoint(), MapManager.goodResolution, false);
                }

                mapView.refresh();
                // 将annotationview平移到视图中心
                annotationView.setPanToMapViewCenter(true);
                return annotationView;
            }
        });

        if (!TextUtils.isEmpty(inputKeyword)) {

            tvSearchResultTitleCount.setText(inputKeyword);
        } else {
            tvSearchResultTitleCount.setText(entityTCFL.getLargeClass() + "的搜索结果");
        }


        //ScrollLayout设置
        scrollDownLayout.setMinOffset(0);
        scrollDownLayout.setMaxOffset((int) (ScreenUtil.getScreenHeight(this) * 0.5));
        scrollDownLayout.setExitOffset(ScreenUtil.dip2px(this, 40));
        scrollDownLayout.setIsSupportExit(true);
        scrollDownLayout.setAllowHorizontalScroll(false);
        scrollDownLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        scrollDownLayout.setToOpen();
        scrollDownLayout.getBackground().setAlpha(0);
        ivSearchResultTitleBack.setVisibility(View.INVISIBLE);
        ///////////////////////////////

        rvSearchResult.setLayoutManager(new LinearLayoutManager(context));
        rvSearchResult.setAdapter(adapter);
        rvSearchResult.addItemDecoration(new com.zondy.jwt.jwtmobile.ui.DividerItemDecoration(context, com.zondy.jwt.jwtmobile.ui.DividerItemDecoration.VERTICAL_LIST));
        rvSearchResult.setPullRefreshEnabled(false);
        rvSearchResult.setLoadingMoreEnabled(true);
        rvSearchResult.setRefreshProgressStyle(ProgressStyle.Pacman);
        rvSearchResult.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        rvSearchResult.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                entityPage.setPageNo(1);
//                searchPresenter.queryZHCXList(layerid, layername, orderType, inputKeyword, radius, longitude, latitude, entityPage.getPageNo(), entityPage.getPageSize());
                searchPresenter.queryPois(userInfo.getUserName(), CommonUtil.getDeviceId(context), inputKeyword, entityTCFL, entityPage.getPageNo(), entityPage.getPageSize());
            }


            @Override
            public void onLoadMore() {
                isRefresh = false;
//                searchPresenter.queryZHCXList(layerid, layername, orderType, inputKeyword, radius, longitude, latitude, entityPage.getComputeNextPage(), entityPage.getPageSize());
                if(isHasMore){

                searchPresenter.queryPois(userInfo.getUserName(), CommonUtil.getDeviceId(context), inputKeyword, entityTCFL, entityPage.getComputeNextPage(), entityPage.getPageSize());
                }else{
                    ToastTool.getInstance().shortLength(context,"没有更多数据了",true);
                }
            }
        });

    }

    public void initOperator() {
//        searchPresenter.queryZHCXList(layerid, layername, orderType, inputKeyword, radius, longitude, latitude, entityPage.getPageNo(), entityPage.getPageSize());
        searchPresenter.queryPois(userInfo.getUserName(), CommonUtil.getDeviceId(context), inputKeyword, entityTCFL, entityPage.getPageNo(), entityPage.getPageSize());
    }

    /**
     * @param datas         所有的搜索结果
     * @param selectedIndex 被选中的index
     */
    public void updateSearchResultOnMap(List<EntityPoi> datas, int selectedIndex) {
        mapView.getAnnotationsOverlay().removeAllAnnotations();
        if (annotations != null) {
            annotations.clear();
        } else {
            annotations = new ArrayList<>();
        }
        for (int i = 0; i < datas.size(); i++) {
            EntityPoi result = datas.get(i);
            Bitmap bitmap = mapManager.createIndexAnnotationView(i, 1, i == selectedIndex);
            Dot dot = null;
            switch (Constant.JWT_AREA_SELECTED){
                case Constant.JWT_AREA_HA:
                    dot = new Dot(Double.valueOf(result.getX()), Double.valueOf(result.getY()));
                    break;
                case Constant.JWT_AREA_WH:
                    double[] xy = mapManager.lonLat2Mercator(Double.valueOf(result.getX()), Double.valueOf(result.getY()));
                    dot = new Dot(xy[0],xy[1]);
                    break;
                default:
                    dot = new Dot(Double.valueOf(result.getX()), Double.valueOf(result.getY()));
                    break;
            }
            Annotation annotation = new Annotation("" + (i + 1), "搜索结果", result.getJsonStr(), dot, bitmap);
            annotations.add(annotation);
        }
        mapView.getAnnotationsOverlay().addAnnotations(annotations);
        mapView.refresh();
    }


    @OnClick({R.id.ll_search_top, R.id.iv_search_result_title_back, R.id.ll_search_result_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search_top:
                startActivity(CompositeSearchInputActivity.createIntent(context));
                CompositeSearchResultListActivity.this.finish();
                break;
            case R.id.iv_search_result_title_back:
            case R.id.ll_search_result_title:
                switch (scrollDownLayout.getCurrentStatus()) {
                    case CLOSED:
                        scrollDownLayout.setToExit();
                        break;
                    case OPENED:
                        scrollDownLayout.setToExit();
                        break;
                    case EXIT:
                        scrollDownLayout.setToOpen();
                        break;
                }
                break;
        }
    }

    @Override
    public void queryPoisSuccess(List<EntityPoi> pois) {
        rvSearchResult.refreshComplete();
        rvSearchResult.loadMoreComplete();
        StringBuffer sb = new StringBuffer();
        sb.append("  isRefresh:"+isRefresh);
        if (isRefresh) {
            mDatas.clear();
            mDatas.addAll(pois);
            sb.append("  isRefresh:"+isRefresh);
        } else {
            mDatas.addAll(pois);
        }


        adapter.notifyDataSetChanged();
        String titleSub = inputKeyword+"";
        if(entityTCFL != null){
            titleSub += entityTCFL.getLargeClass();
        }
        tvSearchResultTitleCount.setText("共找到\"" + titleSub + "\"相关" + mDatas.size() + "个结果");
        if(pois.size() < entityPage.getPageSize()){
            isHasMore = false;
        }

        sb.append("  isHasMore:"+isHasMore);
        sb.append("  set pageNo:"+entityPage.getComputeNextPage());
        entityPage.setPageNo(entityPage.getComputeNextPage());
        sb.append("  entityPagepageNO:"+entityPage.getPageNo());
        updateSearchResultOnMap(mDatas, -1);
        ToastTool.getInstance().shortLength(context,sb.toString(),true);
    }

    @Override
    public void queryPoisFail(Exception e) {

        rvSearchResult.refreshComplete();
        rvSearchResult.loadMoreComplete();
        ToastTool.getInstance().shortLength(this, e.getMessage(), true);
    }
}
