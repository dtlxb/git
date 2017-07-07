package cn.gogoal.im.servise;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVIMClientManager;
import cn.gogoal.im.common.LaunchRequest;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;

public class AppBackServise extends Service {

    public class AppBackBinder extends Binder {

        public void ggAutoLogin(final Activity activity) {

            if (!UserUtils.isLogin()) return;
            Map<String, String> map = new HashMap<>();
            map.put("token", UserUtils.getToken());
            GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
                @Override
                public void onSuccess(String responseInfo) {
                    KLog.e(responseInfo);
                    JSONObject result = JSONObject.parseObject(responseInfo);
                    int code = result.getIntValue("code");
                    if (code == 0) {
                        KLog.e("刷新Token成功");
                        final JSONObject data = result.getJSONObject("data");
                        UserUtils.saveUserInfo(data);

                        try {
                            AVIMClientManager.getInstance().open(data.getString("account_id"), new AVIMClientCallback() {
                                @Override
                                public void done(AVIMClient avimClient, AVIMException e) {
                                    if (e == null) {
                                        KLog.e("IM登录成功");
                                    } else {
                                        UIHelper.toast(activity, "即时通讯登录失败");
                                    }
                                }
                            });


                        } catch (Exception ignored) {
                            UIHelper.toast(activity, "即时通讯登录失败");
                        }

                    } else {
                        UserUtils.logout(activity);
                        UIHelper.toast(activity, "即时通讯登录失败");
                    }
                }

                @Override
                public void onFailure(String msg) {

                }
            };
            new GGOKHTTP(map, GGOKHTTP.USER_AUTO_LOGIN, ggHttpInterface).startGet();
        }

        public void initData(){
            try {
                LaunchRequest.init();//初始化缓存数据
            }catch (Exception e){
                KLog.e(e.getMessage());
            }
        }
    }


    public AppBackBinder sBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        sBinder = new AppBackBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sBinder;
    }

}