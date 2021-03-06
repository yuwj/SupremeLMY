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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.zondy.jwt.jwtmobile.entity.EntityNoticeFank;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.presenter.INoticePresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.NoticePresenter;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.GlideImageLoader;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.INoticeFankView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class NoticeFankActivity extends BaseActivity implements INoticeFankView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.rv_media)
    RecyclerView rvMedia;

    public final int imgCountLimit = 4;//图片选择限制个数


String tuisxxId;
    INoticePresenter noticePresenter;
    EntityUser user;

    CommonAdapter<String> adapterImages;
    List<String> imageDatas;
    private final int REQ_CODE_EDIT_IMAGE = 1;



    public static Intent createIntent(Context context,String tuisxxId) {
        Intent intent = new Intent(context, NoticeFankActivity.class);
        intent.putExtra("bufbkId",tuisxxId);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_notice_fank,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.fank:
                String jh = user.getUserName();
                String simeid = CommonUtil.getDeviceId(context);
                String feedbackStrInfo = etInput.getText().toString().trim();
                StringBuffer sb = new StringBuffer();
                boolean isNeedSpliter = false;
                for(String path : imageDatas){
                    if(path.equals(getResourceUri(R.drawable.ic_handlejingq_add_img))){
                        continue;
                    }
                    if(isNeedSpliter){
                        sb.append(",");
                    }
                    sb.append(path);
                    isNeedSpliter = true;

                }
                noticePresenter.fank(tuisxxId,jh,simeid,sb.toString(),feedbackStrInfo);
                showLoadingDialog("正在提交...");
                break;
        }
       return super.onOptionsItemSelected(item);
    }

    public void initParam() {
        tuisxxId = getIntent().getStringExtra("bufbkId");
        noticePresenter = new NoticePresenter(this);
        user = SharedTool.getInstance().getUserInfo(context);

        imageDatas = new ArrayList<>();
        imageDatas.add(getResourceUri(R.drawable.ic_handlejingq_add_img));
        adapterImages = new CommonAdapter<String>(context, R.layout.item_notice_media, imageDatas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                Glide.with(mContext)
                        .load(s)
                        .crossFade()
                        .into((ImageView) holder.getView(R.id.iv_notice_image));
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
        initActionBar(toolbar,tvTitle,"信息反馈");
        rvMedia.setLayoutManager(new GridLayoutManager(context, 4));
        rvMedia.setAdapter(adapterImages);
    }


    public void initOperator() {    }



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
    public void noticeFankSuccess(EntityNoticeFank entityNoticeFank) {
        dismissLoadingDialog();
        Intent intent = new Intent();
        intent.putExtra("entityNoticeFank", entityNoticeFank);
        setResult(RESULT_OK,intent);
        this.finish();
    }

    @Override
    public void noticeFankFail(Exception e) {
        dismissLoadingDialog();
        ToastTool.getInstance().shortLength(context,e.getMessage(),true);

    }
}
