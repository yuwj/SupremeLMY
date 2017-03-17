package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityBaseGuij;
import com.zondy.jwt.jwtmobile.entity.EntityXunlpcRYXX;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sheep on 2017/3/6.
 */

public class XunlpcRYXXActivity extends BaseActivity {
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
    @BindView(R.id.tv_wangbsw)
    TextView tvWangbsw;
    @BindView(R.id.tv_lvgzs)
    TextView tvLvgzs;
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

    @OnClick({R.id.tv_wangbsw, R.id.tv_lvgzs})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_wangbsw:
                startActivity(GuijMapWithLvgActivity.createIntent(context, createGuijPathDatas(2),"上网轨迹",2));
                break;
            case R.id.tv_lvgzs:
                startActivity(GuijMapWithLvgActivity.createIntent(context, createGuijPathDatas(1),"住宿轨迹",1));
                break;
        }
    }

    public List<EntityBaseGuij> createGuijPathDatas(int type){
        //1-旅馆,2-网吧
        List<EntityBaseGuij> pathDatas = new ArrayList<>();
        for(int i = 0;i<10;i++){
            EntityBaseGuij entitySearchResult = new EntityBaseGuij();
            switch (type){
                case 1:

                    entitySearchResult.setPositionName("旅馆"+i);
                    break;
                case 2:

                    entitySearchResult.setPositionName("网吧"+i);
                    break;
            }
            entitySearchResult.setAddress("地址"+i);
            entitySearchResult.setLongitude(114.40758042680389+i*0.1);
            entitySearchResult.setLatitude(30.493347107757284+i*0.1);
            entitySearchResult.setStartTime((2010+i)+"-01-02 13:35:33");
            entitySearchResult.setStartTime((2010+i)+"-03-02 13:35:33");
            pathDatas.add(entitySearchResult);
        }
        return  pathDatas;
    }
}
