package com.zondy.jwt.jwtmobile.view.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityPredictGridInfo;
import com.zondy.jwt.jwtmobile.entity.EntityLocation;
import com.zondy.jwt.jwtmobile.entity.EntityPred;
import com.zondy.jwt.jwtmobile.entity.EntityPredict;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.global.Constant;
import com.zondy.jwt.jwtmobile.manager.JWTLocationManager;
import com.zondy.jwt.jwtmobile.presenter.IPredictPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.PredictPresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.GsonUtil;
import com.zondy.jwt.jwtmobile.util.MapManager;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IPredictView;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.annotation.AnnotationsOverlay;
import com.zondy.mapgis.android.graphic.GraphicPolygon;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.core.geometry.Dot;
import com.zondy.mapgis.core.map.Map;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 预测方格
 * Created by ywj on 2017/3/29 0029.
 */

public class PredictActivity extends BaseActivity implements IPredictView {
    EntityUser userInfo;
    IPredictPresenter predictPresenter;
    MapManager mapManager;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_zoom_in)
    TextView tvZoomIn;
    @BindView(R.id.tv_zoom_out)
    TextView tvZoomOut;

    String predictId = "91";

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, PredictActivity.class);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_predict;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_predict_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.predict_refresh:
                showLoadingDialog("正在加载...");
                predictPresenter.queryPredict(userInfo.getUserName(), CommonUtil.getDeviceId(context), predictId);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    BroadcastReceiver locationSuccessReceiver;//接收定位成功的广播

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

    void initParam() {
        userInfo = SharedTool.getInstance().getUserInfo(context);
        predictPresenter = new PredictPresenterImpl(this);

        mapManager = new MapManager(mapView, context);
        mapManager.initMap(Constant.getMapPath(), new MapManager.MapLoadListner() {
            @Override
            public void onMapLoadSuccess() {
                EntityLocation userLocation = SharedTool.getInstance().getLocationInfo(context);
                if (userLocation != null) {
                    mapManager.updateUserLocation(userLocation);
//                    mapView.zoomToCenter(new Dot(userLocation.getLongitude(), userLocation.getLatitude()), MapManager.goodResolution, false);
                    mapView.refresh();
                }
            }

            @Override
            public void onMapLoadFail() {
                ToastTool.getInstance().shortLength(context, "地图加载失败", true);
            }
        }, new MapManager.MapAnnotationListener() {
            @Override
            public AnnotationView createAnnotationView(final Annotation annotation) {
                AnnotationView annotationView = new AnnotationView(annotation, context);
                View contentView = LayoutInflater.from(context).inflate(R.layout.content_annotation_jingq, null);

                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels / 2;
                View ll = contentView.findViewById(R.id.ll_xxx);
                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) ll.getLayoutParams();
                p.width = width;
                ll.setLayoutParams(p);
                TextView tv_baojsj = (TextView) contentView.findViewById(R.id.tv_baojsj);
                TextView tv_baojnr = (TextView) contentView.findViewById(R.id.tv_baojnr);
                TextView tv_baojr = (TextView) contentView.findViewById(R.id.tv_baojr);
                TextView tv_annotation_dismiss = (TextView) contentView.findViewById(R.id.tv_annotation_dismiss);
                EntityPred pred = GsonUtil.json2Bean(annotation.getDescription(), EntityPred.class);
                tv_baojsj.setText("" + pred.getCrimeGroupId());
                tv_baojnr.setText("" + pred.getGridNum());
//                tv_baojr.setText("" + pred.getIntervalUpper());
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

    }

    void initView() {
        initActionBar(toolbar, tvTitle, "方格预测");

    }

    void initOperator() {
        showLoadingDialog("正在加载...");
        predictPresenter.queryPredict(userInfo.getUserName(), CommonUtil.getDeviceId(context), predictId);
    }

    @Override
    public void queryPredictSuccess(EntityPredict predict) {
        dismissLoadingDialog();
        if (predict != null) {
            mapView.getGraphicsOverlay().removeAllGraphics();
            mapView.getAnnotationsOverlay().removeAllAnnotations();
            List<EntityPred> entityPreds = predict.getPredResults();
            if (entityPreds != null) {
                for (int i = 0; i < entityPreds.size(); i++) {
                    EntityPred entityPred = entityPreds.get(i);
                    EntityPredictGridInfo gridInfo = entityPred.getGridInfo();
                    Dot dot1 = new Dot(Double.valueOf(gridInfo.getP1X()), Double.valueOf(gridInfo.getP1Y()));
                    Dot dot3 = new Dot(Double.valueOf(gridInfo.getP2X()), Double.valueOf(gridInfo.getP1Y()));
                    Dot dot4 = new Dot(Double.valueOf(gridInfo.getP2X()), Double.valueOf(gridInfo.getP2Y()));
                    Dot dot2 = new Dot(Double.valueOf(gridInfo.getP1X()), Double.valueOf(gridInfo.getP2Y()));
                    Dot dot5 = new Dot(Double.valueOf(gridInfo.getCentX()), Double.valueOf(gridInfo.getCentY()));

                    Dot[] dots = new Dot[]{dot1, dot3, dot4, dot2,dot1};
                    GraphicPolygon graphicPolygon = new GraphicPolygon(dots);
                    graphicPolygon.setBorderlineColor(Color.parseColor("#557ED53D"));
                    graphicPolygon.setBorderlineWidth(4);
                    graphicPolygon.setColor(Color.parseColor("#55C3F4FF"));
                    mapView.getGraphicsOverlay().addGraphic(graphicPolygon);
                    Bitmap bitmap = mapManager.createIndexAnnotationView(i, 1, false);
                    Annotation annotation = new Annotation(gridInfo.getGridNum() + "", "title", entityPred.getJsonStr(), dot5, bitmap);
                    mapView.getAnnotationsOverlay().addAnnotation(annotation);
                    if (i == 0) {
                        mapView.zoomToCenter(dot5, MapManager.goodResolution, false);
                        mapView.refresh();
                    }
                }
            }
            mapView.refresh();
        }
    }

    @Override
    public void queryPredictFail(Exception e) {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context, e.getMessage(), true);

    }

    @OnClick({R.id.tv_location, R.id.tv_zoom_in, R.id.tv_zoom_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_location:
                EntityLocation location = SharedTool.getInstance().getLocationInfo(context);
                if (location != null) {
                    Dot dot = new Dot(location.getLongitude(), location.getLatitude());
                    mapView.zoomToCenter(dot, MapManager.goodResolution, false);
                }
                break;
            case R.id.tv_zoom_in:
                mapView.zoomIn(false);
                break;
            case R.id.tv_zoom_out:
                mapView.zoomOut(false);
                break;
        }
    }
}
