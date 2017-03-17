package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityBaseGuij;
import com.zondy.jwt.jwtmobile.entity.EntityGuijWithLvg;
import com.zondy.jwt.jwtmobile.presenter.IXunlpcPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.XunlpcPresenter;
import com.zondy.jwt.jwtmobile.view.IXunlpcView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GuijTimeSelectActivity extends BaseActivity implements IXunlpcView{


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.btn_search)
    Button btnSearch;

    String userName;
    int searchType;
    IXunlpcPresenter xunlpcPresenter;

    public static Intent createIntent(Context context,String userName,int searchType) {
        Intent intent = new Intent(context, GuijTimeSelectActivity.class);
        intent.putExtra("userName",userName);
        intent.putExtra("searchType",searchType);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_guij_time_select;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
    }

    public void initParam() {
        Intent intent = getIntent();
        searchType = intent.getIntExtra("searchType", EntityBaseGuij.GUIJI_LVG);
        userName = intent.getStringExtra("userName");
        xunlpcPresenter = new XunlpcPresenter(this);
    }

    public void initView() {
        String title = userName;
        switch (searchType){
            case EntityBaseGuij.GUIJI_LVG:
                title+="的住宿轨迹";
                break;
            case EntityBaseGuij.GUIJI_WANGB:
                title+="的上网轨迹";
                break;
        }
        initActionBar(toolbar,tvTitle,title);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }

    @OnClick({R.id.tv_start_time, R.id.tv_end_time, R.id.btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                //TODO 调用李敏阳写好的时间选择控件
                break;
            case R.id.tv_end_time:
                //TODO 调用李敏阳写好的时间选择控件
                break;
            case R.id.btn_search:
                //TODO 搜索
                String startTime = tvStartTime.getText().toString().trim();
                String endTime = tvEndTime.getText().toString().trim();
                xunlpcPresenter.searchGuijWithLvg(startTime,endTime,"用户id");
                break;
        }
    }

    @Override
    public void showGuijWithLvgInMap(List<EntityGuijWithLvg> guijDatas) {
        startActivity(GuijMapWithLvgActivity.createIntent(context,guijDatas,tvTitle.getText().toString()));
    }
}
