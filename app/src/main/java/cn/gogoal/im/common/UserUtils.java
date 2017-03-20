package cn.gogoal.im.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * author wangjd on 2017/2/8 0008.
 * Staff_id 1375
 * phone 18930640263
 */
public class UserUtils {

    /**
     * 获取用户令牌 token
     */
    public static String getToken() {
        JSONObject user = getUserInfo();
        if (user == null) return null;
        return user.getString("token");
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    public static String getUserAccountId() {
        JSONObject user = getUserInfo();
        if (user == null) return null;
        return user.getString("account_id");
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public static String getUserName() {
        JSONObject user = getUserInfo();
        if (user == null) return null;
        return user.getString("nickname");
    }

    /**
     * 获取用户头像地址
     *
     * @return
     */
    public static String getUserAvatar() {
        JSONObject user = getUserInfo();
        if (user == null) return null;
        return user.getString("avatar");
    }

    /**
     * 是否登陆状态
     */
    public static boolean isLogin() {
        return !TextUtils.isEmpty(getToken());
    }

    /**
     * 获取用户本地信息
     */
    public static JSONObject getUserInfo() {

        JSONObject userMessage = SPTools.getJsonObject("userInfo", null);
        return userMessage == null ? null : userMessage;
    }

    /**
     * 删除用户本地信息
     */
    public static void removeUserMessage() {
        SPTools.clearItem("userInfo");
    }

    /*注销*/
    public static void logout(Activity mContext) {
        SPTools.clear();
        SPTools.saveBoolean("notFristTime", true);
//        mContext.startActivity(new Intent(mContext, LoginActivity.class));
        // TODO: 2017/2/8 0008
        mContext.finish();
        UIHelper.toast(mContext, "退出登录成功!");
    }

    @SuppressLint("UseSparseArrays")
    public static String updataFriendList(String newFriendJson) {
        String responseInfo = SPTools.getString(getToken() + "_contact_beans", "");

        if (TextUtils.isEmpty(newFriendJson)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(responseInfo);
        JSONArray friendList = (JSONArray) jsonObject.remove("data");

        JSONObject newObject = JSONObject.parseObject(newFriendJson);

        HashMap<Integer, JSONObject> friendMap = new HashMap<>();

        for (int i = 0; i < friendList.size(); i++) {
            JSONObject object = (JSONObject) friendList.get(i);
            friendMap.put(object.getInteger("friend_id"), object);
        }

        friendMap.put(newObject.getInteger("friend_id"), newObject);

        jsonObject.put("data", friendMap.values());

        return jsonObject.toString();
    }

    @SuppressLint("UseSparseArrays")
    public static String updataFriendList(String responseInfo, String newFriendJson) {
        if (TextUtils.isEmpty(newFriendJson)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(responseInfo);
        JSONArray friendList = (JSONArray) jsonObject.remove("data");

        JSONObject newObject = JSONObject.parseObject(newFriendJson);

        HashMap<Integer, JSONObject> friendMap = new HashMap<>();

        for (int i = 0; i < friendList.size(); i++) {
            JSONObject object = (JSONObject) friendList.get(i);
            friendMap.put(object.getInteger("friend_id"), object);
        }

        friendMap.put(newObject.getInteger("friend_id"), newObject);

        jsonObject.put("data", friendMap.values());

        return jsonObject.toString();
    }

    // TODO: 临时token
    public static String getTemporaryToken() {
        return AppConst.LEAN_CLOUD_TOKEN;
    }
}
