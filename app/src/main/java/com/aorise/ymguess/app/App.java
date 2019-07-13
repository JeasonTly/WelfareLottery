package com.aorise.ymguess.app;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.hjq.toast.ToastUtils;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.util.HashMap;

/**
 * author : yuanshenbin
 * time   : 2019/4/26
 * desc   :
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
//        if (!QbSdk.isTbsCoreInited()) {
//            QbSdk.preInit(getApplicationContext(), null);// 设置X5初始化完成的回调接口
//        }
        QbSdk.setDownloadWithoutWifi(true);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        QbSdk.initTbsSettings(map);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                QbSdk.preInit(getApplicationContext(),cb);
//                //QbSdk.preinstallStaticTbs(getApplicationContext());
//            }
//        }).start();


        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
    private QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
        @Override
        public void onViewInitFinished(boolean arg0) {
            //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            //必须先使用splashActivity ，这样可以先启动X5内核，再去打开webview
            Log.d("appTLY", " 1111111 arg0 = " + arg0);
        }

        @Override
        public void onCoreInitFinished() {

        }
    };
}
