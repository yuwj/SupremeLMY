package com.zondy.jwt.jwtmobile.view.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.YuwjBaseActivity;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 请求服务
 * Created by ywj on 2017/4/1 0001.
 */

public class AskServiceMainActivity extends YuwjBaseActivity implements IAskServiceMainView {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.vpi_tab)
    TabPageIndicator vpiTab;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;

    MapManager mapManager;
    Fragment askBukFragment;
    Fragment askZengyFragment;
//    Fragment askChaxFragment;
//    Fragment askZousFragment;
//    Fragment askQitFragment;
    List<Fragment> fragmentDatas;
    List<String> fragmentTitles;
    PagerAdapter pagerAdapter;

    String jingqid;
    IJingqHandlePresenter jingqHandlePresenter;
    EntityJingq entityJingq;
    EntityUser userInfo;
    BroadcastReceiver locationSuccessReceiver;//接收定位成功的广播
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

    public static Intent createIntent(Context context, String jingqid) {
        Intent intent = new Intent(context, AskServiceMainActivity.class);
        intent.putExtra("jingqid", jingqid);
        return intent;
    }


    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_askforservice;
    }

    @Override
    public void initParam() {
        Intent intent = getIntent();
        jingqid = intent.getStringExtra("jingqid");
        ToastTool.getInstance().shortLength(context, "jingqid:" + jingqid, true);
        userInfo = SharedTool.getInstance().getUserInfo(context);
        jingqHandlePresenter = new JingqHandlePresenterImpl(this);
        fragmentDatas = new ArrayList<>();
        fragmentTitles = new ArrayList<>();
        fragmentTitles.add("请求布控");
//        fragmentTitles.add("请求增援");

        fragmentDatas.add(askBukFragment = AskBukFragment.createInstance(this.entityJingq));
//        fragmentDatas.add(askZengyFragment = AskZengyFragment.createInstance(this.entityJingq));

        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentDatas.get(position);
            }

            @Override
            public int getCount() {
                return fragmentDatas.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return fragmentTitles.get(position);
            }
        };


    }

    @Override
    public void initView() {
        initActionBar(toolbar, tvTitle, "请求服务");

        mapManager = new MapManager(mapView, context);
        mapManager.initMap(Constant.getMapPath(), new MapManager.MapLoadListner() {
            @Override
            public void onMapLoadSuccess() {
                isMapLoadSuccess = true;
                EntityLocation entityLocation = SharedTool.getInstance().getLocationInfo(context);
                double[] xy = mapManager.lonLat2Mercator(entityLocation.getLongitude(), entityLocation.getLatitude());
                mapView.zoomToCenter(new Dot(xy[0], xy[1]), mapManager.goodResolution, false);
                if(entityJingq != null){
                    updateMapView(entityJingq);
                }
                mapView.refresh();
            }

            @Override
            public void onMapLoadFail() {

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

        vpContainer.setAdapter(pagerAdapter);
        vpiTab.setViewPager(vpContainer);
        vpiTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();//刷新菜单
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    public void updateJingqView(EntityJingq entityJingq){

        tvJingqBaojnr.setText(entityJingq.getBaojr());
        tvJingqBaojsj.setText(entityJingq.getBaojsj());
        tvJingqBaojdd.setText(entityJingq.getBaojrdh());
        tvJingqJiejdw.setText(entityJingq.getGxdwdm());
    }
    boolean isMapLoadSuccess;//地图是否加载完成.
    Annotation annotationJingq;//警情标注
    public void updateMapView(EntityJingq entityJingq){
        if (isMapLoadSuccess && entityJingq != null && entityJingq.getLongitude() > 0 && entityJingq.getLatitude() > 0) {
            if(annotationJingq == null){
                annotationJingq = new Annotation("jingq", entityJingq.toJsonStr(), new Dot(entityJingq.getLongitude(), entityJingq.getLatitude()), BitmapFactory.decodeResource(getResources(), R.drawable.ic_position_blue_type1));
                mapManager.addAnnotaion(annotationJingq);
            }else{
                annotationJingq.setPoint(new Dot(entityJingq.getLongitude(), entityJingq.getLatitude()));
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
            if(askBukFragment != null){
                ((AskBukFragment)askBukFragment).setEntityJingq(this.entityJingq);
            }
            if(askZengyFragment != null){
                ((AskZengyFragment)askZengyFragment).setEntityJingq(this.entityJingq);
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
                ((IAskServiceView) fragmentDatas.get(vpContainer.getCurrentItem())).launchAsk();
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
            if (i == vpContainer.getCurrentItem()) {
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
}
