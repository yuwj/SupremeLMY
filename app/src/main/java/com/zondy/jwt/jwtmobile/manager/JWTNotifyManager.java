package com.zondy.jwt.jwtmobile.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.view.impl.BufbkDetailActivity;

import java.util.List;
import java.util.Random;


public class JWTNotifyManager {

    private static final Random random = new Random(System.currentTimeMillis());

    private Context context;

    private NotificationManager notificationManager;

    public static JWTNotifyManager jwtNotifyManager;

    private JWTNotifyManager(Context context) {
        this.context = context;
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }

    }

    public static JWTNotifyManager getInstance(Context context) {
        if (jwtNotifyManager == null) {
            jwtNotifyManager = new JWTNotifyManager(context);
        }
        return jwtNotifyManager;
    }


    @SuppressWarnings("deprecation")
    public void notifyUnacceptBufbkIds(List<String> unacceptBufbkIds) {

        for (String bkid : unacceptBufbkIds) {
            int notifyId = Integer.valueOf(bkid);
            int requestCodeBufbkUnaccept = 1;
            int flags = PendingIntent.FLAG_UPDATE_CURRENT;
            Intent intent = BufbkDetailActivity.createIntent(context, bkid);
            PendingIntent pendingIntent = PendingIntent
                    .getActivity(context, requestCodeBufbkUnaccept,
                            intent, flags);

            Notification.Builder builder = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setContentTitle("ContentTitle 新的布控信息")
                    .setContentText("ContentText 新的布控信息")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setOngoing(true)
                    .setTicker("Ticker 新的布控信息");
            Notification notification = builder.getNotification();

            notification.defaults = Notification.DEFAULT_LIGHTS;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(notifyId,
                    notification);
        }


    }


}
