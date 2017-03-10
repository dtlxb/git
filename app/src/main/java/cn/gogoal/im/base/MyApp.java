package cn.gogoal.im.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.duanqu.qupai.jni.ApplicationGlue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.IMHelpers.MyConversationHandler;
import cn.gogoal.im.common.IMHelpers.MyMessageHandler;
import cn.gogoal.im.common.SPTools;

/**
 * author wangjd on 2017/2/8 0008.
 * Staff_id 1375
 * phone 18930640263
 */
public class MyApp extends Application {

    private static MyApp app;

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);

        app = this;

        SPTools.initSharedPreferences(this);

        //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())) {
            //TODO im初始化
            //初始化参数依次this，AppId,AppKey
            AVOSCloud.initialize(this, AppConst.LEANCLOUD_APP_ID,AppConst.LEANCLOUD_APP_KEY);
            //AVOSCloud.initialize(this,"hi22KV7K693uIQLX5X4ROSbs-gzGzoHsz","qTkdjmpyuVdJAearcTthBw5N");
            //启用北美节点
//          AVOSCloud.useAVCloudUS();
            //必须在启动的时候注册 MessageHandler
            //注册默认的消息处理逻辑
            AVIMMessageManager.registerMessageHandler(AVIMMessage.class, new MyMessageHandler());
            AVIMMessageManager.setConversationEventHandler(new MyConversationHandler());

            //连接服务器
            AVImClientManager.getInstance().open(AppConst.LEAN_CLOUD_TOKEN, new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                }
            });
            //TODO 注册消息接收器

            //阿里云推流
            System.loadLibrary("gnustl_shared");
//        System.loadLibrary("ijkffmpeg");//目前使用微博的ijkffmpeg会出现1K再换wifi不重连的情况
            System.loadLibrary("qupai-media-thirdparty");
//        System.loadLibrary("alivc-media-jni");
            System.loadLibrary("qupai-media-jni");
            ApplicationGlue.initialize(this);
        }

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
    }

    public static Context getContext() {
        return app.getApplicationContext();
    }
}
