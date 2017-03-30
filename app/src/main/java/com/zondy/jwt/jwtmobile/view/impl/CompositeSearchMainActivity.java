package com.zondy.jwt.jwtmobile.view.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityLocation;
import com.zondy.jwt.jwtmobile.global.Constant;
import com.zondy.jwt.jwtmobile.manager.JWTLocationManager;
import com.zondy.jwt.jwtmobile.util.MapManager;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.core.geometry.Dot;

import butterknife.BindView;

/**
 * Created by sheep on 2017/1/5.
 */

public class CompositeSearchMainActivity extends BaseActivity {

    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.tv_locationInfo)
    TextView tv_locationInfo;
    MapManager mapManager;
    BroadcastReceiver locationSuccessReceiver;//接收定位成功的广播

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, CompositeSearchMainActivity.class);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_composite_search_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        initViews();
    }


    private void initParams() {
//        mapView.setTapListener(new MapView.MapViewTapListener() {
//            @Override
//            public void mapViewTap(PointF pointF) {
//
//                ToastTool.getInstance().shortLength(context,""+pointF.x+" "+pointF.y,true);
//                Dot dot = mapView.viewPointToMapPoint(pointF);
//
//                ToastTool.getInstance().shortLength(context,""+dot.x+" "+dot.y,true);
//            }
//        });
//        mapView.setZoomChangedListener(new MapView.MapViewZoomChangedListener() {
//            @Override
//            public void mapViewZoomChanged(MapView mapView, double v, double v1) {
//                ToastTool.getInstance().shortLength(context,"zoom"+v1,true);
//            }
//        });
        mapManager = new MapManager(mapView, context);
        mapManager.initMap(Constant.getMapPath(), new MapManager.MapLoadListner() {
            @Override
            public void onMapLoadSuccess() {
                mapView.getCenterPoint();

                ToastTool.getInstance().shortLength(context, "地图加载成功", false);
                EntityLocation entityLocation = SharedTool.getInstance().getLocationInfo(context);
//                ToastTool.getInstance().shortLength(context, "" + entityLocation.getLongitude() + " " + entityLocation.getLatitude(), true);
                double[] xy = null;
                switch (Constant.JWT_AREA_SELECTED) {
                    case Constant.JWT_AREA_HA:
                        xy = new double[]{entityLocation.getLongitude(), entityLocation.getLatitude()};
                        mapView.zoomToCenter(new Dot(xy[0], xy[1]), mapManager.goodResolution, false);
                        break;
                    case Constant.JWT_AREA_WH:
                        xy = mapManager.lonLat2Mercator(entityLocation.getLongitude(), entityLocation.getLatitude());
                        mapView.zoomToCenter(new Dot(xy[0], xy[1]), mapManager.goodResolution, false);
                        break;
                    default:
                        xy = new double[]{entityLocation.getLongitude(), entityLocation.getLatitude()};
                        mapView.zoomToCenter(new Dot(xy[0], xy[1]), mapManager.goodResolution, false);
                        break;

                }
                mapView.refresh();
            }

            @Override
            public void onMapLoadFail() {
                ToastTool.getInstance().shortLength(context, "地图加载失败", false);

            }
        }, new MapManager.MapAnnotationListener() {
            @Override
            public AnnotationView createAnnotationView(Annotation annotation) {
                return null;
            }
        });

    }

    private void initViews() {

        EntityLocation l = SharedTool.getInstance().getLocationInfo(context);
        mapManager.updateUserLocation(l);
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CompositeSearchInputActivity.createIntent(CompositeSearchMainActivity.this));
                CompositeSearchMainActivity.this.finish();
            }
        });
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
                    String oldStr = tv_locationInfo.getText().toString();
                    tv_locationInfo.setText("\n" + location.getJsonStr() + oldStr);
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

}
