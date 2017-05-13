package com.zondy.jwt.jwtmobile.view.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseFragment;
import com.zondy.jwt.jwtmobile.entity.EntityAskBuk;
import com.zondy.jwt.jwtmobile.entity.EntityAskForBukcl;
import com.zondy.jwt.jwtmobile.entity.EntityAskForBukry;
import com.zondy.jwt.jwtmobile.entity.EntityJingq;
import com.zondy.jwt.jwtmobile.entity.EntityLocation;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.global.Constant;
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
    TextView tvChexValue;
    @BindView(R.id.tv_chep_value)
    TextView tvChepValue;
    @BindView(R.id.tv_chesys_value)
    TextView tvChesysValue;
    @BindView(R.id.et_car_feature_description)
    EditText etCarFeatureDescription;
    @BindView(R.id.ll_bukcl_container)
    LinearLayout llBukclContainer;
    @BindView(R.id.ll_buk)
    LinearLayout llBuk;


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
        addBukryView();
        addBukclView();

        ivAddBukry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBukryView();
            }
        });
        ivAddBukcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBukclView();
            }
        });
    }

    void initOperator() {

    }


    void addBukryView(){
        final View view = LayoutInflater.from(context).inflate(R.layout.content_askbuk_addbukry, llBukryContainer, false);
        llBukryContainer.addView(view);
        view.findViewById(R.id.iv_bukry_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llBukryContainer.removeView(view);
                llBukryContainer.requestLayout();
            }
        });
        final TextView tvHeightValue = (TextView) view.findViewById(R.id.tv_height_value);
        tvHeightValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popHeight(tvHeightValue);
            }
        });
        final TextView tvSexValue = (TextView) view.findViewById(R.id.tv_sex_value);
        tvSexValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popSex(tvSexValue);
            }
        }); final TextView tvAgeValue = (TextView) view.findViewById(R.id.tv_age_value);
        tvAgeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAge(tvSexValue);
            }
        });

    }


    void addBukclView(){
        final View view = LayoutInflater.from(context).inflate(R.layout.content_askbuk_addbukcl, llBukclContainer, false);
        llBukclContainer.addView(view);
        view.findViewById(R.id.iv_bukcl_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llBukclContainer.removeView(view);
                llBukclContainer.requestLayout();
            }
        });
        final TextView tv_chex_value = (TextView) view.findViewById(R.id.tv_chex_value);
        tv_chex_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popChex(tv_chex_value);
            }
        });
        final TextView tv_chep_value = (TextView) view.findViewById(R.id.tv_chep_value);
        tv_chep_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popChep(tv_chep_value);
            }
        });
        final TextView tv_chesys_value = (TextView) view.findViewById(R.id.tv_chesys_value);
        tv_chesys_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popChep(tv_chesys_value);
            }
        });


    }


    PopupWindow popHeight;
    public void popHeight(final TextView bindView){
        final EditText etMinHeight;
        final EditText etMaxHeight;
        Button btnConfirm;
        if(popHeight == null){
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            View popContentView = View.inflate(context,R.layout.content_askservice_input_height, null);
            etMinHeight = (EditText) popContentView.findViewById(R.id.et_min_height);
            etMaxHeight = (EditText) popContentView.findViewById(R.id.et_max_height);
            btnConfirm = (Button) popContentView.findViewById(R.id.btn_confirm);
            popHeight = new PopupWindow(popContentView, width, height);

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String minHeight = etMinHeight.getText().toString().trim();
                    String maxHeight = etMaxHeight.getText().toString().trim();
                    bindView.setText(minHeight+"~"+maxHeight);
                    popHeight.dismiss();
                }
            });
            popHeight.showAtLocation(llBuk, Gravity.CENTER_HORIZONTAL, 0, 0);
        }else{
            popHeight.showAtLocation(llBuk, Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    PopupWindow popAge;
    public void popAge(final TextView bindView){
        final EditText etMinAge;
        final EditText etMaxAge;
        Button btnConfirm;
        if(popAge == null){
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            View popContentView = View.inflate(context,R.layout.content_askservice_input_age, null);
            etMinAge = (EditText) popContentView.findViewById(R.id.et_min_age);
            etMaxAge = (EditText) popContentView.findViewById(R.id.et_max_age);
            btnConfirm = (Button) popContentView.findViewById(R.id.btn_confirm);
            popAge = new PopupWindow(popContentView, width, height);

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String minAge = etMinAge.getText().toString().trim();
                    String maxAge = etMaxAge.getText().toString().trim();
                    bindView.setText(minAge+"~"+maxAge);
                    popAge.dismiss();
                }
            });
            popAge.showAtLocation(llBuk, Gravity.CENTER_HORIZONTAL, 0, 0);
        }else{
            popAge.showAtLocation(llBuk, Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    PopupWindow popSex;
    public void popSex(final TextView bindView){
        RadioGroup radioGroup;
        Button btnConfirm;
        if(popSex == null){
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            final View popContentView = View.inflate(context,R.layout.content_askservice_input_height, null);
            radioGroup = (RadioGroup) popContentView.findViewById(R.id.rg_sex);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    //获取变更后的选中项的ID
                    int radioButtonId = group.getCheckedRadioButtonId();
                    bindView.setText(((RadioButton)popContentView.findViewById(radioButtonId)).getText());
                }
            });
            btnConfirm = (Button) popContentView.findViewById(R.id.btn_confirm);
            popSex = new PopupWindow(popContentView, width, height);

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popSex.dismiss();
                }
            });
            popSex.showAtLocation(llBuk, Gravity.CENTER_HORIZONTAL, 0, 0);
        }else{
            popSex.showAtLocation(llBuk, Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    PopupWindow popChex;
    public void popChex(final TextView bindView){
        RadioGroup radioGroup;
        Button btnConfirm;
        if(popChex == null){
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            final View popContentView = View.inflate(context,R.layout.content_askservice_radio_chex, null);
            radioGroup = (RadioGroup) popContentView.findViewById(R.id.rg_sex);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    //获取变更后的选中项的ID
                    int radioButtonId = group.getCheckedRadioButtonId();
                    bindView.setText(((RadioButton)popContentView.findViewById(radioButtonId)).getText());
                }
            });
            btnConfirm = (Button) popContentView.findViewById(R.id.btn_confirm);
            popChex = new PopupWindow(popContentView, width, height);

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popChex.dismiss();
                }
            });
            popChex.showAtLocation(llBuk, Gravity.CENTER_HORIZONTAL, 0, 0);
        }else{
            popChex.showAtLocation(llBuk, Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    PopupWindow popChesys;
    public void popChesys(final TextView bindView){
        RadioGroup radioGroup;
        Button btnConfirm;
        if(popChesys == null){
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            final View popContentView = View.inflate(context,R.layout.content_askservice_radio_chesys, null);
            radioGroup = (RadioGroup) popContentView.findViewById(R.id.rg_sex);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    //获取变更后的选中项的ID
                    int radioButtonId = group.getCheckedRadioButtonId();
                    bindView.setText(((RadioButton)popContentView.findViewById(radioButtonId)).getText());
                }
            });
            btnConfirm = (Button) popContentView.findViewById(R.id.btn_confirm);
            popChesys = new PopupWindow(popContentView, width, height);

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popChesys.dismiss();
                }
            });
            popChesys.showAtLocation(llBuk, Gravity.CENTER_HORIZONTAL, 0, 0);
        }else{
            popChesys.showAtLocation(llBuk, Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }


    PopupWindow popChep;
    public void popChep(final TextView bindView){
        final EditText et_chep;
        Button btnConfirm;
        if(popChep == null){
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            View popContentView = View.inflate(context,R.layout.content_askservice_input_chep, null);
            et_chep = (EditText) popContentView.findViewById(R.id.et_chep);
            btnConfirm = (Button) popContentView.findViewById(R.id.btn_confirm);
            popChep = new PopupWindow(popContentView, width, height);

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String chep = et_chep.getText().toString().trim();
                    bindView.setText(chep);
                    popChep.dismiss();
                }
            });
            popChep.showAtLocation(llBuk, Gravity.CENTER_HORIZONTAL, 0, 0);
        }else{
            popChep.showAtLocation(llBuk, Gravity.CENTER_HORIZONTAL, 0, 0);
        }
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
