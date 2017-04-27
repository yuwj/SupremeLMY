package com.zondy.jwt.jwtmobile.view.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseFragment;
import com.zondy.jwt.jwtmobile.entity.EntityAskBuk;
import com.zondy.jwt.jwtmobile.entity.EntityAskForBukcl;
import com.zondy.jwt.jwtmobile.entity.EntityAskForBukry;
import com.zondy.jwt.jwtmobile.entity.EntityAskZous;
import com.zondy.jwt.jwtmobile.entity.EntityAskZousry;
import com.zondy.jwt.jwtmobile.entity.EntityJingq;
import com.zondy.jwt.jwtmobile.entity.EntityLocation;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.IAskServicePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.AskServicePresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IAskServiceView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywj on 2017/4/1 0001.
 */

public class AskZousFragment extends BaseFragment implements IAskServiceView {


    EntityJingq entityJingq;
    EntityUser userInfo;
    IAskServicePresenter askServicePresenter;
    EntityAskZous entityAskZous;
    @BindView(R.id.iv_add_zousry)
    ImageView ivAddZousry;
    @BindView(R.id.iv_zousry_delete)
    ImageView ivBukryDelete;
    @BindView(R.id.et_person_name)
    EditText etPersonName;
    @BindView(R.id.tv_person_miss_time)
    TextView tvPersonMissTime;
    @BindView(R.id.tv_height_value)
    TextView tvHeightValue;
    @BindView(R.id.tv_sex_value)
    TextView tvSexValue;
    @BindView(R.id.tv_age_value)
    TextView tvAgeValue;
    @BindView(R.id.et_person_feature_description)
    EditText etPersonFeatureDescription;
    @BindView(R.id.ll_zousry_container)
    LinearLayout llZousryContainer;


    public static Fragment createInstance(EntityJingq entityJingq) {
        BaseFragment fragment = new AskZousFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("entityJingq", entityJingq);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_zous, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initParam();
        initView();
        initOperator();
    }

    void initParam() {
        Bundle bundle = getArguments();
        entityJingq = (EntityJingq) bundle.getSerializable("entityJingq");
        userInfo = SharedTool.getInstance().getUserInfo(context);
        askServicePresenter = new AskServicePresenterImpl(this);
        entityAskZous = new EntityAskZous();

    }

    void initView() {

        ivAddZousry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(context).inflate(R.layout.content_askzous_addzousry, llZousryContainer, true);
                view.findViewById(R.id.iv_zousry_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llZousryContainer.removeView(view);
                        llZousryContainer.requestLayout();
                    }
                });
            }
        });
    }

    void initOperator() {

    }

    @Override
    public void onAskSuccess() {
        getActivity().finish();
    }

    @Override
    public void onAskFail(Exception exception) {
        ToastTool.getInstance().shortLength(context, exception.getMessage(), true);
    }

    public void launchAsk() {
        List<EntityAskZousry> zousrys = new ArrayList<>();
        for (int i = 0; i < llZousryContainer.getChildCount(); i++) {
            EntityAskZousry zousry = new EntityAskZousry();
            View view = llZousryContainer.getChildAt(i);
            TextView tvHeightValue = (TextView) view.findViewById(R.id.tv_height_value);
            String heightValue = tvHeightValue.getText().toString().trim();
            zousry.setHeight(heightValue);
            TextView tvSexValue = (TextView) view.findViewById(R.id.tv_sex_value);
            zousry.setSex(tvSexValue.getText().toString().trim());
            TextView tvAgeValue = (TextView) view.findViewById(R.id.tv_age_value);
            String ageValue = tvAgeValue.getText().toString().trim();
            zousry.setAge(ageValue);
            EditText etFeatureDescription = (EditText) view.findViewById(R.id.et_person_feature_description);
            zousry.setDescription(etFeatureDescription.getText().toString());
            zousrys.add(zousry);
        }
        entityAskZous.setZousryList(zousrys);
        String jh = userInfo.getUserName();
        String zzjgdm = userInfo.getZzjgdm();
        entityAskZous.setZzjgdm(zzjgdm);
        entityAskZous.setJh(jh);
        entityAskZous.setJqbh(entityJingq.getJingqid());
        EntityLocation entityLocation = SharedTool.getInstance().getLocationInfo(context);
        entityAskZous.setX(entityLocation.getLongitude() + "");
        entityAskZous.setX(entityLocation.getLatitude() + "");
        String simid = CommonUtil.getDeviceId(context);
        askServicePresenter.askZous(jh, simid, entityAskZous);
    }

    public void setEntityJingq(EntityJingq entityJingq) {
        this.entityJingq = entityJingq;
    }
}
