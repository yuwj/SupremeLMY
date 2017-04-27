package com.zondy.jwt.jwtmobile.view.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseFragment;
import com.zondy.jwt.jwtmobile.entity.EntityAskChax;
import com.zondy.jwt.jwtmobile.entity.EntityJingq;
import com.zondy.jwt.jwtmobile.entity.EntityLocation;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.IAskServicePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.AskServicePresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IAskServiceView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywj on 2017/4/1 0001.
 */

public class AskChaxFragment extends BaseFragment implements IAskServiceView {


    EntityJingq entityJingq;
    EntityUser userInfo;
    IAskServicePresenter askServicePresenter;
    EntityAskChax entityAskChax;
    @BindView(R.id.et_chax_neir)
    EditText etChaxNeir;
    @BindView(R.id.et_chax_yuany)
    EditText etChaxYuany;


    public static Fragment createInstance(EntityJingq entityJingq) {
        BaseFragment fragment = new AskChaxFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("entityJingq", entityJingq);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_chax, container, false);
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
        entityAskChax = new EntityAskChax();
    }

    void initView() {
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
        String jh = userInfo.getUserName();
        String zzjgdm = userInfo.getZzjgdm();
        entityAskChax.setZzjgdm(zzjgdm);
        entityAskChax.setJh(jh);
        entityAskChax.setJqbh(entityJingq.getJingqid());
        EntityLocation entityLocation = SharedTool.getInstance().getLocationInfo(context);
        entityAskChax.setX(entityLocation.getLongitude() + "");
        entityAskChax.setX(entityLocation.getLatitude() + "");
        entityAskChax.setCxnr(etChaxNeir.getText().toString().trim());
        entityAskChax.setCxyy(etChaxYuany.getText().toString().trim());
        String simid = CommonUtil.getDeviceId(context);
        askServicePresenter.askChax(jh, simid, entityAskChax);
    }

    public void setEntityJingq(EntityJingq entityJingq) {
        this.entityJingq = entityJingq;
    }
}
