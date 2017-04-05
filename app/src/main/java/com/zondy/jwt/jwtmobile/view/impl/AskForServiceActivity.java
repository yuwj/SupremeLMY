package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 请求服务
 * Created by ywj on 2017/4/1 0001.
 */

public class AskForServiceActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.vpi_tab)
    TabPageIndicator vpiTab;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;

    List<Fragment> fragmentDatas;
    List<String> fragmentTitles;
    PagerAdapter pagerAdapter;

    public static Intent createIntent(Context context){
        Intent intent = new Intent(context,AskForServiceActivity.class);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_askforservice;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
        initOperator();

    }

    void initParam() {
        fragmentTitles = new ArrayList<>();
        fragmentTitles.add("请求布控");
        fragmentTitles.add("请求定位");
        fragmentTitles.add("请求增援");
        fragmentDatas = new ArrayList<>();
        fragmentDatas.add(new AskBukServiceFragment());
        fragmentDatas.add(new AskDingwServiceFragment());
        fragmentDatas.add(new AskZengyServiceFragment());




        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentDatas.get(position);
            }

            @Override
            public int getCount() {
                return fragmentDatas.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return fragmentTitles.get(position);
            }
        };

    }

    void initView() {
        initActionBar(toolbar,tvTitle,"请求服务");
        vpContainer.setAdapter(pagerAdapter);
        vpiTab.setViewPager(vpContainer);
    }

    void initOperator() {
    }
}
