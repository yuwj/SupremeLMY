package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityLocation;
import com.zondy.jwt.jwtmobile.global.Constant;
import com.zondy.jwt.jwtmobile.util.MapManager;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.core.geometry.Dot;

import butterknife.BindView;

import static com.zondy.jwt.jwtmobile.R.id.mapView;
import static com.zondy.jwt.jwtmobile.R.id.mapview;

/**
 * Created by sheep on 2017/3/28.
 */

public class ShujcjWBTCCJActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(mapview)
    MapView mapView;
    @BindView(R.id.rl_map_container)
    RelativeLayout rlMapContainer;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    MapManager mapManager;
    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_shujcj_wbtccj;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShujcjWBTCCJActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        initViews();
    }

    private void initParams() {
        mapManager = new MapManager(mapView, context);
        mapManager.initMap(Constant.mapPath, new MapManager.MapLoadListner() {
            @Override
            public void onMapLoadSuccess() {
                EntityLocation entityLocation = SharedTool.getInstance().getLocationInfo(context);
                double[] xy = mapManager.lonLat2Mercator(entityLocation.getLongitude(), entityLocation.getLatitude());
                mapView.zoomToCenter(new Dot(xy[0], xy[1]), mapManager.goodResolution, false);
                mapView.refresh();
            }

            @Override
            public void onMapLoadFail() {

            }
        }, new MapManager.MapAnnotationListener() {
            @Override
            public AnnotationView createAnnotationView(Annotation annotation) {
                return null;
            }
        });
    }

    private void initViews() {
        //解决mapview同scrollview的滑动冲突
        mapView.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    scrollView.requestDisallowInterceptTouchEvent(false);
                }else{
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        btnCommit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
