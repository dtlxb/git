package cn.gogoal.im.ui.copy;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;

import hply.com.niugu.R;
import hply.com.niugu.StringUtils;

;

/**
 * Created by lixs on 2015/10/8.
 */
@SuppressWarnings("deprecation")
public class GoGoalWebView extends WebView {

    private ProgressBar progressbar;
    private LinearLayout loading;
    private Context mContext;

    private GoGoalWebView myView = this;

    public GoGoalWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        progressbar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5, 0, -2));
        progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.news_content_progress_style));
        addView(progressbar);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getSettings().getAllowFileAccess();
        getSettings().setAllowFileAccess(true);
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoadingPage();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoadingPage();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                view.loadUrl("file:///android_asset/404.html");
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                WebResourceResponse res = null;
                if (url.endsWith("/uncheck.png")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("uncheck.png");
                        res = new WebResourceResponse("image/png", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/checkUpdata.png")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("checkUpdata.png");
                        res = new WebResourceResponse("image/png", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/first_1.png")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("first_1.png");
                        res = new WebResourceResponse("image/png", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/second_2.png")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("second_2.png");
                        res = new WebResourceResponse("image/png", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/third_3.png")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("third_3.png");
                        res = new WebResourceResponse("image/png", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/loading_fresh.png")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("loading_fresh.png");
                        res = new WebResourceResponse("image/png", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/close.png")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("close.png");
                        res = new WebResourceResponse("image/png", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/favicon.png")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("favicon.png");
                        res = new WebResourceResponse("image/png", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/headline_normal.png")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("headline_normal.png");
                        res = new WebResourceResponse("image/png", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/headline_press.png")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("headline_press.png");
                        res = new WebResourceResponse("image/png", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/nodata.png")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("nodata.png");
                        res = new WebResourceResponse("image/png", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/weixin.png")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("weixin.png");
                        res = new WebResourceResponse("image/png", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/angular.min.js")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("angular.min.js");
                        res = new WebResourceResponse("text/js", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/angular-animate.min.js")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("angular-animate.min.js");
                        res = new WebResourceResponse("text/js", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/angular-sanitize.min.js")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("angular-sanitize.min.js");
                        res = new WebResourceResponse("text/js", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("/angular-ui-router.min.js")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("angular-ui-router.min.js");
                        res = new WebResourceResponse("text/js", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("bootstrap.min.css")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("bootstrap.min.css");
                        res = new WebResourceResponse("text/css", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("require.js")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("require.js");
                        res = new WebResourceResponse("text/js", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                } else if (url.endsWith("ui-bootstrap-tpls.min.js")) {
                    try {
                        InputStream localCopy = mContext.getAssets().open("ui-bootstrap-tpls.min.js");
                        res = new WebResourceResponse("text/js", "UTF-8", localCopy);
                    } catch (IOException e) {
                    }
                }
                return res;
            }
        });
        setWebChromeClient(new WebChromeClient());
    }

    public ProgressBar getProgressBar() {
        return progressbar;
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }

    /**
     * 显示自定义错误提示页面，用一个View覆盖在WebView
     */
    View mLoadingView;

    protected void showLoadingPage() {
        initLoadingPage();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.setMargins(0, StringUtils.dp2px(mContext, 2), 0, 0);
        if (myView.getChildCount() < 2) {
            addView(mLoadingView, 0, lp);
        }
    }

    protected void hideLoadingPage() {
        if (null != getChildAt(0)) {
            removeViewAt(0);
        }
    }

    protected void initLoadingPage() {
        if (mLoadingView == null) {
            mLoadingView = View.inflate(mContext, R.layout.web_loading, null);
        }
    }

}
