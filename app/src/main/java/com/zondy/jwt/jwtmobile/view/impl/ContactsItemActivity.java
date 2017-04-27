package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityContact;
import com.zondy.jwt.jwtmobile.ui.DividerItemDecoration;
import com.zondy.jwt.jwtmobile.util.ToastTool;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sheep on 2017/1/13.
 */

public class ContactsItemActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.rl_message)
    RelativeLayout rlMessage;
    @BindView(R.id.rl_telephone)
    RelativeLayout rlTelephone;
    @BindView(R.id.tv_jh)
    TextView tvJh;
    @BindView(R.id.tv_zw)
    TextView tvZw;
    @BindView(R.id.tv_ssdwmc)
    TextView tvSsdwmc;
    @BindView(R.id.ll_phone_container)
    LinearLayout llPhoneContainer;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    EntityContact entityContact;

    public static Intent createIntent(Context context, EntityContact entityContact) {
        Intent intent = new Intent(context, ContactsItemActivity.class);
        intent.putExtra("entityContact", entityContact);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_contacts_item;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initparam();
        initViews();

    }

    public void initparam() {
        Intent intent = getIntent();
        entityContact = (EntityContact) intent.getSerializableExtra("entityContact");
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_contacts_item);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbarlayout);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(entityContact.getXm());
        List<String> dhList = entityContact.getDhList();
        if (dhList != null && dhList.size() > 0) {

            for (int i = 0; i < dhList.size(); i++) {
                View phonenumView = LayoutInflater.from(context).inflate(R.layout.content_contact_phonenum, llPhoneContainer, false);
                TextView tvDh = (TextView) phonenumView.findViewById(R.id.tv_dh);
                final String phoneNum = dhList.get(i);
                if (phoneNum.length() < 11) {
                    tvDh.setText("短号：" + phoneNum);
                } else {
                    tvDh.setText("电话：" + phoneNum);
                }
                phonenumView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(phoneNum)) {
                            ToastTool.getInstance().shortLength(context, "电话号码为空,无法拨打", true);
                        } else {
                            showFunction(phoneNum);
                        }

                    }
                });
                llPhoneContainer.addView(phonenumView);

            }

        } else {
            View phonenumView = LayoutInflater.from(context).inflate(R.layout.content_contact_phonenum, llPhoneContainer, false);
            TextView tvDh = (TextView) phonenumView.findViewById(R.id.tv_dh);
            tvDh.setText("电话: 暂无");
            phonenumView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastTool.getInstance().shortLength(context, "电话号码为空,无法拨打", true);
                }
            });
            llPhoneContainer.addView(phonenumView);
        }
        tvJh.setText("警号：" + entityContact.getJh());
        tvZw.setText("职务：" + entityContact.getZw());
        tvSsdwmc.setText("所属单位：" + entityContact.getSsdwmc());
        rlMessage.setOnClickListener(this);
        rlTelephone.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_message:
                showPhoneNum("发送短信");
                break;
            case R.id.rl_telephone:
                showPhoneNum("拨打电话");
                break;
        }
    }


    PopupWindow showPhoneNumpopwindow;//展现电话号码的popupwindow
    PopupWindow showFunctionPopupWindow;//展现拨号和发短信的popupwindow

    public void showFunction(String phoneNum) {
        if(showPhoneNumpopwindow != null && showPhoneNumpopwindow.isShowing()){
            showPhoneNumpopwindow.dismiss();
        }
        if (TextUtils.isEmpty(phoneNum) || phoneNum.contains("无")) {
            ToastTool.getInstance().shortLength(context, "暂无号码", true);
            return;
        } else {
            if (showFunctionPopupWindow == null) {
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                View popContentView = LayoutInflater.from(context).inflate(R.layout.content_contact_popwindow, null);

                showFunctionPopupWindow = new PopupWindow(popContentView,  width/2, height/2);
                showFunctionPopupWindow.setTouchable(true);

                showFunctionPopupWindow.setTouchInterceptor(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                        // 这里如果返回true的话，touch事件将被拦截
                        // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                    }
                });

                // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
                showFunctionPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

                final TextView tvTitle = (TextView)popContentView.findViewById(R.id.tv_title);
                tvTitle.setText(phoneNum);
                Button btn = (Button)popContentView.findViewById(R.id.btn_cancel);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFunctionPopupWindow.dismiss();
                    }
                });
                RecyclerView rvPopChooseZZJG = (RecyclerView) popContentView.findViewById(R.id.rcv_contact_function);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rvPopChooseZZJG.setLayoutManager(linearLayoutManager);
                final List<String> functions = new ArrayList<>();
                functions.add("拨打电话");
                functions.add("发送短信");
                CommonAdapter<String> adapter = new CommonAdapter<String>(context, R.layout.item_contacts_pop_function, functions) {
                    @Override
                    protected void convert(ViewHolder holder, String s, int position) {
                        holder.setText(R.id.tv_contact_function, s);
                    }
                };
                adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                        String function = functions.get(position);
                        if ("拨打电话".equals(function)) {
                            Uri telToUri = Uri.parse("tel:" + tvTitle.getText().toString().trim());
                            Intent intentTel = new Intent(Intent.ACTION_DIAL, telToUri);
                            intentTel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentTel);
                        } else if ("发送短信".equals(function)) {
                            Uri smsToUri = Uri.parse("smsto:" +  tvTitle.getText().toString().trim());
                            Intent intentMes = new Intent(Intent.ACTION_SENDTO, smsToUri);
                            intentMes.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentMes);

                        }
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                });
                rvPopChooseZZJG.setAdapter(adapter);
                rvPopChooseZZJG.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));

                showFunctionPopupWindow.showAtLocation(llContainer, Gravity.CENTER, 0, 0);
            } else {
                TextView tvTitle = (TextView)showFunctionPopupWindow.getContentView().findViewById(R.id.tv_title);
                tvTitle.setText(phoneNum);
                showFunctionPopupWindow.showAtLocation(llContainer, Gravity.CENTER, 0, 0);
            }
        }
    }


    public void showPhoneNum(final String functionType) {
        if(showFunctionPopupWindow != null && showFunctionPopupWindow.isShowing()){
            showFunctionPopupWindow.dismiss();
        }
        switch (functionType) {
            case "拨打电话":
                if (entityContact.getDhList().size() <= 0) {
                    ToastTool.getInstance().shortLength(context, "暂无号码", true);
                    return;
                }
                break;
            case "发送短信":
                if (entityContact.getDhList().size() <= 0) {
                    ToastTool.getInstance().shortLength(context, "暂无号码", true);
                    return;
                }
                break;
        }

        if (showPhoneNumpopwindow == null) {
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            View popContentView = LayoutInflater.from(context).inflate(R.layout.content_contact_popwindow, null);

            showPhoneNumpopwindow = new PopupWindow(popContentView, width/2, height/2);
            showPhoneNumpopwindow.setTouchable(true);

            showPhoneNumpopwindow.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                    // 这里如果返回true的话，touch事件将被拦截
                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                }
            });

            // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
            showPhoneNumpopwindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
            final TextView tvTitle = (TextView)popContentView.findViewById(R.id.tv_title);
            tvTitle.setText("选择操作");
            Button btn = (Button)popContentView.findViewById(R.id.btn_cancel);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPhoneNumpopwindow.dismiss();
                }
            });
            RecyclerView rvPopChoosePhoneNum = (RecyclerView) popContentView.findViewById(R.id.rcv_contact_function);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvPopChoosePhoneNum.setLayoutManager(linearLayoutManager);
            CommonAdapter<String> adapter = new CommonAdapter<String>(context, R.layout.item_contacts_pop_function, entityContact.getDhList()) {
                @Override
                protected void convert(ViewHolder holder, String s, int position) {
                    holder.setText(R.id.tv_contact_function, s);
                }
            };
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if ("拨打电话".equals(functionType)) {
                        String phoneNum = entityContact.getDhList().get(position);
                        Uri telToUri = Uri.parse("tel:" + phoneNum);
                        Intent intentTel = new Intent(Intent.ACTION_DIAL, telToUri);
                        intentTel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentTel);
                    } else if ("发送短信".equals(functionType)) {
                        String phoneNum = entityContact.getDhList().get(position);

                        Uri smsToUri = Uri.parse("smsto:" + phoneNum);
                        Intent intentMes = new Intent(Intent.ACTION_SENDTO, smsToUri);
                        intentMes.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentMes);

                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            rvPopChoosePhoneNum.setAdapter(adapter);
            rvPopChoosePhoneNum.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));

            showPhoneNumpopwindow.showAtLocation(llContainer, Gravity.CENTER, 0, 0);
        } else {
            showPhoneNumpopwindow.showAtLocation(llContainer, Gravity.CENTER, 0, 0);
        }
    }
}
