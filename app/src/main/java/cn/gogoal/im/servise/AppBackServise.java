package cn.gogoal.im.servise;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**创建后台服务
 *
 * 下载文件
 * 更新app
 * 刷新用户token
 *
 * */
public class AppBackServise extends Service {

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}