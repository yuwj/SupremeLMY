package com.zondy.jwt.jwtmobile.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;

/**
 * Created by sheep on 2017/1/9.
 */

public class CommonUtil {
    public static String getDeviceId(Context context) {
        String deviceId = "";
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            deviceId = tm.getDeviceId();
        }
        return deviceId;
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
    public static Bitmap createIndexAnnotationView(Context context,int index, int type, boolean isSelected) {
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

    public static String getResourceUri(Context context,int resId){
        Resources r =context.getResources();
        Uri uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(resId) + "/"
                + r.getResourceTypeName(resId) + "/"
                + r.getResourceEntryName(resId));
        return uri.toString();
    }
}
