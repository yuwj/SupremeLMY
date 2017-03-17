package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tubb.calendarselector.library.FullDay;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityBaseGuij;
import com.zondy.jwt.jwtmobile.entity.EntityGuijWithLvg;
import com.zondy.jwt.jwtmobile.entity.EntityGuijWithWangb;
import com.zondy.jwt.jwtmobile.presenter.IXunlpcPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.XunlpcPresenter;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IXunlpcView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GuijTimeSelectActivity extends BaseActivity implements IXunlpcView {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_kaisrq)
    TextView tvKaisrq;
    @BindView(R.id.tv_jiezrq)
    TextView tvJiezrq;
    @BindView(R.id.btn_xunlpc_search)
    Button btnXunlpcSearch;

    String userName;
    int searchType;
    IXunlpcPresenter xunlpcPresenter;

    public static Intent createIntent(Context context, String userName, int searchType) {
        Intent intent = new Intent(context, GuijTimeSelectActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("searchType", searchType);
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
        switch (searchType) {
            case EntityBaseGuij.GUIJI_LVG:
                title += "的住宿轨迹";
                break;
            case EntityBaseGuij.GUIJI_WANGB:
                title += "的上网轨迹";
                break;
        }
        initActionBar(toolbar, tvTitle, title);

    }

    @Override
    public void showGuijWithLvgInMap(List<EntityGuijWithLvg> guijDatas) {
        startActivity(GuijMapWithLvgActivity.createIntent(context, guijDatas, tvTitle.getText().toString()));
    }

    @Override
    public void showGuijWithWangbInMap(List<EntityGuijWithWangb> guijDatas) {
        startActivity(GuijMapWithWangbActivity.createIntent(context, guijDatas, tvTitle.getText().toString()));
    }

    @OnClick({R.id.tv_kaisrq, R.id.tv_jiezrq, R.id.btn_xunlpc_search})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_xunlpc_search:
                String startTime = tvKaisrq.getText().toString();
                String endTime = tvJiezrq.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (simpleDateFormat.parse(startTime).getTime() > simpleDateFormat.parse(endTime).getTime()) {
                        ToastTool.getInstance().shortLength(this, "截止日期不能小于开始日期！", true);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                switch (searchType) {
                    case EntityBaseGuij.GUIJI_LVG:

                        xunlpcPresenter.searchGuijWithLvg(startTime, endTime, "用户id");
                        break;
                    case EntityBaseGuij.GUIJI_WANGB:
                        xunlpcPresenter.searchGuijWithWangb(startTime, endTime, "用户id");
                        break;
                }
                break;

            case R.id.tv_kaisrq:
                Intent intent = new Intent(context, CalendarSelectorActivity.class);
                startActivityForResult(intent, 8888);
                break;
            case R.id.tv_jiezrq:
                Intent intent1 = new Intent(context, CalendarSelectorActivity.class);
                startActivityForResult(intent1, 9999);
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8888) {
            if (resultCode == RESULT_OK) {
                FullDay day = (FullDay) data.getExtras().get("day");
                String month = "" + day.getMonth();
                if (day.getMonth() < 10) {
                    month = "0" + day.getMonth();
                }
                String date = "" + day.getDay();
                if (day.getDay() < 10) {
                    date = "0" + day.getDay();
                }
                tvKaisrq.setText(day.getYear() + "-" + month + "-" + date);
            }

        }
        if (requestCode == 9999) {
            if (resultCode == RESULT_OK) {
                FullDay day = (FullDay) data.getExtras().get("day");
                String month = "" + day.getMonth();
                if (day.getMonth() < 10) {
                    month = "0" + day.getMonth();
                }
                String date = "" + day.getDay();
                if (day.getDay() < 10) {
                    date = "0" + day.getDay();
                }
                tvJiezrq.setText(day.getYear() + "-" + month + "-" + date);
            }


        }
    }
}
