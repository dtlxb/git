package cn.gogoal.im.base;

import android.Manifest;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alivc.player.AccessKey;
import com.alivc.player.AccessKeyCallback;
import com.alivc.player.AliVcMediaPlayer;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.IMHelpers.MyConversationHandler;
import cn.gogoal.im.common.IMHelpers.MyMessageHandler;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.database.LitePalApplication;
import cn.gogoal.im.common.permission.IPermissionListner;

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

        SPTools.initSharedPreferences(this);

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

        //连接服务器
            /*AVImClientManager.getInstance().open(UserUtils.getUserAccountId(), new AVIMClientCallback() {
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
}
