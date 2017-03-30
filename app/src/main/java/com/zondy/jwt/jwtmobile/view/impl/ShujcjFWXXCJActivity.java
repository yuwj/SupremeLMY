package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.util.ToastTool;

import butterknife.BindView;


/**
 * Created by sheep on 2017/3/28.
 */

public class ShujcjFWXXCJActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.btn_jianzhuwumian)
    Button btnJianzhuwumian;
    @BindView(R.id.ll_parent)
    LinearLayout llParent;

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_shujcj_fwxxcj;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShujcjFWXXCJActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        initViews();
    }

    private void initParams() {

    }

    private void initViews() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        btnCommit.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnJianzhuwumian.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                ShujcjFWXXCXJGActivity.actionStart(this);
                break;
            case R.id.btn_commit:
                View view = getLayoutInflater().inflate(R.layout.pop_fwxxcj, null);
                final PopupWindow popupWindow = new PopupWindow(view,550,360,true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                popupWindow.setOutsideTouchable(false);
                popupWindow.showAtLocation(llParent,Gravity.CENTER,0,0);
                Button btnConfirm= (Button) view.findViewById(R.id.btn_yes);
                Button btnCancel= (Button) view.findViewById(R.id.btn_no);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastTool.getInstance().shortLength(ShujcjFWXXCJActivity.this,"层户信息录入成功！",true);
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.btn_jianzhuwumian:
                ShujcjCHJGGKActivity.actionStart(ShujcjFWXXCJActivity.this);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
