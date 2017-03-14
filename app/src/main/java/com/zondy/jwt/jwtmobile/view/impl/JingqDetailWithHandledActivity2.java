package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
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
import com.zondy.jwt.jwtmobile.entity.EntityJingq;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.IJingqHandlePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.JingqHandlePresenterImpl;
import com.zondy.jwt.jwtmobile.ui.DividerItemDecoration;
import com.zondy.jwt.jwtmobile.ui.FullyLinearLayoutManager;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IJingqDetailWithHandledView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 已处理警情详情界面
 */
public class JingqDetailWithHandledActivity2 extends BaseActivity implements IJingqDetailWithHandledView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_baoj_info)
    RecyclerView rvBaojInfo;
    @BindView(R.id.rv_chuj_info)
    RecyclerView rvChujInfo;
    @BindView(R.id.rv_media)
    RecyclerView rvMedia;

    EntityJingq entityJingq;
    IJingqHandlePresenter jingqHandlePresenter;
    EntityUser user;
    CommonAdapter<String> adapterImages;
    List<String> imageDatas;
    CommonAdapter<JingqProperty> adapterJiejProperties;
    List<JingqProperty> jiejPropertyDatas;
    CommonAdapter<JingqProperty> adapterChujProperties;
    List<JingqProperty> chujPropertyDatas;

    public static Intent createIntent(Context context, EntityJingq jingq) {
        Intent intent = new Intent(context, JingqDetailWithHandledActivity2.class);
        intent.putExtra("entityJingq", jingq);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_jingq_detail_with_handled2;
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
        getMenuInflater().inflate(R.menu.toolbar_jingq_detail, menu);
        return true;
    }

    public void initParam() {
        entityJingq = (EntityJingq) getIntent().getSerializableExtra("entityJingq");
        jingqHandlePresenter = new JingqHandlePresenterImpl(this);
        user = SharedTool.getInstance().getUserInfo(context);
        imageDatas = new ArrayList<>();


        adapterImages = new CommonAdapter<String>(context, R.layout.item_jingq_handled_images, imageDatas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                Glide.with(mContext)
                        .load(s)
                        .placeholder(R.drawable.ic_handlejingq_wait_img)
                        .crossFade()
                        .into((ImageView) holder.getView(R.id.iv_handled_jingq_image));
            }
        };


        jiejPropertyDatas = new ArrayList<>();
        chujPropertyDatas = new ArrayList<>();
        adapterJiejProperties = new CommonAdapter<JingqProperty>(context, R.layout.item_jingq_detail_with_handled, jiejPropertyDatas) {
            @Override
            protected void convert(ViewHolder holder, JingqProperty jingqProperty, int position) {
                holder.setText(R.id.tv_name, jingqProperty.name);
                holder.setText(R.id.tv_value, jingqProperty.value);
            }
        };
        adapterChujProperties = new CommonAdapter<JingqProperty>(context, R.layout.item_jingq_detail_with_handled, chujPropertyDatas) {
            @Override
            protected void convert(ViewHolder holder, JingqProperty jingqProperty, int position) {
                holder.setText(R.id.tv_name, jingqProperty.name);
                holder.setText(R.id.tv_value, jingqProperty.value);
            }
        };
    }

    public void initView() {
        initActionBar(toolbar, tvTitle, "已处理警情详情");
        updateJingqView(entityJingq);
        rvMedia.setLayoutManager(new GridLayoutManager(context, 3));
        rvMedia.setAdapter(adapterImages);

        rvBaojInfo.setLayoutManager(new FullyLinearLayoutManager(context));
        rvBaojInfo.setAdapter(adapterJiejProperties);
        rvBaojInfo.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL_LIST));
        rvChujInfo.setLayoutManager(new FullyLinearLayoutManager(context));
        rvChujInfo.setAdapter(adapterChujProperties);
        rvBaojInfo.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL_LIST));
    }


    public void initOperator() {
    }

    public void updateJingqView(EntityJingq jingq) {


        if (entityJingq != null && !TextUtils.isEmpty(entityJingq.getFilesPath())) {
            String[] filePaths = entityJingq.getFilesPath().split(",");
            for (String s : filePaths) {
                imageDatas.add(s);
            }
            adapterImages.notifyDataSetChanged();
        }

        if (entityJingq != null) {

            String baojsj = entityJingq.getBaojsj();
            JingqProperty p1 = new JingqProperty("报警时间", baojsj, "报警信息", "", 0);
            String baojr = entityJingq.getBaojr();
            JingqProperty p2 = new JingqProperty("报警人", baojr, "报警信息", "", 0);
            String baojrdh = entityJingq.getBaojrdh();
            JingqProperty p3 = new JingqProperty("报警人电话", baojrdh, "报警信息", "", 0);
            String baojnr = entityJingq.getBaojnr();
            JingqProperty p4 = new JingqProperty("报警内容", baojnr, "报警信息", "", 0);
            String baojdz = entityJingq.getBaojdz();
            JingqProperty p5 = new JingqProperty("报警地址", baojdz, "报警信息", "", 0);

            jiejPropertyDatas.add(p1);
            jiejPropertyDatas.add(p2);
            jiejPropertyDatas.add(p3);
            jiejPropertyDatas.add(p4);
            jiejPropertyDatas.add(p5);
            adapterJiejProperties.notifyDataSetChanged();


            String chujsj = entityJingq.getChujsj();
            JingqProperty p6 = new JingqProperty("接警时间", chujsj, "处警信息", "", 0);
            String daodsj = entityJingq.getDaodsj();
            JingqProperty p7 = new JingqProperty("到达时间", daodsj, "处警信息", "", 0);
            String fanksj = entityJingq.getFanksj();
            JingqProperty p8 = new JingqProperty("反馈时间", fanksj, "处警信息", "", 0);
//            String chujrjh = entityJingq.getChujrjh();
//            JingqProperty p9 = new JingqProperty("处警人警号",chujrjh,"处警信息","",0);
//            String chujrxm = entityJingq.getChujrxm();
//            JingqProperty p10 = new JingqProperty("处警人姓名",chujrxm,"处警信息","",0);
            String anjlb = entityJingq.getAjlb();
            JingqProperty p11 = new JingqProperty("案件类别", anjlb, "处警信息", "", 0);
            String anjlx = entityJingq.getAjlx();
            JingqProperty p12 = new JingqProperty("案件类型", anjlx, "处警信息", "", 0);
            String anjxl = entityJingq.getAjxl();
            JingqProperty p13 = new JingqProperty("案件细类", anjxl, "处警信息", "", 0);
            String fanknr = entityJingq.getFknr();
            JingqProperty p14 = new JingqProperty("反馈内容", fanknr, "处警信息", "", 0);
            String mediaPaths = entityJingq.getFilesPath();

            chujPropertyDatas.add(p6);
            chujPropertyDatas.add(p7);
            chujPropertyDatas.add(p8);
            chujPropertyDatas.add(p11);
            chujPropertyDatas.add(p12);
            chujPropertyDatas.add(p13);
            chujPropertyDatas.add(p14);
            adapterChujProperties.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.refresh:
                String jingqid = entityJingq.getJingqid();
                String jh = user.getUserName();
                String simid = CommonUtil.getDeviceId(context);
                jingqHandlePresenter.reloadJingqWithJingqHandled(jingqid, jh, simid);
                break;


            default:
                break;
        }
        return true;
    }


    @Override
    public void loadJingqSuccess(EntityJingq jingq) {
        dismissLoadingDialog();
        this.entityJingq = jingq;
        updateJingqView(jingq);
    }

    @Override
    public void loadJIngqFalied(Exception e) {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context, e.getMessage(), true);
    }


    @OnClick({R.id.tv_reload})
    public void onClick(View view) {
        String jingqid = entityJingq.getJingqid();
        String jh = user.getUserName();
        String simid = CommonUtil.getDeviceId(context);
        switch (view.getId()) {
            case R.id.tv_reload:
                jingqHandlePresenter.reloadJingqWithJingqHandled(jingqid, jh, simid);
                showLoadingDialog();
                break;
        }
    }

    class JingqProperty {
        String typeName;//类型,接警信息,处警信息,处警图片
        String typeValue;//类型对应的value
        String name;//属性名称
        String value;//属性值
        int showType;//0-item,1-divider

        public JingqProperty(String name, String value, String typeName, String typeValue, int showType) {
            this.name = name;
            this.value = value;
            this.typeName = typeName;
            this.typeValue = typeValue;
            this.showType = showType;
        }
    }

}
