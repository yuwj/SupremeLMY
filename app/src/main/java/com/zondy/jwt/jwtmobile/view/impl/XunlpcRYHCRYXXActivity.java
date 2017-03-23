package com.zondy.jwt.jwtmobile.view.impl;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityXunlpcJDCXX;
import com.zondy.jwt.jwtmobile.entity.EntityXunlpcRYXX;
import com.zondy.jwt.jwtmobile.ui.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sheep on 2017/3/14.
 */

public class XunlpcRYHCRYXXActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_media)
    RecyclerView rvMedia;
    @BindView(R.id.tv_xunlpc_ryhcryxx_xidry)
    TextView tvxunlpcRyhcryxxXidry;
    com.zhy.adapter.recyclerview.CommonAdapter<String> adapterImages;
    List<String> imageDatas;

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_xunlpc_ryhcryxx;
    }

    public static void actionStart(Context context, EntityXunlpcJDCXX entityXunlpcJDCXX) {
        Intent intent = new Intent(context, XunlpcRYHCRYXXActivity.class);
        intent.putExtra("JDCXX", entityXunlpcJDCXX);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        initViews();
    }

    private void initParams() {
        imageDatas = new ArrayList<>();
        imageDatas.add(getResourceUri(R.drawable.timg1));
        imageDatas.add(getResourceUri(R.drawable.timg2));
        imageDatas.add(getResourceUri(R.drawable.timg3));

    }

    private void initViews() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        adapterImages = new com.zhy.adapter.recyclerview.CommonAdapter<String>(context, R.layout.item_jingq_handled_images, imageDatas) {
            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, String s, int position) {
                Glide.with(mContext)
                        .load(s)
                        .crossFade()
                        .into((ImageView) holder.getView(R.id.iv_handled_jingq_image));
            }
        };
        rvMedia.setLayoutManager(new FullyGridLayoutManager(this,4));
        rvMedia.setAdapter(adapterImages);
        tvxunlpcRyhcryxxXidry.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.tv_xunlpc_ryhcryxx_xidry:
                EntityXunlpcRYXX ryxx = new EntityXunlpcRYXX();
                ryxx.setName("杨阳洋");
                ryxx.setAddress("武汉光谷广场");
                ryxx.setSex("男");
                startActivity(PancPersonDetailActivity.createIntent(context,ryxx,ryxx.getName()+"的吸毒信息"));
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
}
