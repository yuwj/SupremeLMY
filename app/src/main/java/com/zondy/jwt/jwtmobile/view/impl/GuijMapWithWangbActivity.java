package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityBaseGuij;
import com.zondy.jwt.jwtmobile.entity.EntityGuijWithLvg;
import com.zondy.jwt.jwtmobile.entity.EntityGuijWithWangb;
import com.zondy.jwt.jwtmobile.entity.EntitySearchResult;
import com.zondy.jwt.jwtmobile.global.Constant;
import com.zondy.jwt.jwtmobile.ui.DividerItemDecoration;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.GsonUtil;
import com.zondy.jwt.jwtmobile.util.MapManager;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.annotation.AnnotationsOverlay;
import com.zondy.mapgis.android.graphic.GraphicMultiPoint;
import com.zondy.mapgis.android.graphic.GraphicPolylin;
import com.zondy.mapgis.android.graphic.GraphicsOverlay;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.core.geometry.Dot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GuijMapWithWangbActivity extends BaseActivity {



    @BindView(R.id.mapView)
    MapView mapView;
    MapManager mapManager;
    List<EntityGuijWithWangb> guijDatas;
    List<Dot> dots;
    GraphicPolylin graphicPolylin;
    GraphicMultiPoint graphicMultiPoint;
    GraphicsOverlay graphicsOverlay;
    String title;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_position)
    XRecyclerView rvPosition;
    List<Annotation> annotations;

    CommonAdapter<EntityGuijWithWangb> guijAdapter;


    public static Intent createIntent(Context context, List<EntityGuijWithWangb> pathDatas, String title) {
        Intent intent = new Intent(context, GuijMapWithWangbActivity.class);
        intent.putExtra("guijDatas", (Serializable) pathDatas);
        intent.putExtra("title", title);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_guij_map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
    }

    public void initParam() {
        guijDatas = (List<EntityGuijWithWangb>) getIntent().getSerializableExtra("guijDatas");
        title = getIntent().getStringExtra("title");
        annotations = new ArrayList<>();
        dots = new ArrayList<>();

        int inflatItemId = R.layout.item_guij_lvg;

        guijAdapter = new CommonAdapter<EntityGuijWithWangb>(context, inflatItemId, guijDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityGuijWithWangb entitySearchResult, int position) {


                ImageView iv = holder.getView(R.id.iv_index);
                Bitmap bitmap = CommonUtil.createIndexAnnotationView(context,position-1,1,true);
                if(bitmap != null){

                    iv.setImageBitmap(bitmap);
                }

                holder.setText(R.id.tv_lvg_name, entitySearchResult.getPositionName());
                holder.setText(R.id.tv_address_name, entitySearchResult.getAddress());
                holder.setText(R.id.tv_time, entitySearchResult.getStartTime() + "  ~  " + entitySearchResult.getEndTime());
                RatingBar ratingBar = holder.getView(R.id.rb_star);


//                    ratingBar.setRating(((EntityGuijWithWangb) entitySearchResult).getStartCount());

            }
        };

        guijAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                for (int i = 0; i < annotations.size(); i++) {
                    Annotation a = annotations.get(i);
                    Bitmap b = null;
                    if (a.getUid().equals((position) + "")) {
                        b = mapManager.createIndexAnnotationView(i, 1, true);
                        a.showAnnotationView();
                        mapView.zoomToCenter(a.getPoint(), MapManager.goodResolution, false);
                    } else {
                        b = mapManager.createIndexAnnotationView(i, 1, false);
                    }
                    a.setImage(b);
                }
                mapView.refresh();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


        mapManager = new MapManager(mapView, context);
        mapManager.initMap(Constant.getMapPath(), new MapManager.MapLoadListner() {
            @Override
            public void onMapLoadSuccess() {
                initOperator();
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
                View contentView = LayoutInflater.from(context).inflate(R.layout.content_annotation_guij, null);
                View ll = contentView.findViewById(R.id.ll_xxx);
                ViewGroup.LayoutParams params = ll.getLayoutParams();

                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                if (params != null) {
                    params.width = displayMetrics.widthPixels / 2;
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                } else {
                    params = new ViewGroup.LayoutParams(displayMetrics.widthPixels / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                ll.setLayoutParams(params);
                TextView tv_baojsj = (TextView) contentView.findViewById(R.id.tv_baojsj);
                TextView tv_baojnr = (TextView) contentView.findViewById(R.id.tv_baojnr);
                TextView tv_baojr = (TextView) contentView.findViewById(R.id.tv_baojr);
                TextView tv_annotation_dismiss = (TextView) contentView.findViewById(R.id.tv_annotation_dismiss);


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
                AnnotationsOverlay annotationsOverlay = mapView.getAnnotationsOverlay();
                int index = annotationsOverlay.indexOf(annotation);
                //把annotation放到z轴最前面
                annotationsOverlay.moveAnnotation(index, -1);
                if (mapView.getResolution() > MapManager.goodResolution) {//缩放地图到最佳大小
                    mapView.zoomToCenter(annotation.getPoint(), MapManager.goodResolution, false);
                }
                // 将annotationview平移到视图中心
                annotationView.setPanToMapViewCenter(true);
                mapView.refresh();
                return annotationView;
            }
        });

        for (int i = 0; i < guijDatas.size(); i++) {
            EntityBaseGuij entitySearchResult = guijDatas.get(i);
            boolean isSelected = false;
            if (i == 0) {
                isSelected = true;
            }
            Bitmap bitmap = mapManager.createIndexAnnotationView(i, 1, isSelected);
            double[] xy = mapManager.lonLat2Mercator(entitySearchResult.getLongitude(), entitySearchResult.getLatitude());
            Dot dot = new Dot(xy[0], xy[1]);
            Annotation annotation = new Annotation("" + (i + 1), "轨迹", entitySearchResult.getJsonStr(), dot, bitmap);
            annotations.add(annotation);
            dots.add(dot);
        }
    }

    public void initView() {
        initActionBar(toolbar, tvTitle, title);
        rvPosition.setLayoutManager(new LinearLayoutManager(context));
        rvPosition.setAdapter(guijAdapter);
        rvPosition.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));
        rvPosition.setPullRefreshEnabled(false);
        rvPosition.setLoadingMoreEnabled(false);

    }

    public void initOperator() {

        mapView.getAnnotationsOverlay().addAnnotations(annotations);
        if (annotations.size() > 0) {
            annotations.get(0).showAnnotationView();
        }


        graphicsOverlay = mapView.getGraphicsOverlay();
        graphicPolylin = new GraphicPolylin();
        graphicMultiPoint = new GraphicMultiPoint();
        Dot[] mDots = dots.toArray(new Dot[dots.size()]);
        graphicPolylin.appendPoints(mDots);
        graphicMultiPoint.appendPoints(mDots);
        graphicsOverlay.addGraphic(graphicPolylin);
        graphicsOverlay.addGraphic(graphicMultiPoint);
        mapManager.zoomToRange(dots);
        mapView.refresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }
}
