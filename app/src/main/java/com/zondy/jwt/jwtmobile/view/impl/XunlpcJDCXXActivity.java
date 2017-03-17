package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityXunlpcJDCXX;

import butterknife.BindView;

/**
 * Created by sheep on 2017/3/14.
 */

public class XunlpcJDCXXActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_souyr)
    TextView tvSouyr;
    @BindView(R.id.tv_zhengjh)
    TextView tvZhengjh;
    @BindView(R.id.tv_haop)
    TextView tvHaop;
    @BindView(R.id.tv_pinpys)
    TextView tvPinpys;
    @BindView(R.id.tv_chelxh)
    TextView tvChelxh;
    @BindView(R.id.tv_chucrq)
    TextView tvChucrq;
    @BindView(R.id.tv_fadjh)
    TextView tvFadjh;
    @BindView(R.id.tv_shibm)
    TextView tvShibm;
    @BindView(R.id.btn_jdcxx_renyhc)
    Button btnJdcxxRenyhc;
    @BindView(R.id.tv_jidcwzjl)
    TextView tvJidcwzjl;
    private EntityXunlpcJDCXX entityXunlpcJDCXX;

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_xunlpc_jdcxx;
    }

    public static void actionStart(Context context, EntityXunlpcJDCXX entityXunlpcJDCXX) {
        Intent intent = new Intent(context, XunlpcJDCXXActivity.class);
        intent.putExtra("JDCXX", entityXunlpcJDCXX);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entityXunlpcJDCXX = (EntityXunlpcJDCXX) getIntent().getSerializableExtra("JDCXX");
        initParam();
        initView();
    }

    private void initParam() {

    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        btnJdcxxRenyhc.setOnClickListener(this);
        tvSouyr.setText(entityXunlpcJDCXX.getSuoyr());
        tvZhengjh.setText(entityXunlpcJDCXX.getZhengjh());
        tvHaop.setText(entityXunlpcJDCXX.getHaop());
        tvPinpys.setText(entityXunlpcJDCXX.getPinpys());
        tvChelxh.setText(entityXunlpcJDCXX.getChelxh());
        tvChucrq.setText(entityXunlpcJDCXX.getChucrq());
        tvFadjh.setText(entityXunlpcJDCXX.getFadjh());
        tvShibm.setText(entityXunlpcJDCXX.getShibm());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_jdcxx_renyhc:
                XunlpcRYHCRYXXActivity.actionStart(this,entityXunlpcJDCXX);
                break;
        }
    }
}
