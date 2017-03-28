package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.zondy.jwt.jwtmobile.entity.EntityPage;
import com.zondy.jwt.jwtmobile.entity.EntitySearchResult;
import com.zondy.jwt.jwtmobile.entity.EntityTCFL;
import com.zondy.jwt.jwtmobile.global.Constant;
import com.zondy.jwt.jwtmobile.presenter.ISearchPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.SearchPresenterImpl;
import com.zondy.jwt.jwtmobile.util.GsonUtil;
import com.zondy.jwt.jwtmobile.util.MapManager;
import com.zondy.jwt.jwtmobile.util.ScreenUtil;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.ISearchZHCXListView;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.annotation.AnnotationsOverlay;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.core.geometry.Dot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by sheep on 2016/12/26.
 */

public class SearchResultListActivity extends BaseActivity implements ISearchZHCXListView, View.OnClickListener {
    @BindView(R.id.ll_search_top)
    LinearLayout llSearchTop;
    @BindView(R.id.mapview)
    MapView mapview;
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

    boolean isRefresh;//加载时是否是刷新
    private MapManager mapManager;
    private List<Annotation> annotations;

    //搜索请求及参数
    private ISearchPresenter searchPresenter;
    private String inputKeyword;
    EntityTCFL entityTCFL;
    String layerid, layername;
    private double radius = 10;//半径，单位千米
    private double longitude = 114.3765164268;//经度
    private double latitude = 30.4933471077573;//纬度
    private int orderType = 1;//排序类型，1 代表距离排序，2 代表采集时间排序。 默认为距离排序
    EntityPage entityPage;//分页参数

    private List<EntitySearchResult> mDatas;
    private CommonAdapter<EntitySearchResult> adapter;

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener;


    public static Intent createIntent(Context context, String inputKeyword) {
        Intent intent = new Intent(context, SearchResultListActivity.class);
        intent.putExtra("inputKeyword", inputKeyword);
        return intent;
    }

    public static Intent createIntent(Context context, EntityTCFL entityTCFL) {
        Intent intent = new Intent(context, SearchResultListActivity.class);
        intent.putExtra("entityTCFL", entityTCFL);
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


    private void initParams() {
        entityPage = new EntityPage();
        isRefresh = false;
        Intent intent = getIntent();
        inputKeyword = intent.getStringExtra("inputKeyword");
        entityTCFL = (EntityTCFL) intent.getSerializableExtra("entityTCFL");
        if (entityTCFL != null) {
            layerid = entityTCFL.getLayerid()+"";
            layername = entityTCFL.getLayername();
            layerid = "";
            layername = "";
            inputKeyword = entityTCFL.getLayername();
        } else {
            layerid = "";
            layername = "";

        }
        if (TextUtils.isEmpty(inputKeyword)) {
            inputKeyword = "";
        }

        mDatas = new ArrayList<>();
        adapter = new CommonAdapter<EntitySearchResult>(context,R.layout.item_zonghss_result,mDatas) {
            @Override
            protected void convert(ViewHolder holder, EntitySearchResult entitySearchResult, int position) {
                String dmtlj = entitySearchResult.getDmtlj().split(",")[0];
                Glide.with(SearchResultListActivity.this).load(dmtlj)
                        .placeholder(R.drawable.ic_img_loading)// 这行貌似是glide的bug,在部分机型上会导致第一次图片不在中间
                        .error(R.drawable.ic_img_load_fail)//
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//
                        .crossFade()
                        .into((ImageView) holder.getView(R.id.iv_item_scrollresults));
                holder.setText(R.id.tv_item_scrollresults_mc, entitySearchResult.getMc());
                holder.setText(R.id.tv_item_scrollresults_dz, entitySearchResult.getDz());
                String distance = entitySearchResult.getDistance();
                if (distance.length() > 4) {
                    distance = distance.substring(0, 4);
                }
                holder.setText(R.id.tv_item_scrollresults_distance, distance + "km");
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
                mapview.refresh();
                scrollDownLayout.setToExit();

                Intent intent = new Intent(SearchResultListActivity.this, SearchResultsItemActivity.class);
                intent.putExtra("NAME", mDatas.get(position - 1).getMc());
                intent.putExtra("DZ", mDatas.get(position - 1).getDz());
                intent.putExtra("dmtlj", mDatas.get(position - 1).getDmtlj());
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
                Log.i("xxx", "onScrollProgressChanged: "+currentProgress);
            }

            @Override
            public void onScrollFinished(ScrollLayout.Status currentStatus) {
                //currentProgress exit：-1  中间open 1  顶部close 0
                // （  从底部到中间为 【-1，0）1  从中间到顶部  【1，0】  ）
                switch (currentStatus){
                    case OPENED:

                        ivSearchResultTitleBack.setVisibility(View.GONE);
                        break;
                    case CLOSED:

                        ivSearchResultTitleBack.setVisibility(View.VISIBLE);
                        break;
                    case EXIT:

                        ivSearchResultTitleBack.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onChildScroll(int top) {

            }
        };
    }

    private void initView() {
        mapManager = new MapManager(mapview, context);
        mapManager.initMap(Constant.mapPath, new MapManager.MapLoadListner() {
            @Override
            public void onMapLoadSuccess() {

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
                EntitySearchResult jingq = GsonUtil.json2Bean(annotation.getDescription(), EntitySearchResult.class);
                tv_baojsj.setText("名称:" + jingq.getMc());
                tv_baojnr.setText("地址:" + jingq.getDz());
                tv_baojr.setText("人数:" + jingq.getRs());
                tv_annotation_dismiss.setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 annotation.hideAnnotationView();
                                                             }
                                                         }
                );

                annotationView.setCalloutView(contentView);
                AnnotationsOverlay annotationsOverlay = mapview.getAnnotationsOverlay();
                int index = annotationsOverlay.indexOf(annotation);
                //把annotation放到z轴最前面
                annotationsOverlay.moveAnnotation(index, -1);
                if (mapview.getResolution() > MapManager.goodResolution) {//缩放地图到最佳大小
                    mapview.zoomToCenter(annotation.getPoint(), MapManager.goodResolution, false);
                }

                mapview.refresh();
                // 将annotationview平移到视图中心
                annotationView.setPanToMapViewCenter(true);
                return annotationView;
            }
        });

        if (!TextUtils.isEmpty(inputKeyword)) {

            tvSearchResultTitleCount.setText(inputKeyword);
        } else {
            tvSearchResultTitleCount.setText(entityTCFL.getMc() + "的搜索结果");
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
        ivSearchResultTitleBack.setVisibility(View.GONE);
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
                searchPresenter.queryZHCXList(layerid, layername, orderType, inputKeyword, radius, longitude, latitude, entityPage.getPageNo(), entityPage.getPageSize());
            }

            @Override
            public void onLoadMore() {
                isRefresh = false;
                searchPresenter.queryZHCXList(layerid, layername, orderType, inputKeyword, radius, longitude, latitude, entityPage.getComputeNextPage(), entityPage.getPageSize());
            }
        });

    }

    public void initOperator() {
        searchPresenter.queryZHCXList(layerid, layername, orderType, inputKeyword, radius, longitude, latitude, entityPage.getPageNo(), entityPage.getPageSize());
    }

    @Override
    public void queryZHCXListSuccessed(List<EntitySearchResult> searchResults, int allpages) {

        rvSearchResult.refreshComplete();
        rvSearchResult.loadMoreComplete();
        if (isRefresh) {
            mDatas.clear();
            mDatas.addAll(searchResults);
            adapter.notifyDataSetChanged();
        } else {
            mDatas.addAll(searchResults);
            tvSearchResultTitleCount.setText("共找到\"" + inputKeyword + "\"相关" + mDatas.size() + "个结果");
            adapter.notifyDataSetChanged();
        }

        updateSearchResultOnMap(mDatas, -1);

    }


    @Override
    public void queryZHCXListUnSuccessed(String msg) {
        rvSearchResult.refreshComplete();
        rvSearchResult.loadMoreComplete();
        ToastTool.getInstance().shortLength(this, msg, true);
    }


    /**
     * @param datas         所有的搜索结果
     * @param selectedIndex 被选中的index
     */
    public void updateSearchResultOnMap(List<EntitySearchResult> datas, int selectedIndex) {
        mapview.getAnnotationsOverlay().removeAllAnnotations();
        if (annotations != null) {
            annotations.clear();
        } else {
            annotations = new ArrayList<>();
        }
        for (int i = 0; i < datas.size(); i++) {
            EntitySearchResult result = datas.get(i);
            Bitmap bitmap = mapManager.createIndexAnnotationView(i, 1, i == selectedIndex);
            double[] xy = mapManager.lonLat2Mercator(result.getLongitude(), result.getLatitude());
            Annotation annotation = new Annotation("" + (i + 1), "搜索结果", result.getJsonStr(), new Dot(xy[0], xy[1]), bitmap);
            annotations.add(annotation);
        }
        mapview.getAnnotationsOverlay().addAnnotations(annotations);
        mapview.refresh();
    }


    @OnClick({R.id.ll_search_top, R.id.iv_search_result_title_back,  R.id.ll_search_result_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search_top:
                Intent intent = new Intent(SearchResultListActivity.this, SearchActivity.class);
                startActivity(intent);
                SearchResultListActivity.this.finish();
                break;
            case R.id.iv_search_result_title_back:
            case R.id.ll_search_result_title:
                switch (scrollDownLayout.getCurrentStatus()){
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
}
