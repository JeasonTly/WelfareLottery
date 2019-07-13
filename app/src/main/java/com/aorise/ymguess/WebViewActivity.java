package com.aorise.ymguess;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.aorise.ymguess.model.WebViewData;
import com.aorise.ymguess.view.X5WebClient;
import com.aorise.ymguess.view.X5WebView;
import com.hjq.toast.ToastUtils;
import com.rance.library.ButtonData;
import com.rance.library.ButtonEventListener;
import com.rance.library.SectorMenuButton;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;
import java.util.List;


/**
 * author : yuanshenbin
 * time   : 2018/10/13
 * desc   : 网页
 */

public class WebViewActivity extends AppCompatActivity {

    private WebViewData query_webViewData;
    private SectorMenuButton mSectorMenuButton;
    private X5WebView mWebView;
    private ImageView imageView;

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //去掉标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.common_activity_webview);

        mSectorMenuButton = findViewById(R.id.center_sector_menu);
        mSectorMenuButton.setVisibility(View.GONE);
        mWebView = (X5WebView) findViewById(R.id.x5webView);
//        Handler mHandler = new Handler();
//        imageView = (ImageView) findViewById(R.id.image_view);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                imageView.setVisibility(View.GONE);
//                mSectorMenuButton.setVisibility(View.VISIBLE);
//                setStatusBarVisible(true);
//            }
//        }, 2000);
        mSectorMenuButton.setVisibility(View.VISIBLE);
        com.tencent.smtt.sdk.WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);


        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(com.tencent.smtt.sdk.WebSettings.PluginState.ON_DEMAND);
        //mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new X5WebClient(this));
        mWebView.setWebChromeClient(new WebChromeClient() {

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }
        });
         mWebView.loadUrl("https://f8cp.i8app.app/");
         // mWebView.loadUrl("https://m.7782a.com/home");
        //mWebView.loadUrl("https://dfcp.isutils.com/");

        initDatas();

    }
    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }
    private void setStatusBarVisible(boolean show) {
        if (show) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }

    private void initDatas() {
//        setTitle(query_webViewData.getTitle());
//        if (!TextUtils.isEmpty(query_webViewData.getTitle())) {
//            setTitle(query_webViewData.getTitle());
//        }

        final List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.drawable.jiahao, R.drawable.shuaxin, R.drawable.qianjin,
                R.drawable.shouye, R.drawable.fanhui, R.drawable.qingli,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher};
        for (int i = 0; i < 6; i++) {
            ButtonData buttonData = ButtonData.buildIconButton(this, drawable[i], 0);
            buttonData.setBackgroundColorId(this, R.color.touming);
            buttonDatas.add(buttonData);
        }
        mSectorMenuButton.setButtonDatas(buttonDatas);
        setListener(mSectorMenuButton);
        //悬浮按钮
        mSectorMenuButton.setOnTouchListener(new OnMoveViewTouchListener(this, 0));
        mSectorMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                KeyboardUtils.controlKeyboard(MainActivity.this, AppConfig.getInstance().getInt(AppConfig.ScreenHight) / 4 * 3);
            }
        });
        // mSectorMenuButton.setVisibility(View.VISIBLE);
    }

    private void setListener(final SectorMenuButton button) {
        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                if (index == 1) {
                    //刷新
                    mWebView.reload();

                } else if (index == 2) {
                    //前进

                    mWebView.goForward();
                } else if (index == 3) {
                    //首页
                    // xWalkView.reload(0);

                    mWebView.loadUrl("https://f8cp.i8app.app/");
                    // mWebView.loadUrl("https://m.7782a.com/home");
                   // mWebView.loadUrl("https://dfcp.isutils.com/");
                } else if (index == 4) {//返回
                    mWebView.goBack();
                } else if (index == 5) {//清楚缓存
                    mWebView.clearCache(true);
                    ToastUtils.show("清除缓存成功!");

                }
            }

            @Override
            public void onExpand() {
            }

            @Override
            public void onCollapse() {
            }
        });
    }


    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

