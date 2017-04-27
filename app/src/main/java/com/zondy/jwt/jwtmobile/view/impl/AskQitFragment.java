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
import com.zondy.jwt.jwtmobile.entity.EntityAskQit;
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

public class AskQitFragment extends BaseFragment implements IAskServiceView {


    EntityJingq entityJingq;
    EntityUser userInfo;
    IAskServicePresenter askServicePresenter;
    EntityAskQit entityAskQit;
    @BindView(R.id.et_description)
    EditText etDescription;


    public static Fragment createInstance(EntityJingq entityJingq) {
        BaseFragment fragment = new AskQitFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("entityJingq", entityJingq);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_qit, container, false);
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
        entityAskQit = new EntityAskQit();
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
        entityAskQit.setZzjgdm(zzjgdm);
        entityAskQit.setJh(jh);
        entityAskQit.setJqbh(entityJingq.getJingqid());
        EntityLocation entityLocation = SharedTool.getInstance().getLocationInfo(context);
        entityAskQit.setX(entityLocation.getLongitude() + "");
        entityAskQit.setX(entityLocation.getLatitude() + "");
        entityAskQit.setDescription(etDescription.getText().toString().trim());
        String simid = CommonUtil.getDeviceId(context);
        askServicePresenter.askQit(jh, simid, entityAskQit);
    }

    public void setEntityJingq(EntityJingq entityJingq) {
        this.entityJingq = entityJingq;
    }
}
