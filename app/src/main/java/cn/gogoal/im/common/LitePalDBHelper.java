package cn.gogoal.im.common;

import com.socks.library.KLog;

import org.litepal.LitePal;
import org.litepal.LitePalDB;

import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.bean.SquareUserBean;
import cn.gogoal.im.bean.UserBean;
import cn.gogoal.im.common.IMHelpers.AVIMClientManager;

/**
 * Created by huangxx on 2017/6/9.
 */

public class LitePalDBHelper {

    private static LitePalDBHelper litePalDBHelper;

    private LitePalDB litePalDB;

    public static LitePalDBHelper getInstance() {
        if (null == litePalDBHelper) {
            synchronized (AVIMClientManager.class) {
                if (null == litePalDBHelper) {
                    litePalDBHelper = new LitePalDBHelper();
                }
            }
        }
        return litePalDBHelper;
    }

    public void createSQLite(String userId) {

        litePalDB = new LitePalDB(AppConst.LEANCLOUD_APP_ID + userId, 1);

        litePalDB.addClassName(UserBean.class.getName());
        litePalDB.addClassName(SquareUserBean.class.getName());
        litePalDB.addClassName(IMMessageBean.class.getName());
        LitePal.use(litePalDB); // 建库
    }

}
