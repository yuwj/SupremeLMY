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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityBufbk;
import com.zondy.jwt.jwtmobile.entity.EntityBufbkFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.IBufbkPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.BufbkPresenter;
import com.zondy.jwt.jwtmobile.ui.DividerItemDecoration;
import com.zondy.jwt.jwtmobile.ui.FullyGridLayoutManager;
import com.zondy.jwt.jwtmobile.ui.FullyLinearLayoutManager;
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


    @BindView(R.id.rv_bufbk_detail_info)
    RecyclerView rvBufbkDetailInfo;
    @BindView(R.id.rv_bufbk_dmt_info)
    RecyclerView rvDmtInfo;
    List<EntityBufbkFeedback> feedbackDatas;
    CommonAdapter<EntityBufbkFeedback> feedbackAdapter;

    List<EntityParam> bufbkDetailInfoDatas;
    CommonAdapter<EntityParam> bufbkDetailInfoAdapter;

    List<String> bufbkDuomtDatas;
    CommonAdapter<String> bufbkDuomtAdapter;
    IBufbkPresenter bufbkPresenter;
    EntityUser userInfo;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_fank_info)
    RecyclerView rvFankInfo;

    /**
     * 从列表中点进来
     *
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
     *
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
        if ((EntityBufbk.jszt_unaccept + "").equals(entityBufbk.getJszt())) {
            bufbkPresenter.acceptBufbk(userInfo.getUserName(), CommonUtil.getDeviceId(context), entityBufbk.getId(),userInfo.getCtname());
            showLoadingDialog("正在加载...");
        }

    }

    public void queryBufbkFeedbackDatas() {
        bufbkPresenter.queryBufbkFeedbackDatas(userInfo.getUserName(), CommonUtil.getDeviceId(context), entityBufbk.getId());
        showLoadingDialog("正在加载...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_bufbk_fank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.fank:
                startActivityForResult(BufbkFankActivity.createIntent(context, entityBufbk.getId()), REQ_CODE_FEEDBACK);
                break;
        }
        return true;
    }

    public void initParam() {
        userInfo = SharedTool.getInstance().getUserInfo(context);
        bufbkPresenter = new BufbkPresenter(this);
        Intent intent = getIntent();
        entityBufbk = (EntityBufbk) intent.getSerializableExtra("entityBufbk");

        //详情
        bufbkDetailInfoDatas = new ArrayList<>();
        if (entityBufbk != null) {
            //布控基本信息
//            bufbkDetailInfoDatas.add(new EntityParam("id", entityBufbk.getId()));
            bufbkDetailInfoDatas.add(new EntityParam("布控名称", entityBufbk.getMc()));
            bufbkDetailInfoDatas.add(new EntityParam("布控事由", entityBufbk.getBksy()));
            bufbkDetailInfoDatas.add(new EntityParam("请求单位", entityBufbk.getQqbkdw()));
            bufbkDetailInfoDatas.add(new EntityParam("联 系 人", entityBufbk.getLxr()));
            bufbkDetailInfoDatas.add(new EntityParam("联系电话", entityBufbk.getLxdh()));
            String buklx = "";
            if("1".equals(entityBufbk.getLx())){
                buklx = "智能布控";
            }else
            if("2".equals(entityBufbk.getLx())){
                buklx = "手动布控";
            }else
            if("3".equals(entityBufbk.getLx())){
                buklx = "堵控圈布控";
            }
            bufbkDetailInfoDatas.add(new EntityParam("布控类型:", buklx));
            bufbkDetailInfoDatas.add(new EntityParam("布 控 人", entityBufbk.getBkr()));
            bufbkDetailInfoDatas.add(new EntityParam("布控时间", entityBufbk.getBksj()));
            bufbkDetailInfoDatas.add(new EntityParam("布控单位", entityBufbk.getBkdwbm()));
            bufbkDetailInfoDatas.add(new EntityParam("布控状态", "1".equals(entityBufbk.getZt())?"在布控":"已撤控"));
            bufbkDetailInfoDatas.add(new EntityParam("撤 控 人", entityBufbk.getCkr()));
            bufbkDetailInfoDatas.add(new EntityParam("撤控时间", entityBufbk.getCksj()));
//            bufbkDetailInfoDatas.add(new EntityParam("多媒体路径", entityBufbk.getDmtlj()));

            //布控实时信息
            bufbkDetailInfoDatas.add(new EntityParam("卡点类型", entityBufbk.getKdlx()));//number,
            bufbkDetailInfoDatas.add(new EntityParam("卡点名称", entityBufbk.getKdmc()));//varchar2(50),
            bufbkDetailInfoDatas.add(new EntityParam("值守车车牌", entityBufbk.getCpz()));// varchar2(50),
//            bufbkDetailInfoDatas.add(new EntityParam("sgsj", entityBufbk.getSgsj()));//date,
//            bufbkDetailInfoDatas.add(new EntityParam("ztlgsj", entityBufbk.getZtlgsj()));//varchar2(4000),
//            bufbkDetailInfoDatas.add(new EntityParam("ztdgsj", entityBufbk.getZtdgsj()));//varchar2(4000),
            bufbkDetailInfoDatas.add(new EntityParam("在岗状态", (1+"").equals(entityBufbk.getFlag())?"在岗":"脱岗"));//number,
            bufbkDetailInfoDatas.add(new EntityParam("卡点经度", entityBufbk.getX()));//",entityBufbk.get()));//varchar2(20),
            bufbkDetailInfoDatas.add(new EntityParam("卡点纬度", entityBufbk.getY()));//",entityBufbk.get()));//varchar2(20),
            bufbkDetailInfoDatas.add(new EntityParam("卡点联系人", entityBufbk.getKdlxr()));// varchar2(20),//数据库字段 lxr
            bufbkDetailInfoDatas.add(new EntityParam("卡点联系电话", entityBufbk.getKdlxdh()));//varchar2(20),//数据库字段 lxdh
            bufbkDetailInfoDatas.add(new EntityParam("电台号码", entityBufbk.getDthm()));//varchar2(20),
//            bufbkDetailInfoDatas.add(new EntityParam("sendflag", entityBufbk.getSendflag()));//varchar2(20),
//            bufbkDetailInfoDatas.add(new EntityParam("xtsj", entityBufbk.getXtsj()));//date,
//            bufbkDetailInfoDatas.add(new EntityParam("jszt", entityBufbk.getJszt()));//number default 0
        }


        bufbkDetailInfoAdapter = new CommonAdapter<EntityParam>(context, R.layout.item_bufbk_detail, bufbkDetailInfoDatas) {

            @Override
            protected void convert(ViewHolder holder, EntityParam stringStringMap, int position) {
                holder.setText(R.id.tv_name, bufbkDetailInfoDatas.get(position).getName());
                holder.setText(R.id.tv_value, bufbkDetailInfoDatas.get(position).getValue());
            }
        };

//多媒体
        bufbkDuomtDatas = new ArrayList<>();
        bufbkDuomtDatas.addAll(entityBufbk.getFilePaths());
        bufbkDuomtAdapter = new CommonAdapter<String>(context, R.layout.item_bufbk_media, bufbkDuomtDatas) {

            @Override
            protected void convert(ViewHolder holder, String str, int position) {
                ImageView ivBufbkImage = holder.getView(R.id.iv_bufbk_image);
                Glide.with(mContext)
                        .load(str)
                        .placeholder(R.drawable.ic_handlejingq_wait_img)
                        .error(R.drawable.ic_img_load_fail)
                        .crossFade()
                        .into(ivBufbkImage);
            }
        };
//反馈
        feedbackDatas = new ArrayList<>();
        feedbackDatas.addAll(entityBufbk.getFeedbacks());
        feedbackAdapter = new CommonAdapter<EntityBufbkFeedback>(context, R.layout.item_bufbk_feedback, feedbackDatas) {
            @Override
            protected void convert(ViewHolder holder, EntityBufbkFeedback entityFeedback, int position) {

                holder.setText(R.id.tv_feedback_str_info, entityFeedback.getFkwbxx());
                holder.setText(R.id.tv_feedback_user_and_time, "由 " + entityFeedback.getFkrxm() + " 于 " + entityFeedback.getFksj() + " 反馈");
                final List<String> mediaPaths = entityFeedback.getFbcMediaPaths();

                if (mediaPaths != null && mediaPaths.size() > 0) {
                    RecyclerView recyclerView = holder.getView(R.id.rv_feedback_medias);
                    CommonAdapter<String> mediaAdapter = new CommonAdapter<String>(context, R.layout.item_bufbk_media, mediaPaths) {
                        @Override
                        protected void convert(ViewHolder holder, String s, int position) {
                            ImageView ivBufbkImage = holder.getView(R.id.iv_bufbk_image);
                            Glide.with(mContext)
                                    .load(s)
                                    .placeholder(R.drawable.ic_handlejingq_wait_img)
                                    .error(R.drawable.ic_img_load_fail)
                                    .crossFade()
                                    .into(ivBufbkImage);
                        }
                    };
                    recyclerView.setLayoutManager(new FullyGridLayoutManager(context, 4));
                    recyclerView.setAdapter(mediaAdapter);
                    mediaAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                            startActivity(JingqImageEditActivity.createIntent(context, mediaPaths, position, false));
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
        initActionBar(toolbar, tvTitle, entityBufbk.getMc());
        rvBufbkDetailInfo.setLayoutManager(new FullyLinearLayoutManager(context));
        rvBufbkDetailInfo.setAdapter(bufbkDetailInfoAdapter);
        rvBufbkDetailInfo.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        rvDmtInfo.setLayoutManager(new FullyGridLayoutManager(context,4));
        rvDmtInfo.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        rvDmtInfo.setAdapter(bufbkDuomtAdapter);
        rvFankInfo.setLayoutManager(new FullyLinearLayoutManager(context));
        rvFankInfo.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        rvFankInfo.setAdapter(feedbackAdapter);
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
        ToastTool.getInstance().shortLength(context, "查询布控反馈信息成功", true);
    }

    @Override
    public void acceptBufbkSuccess() {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context, "布控信息接收成功", true);
    }

    @Override
    public void acceptBufbkFail() {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context, "布控信息接收失败", true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQ_CODE_FEEDBACK:
                    queryBufbkFeedbackDatas();
                    break;
            }
        }
    }

    /**
     * 定义的属性实体,用来在字段很多的情况下,通过recycleView来展示信息
     */
    class EntityParam {
        String name;
        String value;

        EntityParam(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
