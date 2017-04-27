package com.zondy.jwt.jwtmobile.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.zondy.jwt.jwtmobile.entity.EntityJingq;

/**
 * Created by ywj on 2017/4/13 0013.
 */

public class BaseFragment extends Fragment{
    public Context context;

    public static Fragment createInstance(){
        BaseFragment fragment = new BaseFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }
}
