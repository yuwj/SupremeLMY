package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityJingq;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.IJingqHandlePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.JingqHandlePresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IJingqDetailWithUnhandleView;
import com.zondy.mapgis.android.mapview.MapView;

import java.io.File;

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
        com.zondy.mapgis.android.environment.Environment.requestAuthorization(this, new com.zondy.mapgis.android.environment.Environment.AuthorizeCallback() {
            @Override
            public void onComplete() {
                initMap();
            }
        });
        updateJingqView(entityJingq);
    }

    private void initMap() {
        mapview.loadFromFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MapGIS/map/wuhan/wuhan.xml");
        mapview.setZoomControlsEnabled(false);
        mapview.setShowLogo(false);
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
                showLoadingDialog();
                break;
            case R.id.btn_accept:
                jingqHandlePresenter.acceptJingq(jingqid, carid, jh, simid);
                showLoadingDialog();
                break;
            case R.id.btn_reback:
                jingqHandlePresenter.rollbackJingq(jingqid, jh, simid);
                showLoadingDialog();
                break;
        }
    }
}
