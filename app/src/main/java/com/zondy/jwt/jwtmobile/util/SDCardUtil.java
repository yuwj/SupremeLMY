package com.zondy.jwt.jwtmobile.util;

import android.os.Environment;

import com.zondy.jwt.jwtmobile.global.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ywj on 2017/3/26 0026.
 */

public class SDCardUtil {

    /***
     * 保存url请求信息到文件中
     * @param url  请求地址
     * @param info 请求信息
     * @return
     */
    public static String saveHttpRequestInfo2File(String url,String info) {
        if(url.contains("/")){
            url = url.substring(url.lastIndexOf("/")+1,url.length());
        }
        String time =
                new SimpleDateFormat("HH:mm:ss").format(new Date());
        time = time.replaceAll(":","-");
        try {
            String fileName = url + ".txt";
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String path = Constant.reqPath;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + time+fileName);
                fos.write(info.getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
        }
        return null;
    }
}
