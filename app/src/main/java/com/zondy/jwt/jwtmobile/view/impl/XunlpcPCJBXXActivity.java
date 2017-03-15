package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.util.ToastTool;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by sheep on 2017/3/15.
 */

public class XunlpcPCJBXXActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_pancsj)
    TextView tvPancsj;
    @BindView(R.id.tv_daibmj)
    TextView tvDaibmj;
    @BindView(R.id.tv_suitmj)
    TextView tvSuitmj;
    @BindView(R.id.iv_xunlpc_stmj)
    ImageView ivXunlpcStmj;
    @BindView(R.id.iv_xunlpc_xjrs_plus)
    ImageView ivXunlpcXjrsPlus;
    @BindView(R.id.et_xunlpc_xjrs)
    EditText etXunlpcXjrs;
    @BindView(R.id.iv_xunlpc_xjrs_minus)
    ImageView ivXunlpcXjrsMinus;
    @BindView(R.id.iv_xunlpc_pcdd)
    ImageView ivXunlpcPcdd;
    @BindView(R.id.tv_pancdd)
    TextView tvPancdd;
    @BindView(R.id.tv_xunlfs)
    TextView tvXunlfs;
    @BindView(R.id.tv_xunllx)
    TextView tvXunllx;
    @BindView(R.id.btn_xiayibu)
    Button btnXiayibu;

    private int xiejrs;
    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_xunlpc_pcjbxx;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, XunlpcPCJBXXActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
    }

    private void initParam() {
        xiejrs=Integer.valueOf(etXunlpcXjrs.getText().toString());
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentTime=sdf.format(date);
        tvPancsj.setText(currentTime);
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        btnXiayibu.setOnClickListener(this);
        ivXunlpcXjrsMinus.setOnClickListener(this);
        ivXunlpcXjrsPlus.setOnClickListener(this);
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
                XunlpcPCGLXXActivity.actionStart(this);
                break;
            case R.id.iv_xunlpc_xjrs_minus:
                if(xiejrs>1){
                    xiejrs=xiejrs-1;
                    etXunlpcXjrs.setText(""+xiejrs);
                }else {
                    ToastTool.getInstance().shortLength(XunlpcPCJBXXActivity.this,"协警人数不能少于1人！",true);
                }
                break;
            case R.id.iv_xunlpc_xjrs_plus:
                xiejrs=xiejrs+1;
                etXunlpcXjrs.setText(""+xiejrs);
                break;
        }
    }
}
