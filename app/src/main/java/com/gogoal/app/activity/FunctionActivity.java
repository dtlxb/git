package com.gogoal.app.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.EditText;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.gogoal.app.R;
import com.gogoal.app.base.BaseActivity;
import com.gogoal.app.common.DialogHelp;

import butterknife.BindView;
import butterknife.OnClick;

public class FunctionActivity extends BaseActivity {

    @BindView(R.id.webView)
    BridgeWebView webView;

    @BindView(R.id.et_input)
    EditText etInput;

    @Override
    public int bindLayout() {
        return R.layout.activity_function;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("web测试",true);

        etInput.setSelection(etInput.getText().length());

        etInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    loadUrl(etInput.getText().toString());
                    return true;
                }
                return false;
            }
        });

        initWebView(webView);

        webView.loadUrl("file:///android_asset/demo.html");

        //1.添加原生方法，测试弹窗
        webView.registerHandler("naviveDialog", new BridgeHandler() {
            @Override
            public void handler(String data, final ValueCallback<String> function) {
                DialogHelp.getMessageDialog(FunctionActivity.this,data).show();
            }
        });

        //1.添加原生方法，带回调
        webView.registerHandler("naviveDialogWithCallBack", new BridgeHandler() {
            @Override
            public void handler(String data, final ValueCallback<String> function) {
                DialogHelp.getMessageDialog(FunctionActivity.this,data)
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

    }

    @OnClick(R.id.fab)
    void doJs(View view){
        webView.callHandler("methodFromJs","这是一段来自java的数据", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String data) {
                //调通js方法methodFromJs后的返回
                DialogHelp.getMessageDialog(FunctionActivity.this,"调用js方法后的回调=="+data).show();
            }
        });
    }

    private void loadUrl(String url) {
        if (!TextUtils.isEmpty(url)){
            webView.loadUrl(url);
        }
    }

    private void initWebView(BridgeWebView mWebView) {
        mWebView.getSettings().setBuiltInZoomControls(false);

        // 开启DOM缓存。
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDatabasePath(getActivity().getApplicationContext().getCacheDir().getAbsolutePath());
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient());//启动JS弹窗
    }
}
