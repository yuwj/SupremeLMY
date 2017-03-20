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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tubb.calendarselector.library.FullDay;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityXunlpcJDCXX;
import com.zondy.jwt.jwtmobile.util.AutoCaseTransformationMethod;
import com.zondy.jwt.jwtmobile.util.BottomMenu;
import com.zondy.jwt.jwtmobile.util.KeyboardUtil;
import com.zondy.jwt.jwtmobile.util.ProvinceBottomMenu;
import com.zondy.jwt.jwtmobile.util.ToastTool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

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
    @BindView(R.id.ll_cheliang)
    LinearLayout llCheliang;
    @BindView(R.id.ll_renyuan)
    LinearLayout llRenyuan;
    @BindView(R.id.ll_renyuan_all)
    LinearLayout llRenyuanAll;
    @BindView(R.id.ll_cheliang_all)
    LinearLayout llCheliangAll;
    @BindView(R.id.sp_xunlpc_chellx)
    Spinner spXunlpcChellx;
    @BindView(R.id.sp_xunlpc_chep)
    Spinner spXunlpcChep;
    @BindView(R.id.et_xunlpc_chel_chep)
    EditText etXunlpcChelChep;
    @BindView(R.id.btn_xunlpc_cheliang_search)
    Button btnXunlpcCheliangSearch;
    @BindView(R.id.tv_xunlpc_chep)
    TextView tvXunlpcChep;

    private CommonAdapter<String> chellxAdapter;
    private CommonAdapter<String> chepdAdapter;

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
        etXunlpcChelChep.setTransformationMethod(new AutoCaseTransformationMethod());
        llCheliang.setOnClickListener(this);
        llRenyuan.setOnClickListener(this);
        btnXunlpcSearch.setOnClickListener(this);
        rlKaisrq.setOnClickListener(this);
        rlJiezrq.setOnClickListener(this);
        btnXunlpcCheliangSearch.setOnClickListener(this);
        List<String> chellxDatas = new ArrayList<>();
        chellxDatas.add("小型汽车");
        chellxDatas.add("大型货车");
        chellxDatas.add("中型客车");
        chellxDatas.add("牵引车");
        chellxDatas.add("大型客车");
        spXunlpcChellx.setAdapter(chellxAdapter = new CommonAdapter<String>(this, R.layout.item_jingqhandle_sp, chellxDatas) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(R.id.tv_value, item);
            }
        });
        List<String> chepdDatas = new ArrayList<>();
        chepdDatas.add("鄂");
        chepdDatas.add("沪");
        chepdDatas.add("京");
        chepdDatas.add("粤");

        spXunlpcChep.setAdapter(chepdAdapter = new CommonAdapter<String>(this, R.layout.item_jingqhandle_sp, chepdDatas) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(R.id.tv_value, item);
            }
        });
        tvXunlpcChep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProvinceBottomMenu bottomMenu=new ProvinceBottomMenu(XunlpcActivity.this,this,tvXunlpcChep);
                bottomMenu.show();
            }
        });
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
                XunlpcPCJLActivity.actionStart(this);
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (simpleDateFormat.parse(tvKaisrq.getText().toString()).getTime() > simpleDateFormat.parse(tvJiezrq.getText().toString()).getTime()) {
                        ToastTool.getInstance().shortLength(this, "截止日期不能小于开始日期！", true);
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
            case R.id.ll_cheliang:
                if (llRenyuanAll.getVisibility() == View.VISIBLE) {
                    etXunlpcChelChep.setFocusable(true);
                    etXunlpcChelChep.setFocusableInTouchMode(true);
                    etXunlpcChelChep.requestFocus();
                    llRenyuanAll.setVisibility(View.GONE);
                    llCheliangAll.setVisibility(View.VISIBLE);
                    llCheliang.setBackground(getDrawable(R.drawable.selector_xunlpc_huang));
                    llRenyuan.setBackground(getDrawable(R.drawable.selector_xunlpc_hui));
                }
                break;
            case R.id.ll_renyuan:
                if (llRenyuanAll.getVisibility() != View.VISIBLE) {
                    llRenyuanAll.setVisibility(View.VISIBLE);
                    llCheliangAll.setVisibility(View.GONE);
                    llRenyuan.setBackground(getDrawable(R.drawable.selector_xunlpc_huang));
                    llCheliang.setBackground(getDrawable(R.drawable.selector_xunlpc_hui));
                }
                break;
            case R.id.btn_xunlpc_cheliang_search:
                if (etXunlpcChelChep.getText().toString().length() == 0) {
                    ToastTool.getInstance().shortLength(XunlpcActivity.this, "车牌号码不能为空!", true);
                    return;
                }
                Pattern p = Pattern.compile("[a-zA-Z]");
                if (etXunlpcChelChep.getText().toString().length() > 0) {
                    String a = String.valueOf(etXunlpcChelChep.getText().toString().charAt(0));
                    if (etXunlpcChelChep.getText().toString().length() != 6 || !p.matcher(a).matches()) {
                        ToastTool.getInstance().shortLength(XunlpcActivity.this, "车牌号码不符合规范!", true);
                        return;
                    }
                }

                //车辆假数据
                EntityXunlpcJDCXX entityXunlpcJDCXX = new EntityXunlpcJDCXX();
                entityXunlpcJDCXX.setSuoyr("哈登");
                entityXunlpcJDCXX.setZhengjh("420593197811237485");
                entityXunlpcJDCXX.setPinpys("法拉利 红色");
                entityXunlpcJDCXX.setChelxh("Ferrari458");
                entityXunlpcJDCXX.setChucrq("2014-08-28");
                entityXunlpcJDCXX.setFadjh("482245829450");
                entityXunlpcJDCXX.setShibm("DMWIYFS45582042");
                entityXunlpcJDCXX.setHaop(etXunlpcChelChep.getText().toString().toUpperCase());
                XunlpcJDCXXActivity.actionStart(XunlpcActivity.this, entityXunlpcJDCXX);
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
