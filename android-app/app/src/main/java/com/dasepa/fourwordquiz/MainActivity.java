package com.dasepa.fourwordquiz;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;

public final class MainActivity extends Activity {
    private static final String APP_ASSET_HOST = "appassets.androidplatform.net";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setStatusBarColor(Color.rgb(255, 248, 233));
        window.setNavigationBarColor(Color.rgb(255, 248, 233));
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        );

        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(255, 248, 233));
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);

        WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();
        webView.setWebViewClient(new WebViewClientCompat() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }

            @Override
            @SuppressWarnings("deprecation")
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return assetLoader.shouldInterceptRequest(Uri.parse(url));
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return !APP_ASSET_HOST.equals(request.getUrl().getHost());
            }
        });
        setContentView(webView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        webView.loadUrl("https://" + APP_ASSET_HOST + "/assets/index.html");
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
