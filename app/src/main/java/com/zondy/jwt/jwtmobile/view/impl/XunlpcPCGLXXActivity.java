package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by sheep on 2017/3/15.
 */

public class XunlpcPCGLXXActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_panccl)
    TextView tvPanccl;
    @BindView(R.id.iv_xunlpc_pccl)
    ImageView ivXunlpcPccl;
    @BindView(R.id.tv_tongxry)
    TextView tvTongxry;
    @BindView(R.id.iv_xunlpc_txry)
    ImageView ivXunlpcTxry;
    @BindView(R.id.tv_pancwp)
    TextView tvPancwp;
    @BindView(R.id.iv_xunlpc_pcwp)
    ImageView ivXunlpcPcwp;
    @BindView(R.id.btn_xiayibu)
    Button btnXiayibu;

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_xunlpc_pcglxx;
    }
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, XunlpcPCGLXXActivity.class);
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
        btnXiayibu.setOnClickListener(this);
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
            case R.id.btn_xiayibu:
                XunlpcPCFKXXActivity.actionStart(this);
                break;
        }

    }
}
