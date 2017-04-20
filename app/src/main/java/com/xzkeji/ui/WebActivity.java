package com.xzkeji.ui;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xzkeji.R;
import com.xzkeji.base.BaseActivity;
import com.xzkeji.utils.StringUtil;


/**
 * Web页
 */
public class WebActivity extends BaseActivity {


    private static final int REQUEST_UPLOAD_FILE = 0;   //进入选择文件页面

    private static String mUrl = "https://www.baidu.com";

    private static String mCurrentUrl;  // 当前访问的地址

    boolean mIsFailed = false;


    private com.tencent.smtt.sdk.ValueCallback<Uri[]> mValueCallback = null;//拦截web的请求之后的回调接口。

    private WebView mWebView;
    private View mBackBtn;
    private TextView mTitleTv;
    private View mRefreshBtn;
    private ProgressBar progressBar;
    private TextView mErrorTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        bindViews();
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        init();
        loadUrl(mUrl);
    }

    private void bindViews() {
        mBackBtn = $(R.id.btn_back);
        mTitleTv = $(R.id.tv_title);
        mRefreshBtn = $(R.id.btn_refresh);
        mErrorTv = $(R.id.tv_error);
        progressBar = $(R.id.progress);
        mWebView = $(R.id.view_web);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsFailed = false;
                loadUrl(mCurrentUrl);
            }
        });
    }

    private void init() {
        mWebView.setWebChromeClient(new WebChromeClient() {

            /**腾讯X5webView封装的,用于拦截web打开文件的请求。
             * @param webView               webView
             * @param valueCallback         用于回调接口。将本地找到的uir通过该接口传给web
             * @param fileChooserParams     请求的参数。
             * @return true:该请求已被处理。false:该请求未被处理。
             */
            @Override
            public boolean onShowFileChooser(WebView webView, com.tencent.smtt.sdk.ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
                mValueCallback = valueCallback;
                startActivityForResult(fileChooserParams.createIntent(), REQUEST_UPLOAD_FILE);
                return true;
            }

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                if (StringUtil.isNotEmpty(webView.getTitle())) {
                    mTitleTv.setText(webView.getTitle());
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
                if (StringUtil.isEmpty(url)) {
                    mCurrentUrl = url;
                }
                mHandler.sendEmptyMessage(1);
                if (!mIsFailed) {
                    mWebView.setVisibility(View.VISIBLE);
                    mErrorTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                mIsFailed = true;
                mWebView.setVisibility(View.INVISIBLE);
                mErrorTv.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mCurrentUrl = url;
                return false;
            }
        });

        // 各种设置
        if (mWebView.getX5WebViewExtension() != null) {
            Log.e("robins", "CoreVersion_FromSDK::" + mWebView.getX5WebViewExtension().getQQBrowserVersion());
            mWebView.getX5WebViewExtension()
                    .setWebViewClientExtension(new ProxyWebViewClientExtension() {
                        @Override
                        public Object onMiscCallBack(String method, Bundle bundle) {
                            if ("onSecurityLevelGot".equals(method)) {
                            }
                            return null;
                        }
                    });
        } else {
            Log.e("robins", "CoreVersion");
        }
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);

        long time = System.currentTimeMillis();
        loadUrl(mUrl);

        Log.d("time-cost", "cost time: " + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPLOAD_FILE) {
            if (null == mValueCallback) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (result == null) {
                mValueCallback.onReceiveValue(null);
                mValueCallback = null;
                return;
            }
            mValueCallback.onReceiveValue(new Uri[]{result});
            mValueCallback = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            if (mWebView != null) {
                mWebView.destroy();
            }
            super.onBackPressed();
        }
    }

    private void loadUrl(String url) {

        progressBar.setProgress(1);
        mWebView.loadUrl(url);
        mHandler.sendEmptyMessage(0);
    }

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = progressBar.getProgress();
            if (progress < 100) {
                progressBar.setVisibility(View.VISIBLE);
                switch (msg.what) {
                    case 0:
                        progressBar.setProgress(progress + 1);
                        sendEmptyMessageDelayed(0, 200);
                        break;
                    case 1:
                        progressBar.setProgress(progress + (progress > 70 ? 100 - progress : 30));
                        sendEmptyMessageDelayed(1, 100);
                        break;
                }
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    };


}
