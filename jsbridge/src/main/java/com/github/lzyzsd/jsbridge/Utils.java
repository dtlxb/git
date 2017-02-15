package com.github.lzyzsd.jsbridge;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.lzyzsd.library.R;

/**
 * author wangjd on 2017/1/17 0017.
 * Staff_id 1375
 * phone 18930640263
 */
public class Utils {

    // 手机网络类型
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    private static DisplayMetrics getDms(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int getScreemWidth(Context context) {
        return getDms(context).widthPixels;
    }

    public static int getScreemHeight(Context context) {
        return getDms(context).heightPixels;
    }

    public static int dp2px(Context context,int dpValue){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density*dpValue+0.5);
    }

    public static Dialog getCustomLoading(Context context, String dialogMsg) {
        Dialog loadDialog = new Dialog(context,com.github.lzyzsd.library.R.style.progress_dialog);
        loadDialog.setContentView(com.github.lzyzsd.library.R.layout.dialog_loading);
        loadDialog.setCancelable(true);
        Window window=loadDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.width = 9*Utils.getScreemWidth(context)/20;
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        TextView msg = (TextView) loadDialog.findViewById(com.github.lzyzsd.library.R.id.tv_loading_msg);
        msg.setText(TextUtils.isEmpty(dialogMsg) ? "加载中..." : dialogMsg);
        ProgressBar progressBar= (ProgressBar) loadDialog.findViewById(R.id.progress_loading);

        ViewGroup.LayoutParams progressParams = progressBar.getLayoutParams();
        progressParams.width=2085*Utils.getScreemWidth(context)/10000;
        progressParams.height=1956*Utils.getScreemWidth(context)/10000;

        loadDialog.setCanceledOnTouchOutside(false);
        return loadDialog;
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */

    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }
}
