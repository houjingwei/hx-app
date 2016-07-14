package com.huixiangtv.liveshow.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.huixiangtv.liveshow.Js.GlobalWebChromeClient;
import com.huixiangtv.liveshow.Js.IJsInterface;
import com.huixiangtv.liveshow.Js.JsInterfaceFactory;
import com.huixiangtv.liveshow.R;
import com.huixiangtv.liveshow.ui.CommonTitle;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Stone on 16/6/13.
 */
public class WebActivity extends  BaseBackActivity {

    private IJsInterface jsInterface = JsInterfaceFactory
            .createJsInterface(this);


    @ViewInject(R.id.myTitle)
    CommonTitle commonTitle;

    @ViewInject(R.id.wv_ll)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        x.view().inject(this);
        commonTitle.setActivity(this);
        commonTitle.setTitleText(getResources().getString(R.string.bann_details));
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        webView.addJavascriptInterface(jsInterface, IJsInterface.NAME);
        webView.setWebChromeClient(new GlobalWebChromeClient(jsInterface));
        if (url == null || url.equals("")) {
        } else {
            webView.loadUrl(url);
        }
    }
}