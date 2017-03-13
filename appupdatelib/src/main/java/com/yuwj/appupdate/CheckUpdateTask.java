package com.yuwj.appupdate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author feicien (ithcheng@gmail.com)
 * @since 2016-07-05 19:21
 */
class CheckUpdateTask extends AsyncTask<Void, Void, String> {

    private ProgressDialog dialog;
    private Context mContext;
    private int mType;
    private boolean mShowProgressDialog;
    private String url;

    CheckUpdateTask(Context context, int type, boolean showProgressDialog,String versionInfoUrl) {

        this.mContext = context;
        this.mType = type;
        this.mShowProgressDialog = showProgressDialog;
        this.url = versionInfoUrl;

    }


    protected void onPreExecute() {
        if (mShowProgressDialog) {
            dialog = new ProgressDialog(mContext);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
            dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
            dialog.setMessage(mContext.getString(R.string.android_auto_update_dialog_checking));
            dialog.show();

        }
    }


    @Override
    protected void onPostExecute(String result) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (!TextUtils.isEmpty(result)) {
            Log.i(CheckUpdateTask.class.getSimpleName(),result);
            parseJson(result);
        }
    }

    private void parseJson(String result) {
        try {

            JSONObject objUpdateInfo = new JSONObject(result);
            String s = objUpdateInfo.getString(Constants.APK_UPDATE_IFNO);

            JSONObject obj = new JSONObject(s);
            String apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL);
            int apkCode = obj.getInt(Constants.APK_VERSION_CODE);
            String updateTextInfo = obj.getString(Constants.APK_UPDATE_CONTENT);

            int versionCode = AppUtils.getVersionCode(mContext);

            if (apkCode > versionCode) {
                if (mType == Constants.TYPE_NOTIFICATION) {
                    showNotification(mContext, updateTextInfo, apkUrl);
                } else if (mType == Constants.TYPE_DIALOG) {
                    showDialog(mContext, updateTextInfo, apkUrl);
                }
            } else if (mShowProgressDialog) {
                Toast.makeText(mContext, mContext.getString(R.string.android_auto_update_toast_no_new_update), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e(Constants.TAG, "parse json error");
        }
    }


    /**
     * Show dialog
     */
    private void showDialog(Context context, String content, String apkUrl) {
        UpdateDialog.show(context, content, apkUrl);
    }

    /**
     * Show Notification
     */
    private void showNotification(Context context, String content, String apkUrl) {
        Intent myIntent = new Intent(context, DownloadService.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.putExtra(Constants.APK_DOWNLOAD_URL, apkUrl);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int smallIcon = context.getApplicationInfo().icon;
        Notification notify = new NotificationCompat.Builder(context)
                .setTicker(context.getString(R.string.android_auto_update_notify_ticker))
                .setContentTitle(context.getString(R.string.android_auto_update_notify_content))
                .setContentText(content)
                .setSmallIcon(smallIcon)
                .setContentIntent(pendingIntent).build();

        notify.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notify);
    }

    @Override
    protected String doInBackground(Void... args) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(CheckUpdateTask.class.getSimpleName(),url);
        return HttpUtils.get(url);
    }
}
