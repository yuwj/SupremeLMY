package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityGuijWithLvg;
import com.zondy.jwt.jwtmobile.entity.EntityXunlpcRYXX;
import com.zondy.jwt.jwtmobile.entity.EntityXunlpcRyxxWithXid;
import com.zondy.jwt.jwtmobile.ui.DividerItemDecoration;
import com.zondy.jwt.jwtmobile.ui.FullyLinearLayoutManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 巡逻盘查各类人员的详细信息
 * Created by sheep on 2017/1/13.
 */

public class PancPersonDetailActivity extends BaseActivity {


    @BindView(R.id.iv_contacts_item)
    ImageView ivContactsItem;
    @BindView(R.id.toolbar_contacts_item)
    Toolbar toolbarContactsItem;
    @BindView(R.id.rv_person_info)
    RecyclerView rvPersonInfo;
    CommonAdapter<EntityRenyProperty> renyPropertyCommonAdapter;
    List<EntityRenyProperty> renyPropertieDatas;
    EntityXunlpcRYXX renyxx;
    @BindView(R.id.collapsingtoolbarlayout)
    CollapsingToolbarLayout collapsingtoolbarlayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    String title;


    public static Intent createIntent(Context context, EntityXunlpcRYXX renyxx, String title) {
        Intent intent = new Intent(context, PancPersonDetailActivity.class);
        intent.putExtra("renyxx", renyxx);
        intent.putExtra("title", title);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_panc_person_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
        initOperator();
    }

    public void initParam() {
        Intent intent = getIntent();
        renyxx = (EntityXunlpcRYXX) intent.getSerializableExtra("renyxx");
        title = intent.getStringExtra("title");

        renyPropertieDatas = new ArrayList<>();
        List<EntityRenyProperty> propertiess = createPeopleProperties(renyxx);
        if (propertiess != null && propertiess.size() > 0) {
            renyPropertieDatas.addAll(propertiess);
        }
        renyPropertyCommonAdapter = new CommonAdapter<EntityRenyProperty>(context, R.layout.item_xunlpc_renyxx_detail, renyPropertieDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityRenyProperty entityRenyProperty, int position) {
                holder.setText(R.id.tv_name, entityRenyProperty.getName());
                holder.setText(R.id.tv_value, entityRenyProperty.getValue());
            }
        };

    }

    public void initView() {
        if (toolbarContactsItem != null) {
            setSupportActionBar(toolbarContactsItem);
        }
        collapsingtoolbarlayout.setTitle(title);
        rvPersonInfo.setLayoutManager(new FullyLinearLayoutManager(context));
        rvPersonInfo.setAdapter(renyPropertyCommonAdapter);
        rvPersonInfo.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));

    }

    public void initOperator() {
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public List<EntityRenyProperty> createPeopleProperties(EntityXunlpcRYXX ryxx) {
        List<EntityRenyProperty> properties = new ArrayList<>();
        EntityRenyProperty entityRenyProperty1 = new EntityRenyProperty("姓名", ryxx.getName());
        EntityRenyProperty entityRenyProperty2 = new EntityRenyProperty("地址", ryxx.getAddress());
        EntityRenyProperty entityRenyProperty3 = new EntityRenyProperty("性别", ryxx.getSex());
        properties.add(entityRenyProperty1);
        properties.add(entityRenyProperty2);
        properties.add(entityRenyProperty3);
        if(ryxx instanceof EntityXunlpcRyxxWithXid){//吸毒人员
            properties.add(new EntityRenyProperty("吸毒史","2017年3月20日11:17:38"));
        }
        return properties;
    }

    public class EntityRenyProperty {
        String name;
        String value;

        public EntityRenyProperty() {
        }

        public EntityRenyProperty(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
