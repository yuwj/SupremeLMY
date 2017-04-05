package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by sheep on 2017/3/28.
 */

public class ShujcjActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_shujcj_jcsj)
    LinearLayout llShujcjJcsj;
    @BindView(R.id.ll_shujcj_fwxx)
    LinearLayout llShujcjFwxx;
    @BindView(R.id.ll_shujcj_rfxx)
    LinearLayout llShujcjRfxx;

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_shujcj;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShujcjActivity.class);
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
        llShujcjJcsj.setOnClickListener(this);
        llShujcjFwxx.setOnClickListener(this);
        llShujcjRfxx.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_shujcj_jcsj:
                ShujcjJCSJCJActivity.actionStart(ShujcjActivity.this);
                break;
            case R.id.ll_shujcj_fwxx:
                ShujcjFWXXCJActivity.actionStart(ShujcjActivity.this);
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
