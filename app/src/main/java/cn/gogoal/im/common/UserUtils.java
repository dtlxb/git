package cn.gogoal.im.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cn.gogoal.im.activity.CreateLiveActivity;
import cn.gogoal.im.activity.LiveActivity;
import cn.gogoal.im.activity.TypeLoginActivity;
import cn.gogoal.im.base.MyApp;
import cn.gogoal.im.bean.Advisers;
import cn.gogoal.im.bean.AdvisersBean;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVIMClientManager;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.permission.CheckLivePermissionListener;
import cn.gogoal.im.ui.dialog.NormalAlertDialog;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * author wangjd on 2017/2/8 0008.
 * Staff_id 1375
 * phone 18930640263
 */
public class UserUtils {

    public static void saveUserInfo(JSONObject data) {
        SPTools.saveJsonObject("userInfo", data);
    }

    /**
     * 获取用户令牌 token
     */
    public static String getToken() {
        JSONObject user = getUserInfo();
        if (user == null) return null;
        return user.getString("token");
    }

    /**
     * 获取用户令牌 token请求参数
     */
    public static HashMap<String, String> getTokenParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("token", getToken());
        return map;
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

    public static void getUserAvatar(final AvatarTakeListener listener) {
        if (StringUtils.isActuallyEmpty(getBitmapFilePaht())) {
            ImageUtils.getUrlBitmap(MyApp.getAppContext(), UserUtils.getUserAvatar(), new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    listener.success(resource);
                    ImageUtils.saveImageToSD(MyApp.getAppContext(), MyApp.getAppContext().getExternalFilesDir("avatar") +
                                    File.separator + "avatar_" + MD5Utils.getMD5EncryptyString16(UserUtils.getUserAvatar()) +
                                    ImageUtils.getImageSuffix(UserUtils.getUserAvatar()),
                            resource, 100);
                }
            });
        } else {
            listener.success(BitmapFactory.decodeFile(getBitmapFilePaht()));
        }
    }

    /**
     * 拿取本地缓存的(自己)头像绝对路径
     */
    private static String getBitmapFilePaht() {
        File filesDir = MyApp.getAppContext().getExternalFilesDir("avatar");
        String bitmapPath = "";
        if (filesDir == null || !filesDir.exists()) {
            return null;
        }
        String[] fileList = filesDir.list();
        for (String path : fileList) {
            if (path.contains("avatar_" + MD5Utils.getMD5EncryptyString16(UserUtils.getUserAvatar()))) {
                bitmapPath = filesDir.getAbsolutePath() + File.separator + path;
                break;
            }
        }
        return bitmapPath;
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

        new GGOKHTTP(map, GGOKHTTP.UPDATE_ACCOUNT_INFO, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                if (parseObject(responseInfo).getIntValue("code") == 0) {
                    JSONObject result = parseObject(responseInfo).getJSONObject("data");
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
                    updataListener.failed(parseObject(responseInfo).getString("message"));
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

    /*
    * 保存用户是登录记录
    * @return
    */
    public static void saveLoginHistory(String accountId) {
        Set<String> loginHistory = SPTools.getSetData("login_history", new HashSet<String>());
        loginHistory.add(accountId);
        SPTools.saveSetData("login_history", loginHistory);
    }

    /**
     * 判断用户是否第一次登录
     */
    public static Boolean hadLogin(String accountId) {
        Set<String> loginHistory = SPTools.getSetData("login_history", new HashSet<String>());

        return !loginHistory.isEmpty() && loginHistory.contains(accountId);
    }

    /**
     * 获取职务
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

        SPTools.clearItem("userInfo");

        mContext.startActivity(new Intent(mContext, TypeLoginActivity.class));
        AVIMClientManager.getInstance().close(UserUtils.getMyAccountId(), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {

            }
        });
    }

    @SuppressLint("UseSparseArrays")
    public static String updataFriendList(String newFriendJson) {
        String responseInfo = SPTools.getString(getMyAccountId() + "_contact_beans", "");

        if (TextUtils.isEmpty(newFriendJson)) {
            return null;
        }
        JSONObject jsonObject = parseObject(responseInfo);
        JSONArray friendList = (JSONArray) jsonObject.remove("data");

        JSONObject newObject = parseObject(newFriendJson);

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
        JSONObject jsonObject = parseObject(responseInfo);
        JSONArray friendList = (JSONArray) jsonObject.remove("data");

        JSONObject newObject = parseObject(newFriendJson);

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
        String contactArray = parseObject(contactStringRes).getJSONArray("data").toJSONString();

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

        JSONArray userInTeamArray = getGroupContactInfo(conversationId);

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
     * 获取群好友
     */
    public static List<ContactBean> getAllFriendsInTeam(String conversationId) {
        JSONArray userInTeamArray = getGroupContactInfo(conversationId);
        KLog.e(userInTeamArray);
        //万一没有群信息
        if (null == userInTeamArray || userInTeamArray.size() == 0) {
            getChatGroup(AppConst.CHAT_GROUP_CONTACT_BEANS, null, conversationId, new getSquareInfo() {
                @Override
                public void squareGetSuccess(JSONObject object) {
                }

                @Override
                public void squareGetFail(String error) {
                }
            });
        }
        //获取我的conversationId群中的全部用户
        return JSON.parseArray(String.valueOf(userInTeamArray), ContactBean.class);
    }

    /**
     * 找出当前群中自己的好友
     */
    public static List<ContactBean> getOthersInTeam(String conversationId, int fromWhere) {
        JSONArray userInTeamArray = null;
        if (fromWhere == AppConst.SQUARE_ROOM_DELETE_ANYONE) {
            userInTeamArray = getGroupContactInfo(conversationId);
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
                break;
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
     * 保存消息列表
     */
    public static void saveMessageListInfo(JSONArray jsonArray) {
        SPTools.saveJsonArray(UserUtils.getMyAccountId() + "_conversation_beans", jsonArray);
    }

    /**
     * 获取消息列表
     */
    public static JSONArray getMessageListInfo() {
        return SPTools.getJsonArray(UserUtils.getMyAccountId() + "_conversation_beans", new JSONArray());
    }

    /**
     * 群通讯录信息保存
     */
    public static void saveGroupContactInfo(String conversationID, JSONArray jsonArray) {
        //先整体缓存
        SPTools.saveJsonArray(UserUtils.getMyAccountId() + conversationID + "_accountList_beans", jsonArray);
        //拆解缓存
        if (!TextUtils.isEmpty(conversationID) && jsonArray != null && jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject oneObject = (JSONObject) jsonArray.get(i);
                SPTools.saveJsonObject(UserUtils.getMyAccountId() + conversationID + oneObject.getInteger("friend_id"), oneObject);
            }
        }
    }

    /**
     * 群通讯录信息获取
     */
    public static JSONArray getGroupContactInfo(String conversationID) {
        return SPTools.getJsonArray(UserUtils.getMyAccountId() + conversationID + "_accountList_beans", new JSONArray());
    }

    /**
     * 群通讯录信息删除
     */
    public static void deleteGroupContactInfo(String conversationID) {
        //先整体缓存清除
        SPTools.clearItem(UserUtils.getMyAccountId() + conversationID + "_accountList_beans");
        JSONArray jsonArray = getGroupContactInfo(conversationID);
        //拆解缓存清除
        if (!TextUtils.isEmpty(conversationID) && jsonArray != null && jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject oneObject = (JSONObject) jsonArray.get(i);
                SPTools.clearItem(UserUtils.getMyAccountId() + conversationID + oneObject.getInteger("friend_id"));
            }
        }
    }

    /**
     * 好友通讯录信息保存
     */
    public static void saveContactInfo(String responseInfo) {
        //先整体缓存
        SPTools.saveString(UserUtils.getMyAccountId() + "_contact_beans", responseInfo);
        //拆解缓存
        if (!TextUtils.isEmpty(responseInfo)) {
            JSONObject jsonObject = parseObject(responseInfo);
            if (jsonObject.get("data") != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject oneObject = (JSONObject) jsonArray.get(i);
                    SPTools.saveJsonObject(UserUtils.getMyAccountId() + oneObject.getInteger("friend_id") + "", oneObject);
                }
            }
        }
    }


    /**
     * 好友通讯录信息更新
     */
    public static void upDataContactInfo(int friendId, String avatar, String nickname, String conv_id) {
        String string = SPTools.getString(UserUtils.getMyAccountId() + "_contact_beans", null);
        KLog.e(string);
        if (!TextUtils.isEmpty(string)) {
            JSONObject jsonObject = parseObject(string);
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
                        SPTools.saveJsonObject(friendId + "", ((JSONObject) jsonArray.get(i)));
                        break;
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
            JSONObject jsonObject = parseObject(string);
            KLog.e(jsonObject.toString());
            if (jsonObject.get("data") != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                KLog.e(jsonArray.toString());
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject thisObject = (JSONObject) jsonArray.get(i);
                    if (thisObject.getInteger("friend_id") == friendId) {
                        jsonArray.remove(jsonArray.get(i));
                        SPTools.clearItem(thisObject.getInteger("friend_id") + "");
                        break;
                    }
                }
                jsonObject.put("data", jsonArray);
                SPTools.saveString(UserUtils.getMyAccountId() + "_contact_beans", JSON.toJSONString(jsonObject));
            }
        }
    }

    /**
     * 登录时需要拉取
     * 我收藏的群列表
     */
    public static void getMyGroupList(final ResponCallback callback) {
        new GGOKHTTP(UserUtils.getTokenParams(), GGOKHTTP.GET_GROUP_LIST, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject mainObject = JSONObject.parseObject(responseInfo);

                //返回数据是否为空
                if (StringUtils.isActuallyEmpty(responseInfo)) {
                    if (callback != null) callback.onError("responseInfo null");
                }
                //返回的json是否包含code字段
                else if (!mainObject.containsKey("code")) {
                    if (callback != null) callback.onError("code null");
                } else {
                    //code ==0 正常
                    if (mainObject.getIntValue("code") == 0) {
                        String resp = mainObject.containsKey("data") ? mainObject.getString("data") : null;
                        if (callback != null) {
                            if (resp == null) {
                                callback.onError("data null");
                            } else {
                                callback.onSuccess(resp);
                                SPTools.saveString(UserUtils.getMyAccountId() + "_my_group_list", resp);
                            }
                        }
                    }

                    //code 1001 没有获取到数据
                    else if (mainObject.getIntValue("code") == 1001) {
                        if (callback != null) callback.onEmpty();
                    }

                    //其他code 出错
                    else {
                        if (callback != null)
                            callback.onError(mainObject.getString("message"));
                    }
                }
            }

            public void onFailure(String msg) {
                if (callback != null) callback.onError(msg);
            }
        }).startGet();
    }

    /**
     * 获取本地缓存的【我的群组】集
     */
    public static JSONArray getLocalMyGooupList() {
        return SPTools.getJsonArray(UserUtils.getMyAccountId() + "_my_group_list", new JSONArray());
    }

    /**
     * 添加群组到本地缓存的【我的群组】集
     */
    public static boolean addGroup2LocalMyGooupList(JSONObject groupObject) {
        JSONArray jsonArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + "_my_group_list", new JSONArray());
        boolean add = jsonArray.add(groupObject);
        SPTools.saveJsonArray(UserUtils.getMyAccountId() + "_my_group_list", jsonArray);
        return add;
    }

    /**
     * 移除群组到本地缓存的【我的群组】集
     */
    public static boolean removeGroup2LocalMyGooupList(JSONObject groupObject) {
        JSONArray array = SPTools.getJsonArray(UserUtils.getMyAccountId() + "_my_group_list", new JSONArray());
        boolean remove = array.remove(groupObject);
        SPTools.saveJsonArray(UserUtils.getMyAccountId() + "_my_group_list", array);
        return remove;
    }


    /**
     * 获取投资顾问
     */
    public static void getAdvisers(final GetAdvisersCallback callback) {

        String advisersList = SPTools.getString(UserUtils.getMyAccountId() + "_ADVISERS_LIST", "");

        if (!StringUtils.isActuallyEmpty(advisersList)) {
            List<Advisers> list = JSONObject.parseArray(advisersList, Advisers.class);
            if (callback != null) {
                callback.onSuccess(list);
                SPTools.clearItem(UserUtils.getMyAccountId() + "_ADVISERS_LIST");
            }

        } else {
            new GGOKHTTP(UserUtils.getTokenParams(), GGOKHTTP.GET_MY_ADVISERS, new GGOKHTTP.GGHttpInterface() {
                @Override
                public void onSuccess(String responseInfo) {
                    int code = parseObject(responseInfo).getIntValue("code");
                    if (code == 0) {
                        List<Advisers> data = parseObject(responseInfo, AdvisersBean.class).getData();
                        SPTools.saveString(UserUtils.getMyAccountId() + "_ADVISERS_LIST", JSONObject.toJSONString(data));
                        if (callback != null) {
                            callback.onSuccess(data);
                        }
                    } else if (code == 1001) {
                        if (callback != null) {
                            callback.onFailed("数据为空");
                        }
                    } else {
                        if (callback != null) {
                            callback.onFailed(parseObject(responseInfo).getString("message"));
                        }
                    }
                }

                @Override
                public void onFailure(String msg) {
                    if (callback != null) {
                        callback.onFailed(msg);
                    }
                }
            }).startGet();
        }
    }

    /**
     * 当群信息没有的时候 网上拉取
     */
    public static void getChatGroup(final int type, List<String> groupMembers, final String conversationId, final getSquareInfo mGetSquareInfo) {
        Map<String, String> params = new HashMap<>();
        params.put("token", getToken());
        params.put("conv_id", conversationId);
        if (groupMembers != null && groupMembers.size() > 0) {
            params.put("id_list", JSONObject.toJSONString(groupMembers));
        }

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    if (null != result.getJSONObject("data")) {
                        JSONObject jsonObject = result.getJSONObject("data");
                        if (null != mGetSquareInfo) {
                            mGetSquareInfo.squareGetSuccess(jsonObject);
                        }
                        if (null != jsonObject.get("accountList")) {
                            if (type == AppConst.CHAT_GROUP_CONTACT_BEANS) {
                                UserUtils.saveGroupContactInfo(conversationId, result.getJSONObject("data").getJSONArray("accountList"));
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

    //检查是否有权限发起直播
    private static void checkLivePermission(@NonNull final CheckLivePermissionListener listener) {
        new GGOKHTTP(UserUtils.getTokenParams(), GGOKHTTP.VIDEO_MOBILE, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject jsonObject = JSONObject.parseObject(responseInfo);

                if (jsonObject.getIntValue("code") == 0) {
                    if (jsonObject.getJSONObject("data").containsKey("live_id") && jsonObject.getJSONObject("data").getIntValue("code") == 1) {
                        listener.hasPermission(jsonObject.getJSONObject("data").getString("live_id"), true);
                    } else {
                        listener.hasPermission(null, false);
                    }
                } else {
                    listener.hasPermission(null, false);
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.hasPermission(null, false);
            }
        }).startGet();
    }

    //检查当前用户是否有权限发起直播
    public static void checkLivePermission(final FragmentActivity context) {
        checkLivePermission(new CheckLivePermissionListener() {
            @Override
            public void hasPermission(String liveId, boolean hasPermission) {
                if (hasPermission) {
                    if (StringUtils.isActuallyEmpty(liveId)) {
                        context.startActivity(new Intent(context, CreateLiveActivity.class));
                    } else {
                        Intent intent = new Intent(context, LiveActivity.class);
                        intent.putExtra("live_id", liveId);
                        context.startActivity(intent);
                    }
                } else {
                    NormalAlertDialog.newInstance("该直播目前仅限由朝阳永续定向邀约发起，如果您有直播合作意向请联系曹小姐：021-68889706-8127",
                            "免费通话", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AppDevice.openDial(v.getContext(), "021688897068127");
                                }
                            }).show(context.getSupportFragmentManager());
                }
            }
        });
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

    public interface GetAdvisersCallback {
        void onSuccess(List<Advisers> advisersList);

        void onFailed(String errorMsg);
    }
}
