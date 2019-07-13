package com.aorise.ymguess;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


/**
 */
public class SplashActivity extends AppCompatActivity {

    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    List<String> mPermissionList = new ArrayList<>();

    private final int mRequestCode = 100;//权限请求码
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Log.d("appTLY","手机厂商"+Build.MANUFACTURER);
        initPermission();
    }



    //权限判断和申请
    private void initPermission() {

        mPermissionList.clear();//清空没有通过的权限

        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }

        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        }else{
            Log.d("appTLY","启动WebViewActivity");
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            startActivity(new Intent(SplashActivity.this, WebViewActivity.class));
                            finish();
                        }
                    }, 1000);
        }
    }


    //请求权限后回调的方法
    //参数： requestCode  是我们自己定义的权限请求码
    //参数： permissions  是我们请求的权限名称数组
    //参数： grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mRequestCode == requestCode) {
            if ((grantResults[0] == PERMISSION_GRANTED)&&(grantResults[1] == PERMISSION_GRANTED) ){
                QbSdk.initX5Environment(getApplicationContext(), cb);
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(SplashActivity.this, WebViewActivity.class));
                                finish();
                            }
                        }, 1500);
            }
            //如果有权限没有被允许

        }

    }
    private QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
        @Override
        public void onViewInitFinished(boolean arg0) {
            //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            //必须先使用splashActivity ，这样可以先启动X5内核，再去打开webview
            Log.d("appTLY", " 0000 arg0 = " + arg0);
        }

        @Override
        public void onCoreInitFinished() {

        }
    };
}
