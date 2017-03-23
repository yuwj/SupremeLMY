package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;
import com.zondy.jwt.jwtmobile.view.INoticeDetailView;

import butterknife.BindView;

public class NoticeDetailActivity extends BaseActivity implements INoticeDetailView {

    EntityNotice entityNotice;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.sv_notice_container)
    ScrollView svNoticeContainer;

    public static Intent createIntent(Context context, EntityNotice entityNotice) {
        Intent intent = new Intent(context, NoticeDetailActivity.class);
        intent.putExtra("entityNotice", entityNotice);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_notice_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
        initOperator();
    }

    public void initParam() {
        Intent intent = getIntent();
        entityNotice = (EntityNotice) intent.getSerializableExtra("entityNotice");
    }

    public void initView() {
        initActionBar(toolbar,tvTitle,entityNotice.getTitle());
        tvTime.setText(entityNotice.getTime());
        tvContent.setText(entityNotice.getContent());
    }

    public void initOperator() {

    }

    @Override
    public void queryNoticeDetailSuccess(EntityNotice entityNotice) {

    }

    @Override
    public void queryNoticeDetailFail(Exception e) {

    }
}
