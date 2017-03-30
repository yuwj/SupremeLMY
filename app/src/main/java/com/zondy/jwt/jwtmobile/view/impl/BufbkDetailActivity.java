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
import com.zondy.jwt.jwtmobile.entity.EntityBufbkFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityBufbk;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.IBufbkPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.BufbkPresenter;
import com.zondy.jwt.jwtmobile.ui.DividerItemDecoration;
import com.zondy.jwt.jwtmobile.ui.FullyGridLayoutManager;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IBufbkDetailView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BufbkDetailActivity extends BaseActivity implements IBufbkDetailView {

    public static final int REQ_CODE_FEEDBACK = 1;
    EntityBufbk entityBufbk;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rv_feedback_info)
    RecyclerView rvFeedbackInfo;
    List<EntityBufbkFeedback> feedbackDatas;
    CommonAdapter<EntityBufbkFeedback> feedbackAdapter;
    IBufbkPresenter bufbkPresenter;
    EntityUser userInfo;

    /**
     * 从列表中点进来
     * @param context
     * @param entityBufbk
     * @return
     */
    public static Intent createIntent(Context context, EntityBufbk entityBufbk) {
        Intent intent = new Intent(context, BufbkDetailActivity.class);
        intent.putExtra("entityBufbk", entityBufbk);
        return intent;
    }

    /**
     * 从通知中点进来
     * @param context
     * @param bufbkId
     * @return
     */
    public static Intent createIntent(Context context, String bufbkId) {
        Intent intent = new Intent(context, BufbkDetailActivity.class);
        intent.putExtra("bufbkId", bufbkId);
        return intent;
    }



    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_bufbk_detail;
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
        if((EntityBufbk.jszt_unaccept+"").equals(entityBufbk.getJszt()) ){
            bufbkPresenter.acceptBufbk(userInfo.getUserName(), CommonUtil.getDeviceId(context),userInfo.getCtname());
            showLoadingDialog("正在加载...");
        }

    }

    public void queryBufbkFeedbackDatas(){
        bufbkPresenter.queryBufbkFeedbackDatas(userInfo.getUserName(),CommonUtil.getDeviceId(context),entityBufbk.getId());
        showLoadingDialog("正在加载...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(context).inflate(R.menu.toolbar_bufbk_feedback,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.feedback:
                startActivityForResult(BufbkFeedbackActivity.createIntent(context,entityBufbk.getId()),REQ_CODE_FEEDBACK);
                break;
        }
        return true;
    }

    public void initParam() {
        userInfo = SharedTool.getInstance().getUserInfo(context);
        bufbkPresenter = new BufbkPresenter(this);
        Intent intent = getIntent();
        entityBufbk = (EntityBufbk) intent.getSerializableExtra("entityBufbk");
        feedbackDatas = new ArrayList<>();
        feedbackDatas.addAll(entityBufbk.getFeedbacks());
        feedbackAdapter = new CommonAdapter<EntityBufbkFeedback>(context,R.layout.item_bufbk_feedback,feedbackDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityBufbkFeedback entityFeedback, int position) {

                holder.setText(R.id.tv_feedback_str_info,entityFeedback.getFkwbxx());
                holder.setText(R.id.tv_feedback_user_and_time,"由 "+entityFeedback.getFkrxm()+" 于 "+entityFeedback.getFksj()+" 反馈");
                final List<String> mediaPaths = entityFeedback.getFbcMediaPaths();

                if(mediaPaths != null && mediaPaths.size()>0){
                    RecyclerView recyclerView = holder.getView(R.id.rv_feedback_medias);
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
    }

    public void initView() {
        initActionBar(toolbar,tvTitle,entityBufbk.getMc());
        tvTime.setText(entityBufbk.getBksj());
        tvContent.setText(entityBufbk.getBksy());
        rvFeedbackInfo.setLayoutManager(new LinearLayoutManager(context));
        rvFeedbackInfo.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));
        rvFeedbackInfo.setAdapter(feedbackAdapter);
    }

    public void initOperator() {
        queryBufbkFeedbackDatas();
    }

    @Override
    public void queryBufbkFeedbacksSuccess(List<EntityBufbkFeedback> entityBufbkFeedbacks) {
        dismissLoadingDialog();
        feedbackDatas.clear();
        feedbackDatas.addAll(entityBufbkFeedbacks);
        feedbackAdapter.notifyDataSetChanged();
    }

    @Override
    public void queryBufbkFeedbacksFail(Exception e) {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context,"查询布控反馈信息成功",true);
    }

    @Override
    public void acceptBufbkSuccess() {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context,"布控信息接收成功",true);
    }

    @Override
    public void acceptBufbkFail() {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context,"布控信息接收失败",true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){

        switch (requestCode){
            case REQ_CODE_FEEDBACK:
                queryBufbkFeedbackDatas();
                break;
        }
        }
    }
}
