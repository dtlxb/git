package cn.gogoal.im.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.alivc.player.AccessKey;
import com.alivc.player.AccessKeyCallback;
import com.alivc.player.AliVcMediaPlayer;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.MainActivity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.IMHelpers.MyConversationHandler;
import cn.gogoal.im.common.IMHelpers.MyMessageHandler;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.database.LitePalApplication;
import cn.gogoal.im.common.permission.IPermissionListner;
import cn.gogoal.im.ui.view.XLayout;

/**
 * author wangjd on 2017/2/8 0008.
 * Staff_id 1375
 * phone 18930640263
 */
public class MyApp extends LitePalApplication {

    private static MyApp app;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        registerActivityLifecycleCallbacks(new LifeCircle());

        if (!SPTools.isSpInited()) {
            SPTools.initSharedPreferences(this);
        }


        XLayout.getConfig()
                .setErrorText("出错啦~请稍后重试！")
                .setEmptyText("抱歉，暂无数据")
                .setNoNetworkText("无网络连接，请检查您的网络···")
                .setErrorImage(R.mipmap.error)
                .setEmptyImage(R.mipmap.img_no_data)
                .setNoNetworkImage(R.mipmap.no_network)
                .setAllTipTextColor(R.color.textColor_999999)
                .setAllTipTextSize(14)
                .setReloadButtonText("点我重试哦")
                .setReloadButtonTextSize(14)
                .setReloadButtonTextColor(R.color.textColor_999999)
                .setReloadButtonWidthAndHeight(150, 40)
                .setAllPageBackgroundColor(android.R.color.white);


//        //只有主进程运行的时候才需要初始化
//        if (getApplicationInfo().packageName.equals(getMyProcessName())) {
        //TODO im初始化
        //初始化参数依次this，AppId,AppKey
        AVOSCloud.initialize(this, AppConst.LEANCLOUD_APP_ID, AppConst.LEANCLOUD_APP_KEY);
        //AVOSCloud.initialize(this, "hi22KV7K693uIQLX5X4ROSbs-gzGzoHsz", "qTkdjmpyuVdJAearcTthBw5N");
        //启用北美节点
        //AVOSCloud.useAVCloudUS();
        //必须在启动的时候注册 MessageHandler
        //注册默认的消息处理逻辑
        AVIMMessageManager.registerMessageHandler(AVIMMessage.class, new MyMessageHandler());
        AVIMMessageManager.setConversationEventHandler(new MyConversationHandler());
        //相互踢监听
        AVImClientManager.setEventHandler();
        //连接服务器
            /*AVImClientManager.getInstance().open(UserUtils.getMyAccountId(), new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    //KLog.e(e.getAppCode()+";"+e.getMessage());
                }
            });*/

        //TODO 注册消息接收器

        BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.READ_PHONE_STATE}, new IPermissionListner() {
            @Override
            public void onUserAuthorize() {
                //阿里云播放器初始化
                AliVcMediaPlayer.init(getApplicationContext(), AppConst.businessId, new AccessKeyCallback() {
                    @Override
                    public AccessKey getAccessToken() {
                        return new AccessKey(AppConst.accessKeyId, AppConst.accessKeySecret);
                    }
                });
            }

            @Override
            public void onRefusedAuthorize(List<String> deniedPermissions) {

            }
        });
//        }

        // leancloud推送注册
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    // 关联  installationId 到用户表等操作……
                } else {
                    // 保存失败，输出错误信息
                }
            }
        });

        PushService.setDefaultPushCallback(this, MainActivity.class);
    }

    /**
     * @return 获取当前运行的进程名
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getAppContext() {
        return app.getApplicationContext();
    }

    private class LifeCircle implements ActivityLifecycleCallbacks {

//        private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(final Context context, Intent intent) {
//                if (NetworkAvailable()) {
//                    AVImClientManager.getInstance().open(UserUtils.getMyAccountId(), new AVIMClientCallback() {
//                        @Override
//                        public void done(AVIMClient avimClient, AVIMException e) {
//                            if (e == null) {
//                                KLog.e("IM登录成功");
//                            } else {
//                                UIHelper.toast(context, "即时通讯登录失败");
//                            }
//                        }
//                    });
//
//                } else {
//                    Toast.makeText(context, "请检查网络环境", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
//
//        /**
//         * 检测网络是否连接
//         */
//        private boolean NetworkAvailable() {
//            ConnectivityManager manager;
//            try {
//                Thread.sleep(600);
//                //得到网络连接信息
//                manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                if (manager != null) {
//                    // 获取NetworkInfo对象
//                    NetworkInfo networkInfo = manager.getActiveNetworkInfo();
//                    //去进行判断网络是否连接
//                    if (networkInfo!=null && networkInfo.isAvailable()) {
//                        return true;
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return false;
//        }

        @Override
        public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
            AppManager.getInstance().addActivity(activity);

//            IntentFilter intentFilter = new IntentFilter();
//            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//            registerReceiver(networkReceiver, intentFilter);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            AppManager.getInstance().finishActivity(activity);
//            try {
//                unregisterReceiver(networkReceiver);
//            }catch (Exception e){
//                e.getMessage();
//            }
        }
    }
}
