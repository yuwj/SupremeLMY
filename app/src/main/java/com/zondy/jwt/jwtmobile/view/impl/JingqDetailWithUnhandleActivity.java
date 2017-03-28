package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityJingq;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.global.Constant;
import com.zondy.jwt.jwtmobile.presenter.IJingqHandlePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.JingqHandlePresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.GsonUtil;
import com.zondy.jwt.jwtmobile.util.MapManager;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IJingqDetailWithUnhandleView;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.annotation.AnnotationsOverlay;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.core.geometry.Dot;

import butterknife.BindView;
import butterknife.OnClick;


public class JingqDetailWithUnhandleActivity extends BaseActivity implements IJingqDetailWithUnhandleView {


    EntityJingq entityJingq;
    IJingqHandlePresenter jingqHandlePresenter;
    EntityUser user;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mapview)
    MapView mapview;
    @BindView(R.id.tv_baojingshijian)
    TextView tvBaojingshijian;
    @BindView(R.id.tv_jingqxq_anjlx)
    TextView tvJingqxqAnjlx;
    @BindView(R.id.tv_baojingren)
    TextView tvBaojingren;
    @BindView(R.id.tv_baojingdianhua)
    TextView tvBaojingdianhua;
    @BindView(R.id.tv_baojingdizhi)
    TextView tvBaojingdizhi;
    @BindView(R.id.tv_baojingneirong)
    TextView tvBaojingneirong;
    @BindView(R.id.btn_reached_confirm)
    Button btnReachedConfirm;
    @BindView(R.id.btn_accept)
    Button btnAccept;
    @BindView(R.id.btn_reback)
    Button btnReback;
    @BindView(R.id.sv_jingq_container)
    ScrollView scrollView;

    MapManager mapManager;
    Annotation annoJingq;

    public static Intent createIntent(Context context, EntityJingq jingq) {
        Intent intent = new Intent(context, JingqDetailWithUnhandleActivity.class);
        intent.putExtra("entityJingq", jingq);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {

        return R.layout.activity_jingqxq;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
    }

    public void initParam() {
        entityJingq = (EntityJingq) getIntent().getSerializableExtra("entityJingq");
        jingqHandlePresenter = new JingqHandlePresenterImpl(this);
        user = SharedTool.getInstance().getUserInfo(context);


    }

    public void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //解决mapview同scrollview的滑动冲突
        mapview.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
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
        mapManager = new MapManager(mapview, context);
        mapManager.initMap(Constant.mapPath, new MapManager.MapLoadListner() {
            @Override
            public void onMapLoadSuccess() {
                Log.i("xxxx", "" + entityJingq.getLongitude());
                Log.i("xxxx", "" + entityJingq.getLatitude());
                if (entityJingq.getLongitude() > 0 && entityJingq.getLatitude() > 0) {
                    double[] xy = mapManager.lonLat2Mercator(entityJingq.getLongitude(), entityJingq.getLatitude());
                    Log.i("xxxx", "xy: " + xy[0] + "   " + xy[1]);

                    double[] yz = mapManager.Mercator2lonLat(1.2735793594229463E7, 3567123.954291529);
                    Log.i("xxxx", "yz: " + yz[0] + "   " + yz[1]);
                    double[] za = mapManager.lonLat2Mercator(yz[0], yz[1]);
                    Log.i("xxxx", "za: " + za[0] + "   " + za[1]);
                    annoJingq = new Annotation("警情", entityJingq.toJsonStr(), new Dot(xy[0], xy[1]), BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_position_blue_type1));
                    mapManager.addAnnotaion(annoJingq);
                    annoJingq.showAnnotationView();
                    mapview.refresh();
                }
//                mapview.setTapListener(new MapView.MapViewTapListener() {
//                    @Override
//                    public void mapViewTap(PointF pointF) {
//                        Dot point = mapview.viewPointToMapPoint(pointF);
//                        Log.i("xxxx", "onMapLoadSuccess2: " + point.getX() + "   " + point.getY());
//                        Annotation annoJingq = new Annotation("警情", entityJingq.toJsonStr(), point, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_position_blue_type1));
//                        mapManager.addAnnotaion(annoJingq);
//                    }
//                });
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
                EntityJingq jingq = GsonUtil.json2Bean(annotation.getDescription(), EntityJingq.class);
                tv_baojsj.setText("报警时间:"+jingq.getBaojsj());
                tv_baojnr.setText("报警内容:"+jingq.getBaojnr());
                tv_baojr.setText("报警人:"+jingq.getBaojr());
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
                if(mapview.getResolution() > MapManager.goodResolution){//缩放地图到最佳大小
                    mapview.zoomToCenter(annotation.getPoint(), MapManager.goodResolution, false);
                }
                mapview.refresh();
                // 将annotationview平移到视图中心
                annotationView.setPanToMapViewCenter(true);
                return annotationView;
            }
        });
        mapview.setZoomChangedListener(new MapView.MapViewZoomChangedListener() {
            @Override
            public void mapViewZoomChanged(MapView mapView, double v, double v1) {
                Log.i("xxx",v+"-->"+v1);
            }
        });
        updateJingqView(entityJingq);
    }

    public void updateJingqView(EntityJingq jingq) {
        tvBaojingshijian.setText(entityJingq.getBaojsj());
        if (entityJingq.getAjlb() != null && entityJingq.getAjlx() != null && entityJingq.getAjxl() != null) {
            tvJingqxqAnjlx.setText(entityJingq.getAjlb() + ">" + entityJingq.getAjlx() + ">" + entityJingq.getAjxl());
        } else if (entityJingq.getAjlx() != null) {
            tvJingqxqAnjlx.setText(entityJingq.getAjlb() + ">" + entityJingq.getAjlx());
        } else {
            tvJingqxqAnjlx.setText(entityJingq.getAjlb());
        }
        tvBaojingren.setText(entityJingq.getBaojr());
        String phoneNum = entityJingq.getBaojrdh();
        if (!TextUtils.isEmpty(phoneNum)) {
            if (phoneNum.startsWith("0")) {
                phoneNum = phoneNum.replaceFirst("0", "");
            }
        } else {
            phoneNum = "";
        }
        tvBaojingdianhua.setText(phoneNum);
        tvBaojingdizhi.setText(entityJingq.getBaojdz());
        tvBaojingneirong.setText("    " + entityJingq.getBaojnr());
        if(jingq.getJingqzt()==0){
            btnAccept.setVisibility(View.VISIBLE);
            btnReback.setVisibility(View.GONE);
        }
        if(jingq.getJingqzt()==1){
            btnAccept.setVisibility(View.GONE);
            btnReback.setVisibility(View.VISIBLE);
        }
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


//    @Override
//    public void reLoadJingqSuccess(EntityJingq jingq) {
//        dismissLoadingDialog();
//        this.entityJingq = jingq;
//        updateJingqView(jingq);
//    }
//
//    @Override
//    public void reLoadJIngqFalied(Exception e) {
//        dismissLoadingDialog();
//        ToastTool.getInstance().shortLength(context, e.getMessage(), true);
//    }

    @Override
    public void receiveJingqSuccess() {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context, "接收成功", true);

        entityJingq
                .setState(EntityJingq.HADREAD);
        btnAccept.setVisibility(View.GONE);
        btnReback.setVisibility(View.VISIBLE);
    }

    @Override
    public void receiveJIngqFalied(Exception e) {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context, e.getMessage(), true);
    }

    @Override
    public void arriveConfirmSuccess() {
        dismissLoadingDialog();
        entityJingq
                .setState(EntityJingq.HADREACHCONFIRM);
        startActivity(JingqHandleActivity.createIntent(context, entityJingq));
        finish();
    }

    @Override
    public void arriveConfirmFailed(Exception e) {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context, e.getMessage(), true);
    }

    @Override
    public void rollbackJingqSuccess() {
        dismissLoadingDialog();

        ToastTool.getInstance().shortLength(context, "回退成功", true);
        entityJingq.setState(EntityJingq.UNREAD);
        btnAccept.setVisibility(View.VISIBLE);
        btnReback.setVisibility(View.GONE);
    }

    @Override
    public void rollbackJingqFailed(Exception e) {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context, e.getMessage(), true);
    }


    @OnClick({R.id.btn_reached_confirm, R.id.btn_accept, R.id.btn_reback})
    public void onClick(View view) {
        String jingqid = entityJingq.getJingqid();
        String jingyid = entityJingq.getJingyid();
        String carid = user.getCarid();
        String jh = user.getUserName();
        String simid = CommonUtil.getDeviceId(context);
        switch (view.getId()) {
            case R.id.btn_reached_confirm:
                String longitude = "120";
                String latitude = "31";
                jingqHandlePresenter.arriveConfirm(jingyid, jingqid, longitude, latitude, jh, simid);
                showLoadingDialog("到场确认中...");
                break;
            case R.id.btn_accept:
                jingqHandlePresenter.acceptJingq(jingqid, carid, jh, simid);
                showLoadingDialog("接警中...");
                break;
            case R.id.btn_reback:
                jingqHandlePresenter.rollbackJingq(jingqid, jh, simid);
                showLoadingDialog("回退中...");
                break;
        }
    }
}
