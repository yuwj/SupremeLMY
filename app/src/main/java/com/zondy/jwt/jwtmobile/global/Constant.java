package com.zondy.jwt.jwtmobile.global;

import android.os.Environment;

import com.zondy.jwt.jwtmobile.util.MapManager;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;

/**
 * Created by sheep on 2017/1/9.
 */

public class Constant {
    //警务通区域选择
    public static final String JWT_AREA_FN = "阜宁";
    public static final String JWT_AREA_LYG = "连云港";
    public static final String JWT_AREA_ZJG = "张家港";
    public static final String JWT_AREA_WH = "武汉";
    public static final String JWT_AREA_TEST = "测试";
    public static final String JWT_AREA_HA = "淮安";
    public static final String JWT_AREA_YZ = "永州";
    public static final String JWT_AREA_DZ = "邓州";
    public static final String JWT_AREA_SELECTED = JWT_AREA_HA;

    public static final String USER_SHARED_FILE = "user_shared_file";

    //获取字典数据集合时指定的字典类型
    public static final String ZDLX_ZZJG = "组织机构";

    public static final CacheControl FORCE_CACHE = new CacheControl.Builder()
            .onlyIfCached()
            .maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS)
            .build();

    public static final String cachePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "jwtInfo/";
    public static final String reqPath = cachePath +"req/";
    public static final String uploadCompressMediaPath = cachePath +"uploadCompressMediaPath/";
    public static final String crashPath = cachePath +"uploadCompressMediaPath/";

    public static final String getMapPath(){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator ;
        switch (JWT_AREA_SELECTED){
            case JWT_AREA_FN:
                path += "MapGIS/map/fn/fn.xml";
                break;
            case JWT_AREA_LYG:
                path += "MapGIS/map/lyg/lyg.xml";
                break;
            case JWT_AREA_ZJG:
                path += "MapGIS/map/zjg/zjg.xml";
                break;
            case JWT_AREA_WH:
                path += "MapGIS/map/wuhan/wuhan.xml";
                MapManager.goodResolution= 2.0839483373047303;
                break;
            case JWT_AREA_HA:
                path += "MapGIS/map/ha/ha.xml";
                MapManager.goodResolution = 0.00001;
                break;
            case JWT_AREA_DZ:
                path += "MapGIS/map/dz/dz.xml";
                break;
            default:
                path += "MapGIS/map/wuhan/wuhan.xml";
                break;
        }
        return path;
    }
}
