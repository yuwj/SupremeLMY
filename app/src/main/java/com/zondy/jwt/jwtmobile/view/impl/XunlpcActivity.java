package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tubb.calendarselector.library.FullDay;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.util.ToastTool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by sheep on 2017/3/2.
 */

public class XunlpcActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_xunlpc_search)
    Button btnXunlpcSearch;

    @BindView(R.id.rl_kaisrq)
    RelativeLayout rlKaisrq;
    @BindView(R.id.rl_jiezrq)
    RelativeLayout rlJiezrq;
    @BindView(R.id.tv_kaisrq)
    TextView tvKaisrq;
    @BindView(R.id.tv_jiezrq)
    TextView tvJiezrq;


    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_xunlpc;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, XunlpcActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
    }

    private void initParam() {
        Date date = new Date(System.currentTimeMillis());
        Date date1 = new Date(System.currentTimeMillis() - 604800000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String jzrq = sdf.format(date);
        String ksrq = sdf.format(date1);
        tvJiezrq.setText(jzrq);
        tvKaisrq.setText(ksrq);
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        btnXunlpcSearch.setOnClickListener(this);

        rlKaisrq.setOnClickListener(this);
        rlJiezrq.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_xunlpc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                ToastTool.getInstance().shortLength(this, "盘查记录", true);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_xunlpc_search:
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if(simpleDateFormat.parse(tvKaisrq.getText().toString()).getTime()>simpleDateFormat.parse(tvJiezrq.getText().toString()).getTime()){
                        ToastTool.getInstance().shortLength(this,"截止日期不能小于开始日期！",true);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                XunlpcRycxlistActivity.actionStart(XunlpcActivity.this);
                break;

            case R.id.rl_kaisrq:
                Intent intent = new Intent(XunlpcActivity.this, CalendarSelectorActivity.class);
                startActivityForResult(intent, 8888);
                break;
            case R.id.rl_jiezrq:
                Intent intent1 = new Intent(XunlpcActivity.this, CalendarSelectorActivity.class);
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
