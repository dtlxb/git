package cn.gogoal.im.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.socks.library.KLog;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.PdfData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.WebViewUtil;
import cn.gogoal.im.ui.view.XTitle;

/*
* web页面
* */
public class FunctionActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    XTitle title_bar;

    @BindView(R.id.webView)
    BridgeWebView webView;

    private String title;
    private boolean needShare;
    private XTitle xTitle;

    @Override
    public int bindLayout() {
        return R.layout.activity_function;
    }

    //    d4d0f74e3c87483cb3bf91dd10dc53f8
    @Override
    public void doBusiness(final Context mContext) {

        getSupportFragmentManager().beginTransaction();
        //接收传值
        title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("function_url");
        KLog.e(url);
        needShare = getIntent().getBooleanExtra("need_share", false);

        //初始化标题
        if (!StringUtils.isActuallyEmpty(title)) {
            xTitle = setMyTitle(title, true);
        } else {
            xTitle = setMyTitle("", true);
        }

        //分享web页
        if (needShare) {
            xTitle.addAction(new XTitle.TextAction(getString(R.string.str_share)) {
                @Override
                public void actionClick(View view) {
                    //TODO web页分享
                }
            });
        }

        initWebView(webView);

        webView.loadUrl(url);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        //添加让web获取用户信息
        /*webView.registerHandler("getUserInfo", new BridgeHandler() {
            @Override
            public void handler(String data, ValueCallback<String> function) {
                function.onReceiveValue(UserUtils.getUserInfo().toJSONString());
            }
        });*/

//        // H5页面跳转时获取页面title和url
        webView.setOnWebChangeListener(new BridgeWebView.WebChangeListener() {
            @Override
            public void getWebInfo(String url, String title) {
//                KLog.e("url==="+url + ";title==="+title);
            }
        });

        //1.添加原生方法，测试弹窗
        webView.registerHandler("naviveDialog", new BridgeHandler() {
            @Override
            public void handler(String data, final ValueCallback<String> function) {
                DialogHelp.getMessageDialog(FunctionActivity.this, data).show();
            }
        });

        //1.添加原生方法，带回调
        webView.registerHandler("naviveDialogWithCallBack", new BridgeHandler() {
            @Override
            public void handler(String data, final ValueCallback<String> function) {
                DialogHelp.getMessageDialog(FunctionActivity.this, data)
                        .setPositiveButton("是的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                function.onReceiveValue("原生点击了[是]");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                function.onReceiveValue("原生点击了[否]");
                            }
                        })
                        .show();
            }
        });

        //添加Go_Goal直播中的页面跳转
        webView.registerHandler("JumpPlayerDetial", new BridgeHandler() {
            @Override
            public void handler(String data, ValueCallback<String> function) {
                JSONObject object = JSONObject.parseObject(data);
                Intent intent = null;
                if (object.getString("source").equals("live")) {
                    intent = new Intent(getContext(), WatchLiveActivity.class);
                } else if (object.getString("source").equals("video")) {
                    intent = new Intent(getContext(), PlayerActivity.class);
                }

                intent.putExtra("live_id", object.getString("live_id"));
                startActivity(intent);
            }
        });

        /*pdf阅读*/
        webView.registerHandler("loadPdfFromWeb", new BridgeHandler() {
            @Override
            public void handler(final String data, ValueCallback<String> function) {
                if (AppDevice.getNetworkType(getContext()) == 2 || AppDevice.getNetworkType(getContext()) == 3) {
                    showPdf(data);
                } else if (AppDevice.getNetworkType(getContext()) == 1) {
                    showPdf(data);
                } else {
                    UIHelper.toastError(mContext, "跳转出错", null);
                }

            }
        });

        //添加发起直播填写信息中的页面跳转
        webView.registerHandler("applyComplete", new BridgeHandler() {
            @Override
            public void handler(String data, ValueCallback<String> function) {
                JSONObject object = JSONObject.parseObject(data);
                Intent intent = new Intent(getContext(), LiveActivity.class);
                intent.putExtra("live_id", object.getString("live_id"));
                startActivity(intent);
                finish();
            }
        });
    }

    private void showPdf(String data) {
        if (!TextUtils.isEmpty(data)) {
            PdfData pdfData = JSONObject.parseObject(data, PdfData.class);
            NormalIntentUtils.go2PdfDisplayActivity(
                    getActivity(),
                    pdfData.getPdfUrl(),
                    pdfData.getTitle());
        }
    }

    /*@OnClick(R.id.fab)
    void doJs(View view) {
        webView.callHandler("methodFromJs", "这是一段来自java的数据", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String data) {
                //调通js方法methodFromJs后的返回
                DialogHelp.getMessageDialog(FunctionActivity.this, "调用js方法后的回调==" + data).show();
            }
        });
    }*/

    private void loadUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    private FunctionActivity getContext() {
        return FunctionActivity.this;
    }

    private void initWebView(BridgeWebView mWebView) {
        mWebView.addJavascriptInterface(new WebViewUtil(this, this), "interaction");

        WebSettings settings = mWebView.getSettings();
        mWebView.setDefaultHandler(new DefaultHandler());

        settings.setBuiltInZoomControls(false);

        String ua = mWebView.getSettings().getUserAgentString() + " GoGoaler/" + AppDevice.getAppVersionName(getContext());
        mWebView.getSettings().setUserAgentString(ua);

        // 开启DOM缓存。
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(getApplicationContext().getCacheDir().getAbsolutePath());
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
