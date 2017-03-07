package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityJingq;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.IJingqHandlePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.JingqHandlePresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IJingqListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class JingqListActivity extends BaseActivity implements IJingqListView {


    @BindView(R.id.rl_jingqdatas)
    XRecyclerView rlJingqdatas;

    IJingqHandlePresenter jingqclPresenter;
    List<EntityJingq> jingqDatas;
    CommonAdapter<EntityJingq> adapterJingqList;
    EntityUser user;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    private ImageView ivChay;
    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, JingqListActivity.class);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_jingqcl_list;
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
        queryJingqDatas();

    }

    public void initParam() {
        jingqclPresenter = new JingqHandlePresenterImpl(this);
        user = SharedTool.getInstance().getUserInfo(context);
        jingqDatas = new ArrayList<EntityJingq>();

        adapterJingqList = new CommonAdapter<EntityJingq>(context, R.layout.item_jingqcl_list, jingqDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityJingq entityJingq, int position) {
                holder.setText(R.id.tv_area, entityJingq.getBaojdz());
                holder.setText(R.id.tv_message, entityJingq.getBaojnr());
                holder.setText(R.id.tv_time, entityJingq.getBaojsj());
            }


        };
    }

    public void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rlJingqdatas.setLayoutManager(linearLayoutManager);
        rlJingqdatas.setRefreshProgressStyle(ProgressStyle.Pacman);
        rlJingqdatas.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        rlJingqdatas.setPullRefreshEnabled(true);
        rlJingqdatas.setLoadingMoreEnabled(false);
        rlJingqdatas.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                String jh = user.getUserName();
                String simid = CommonUtil.getDeviceId(context);
                jingqclPresenter.queryJingqDatas(jh, simid);
                if(ivChay!=null){
                    ivChay.setImageDrawable(getDrawable(R.drawable.ic_yichayue));
                }
            }

            @Override
            public void onLoadMore() {

            }
        });

        rlJingqdatas.setAdapter(adapterJingqList);
        rlJingqdatas.addItemDecoration(new SimpleDividerDecoration(context));
        adapterJingqList.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                EntityJingq jingq = jingqDatas.get(position - 1);
                ivChay= (ImageView) holder.itemView.findViewById(R.id.iv_jingqcl_chayue);
                ivChay.setImageDrawable(getDrawable(R.drawable.ic_weichayue));
                startActivity(JingqDetailWithUnhandleActivity.createIntent(context, jingq));
                etSearch.clearFocus();

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }


    public void initOperator() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_jingqcl, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                ToastTool.getInstance().shortLength(this, "菜单", true);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public void queryJingqDatas() {
        String jh = user.getUserName();
        String simid = CommonUtil.getDeviceId(context);
        jingqclPresenter.queryJingqDatas(jh, simid);
    }


    @Override
    public void onGetJingqDatasSuccess(final List<EntityJingq> jingqDatas) {
        recyclerViewLoadFinish();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (jingqDatas != null) {
                    JingqListActivity.this.jingqDatas.clear();
                    JingqListActivity.this.jingqDatas.addAll(jingqDatas);
                    adapterJingqList.notifyDataSetChanged();
                }

            }
        });

    }

    public void recyclerViewLoadFinish() {
        rlJingqdatas.refreshComplete();
        rlJingqdatas.loadMoreComplete();
    }

    @Override
    public void onGetJingqDatasFailed(Exception e) {
        recyclerViewLoadFinish();
        ToastTool.getInstance().shortLength(context, e.getMessage(), true);
    }

    @Override
    public void showLoadingProgress(boolean isShow) {
        if (isShow) {
            showLoadingDialog();
        } else {
            dismissLoadingDialog();
        }
    }
}
