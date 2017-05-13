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
import com.zondy.jwt.jwtmobile.ui.DividerItemDecoration;
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
    public List<ImageView> ivChays=new ArrayList<>();

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
                ImageView iv1=holder.getView(R.id.iv_jingqcl_state1);
                ImageView iv2=holder.getView(R.id.iv_jingqcl_state2);
                ImageView iv3=holder.getView(R.id.iv_jingqcl_state3);
                ImageView iv4=holder.getView(R.id.iv_jingqcl_state4);
                TextView tvState=holder.getView(R.id.tv_state);
                TextView tvBaojsj=holder.getView(R.id.tv_time_baojsj);
                TextView tvJiejsj=holder.getView(R.id.tv_time_jiejsj);
                TextView tvDaodsj=holder.getView(R.id.tv_time_chujsj);
                TextView tvFanksj=holder.getView(R.id.tv_time_wancsj);
                holder.setText(R.id.tv_area, entityJingq.getBaojdz());
                holder.setText(R.id.tv_message, entityJingq.getBaojnr());
                holder.setText(R.id.tv_time, entityJingq.getBaojsj());
                if(entityJingq.getJingqzt()==0){
                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.GONE);
                    iv3.setVisibility(View.GONE);
                    iv4.setVisibility(View.GONE);
                    tvState.setText("未接警");
                    tvBaojsj.setText(entityJingq.getBaojsj().substring(11));
                    tvJiejsj.setText("五分钟内");
                    tvDaodsj.setText("三十分钟内");
                    tvFanksj.setText("一小时内");
                }else if(entityJingq.getJingqzt()==1){
                    iv1.setVisibility(View.GONE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.GONE);
                    iv4.setVisibility(View.GONE);
                    tvState.setText("已接警");
                    tvBaojsj.setText(entityJingq.getBaojsj().substring(11));
                    if(entityJingq.getChujsj()!=null){
                        tvJiejsj.setText(entityJingq.getChujsj().substring(11));
                    }
                    tvDaodsj.setText("三十分钟内");
                    tvFanksj.setText("一小时内");
                }else if(entityJingq.getJingqzt()==2){
                    iv1.setVisibility(View.GONE);
                    iv2.setVisibility(View.GONE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.GONE);
                    tvState.setText("到达现场");
                    tvBaojsj.setText(entityJingq.getBaojsj().substring(11));
                    if(entityJingq.getChujsj()!=null){
                        tvJiejsj.setText(entityJingq.getChujsj().substring(11));
                    }
                    if(entityJingq.getDaodsj()!=null){
                        tvDaodsj.setText(entityJingq.getDaodsj().substring(11));
                    }
                    tvFanksj.setText("一小时内");
                }else if(entityJingq.getJingqzt()==3){
                    iv1.setVisibility(View.GONE);
                    iv2.setVisibility(View.GONE);
                    iv3.setVisibility(View.GONE);
                    iv4.setVisibility(View.VISIBLE);
                    tvState.setText("处警完毕，资料提交成功");
                    tvBaojsj.setText(entityJingq.getBaojsj().substring(11));
                    if(entityJingq.getChujsj()!=null){
                        tvJiejsj.setText(entityJingq.getChujsj().substring(11));
                    }
                    if(entityJingq.getDaodsj()!=null){
                        tvDaodsj.setText(entityJingq.getDaodsj().substring(11));
                    }
                    if(entityJingq.getFanksj()!=null){
                        tvFanksj.setText(entityJingq.getFanksj().substring(11));
                    }
                }else if(entityJingq.getJingqzt()==4){
                    iv1.setVisibility(View.GONE);
                    iv2.setVisibility(View.GONE);
                    iv3.setVisibility(View.GONE);
                    iv4.setVisibility(View.VISIBLE);
                    tvState.setText("处警完毕，资料提交失败");
                    tvBaojsj.setText(entityJingq.getBaojsj().substring(11));
                    if(entityJingq.getChujsj()!=null){
                        tvJiejsj.setText(entityJingq.getChujsj().substring(11));
                    }
                    if(entityJingq.getDaodsj()!=null){
                        tvDaodsj.setText(entityJingq.getDaodsj().substring(11));
                    }
                    if(entityJingq.getFanksj()!=null){
                        tvFanksj.setText(entityJingq.getFanksj().substring(11));
                    }
                }
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
                queryJingqDatas();
                if(ivChays!=null&&ivChays.size()>0){
                    for(ImageView ivCY:ivChays){
                        ivCY.setImageDrawable(getResources().getDrawable(R.drawable.ic_yichayue));
                    }
                }
            }

            @Override
            public void onLoadMore() {

            }
        });

        rlJingqdatas.setAdapter(adapterJingqList);
        rlJingqdatas.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));
        adapterJingqList.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                EntityJingq jingq = jingqDatas.get(position - 1);
                ImageView ivChay= (ImageView) holder.itemView.findViewById(R.id.iv_jingqcl_chayue);
                ivChays.add(ivChay);
                ivChay.setImageDrawable(getResources().getDrawable(R.drawable.ic_weichayue));
                switch (jingq.getState()){
                    case EntityJingq.HADHANDLED:
                        startActivity(JingqDetailWithHandledActivity2.createIntent(JingqListActivity.this,jingq));
                        break;
                    case EntityJingq.HADREACHCONFIRM:

                        startActivity(JingqHandleActivity.createIntent(context, jingq));
                        break;
                    case EntityJingq.UNREAD:
                    case EntityJingq.HADREAD:
                        startActivity(JingqDetailWithUnhandleActivity.createIntent(context, jingq));
                        break;

                }
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
        showLoadingProgress(true);
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
        dismissLoadingDialog();
    }

    @Override
    public void onGetJingqDatasFailed(Exception e) {
        recyclerViewLoadFinish();
        ToastTool.getInstance().shortLength(context, e.getMessage(), true);
    }

    @Override
    public void showLoadingProgress(boolean isShow) {
        if (isShow) {
            showLoadingDialog("正在加载...");
        } else {
            dismissLoadingDialog();
        }
    }
}
