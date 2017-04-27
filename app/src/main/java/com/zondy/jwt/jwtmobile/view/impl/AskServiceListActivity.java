package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.YuwjBaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityAskService;
import com.zondy.jwt.jwtmobile.entity.EntityPage;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.IAskServicePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.AskServicePresenterImpl;
import com.zondy.jwt.jwtmobile.ui.DividerItemDecoration;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IAskServiceListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 已发送的请求服务
 */
public class AskServiceListActivity extends YuwjBaseActivity implements IAskServiceListView {

    @BindView(R.id.rl_jingqdatas)
    XRecyclerView rlAskService;

    IAskServicePresenter askServicePresenter;
    List<EntityAskService> askServiceDatas;
    CommonAdapter<EntityAskService> adapterAskServiceList;
    EntityUser user;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    EntityPage entityPage;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, AskServiceListActivity.class);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_askforservice_list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryJingqDatas(true);
    }

    public void initParam() {
        entityPage = new EntityPage();
        askServicePresenter = new AskServicePresenterImpl(this);
        user = SharedTool.getInstance().getUserInfo(context);
        askServiceDatas = new ArrayList<>();

        adapterAskServiceList = new CommonAdapter<EntityAskService>(context, R.layout.item_askforservice_list, askServiceDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityAskService entityJingq, int position) {
                holder.setText(R.id.tv_description, entityJingq.getServiceDescription());
                holder.setText(R.id.tv_type, entityJingq.getType() + "");
                holder.setText(R.id.tv_time, entityJingq.getTime());
            }
        };
        adapterAskServiceList.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                EntityAskService askService = askServiceDatas.get(position-1);
                switch (askService.getType()){
                    case EntityAskService.ASK_FOR_SERVICE_TYPE_BUK:
                        startActivity(AskBukDetailActivity.createIntent(context,askService.getAskServiceID(),askService.getJingqID()));
                        break;
                    case EntityAskService.ASK_FOR_SERVICE_TYPE_ZENGY:
                        ToastTool.getInstance().shortLength(context,"增援",true);
                        break;
                    case EntityAskService.ASK_FOR_SERVICE_TYPE_CHAX:
                        ToastTool.getInstance().shortLength(context,"查询",true);
                        break;
                    case EntityAskService.ASK_FOR_SERVICE_TYPE_ZOUS:
                        ToastTool.getInstance().shortLength(context,"走失",true);
                        break;
                    case EntityAskService.ASK_FOR_SERVICE_TYPE_QIT:
                        ToastTool.getInstance().shortLength(context,"其他",true);
                        break;
                    default:
                    break;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    public void initView() {
        toolbar.setTitle("请求服务");
        setSupportActionBar(toolbar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rlAskService.setLayoutManager(linearLayoutManager);
        rlAskService.setRefreshProgressStyle(ProgressStyle.Pacman);
        rlAskService.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        rlAskService.setPullRefreshEnabled(true);
        rlAskService.setLoadingMoreEnabled(false);
        rlAskService.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                entityPage.setPageNo(1);
                queryJingqDatas(true);
            }

            @Override
            public void onLoadMore() {
                queryJingqDatas(false);
            }
        });

        rlAskService.setAdapter(adapterAskServiceList);
        rlAskService.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));

    }


    public void initOperator() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_askservice_launchservice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_launch_service:
                ToastTool.getInstance().shortLength(this, "发起请求", true);
                startActivity(AskServiceMainActivity.createIntent(context,""));
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    /**
     * 查询数据
     * @param isRefresh 是否是刷新,是的话页码是第一页,否则就是加载下一页.
     */
    public void queryJingqDatas(boolean isRefresh) {
        String jh = user.getUserName();
        String simid = CommonUtil.getDeviceId(context);
        String xingm = user.getCtname();
        if (isRefresh) {
            askServicePresenter.queryAskServiceList(jh, simid, xingm, entityPage.getPageSize(), entityPage.getPageNo());
        } else {
            askServicePresenter.queryAskServiceList(jh, simid, xingm, entityPage.getComputeNextPage(), entityPage.getPageNo());

        }
        showLoadingDialog("正在加载");
    }


    @Override
    public void onGetServiceDatasSuccess(final List<EntityAskService> jingqDatas) {
        recyclerViewLoadFinish();
        dismissLoadingDialog();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (jingqDatas != null) {
                    AskServiceListActivity.this.askServiceDatas.clear();
                    AskServiceListActivity.this.askServiceDatas.addAll(jingqDatas);
                    adapterAskServiceList.notifyDataSetChanged();
                }
            }
        });

    }

    public void recyclerViewLoadFinish() {
        rlAskService.refreshComplete();
        rlAskService.loadMoreComplete();
        dismissLoadingDialog();
    }

    @Override
    public void onGetServiceDatasFailed(Exception e) {
        recyclerViewLoadFinish();
        ToastTool.getInstance().shortLength(context, e.getMessage(), true);
    }
}
