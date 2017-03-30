package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuwj.appupdate.UpdateChecker;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.callback.IipSetListener;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.manager.IpSetManager;
import com.zondy.jwt.jwtmobile.manager.UrlManager;
import com.zondy.jwt.jwtmobile.presenter.ILoginPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.LoginPresenterImpl;
import com.zondy.jwt.jwtmobile.util.BottomMenu;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.ILoginView;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.zondy.jwt.jwtmobile.R.id.btn_login;
import static com.zondy.jwt.jwtmobile.R.id.btn_login_recorded;
import static com.zondy.jwt.jwtmobile.R.id.tv_ip;
import static com.zondy.jwt.jwtmobile.R.id.tv_more;

public class LoginActivity extends BaseActivity implements ILoginView, View.OnClickListener {
    EntityUser entityUser;
    IpSetManager ipSetManager;
    IipSetListener ipSetListener;

    ILoginPresenter loginPresenter = new LoginPresenterImpl(this);
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.icon_image)
    CircleImageView iconImage;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.et_password_recorded)
    EditText etPasswordRecorded;
    @BindView(R.id.btn_login_recorded)
    Button btnLoginRecorded;
    @BindView(R.id.ll_recorded)
    LinearLayout llRecorded;
    @BindView(R.id.ll_norecord)
    LinearLayout llNorecord;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;
    @BindView(R.id.tv_ip)
    TextView tvIp;

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
        initOperator();
    }

    private void initParam() {
        entityUser = SharedTool.getInstance().getUserInfo(this);
        ipSetManager = new IpSetManager();
        ipSetListener = new IipSetListener() {
            @Override
            public void onIpSeted(String serverIp, String serverPort, String pushIp, String pushPort) {

            }
        };
    }

    private void initView() {
        if (!entityUser.getUserName().equals("")) {
            llNorecord.setVisibility(View.GONE);
            tvIp.setVisibility(View.GONE);
            llRecorded.setVisibility(View.VISIBLE);
            tvMore.setVisibility(View.VISIBLE);
            tvUsername.setText(entityUser.getUserName());
            etPasswordRecorded.setText(entityUser.getPassword());
            rlBg.setBackgroundResource(R.drawable.bg_login_recorded);
            Glide.with(context).load(entityUser.getUserPhotoUrl())
                    .placeholder(R.drawable.ic_default_photo)//
                    .error(R.drawable.ic_default_photo)//
                    .animate( android.R.anim.fade_in)
                    .into(iconImage);
        }
        btnLoginRecorded.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvMore.setOnClickListener(this);
        tvIp.setOnClickListener(this);
    }

    private void initOperator() {
        String updateInfoUrl = UrlManager.getSERVER() + UrlManager.UPDATE;
        UpdateChecker.checkForDialog(context,false,updateInfoUrl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                llNorecord.setVisibility(View.VISIBLE);
                tvIp.setVisibility(View.VISIBLE);
                llRecorded.setVisibility(View.GONE);
                tvMore.setVisibility(View.GONE);
                rlBg.setBackgroundResource(R.drawable.bg_login);
                break;
            case R.id.btn2:
                ipSetManager.showSetIpDialog(LoginActivity.this,ipSetListener);
                break;
            case tv_more:
                BottomMenu bottomMenu=new BottomMenu(LoginActivity.this,this);
                bottomMenu.show();
                break;
            case btn_login:
                final String userName = etUsername.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    ToastTool.getInstance().shortLength(context, "用户名或密码不能为空", true);
                    return;
                }

                if (TextUtils.isEmpty(UrlManager.HOST_IP)
                        || TextUtils.isEmpty(UrlManager.HOST_PORT)) {
                    ToastTool.getInstance().shortLength(context, "请先设置ip和端口", true);
                    return;
                }
                String deviceId = CommonUtil.getDeviceId(context);
                loginPresenter.login(context,userName, password, deviceId);
                showLoadingDialog("正在登录...");
                break;
            case btn_login_recorded:
                final String userNameRecorded = tvUsername.getText().toString().trim();
                final String passwordRecorded = etPasswordRecorded.getText().toString().trim();
                if (TextUtils.isEmpty(passwordRecorded)) {
                    ToastTool.getInstance().shortLength(context, "密码不能为空", true);
                    return;
                }

                if (TextUtils.isEmpty(UrlManager.HOST_IP)
                        || TextUtils.isEmpty(UrlManager.HOST_PORT)) {
                    ToastTool.getInstance().shortLength(context, "请先设置ip和端口", true);
                    return;
                }
                String deviceIdRecorded = CommonUtil.getDeviceId(context);
                loginPresenter.login(context,userNameRecorded, passwordRecorded, deviceIdRecorded);
                showLoadingDialog("正在登录...");
                break;
            case tv_ip:
                ipSetManager.showSetIpDialog(LoginActivity.this, ipSetListener);
                break;
        }
    }


    /**
     * 保存GPS定位配置及更新定位服务.
     *
     * @param sTimeInterval
     * @param sDistanceInterval
     */
    public void saveGPSSetting(String sTimeInterval, String sDistanceInterval) {
        long distanceInterval = 0;
        long timeInterval = 0;
        try {
            timeInterval = Long.valueOf(sTimeInterval);
        } catch (Exception e) {
        }
        try {
            distanceInterval = Long.valueOf(sDistanceInterval);
        } catch (Exception e) {
        }
        if (distanceInterval == 0 && timeInterval == 0) {// 都为0时,不需要实时每秒都上传GPS,默认30s一次.
            distanceInterval = 0;
            timeInterval = 30;
        }
        SharedTool.getInstance().saveLocationInterval(context, timeInterval,
                distanceInterval);

    }


    @Override
    public void loginSuccessed(EntityUser entityUser) {
        dismissLoadingDialog();
        //保存用户信息
        SharedTool.getInstance().saveUserInfo(LoginActivity.this, entityUser);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent = MainWithGridActivity.createIntent(context);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void loginFailed() {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(this, "网络请求失败！", true);
//        startActivity(CompositeSearchMainActivity.createIntent(context));
    }

    @Override
    public void loginUnSuccessed(String msg) {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(this, msg, true);
    }

}

