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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.zondy.jwt.jwtmobile.entity.EntityBufbkFeedback;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.IBufbkPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.BufbkPresenter;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.GlideImageLoader;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.IBufbkFeedbackView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class BufbkFeedbackActivity extends BaseActivity implements IBufbkFeedbackView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.rv_media)
    RecyclerView rvMedia;
    @BindView(R.id.tv_confirm_handle)
    Button tvConfirmHandle;

    public final int imgCountLimit = 4;//图片选择限制个数


    String bufbkId;
    IBufbkPresenter bufbkPresenter;
    EntityUser user;

    CommonAdapter<String> adapterImages;
    List<String> imageDatas;
    private final int REQ_CODE_EDIT_IMAGE = 1;


    public static Intent createIntent(Context context, String bufbkId) {
        Intent intent = new Intent(context, BufbkFeedbackActivity.class);
        intent.putExtra("bufbkId", bufbkId);
        return intent;
    }

    @Override
    public int setCustomContentViewResourceId() {

        return R.layout.activity_tuisxx_feedback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initView();
        initOperator();
    }

    public void initParam() {
        bufbkId = getIntent().getStringExtra("bufbkId");
        bufbkPresenter = new BufbkPresenter(this);
        user = SharedTool.getInstance().getUserInfo(context);

        imageDatas = new ArrayList<>();
        imageDatas.add(getResourceUri(R.drawable.ic_handlejingq_add_img));
        adapterImages = new CommonAdapter<String>(context, R.layout.item_tuisxx_media, imageDatas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                Glide.with(mContext)
                        .load(s)
                        .crossFade()
                        .into((ImageView) holder.getView(R.id.iv_tuisxx_image));
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

    public void initView() {
        initActionBar(toolbar, tvTitle, "信息反馈");
        rvMedia.setLayoutManager(new GridLayoutManager(context, 4));
        rvMedia.setAdapter(adapterImages);
    }


    public void initOperator() {
    }

    @OnClick({R.id.tv_confirm_handle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm_handle:
                String jh = user.getUserName();
                String xingm = user.getCtname();
                String simeid = CommonUtil.getDeviceId(context);
                String feedbackStrInfo = etInput.getText().toString().trim();
                StringBuffer sb = new StringBuffer();
                boolean isNeedSpliter = false;
                for (String path : imageDatas) {
                    if (path.equals(getResourceUri(R.drawable.ic_handlejingq_add_img))) {
                        continue;
                    }
                    if (isNeedSpliter) {
                        sb.append(",");
                    }
                    sb.append(path);
                    isNeedSpliter = true;

                }
                bufbkPresenter.feedback(bufbkId, jh, simeid,xingm,  sb.toString(), feedbackStrInfo);
                showLoadingDialog("正在提交...");
                break;
        }
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
        Intent intent = new Intent(getApplicationContext(), ImageGridActivity.class);
        startActivityForResult(intent, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_EDIT_IMAGE) {//图片编辑
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

            if (imageDatas.size() < imgCountLimit) {
                imageDatas.add(getResourceUri(R.drawable.ic_handlejingq_add_img));
            }
            adapterImages.notifyDataSetChanged();
        }

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {//图片选择
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
                }
                if (imageDatas.size() < imgCountLimit) {
                    Resources r = context.getResources();
                    imageDatas.add(getResourceUri(R.drawable.ic_handlejingq_add_img));
                }
                adapterImages.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getResourceUri(int resId) {
        Resources r = context.getResources();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(resId) + "/"
                + r.getResourceTypeName(resId) + "/"
                + r.getResourceEntryName(resId));
        return uri.toString();
    }

    @Override
    public void feedbackSuccess(EntityBufbkFeedback entityBufbkFeedback) {
        dismissLoadingDialog();
        Intent intent = new Intent();
        intent.putExtra("entityBufbkFeedback", entityBufbkFeedback);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void feedbackFalied(Exception e) {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context, e.getMessage(), true);

    }
}
