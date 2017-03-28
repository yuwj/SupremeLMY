package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityNotice;
import com.zondy.jwt.jwtmobile.ui.DividerItemDecoration;
import com.zondy.jwt.jwtmobile.ui.FullyGridLayoutManager;
import com.zondy.jwt.jwtmobile.view.INoticeDetailView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NoticeDetailActivity extends BaseActivity implements INoticeDetailView {

    public static final int REQ_CODE_FEEDBACK = 1;
    EntityNotice entityNotice;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.sv_notice_container)
    ScrollView svNoticeContainer;
    @BindView(R.id.rv_feedback_info)
    RecyclerView rvFeedbackInfo;
    List<EntityFeedback> feedbackDatas;
    CommonAdapter<EntityFeedback> feedbackAdapter;

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
        new MenuInflater(context).inflate(R.menu.toolbar_tuisxx_feedback,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.feedback:
                startActivityForResult(NoticeFeedbackActivity.createIntent(context,entityNotice.getId()),REQ_CODE_FEEDBACK);
                break;
        }
        return true;
    }

    public void initParam() {
        Intent intent = getIntent();
        entityNotice = (EntityNotice) intent.getSerializableExtra("entityNotice");
        feedbackDatas = new ArrayList<>();
        feedbackDatas.addAll(entityNotice.getFeedbacks());
        feedbackAdapter = new CommonAdapter<EntityFeedback>(context,R.layout.item_tuisxx_feedback,feedbackDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityFeedback entityFeedback, int position) {

                holder.setText(R.id.tv_feedback_str_info,entityFeedback.getFbcStringInfo());
                holder.setText(R.id.tv_feedback_user_and_time,"由 "+entityFeedback.getFbcUser()+" 于 "+entityFeedback.getFbcTime()+" 反馈");
                final List<String> mediaPaths = entityFeedback.getFbcMediaPaths();

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
    }

    public void initView() {
        initActionBar(toolbar,tvTitle,entityNotice.getTitle());
        tvTime.setText(entityNotice.getTime());
        tvContent.setText(entityNotice.getContent());
        rvFeedbackInfo.setLayoutManager(new LinearLayoutManager(context));
        rvFeedbackInfo.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));
        rvFeedbackInfo.setAdapter(feedbackAdapter);
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
            case REQ_CODE_FEEDBACK:
                EntityFeedback feedback = (EntityFeedback) data.getSerializableExtra("entityFeedback");
                if(feedback != null){
                    feedbackDatas.add(feedback);
                    feedbackAdapter.notifyDataSetChanged();
                }
                break;
        }
        }
    }
}
