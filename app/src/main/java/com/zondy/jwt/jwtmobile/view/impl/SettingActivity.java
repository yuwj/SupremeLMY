package com.zondy.jwt.jwtmobile.view.impl;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.listener.UploadListener;
import com.lzy.okserver.upload.UploadInfo;
import com.lzy.okserver.upload.UploadManager;
import com.mpush.demo.PushActivity;
import com.yuwj.appupdate.UpdateChecker;
import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.base.BaseActivity;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.manager.ActivityCollector;
import com.zondy.jwt.jwtmobile.manager.UrlManager;
import com.zondy.jwt.jwtmobile.presenter.ISettingPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.SettingPresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.GlideImageLoader;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.util.ToastTool;
import com.zondy.jwt.jwtmobile.view.ISettingView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Response;

/**
 * Created by sheep on 2017/1/11.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener, ISettingView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    ISettingPresenter settingPresenter = new SettingPresenterImpl(this);
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_jh)
    TextView tvJh;
    @BindView(R.id.rl_account_info)
    RelativeLayout rlAccountInfo;
    @BindView(R.id.tv_about_jwt)
    TextView tvAboutJwt;
    @BindView(R.id.tv_caiji_info)
    TextView tvCaijiInfo;
    @BindView(R.id.tv_mpush)
    TextView tvMpush;
    @BindView(R.id.tv_check_update)
    TextView tvCheckUpdate;
    @BindView(R.id.tv_update_password)
    TextView tv_update_password;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.mask)
    ImageView mask;
    @BindView(R.id.civ)
    com.zondy.jwt.jwtmobile.ui.ProgressPieView civ;
    @BindView(R.id.fl_update_photo)
    FrameLayout flUpdatePhoto;

    @Override
    public int setCustomContentViewResourceId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        EntityUser user = SharedTool.getInstance().getUserInfo(SettingActivity.this);
        if (user != null) {
            tvName.setText(user.getCtname());
            tvJh.setText("警号：" + user.getUserName());
            String photoUrl = user.getUserPhotoUrl();
            if(!TextUtils.isEmpty(photoUrl)){


            Glide.with(context).load(photoUrl).into(imageView);}
        }

        tvLogout.setOnClickListener(this);
        rlAccountInfo.setOnClickListener(this);
        tvAboutJwt.setOnClickListener(this);
        tvCaijiInfo.setOnClickListener(this);
        tvMpush.setOnClickListener(this);
        tvCheckUpdate.setOnClickListener(this);
        flUpdatePhoto.setOnClickListener(this);
        tv_update_password.setOnClickListener(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        civ.setVisibility(View.INVISIBLE);
        mask.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sos:
                ToastTool.getInstance().shortLength(this, "一键报警", true);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_logout:
                Snackbar.make(tvLogout, "确定退出登录？", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                EntityUser userInfo = SharedTool.getInstance().getUserInfo(SettingActivity.this);
                                String simid = CommonUtil.getDeviceId(SettingActivity.this);
                                settingPresenter.logout(userInfo.getUserName(), simid);
                            }
                        }).show();
                break;
            case R.id.rl_account_info:
                Intent intentAccountInfo = new Intent(SettingActivity.this, SettingAccountInfo.class);
                startActivity(intentAccountInfo);
                break;
            case R.id.fl_update_photo:
                //使用rxgallery选择图片后,使用uploadManager上传
//                UploadPhotoManager.UploadPhotoListener uploadPhotoListener = new UploadPhotoManager.UploadPhotoListener() {
//                    @Override
//                    public void onUploadSuccess(String imgUrl) {
//                        Toast.makeText(getApplicationContext(), imgUrl, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onUploadFailed(String msg) {
//                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                    }
//                };
//                UploadPhotoManager m = new UploadPhotoManager(SettingActivity.this, uploadPhotoListener);
//                m.showPickPhoto(ivAddPhoto);
                //使用imagePicker选择图片后用okgo上传
                ImagePicker imagePicker = ImagePicker.getInstance();
                imagePicker.setImageLoader(new GlideImageLoader());
                imagePicker.setShowCamera(true);
                imagePicker.setSelectLimit(1);
                imagePicker.setCrop(false);
                Intent intent = new Intent(getApplicationContext(), ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_about_jwt:
                Intent intentAboutJwt = new Intent(SettingActivity.this, SettingAboutJWTActivity.class);
                startActivity(intentAboutJwt);
                break;
            case R.id.tv_caiji_info:
                ToastTool.getInstance().shortLength(this, "即将开放...", true);
                break;
            case R.id.tv_mpush:
                startActivity(PushActivity.getIntent(SettingActivity.this));
                break;
            case R.id.tv_check_update:
                String updateInfoUrl = UrlManager.getSERVER() + UrlManager.UPDATE;
                UpdateChecker.checkForDialog(context,true, updateInfoUrl);
                break;
            case R.id.tv_update_password:
                startActivity(SettingAccountUpdatePwdActivity.createIntent(context));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                civ.setVisibility(View.VISIBLE);
                mask.setVisibility(View.VISIBLE);
                final ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && images.size() > 0) {
                    ImageItem i = images.get(0);
                    final String path = i.path;


                    UploadListener listener = new UploadListener() {
                        @Override
                        public void onProgress(UploadInfo uploadInfo) {

                            if (uploadInfo.getState() == DownloadManager.NONE) {
//                                tvProgress.setText("请上传");
                                civ.setText("请上传");
                            } else if (uploadInfo.getState() == UploadManager.ERROR) {
//                                tvProgress.setText("上传出错");
                                civ.setText("错误");
                            } else if (uploadInfo.getState() == UploadManager.WAITING) {
//                                tvProgress.setText("等待中");
                                civ.setText("等待");
                            } else if (uploadInfo.getState() == UploadManager.FINISH) {
//                                tvProgress.setText("上传成功");
                                civ.setText("成功");
                            } else if (uploadInfo.getState() == UploadManager.UPLOADING) {
//                                tvProgress.setText("上传中");
                                civ.setProgress((int) (uploadInfo.getProgress() * 100));
                                civ.setText((Math.round(uploadInfo.getProgress() * 10000) * 1.0f / 100) + "%");
                            }
                        }

                        @Override
                        public void onFinish(Object o) {
                            Log.i("xxx", "onFinish: "+o.toString());
//                            tvProgress.setText("上传成功");
                            civ.setVisibility(View.GONE);
                            mask.setVisibility(View.GONE);
                            imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                        }

                        @Override
                        public void onError(UploadInfo uploadInfo, String errorMsg, Exception e) {
                            Log.i("xx",e.getMessage());
                        }

                        @Override
                        public Object parseNetworkResponse(Response response) throws Exception {
                            return null;
                        }
                    };

                    String uploadUrl = UrlManager.getSERVER() + UrlManager.UPLOAD_PHOTO;
                    EntityUser user = SharedTool.getInstance().getUserInfo(context);
                    File file  = new File(path);
                    List<File> fileList = new ArrayList<File>();
                    fileList.add(file);

                    JSONObject jsonParam = new JSONObject();
                    try {
                        jsonParam.put("filesPath", path);
                        jsonParam.put("jh", user.getUserName());
                        jsonParam.put("simid", CommonUtil.getDeviceId(context));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    PostRequest postRequest = OkGo.post(uploadUrl)
                            .tag(this)
                            .isMultipart(true)  //
                            .params("strBody", jsonParam.toString())//
                            .addFileParams("fileKey" , fileList);
                    UploadManager uploadManager = UploadManager.getInstance();
                    uploadManager.getThreadPool().setCorePoolSize(1);
                    uploadManager.addTask(path, postRequest, listener);

                }
            } else {
                Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void logoutSuccessed() {
        SharedTool.getInstance().clearUserInfo(this);
        ActivityCollector.finishAll();
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void logoutUnSuccessed() {
        ToastTool.getInstance().shortLength(this, "登出失败！", true);
    }


}
