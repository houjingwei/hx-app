package com.huixiangtv.liveshow.Js;

import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Stone on 16/6/13.
 */
public class GlobalWebChromeClient extends WebChromeClient {

    private IJsInterface jsInterface;

    public GlobalWebChromeClient(IJsInterface jsInterface) {
        this.jsInterface = jsInterface;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }


}