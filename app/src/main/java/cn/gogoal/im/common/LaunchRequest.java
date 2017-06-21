package cn.gogoal.im.common;

import com.socks.library.KLog;

/**
 * author wangjd on 2017/6/21 0021.
 * Staff_id 1375
 * phone 18930640263
 * description :APP启动需要获取保存的请求
 */
public class LaunchRequest {

    //只需调用init方法即可;
    public static void init() {

        //1.请求专属顾问
        UserUtils.getAdvisers(null);

        //2.拉取我的群组
        UserUtils.getMyGroupList(null);

        //3.获取我的诊断工具
        UserUtils.getAllMyTools(null);

        KLog.e("初始化拉取数据成功~~~~~~~~~~");
    }


}
