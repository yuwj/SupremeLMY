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

public class AskBukFragment extends BaseFragment implements IAskServiceView {


    EntityJingq entityJingq;
    EntityUser userInfo;
    IAskServicePresenter askServicePresenter;
    EntityAskBuk entityAskBuk;
    @BindView(R.id.iv_add_bukry)
    ImageView ivAddBukry;
    @BindView(R.id.iv_bukry_delete)
    ImageView ivBukryDelete;
    @BindView(R.id.tv_height_value)
    TextView tvHeightValue;
    @BindView(R.id.tv_sex_value)
    TextView tvSexValue;
    @BindView(R.id.tv_age_value)
    TextView tvAgeValue;
    @BindView(R.id.et_person_feature_description)
    EditText etPersonFeatureDescription;
    @BindView(R.id.ll_bukry_container)
    LinearLayout llBukryContainer;
    @BindView(R.id.iv_add_bukcl)
    ImageView ivAddBukcl;
    @BindView(R.id.iv_bukcl_delete)
    ImageView ivBukclDelete;
    @BindView(R.id.tv_chex_value)
    TextView tvChexValue;
    @BindView(R.id.tv_chep_value)
    TextView tvChepValue;
    @BindView(R.id.tv_chesys_value)
    TextView tvChesysValue;
    @BindView(R.id.et_car_feature_description)
    EditText etCarFeatureDescription;
    @BindView(R.id.ll_bukcl_container)
    LinearLayout llBukclContainer;


    public static Fragment createInstance(EntityJingq entityJingq) {
        BaseFragment fragment = new AskBukFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("entityJingq", entityJingq);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_buk, container, false);
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
        entityAskBuk = new EntityAskBuk();

    }

    void initView() {

        ivAddBukry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(context).inflate(R.layout.content_askbuk_addbukry, llBukryContainer, true);
                view.findViewById(R.id.iv_bukry_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llBukryContainer.removeView(view);
                        llBukryContainer.requestLayout();
                    }
                });
            }
        });
        ivAddBukcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(context).inflate(R.layout.content_askbuk_addbukcl, llBukclContainer, true);


                view.findViewById(R.id.iv_bukcl_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llBukryContainer.removeView(view);
                        llBukryContainer.requestLayout();
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
        List<EntityAskForBukry> bukrys = new ArrayList<>();
        for (int i = 0; i < llBukryContainer.getChildCount(); i++) {
            EntityAskForBukry bukry = new EntityAskForBukry();
            View view = llBukryContainer.getChildAt(i);
            TextView tvHeightValue = (TextView) view.findViewById(R.id.tv_height_value);
            String heightValues[] = tvHeightValue.getText().toString().trim().split("~");
            if (heightValues != null && heightValues.length >= 2) {
                bukry.setMinheight(heightValues[0]);
                bukry.setMaxheight(heightValues[1]);
            }
            TextView tvSexValue = (TextView) view.findViewById(R.id.tv_sex_value);
            bukry.setSex(tvSexValue.getText().toString().trim());
            TextView tvAgeValue = (TextView) view.findViewById(R.id.tv_age_value);
            String ageValues[] = tvAgeValue.getText().toString().trim().split("~");
            if (ageValues != null && ageValues.length >= 2) {
                bukry.setMinage(ageValues[0]);
                bukry.setMaxage(ageValues[1]);
            }
            EditText etFeatureDescription = (EditText) view.findViewById(R.id.et_person_feature_description);
            bukry.setDescription(etFeatureDescription.getText().toString());
            bukrys.add(bukry);
        }
        entityAskBuk.setBukryList(bukrys);


        List<EntityAskForBukcl> bukcls = new ArrayList<>();
        for (int i = 0; i < llBukclContainer.getChildCount(); i++) {
            View view = llBukclContainer.getChildAt(i);

            TextView tvChexValue = (TextView) view.findViewById(R.id.tv_chex_value);
            TextView tvChepValue = (TextView) view.findViewById(R.id.tv_chep_value);
            TextView tvChesyxValue = (TextView) view.findViewById(R.id.tv_chesys_value);

            EditText etFeatureDescription = (EditText) view.findViewById(R.id.et_car_feature_description);

            EntityAskForBukcl bukcl = new EntityAskForBukcl();
            bukcl.setDescription(etFeatureDescription.getText().toString().trim());
            bukcl.setCx(tvChexValue.getText().toString());
            bukcl.setCsys(tvChesyxValue.getText().toString());
            bukcl.setCp(tvChepValue.getText().toString());
            bukcl.setDescription(etFeatureDescription.getText().toString());
            bukcls.add(bukcl);
        }
        entityAskBuk.setBukclList(bukcls);
        String jh = userInfo.getUserName();
        String zzjgdm = userInfo.getZzjgdm();
        entityAskBuk.setZzjgdm(zzjgdm);
        entityAskBuk.setJh(jh);
        entityAskBuk.setJqbh(entityJingq.getJingqid());
        EntityLocation entityLocation = SharedTool.getInstance().getLocationInfo(context);
        entityAskBuk.setX(entityLocation.getLongitude() + "");
        entityAskBuk.setX(entityLocation.getLatitude() + "");
        String simid = CommonUtil.getDeviceId(context);
        askServicePresenter.askBuk(jh, simid, entityAskBuk);
    }

    public void setEntityJingq(EntityJingq entityJingq) {
        this.entityJingq = entityJingq;
    }
}
