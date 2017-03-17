package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.base.MyApplication;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.ISettingPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.SettingPresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.ISettingView;
import com.zondy.mapgis.android.mapview.MapView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sheep on 2017/1/5.
 */

public class MainActivity extends BaseActivity implements ISettingView{
    @BindView(R.id.tv_ll_name)
    TextView tvName;
    @BindView(R.id.tv_ll_jh)
    TextView tvJh;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;
    private DrawerLayout drawerLayout;
    private MapView mapView;
    private FloatingActionButton fab;
    @BindView(R.id.rv_menu)
    RecyclerView rv_menu;//菜单
    @BindView(R.id.icon_image)
    CircleImageView icon_image;//圆形头像控件
    List<EntityMenu> menus;
    CommonAdapter<EntityMenu> adapterMenuList;

    ISettingPresenter settingPresenter = new SettingPresenterImpl(this);

    private static boolean isExit = false;// 标识是否退出系统

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.zondy.mapgis.android.environment.Environment.requestAuthorization(this, new com.zondy.mapgis.android.environment.Environment.AuthorizeCallback() {
            @Override
            public void onComplete() {
                initMap();
            }
        });
        initParams();
        initViews();
    }

    private void initMap() {
        mapView.loadFromFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MapGIS/map/wuhan/wuhan.xml");
        mapView.setZoomControlsEnabled(true);
        mapView.setShowLogo(true);
    }

    private void initParams() {
        mapView = (MapView) findViewById(R.id.mapview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        menus = new ArrayList<>();
        menus.add(new EntityMenu("通讯录", R.drawable.ic_tongxl));
        menus.add(new EntityMenu("警情处理", R.drawable.ic_jingqcl));
        menus.add(new EntityMenu("巡逻盘查", R.drawable.ic_pancbd));
        menus.add(new EntityMenu("数据采集", R.drawable.ic_shujucj));
        menus.add(new EntityMenu("设置", R.drawable.ic_shezhi));
        adapterMenuList = new CommonAdapter<EntityMenu>(context, R.layout.item_main_menu, menus) {
            @Override
            protected void convert(ViewHolder holder, EntityMenu s, int position) {
                TextView tv = holder.getView(R.id.tv_value);
                tv.setText(s.getMenuTitle());
                ImageView iv = holder.getView(R.id.iv_value);
                Glide.with(context).load(s.getMenuResourceId()).into(iv);

            }


        };
        adapterMenuList.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                drawerLayout.closeDrawers();
                TextView tv = (TextView) view.findViewById(R.id.tv_value);

                String menuTxt = tv.getText().toString().trim();
                if ("通讯录".equals(menuTxt)) {
                    Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
                    startActivity(intent);
                    etSearch.clearFocus();
                    return;
                }
                if ("警情处理".equals(menuTxt)) {
                    startActivity(JingqListActivity.createIntent(context));
                    etSearch.clearFocus();
                    return;
                }
                if ("设置".equals(menuTxt)) {
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                    etSearch.clearFocus();
                    return;
                }
                if ("巡逻盘查".equals(menuTxt)) {
                    XunlpcActivity.actionStart(MainActivity.this);
                    etSearch.clearFocus();
                    return;
                }
                if ("数据采集".equals(menuTxt)) {
                    ToastTool.getInstance().shortLength(context, menuTxt, true);
                    return;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initViews() {
        EntityUser user = SharedTool.getInstance().getUserInfo(MainActivity.this);
        if (user != null) {
            tvName.setText(user.getCtname());
            tvJh.setText("警号：" + user.getUserName());

//            String photoUrl = user.getUserPhotoUrl();
//            if(!TextUtils.isEmpty(photoUrl)){
//                Glide.with(context).load(photoUrl).into(icon_image);}
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_menu.setLayoutManager(linearLayoutManager);
        rv_menu.setAdapter(adapterMenuList);
        rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                etSearch.clearFocus();
            }
        });
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                    etSearch.clearFocus();
                }
            }
        });
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                etSearch.clearFocus();
            }
        });
    }

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
            EntityUser user = SharedTool.getInstance().getUserInfo(MainActivity.this);
            if (user != null) {
                String jh = user.getUserName();
                String simid = CommonUtil.getDeviceId(context);
                settingPresenter.logout(jh,simid);
            }else{
                ToastTool.getInstance().shortLength(context,"账号有误,退出失败",true);
            }
        }
    }

    @Override
    public void logoutSuccessed() {
        this.finish();
    }

    @Override
    public void logoutUnSuccessed() {

        ToastTool.getInstance().shortLength(context,"退出失败,请重试!",true);
    }


    class EntityMenu {
        String menuTitle;
        int menuResourceId;

        public EntityMenu(String menuTitle, int menuResourceId) {
            this.menuResourceId = menuResourceId;
            this.menuTitle = menuTitle;
        }

        public int getMenuResourceId() {
            return menuResourceId;
        }

        public void setMenuResourceId(int menuResourceId) {
            this.menuResourceId = menuResourceId;
        }

        public String getMenuTitle() {
            return menuTitle;
        }

        public void setMenuTitle(String menuTitle) {
            this.menuTitle = menuTitle;
        }
    }
}
