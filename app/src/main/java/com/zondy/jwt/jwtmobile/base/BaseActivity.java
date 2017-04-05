package com.zondy.jwt.jwtmobile.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.manager.ActivityCollector;
import com.zondy.jwt.jwtmobile.ui.LoadingProgressDialog;

import butterknife.ButterKnife;


/**
 * Created by sheep on 2016/12/23.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public abstract int setCustomContentViewResourceId();
    public Context context;
    DialogInterface.OnCancelListener taskCancelListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setCustomContentViewResourceId());
        context=this;
        ActivityCollector.addActivity(this);
        //使用ButterKnife 注入view
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    /**
     * 初始化标题
     *
     * @param toolbar
     * @param tvTitle
     * @param title
     */
    public void initActionBar(Toolbar toolbar, TextView tvTitle, String title) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("");
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("");
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(false);//是否显示当前程序的图标
            }
        }
        if (tvTitle != null && !TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }

//    ProgressDialog loadingProgressDialog;
   LoadingProgressDialog loadingProgressDialog;
    int taskCount = 0;//有时一个页面会发出多个请求
  /**
     * 显示加载时的对话框
     */
    public void showLoadingDialog(String loadInfo) {
        taskCount++;
        if (loadingProgressDialog == null) {
            loadingProgressDialog = new LoadingProgressDialog(this, R.style.loading_progress_dialog);
            loadingProgressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
            loadingProgressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
            if(taskCancelListener != null){

                loadingProgressDialog.setOnCancelListener(taskCancelListener);
            }
        }
        loadingProgressDialog.setTitle(loadInfo);
        loadingProgressDialog.show();
    }

    /**
     * 隐藏加载时的对话框
     */
    public void dismissLoadingDialog() {
        if (loadingProgressDialog != null && loadingProgressDialog.isShowing()) {
            if(--taskCount <=0){
             loadingProgressDialog.dismiss();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public DialogInterface.OnCancelListener getTaskCancelListener() {
        return taskCancelListener;
    }

    public void setTaskCancelListener(DialogInterface.OnCancelListener taskCancelListener) {
        this.taskCancelListener = taskCancelListener;
    }
}
