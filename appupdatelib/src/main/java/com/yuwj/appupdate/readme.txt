使用方法说明:
1. 在主模块manifest.xml中声明权限.
    <uses-permission android:name="android.permission.INTERNET" />
    <!-- 可选 ，如果添加这个权限 apk下载在sdcard中的Android/data/包名/cache目录下 否则下载到 内存中的 /data/data/包名/cache中 -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
2. 在主模块manifest.xml中注册DownloadService.
    <service
                android:name="com.loveplusplus.update.DownloadService"
                android:exported="true" />
3. 在代码中进行调用.
    //以对话框形式展现
    UpdateChecker.checkForDialog(context,updateInfoUrl);
    //以对话框形式展现
    UpdateChecker.checkForNotification(context,updateInfoUrl);