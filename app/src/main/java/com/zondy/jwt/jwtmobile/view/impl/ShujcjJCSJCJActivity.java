package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by sheep on 2017/3/28.
 */

public class ShujcjJCSJCJActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_jcsjcj_wbtc)
    LinearLayout llJcsjcjWbtc;
    @BindView(R.id.ll_jcsjcj_lgtc)
    LinearLayout llJcsjcjLgtc;
    @BindView(R.id.ll_jcsjcj_jdtc)
    LinearLayout llJcsjcjJdtc;
    @BindView(R.id.ll_jcsjcj_yhtc)
    ImageView llJcsjcjYhtc;
    @BindView(R.id.ll_jcsjcj_yytc)
    LinearLayout llJcsjcjYytc;
    @BindView(R.id.ll_jcsjcj_zddwtc)
    LinearLayout llJcsjcjZddwtc;

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_shujcj_jcsjcj;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShujcjJCSJCJActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        initViews();
    }

    private void initParams() {

    }

    private void initViews() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        llJcsjcjWbtc.setOnClickListener(this);
        llJcsjcjLgtc.setOnClickListener(this);
        llJcsjcjJdtc.setOnClickListener(this);
        llJcsjcjYhtc.setOnClickListener(this);
        llJcsjcjYytc.setOnClickListener(this);
        llJcsjcjZddwtc.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_jcsjcj_wbtc:
                ShujcjWBTCCJActivity.actionStart(ShujcjJCSJCJActivity.this);
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
