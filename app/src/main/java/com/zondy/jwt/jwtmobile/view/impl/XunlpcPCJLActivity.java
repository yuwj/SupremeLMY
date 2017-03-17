package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityPANCJL;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sheep on 2017/3/16.
 */

public class XunlpcPCJLActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_xunlpc_pancjl)
    XRecyclerView rvXunlpcPancjl;
    private CommonAdapter<EntityPANCJL> pancjlAdapter;
    private List<EntityPANCJL> pancjlDatas;

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_xunlpc_pcjl;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, XunlpcPCJLActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
    }

    private void initParam() {
        pancjlDatas = new ArrayList<>();
        EntityPANCJL entityPANCJL = new EntityPANCJL();
        entityPANCJL.setPclx("1");
        entityPANCJL.setPczt("2");
        entityPANCJL.setXm("喻文杰");
        entityPANCJL.setXb("男");
        entityPANCJL.setMz("汉");
        entityPANCJL.setSfzh("420206199204277726");
        entityPANCJL.setDz("关山大道598号中地数码科技");
        pancjlDatas.add(entityPANCJL);
        EntityPANCJL entityPANCJL1 = new EntityPANCJL();
        entityPANCJL1.setPclx("2");
        entityPANCJL1.setPczt("1");
        entityPANCJL1.setCheph("鄂AFF458");
        entityPANCJL1.setChepp("法拉利458");
        entityPANCJL1.setChesyr("阳神");
        entityPANCJL1.setChesyrhm("18677364472");
        pancjlDatas.add(entityPANCJL1);
        pancjlDatas.add(entityPANCJL);

        pancjlDatas.add(entityPANCJL1);
        pancjlDatas.add(entityPANCJL1);
        pancjlDatas.add(entityPANCJL);
        pancjlDatas.add(entityPANCJL);

        pancjlDatas.add(entityPANCJL1);
        pancjlDatas.add(entityPANCJL1);
        pancjlDatas.add(entityPANCJL);

        pancjlAdapter = new CommonAdapter<EntityPANCJL>(this, R.layout.item_xunlpc_pancjl, pancjlDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityPANCJL entityPANCJL, int position) {
                View viewChe = holder.getView(R.id.rl_pancjl_che);
                View viewRen = holder.getView(R.id.rl_pancjl_ren);
                if (Integer.valueOf(entityPANCJL.getPclx()) == 1) {
                    viewChe.setVisibility(View.GONE);
                    viewRen.setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_pancjl_xm, entityPANCJL.getXm());
                    holder.setText(R.id.tv_pancjl_xb, entityPANCJL.getXb());
                    holder.setText(R.id.tv_pancjl_mz, entityPANCJL.getMz());
                    holder.setText(R.id.tv_pancjl_sfzh, entityPANCJL.getSfzh());
                    holder.setText(R.id.tv_pancjl_dz, entityPANCJL.getDz());
                    if (Integer.valueOf(entityPANCJL.getPczt()) == 2) {
                        holder.setImageDrawable(R.id.iv_pancjl_ren, getDrawable(R.drawable.ic_xunlpcpcjl_person_xianyi));
                    } else if (Integer.valueOf(entityPANCJL.getPczt()) == 1) {
                        holder.setImageDrawable(R.id.iv_pancjl_ren, getDrawable(R.drawable.ic_xunlpcpcjl_person));
                    }

                } else if (Integer.valueOf(entityPANCJL.getPclx()) == 2) {
                    viewChe.setVisibility(View.VISIBLE);
                    viewRen.setVisibility(View.GONE);
                    holder.setText(R.id.tv_pancjl_chep, entityPANCJL.getCheph());
                    holder.setText(R.id.tv_pancjl_pinp, entityPANCJL.getChepp());
                    holder.setText(R.id.tv_pancjl_suoyr, entityPANCJL.getChesyr());
                    holder.setText(R.id.tv_pancjl_haom, entityPANCJL.getChesyrhm());
                    if (Integer.valueOf(entityPANCJL.getPczt()) == 2) {
                        holder.setImageDrawable(R.id.iv_pancjl_che, getDrawable(R.drawable.ic_xunlpcpcjl_person_xianyi));
                    } else if (Integer.valueOf(entityPANCJL.getPczt()) == 1) {
                        holder.setImageDrawable(R.id.iv_pancjl_che, getDrawable(R.drawable.ic_xunlpcpcjl_car));
                    }
                }
            }
        };
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvXunlpcPancjl.setLayoutManager(linearLayoutManager);
        rvXunlpcPancjl.setRefreshProgressStyle(ProgressStyle.Pacman);
        rvXunlpcPancjl.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        rvXunlpcPancjl.setPullRefreshEnabled(false);
        rvXunlpcPancjl.setLoadingMoreEnabled(true);
        rvXunlpcPancjl.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvXunlpcPancjl.loadMoreComplete();
                    }
                }, 2000);
            }
        });
        rvXunlpcPancjl.setAdapter(pancjlAdapter);
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

    }
}
