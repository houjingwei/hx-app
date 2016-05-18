package com.huixiangtv.live.activity;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huixiangtv.live.R;
import com.huixiangtv.live.utils.ForwardUtils;
import com.huixiangtv.live.webview.WVJBWebViewClient;

import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@SuppressLint("SetJavaScriptEnabled")
public class H5Activity extends BaseBackActivity implements View.OnClickListener {

    public static final String support = "support,getUserInfo,login,closeWebView,getDeviceInfo,copy,setShareInfo,jumpPage,getUserAgent,setBgColor,uploadCallback,loginSuccess,payCallback";

    String url = "", title = "";

    @ViewInject(R.id.title)
    TextView tvTitle;
    @ViewInject(R.id.back)
    ImageView ivBack;
    @ViewInject(R.id.webView)
    WebView webview;
    @ViewInject(R.id.progress)
    ProgressBar progress;
    private WVJBWebViewClient webViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);
        x.view().inject(this);
        ivBack.setOnClickListener(this);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        tvTitle.setText(title);

        WebChromeClient webChromeClient = new WebChromeClient(){

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tvTitle.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progress.setVisibility(View.GONE);
                } else {
                    if (progress.getVisibility() == View.GONE)
                        progress.setVisibility(View.VISIBLE);
                    progress.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        };
        webview.setWebChromeClient(webChromeClient);
        webViewClient = new MyWebViewClient(webview);
        webViewClient.enableLogging();
        webview.setWebViewClient(webViewClient);
        webview.loadUrl(getIntent().getStringExtra("url"));
    }



    class MyWebViewClient extends WVJBWebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            //处理代码
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //处理代码
            if(url.startsWith("mglive://")){
                ForwardUtils.target(H5Activity.this,url,null);
                return true ;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        public MyWebViewClient(WebView webView) {

            super(webView, new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    callback.callback("Response for message from ObjC!");
                }
            });

            enableLogging();

            //是否支持该方法
            registerHandler("support", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    if (data != null && H5Activity.support.indexOf(data.toString()) != -1) {
                        callback.callback("{\"status\":\"yes\"}");
                    }
                }
            });
            //获得用户信息
            registerHandler("getUserInfo", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                }
            });
            //获得UserAgent
            registerHandler("getUserAgent", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                }
            });
            //登陆
            registerHandler("login", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                }
            });
            //关闭webview
            registerHandler("closeWebView", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    H5Activity.this.onBackPressed();
                    callback.callback("{\"status\":\"yes\"}");
                }
            });
            //获得设备信息
            registerHandler("getDeviceInfo", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                }
            });

            //拷贝
            registerHandler("copy", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    if (data != null) {
                        try{
                            JSONObject ja = new JSONObject(data.toString());
                            ClipboardManager clip = (ClipboardManager) H5Activity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                            clip.setText(ja.getString("content"));

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    callback.callback("{\"status\":\"yes\"}");
                }
            });

            //分享
            registerHandler("setShareInfo", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                }
            });

            //跳转URL
            registerHandler("jumpPage", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    if (data != null) {
                        try{
                            JSONObject ja = new JSONObject(data.toString());
                            Uri uri = Uri.parse(ja.getString("url"));
                            Intent it = new Intent(Intent.ACTION_VIEW, uri);
                            H5Activity.this.startActivity(it);

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    callback.callback("{\"status\":\"yes\"}");
                }
            });

            //设置背景颜色
            registerHandler("setBgColor", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    try{

                        JSONObject ja = new JSONObject(data.toString());

                        int r = ja.getInt("r");

                        int g = ja.getInt("g");

                        int b = ja.getInt("b");

                        webview.setBackgroundColor(Color.argb(1,r,g,b));

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.back){
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //页面激活
        webViewClient.callHandler("reActive");
    }

}


