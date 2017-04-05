package com.zondy.jwt.jwtmobile.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;

/**
 * Created by ywj on 2017/4/2 0002.
 */

public class LoadingProgressDialog extends Dialog {

    TextView tvInfo;
    TextView tvPoint;
    ImageView ivLoading;
    RotateAnimation mAnim;

    public LoadingProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }


    private void init() {
        setContentView(R.layout.content_loading_progress);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        tvPoint = (TextView) findViewById(R.id.tv_point);
        initAnim();
    }


    private void initAnim() {
        mAnim = new RotateAnimation(0, 360, Animation.RESTART, 0.5f, Animation.RESTART, 0.5f);
        mAnim.setDuration(2000);
        mAnim.setRepeatCount(Animation.INFINITE);
        mAnim.setRepeatMode(Animation.RESTART);
        mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
    }


    @Override
    public void show() {//在要用到的地方调用这个方法
        ivLoading.startAnimation(mAnim);
        super.show();
    }


    @Override
    public void dismiss() {
        mAnim.cancel();
        super.dismiss();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            tvInfo.setText("正在加载");
        } else {
            tvInfo.setText(title);
        }
    }
}
