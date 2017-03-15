package com.zondy.jwt.jwtmobile.view.impl;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.util.GlideImageLoader;
import com.zondy.jwt.jwtmobile.util.ToastTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.zondy.jwt.jwtmobile.R.id.rv_media;

/**
 * Created by sheep on 2017/3/15.
 */

public class XunlpcPCFKXXActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sp_xunlpc_chulfk)
    Spinner spXunlpcChulfk;
    @BindView(R.id.et_xunlpc_fanknr)
    EditText etXunlpcFanknr;
    @BindView(R.id.iv_add_img)
    ImageView ivAddImg;
    @BindView(rv_media)
    RecyclerView rvMedia;
    @BindView(R.id.btn_queren)
    Button btnQueren;
    private CommonAdapter<String> chulfkAdapter;
    com.zhy.adapter.recyclerview.CommonAdapter<String> adapterImages;
    List<String> imageDatas;
    private final int REQ_CODE_EDIT_IMAGE = 1;
    public final int imgCountLimit = 4;

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_xunlpc_pcfkxx;
    }
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, XunlpcPCFKXXActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(imageDatas.size()==0){
            imageDatas.add(getResourceUri(R.drawable.ic_handlejingq_add_img));
        }
        adapterImages.notifyDataSetChanged();
    }

    private void initParams() {
        imageDatas = new ArrayList<>();
        imageDatas.add(getResourceUri(R.drawable.ic_handlejingq_add_img));
        adapterImages = new com.zhy.adapter.recyclerview.CommonAdapter<String>(context, R.layout.item_jingq_handled_images, imageDatas) {
            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, String s, int position) {
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
    private void initViews() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        btnQueren.setOnClickListener(this);
        rvMedia.setLayoutManager(new GridLayoutManager(context, 4));
        rvMedia.setAdapter(adapterImages);
        List<String> chulfkDatas=new ArrayList<>();
        chulfkDatas.add("核查完毕，正常放行");
        chulfkDatas.add("需再深入核查");
        chulfkDatas.add("非法人员，拘留带回");
        spXunlpcChulfk.setAdapter(chulfkAdapter=new CommonAdapter<String>(this,R.layout.item_jingqhandle_sp,chulfkDatas) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(R.id.tv_value,item);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_queren:
                Intent intent=new Intent(XunlpcPCFKXXActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ToastTool.getInstance().shortLength(this,"提交成功！",true);
                break;
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
                }
                if(imageDatas.size()< imgCountLimit){
                    Resources r =context.getResources();
                    imageDatas.add(getResourceUri(R.drawable.ic_handlejingq_add_img));
                }
                adapterImages.notifyDataSetChanged();
            }
        }
    }
}
