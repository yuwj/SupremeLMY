package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityBufbkFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityBufbk;
import com.zondy.jwt.jwtmobile.entity.EntityPage;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.IBufbkPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.BufbkPresenter;
import com.zondy.jwt.jwtmobile.ui.DividerItemDecoration;
import com.zondy.jwt.jwtmobile.ui.FullyGridLayoutManager;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IBufbkView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BufbkListActivity extends BaseActivity implements IBufbkView {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.xrv_bufbk_list)
    XRecyclerView xrvBufbkList;
    List<EntityBufbk> bufbkDatas;
    CommonAdapter<EntityBufbk> bufbkAdapter;
    IBufbkPresenter bufbkPresenter;
    EntityUser userInfo;
    EntityPage entityPage;
    EntityBufbk selectedEntityBufbk;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, BufbkListActivity.class);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_bufbk_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
        initOperator();
    }

    public void initParam() {
        Intent intent = getIntent();
        entityPage = new EntityPage();
        userInfo = SharedTool.getInstance().getUserInfo(context);
        bufbkPresenter = new BufbkPresenter(this);
        bufbkDatas = new ArrayList<>();
        bufbkAdapter = new CommonAdapter<EntityBufbk>(context, R.layout.item_bufbk_list, bufbkDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityBufbk entityBufbk, int position) {
                holder.setText(R.id.tv_title,"标题: "+entityBufbk.getMc());
                holder.setText(R.id.tv_content,"内容: "+entityBufbk.getBksy());
                holder.setText(R.id.tv_time,"时间: "+entityBufbk.getBksj());

                final List<String> mediaPaths = entityBufbk.getFilePaths();

                if(mediaPaths != null && mediaPaths.size()>0){
                    RecyclerView recyclerView = holder.getView(R.id.rv_bufbk_medias);
                    CommonAdapter<String> mediaAdapter = new CommonAdapter<String>(context,R.layout.item_bufbk_media,mediaPaths) {
                        @Override
                        protected void convert(ViewHolder holder, String s, int position) {
                            ImageView ivTuisxx = holder.getView(R.id.iv_bufbk_image);
                            Glide.with(mContext)
                                    .load(s)
                                    .placeholder(R.drawable.ic_handlejingq_wait_img)
                                    .error(R.drawable.ic_img_load_fail)
                                    .crossFade()
                                    .into(ivTuisxx);
                        }
                    };
                    recyclerView.setLayoutManager(new FullyGridLayoutManager(context,4));
                    recyclerView.setAdapter(mediaAdapter);
                    mediaAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                            startActivity(JingqImageEditActivity.createIntent(context,mediaPaths,position,false));
                        }

                        @Override
                        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                            return false;
                        }
                    });
                }
            }
        };

        bufbkAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                selectedEntityBufbk = bufbkDatas.get(position-1);
                String jh = userInfo.getUserName();
                String simid = CommonUtil.getDeviceId(context);
                bufbkPresenter.queryBufbkFeedbackDatas(jh, simid, selectedEntityBufbk.getId());
                showLoadingDialog("正在加载...");
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    public void initView() {
        String title = "布控布防";
        initActionBar(toolbar,tvTitle,title);
        xrvBufbkList.setLayoutManager(new LinearLayoutManager(context));
        xrvBufbkList.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));
        xrvBufbkList.setAdapter(bufbkAdapter);
        xrvBufbkList.setLoadingMoreEnabled(false);
        xrvBufbkList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoadMore() {

            }
        });

    }

    public void initOperator() {
        refresh();
    }

    public void refresh() {
        bufbkPresenter.queryBufbkList( userInfo.getUserName(), CommonUtil.getDeviceId(context),userInfo.getCtname(),entityPage.getPageSize(),entityPage.getPageNo());
        showLoadingDialog("正在加载...");
    }

    @Override
    public void queryBufbkListSuccess(List<EntityBufbk> entityBufbkList) {
        dismissLoadingDialog();
        xrvBufbkList.refreshComplete();
        xrvBufbkList.loadMoreComplete();
        ToastTool.getInstance().shortLength(context, "查询通知列表成功" + entityBufbkList.size(), true);
        if (entityBufbkList != null && entityBufbkList.size() > 0) {
            bufbkDatas.clear();
            bufbkDatas.addAll(entityBufbkList);
            bufbkAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void queryBufbkListFail(Exception e) {
        dismissLoadingDialog();
        xrvBufbkList.refreshComplete();
        xrvBufbkList.loadMoreComplete();
        ToastTool.getInstance().shortLength(context, "查询通知列表失败", true);
    }

    @Override
    public void queryBufbkDetailSuccess(List<EntityBufbkFeedback> entityFeedbacks) {
        dismissLoadingDialog();
        selectedEntityBufbk.setFeedbacks(entityFeedbacks);

        ToastTool.getInstance().shortLength(context, "查询通知详情成功", true);
        startActivity(BufbkDetailActivity.createIntent(context,selectedEntityBufbk));
    }

    @Override
    public void queryBufbkDetailFail(Exception se) {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context, "查询通知详情失败功", true);
    }
}
