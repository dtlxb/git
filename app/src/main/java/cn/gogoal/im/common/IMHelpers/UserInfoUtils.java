package cn.gogoal.im.common.IMHelpers;

import android.content.ContentValues;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.SquareUserBean;
import cn.gogoal.im.bean.UserBean;

/**
 * Created by huangxx on 2017/6/6.
 */

public class UserInfoUtils {
    /**
     * 联系人添加
     *
     * @param responseInfo
     */
    public static void saveAllUserInfo(String responseInfo) {
        BaseBeanList<UserBean> beanList = JSONObject.parseObject(
                responseInfo,
                new TypeReference<BaseBeanList<UserBean>>() {
                });

        for (int i = 0; i < beanList.getData().size(); i++) {
            if (null == getSomeone(beanList.getData().get(i).getFriend_id())) {
                UserBean userBean = beanList.getData().get(i);
                userBean.setInYourContact(true);
                userBean.save();
            }
        }
    }

    /**
     * 返回所有通讯录联系人
     */
    public static List<UserBean> getAllUserInfo() {
        List<UserBean> AllBean = new ArrayList<>();
        AllBean.addAll(DataSupport.where("inYourContact = ? ", "1").find(UserBean.class));
        return AllBean;
    }

    /**
     * 删除通讯录里某人
     */
    public static boolean deleteSomeone(int friendId) {
        List<UserBean> listBean = DataSupport.where("friend_id = ? ", friendId + "").find(UserBean.class);
        if (listBean != null && listBean.size() > 0) {
            DataSupport.deleteAll(UserBean.class, "friend_id = ? ", friendId + "");
            return true;
        }
        return false;
    }

    /**
     * 返回通讯录里某人
     */
    public static UserBean getSomeone(int friendId) {
        List<UserBean> listBean = DataSupport.where("friend_id = ? ", friendId + "").find(UserBean.class);
        if (listBean != null && listBean.size() > 0) {
            return listBean.get(0);
        }
        return null;
    }

    /**
     * 群联系人添加
     *
     * @param conversationId,jsonArray
     */
    public static void saveGroupUserInfo(String conversationId, JSONArray jsonArray) {
        if (!TextUtils.isEmpty(conversationId) && jsonArray != null && jsonArray.size() > 0) {
            List<UserBean> userBeanList = JSON.parseArray(jsonArray.toJSONString(), UserBean.class);
            //联系人缓存
            for (int i = 0; i < userBeanList.size(); i++) {
                //先查询有没，然后存储
                List<UserBean> beanList = DataSupport.where("friend_id = ? ",
                        userBeanList.get(i).getFriend_id() + "").find(UserBean.class);
                if (null == beanList || beanList.size() == 0) {
                    userBeanList.get(i).save();
                }

                //先查询有没，然后存储
                List<SquareUserBean> SquareUserBeanList = DataSupport.where("conversationId = ? and friend_id = ?",
                        conversationId, userBeanList.get(i).getFriend_id() + "").find(SquareUserBean.class);

                if (SquareUserBeanList == null || SquareUserBeanList.size() == 0) {
                    SquareUserBean squareUserBean = new SquareUserBean(conversationId, userBeanList.get(i).getFriend_id());
                    squareUserBean.save();
                }

            }
        }
    }

    /**
     * 返回群里所有联系人
     *
     * @param conversationId
     */
    public static List<UserBean> getAllGroupUserInfo(String conversationId) {
        List<SquareUserBean> beanList = DataSupport.where("conversationId = ?", conversationId).find(SquareUserBean.class);
        List<UserBean> userBeanList = new ArrayList<>();
        if (null != beanList && beanList.size() > 0) {
            for (int i = 0; i < beanList.size(); i++) {
                List<UserBean> listBean = DataSupport.where("friend_id = ? ",
                        beanList.get(i).getFriend_id() + "").find(UserBean.class);
                if (null != listBean && listBean.size() > 0) {
                    userBeanList.add(listBean.get(0));
                }
            }
        }
        KLog.e(userBeanList.size());
        return userBeanList;
    }

    /**
     * 删除所有群联系人
     *
     * @param conversationId
     */
    public static void deleteTheGroup(String conversationId) {
        List<SquareUserBean> beanList = DataSupport.where("conversationId = ?", conversationId).find(SquareUserBean.class);
        //删除群联系人
        if (null != beanList && beanList.size() > 0) {
            for (int i = 0; i < beanList.size(); i++) {

                List<UserBean> listBean = DataSupport.where("friend_id = ? and inYourContact = ?",
                        beanList.get(i).getFriend_id() + "", "0").find(UserBean.class);

                if (null != listBean && listBean.size() > 0) {
                    DataSupport.deleteAll(UserBean.class, "friend_id = ? and inYourContact = ?",
                            beanList.get(i).getFriend_id() + "", "0");
                }
            }
        }
        //删除群列表
        ContentValues values = new ContentValues();
        values.put("conversationId", conversationId);
        DataSupport.deleteAll(SquareUserBean.class, "conversationId = ?", conversationId);
    }

    /**
     * 群联系人删除
     *
     * @param conversationId,jsonArray
     */

    public static void deleteGroupUserInfo(String conversationId, JSONArray jsonArray) {
        if (!TextUtils.isEmpty(conversationId) && jsonArray != null && jsonArray.size() > 0) {
            List<UserBean> userBeanList = JSON.parseArray(jsonArray.toJSONString(), UserBean.class);

            for (int i = 0; i < userBeanList.size(); i++) {
                //删除群列表联系人
                DataSupport.deleteAll(SquareUserBean.class, "conversationId = ? and friend_id = ?",
                        conversationId, userBeanList.get(i).getFriend_id() + "");
                //删除联系人
                DataSupport.deleteAll(UserBean.class, "friend_id = ? and inYourContact = ?",
                        userBeanList.get(i).getFriend_id() + "", "0");
            }
        }
    }

    /**
     * 通讯录、群联系人更新
     *
     * @param friendId
     */
    public static void upDateUserInfo(int friendId, String avatar, String nickname) {
        ContentValues values = new ContentValues();
        values.put("avatar", avatar);
        values.put("nickname", nickname);
        DataSupport.update(UserBean.class, values, friendId);
    }

}
