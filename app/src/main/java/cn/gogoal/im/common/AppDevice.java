package cn.gogoal.im.common;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.gogoal.im.base.MyApp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.ui.dialog.NormalSingleAlertDialog;
import cn.gogoal.im.ui.dialog.UpdataDialog;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AppDevice {

    public static final int DPI480P = 480;
    public static final int DPI720P = 720;
    public static final int DPI1080P = 1080;

    // 手机网络类型
    private static final int NETTYPE_WIFI = 0x01;
    private static final int NETTYPE_CMWAP = 0x02;
    private static final int NETTYPE_CMNET = 0x03;
    private static boolean GTE_HC;
    private static boolean GTE_ICS;
    private static boolean PRE_HC;
    private static Boolean _hasCamera = null;

    static {
        GTE_ICS = Build.VERSION.SDK_INT >= 14;
        GTE_HC = Build.VERSION.SDK_INT >= 11;
        PRE_HC = Build.VERSION.SDK_INT < 11;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    @Deprecated
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f * (dpValue >= 0 ? 1 : -1));
    }

    public static int dp2px(float dpValue) {
        final float scale = MyApp.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f * (dpValue >= 0 ? 1 : -1));
    }


    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f * (pxValue >= 0 ? 1 : -1));
    }

    public static int sp2px(Context ctx, float spValue) {
        final float scaledDensity = ctx.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scaledDensity + 0.5f * (spValue >= 0 ? 1 : -1));
    }

    /**
     * 获得屏幕宽度 px
     *
     * @return ;
     */
    public static int getWidth(Context context) {
        WindowManager wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static boolean isLowDpi() {
        return SPTools.getBoolean("low_dpi", false);
    }

    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static int getDefaultActionBarSize(Context mContext) {
        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
        int[] attribute = new int[]{android.R.attr.actionBarSize};
        TypedArray array = mContext.obtainStyledAttributes(typedValue.resourceId, attribute);
        int titleHeight = array.getDimensionPixelSize(0 /* index */, -1 /* default size */);
        array.recycle();
        return titleHeight;
    }

    /**
     * 代码动态控制布局中控件的宽高
     */
    public static void setViewWidth$Height(View view, int width, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (null != params) {
            params.height = height;
            params.width = width;
            view.setLayoutParams(params);
        } else {
            FrameLayout.LayoutParams framelayoutParams = new FrameLayout.LayoutParams(width, height);
            view.setLayoutParams(framelayoutParams);
        }
    }


    /**
     * 获得屏幕高度 px
     *
     * @return
     */
    public static int getHeight(Context context) {
        WindowManager wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 判断当前是否有网络
     */
    public static boolean hasNetwork(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    /**
     * 跳转到应用详情设置页
     */
    public static void go2AppDetail(Context context) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        context.startActivity(intent);
    }

    /**
     * 屏幕添加灰色蒙版
     */
    public static void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0 --》不透明-全透明
        context.getWindow().setAttributes(lp);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /**
     * 判断当前应用程序是否后台运行
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();

        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcess.processName.equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前应用程序是否后台运行
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (tasks.size() > 0) {
            if (context.getPackageName().equals(tasks.get(0).topActivity.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    /*通过三原色调配*/
    public static int getColor(@ColorInt int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * @param alpher      透明度,0.0d~1.0d
     * @param colorString 6位颜色值 ffaa25
     */
    private static String get16Alpher(double alpher, String colorString) {
        return new BigInteger(
                String.valueOf(
                        Math.round(alpher * 255)), 10)
                .toString(16) + colorString;
    }

    //获取真实的手机屏幕尺寸
    public static int[] getRealScreenSize(Activity activity) {
        int[] size = new int[2];
        int screenWidth = 0, screenHeight = 0;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                screenWidth = (Integer) Display.class.getMethod("getRawWidth")
                        .invoke(d);
                screenHeight = (Integer) Display.class
                        .getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d,
                        realSize);
                screenWidth = realSize.x;
                screenHeight = realSize.y;
            } catch (Exception ignored) {
            }
        size[0] = screenWidth;
        size[1] = screenHeight;
        return size;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return bp
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        if (bmp == null) {
            return null;
        }
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, bmp.getWidth(), bmp.getHeight() - statusBarHeight);
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);

        return bp;
    }

    /**
     * 获取DisplayMetrics，包括屏幕高宽，密度等
     *
     * @param activity
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        if (activity != null) {
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        }
        return dm;
    }

    public static String getIMSI(Context context) {
        try {
            if (context == null) {
                return "";
            }
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getSubscriberId();
        } catch (Exception exception1) {
        }
        return "";
    }

    public static String getIMEI(Context context) {
        try {
            if (context == null) {
                return "";
            }
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            if (imei != null && !imei.equals("")) {
                return imei;
            }
        } catch (Exception exception1) {
        }

        return "";
    }

    public static String getUdid() {
        String udid = SPTools.getString("udid", "");
        if (udid.length() == 0) {
            udid = String.format("%s", UUID.randomUUID());
            SPTools.saveString("udid", udid);
        }
        return udid;
    }

    //相机是否可用
    public static final boolean hasCamera(Context context) {
        if (_hasCamera == null) {
            PackageManager pckMgr = context.getPackageManager();
            boolean flag = pckMgr
                    .hasSystemFeature("android.hardware.camera.front");
            boolean flag1 = pckMgr.hasSystemFeature("android.hardware.camera");
            boolean flag2;
            if (flag || flag1)
                flag2 = true;
            else
                flag2 = false;
            _hasCamera = Boolean.valueOf(flag2);
        }
        return _hasCamera.booleanValue();
    }

    //是否存在实体物理菜单按键
    public static boolean hasHardwareMenuKey(Context context) {
        boolean flag = false;
        if (PRE_HC)
            flag = true;
        else if (GTE_ICS) {
            flag = ViewConfiguration.get(context).hasPermanentMenuKey();
        } else
            flag = false;
        return flag;
    }

    public static boolean isPackageExist(Context context, String pckName) {
        try {
            PackageInfo pckInfo = context.getPackageManager()
                    .getPackageInfo(pckName, 0);
            if (pckInfo != null)
                return true;
        } catch (NameNotFoundException e) {
        }
        return false;
    }

    public static void hideAnimatedView(View view) {
        if (PRE_HC && view != null)
            view.setPadding(view.getWidth(), 0, 0, 0);
    }

    /**
     * 是横屏？
     */
    public static boolean isLandscape(Context context) {
        boolean flag;
        if (context.getResources().getConfiguration().orientation == 2)
            flag = true;
        else
            flag = false;
        return flag;
    }

    /**
     * 是竖屏？
     */
    public static boolean isPortrait(Context context) {
        boolean flag = true;
        if (context.getResources().getConfiguration().orientation != 1)
            flag = false;
        return flag;
    }

    public static void showAnimatedView(View view) {
        if (PRE_HC && view != null)
            view.setPadding(0, 0, 0, 0);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideKeyBoard(Activity context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) return;
        boolean active = inputMethodManager.isActive();
        if (active) {
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyboard(View view) {
        if (view == null)
            return;
        View focusView = null;
        if (view instanceof EditText)
            focusView = view;

        if (focusView != null) {
                /*
                if (focusView.isFocusable()) {
                    focusView.setFocusable(false);
                    focusView.setFocusable(true);
                }
                */
            if (focusView.isFocused()) {
                focusView.clearFocus();
            }
            InputMethodManager manager = (InputMethodManager) focusView.getContext().getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            manager.hideSoftInputFromInputMethod(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘(更改弹起方式)
     */
    public static void hideSoftChangeMethod(View view) {
        Activity mActivity = null;
        if (view == null)
            return;
        View focusView = null;
        if (view instanceof EditText)
            focusView = view;
        Context context = view.getContext();
        if (context != null && context instanceof Activity) {
            mActivity = ((Activity) context);
            focusView = mActivity.getCurrentFocus();
        }

        if (focusView != null) {
            if (focusView.isFocused()) {
                focusView.clearFocus();
            }
            InputMethodManager manager = (InputMethodManager) focusView.getContext().getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            manager.hideSoftInputFromInputMethod(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            if (null != mActivity) {
                mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            }
        }
    }

    /**
     * 显示软键盘--弹窗
     */
    public static void showSoftKeyboard(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    /**
     * 显示软键盘--VIEW
     */
    public static void showSoftKeyboard(View view) {
        if (view == null)
            return;
        /*
        ((InputMethodManager) MyApp.getWebInfo().getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(view,
                InputMethodManager.SHOW_FORCED);
        */
        if (!view.isFocusable())
            view.setFocusable(true);
        if (!view.isFocusableInTouchMode())
            view.setFocusableInTouchMode(true);
        if (!view.isFocused()) {
            view.requestFocus();
        }
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
        inputMethodManager.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    /**
     * 显示软键盘--VIEW
     */
    public static void showSoftChangeMethod(View view) {
        Activity mActivity = null;
        if (view == null)
            return;
        if (!view.isFocusable())
            view.setFocusable(true);
        if (!view.isFocusableInTouchMode())
            view.setFocusableInTouchMode(true);
        if (!view.isFocused()) {
            view.requestFocus();
        }
        Context context = view.getContext();
        if (context != null && context instanceof Activity) {
            mActivity = ((Activity) context);
        }
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
        inputMethodManager.showSoftInputFromInputMethod(view.getWindowToken(), 0);
        if (null != mActivity) {
            Window window = mActivity.getWindow();
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    /**
     * 开关软键盘
     */
    public static void toogleSoftKeyboard(View view) {
        ((InputMethodManager) view.getContext().getSystemService(
                INPUT_METHOD_SERVICE)).toggleSoftInput(0,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * SD卡是否可用
     */
    public static boolean isSdcardReady() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 获取当前的语言环境
     */
    public static String getCurCountryLan(Context context) {
        return context.getResources().getConfiguration().locale
                .getLanguage()
                + "-"
                + context.getResources().getConfiguration().locale
                .getCountry();
    }

    public static boolean isZhCN(Context context) {
        String lang = context.getResources()
                .getConfiguration().locale.getCountry();
        if (lang.equalsIgnoreCase("CN")) {
            return true;
        }
        return false;
    }

    public static String percent(double p1, double p2) {
        String str;
        double p3 = p1 / p2;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        str = nf.format(p3);
        return str;
    }

    public static String percent2(double p1, double p2) {
        String str;
        double p3 = p1 / p2;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(0);
        str = nf.format(p3);
        return str;
    }

    //去应用市场评价，下载
    public static void gotoMarket(Context context, String pck) {
        if (!isHaveMarket(context)) {
            UIHelper.toastInCenter(context, "你手机中没有安装应用市场！");
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + pck));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static boolean isHaveMarket(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        return infos.size() > 0;
    }

    public static void openAppInMarket(Context context) {
        if (context != null) {
            String pckName = context.getPackageName();
            try {
                gotoMarket(context, pckName);
            } catch (Exception ex) {
                try {
                    String otherMarketUri = "http://market.android.com/details?id="
                            + pckName;
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(otherMarketUri));
                    context.startActivity(intent);
                } catch (Exception e) {

                }
            }
        }
    }

    /**
     * 设置全屏
     */
    public static void setFullScreen(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow()
                .getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(params);
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 取消全屏
     */
    public static void cancelFullScreen(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow()
                .getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setAttributes(params);
        activity.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 获取当前应用程序的包名
     *
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

    /**
     * 获取程序 图标
     *
     * @param context  ;
     * @param packname 应用包名
     * @return ;
     */
    public Drawable getAppIcon(Context context, String packname) {
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            //获取到应用信息
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取程序的版本号
     *
     * @param context ;
     * @return ;
     */
    public static int getAppVersionCode(Context context) {
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packinfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();

        }
        return 0;
    }

    /**
     * 获取程序的版本号
     *
     * @param context  ;
     * @param packname ;
     * @return ;
     */
    public static int getAppVersionCode(Context context, String packname) {
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            return packinfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();

        }
        return 0;
    }

    /**
     * 获取程序的版本名
     *
     * @param context  ;
     * @param packname ;
     * @return ;
     */
    public static String getAppVersionName(Context context, String packname) {
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            return packinfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();

        }
        return packname;
    }

    /**
     * 获取当前程序的版本名
     *
     * @param context ;
     * @return ;
     */
    public static String getAppVersionName(Context context) {
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packinfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();

        }
        return context.getPackageName();
    }


    /**
     * 获取程序的名字
     *
     * @param context  ;
     * @param packname ;
     * @return ;
     */
    public static String getAppName(Context context, String packname) {
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadLabel(pm).toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return packname;
    }

    /**
     * 获取程序的签名
     *
     * @param context  ;
     * @param packname ;
     * @return ;
     */
    public static String getAppSignature(Context context, String packname) {
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
            //获取当前应用签名
            return packinfo.signatures[0].toCharsString();

        } catch (NameNotFoundException e) {
            e.printStackTrace();

        }
        return packname;
    }

    /**
     * 安装apk
     */
    public static void installAPK(Context context, File apkFile) {
        if (apkFile == null || !apkFile.exists())
            return;
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkFile),
                "application/vnd.android.package-archive");
        context.startActivity(intent);

    }

    /**
     * 安装意图
     */
    public static Intent getInstallApkIntent(File file) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 到拨号盘
     */
    public static void openDial(Context context, String number) {
        if (number.contains(" ")) {
            number = number.replace(" ", "");
        }
        if (number.contains("-")) {
            number = number.replace("-", "");
        }

        Uri uri = Uri.parse("tel:" + number);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(it);
    }

    /**
     * 直接拨打
     */
    public static void openCall(Activity context, String number) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            Uri uri = Uri.parse("tel:" + number);
            Intent it = new Intent(Intent.ACTION_CALL, uri);
            context.startActivity(it);
        }
    }

    /***
     * 发信息
     */
    public static void openSMS(Context context, String smsBody, String tel) {
        Uri uri = Uri.parse("smsto:" + tel);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsBody);
        context.startActivity(it);
    }

   /* public static void openWeChat(Context context) {
        copyTextToBoard(context, "guoqiang1102");
        openApp(context, "com.tencent.mm");
        UIHelper.toastInCenter(context, "管理员微信号已复制到剪切板，请在微信[添加朋友]检索框粘贴");
    }*/

    /**
     * @param context     上下文
     * @param packageName 判断程序的包名
     * @return : boolean
     * @Description : 这个包名的程序是否在运行
     * @Method_Name : isRunningApp
     */
    public static boolean isRunningApp(Context context, String packageName) {
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
                isAppRunning = true;
                // find it, break
                break;
            }
        }
        return isAppRunning;
    }

    /**
     * 获取应用程序所有的非系统应用的包名
     */
    public static Map<String, String> getAppList(Context context) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        Map<String, String> appList = new HashMap<>();
        for (PackageInfo info : packages) {
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {//如果非系统应用，则添加至appList
                appList.put(info.applicationInfo.packageName, info.applicationInfo.loadLabel(context.getPackageManager()).toString());
            }
        }
        return appList;
    }

    /**
     * 打开APP
     * try {
     * Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.gogoal.yuanfang");
     * startActivity(intent);
     * } catch (Exception e) {
     * Toast.makeText(getActivity(), "启动异常", Toast.LENGTH_SHORT).show();
     * }
     */
    public static void openApp(Context context, String packageName) {
        Map<String, String> appList = getAppList(context);
        if (appList.containsKey(packageName)) {
            Intent mainIntent = context.getPackageManager()
                    .getLaunchIntentForPackage(packageName);
            if (mainIntent == null) {
                mainIntent = new Intent(packageName);
            } else {
            }
            context.startActivity(mainIntent);
        } else {
            UIHelper.toastInCenter(context, "应用未安装");
        }
    }

//    private void getName(Context context){
//        PackageManager pm = context.getPackageManager();
//        packageInfo.applicationInfo.loadLabel(pm).toString();
//    }

    /**
     * 启动指定的app中的Activity通知栏消息进入应用
     */
    public static boolean openAppActivity(Context context, String packageName,
                                          String activityName) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName(packageName, activityName);
        intent.setComponent(cn);
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断wifi是否打开
     */
    public static boolean isWifiOpen(Context context) {
        boolean isWifiConnect = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // checkUpdata the networkInfos numbers
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        for (int i = 0; i < networkInfos.length; i++) {
            if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                if (networkInfos[i].getType() == ConnectivityManager.TYPE_MOBILE) {
                    isWifiConnect = false;
                }
                if (networkInfos[i].getType() == ConnectivityManager.TYPE_WIFI) {
                    isWifiConnect = true;
                }
            }
        }
        return isWifiConnect;
    }

    /**
     * 卸载App
     */
    public static void uninstallApk(Context context, String packageName) {
        if (isPackageExist(context, packageName)) {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
                    packageURI);
            context.startActivity(uninstallIntent);
        }
    }

    /**
     * 复制文本
     */
    public static void copyTextToBoard(Context context, String string) {
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip;
        myClip = ClipData.newPlainText("text", string);
        myClipboard.setPrimaryClip(myClip);
    }

    /**
     * 发送邮件
     *
     * @param context
     * @param subject 主题
     * @param content 内容
     * @param emails  邮件地址
     */
    public static void sendEmail(Context context, String subject,
                                 String content, String... emails) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            // 模拟器
            // intent.setType("text/plain");
            intent.setType("message/rfc822"); // 真机
            intent.putExtra(Intent.EXTRA_EMAIL, emails);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, content);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasStatusBar(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 调用系统安装了的应用分享
     *
     * @param context
     * @param title
     * @param url
     */
    public static void showSystemShareOption(Activity context,
                                             final String title, final String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title);
        intent.putExtra(Intent.EXTRA_TEXT, title + " " + url);
        context.startActivity(Intent.createChooser(intent, "选择分享"));
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

    /**
     * 禁止EditText输入空格 回车
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.equals("\n\r") || source.equals("\n") || source.equals("\r")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    public static int getAudioDurition(Context context, String path) {

        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(context, Uri.fromFile(new File(path)));

            mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);

            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }

        int duration = mediaPlayer.getDuration();

        mediaPlayer.release();
        mediaPlayer = null;

        return duration;
    }

    public static void operationRevyvler(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }

    public static void setTabLayoutWidth(TabLayout t, int dpMargin) {
        try {
            Class<?> tablayout = t.getClass();
            Field tabStrip = tablayout.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
            LinearLayout ll_tab = (LinearLayout) tabStrip.get(t);
            for (int i = 0; i < ll_tab.getChildCount(); i++) {
                View child = ll_tab.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);

                params.setMargins(
                        AppDevice.dp2px(MyApp.getAppContext(), dpMargin),
                        0,
                        AppDevice.dp2px(MyApp.getAppContext(), dpMargin),
                        0);

                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * 应用检测更新
     *
     * @param manager     FragmentManager
     * @param checkByUser 是否是手动检测，还是自动检测
     */
    public static void checkUpdata(final FragmentManager manager, final boolean checkByUser) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("versions", AppDevice.getAppVersionName(MyApp.getAppContext()));
        new GGOKHTTP(map, GGOKHTTP.GET_ANDROID_VERSIONS_INFO, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    JSONObject updataObject = JSONObject.parseObject(responseInfo).getJSONObject("data");
                    if (updataObject.getBooleanValue("isUpdate")) {

                        UpdataDialog updataDialog = UpdataDialog.newDialog(
                                updataObject.getString("content"),
                                updataObject.getString("versions"),
                                updataObject.getString("url"));
                        updataDialog.setCancelable(false);
                        updataDialog.show(manager);
                    } else {
                        if (checkByUser)
                            NormalSingleAlertDialog.newInstance("当前为最新版本", "哦，知道了", null).show(manager);
                    }
                } else {
                    //
                }
            }

            @Override
            public void onFailure(String msg) {
//
            }
        }).startGet();
    }
}