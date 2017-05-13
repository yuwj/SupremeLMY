package com.zondy.jwt.jwtmobile.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.IBinder;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.entity.EntityUser;
import com.zondy.jwt.jwtmobile.manager.JWTLocationManager;
import com.zondy.jwt.jwtmobile.presenter.ILoginPresenter;
import com.zondy.jwt.jwtmobile.presenter.impl.LoginPresenterImpl;
import com.zondy.jwt.jwtmobile.util.CommonUtil;
import com.zondy.jwt.jwtmobile.util.SDCardUtil;
import com.zondy.jwt.jwtmobile.util.SharedTool;
import com.zondy.jwt.jwtmobile.view.impl.MainWithGridActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 轮询服务,轮询上传GPS及获取最新的布防布控,通知公告信息.
 * Created by sheep on 2017/1/18.
 */

public class PollService extends Service {

    public final int SERVICE_ID = 110;
    public final static String SERVICE_REPEAT = "com.zondy.service.intent.action.ServiceRepeat";
    String tag = PollService.class.getSimpleName();

    JWTLocationManager jwtLocationManager;
    Timer timer;// 定时上传登陆信息
    static Context context;
    boolean isRepeat = true;// 是否开始重复心跳
    int repeatTime = 30;// 默认30秒跳一次
    ILoginPresenter loginPresenter;
    EntityUser userInfo;

	public static Intent createIntent(Context context, int repeatTime,
			boolean isRepeat) {
		Intent intent = new Intent(context, PollService.class);
		intent.putExtra("isRepeat", isRepeat);
		intent.putExtra("repeatTime", repeatTime);
        PollService.context = context;
		return intent;
	}

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            isRepeat = true;
            repeatTime = 30;
        } else {
            repeatTime = intent.getIntExtra("repeatTime", 30);
            isRepeat = intent.getBooleanExtra("isRepeat", true);
        }
        context = this;
        if (isRepeat) {

            startRepeat();

        } else {
            stopRepeat();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void stopRepeat() {
        if (jwtLocationManager != null) {
            jwtLocationManager.stopGPSListener();
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        stopSelf();
    }

    public void startRepeat() {
        if(context == null){
            stopSelf();
            return;
        }
        // 在API11之后构建Notification的方式,创建notification,用于前台服务
        Notification.Builder builder = new Notification.Builder(context); // 获取一个Notification构造器
        Intent intent = MainWithGridActivity.createIntent(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        builder.setContentIntent(pendingIntent)

                .setLargeIcon(
                        BitmapFactory.decodeResource(this.getResources(),
                                R.drawable.ic_launcher)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("移动处警平台") // 设置下拉列表里的标题
                .setSmallIcon(R.drawable.ic_launcher) // 设置状态栏内的小图标
                .setContentText("移动处警平台保持在后台运行") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        Notification notification = builder.getNotification(); // 获取构建好的Notification
        startForeground(SERVICE_ID, notification);// 开始前台服务

        //请求GPS定位
        Map<String, Long> locationIntervalMap = SharedTool.getInstance()
                .getLocationInterval(context);
        long timeInterval = locationIntervalMap.get("timeInterval");
        float distanceInterval = Float.valueOf(locationIntervalMap
                .get("distanceInterval"));
        jwtLocationManager = JWTLocationManager.getInstance(context,
                timeInterval, distanceInterval);
        jwtLocationManager.startLocation();


        if(loginPresenter == null){
            loginPresenter = new LoginPresenterImpl();
        }
        if (timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                StringBuffer sb = new StringBuffer();
                if(userInfo == null){
                    userInfo = SharedTool.getInstance().getUserInfo(context);
                }
                String username = userInfo.getUserName();
                String xingm = userInfo.getCtname();
                String deviceId = CommonUtil.getDeviceId(context);
                // 上传定位信息
                Location location = jwtLocationManager.getLocation();

                if (location == null || location.getLongitude() <= 0
                        || location.getLatitude() <= 0) {
                    sb.append("\n\nunget location info");
                    // 获取不到定位
                } else {

                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    sb.append("\n\n lnglat:"+longitude + " "+latitude);

                    sb.append("\n\ndo uploadGPS");
                    loginPresenter.uploadGPS(username,deviceId,longitude,latitude);
                }

                // 更新登陆后的在线时间
                sb.append("\n\ndo updateDLSSXX");
                loginPresenter.updateDLSSXX(username,deviceId);
                //获取未接收的布防布控信息ids
                sb.append("\n\ndo queryUnacceptBufbkIds");
                loginPresenter.queryUnacceptBufbkIds(getApplicationContext(),username,deviceId,xingm);
                SimpleDateFormat f = new SimpleDateFormat("HH:mm");
                String s = f.format(new Date()).replace(":","-");
                SDCardUtil.saveHttpRequestInfo2File("pollService"+s,sb.toString());
            }
        }, 0, repeatTime * 1000);


    }

    @Override
    public void onDestroy() {
        stopRepeat();
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        super.onDestroy();
    }
}
