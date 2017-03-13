package com.yuwj.appupdate;

import android.content.Context;
import android.util.Log;

public class UpdateChecker {


    public static void checkForDialog(Context context,boolean isShowProgress,String versionInfoUrl) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_DIALOG, isShowProgress,versionInfoUrl).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }
    }


    public static void checkForNotification(Context context,boolean isShowProgress,String versionInfoUrl) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_NOTIFICATION, isShowProgress,versionInfoUrl).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }

    }


}
