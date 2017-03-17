package com.zondy.jwt.jwtmobile.view.impl;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.base.BaseCommonAdapter;
import com.zondy.jwt.jwtmobile.base.BaseViewHolder;
import com.zondy.jwt.jwtmobile.entity.EntityJingq;
import com.zondy.jwt.jwtmobile.entity.EntityMedia;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.entity.EntityZD;
import com.zondy.jwt.jwtmobile.presenter.IJingqHandlePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.JingqHandlePresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.GlideImageLoader;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IJingqhandleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class JingqHandleActivity extends BaseActivity implements IJingqhandleView {

    @BindView(R.id.tv_add_media)
    TextView tv_add_media;
    @BindView(R.id.sp_anjian_leibie)
    Spinner sp_anjian_leibie;// 案件类别
    @BindView(R.id.sp_anjian_leixing)
    Spinner sp_anjian_leixing;// 案件类型
    @BindView(R.id.sp_anjian_xilei)
    Spinner sp_anjian_xilei;// 案件细类
    @BindView(R.id.sp_ksxz_ajcl)
    Spinner sp_ksxz_ajcl;// 警情处理结果快速选择
    @BindView(R.id.iv_add_img)
    ImageView iv_add_img;// 添加图片
//    @BindView(R.id.et_chujing_result_content)
//    EditText et_chujing_result_content;// 案件处理结果描述
    @BindView(R.id.tv_confirm_handle)
    TextView tv_confirm_handle;// 确认处理
    @BindView(R.id.rv_media)
    RecyclerView rv_media;
    public final int imgCountLimit = 4;

    EntityJingq entityJingq;
    IJingqHandlePresenter jingqHandlePresenter;
    EntityUser user;

    List<EntityZD> ajlbDatas;
    BaseCommonAdapter<EntityZD> ajlbAdapter;
    List<EntityZD> ajlxDatas;
    BaseCommonAdapter<EntityZD> ajlxAdapter;
    List<EntityZD> ajxlDatas;
    BaseCommonAdapter<EntityZD> ajxlAdapter;
    List<EntityZD> ajclDatas;
    BaseCommonAdapter<EntityZD> ajclAdapter;

    //    String ajlx = "";
    EntityZD anjlx;
    //    String ajlb = "";
    EntityZD anjlb;
    //    String ajxl = "";
    EntityZD anjxl;
    boolean isAnjlbFirstShow = true;// 是否是第一次显示，是的话下拉框需要需要选中到制定的index
    boolean isAnjlxFistShow = true;// 是否是第一次显示，是的话下拉框需要需要选中到制定的index
    boolean ajxl_flag = true;// 是否是第一次显示，是的话下拉框需要需要选中到制定的index
    List<EntityMedia> medias;

    List<EntityZD> allJingqTypes;
    String result;
    CommonAdapter<String> adapterImages;
    List<String> imageDatas;
    private final int REQ_CODE_EDIT_IMAGE = 1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static Intent createIntent(Context context, EntityJingq jingq) {
        Intent intent = new Intent(context, JingqHandleActivity.class);
        intent.putExtra("entityJingq", jingq);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {

        return R.layout.activity_jingqhandle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
        initOperator();
    }

    public void initParam() {
        entityJingq = (EntityJingq) getIntent().getSerializableExtra("entityJingq");
        jingqHandlePresenter = new JingqHandlePresenterImpl(this);
        user = SharedTool.getInstance().getUserInfo(context);

        allJingqTypes = new ArrayList<EntityZD>();
        ajlbDatas = new ArrayList<EntityZD>();
        ajlxDatas = new ArrayList<EntityZD>();
        ajxlDatas = new ArrayList<EntityZD>();
        ajclDatas = new ArrayList<EntityZD>();

        imageDatas = new ArrayList<>();
        imageDatas.add(getResourceUri(R.drawable.ic_handlejingq_add_img));
        adapterImages = new CommonAdapter<String>(context, R.layout.item_jingq_handled_images, imageDatas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                Glide.with(mContext)
                        .load(s)
                        .crossFade()
                        .into((ImageView) holder.getView(R.id.iv_handled_jingq_image));
            }
        };
        adapterImages.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                String imgPath = adapterImages.getDatas().get(position);
                if(getResourceUri(R.drawable.ic_handlejingq_add_img).equals(imgPath)){
                    //添加图片
                    addImg();
                }else{
                    //查看图片
                    if(imageDatas.contains(getResourceUri(R.drawable.ic_handlejingq_add_img))){
                        imageDatas.remove(getResourceUri(R.drawable.ic_handlejingq_add_img));
                    }
                    startActivityForResult(JingqImageEditActivity.createIntent(context, imageDatas, position, true), REQ_CODE_EDIT_IMAGE);

                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    public void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        updateJingqView(entityJingq);
        rv_media.setLayoutManager(new GridLayoutManager(context, 4));
        rv_media.setAdapter(adapterImages);
    }


    public void initOperator() {
        String jh = user.getUserName();
        String simid = CommonUtil.getDeviceId(context);
        jingqHandlePresenter.queryAllJingqTypes(jh, simid);
        jingqHandlePresenter.queryAllJingqKuaisclTypes(jh, simid);

    }

    public void updateJingqView(EntityJingq jingq) {


        sp_anjian_leibie
                .setAdapter(ajlbAdapter = new BaseCommonAdapter<EntityZD>(
                        context, ajlbDatas, R.layout.item_jingqhandle_sp) {
                    @Override
                    public void convert(BaseViewHolder holder, EntityZD item) {
                        holder.setText(R.id.tv_value, item.getMc());
                    }
                });
        sp_anjian_leixing
                .setAdapter(ajlxAdapter = new BaseCommonAdapter<EntityZD>(
                        context, ajlxDatas, R.layout.item_jingqhandle_sp) {
                    @Override
                    public void convert(BaseViewHolder holder, EntityZD item) {
                        holder.setText(R.id.tv_value, item.getMc());
                    }
                });

        sp_anjian_xilei
                .setAdapter(ajxlAdapter = new BaseCommonAdapter<EntityZD>(
                        context, ajxlDatas, R.layout.item_jingqhandle_sp) {
                    @Override
                    public void convert(BaseViewHolder holder, EntityZD item) {
                        holder.setText(R.id.tv_value, item.getMc());
                    }
                });

        sp_ksxz_ajcl.setAdapter(ajclAdapter = new BaseCommonAdapter<EntityZD>(
                context, ajclDatas, R.layout.item_jingqhandle_sp) {
            @Override
            public void convert(BaseViewHolder holder, EntityZD item) {
                holder.setText(R.id.tv_value, item.getMc());
            }
        });

        sp_anjian_leibie
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        anjlb = ajlbAdapter.getItem(arg2);
                        List<EntityZD> lxs = getChildJingqType(allJingqTypes, anjlb);

                        if (lxs != null) {
                            ajlxDatas.clear();
                            ajlxDatas.addAll(lxs);
                            ajlxAdapter.notifyDataSetChanged();


                            if (isAnjlxFistShow) {// 是否是案件类型第一次显示，是的话需要指定到对应的index
                                String ajlxMc = entityJingq.getAjlx();

                                int ajlxIndex = -1;
                                for (int i = 0; i < ajlxDatas.size(); i++) {
                                    EntityZD ajlx_tmp = ajlxDatas.get(i);

                                    if (ajlx_tmp.getMc().equals(ajlxMc)) {
                                        ajlxIndex = i;
                                        break;
                                    }
                                }
                                if (ajlxIndex > -1) {
                                    sp_anjian_leixing.setSelection(ajlxIndex);
                                }

                                isAnjlxFistShow = false;
                            } else {
                                if (lxs != null && lxs.size() > 0) {

                                    sp_anjian_leixing.setSelection(0);
                                    anjlx = ajlxDatas.get(0);
                                } else {
                                    ajlxAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        ajlxDatas.clear();
                        ajlxAdapter.notifyDataSetChanged();
                        ajxlDatas.clear();
                        ajxlAdapter.notifyDataSetChanged();
                    }
                });

        sp_anjian_leixing
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        EntityZD spOption = ajlxAdapter.getItem(arg2);

                        List<EntityZD> xls = getChildJingqType(allJingqTypes, anjlx);

                        if (xls != null) {
                            ajxlDatas.clear();
                            ajxlDatas.addAll(xls);
                            ajxlAdapter.notifyDataSetChanged();

                            if (ajxl_flag) {
                                String ajxl = entityJingq.getAjxl();

                                int ajxlIndex = -1;
                                for (int i = 0; i < ajxlDatas.size(); i++) {
                                    EntityZD ajxl_tmp = ajxlDatas.get(i);

                                    if (ajxl_tmp.getMc().equals(ajxl)) {
                                        ajxlIndex = i;
                                        break;
                                    }
                                }
                                if (ajxlIndex > -1) {
                                    sp_anjian_xilei.setSelection(ajxlIndex);
                                }

                                ajxl_flag = false;
                            } else {
                                if (xls != null && xls.size() > 0) {
                                    sp_anjian_xilei.setSelection(0);
                                    anjxl = ajxlDatas.get(0);
                                }
                            }
                        }

                        // ========================


                        // ========================

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                        ajxlDatas.clear();
                        ajxlAdapter.notifyDataSetChanged();
                    }
                });

        sp_anjian_xilei
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        EntityZD spOption = ajxlAdapter.getItem(arg2);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

        sp_ksxz_ajcl
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        EntityZD spOption = ajclAdapter.getItem(arg2);
//                        et_chujing_result_content.setText(spOption.getMc());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }


    /**
     * 获取子警情类型
     *
     * @param parentZDDatas
     * @param selectedZD
     * @return
     */
    public List<EntityZD> getChildJingqType(List<EntityZD> parentZDDatas, EntityZD selectedZD) {
        List<EntityZD> chidrenZD = new ArrayList<EntityZD>();
        if (selectedZD != null && parentZDDatas.size() > 0) {
            if (parentZDDatas != null && parentZDDatas.size() > 0) {
                for (EntityZD zd : parentZDDatas) {
                    if (selectedZD.getBm().equals(zd.getPid())) {
                        chidrenZD.add(zd);
                    }
                }
            }
        }
        return chidrenZD;

    }


    @Override
    public void handleJingqSuccess(EntityJingq jingq) {

        ToastTool.getInstance().shortLength(context, "处理成功", true);
        startActivity(JingqDetailWithHandledActivity2.createIntent(context,jingq));
        this.finish();
    }

    @Override
    public void handleJingqFalied(Exception e) {
        ToastTool.getInstance().shortLength(context, "处理失败," + e.getMessage(), true);
    }

    @Override
    public void updateJingqTypes(List<EntityZD> jignqtypes) {
        if (jignqtypes != null
                ) {
            allJingqTypes.clear();
            allJingqTypes.addAll(jignqtypes);
            List<EntityZD> lbs = getChildJingqType(allJingqTypes, new EntityZD("0", "", "0"));
            if (lbs != null && lbs.size() > 0) {
                ajlbDatas.clear();
                ajlbDatas.addAll(lbs);
                ajlbAdapter.notifyDataSetChanged();


                if (isAnjlbFirstShow) {// 是否是案件类别第一次显示，是的话需要指定到对应的index
                    String ajlbbm = entityJingq.getAjlbbm();

                    int ajlbIndex = -1;
                    for (int i = 0; i < ajlbDatas.size(); i++) {
                        EntityZD ajlb_tmp = ajlbDatas.get(i);

                        if (ajlb_tmp.getBm().equals(ajlbbm)) {
                            ajlbIndex = i;
                            break;
                        }
                    }
                    if (ajlbIndex > -1) {
                        sp_anjian_leibie.setSelection(ajlbIndex);
                    }

                    isAnjlbFirstShow = false;
                }
            }
        } else {
            ToastTool.getInstance().shortLength(context, "获取所有警情类型失败", true);
        }
    }

    @Override
    public void updateJingqKuaisclTypes(List<EntityZD> jignqKuaiscltypes) {
        ajclDatas.clear();
        ajclDatas.addAll(jignqKuaiscltypes);
        ajclAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void updateSelectedImages(final List<String> imagePaths) {
//        rv_media.setLayoutManager(new GridLayoutManager(context,3));
//        CommonAdapter adapter = null;
//        rv_media.setAdapter(adapter = new CommonAdapter(context,R.layout.item_jingq_handled_images,imagePaths) {
//            @Override
//            protected void convert(ViewHolder holder, Object o, int position) {
//                ImageView iv = holder.getView(R.id.iv_handled_jingq_image);
//                final String path = imagePaths.get(position);
//                Glide.with(context).load(path).into(iv);
//            }
//        });
//        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                if(position >= imagePaths.size()){
//                    //添加图片
//                }else{
//                    //查看图片
//                }
//            }
//
//            @Override
//            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
//                return false;
//            }
//        });
//        rv_media.addItemDecoration(new DividerGridItemDecoration(context));
//
//    }

    @OnClick({R.id.tv_confirm_handle, R.id.tv_add_media})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm_handle:
                String jingyid = user.getUserId();
                String jingqid = entityJingq.getJingqid() + "";
                String filesPath = entityJingq.getFilesPath();
                EntityZD chuljgZD = (EntityZD) sp_ksxz_ajcl.getSelectedItem();
                final String chuljg = chuljgZD == null ? "" : chuljgZD.getMc();
//                String chuljgms = et_chujing_result_content.getText()
//                        .toString().trim();
                String ajlb = "-1";
                if (anjlb != null) {
                    ajlb = anjlb.getBm();
                }
                String ajlx = "-1";
                if (anjlx != null) {
                    ajlx = anjlx.getBm();
                }
                String ajxl = "-1";
                if (anjxl != null) {
                    ajxl = anjxl.getBm();
                }
                String jh = user.getUserName();
                String simid = CommonUtil.getDeviceId(context);
                jingqHandlePresenter.jingqHandle(jingyid, jingqid, chuljg, ajlb, ajlx, ajxl, "", filesPath, jh, simid);
                break;
            case R.id.tv_add_media:
                //==========================
                //以下是rxGalleryFinal写法,但是引入此模块会导致地图无法加载,故舍弃.
//                RxGalleryFinal
//                        .with(JingqHandleActivity.this)
//                        .image()
//                        .multiple()
//                        .maxSize(8)
//                        .imageLoader(ImageLoaderType.GLIDE)
//                        .subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
//                            @Override
//                            protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
//                                List<MediaBean> medias = imageMultipleResultEvent.getResult();
//                                StringBuffer sb = new StringBuffer();
//                                imageDatas.clear();
//                                for (MediaBean bean : medias) {
//                                    String filePath = bean.getOriginalPath();
//                                    sb.append(filePath + ",");
//                                    imageDatas.add(filePath);
//                                }
//                                entityJingq.setFilesPath(sb.toString());
//                                adapterImages.notifyDataSetChanged();
//                                Toast.makeText(getBaseContext(), "已选择" + sb.toString() + "张图片", Toast.LENGTH_SHORT).show();
//
//
//                            }
//                        })
//                        .openGallery();
                //==========================
                addImg();

                break;
        }
    }

    public void addImg(){
        int count  = imgCountLimit -imageDatas.size();
        if(imageDatas.contains(getResourceUri(R.drawable.ic_handlejingq_add_img))){
            imageDatas.remove(getResourceUri(R.drawable.ic_handlejingq_add_img));
            count++;
        }
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setShowCamera(true);
        imagePicker.setSelectLimit(count);
        imagePicker.setCrop(false);
        Intent intent = new Intent(getApplicationContext(), ImageGridActivity.class);
        startActivityForResult(intent, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_EDIT_IMAGE) {
            imageDatas.clear();
            imageDatas.addAll((List<String>) data.getSerializableExtra("imageDatas"));

            StringBuffer sb = new StringBuffer();
            boolean isAddSpliteChar = false;
            for (String s : imageDatas) {
                if (isAddSpliteChar) {
                    sb.append(",");
                }
                sb.append(s);
                isAddSpliteChar = true;
            }
            entityJingq.setFilesPath(sb.toString());
            if(imageDatas.size()< imgCountLimit){
                imageDatas.add(getResourceUri(R.drawable.ic_handlejingq_add_img));
            }
            adapterImages.notifyDataSetChanged();
        }

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                final ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                if (images != null && images.size() > 0) {
                    StringBuffer sb = new StringBuffer();
                    boolean isAddDivder = false;

                    for(int i = 0;i<images.size();i++){
                        ImageItem item = images.get(i);
                        final String path = item.path;
                        if(isAddDivder){
                            sb.append(",");
                        }
                        sb.append(path);
                        isAddDivder = true;
                        imageDatas.add(path);
                    }
                    entityJingq.setFilesPath(sb.toString());
                }
                if(imageDatas.size()< imgCountLimit){
                    Resources r =context.getResources();
                    imageDatas.add(getResourceUri(R.drawable.ic_handlejingq_add_img));
                }
                adapterImages.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getResourceUri(int resId){
        Resources r =context.getResources();
        Uri uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(resId) + "/"
                + r.getResourceTypeName(resId) + "/"
                + r.getResourceEntryName(resId));
        return uri.toString();
    }

}
