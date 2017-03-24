package cn.gogoal.im.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

/**
 * js交互
 */
public class WebViewUtil {

    private Context mContext;
    private Activity mActivity;

    public WebViewUtil(Context mContext, Activity activity) {
        this.mContext = mContext;
        this.mActivity = activity;
    }

    @JavascriptInterface
    public void toast(String s) {
        UIHelper.toast(mContext, s);
    }

    @JavascriptInterface
    public String getToken() {
        return UserUtils.getToken();
    }
}
