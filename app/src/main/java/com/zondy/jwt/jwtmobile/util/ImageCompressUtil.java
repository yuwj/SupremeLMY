package com.zondy.jwt.jwtmobile.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.zondy.jwt.jwtmobile.global.Constant;
import com.zondy.jwt.jwtmobile.view.impl.BufbkFankActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by ywj on 2017/5/8 0008.
 */

public class ImageCompressUtil {
    /**
     * 缩放图片
     *
     * @param imgFromPath 图片来源路径
     * @param toWidth     压缩的目标宽
     * @param toHeight    压缩的目标高
     * @return
     */
    public static String compressImage(String imgFromPath,
                                     int toWidth, int toHeight) {
        if(TextUtils.isEmpty(imgFromPath)){
            return null;
        }
        String imageName = imgFromPath.substring(imgFromPath.lastIndexOf("/")+1,imgFromPath.length());
        if(TextUtils.isEmpty(imageName)){
            return null;
        }
        String imgToPath = Constant.uploadCompressMediaPath+imageName;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 不加载bitmap到内存中
        int originalOutWidth = options.outWidth;
        int originalOutHeight = options.outHeight;

        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 1;

        if (originalOutWidth != 0 && originalOutHeight != 0 && toWidth != 0 && toHeight != 0) {
            float widthSample = (float) originalOutWidth / toWidth;
            if (widthSample < 1) {
                widthSample = 1;
            }
            float heightSample = (float) originalOutHeight / toHeight;
            if (heightSample < 1) {
                heightSample = 1;
            }

            if (widthSample > heightSample) {
                toHeight = (int) (originalOutHeight / widthSample);
            } else {
                toWidth = (int) (originalOutWidth / heightSample);
            }


            int sampleSize = (int) (widthSample > heightSample ? widthSample : heightSample);


            Log.d("Bimp", "sampleSize = " + sampleSize);
            options.inSampleSize = sampleSize;
        }

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imgFromPath, options);

        bitmap = ThumbnailUtils.extractThumbnail(bitmap, toWidth, toHeight);
        File file = new File(imgToPath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                fos.flush();
                fos.close();
                return imgToPath;

            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    /**
     * 利用glide压缩图片
     * @param context
     * @param filePath
     * @param compressCallback
     */
    public void compassImage(final Context context, final String filePath, final CompressCallback compressCallback) {
        if (TextUtils.isEmpty(filePath) || !"jpgjpegpnggif".contains(filePath.substring(filePath.lastIndexOf("."), filePath.length()))) {
            compressCallback.onCompressFail();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                String resultPath = null;
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(context)
                            .load(filePath)
                            .asBitmap()
                            .into(width, height)
                            .get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    // 在这里执行图片保存方法
                    // 首先保存图片
                    File director = new File(Constant.uploadCompressMediaPath);
                    String fileName = filePath.substring(filePath.lastIndexOf("/", filePath.length()));
                    resultPath = director + fileName;
                    if (!director.exists()) {
                        director.mkdirs();
                    }
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(resultPath);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);//压缩率100,表示不压缩
                        fos.flush();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!TextUtils.isEmpty(resultPath)) {
                    compressCallback.onCompressSuccess(resultPath);
                }
            }

        }).start();
    }

    interface CompressCallback {
        void onCompressSuccess(String compressedPath);

        void onCompressFail();
    }

}
