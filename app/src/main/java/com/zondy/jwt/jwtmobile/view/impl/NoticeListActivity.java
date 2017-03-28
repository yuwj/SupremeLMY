package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;
import com.zondy.jwt.jwtmobile.entity.EntityPage;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.INoticePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.NoticePresenter;
import com.zondy.jwt.jwtmobile.ui.DividerItemDecoration;
import com.zondy.jwt.jwtmobile.ui.FullyGridLayoutManager;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.INoticeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeListActivity extends BaseActivity implements INoticeView {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.xrv_notice_list)
    XRecyclerView xrvNoticeList;
    @BindView(R.id.activity_notice_list)
    LinearLayout activityNoticeList;
    List<EntityNotice> noticeDatas;
    CommonAdapter<EntityNotice> noticeAdapter;
    INoticePresenter noticePresenter;
    EntityUser userInfo;
    int noticeType;
    EntityPage entityPage;
    EntityNotice selectedEntityNotice;

    public static Intent createIntent(Context context,int noticeType) {
        Intent intent = new Intent(context, NoticeListActivity.class);
        intent.putExtra("noticeType",noticeType);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_notice_list;
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
        noticeType = intent.getIntExtra("noticeType",EntityNotice.NOTICE_TYPE_TONGZGG);
        entityPage = new EntityPage();
        userInfo = SharedTool.getInstance().getUserInfo(context);
        noticePresenter = new NoticePresenter(this);
        noticeDatas = new ArrayList<>();
        noticeAdapter = new CommonAdapter<EntityNotice>(context, R.layout.item_notice_list, noticeDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityNotice entityNotice, int position) {
                holder.setText(R.id.tv_title,"标题: "+entityNotice.getTitle());
                holder.setText(R.id.tv_content,"内容: "+entityNotice.getContent());
                holder.setText(R.id.tv_time,"时间: "+entityNotice.getTime());

                final List<String> mediaPaths = entityNotice.getFilePaths();

                if(mediaPaths != null && mediaPaths.size()>0){
                    RecyclerView recyclerView = holder.getView(R.id.rv_feedback_medias);
                    CommonAdapter<String> mediaAdapter = new CommonAdapter<String>(context,R.layout.item_tuisxx_media,mediaPaths) {
                        @Override
                        protected void convert(ViewHolder holder, String s, int position) {
                            ImageView ivTuisxx = holder.getView(R.id.iv_tuisxx_image);
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

        noticeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                selectedEntityNotice = noticeDatas.get(position-1);
                String jh = userInfo.getUserName();
                String simid = CommonUtil.getDeviceId(context);
                noticePresenter.queryNoticeDetail(jh, simid, selectedEntityNotice.getId());
                showLoadingDialog("正在加载...");
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    public void initView() {
        String title = "通知";
        switch (noticeType){
            case EntityNotice.NOTICE_TYPE_BUK:
                title = "布控布防";
                break;
            case EntityNotice.NOTICE_TYPE_TONGZGG:
                title = "通知公告";
                break;

        }
        initActionBar(toolbar,tvTitle,title);
        xrvNoticeList.setLayoutManager(new LinearLayoutManager(context));
        xrvNoticeList.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));
        xrvNoticeList.setAdapter(noticeAdapter);
        xrvNoticeList.setLoadingMoreEnabled(false);
        xrvNoticeList.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        noticePresenter.queryNoticeList(noticeType, userInfo.getUserName(), CommonUtil.getDeviceId(context),userInfo.getZzjgdm(),entityPage.getPageSize(),entityPage.getPageNo());
        showLoadingDialog("正在加载...");
    }

    @Override
    public void queryNoticeListSuccess(List<EntityNotice> entityNoticeList) {
        dismissLoadingDialog();
        xrvNoticeList.refreshComplete();
        xrvNoticeList.loadMoreComplete();
        ToastTool.getInstance().shortLength(context, "查询通知列表成功" + entityNoticeList.size(), true);
        if (entityNoticeList != null && entityNoticeList.size() > 0) {
            noticeDatas.clear();
            noticeDatas.addAll(entityNoticeList);
            noticeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void queryNoticeListFail(Exception e) {
        dismissLoadingDialog();
        xrvNoticeList.refreshComplete();
        xrvNoticeList.loadMoreComplete();
        ToastTool.getInstance().shortLength(context, "查询通知列表失败", true);
    }

    @Override
    public void queryNoticeDetailSuccess(List<EntityFeedback> entityFeedbacks) {
        dismissLoadingDialog();
        selectedEntityNotice.setFeedbacks(entityFeedbacks);

        ToastTool.getInstance().shortLength(context, "查询通知详情成功", true);
        startActivity(NoticeDetailActivity.createIntent(context,selectedEntityNotice));
    }

    @Override
    public void queryNoticeDetailFail(Exception se) {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context, "查询通知详情失败功", true);
    }
}
