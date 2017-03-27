package com.github.lzyzsd.jsbridge;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.github.lzyzsd.library.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by bruce on 10/28/15.
 */
public class BridgeWebViewClient extends WebViewClient {

    private BridgeWebView webView;

    private PageLoadFinishListener mFinishListener;

    BridgeWebViewClient(BridgeWebView webView, PageLoadFinishListener mFinishListener) {
        this.webView = webView;
        this.mFinishListener = mFinishListener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (url.startsWith(BridgeUtil.YY_BRIDGE_LOADED)) {
            if (BridgeUtil.useEvaluateJS()) {
                BridgeUtil.webViewLoadLocalJs(view, "WebViewJavascriptBridgeGE19.js");
            } else {
                BridgeUtil.webViewLoadLocalJs(view, "WebViewJavascriptBridge.js");
            }

            if (webView.getStartupMessage() != null) {
                for (Message m : webView.getStartupMessage()) {
                    webView.dispatchMessage(m);
                }
                webView.setStartupMessage(null);
            }
            return true;
        } else if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            if (!BridgeUtil.useEvaluateJS()) {
                webView.handlerReturnData(url);
            }
            return true;
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { // 唤起native去拉去数据
            webView.flushMessageQueue();
            return true;
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    //loading
    private View mLoadingView;

    private void showLoadingPage() {
        initLoadingPage();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, Utils.dp2px(webView.getContext(), 2), 0, 0);
        if (webView.getChildCount() < 2) {
            webView.addView(mLoadingView, 0, lp);
        }
    }

    private void hideLoadingPage() {
        if (null != webView.getChildAt(0)) {
            webView.removeViewAt(0);
        }
    }

    private void initLoadingPage() {
        if (mLoadingView == null) {
            mLoadingView = View.inflate(
                    webView.getContext(),
                    R.layout.web_loading,
                    new RelativeLayout(webView.getContext()));
            ImageView imageView = (ImageView) mLoadingView.findViewById(R.id.image_loading);

            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.width = 2 * Utils.getScreemWidth(webView.getContext()) / 5;
            params.height = 4 * Utils.getScreemHeight(webView.getContext()) / 25;
            imageView.setLayoutParams(params);

            Glide.with(webView.getContext()).load(R.mipmap.web_loading).asGif().fitCenter().into(imageView);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        showLoadingPage();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        if (mFinishListener != null) {
            try {
                mFinishListener.onFinish(view.getUrl(), view.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        hideLoadingPage();

    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);

//        if (!view.canGoBack()) {
//            switch (Utils.getNetworkType(view.getContext())) {//获取网络状态
//                case 0://无网络
//                    view.loadUrl("file:///android_asset/404.html");
//                    break;
//                default:
//                    view.loadUrl("file:///android_asset/erro.html");
//                    break;
//            }
//        }
        if (!view.canGoBack()) {
            view.loadUrl("file:///android_asset/404.html");
        }
    }
}