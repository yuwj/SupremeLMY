package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.IHomePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.HomePresenter;
import com.zondy.jwt.jwtmobile.service.PollService;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IHomeView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class MainWithGridActivity extends BaseActivity implements IHomeView {


    @BindView(R.id.tv_jiecj_msg_count)
    TextView tvJiecjMsgCount;
    @BindView(R.id.tv_bufbk_msg_count)
    TextView tvBufbkMsgCount;
    @BindView(R.id.tv_tongzgg_msg_count)
    TextView tvTongzggMsgCount;
    IHomePresenter homePresenter;
    EntityUser userInfo;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MainWithGridActivity.class);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_main_with_grid;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
        initOperator();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUnacceptMsgCount();
    }

    void initParam() {
        homePresenter = new HomePresenter(this);
        userInfo = SharedTool.getInstance().getUserInfo(context);
    }

    void initView() {
        tvJiecjMsgCount.setVisibility(View.INVISIBLE);
        tvBufbkMsgCount.setVisibility(View.INVISIBLE);
        tvTongzggMsgCount.setVisibility(View.INVISIBLE);
    }


    void initOperator() {

    }

    public void updateUnacceptMsgCount() {
        // 查询各模块的请求数据,然后显示msgCount
        homePresenter.queryUnacceptJingqCount(userInfo.getUserName(), CommonUtil.getDeviceId(context), userInfo.getZzjgdm());
        homePresenter.queryUnacceptBufbkCount(userInfo.getUserName(), CommonUtil.getDeviceId(context), userInfo.getCtname());
        homePresenter.queryUnacceptTongzggCount(userInfo.getUserName(), CommonUtil.getDeviceId(context), userInfo.getCtname());

    }

    @OnClick({R.id.iv_header, R.id.rl_jiecj, R.id.rl_zonghcx, R.id.rl_yucfg, R.id.rl_bufbk, R.id.rl_tongzgg, R.id.rl_qingqfw, R.id.rl_tongxl, R.id.tv_home, R.id.tv_mine})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_header:
                break;
            case R.id.rl_jiecj:
                intent = JingqListActivity.createIntent(context);
                break;
            case R.id.rl_zonghcx:
//                ToastTool.getInstance().shortLength(context, "综合查询模块未开放", true);
                intent = CompositeSearchMainActivity.createIntent(MainWithGridActivity.this);

//                intent = new Intent(context,SearchResultsItemActivity.class);
//                intent.putExtra("NAME","xx");
//                intent.putExtra("NAME","DZ");
//                intent.putExtra("NAME","DH");
                break;
            case R.id.rl_yucfg:
                ToastTool.getInstance().shortLength(context, "预测方格模块未开放", true);
                break;
            case R.id.rl_bufbk:
                intent = BufbkListActivity.createIntent(context);
                break;
            case R.id.rl_tongzgg:
//                intent = NoticeListActivity.createIntent(context, EntityNotice.NOTICE_TYPE_TONGZGG);
                intent = MaterialDesignTest.createIntent(context);
                break;
            case R.id.rl_qingqfw:
//                ToastTool.getInstance().shortLength(context, "请求服务模块未开放", true);
                intent = AskForServiceActivity.createIntent(context);
                break;
            case R.id.rl_tongxl:
                intent = new Intent(MainWithGridActivity.this, ContactsActivity.class);
                break;
            case R.id.tv_home:
                ToastTool.getInstance().shortLength(context, "首页", true);
                break;
            case R.id.tv_mine:
                intent = new Intent(MainWithGridActivity.this, SettingActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    private boolean isExit = false;// 标识是否退出系统

    @Override
    public void onBackPressed() {
        // 2秒内双击退出系统
        if (!isExit) {
            isExit = true;
            ToastTool.getInstance().shortLength(this, "再按一次退出程序！", true);
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            EntityUser user = SharedTool.getInstance().getUserInfo(MainWithGridActivity.this);
            if (user != null) {
                String jh = user.getUserName();
                String simid = CommonUtil.getDeviceId(context);
                homePresenter.logout(jh, simid);
            } else {
                ToastTool.getInstance().shortLength(context, "账号有误,退出失败", true);
            }
        }
    }

    @Override
    public void queryUnacceptJingqCountSuccess(int count) {
        if (count > 0) {
            tvJiecjMsgCount.setVisibility(View.VISIBLE);
            tvJiecjMsgCount.setText(count + "");
        }
    }

    @Override
    public void queryUnacceptJingqCountFail(Exception e) {

        tvJiecjMsgCount.setVisibility(View.GONE);
    }

    @Override
    public void queryUnacceptBufbkCountSuccess(int count) {
        if (count > 0) {
            tvBufbkMsgCount.setVisibility(View.VISIBLE);
            tvBufbkMsgCount.setText(count + "");
        }
    }

    @Override
    public void queryUnacceptBufbkCountFail(Exception e) {

        tvBufbkMsgCount.setVisibility(View.GONE);
    }

    @Override
    public void queryUnacceptTongzggCountSuccess(int count) {
        if (count > 0) {
            tvTongzggMsgCount.setVisibility(View.VISIBLE);
            tvTongzggMsgCount.setText(count + "");
        }
    }

    @Override
    public void queryUnacceptTongzggCountFail(Exception e) {

        tvTongzggMsgCount.setVisibility(View.GONE);
    }

    @Override
    public void logoutSuccess() {
        startService(PollService.createIntent(context,30,false));
        this.finish();
    }

    @Override
    public void logoutFail(Exception e) {
        ToastTool.getInstance().shortLength(context, e.getMessage(), true);
    }
}
