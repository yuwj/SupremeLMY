package com.zondy.jwt.jwtmobile.view.impl;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.YuwjBaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityAskService;
import com.zondy.jwt.jwtmobile.entity.EntityJingq;
import com.zondy.jwt.jwtmobile.entity.EntityLocation;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.global.Constant;
import com.zondy.jwt.jwtmobile.manager.JWTLocationManager;
import com.zondy.jwt.jwtmobile.presenter.IJingqHandlePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.JingqHandlePresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.GsonUtil;
import com.zondy.jwt.jwtmobile.util.MapManager;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IAskServiceMainView;
import com.zondy.jwt.jwtmobile.view.IAskServiceView;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.annotation.AnnotationsOverlay;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.core.geometry.Dot;
import com.zondy.mapgis.core.geometry.Rect;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 请求服务
 * Created by ywj on 2017/4/1 0001.
 */

public class AskServiceMainActivity2 extends YuwjBaseActivity implements IAskServiceMainView {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
//    @BindView(R.id.vp_container)
//    ViewPager vpContainer;

    MapManager mapManager;
    Fragment askBukFragment;
    Fragment askZengyFragment;
    Fragment askChaxFragment;
    Fragment askZousFragment;
    Fragment askQitFragment;
    Fragment selectedFragment;//选中的fragment
    int selectedFragmentPosition;//选中的fragment的position

    String jingqid;
    IJingqHandlePresenter jingqHandlePresenter;
    EntityJingq entityJingq;
    EntityUser userInfo;
    BroadcastReceiver locationSuccessReceiver;//接收定位成功的广播
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tv_jingq_baojnr)
    TextView tvJingqBaojnr;
    @BindView(R.id.tv_jingq_baojsj)
    TextView tvJingqBaojsj;
    @BindView(R.id.tv_jingq_baojdd)
    TextView tvJingqBaojdd;
    @BindView(R.id.tv_jingq_jiejdw)
    TextView tvJingqJiejdw;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.tv_jingq_position)
    LinearLayout tvJingqPosition;
    @BindView(R.id.rb_ask_buk)
    RadioButton rbAskBuk;
    @BindView(R.id.rb_ask_zengy)
    RadioButton rbAskZengy;
    @BindView(R.id.rg_ask_type)
    RadioGroup rgAskType;
    @BindView(R.id.ll_ask_service_input_container)
    LinearLayout llAskServiceInputContainer;

    FragmentManager fragmentManager;
    boolean isFirstZoomRange = true;//是否是第一次将警情标注和手机定位信息缩放到屏幕范围内

    public static Intent createIntent(Context context, String jingqid) {
        Intent intent = new Intent(context, AskServiceMainActivity2.class);
        intent.putExtra("jingqid", jingqid);
        return intent;
    }


    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_askforservice2;
    }

    @Override
    public void initParam() {
        Intent intent = getIntent();
        jingqid = intent.getStringExtra("jingqid");
        ToastTool.getInstance().shortLength(context, "jingqid:" + jingqid, true);
        userInfo = SharedTool.getInstance().getUserInfo(context);
        jingqHandlePresenter = new JingqHandlePresenterImpl(this);

        fragmentManager = getSupportFragmentManager();


    }

    @Override
    public void initView() {
        initActionBar(toolbar, tvTitle, "请求服务");
        scrollView.smoothScrollTo(0,0);//解决scrollView自动滑到获取焦点控件的位置.
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
        mapManager = new MapManager(mapView, context);
        mapManager.initMap(Constant.getMapPath(), new MapManager.MapLoadListner() {
            @Override
            public void onMapLoadSuccess() {
                isMapLoadSuccess = true;
                EntityLocation entityLocation = SharedTool.getInstance().getLocationInfo(context);
                if(entityLocation != null && entityLocation.getLongitude()>0 && entityLocation.getLatitude() >0){
                    double[] xy = mapManager.lonLat2Mercator(entityLocation.getLongitude(), entityLocation.getLatitude());
                    mapView.zoomToCenter(new Dot(entityLocation.getLongitude(), entityLocation.getLatitude()), mapManager.goodResolution, false);
                }
                 if (entityJingq != null) {
                    updateMapView(entityJingq);
                }
                mapView.refresh();
            }

            @Override
            public void onMapLoadFail() {
                ToastTool.getInstance().shortLength(context,"地图加载失败",true);
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

                if ("jingq".equals(annotation.getTitle())) {
                    EntityJingq jingq = GsonUtil.json2Bean(annotation.getDescription(), EntityJingq.class);
                    tv_baojsj.setText("报警时间:" + jingq.getBaojsj());
                    tv_baojnr.setText("报警内容:" + jingq.getBaojnr());
                    tv_baojr.setText("报警人:" + jingq.getBaojr());
                    tv_annotation_dismiss.setOnClickListener(new View.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(View v) {
                                                                     annotation.hideAnnotationView();
                                                                 }
                                                             }
                    );
                }


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
        rgAskType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                try {
                    int selectType = Integer.valueOf(rb.getTag().toString());
                    updateSelectedFragment(selectType);
                    selectedFragmentPosition = selectType - 1;
                    invalidateOptionsMenu();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        updateSelectedFragment(EntityAskService.ASK_FOR_SERVICE_TYPE_BUK);

    }


    public void updateSelectedFragment(int selectedType) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (selectedType) {
            case EntityAskService.ASK_FOR_SERVICE_TYPE_BUK:
                if (askBukFragment == null) {
                    askBukFragment = AskBukFragment.createInstance(entityJingq);
                }
                fragmentTransaction.replace(R.id.ll_ask_service_input_container, askBukFragment);
                fragmentTransaction.commit();
                selectedFragment = askBukFragment;

                break;
            case EntityAskService.ASK_FOR_SERVICE_TYPE_ZENGY:
                if (askZengyFragment == null) {
                    askZengyFragment = AskZengyFragment.createInstance(entityJingq);
                }
                fragmentTransaction.replace(R.id.ll_ask_service_input_container, askZengyFragment);
                fragmentTransaction.commit();
                selectedFragment = askZengyFragment;
                break;

            case EntityAskService.ASK_FOR_SERVICE_TYPE_CHAX:
                if (askChaxFragment == null) {
                    askChaxFragment = AskChaxFragment.createInstance(entityJingq);
                }
                fragmentTransaction.replace(R.id.ll_ask_service_input_container, askChaxFragment);
                fragmentTransaction.commit();
                selectedFragment = askChaxFragment;
                break;

            case EntityAskService.ASK_FOR_SERVICE_TYPE_ZOUS:
                if (askZousFragment == null) {
                    askZousFragment = AskZousFragment.createInstance(entityJingq);
                }
                fragmentTransaction.replace(R.id.ll_ask_service_input_container, askZousFragment);
                fragmentTransaction.commit();
                selectedFragment = askZousFragment;
                break;

            case EntityAskService.ASK_FOR_SERVICE_TYPE_QIT:
                if (askQitFragment == null) {
                    askQitFragment = AskQitFragment.createInstance(entityJingq);
                }
                fragmentTransaction.replace(R.id.ll_ask_service_input_container, askQitFragment);
                fragmentTransaction.commit();
                selectedFragment = askQitFragment;
                break;
        }
    }

    @Override
    public void initOperator() {
        queryData();
    }

    void queryData() {
        String jh = userInfo.getUserName();
        String simid = CommonUtil.getDeviceId(context);
        jingqHandlePresenter.queryJingqWithAskServiceMainView(jingqid, jh, simid);
        showLoadingDialog("正在加载警情...");
    }

    public void updateJingqView(EntityJingq entityJingq) {

        tvJingqBaojnr.setText(entityJingq.getBaojr());
        tvJingqBaojsj.setText(entityJingq.getBaojsj());
        tvJingqBaojdd.setText(entityJingq.getBaojrdh());
        tvJingqJiejdw.setText(entityJingq.getGxdwdm());
    }

    boolean isMapLoadSuccess;//地图是否加载完成.
    Annotation annotationJingq;//警情标注

    public void updateMapView(EntityJingq entityJingq) {
        if (isMapLoadSuccess && entityJingq != null && entityJingq.getLongitude() > 0 && entityJingq.getLatitude() > 0) {
            if (annotationJingq == null) {
                annotationJingq = new Annotation("jingq", entityJingq.toJsonStr(), new Dot(entityJingq.getLongitude(), entityJingq.getLatitude()), BitmapFactory.decodeResource(getResources(), R.drawable.ic_position_blue_type1));
                mapManager.addAnnotaion(annotationJingq);
            } else {
                annotationJingq.setPoint(new Dot(entityJingq.getLongitude(), entityJingq.getLatitude()));
            }
            EntityLocation location = SharedTool.getInstance().getLocationInfo(context);
            if(isFirstZoomRange&&entityJingq!= null && entityJingq.getLatitude()>0&&entityJingq.getLatitude()>0&&location.getLongitude()>0){
                double xMin = entityJingq.getLongitude()-location.getLongitude()>0?location.getLongitude():entityJingq.getLongitude();
                double yMin = entityJingq.getLatitude()-location.getLatitude()>0?location.getLatitude():entityJingq.getLatitude();
                double xMax = entityJingq.getLongitude()-location.getLongitude()>0?entityJingq.getLongitude():location.getLongitude();
                double yMax = entityJingq.getLatitude()-location.getLatitude()>0?entityJingq.getLatitude():location.getLatitude();
                mapView.zoomToRange(new Rect(xMin,yMin,xMax,yMax),false);
                mapView.refresh();
                isFirstZoomRange = false;
            }

            mapView.refresh();
            annotationJingq.showAnnotationView();
        }
    }

    @Override
    public void onQueryJingqSuccess(EntityJingq entityJingq) {
        dismissLoadingDialog();
        if (entityJingq != null) {
            this.entityJingq = entityJingq;
            updateJingqView(this.entityJingq);
            updateMapView(this.entityJingq);
            if (selectedFragment != null) {
                if (selectedFragment instanceof AskBukFragment) {
                    ((AskBukFragment) selectedFragment).setEntityJingq(this.entityJingq);
                } else if (selectedFragment instanceof AskZengyFragment) {
                    ((AskZengyFragment) askZengyFragment).setEntityJingq(this.entityJingq);
                }
            }


        }
    }

    @Override
    public void onQueryJingqFail(Exception e) {
        dismissLoadingDialog();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_askservice_launch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_launch_buk:
            case R.id.menu_launch_zengy:
            case R.id.menu_launch_chax:
            case R.id.menu_launch_zous:
            case R.id.menu_launch_qit:
                ((IAskServiceView) selectedFragment).launchAsk();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        List<MenuItem> menuItems = new ArrayList<>();
        MenuItem buk = menu.findItem(R.id.menu_launch_buk);
        MenuItem zengy = menu.findItem(R.id.menu_launch_zengy);
        MenuItem chax = menu.findItem(R.id.menu_launch_chax);
        MenuItem zous = menu.findItem(R.id.menu_launch_zous);
        MenuItem qit = menu.findItem(R.id.menu_launch_qit);
        menuItems.add(buk);
        menuItems.add(zengy);
        menuItems.add(chax);
        menuItems.add(zous);
        menuItems.add(qit);
        for (int i = 0; i < menuItems.size(); i++) {
            if (i == selectedFragmentPosition) {
                menuItems.get(i).setVisible(true);
            } else {
                menuItems.get(i).setVisible(false);
            }
        }
        return super.onPrepareOptionsMenu(menu);
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


    public void updatePosition(EntityLocation entityLocation) {
        if (mapManager != null) {
            mapManager.updateUserLocation(entityLocation);
        }
    }

    public void registerBroadcast() {
        locationSuccessReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                EntityLocation location = (EntityLocation) intent.getSerializableExtra("entityLocation");


                updatePosition(location);
                if(isFirstZoomRange&&entityJingq!= null && entityJingq.getLatitude()>0&&entityJingq.getLatitude()>0){
                    double xMin = entityJingq.getLongitude()-location.getLongitude()>0?location.getLongitude():entityJingq.getLongitude();
                    double yMin = entityJingq.getLatitude()-location.getLatitude()>0?location.getLatitude():entityJingq.getLatitude();
                    double xMax = entityJingq.getLongitude()-location.getLongitude()>0?entityJingq.getLongitude():location.getLongitude();
                    double yMax = entityJingq.getLatitude()-location.getLatitude()>0?entityJingq.getLatitude():location.getLatitude();
                    mapView.zoomToRange(new Rect(xMin,yMin,xMax,yMax),false);
                    mapView.refresh();
                    isFirstZoomRange = false;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS || (resultCode == Activity.RESULT_OK && requestCode == AskZengyFragment.REQ_CODE_EDIT_IMAGE)) {

            if(askZengyFragment != null){
                ((AskZengyFragment)askZengyFragment).onAskZengyFragmentResult(requestCode,resultCode,data);
            }
        }
    }
}
