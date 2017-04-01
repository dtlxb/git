package cn.gogoal.im.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.activity.LoginActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;

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
        return user.getString("simple_avatar");
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
        String responseInfo = SPTools.getString(getUserAccountId() + "_contact_beans", "");

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

        KLog.e(friendList);
        KLog.e(friendMap.values());

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

    public static boolean checkToken(Context context) {
        if (TextUtils.isEmpty(getToken())) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取用户好友列表
     */
    public static List<ContactBean> getUserContacts() {
        String contactStringRes = SPTools.getString(getUserAccountId() + "_contact_beans", "");
        if (TextUtils.isEmpty(contactStringRes)) {
            return new ArrayList<>();
        }
        String contactArray = JSONObject.parseObject(contactStringRes).getJSONArray("data").toJSONString();

        return JSONObject.parseArray(contactArray, ContactBean.class);
    }

    // TODO: 目前没接口，先从本地查
    public static boolean isMyFriend(int friendId) {
        List<ContactBean> userContacts = getUserContacts();
        for (ContactBean bean : userContacts) {
            if (bean.getUserId() == friendId) {
                return true;
            }
        }
        return false;
    }

    /**
     * 找出当前群中自己的好友
     * <p>
     * 获取当前群的已经加入的好友，当前群可能有的不是好友，要匹配
     */
    public static List<ContactBean> getFriendsInTeam(String conversationId) {

        JSONArray userInTeamArray = SPTools.getJsonArray(UserUtils.getUserAccountId() + conversationId + "_accountList_beans", new JSONArray());

        KLog.e(userInTeamArray);

        List<ContactBean> contacts = getUserContacts();

        if (null == contacts || contacts.isEmpty()) {
            return new ArrayList<>();//用户没有好友,返回空集合，别返回空
        }
        //获取我的conversationId群中的全部用户
        List<ContactBean> list = JSON.parseArray(String.valueOf(userInTeamArray), ContactBean.class);

        list.retainAll(contacts);

        return list;
    }

    /**
     * 传入 好友集合 返回好友的id集合
     */
    public static HashSet<Integer> getUserFriendsIdList(Collection<ContactBean> friends) {
        HashSet<Integer> list = new HashSet<>();
        for (ContactBean contactBean : friends) {
            list.add(contactBean.getFriend_id());
        }
        return list;
    }

    /**
     * 当群信息没有的时候 网上拉取
     */
    public static void getChatGroup(List<String> groupMembers, final String conversationId, final getSquareInfo mGetSquareInfo) {
        Map<String, String> params = new HashMap<>();
        params.put("token", getToken());
        params.put("conv_id", conversationId);
        params.put("id_list", JSONObject.toJSONString(groupMembers));

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    if (null != result.getJSONObject("data")) {
                        if (null != mGetSquareInfo) {
                            mGetSquareInfo.squareGetSuccess(result.getJSONObject("data"));
                        }
                        SPTools.saveJsonArray(UserUtils.getUserAccountId() + conversationId + "_accountList_beans", result.getJSONObject("data").getJSONArray("accountList"));
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != mGetSquareInfo) {
                    mGetSquareInfo.squareGetFail(msg);
                }
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_MEMBER_INFO, ggHttpInterface).startGet();
    }

    /**
     * 返回Conversation管理类
     */
    public interface getSquareInfo {

        void squareGetSuccess(JSONObject object);   ///< 加入房间成功

        void squareGetFail(String error);      ///< 加入房间失败
    }
}
