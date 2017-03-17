package cn.gogoal.im.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.socks.library.KLog;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.ui.view.XTitle;

import static cn.gogoal.im.base.MyApp.getContext;

public class FunctionActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    XTitle title_bar;

    @BindView(R.id.webView)
    BridgeWebView webView;

    private String title;

    @Override
    public int bindLayout() {
        return R.layout.activity_function;
    }

    @Override
    public void doBusiness(Context mContext) {

        title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("function_url");

        setMyTitle(title, false);
        //设置返回
        title_bar.setLeftImageResource(R.mipmap.image_title_back_0);
        title_bar.setLeftText("返回");
        title_bar.setLeftTextColor(Color.BLACK);
        title_bar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });

        initWebView(webView);
        webView.loadUrl(url);

//        // H5页面跳转时获取页面title和url
//        webView.setOnWebChangeListener(new BridgeWebView.WebChangeListener() {
//            @Override
//            public void getWebInfo(String url, String title) {
//                KLog.e(url + title);
//            }
//        });

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

                Intent intent = new Intent(getContext(), PlayerActivity.class);
                intent.putExtra("live_id", object.getString("live_id"));
                intent.putExtra("source", object.getString("source"));
                startActivity(intent);
            }
        });

        /*pdf阅读*/
        webView.registerHandler("loadPdfFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, ValueCallback<String> function) {
                if (!TextUtils.isEmpty(data)) {

                    Intent intent = new Intent(FunctionActivity.this, PdfDisplayActivity.class);
                    intent.putExtra("pdf_data", data);
                    startActivity(intent);
                }
            }
        });


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

    private void initWebView(BridgeWebView mWebView) {
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

        mWebView.setWebChromeClient(new WebChromeClient());//启动JS弹窗
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
