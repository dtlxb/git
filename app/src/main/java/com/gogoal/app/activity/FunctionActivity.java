package com.gogoal.app.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.gogoal.app.R;
import com.gogoal.app.base.BaseActivity;
import com.gogoal.app.common.DialogHelp;
import com.gogoal.app.common.GGOKHTTP.GGAPI;
import com.socks.library.KLog;

import butterknife.BindView;

public class FunctionActivity extends BaseActivity {

    @BindView(R.id.webView)
    BridgeWebView webView;

    private String url;

    private String title;
    private int type;

    @Override
    public int bindLayout() {
        return R.layout.activity_function;
    }

    @Override
    public void doBusiness(Context mContext) {

        title = getIntent().getStringExtra("title");
        type = getIntent().getIntExtra("type", 0);

        setMyTitle(title, true);


        initWebView(webView);

        if (type == 1) {
            url = GGAPI.WEB_URL + "/live/list";
        } else if (type == 2) {
            url = GGAPI.WEB_URL + "/report";
        } else {
            url = "file:///android_asset/demo.html";
        }
        webView.loadUrl(url);

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
                KLog.json(data);
//                Intent intent = new Intent(getContext(), PlayerActivity.class);
//                intent.putExtra("live_id", data);
//                startActivity(intent);
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
        mWebView.getSettings().setBuiltInZoomControls(false);

        // 开启DOM缓存。
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDatabasePath(getContext().getApplicationContext().getCacheDir().getAbsolutePath());
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);

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
