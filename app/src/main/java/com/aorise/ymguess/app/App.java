package com.aorise.ymguess.app;

import android.app.Application;
import android.util.Log;


import com.hjq.toast.ToastUtils;
import com.tencent.smtt.sdk.QbSdk;

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
//        QbSdk.setDownloadWithoutWifi(true);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " arg0 = " + arg0);
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}
