package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;

/**
 * Created by ywj on 2017/4/2 0002.
 */

public class MaterialDesignTest extends BaseActivity{
    public static Intent createIntent(Context context){
        Intent intent = new Intent(context,MaterialDesignTest.class);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_materaildesign_test;
    }

    void s(){
        android.support.design.widget.AppBarLayout s;
        CoordinatorLayout.Behavior a;
    }
}
