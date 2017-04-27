package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.YuwjBaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityAskBuk;
import com.zondy.jwt.jwtmobile.entity.EntityAskBukfk;
import com.zondy.jwt.jwtmobile.entity.EntityJingq;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.IAskServicePresenter;
import com.zondy.jwt.jwtmobile.presenter.IJingqHandlePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.AskServicePresenterImpl;
import com.zondy.jwt.jwtmobile.presenter.impl.JingqHandlePresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IAskBukDetailView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywj on 2017/4/13 0013.
 */

public class AskBukDetailActivity extends YuwjBaseActivity implements IAskBukDetailView {

    @BindView(R.id.tv_detail)
    TextView tvDetail;
    String bukid;
    String jingqid;
    EntityAskBuk entityAskBuk;
    EntityJingq entityJingq;
    EntityUser userInfo;
    IAskServicePresenter askServicePresenter;
    IJingqHandlePresenter jingqHandlePresenter;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_relate_jingq)
    TextView tvRelateJingq;
    @BindView(R.id.tv_askbuk_detail)
    TextView tvAskbukDetail;
    @BindView(R.id.tv_askbukfk_detail)
    TextView tvAskbukfkDetail;

    public static Intent createIntent(Context context, String bukID, String jingqid) {
        Intent intent = new Intent(context,AskBukDetailActivity.class);
        intent.putExtra("bukid", bukID);
        intent.putExtra("jingqid", jingqid);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_asked_buk_detail;
    }

    @Override
    public void initParam() {
        Intent intent = getIntent();
        bukid = intent.getStringExtra("bukid");
        jingqid = intent.getStringExtra("jingqid");
        askServicePresenter = new AskServicePresenterImpl(this);
        jingqHandlePresenter = new JingqHandlePresenterImpl(this);
    }

    @Override
    public void initView() {
        String title = "请求布控详情";
        initActionBar(toolbar, tvTitle, title);
    }

    @Override
    public void initOperator() {
        queryData();
    }

    public void queryData() {
        String jh = userInfo.getUserName();
        String simid = CommonUtil.getDeviceId(context);
        askServicePresenter.queryAskBukDetail(jh, simid, bukid);
        jingqHandlePresenter.queryJingqWithAskBukDetail(jingqid, jh, simid);
    }

    public void updateAskBukDetail(EntityAskBuk entityAskBuk) {
        if (entityAskBuk != null) {
            tvDetail.setText(entityAskBuk.toJsonStr());
        }

    }

    public void udpateJingq(EntityJingq entityJingq) {
        if (entityJingq != null) {

            tvRelateJingq.setText(entityJingq.toJsonStr());
        }
    }

    @Override
    public void onQueryAskBukDetailSuccess(EntityAskBuk entityAskBuk) {
        ToastTool.getInstance().shortLength(context, "query bukong detail success", true);
        if (entityAskBuk != null) {

            this.entityAskBuk = entityAskBuk;
            updateAskBukDetail(this.entityAskBuk);
        }
    }

    @Override
    public void onQueryAskBukDetailFailed(Exception exception) {
        ToastTool.getInstance().shortLength(context, exception.getMessage(), true);

    }


    @Override
    public void onQueryJingqSuccess(EntityJingq entityJingq) {

        if (entityJingq != null) {
            this.entityJingq = entityJingq;
            udpateJingq(this.entityJingq);
        }
    }

    @Override
    public void onQueryJingqFail(Exception e) {

        ToastTool.getInstance().shortLength(context, e.getMessage(), true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
