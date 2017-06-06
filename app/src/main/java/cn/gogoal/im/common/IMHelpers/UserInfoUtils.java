package cn.gogoal.im.common.IMHelpers;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.UserBean;
import cn.gogoal.im.common.database.crud.DataSupport;

/**
 * Created by huangxx on 2017/6/6.
 */

public class UserInfoUtils {

    /**
     * 联系人
     *
     * @param responseInfo
     */
    public static void saveAllUserInfo(String responseInfo) {
        BaseBeanList<UserBean<String>> beanList = JSONObject.parseObject(
                responseInfo,
                new TypeReference<BaseBeanList<UserBean<String>>>() {
                });
        DataSupport.saveAll(beanList.getData());
    }

}
