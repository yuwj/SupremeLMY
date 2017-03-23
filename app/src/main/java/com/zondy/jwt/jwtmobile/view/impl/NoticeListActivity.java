package com.zondy.jwt.jwtmobile.view.impl;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.INoticePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.NoticePresenter;
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
    CommonAdapter noticeAdapter;
    INoticePresenter noticePresenter;
    EntityUser userInfo;

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_notice_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        ButterKnife.bind(this);
    }

    public void initParam() {
        userInfo = SharedTool.getInstance().getUserInfo(context);
        noticePresenter = new NoticePresenter(this);
        noticeDatas = new ArrayList<>();
        noticeAdapter = new CommonAdapter(context, R.layout.item_notice_list, noticeDatas) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {

            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }
        };
        noticeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                EntityNotice notice = noticeDatas.get(position);
                String jh = userInfo.getUserName();
                String simid = CommonUtil.getDeviceId(context);
                noticePresenter.queryNoticeDetail(jh, simid, notice.getId());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    public void initView() {
        initActionBar(toolbar,tvTitle,"通知公告");
        xrvNoticeList.setLayoutManager(new LinearLayoutManager(context));
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
        noticePresenter.queryNoticeList(EntityNotice.NOTICE_TYPE_TONGZGG, userInfo.getUserName(), CommonUtil.getDeviceId(context));

    }

    @Override
    public void queryNoticeListSuccess(List<EntityNotice> entityNoticeList) {
        ToastTool.getInstance().shortLength(context, "查询通知列表成功" + entityNoticeList.size(), true);
        if (entityNoticeList != null && entityNoticeList.size() > 0) {
            noticeDatas.clear();
            noticeDatas.addAll(entityNoticeList);
            noticeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void queryNoticeListFail(Exception e) {
        ToastTool.getInstance().shortLength(context, "查询通知列表失败", true);
    }

    @Override
    public void queryNoticeDetailSuccess(EntityNotice entityNotice) {

        ToastTool.getInstance().shortLength(context, "查询通知详情成功", true);
    }

    @Override
    public void queryNoticeDetailFail(Exception se) {
        ToastTool.getInstance().shortLength(context, "查询通知详情失败功", true);
    }
}
