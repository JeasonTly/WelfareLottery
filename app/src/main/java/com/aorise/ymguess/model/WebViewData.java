package com.aorise.ymguess.model;

import java.io.Serializable;

/**
 * author : yuanshenbin
 * time   : 2019/4/26
 * desc   :
 */
public class WebViewData implements Serializable {
    public WebViewData() {
    }

    public WebViewData(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    private String webUrl="";

    private  boolean html;

    private   String title;
    private  String urlHtml;

    public String getUrlHtml() {
        return urlHtml;
    }

    public void setUrlHtml(String urlHtml) {
        this.urlHtml = urlHtml;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }
}
