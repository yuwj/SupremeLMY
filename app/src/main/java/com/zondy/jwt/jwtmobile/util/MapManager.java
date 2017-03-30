package com.zondy.jwt.jwtmobile.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.entity.EntityLocation;
import com.zondy.jwt.jwtmobile.global.Constant;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.annotation.AnnotationsOverlay;
import com.zondy.mapgis.android.environment.Environment;
import com.zondy.mapgis.android.graphic.GraphicImage;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.core.geometry.Dot;
import com.zondy.mapgis.core.geometry.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywj on 2017/3/12 0012.
 */

public class MapManager {
    MapView mapView;
    Context context;
    public static double goodResolution = 0.00001;//地图展现比较好的分辨率

    public MapManager(MapView mapView, Context context) {
        this.mapView = mapView;
        this.context = context;
    }

    /**
     * 初始化地图,包括地图注册,地图加载,气泡显示.
     * @param mapPath
     * @param mapLoadListner
     * @param mapAnnotationListener
     */
    public void initMap(final String mapPath, final MapLoadListner mapLoadListner, final MapAnnotationListener mapAnnotationListener) {
        Environment.requestAuthorization(context, new Environment.AuthorizeCallback() {
            @Override
            public void onComplete() {
                mapView.loadFromFileAsync(mapPath);

                mapView.setMapLoadListener(new MapView.MapViewMapLoadListener() {
                    @Override
                    public void mapViewWillStartLoadingMap(MapView mapView, String s) {

                    }

                    @Override
                    public void mapViewDidFinishLoadingMap(MapView mapView, String s) {
//显示缩放按钮
                        mapView.setZoomControlsEnabled(true);
                        //显示指南针按钮
                        mapView.setShowNorthArrow(true);
                        //显示图标
                        mapView.setShowLogo(true);
                        //显示比例尺
                        mapView.setShowScaleBar(true);
//双指单击缩小
                        mapView.setTwoFingerTapZooming(true);
                        //单指双击变大
                        mapView.setDoubleTapZooming(true);

                        // 自由缩放地图
                        mapView.setMapZoomGesturesEnabled(true);
// 手势滑动地图
                        mapView.setMapPanGesturesEnabled(true);
//        倾斜（双指竖直下滑）
                        mapView.setMapSlopeGesturesEnabled(true);
                        //双指旋转地图
                        mapView.setMapRotateGesturesEnabled(true);
                        mapView.setAnnotationListener(new MapView.MapViewAnnotationListener() {
                            @Override
                            public void mapViewClickAnnotation(MapView mapView, Annotation annotation) {

                            }

                            @Override
                            public boolean mapViewWillShowAnnotationView(MapView mapView, AnnotationView annotationView) {
                                return false;
                            }

                            @Override
                            public boolean mapViewWillHideAnnotationView(MapView mapView, AnnotationView annotationView) {
                                return false;
                            }

                            @Override
                            public AnnotationView mapViewViewForAnnotation(MapView mapView, Annotation annotation) {
                                return mapAnnotationListener.createAnnotationView(annotation);
                            }

                            @Override
                            public void mapViewClickAnnotationView(MapView mapView, AnnotationView annotationView) {

                            }
                        });
                        mapLoadListner.onMapLoadSuccess();
                    }

                    @Override
                    public void mapViewDidFailLoadingMap(MapView mapView, String s) {
                        mapLoadListner.onMapLoadFail();
                    }
                });

            }
        });


    }


    public void addAnnotaion(Annotation annotation) {
        AnnotationsOverlay annotationsOverlay = mapView.getAnnotationsOverlay();
        annotationsOverlay.addAnnotation(annotation);
        // 将annotationview平移到视图中心
        mapView.zoomToCenter(annotation.getPoint(), goodResolution, false);
        mapView.refresh();
    }

    public interface MapLoadListner {
        void onMapLoadSuccess();

        void onMapLoadFail();
    }

    public interface MapAnnotationListener{
        AnnotationView createAnnotationView(Annotation annotation);
    }


    /**
     * 经纬度转墨卡托
     *
     * @param lon
     * @param lat
     * @return
     */
    public double[] lonLat2Mercator(double lon, double lat) {
        double[] xy = new double[2];
        double x = lon * 20037508.342789 / 180;
        double y = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
        y = y * 20037508.34789 / 180;
        xy[0] = x;
        xy[1] = y;
        return xy;
    }

    /**
     * 墨卡托转经纬度
     *
     * @param mercatorX
     * @param mercatorY
     * @return
     */
    public double[] Mercator2lonLat(double mercatorX, double mercatorY) {
        double[] xy = new double[2];
        double x = mercatorX / 20037508.34 * 180;
        double y = mercatorY / 20037508.34 * 180;
        y = 180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2);
        xy[0] = x;
        xy[1] = y;
        return xy;
    }


    /**
     *  创建带数字的标注.
     * @param index
     *            索引,从0开始,index0显示为1
     * @param type
     *            1-生成123这样的图标,2-生成ABC这样的图标
     * @param isSelected
     * 				是否被选中,未选中为红色,选中为蓝色.
     * @return
     */
    public Bitmap createIndexAnnotationView(int index, int type,boolean isSelected) {
        RelativeLayout view = (RelativeLayout) View.inflate(context,
                R.layout.content_anno, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_value);
        if(isSelected){
            view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_position_red_type2));
        }else{
            view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_position_blue_type2));
        }

        switch (type) {
            case 1:
                tv.setText((index + 1) + "");
                break;
            case 2:
                char c = (char) (65 + index);
                tv.setText(String.valueOf(c));
                break;

            default:
                tv.setText((index + 1) + "");
                break;
        }
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 地图缩放到指定范围,将所有指定点都显示在地图中。
     *
     * @param dots
     */
    public void zoomToRange(List<Dot> dots) {
        if (dots == null || dots.size() <= 0) {
            return;
        }
        double xMin = 0;
        double xMax = 0;
        double yMin = 0;
        double yMax = 0;
        for (int i = 0;i<dots.size();i++) {
            Dot dot = dots.get(i);
            if(i == 0){
                xMin = dot.getX();
                xMax = dot.getX();
                yMin = dot.getY();
                yMax = dot.getY();
            }else{
                xMin = xMin < dot.getX() ? xMin : dot.getX();
                xMax = xMax > dot.getX() ? xMax : dot.getX();
                yMin = yMin < dot.getY() ? yMin : dot.getY();
                yMax = yMax > dot.getY() ? yMax : dot.getY();
            }
        }
        Rect rect = new Rect(xMin, yMin, xMax, yMax);
        mapView.zoomToRange(rect, false);
        mapView.refresh();
    }

    GraphicImage graphicImageCurrentPosition;
    public void updateUserLocation(EntityLocation location){
        if(graphicImageCurrentPosition == null){

            graphicImageCurrentPosition = new GraphicImage();
            graphicImageCurrentPosition.setImage(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_user_position));
            mapView.getGraphicsOverlay().addGraphic(graphicImageCurrentPosition);

        }
        if(location != null){
            ToastTool.getInstance().shortLength(context,"更新位置",true);
            Dot dot = null;
            switch (Constant.JWT_AREA_SELECTED){
                case Constant.JWT_AREA_HA:
                    dot = new Dot(location.getLongitude(),location.getLatitude());
                    break;
                case Constant.JWT_AREA_WH:
                    double[] xy = lonLat2Mercator(location.getLongitude(),location.getLatitude());
                    dot = new Dot(xy[0],xy[1]);
                    break;
                default:
                    dot = new Dot(location.getLongitude(),location.getLatitude());
                    break;
            }


            graphicImageCurrentPosition.setPoint(dot);
            graphicImageCurrentPosition.setRotateAngle(location.getBearing());

        }
        mapView.refresh();
    }
}
