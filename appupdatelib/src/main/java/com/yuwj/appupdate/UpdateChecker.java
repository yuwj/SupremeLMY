package com.yuwj.appupdate;

import android.content.Context;
import android.util.Log;

public class UpdateChecker {


    public static void checkForDialog(Context context,String versionInfoUrl) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_DIALOG, true,versionInfoUrl).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }
    }


    public static void checkForNotification(Context context,String versionInfoUrl) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_NOTIFICATION, true,versionInfoUrl).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }

    }


}
