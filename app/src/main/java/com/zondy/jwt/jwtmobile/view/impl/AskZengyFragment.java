package com.zondy.jwt.jwtmobile.view.impl;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.zondy.jwt.jwtmobile.base.BaseFragment;
import com.zondy.jwt.jwtmobile.entity.EntityAskZengy;
import com.zondy.jwt.jwtmobile.entity.EntityJingq;
import com.zondy.jwt.jwtmobile.entity.EntityLocation;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.entity.EntityZD;
import com.zondy.jwt.jwtmobile.global.Constant;
import com.zondy.jwt.jwtmobile.presenter.IAskServicePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.AskServicePresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.GlideImageLoader;
import com.zondy.jwt.jwtmobile.util.GsonUtil;
import com.zondy.jwt.jwtmobile.util.MapManager;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IAskServiceView;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.annotation.AnnotationsOverlay;
import com.zondy.mapgis.core.geometry.Dot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ywj on 2017/4/1 0001.
 */

public class AskZengyFragment extends BaseFragment implements IAskServiceView {


    EntityJingq entityJingq;
    EntityUser userInfo;
    IAskServicePresenter askServicePresenter;
    EntityAskZengy entityAskZengy;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.rv_media)
    RecyclerView rv_media;
    public static final int REQ_CODE_EDIT_IMAGE = 1;
    CommonAdapter<String> adapterImages;
    public final int imgCountLimit = 4;
    List<String> imageDatas;

    public static Fragment createInstance(EntityJingq entityJingq) {
        BaseFragment fragment = new AskZengyFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("entityJingq", entityJingq);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_zengy, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initParam();
        initView();
        initOperator();
    }

    void initParam() {
        Bundle bundle = getArguments();
        entityJingq = (EntityJingq) bundle.getSerializable("entityJingq");
        userInfo = SharedTool.getInstance().getUserInfo(context);
        askServicePresenter = new AskServicePresenterImpl(this);
        entityAskZengy = new EntityAskZengy();

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
                if (getResourceUri(R.drawable.ic_handlejingq_add_img).equals(imgPath)) {
                    //添加图片
                    addImg();
                } else {
                    //查看图片
                    if (imageDatas.contains(getResourceUri(R.drawable.ic_handlejingq_add_img))) {
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

    void initView() {
        rv_media.setLayoutManager(new GridLayoutManager(context, 4));
        rv_media.setAdapter(adapterImages);

    }

    void initOperator() {

    }



    public void addImg() {
        int count = imgCountLimit - imageDatas.size();
        if (imageDatas.contains(getResourceUri(R.drawable.ic_handlejingq_add_img))) {
            imageDatas.remove(getResourceUri(R.drawable.ic_handlejingq_add_img));
            count++;
        }
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setShowCamera(true);
        imagePicker.setSelectLimit(count);
        imagePicker.setCrop(false);
        Intent intent = new Intent(context, ImageGridActivity.class);
        getActivity().startActivityForResult(intent, 100);
    }


    public void onAskZengyFragmentResult(int requestCode, int resultCode, Intent data) {

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
            if (imageDatas.size() < imgCountLimit) {
                imageDatas.add(getResourceUri(R.drawable.ic_handlejingq_add_img));
            }
            adapterImages.notifyDataSetChanged();
        } else if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                final ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                if (images != null && images.size() > 0) {
                    StringBuffer sb = new StringBuffer();
                    boolean isAddDivder = false;

                    for (int i = 0; i < images.size(); i++) {
                        ImageItem item = images.get(i);
                        final String path = item.path;
                        if (isAddDivder) {
                            sb.append(",");
                        }
                        sb.append(path);
                        isAddDivder = true;
                        imageDatas.add(path);
                    }
                    entityJingq.setFilesPath(sb.toString());
                }
                if (imageDatas.size() < imgCountLimit) {
                    Resources r = context.getResources();
                    imageDatas.add(getResourceUri(R.drawable.ic_handlejingq_add_img));
                }
                adapterImages.notifyDataSetChanged();
            } else {
                Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAskSuccess() {
        getActivity().finish();
    }

    @Override
    public void onAskFail(Exception exception) {
        ToastTool.getInstance().shortLength(context, exception.getMessage(), true);
    }

    public void launchAsk() {


        String jh = userInfo.getUserName();
        String zzjgdm = userInfo.getZzjgdm();
        entityAskZengy.setZzjgdm(zzjgdm);
        entityAskZengy.setJh(jh);
        entityAskZengy.setJqbh(entityJingq.getJingqid());
        EntityLocation entityLocation = SharedTool.getInstance().getLocationInfo(context);
        entityAskZengy.setX(entityLocation.getLongitude() + "");
        entityAskZengy.setX(entityLocation.getLatitude() + "");
        String simid = CommonUtil.getDeviceId(context);
        askServicePresenter.askZengy(jh, simid, entityAskZengy);
    }

    public void setEntityJingq(EntityJingq entityJingq) {
        this.entityJingq = entityJingq;
    }

    public String getResourceUri(int resId) {
        Resources r = context.getResources();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(resId) + "/"
                + r.getResourceTypeName(resId) + "/"
                + r.getResourceEntryName(resId));
        return uri.toString();
    }
}
