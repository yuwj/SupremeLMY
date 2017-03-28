package com.zondy.jwt.jwtmobile.manager;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.zondy.jwt.jwtmobile.entity.EntityLocation;
import com.zondy.jwt.jwtmobile.util.SharedTool;

/**
 * Created by sheep on 2017/1/18.
 */

public class JWTLocationManager {
    // 单例
    private static JWTLocationManager jwtLocationManager;

    private Context context;
    private long timeInterval = 30;// 单位秒
    private float distanceInterval = 10;// 单位m
    private LocationManager manager;
    private MyLoactionListener jwtLocationListener;

    public final static String LOCATION_SUCCESS_BROADCAST = JWTLocationManager.class.getName()+".action.locationsuccess";
    // 1.私有化构造方法
    private JWTLocationManager(Context context,
                               long timeInterval, float distanceInterval) {
        this.context = context;
        this.timeInterval = timeInterval;
        this.distanceInterval = distanceInterval;
        manager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        jwtLocationListener = new MyLoactionListener();
    };

    // 2. 提供一个静态的方法 可以返回他的一个实例 保证代码必须执行完成 放在 synchronized 单态的GPSInfoProvider
    public static synchronized JWTLocationManager getInstance(Context context,
                                                              long timeInterval, float distanceInterval) {

        if (jwtLocationManager == null) {
            jwtLocationManager = new JWTLocationManager(context,timeInterval,distanceInterval);
        }
        return jwtLocationManager;
    }

    // 获取gps 信息
    public void startLocation() {
        if(manager == null){
            manager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
        }

        // 选择一种目前状态下最好的定位方式
        String provider = getProvider(manager);
        if(jwtLocationListener == null){
            jwtLocationListener = new MyLoactionListener();
        }
        // 注册位置的监听器
        /**
         * provider 定位方式 用什么设备定位 基站 网络 GPS AGPS 时间 gps 多长时间重新获取一下位置 最小为1分钟 位置
         * 最短位移 位置改变多少 重新获取一下位置 jwtLocationListener 位置发生改变时 对应的回调方法
         */
        manager.requestLocationUpdates(provider, timeInterval * 1000,
                distanceInterval, jwtLocationListener);
    }

    /**
     * 获取最新定位信息
     *
     * @return
     */
    public Location getLocation() {
        if (manager != null) {
            return manager.getLastKnownLocation(getProvider(manager));
        } else {
            return null;
        }

    }

    // 停止gps监听
    public void stopGPSListener() {
        // 参数为LocationListener
        if (manager != null) {
            manager.removeUpdates(jwtLocationListener);
            manager = null;
            jwtLocationListener = null;
        }
    }


    private class MyLoactionListener implements LocationListener {

        /**
         * 当手机位置发生改变的时候 调用的方法
         */
        public void onLocationChanged(Location location) {
            if (location != null && location.getLongitude() > 0
                    && location.getLatitude() > 0) {
                float bearing = location.getBearing();
                SharedTool.getInstance().saveLastLocation(context, location.getLongitude(), location.getLatitude(),location.getBearing());
                EntityLocation entityLocation = new EntityLocation();
                entityLocation.setBearing(location.getBearing());
                entityLocation.setLongitude(location.getLongitude());
                entityLocation.setLatitude(location.getLatitude());
                Intent locationSuccessIntent = new Intent(LOCATION_SUCCESS_BROADCAST);
                locationSuccessIntent.putExtra("entityLocation",entityLocation);
                context.sendBroadcast(locationSuccessIntent);
            }
        }

        /**
         * 某一个设备的状态发生改变的时候 调用 可用->不可用 不可用->可用 GPS是否可用
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        /**
         * 某个设备被打开 GPS被打开
         */
        public void onProviderEnabled(String provider) {

        }

        /**
         * 某个设备被禁用 GPS被禁用
         *
         */
        public void onProviderDisabled(String provider) {

        }

    }

    /**
     *
     * @param manager
     *            位置管理服务
     * @return 最好的位置提供者 // gps //wifi //
     */
    private String getProvider(LocationManager manager) {
        // 一组查询条件
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 获取精准位置、
        criteria.setAltitudeRequired(false);// 对海拔不敏感
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);// 耗电量中等
        criteria.setSpeedRequired(true);// 速度变化敏感
        criteria.setCostAllowed(true);// 产生开销 通信费用
        // 返回最好的位置提供者 true 表示只返回当前已经打开的定位设备
        return manager.getBestProvider(criteria, true);
    }
}
