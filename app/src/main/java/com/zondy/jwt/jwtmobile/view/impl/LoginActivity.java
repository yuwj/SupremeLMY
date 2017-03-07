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
                // TODO: 2017/1/9    需要增加检查更新的操作
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
            rlBg.setBackground(getDrawable(R.drawable.bg_login_recorded));
        }
        btnLoginRecorded.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvMore.setOnClickListener(this);
        tvIp.setOnClickListener(this);
    }

    private void initOperator() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                llNorecord.setVisibility(View.VISIBLE);
                tvIp.setVisibility(View.VISIBLE);
                llRecorded.setVisibility(View.GONE);
                tvMore.setVisibility(View.GONE);
                rlBg.setBackground(getDrawable(R.drawable.bg_login));
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
                loginPresenter.login(userName, password, deviceId);
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
                loginPresenter.login(userNameRecorded, passwordRecorded, deviceIdRecorded);
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
        entityUser.setPassword(etPassword.getText().toString());//因为服务端没有返回用户密码，此操作为保存密码
        //保存用户信息
        SharedTool.getInstance().saveUserInfo(LoginActivity.this, entityUser);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void loginFailed() {
        ToastTool.getInstance().shortLength(this, "网络请求失败！", true);
    }

    @Override
    public void loginUnSuccessed(String msg) {
        ToastTool.getInstance().shortLength(this, msg, true);
    }

}

