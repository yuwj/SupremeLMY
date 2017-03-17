package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityBaseResponse;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.ISettingPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.SettingPresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.ISettingAccountUpdatePasswordView;

import butterknife.BindView;

public class SettingAccountUpdatePwdActivity extends BaseActivity implements ISettingAccountUpdatePasswordView, View.OnClickListener {
    ISettingPresenter settingPresenter = new SettingPresenterImpl(this);
    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_new_pwd2)
    EditText etNewPwd2;
    @BindView(R.id.btn_change_pwd)
    Button btnChangePwd;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_setting_account_update_pwd;
    }


    @Override
    public void updatePwd(EntityBaseResponse resp) {
        switch (resp.getResult()) {
            case 1:
                ToastTool.getInstance().shortLength(context, resp.getMessage(), true);
                this.finish();
                break;
            case 0:
                ToastTool.getInstance().shortLength(context, resp.getMessage(), true);
                break;
            case 2:
                ToastTool.getInstance().shortLength(context, resp.getMessage(), true);
                break;
        }
    }


    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, SettingAccountUpdatePwdActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        initParam();
        initView();
        initOperator();
    }

    public void initParam() {
    }

    public void initView() {

        btnChangePwd.setOnClickListener(this);

        initActionBar(toolbar, tvTitle, "修改密码");

    }

    public void initOperator() {
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_change_pwd:
                String oldPwd = etOldPwd.getText().toString();
                final String newPwd = etNewPwd.getText().toString();
                String newPwd2 = etNewPwd2.getText().toString();
                if (TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd)
                        || TextUtils.isEmpty(newPwd2)) {
                    ToastTool.getInstance().shortLength(context, "请输入旧密码和两次新密码",
                            true);
                } else {
                    if (!newPwd.equals(newPwd2)) {
                        ToastTool.getInstance().shortLength(context,
                                "两次输入的新密码不统一，请重新输入", true);
                    } else if (!oldPwd.equals(SharedTool.getInstance()
                            .getUserInfo(context).getPassword())) {
                        ToastTool.getInstance().shortLength(context, "旧密码错误，请重新输入",
                                true);
                    } else {

                        EntityUser userInfo = SharedTool.getInstance().getUserInfo(
                                context);
                        String jh = userInfo.getUserName();
                        String simid = CommonUtil.getDeviceId(context);
                        settingPresenter.updatePassword(userInfo.getUserName(), oldPwd, newPwd, jh, simid);

                    }
                }
                break;

            default:
                break;
        }
    }
}
