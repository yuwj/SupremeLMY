package com.zondy.jwt.jwtmobile.view.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.base.BaseCommonAdapter;
import com.zondy.jwt.jwtmobile.base.BaseViewHolder;
import com.zondy.jwt.jwtmobile.entity.EntityPredictGridInfo;
import com.zondy.jwt.jwtmobile.entity.EntityLocation;
import com.zondy.jwt.jwtmobile.entity.EntityPred;
import com.zondy.jwt.jwtmobile.entity.EntityPredict;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.entity.EntityZD;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.iv_zoom_in)
    ImageView ivZoomIn;
    @BindView(R.id.iv_zoom_out)
    ImageView ivZoomOut;
    @BindView(R.id.sp_ycfgs)
    Spinner spYcfgs;

    List<EntityPredict> predicts;
    String selectedFanggid;
    BaseCommonAdapter<EntityPredict> predictAdapter;

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
                updatePredictData();
                return super.onOptionsItemSelected(item);
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
        predicts = new ArrayList<>();
        String fgidsStr = userInfo.getFgids();
        if (!TextUtils.isEmpty(fgidsStr)) {
            String[] s = fgidsStr.split(",");
            List<String> list = Arrays.asList(s);
            if (list != null && list.size() > 0) {
                for (String idAndMc : list) {
                    String[] ss = idAndMc.split("_");
                    if (ss != null && ss.length == 2) {
                        EntityPredict p = new EntityPredict();
                        p.setDeptName(ss[0]);
                        p.setFgid(ss[1]);
                        predicts.add(p);
                    }
                }
            }
        }
        mapManager = new MapManager(mapView, context);
        String mapPath = Constant.getMapPath();
        mapManager.initMap(mapPath, new MapManager.MapLoadListner() {
            @Override
            public void onMapLoadSuccess() {
                EntityLocation userLocation = SharedTool.getInstance().getLocationInfo(context);
                if (userLocation != null) {
                    mapManager.updateUserLocation(userLocation);
//                    mapView.zoomToCenter(new Dot(userLocation.getLongitude(), userLocation.getLatitude()), MapManager.goodResolution, false);
                    mapView.refresh();
                }
                updatePredictData();
            }

            @Override
            public void onMapLoadFail() {
                ToastTool.getInstance().shortLength(context, "地图加载失败", true);
            }
        }, new MapManager.MapAnnotationListener() {
            @Override
            public AnnotationView createAnnotationView(final Annotation annotation) {
                AnnotationView annotationView = new AnnotationView(annotation, context);
                View contentView = LayoutInflater.from(context).inflate(R.layout.content_annotation_predict, null);

                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels / 2;
                View ll = contentView.findViewById(R.id.ll_xxx);
                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) ll.getLayoutParams();
                p.width = width;
                ll.setLayoutParams(p);
                TextView tvValue1 = (TextView) contentView.findViewById(R.id.tv_value1);
                TextView tvValue2 = (TextView) contentView.findViewById(R.id.tv_value2);
                TextView tvValue3 = (TextView) contentView.findViewById(R.id.tv_value3);
                TextView tvValue4 = (TextView) contentView.findViewById(R.id.tv_value4);
                TextView tv_annotation_dismiss = (TextView) contentView.findViewById(R.id.tv_annotation_dismiss);
                EntityPred pred = GsonUtil.json2Bean(annotation.getDescription(), EntityPred.class);
                String date = "";
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                try {
                    date = sdf1.format(sdf2.parse(pred.getRealDate()
                    ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tvValue1.setText("预测日期:" + date);
                tvValue2.setText("方格编号:" + pred.getGridNum());
                tvValue3.setText("predProb" + pred.getPredProb());
                tvValue4.setText("interval" + pred.getIntervalUpper() + " ~ " + pred.getIntervalLower());
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
//显示缩放按钮
        mapView.setZoomControlsEnabled(false);
    }

    void initView() {
        initActionBar(toolbar, tvTitle, "方格预测");
        spYcfgs
                .setAdapter(predictAdapter = new BaseCommonAdapter<EntityPredict>(
                        context, predicts, R.layout.item_predict_sp) {
                    @Override
                    public void convert(BaseViewHolder holder, EntityPredict item) {
                        holder.setText(R.id.tv_value, item.getDeptName());
                    }
                });
        spYcfgs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EntityPredict predict = predictAdapter.getItem(position);
                selectedFanggid = predict.getFgid();
                updatePredictData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.iv_location:
                        EntityLocation location = SharedTool.getInstance().getLocationInfo(context);
                        if(location!=null && location.getLatitude()>0&&location.getLongitude()>0){
                            mapView.zoomToCenter(new Dot(location.getLongitude(),location.getLatitude()),MapManager.goodResolution,false);
                        }else{
                            ToastTool.getInstance().shortLength(context,"当前无法定位",true);
                        }
                        break;

                    case R.id.iv_zoom_in:
                        mapView.zoomIn(true);
                        break;

                    case R.id.iv_zoom_out:
                        mapView.zoomOut(true);
                        break;
                }
            }
        };
        ivLocation.setOnClickListener(onClickListener);
        ivZoomOut.setOnClickListener(onClickListener);
        ivZoomIn.setOnClickListener(onClickListener);
    }

    void initOperator() {
    }

    void updatePredictData() {

        showLoadingDialog("正在加载...");
        predictPresenter.queryPredict(userInfo.getUserName(), CommonUtil.getDeviceId(context), selectedFanggid);

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

                    Dot[] dots = new Dot[]{dot1, dot3, dot4, dot2, dot1};
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

    @OnClick({R.id.iv_location, R.id.iv_zoom_in, R.id.iv_zoom_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_location:
                EntityLocation location = SharedTool.getInstance().getLocationInfo(context);
                if (location != null) {
                    Dot dot = new Dot(location.getLongitude(), location.getLatitude());
                    mapView.zoomToCenter(dot, MapManager.goodResolution, false);
                }
                break;
            case R.id.iv_zoom_in:
                mapView.zoomIn(false);
                break;
            case R.id.iv_zoom_out:
                mapView.zoomOut(false);
                break;
        }
    }
}
