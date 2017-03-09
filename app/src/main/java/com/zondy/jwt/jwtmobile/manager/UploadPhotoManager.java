package com.zondy.jwt.jwtmobile.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.ImageCropBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

/**
 * Created by ywj on 2017/3/6 0006.
 */

public class UploadPhotoManager {

    Activity context;
    UploadPhotoListener uploadPhotoListener;
    ProgressDialog prodressDialog;


    public UploadPhotoManager(Activity context, UploadPhotoListener uploadPhotoListener) {
        this.context = context;
        this.uploadPhotoListener = uploadPhotoListener;
        prodressDialog = new ProgressDialog(context, ProgressDialog.STYLE_HORIZONTAL);
    }


    public void showPickPhoto(final ImageView iv){
        RxGalleryFinal
                .with(context)
                .image()
                .radio()
                .crop()
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        //图片选择结果
                        ImageCropBean b = imageRadioResultEvent.getResult();
                        String path = b.getOriginalPath();
                        File f = new File(path);
                        if(f.exists()){

                            uploadFile("http://www.baidu.com",f,"param");
                            Bitmap bb = BitmapFactory.decodeFile(f.getAbsolutePath());
                            iv.setImageBitmap(bb);
                        }
                    }
                })
                .openGallery();
    }

    public void showProgressDialog() {
        if (!prodressDialog.isShowing()) {
            prodressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if ((prodressDialog.isShowing())) {
            prodressDialog.dismiss();
        }
    }


    /**
     * fileName 文件名(不带后缀)
     * filePath 文件的本地路径 (xxx / xx / test.jpg)
     */
    public void uploadFile(final String uploadUrl, final File file, final String param) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressDialog();
                    }
                });
                HttpURLConnection conn = null;

                /// boundary就是request头和上传文件内容的分隔符(可自定义任意一组字符串)
                String BOUNDARY = "******";
                // 用来标识payLoad+文件流的起始位置和终止位置(相当于一个协议,告诉你从哪开始,从哪结束)
                String preFix = ("\r\n--" + BOUNDARY + "--\r\n");

                try {
                    // (HttpConst.uploadImage 上传到服务器的地址
                    URL url = new URL(uploadUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(30000);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    // 设置请求方法
                    conn.setRequestMethod("POST");
                    // 设置header
                    conn.setRequestProperty("Accept", "*/*");
                    conn.setRequestProperty("Connection", "keep-alive");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=" + BOUNDARY);
                    // 获取写输入流
                    OutputStream out = new DataOutputStream(conn.getOutputStream());
                    // 获取上传文件
//            File file = new File(filePath);

                    // 要上传的数据
                    StringBuffer strBuf = new StringBuffer();

                    // 标识payLoad + 文件流的起始位置
                    strBuf.append(preFix);

                    // 下面这三行代码,用来标识服务器表单接收文件的name和filename的格式
                    // 在这里,我们是file和filename.后缀[后缀是必须的]。
                    // 这里的fileName必须加个.jpg,因为后台会判断这个东西。
                    // 这里的Content-Type的类型,必须与fileName的后缀一致。
                    // 如果不太明白,可以问一下后台同事,反正这里的name和fileName得与后台协定！
                    // 这里只要把.jpg改成.txt，把Content-Type改成上传文本的类型，就能上传txt文件了。
                    String fileName = file.getName();
                    strBuf.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + ".jpg" + "\"\r\n");
                    strBuf.append("Content-Type: image/jpeg" + "\r\n\r\n");

                    out.write(strBuf.toString().getBytes());

                    // 获取文件流
                    FileInputStream fileInputStream = new FileInputStream(file);
                    DataInputStream inputStream = new DataInputStream(fileInputStream);

                    // 每次上传文件的大小(文件会被拆成几份上传)
                    int bytes = 0;
                    // 计算上传进度
                    float count = 0;
                    // 获取文件总大小
                    int fileSize = fileInputStream.available();
                    // 每次上传的大小
                    byte[] bufferOut = new byte[1024];
                    // 上传文件
                    while ((bytes = inputStream.read(bufferOut)) != -1) {
                        // 上传文件(一份)
                        out.write(bufferOut, 0, bytes);
                        // 计算当前已上传的大小
                        count += bytes;
                        // 打印上传文件进度(已上传除以总大*100就是进度)
                        Log.i(this.getClass().getSimpleName(), "progress:" + (count / fileSize * 100) + "%");
                    }

                    // 关闭文件流
                    inputStream.close();

                    // 标识payLoad + 文件流的结尾位置
                    out.write(preFix.getBytes());

                    // 至此上传代码完毕

                    // 总结上传数据的流程：preFix + payLoad(标识服务器表单接收文件的格式) + 文件(以流的形式) + preFix
                    // 文本与图片的不同,仅仅只在payLoad那一处的后缀的不同而已。

                    // 输出所有数据到服务器
                    out.flush();

                    // 关闭网络输出流
                    out.close();

                    // 重新构造一个StringBuffer,用来存放从服务器获取到的数据
                    strBuf = new StringBuffer();

                    // 打开输入流 , 读取服务器返回的数据
                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(conn.getInputStream()));

                    String line;

                    // 一行一行的读取服务器返回的数据
                    while ((line = reader.readLine()) != null) {
                        strBuf.append(line).append("\n");
                    }

                    // 关闭输入流
                    reader.close();
                    // 打印服务器返回的数据
                    Log.i(this.getClass().getSimpleName(), "上传成功:" + strBuf.toString());

                    try {
                        JSONObject obj = new JSONObject(strBuf.toString());
                        String imgUrl = obj.optString("imgUrl", "");
                        if (!TextUtils.isEmpty(imgUrl)) {
                            if (uploadPhotoListener != null) {
                                uploadPhotoListener.onUploadSuccess(strBuf.toString());
                            }
                        } else {
                            if (uploadPhotoListener != null) {
                                uploadPhotoListener.onUploadFailed("上传失败,请重新上传");
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    Log.i(this.getClass().getSimpleName(), "上传图片出错:" + e.toString());

                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgressDialog();
                        }
                    });
                }

            }
        }).start();
    }

    public interface UploadPhotoListener {
        void onUploadSuccess(String imgUrl);

        void onUploadFailed(String msg);

    }
}
