package cn.gogoal.im.ui.dialog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.gogoal.im.R;
import cn.gogoal.im.base.MyApp;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.ui.dialog.base.BaseCentDailog;

/**
 * author wangjd on 2017/7/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :版本更新弹窗
 */
public class UpdataDialog extends BaseCentDailog {

    // 下载包安装路径
    private String filePath;

    // 文件路径
    private static final String saveFileName = "GoGoalApp.apk";

    private String newAppUrl;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager nManager;
    private int mNotifyId = 10002;
    //下载进度
    private int progress;
    //下载开关
    public static boolean canceled = false;

    public static UpdataDialog newDialog(String message, String versionName, String newAppUrl) {
        UpdataDialog dialog = new UpdataDialog();
        Bundle bundle = new Bundle();
        bundle.putString("updata_message", message);
        bundle.putString("version_name", versionName);
        bundle.putString("new_app_url", newAppUrl);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public int getWidth() {
        return 4 * AppDevice.getWidth(getContext()) / 5;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_updata;
    }

    @Override
    public void bindView(View v) {

        File appFile = v.getContext().getExternalFilesDir("apk");

        if (!appFile.exists()){
            appFile.mkdirs();
        }

        filePath = appFile.getAbsolutePath();

        KLog.e(filePath);

        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }

        TextView tvTitle = (TextView) v.findViewById(R.id.tv_dialog_title);
        TextView tvMessage = (TextView) v.findViewById(R.id.tv_dialog_msg);

        String versionName = bundle.getString("version_name");
        String versionMsg = bundle.getString("updata_message");
        newAppUrl = bundle.getString("new_app_url");
        newAppUrl = "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk";

        tvTitle.setText("发现新版本(" + versionName + ")");
        tvMessage.setText(StringUtils.getNotNullString(versionMsg));

        tvMessage.setMovementMethod(ScrollingMovementMethod.getInstance());

        v.findViewById(R.id.iv_dialog_dismis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdataDialog.this.dismiss();
            }
        });

        v.findViewById(R.id.btn_do_updata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下载 newAppUrl
                showDownLoadNotify();
                new Thread(downApkRunnable).start();

                UpdataDialog.this.dismiss();
            }
        });

    }

    private void initNotify() {
        nManager = (NotificationManager) MyApp.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(MyApp.getAppContext());
        PendingIntent pendingIntent = PendingIntent.getActivity(MyApp.getAppContext(), 1, new Intent(), 0);
        mBuilder.setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setSmallIcon(R.mipmap.logo);
    }

    private void showDownLoadNotify() {
        initNotify();
        mBuilder.setContentTitle("Go-Goal股票")
                .setContentText("Go-Goal股票更新下载")
                .setTicker("ticker");
        mBuilder.setProgress(100, progress, false);
        nManager.notify(mNotifyId, mBuilder.build());
    }

    private void showInstallAppNotify() {
        mBuilder.setContentTitle("Go-Goal股票")
                .setContentText("下载完成，点击安装")
                .setTicker("ticker")
                .setProgress(100, 100, false)
                .setAutoCancel(true);

        //下载完成自动运行安装
        installAPK(MyApp.getAppContext(), new File(filePath + saveFileName));

        Intent apkIntent = new Intent();
        apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        apkIntent.setAction(android.content.Intent.ACTION_VIEW);
        String apk_path = filePath + saveFileName;
        Uri uri = Uri.fromFile(new File(apk_path));
        apkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        PendingIntent contextIntent = PendingIntent.getActivity(MyApp.getAppContext(), 0, apkIntent, 0);
        mBuilder.setContentIntent(contextIntent);
        nManager.notify(mNotifyId, mBuilder.build());
    }

    private Runnable downApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(newAppUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(15000); //连接超时时间
                conn.setReadTimeout(15000); //读取超时时间
                conn.setRequestMethod("GET");
                conn.setDoInput(true); //打开输入流
                conn.connect();

                int count = conn.getContentLength(); //文件总大小 字节
                InputStream is = conn.getInputStream();
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }

                File ApkFile = new File(filePath + saveFileName);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int proLength = 0;
                int mp = 5;
                byte[] buf = new byte[1024];
                do {
                    int readCount = is.read(buf);
                    proLength += readCount;
                    progress = (int) (((float) proLength / count) * 100);
                    fos.write(buf, 0, readCount);

                    if (progress == 100) {
                        // 下载完成通知安装
                        canceled = true;
                        showInstallAppNotify();
                        break;
                    } else {
                        if (progress >= mp) {
                            mp = progress + 5;
                            mBuilder.setProgress(100, progress, false);
                            mBuilder.setContentText("已下载完成" + progress + "%");
                            nManager.notify(mNotifyId, mBuilder.build());
                        }
                    }
                } while (!canceled);
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
                mBuilder.setContentText("下载失败");
                mBuilder.setColor(Color.RED);
                nManager.notify(mNotifyId, mBuilder.build());
            }
        }
    };

    /**
     * 安装apk
     */
    public static void installAPK(Context context, File apkFile) {
        if (apkFile == null || !apkFile.exists())
            return;
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
