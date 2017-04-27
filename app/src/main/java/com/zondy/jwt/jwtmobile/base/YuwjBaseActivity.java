package com.zondy.jwt.jwtmobile.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by ywj on 2017/4/13 0013.
 */

public abstract class YuwjBaseActivity extends BaseActivity {

    public abstract void initParam();
    public abstract void initView();
    public abstract void initOperator();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
        initOperator();
    }
}
