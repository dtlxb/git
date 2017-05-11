package cn.gogoal.im.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import cn.gogoal.im.activity.TypeLoginActivity;
import cn.gogoal.im.base.MyApp;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageUtils;

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

    public static String getUserId() {
        return "";
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    public static String getMyAccountId() {
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
        return getGoGoalId();
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public static String getGoGoalId() {
        JSONObject user = getUserInfo();
        if (user == null) return null;
        return user.getString("account_name");
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public static String getNickname() {
        JSONObject user = getUserInfo();
        if (user == null) return null;
        return user.getString("nickname");
    }

    /**
     * 获取手机号
     *
     * @return
     */
    public static String getPhoneNumber() {
        JSONObject user = getUserInfo();
        if (user == null) return null;
        return TextUtils.isEmpty(user.getString("mobile")) ? "未设置" : user.getString("mobile");
    }

    /**
     * 获取工作地点
     *
     * @return
     */
    public static String getOrganizationAddress() {
        JSONObject user = getUserInfo();
        if (user == null) return null;
        return user.getString("city");
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

    public static void cacheUserAvatar() {
//        //头像缓存
        ImageUtils.getUrlBitmap(MyApp.getAppContext(), UserUtils.getUserAvatar(), new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ImageUtils.saveImageToSD(MyApp.getAppContext(),getUserAvatarCacheAbsolutePath(),
                        resource, 100);
            }
        });
    }

    /**
     * 获取用户本地缓存头像
     */
    public static File getUserCacheAvatarFile() {
        File file = new File(getUserAvatarCacheAbsolutePath());
        if (file.exists()) {
            return file;
        }
        return null;
    }

    /**
     * 缓存头像的绝对路径
     */
    private static String getUserAvatarCacheAbsolutePath() {
        return MyApp.getAppContext().getExternalFilesDir("avatar") +
                File.separator + "avatar_" + MD5Utils.getMD5EncryptyString16(UserUtils.getUserAvatar()) +
                ImageUtils.getImageSuffix(UserUtils.getUserAvatar());
    }

    /**
     * 更新用户信息
     */
    public static void updataLocalUserInfo(JSONObject userInfo) {
        if (null != userInfo) {
            SPTools.clearItem("userInfo");
            SPTools.saveString("userInfo", userInfo.toJSONString());
        }
    }

    /**
     * 更新用户指定字段信息
     */
    public static void updataLocalUserInfo(String key, String newValue) {
        KLog.e(key, "value=" + newValue);

        JSONObject user = getUserInfo();
        if (user == null) {
            return;
        }
        if (user.containsKey(key)) {
            user.remove(key);
        }
        user.put(key, newValue);
        SPTools.saveJsonObject("userInfo", user);

        KLog.json(SPTools.getString("userInfo", ""));
    }

    /**
     * 发请求更新用户信息
     */
    public static void updataNetUserInfo(final Map<String, String> map, final UpdataListener updataListener) {
//        Map<String, String> map = new HashMap<>();
//        map.put("avatar", "7");
//        map.put("name", "7");
//        map.put("company", "7");
//        map.put("duty", "7");
//        map.put("province", "7");
//        map.put("city", "7");
        map.put("token", UserUtils.getToken());

        KLog.e(StringUtils.map2ggParameter(map));

        new GGOKHTTP(map, GGOKHTTP.UPDATE_ACCOUNT_INFO, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    JSONObject result = JSONObject.parseObject(responseInfo).getJSONObject("data");
                    boolean success = result.getBoolean("success");
                    if (success) {
                        if (null != updataListener)
                            updataListener.success(responseInfo);
                    } else {
                        if (updataListener != null) {
                            if (map.containsKey("name")) {
                                switch (result.getIntValue("code")) {
                                    case 115://昵称已存在
                                        updataListener.failed("昵称已存在！");
                                        break;
                                    default:
                                        updataListener.failed(result.getString("msg"));
                                        break;
                                }
                            } else {
                                updataListener.failed(result.getString("msg"));
                            }
                        }
                    }
                } else {
                    updataListener.failed(JSONObject.parseObject(responseInfo).getString("message"));
                }
            }

            @Override
            public void onFailure(String msg) {
                if (updataListener != null) {
                    updataListener.failed(msg);
                }
            }
        }).startGet();
    }

    /* 判断用户是否第一次登录
    *
    * @return
    */
    public static Boolean isFirstLogin(int accountId) {
        int savedAccountId = SPTools.getInt(accountId + "_saved_account", -1);
        return accountId == savedAccountId;
    }

    /**
     * 获取职务
     *
     * @return
     */
    public static String getDuty() {
        JSONObject user = getUserInfo();
        if (user == null) return null;
        return user.getString("duty");
    }

    /**
     * 获取组织信息
     *
     * @return
     */
    public static String getorgName() {
        JSONObject user = getUserInfo();
        if (user == null) return null;
        return user.getString("organization_name");
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
//        mContext.startActivity(new Intent(mContext, TypeLoginActivity.class));
        // TODO: 2017/2/8 0008
        mContext.finish();
        UIHelper.toast(mContext, "退出登录成功!");
    }

    @SuppressLint("UseSparseArrays")
    public static String updataFriendList(String newFriendJson) {
        String responseInfo = SPTools.getString(getMyAccountId() + "_contact_beans", "");

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
            context.startActivity(new Intent(context, TypeLoginActivity.class));
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取用户好友列表
     */
    public static List<ContactBean> getUserContacts() {
        String contactStringRes = SPTools.getString(getMyAccountId() + "_contact_beans", "");
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

    public static ContactBean findAnyoneByFriendId(int friendId) {
        List<ContactBean> userContacts = getUserContacts();
        for (ContactBean bean : userContacts) {
            if (bean.getUserId() == friendId) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 找出当前群中自己的好友
     * <p>
     * 获取当前群的已经加入的好友，当前群可能有的不是好友，要匹配
     */
    public static List<ContactBean> getFriendsInTeam(String conversationId) {

        JSONArray userInTeamArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + conversationId + "_accountList_beans", new JSONArray());

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
     * 找出当前群中自己的好友
     */
    public static List<ContactBean> getOthersInTeam(String conversationId, int fromWhere) {
        JSONArray userInTeamArray = null;
        if (fromWhere == AppConst.SQUARE_ROOM_DELETE_ANYONE) {
            userInTeamArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + conversationId + "_accountList_beans", new JSONArray());
        } else if (fromWhere == AppConst.LIVE_CONTACT_SOMEBODY) {
            userInTeamArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + conversationId + "_live_group_beans", new JSONArray());
        } else {

        }

        KLog.e(userInTeamArray);
        //获取我的conversationId群中的全部用户
        List<ContactBean> list = JSON.parseArray(String.valueOf(userInTeamArray), ContactBean.class);

        ContactBean mySelfBean = new ContactBean();

        mySelfBean.setFriend_id(Integer.parseInt(getMyAccountId()));
        mySelfBean.setNickname(getUserName());
        mySelfBean.setAvatar(getUserAvatar());

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getFriend_id() == mySelfBean.getFriend_id()) {
                list.remove(i);
            }
        }
        KLog.e(list);
        return list;
    }

    /**
     * 传入 好友集合 返回好友的id集合
     */
    public static TreeSet<Integer> getUserFriendsIdList(Collection<ContactBean> friends) {
        TreeSet<Integer> list = new TreeSet<>();
        for (ContactBean contactBean : friends) {
            list.add(contactBean.getFriend_id());
        }
        return list;
    }

    /**
     * 好友通讯录信息更新
     */
    public static void upDataContactInfo(int friendId, String avatar, String nickname, String conv_id) {
        String string = SPTools.getString(UserUtils.getMyAccountId() + "_contact_beans", null);
        KLog.e(string);
        if (!TextUtils.isEmpty(string)) {
            JSONObject jsonObject = JSON.parseObject(string);
            KLog.e(jsonObject.toString());
            if (jsonObject.get("data") != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                //有这个人修改
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject oldObject = (JSONObject) jsonArray.get(i);
                    if (oldObject.getInteger("friend_id") == friendId) {
                        ((JSONObject) jsonArray.get(i)).put("avatar", avatar);
                        ((JSONObject) jsonArray.get(i)).put("nickname", nickname);
                        ((JSONObject) jsonArray.get(i)).put("friend_id", friendId);
                        ((JSONObject) jsonArray.get(i)).put("conv_id", conv_id);
                    }
                }

                KLog.e(jsonObject);
                SPTools.saveString(UserUtils.getMyAccountId() + "_contact_beans", JSON.toJSONString(jsonObject));
                KLog.e(SPTools.getString(UserUtils.getMyAccountId() + "_contact_beans", ""));
            }
        }
    }

    /**
     * 好友通讯录删除操作(byId)
     */
    public static void deleteContactsSomeone(int friendId) {
        String string = SPTools.getString(UserUtils.getMyAccountId() + "_contact_beans", null);
        KLog.e(string);
        //清除缓存中的这个人
        if (!TextUtils.isEmpty(string)) {
            JSONObject jsonObject = JSON.parseObject(string);
            KLog.e(jsonObject.toString());
            if (jsonObject.get("data") != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                KLog.e(jsonArray.toString());
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject thisObject = (JSONObject) jsonArray.get(i);
                    if (thisObject.getInteger("friend_id") == friendId) {
                        jsonArray.remove(jsonArray.get(i));
                    }
                }
                jsonObject.put("data", jsonArray);
                KLog.e(jsonObject);
                SPTools.saveString(UserUtils.getMyAccountId() + "_contact_beans", JSON.toJSONString(jsonObject));
                KLog.e(SPTools.getString(UserUtils.getMyAccountId() + "_contact_beans", ""));
            }
        }
    }

    /**
     * 当群信息没有的时候 网上拉取
     */
    public static void getChatGroup(final int type, List<String> groupMembers, final String conversationId, final getSquareInfo mGetSquareInfo) {
        Map<String, String> params = new HashMap<>();
        params.put("token", getToken());
        params.put("conv_id", conversationId);
        params.put("id_list", JSONObject.toJSONString(groupMembers));
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(responseInfo);
                if ((int) result.get("code") == 0) {
                    if (null != result.getJSONObject("data")) {
                        JSONObject jsonObject = result.getJSONObject("data");
                        if (null != mGetSquareInfo) {
                            mGetSquareInfo.squareGetSuccess(jsonObject);
                        }
                        if (null != jsonObject.get("accountList")) {
                            if (type == AppConst.CHAT_GROUP_CONTACT_BEANS) {
                                SPTools.saveJsonArray(UserUtils.getMyAccountId() + conversationId + "_accountList_beans",
                                        result.getJSONObject("data").getJSONArray("accountList"));
                            } else if (type == AppConst.LIVE_GROUP_CONTACT_BEANS) {
                                //缓存群通讯录
                                SPTools.saveJsonArray(UserUtils.getMyAccountId() + conversationId + "_live_group_beans", result.getJSONObject("data").getJSONArray("accountList"));
                            }
                        }
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

    /**
     * 发请求更新用户信息回调
     */
    public interface UpdataListener {
        void success(String responseInfo);

        void failed(String errorMsg);
    }
}
