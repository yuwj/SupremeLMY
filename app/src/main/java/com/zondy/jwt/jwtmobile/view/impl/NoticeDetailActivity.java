package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityNoticeFank;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;
import com.zondy.jwt.jwtmobile.ui.DividerItemDecoration;
import com.zondy.jwt.jwtmobile.ui.FullyGridLayoutManager;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.INoticeDetailView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NoticeDetailActivity extends BaseActivity implements INoticeDetailView {

    public static final int REQ_CODE_FANK = 1;
    EntityNotice entityNotice;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ll_notice_container)
    LinearLayout llNoticeContainer;
    @BindView(R.id.rv_fank_info)
    RecyclerView rvFankInfo;
    @BindView(R.id.rv_fuj_info)
    RecyclerView rvFujInfo;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    List<EntityNoticeFank> fankDatas;
    CommonAdapter<EntityNoticeFank> feedbackAdapter;
    List<String> fujDatas;
    CommonAdapter<String> fujAdapter;

    public static Intent createIntent(Context context, EntityNotice entityNotice) {
        Intent intent = new Intent(context, NoticeDetailActivity.class);
        intent.putExtra("entityNotice", entityNotice);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_notice_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
        initOperator();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_notice_fank,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.fank:
                startActivityForResult(NoticeFankActivity.createIntent(context,entityNotice.getId()), REQ_CODE_FANK);
                break;
        }
        return true;
    }

    public void initParam() {
        Intent intent = getIntent();
        entityNotice = (EntityNotice) intent.getSerializableExtra("entityNotice");
        fankDatas = new ArrayList<>();
        fankDatas.addAll(entityNotice.getFankList());
        feedbackAdapter = new CommonAdapter<EntityNoticeFank>(context,R.layout.item_notice_fank, fankDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityNoticeFank entityNoticeFank, int position) {

                holder.setText(R.id.tv_fankxx, entityNoticeFank.getFkxx());
                holder.setText(R.id.tv_fankr,entityNoticeFank.getFkrjh());
                holder.setText(R.id.tv_fank_time,entityNoticeFank.getFksj());
                final List<String> mediaPaths = entityNoticeFank.getDmtljList();

                if(mediaPaths != null && mediaPaths.size()>0){
                    RecyclerView recyclerView = holder.getView(R.id.rv_fank_medias);
                    CommonAdapter<String> mediaAdapter = new CommonAdapter<String>(context,R.layout.item_notice_media,mediaPaths) {
                        @Override
                        protected void convert(ViewHolder holder, String s, int position) {
                            ImageView ivTuisxx = holder.getView(R.id.iv_notice_image);
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
        fujDatas = new ArrayList<>();
        fujDatas.addAll(entityNotice.getDmtljList());
        fujAdapter = new CommonAdapter<String>(context,R.layout.item_notice_fuj,fujDatas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                ImageView ivFujType = holder.getView(R.id.iv_fuj_type);
                TextView tvFujName = holder.getView(R.id.tv_fuj_name);
                ImageView ivFujDownload = holder.getView(R.id.iv_fuj_download);
                tvFujName.setText(s);
                if(s.endsWith("jpg")){
                    ivFujType.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_caidan));
                }else{
                    ivFujType.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_caidan));
                }
                ivFujDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastTool.getInstance().shortLength(context,"下载",true);
                    }
                });
            }
        };
    }

    public void initView() {
        initActionBar(toolbar,tvTitle,entityNotice.getBt());
        tvTime.setText(entityNotice.getFbrq());
        tvContent.setText(entityNotice.getContent());
        rvFankInfo.setLayoutManager(new LinearLayoutManager(context));
        rvFankInfo.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));
        rvFankInfo.setAdapter(feedbackAdapter);

        rvFujInfo.setLayoutManager(new LinearLayoutManager(context));
        rvFujInfo.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));
        rvFujInfo.setAdapter(fujAdapter);
        scrollView.smoothScrollTo(0,0);
    }

    public void initOperator() {

    }

    @Override
    public void queryNoticeDetailSuccess(EntityNotice entityNotice) {

    }

    @Override
    public void queryNoticeDetailFail(Exception e) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){

        switch (requestCode){
            case REQ_CODE_FANK:
                EntityNoticeFank feedback = (EntityNoticeFank) data.getSerializableExtra("entityFeedback");
                if(feedback != null){
                    fankDatas.add(feedback);
                    feedbackAdapter.notifyDataSetChanged();
                }
                break;
        }
        }
    }
}
