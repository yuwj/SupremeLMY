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
import com.zondy.jwt.jwtmobile.entity.EntityXunlpcRYXX;

import butterknife.BindView;

/**
 * Created by sheep on 2017/3/6.
 */

public class XunlpcRYXXActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_nation)
    TextView tvNation;
    @BindView(R.id.tv_sfzh)
    TextView tvSfzh;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_csrq)
    TextView tvCsrq;
    @BindView(R.id.tv_huji)
    TextView tvHuji;
    @BindView(R.id.tv_jiguan)
    TextView tvJiguan;
    @BindView(R.id.tv_xueli)
    TextView tvXueli;
    @BindView(R.id.tv_hunyin)
    TextView tvHunyin;
    @BindView(R.id.btn_xuanzcl)
    Button btnXuanzcl;
    @BindView(R.id.btn_zhengcfx)
    Button btnZhengcfx;
    private EntityXunlpcRYXX entityXunlpcRYXX;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_xunlpc_ryxx;
    }

    public static void actionStart(Context context, EntityXunlpcRYXX entityXunlpcRYXX) {
        Intent intent = new Intent(context, XunlpcRYXXActivity.class);
        intent.putExtra("RYXX", entityXunlpcRYXX);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entityXunlpcRYXX = (EntityXunlpcRYXX) getIntent().getSerializableExtra("RYXX");
        initParam();
        initView();
    }

    private void initParam() {

    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        btnXuanzcl.setOnClickListener(this);
        btnZhengcfx.setOnClickListener(this);
        tvName.setText(entityXunlpcRYXX.getName());
        tvNation.setText(entityXunlpcRYXX.getNation());
        tvSex.setText(entityXunlpcRYXX.getSex());
        tvSfzh.setText(entityXunlpcRYXX.getSfzh());
        tvAddress.setText(entityXunlpcRYXX.getAddress());
        tvCsrq.setText(entityXunlpcRYXX.getBirthday());
        tvHuji.setText(entityXunlpcRYXX.getHuji());
        tvJiguan.setText(entityXunlpcRYXX.getJiguan());
        tvXueli.setText(entityXunlpcRYXX.getXueli());
        tvHunyin.setText(entityXunlpcRYXX.getHunyin());
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
            case R.id.btn_xuanzcl:
                XunlpcPCJBXXActivity.actionStart(XunlpcRYXXActivity.this);
                break;
            case R.id.btn_zhengcfx:
                finish();
                break;
        }
    }
}
