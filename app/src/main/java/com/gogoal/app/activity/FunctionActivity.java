package com.gogoal.app.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.gogoal.app.R;
import com.gogoal.app.base.BaseActivity;

import butterknife.BindView;

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
    protected void setStatusBar(int statusBarColorId) {
        super.setStatusBar(statusBarColorId);
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
    }

    private void loadUrl(String url) {
        if (!TextUtils.isEmpty(url)){
            webView.loadUrl(url);
        }
    }
}
