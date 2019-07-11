package com.aorise.ymguess.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/11.
 */
public class X5WebClient extends WebViewClient {
    private Context mContext;
    public X5WebClient(Context context) {
        mContext = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (!url.startsWith("http")) {
            try {
                // 以下固定写法,表示跳转到第三方应用
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(intent);
              //  mIsDownload = false;  //该字段是用于判断是否需要跳转浏览器下载
            } catch (Exception e) {
                // 防止没有安装的情况
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            return true;//返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
        }
    }
}
