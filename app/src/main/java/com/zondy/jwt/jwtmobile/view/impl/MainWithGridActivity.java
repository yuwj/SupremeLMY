package com.zondy.jwt.jwtmobile.view.impl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;

public class MainWithGridActivity extends BaseActivity {

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_main_with_grid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
        initOperator();
    }

    void initParam(){

    }
    void initView(){}
    void initOperator(){}

}
