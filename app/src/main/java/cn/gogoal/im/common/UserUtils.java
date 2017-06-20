package cn.gogoal.im.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

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

import cn.gogoal.im.R;
import cn.gogoal.im.activity.CreateLiveActivity;
import cn.gogoal.im.activity.LiveActivity;
import cn.gogoal.im.activity.TypeLoginActivity;
import cn.gogoal.im.base.MyApp;
import cn.gogoal.im.bean.Advisers;
import cn.gogoal.im.bean.AdvisersBean;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.UserBean;
import cn.gogoal.im.bean.group.GroupData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVIMClientManager;
import cn.gogoal.im.common.IMHelpers.UserInfoUtils;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.permission.CheckLivePermissionListener;
import cn.gogoal.im.servise.MessageSaveService;
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
        return getMyAccountId();
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

    /**
     * 有缓存拿缓存头像，没有获取缓存，并保存
     */
    public static void getUserAvatar(final Impl<Bitmap> listener) {
        if (StringUtils.isActuallyEmpty(getBitmapFilePaht())) {
            ImageUtils.getUrlBitmap(MyApp.getAppContext(), UserUtils.getUserAvatar(), new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    listener.response(Impl.RESPON_DATA_SUCCESS, resource);
                    ImageUtils.saveImageToSD(MyApp.getAppContext(), MyApp.getAppContext().getExternalFilesDir("avatar") +
                                    File.separator + "avatar_" + MD5Utils.getMD5EncryptyString16(UserUtils.getUserAvatar()) +
                                    ImageUtils.getImageSuffix(UserUtils.getUserAvatar()),
                            resource, 100);
                }
            });
        } else {
            listener.response(Impl.RESPON_DATA_SUCCESS, BitmapFactory.decodeFile(getBitmapFilePaht()));
        }
    }

    public static Bitmap getUserAvatarBitmap() {
        final Bitmap[] bitmap = new Bitmap[1];
        getUserAvatar(new Impl<Bitmap>() {
            @Override
            public void response(int code, Bitmap data) {
                if (code == Impl.RESPON_DATA_SUCCESS) {
                    bitmap[0] = data;
                } else {
                    bitmap[0] = BitmapFactory.decodeResource(MyApp.getAppContext().getResources(), R.mipmap.logo);
                }
            }
        });
        return bitmap[0];
    }

    /**
     * 通过用户account_id直接获取用户信息
     */
    public static void getUserInfo(String accountId, final Impl<String> listener) {
        HashMap<String, String> params = getTokenParams();
        params.put("account_id", accountId);
        new GGOKHTTP(params, GGOKHTTP.GET_ACCOUNT_DETAIL, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (listener != null) {
                    if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                        listener.response(Impl.RESPON_DATA_SUCCESS, JSONObject.parseObject(responseInfo).getString("data"));
                    } else {
                        listener.response(Impl.RESPON_DATA_EMPTY, "没有查询到相关信息");
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (listener != null) listener.response(Impl.RESPON_DATA_ERROR, "请求出错");
            }
        }).startGet();
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
    public static void updateLocalUserInfo(JSONObject userInfo) {
        if (null != userInfo) {
            SPTools.clearItem("userInfo");
            SPTools.saveString("userInfo", userInfo.toJSONString());
        }
    }

    /**
     * 更新用户指定字段信息
     */
    public static void updateLocalUserInfo(String key, String newValue) {
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

        AVIMClientManager.getInstance().close(UserUtils.getMyAccountId(), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
            }
        });
        SPTools.clearItem("userInfo");
        mContext.stopService(new Intent(mContext, MessageSaveService.class));
        mContext.startActivity(new Intent(mContext, TypeLoginActivity.class));
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
        List<UserBean> userBeanList = new ArrayList<>();
        userBeanList.addAll(UserInfoUtils.getAllUserInfo());
        return copyClass(userBeanList);
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

        List<ContactBean> contacts = getUserContacts();

        if (null == contacts || contacts.isEmpty()) {
            return new ArrayList<>();//用户没有好友,返回空集合，别返回空
        }

        //获取我的conversationId群中的全部用户
        List<ContactBean> list = getAllFriendsInTeam(conversationId);

        list.retainAll(contacts);

        return list;
    }

    /**
     * 获取群好友
     */
    public static List<ContactBean> getAllFriendsInTeam(String conversationId) {
        List<UserBean> userBeanList = new ArrayList<>();
        userBeanList.addAll(UserInfoUtils.getAllGroupUserInfo(conversationId));
        return copyClass(userBeanList);
    }

    private static List<ContactBean> copyClass(List<UserBean> userBeanList) {
        List<ContactBean> contactBeanList = new ArrayList<>();
        for (int i = 0; i < userBeanList.size(); i++) {
            ContactBean contactBean = new ContactBean();
            contactBean.setConv_id(userBeanList.get(i).getConv_id());
            contactBean.setNickname(userBeanList.get(i).getNickname());
            contactBean.setFriend_id(userBeanList.get(i).getFriend_id());
            contactBean.setAvatar(userBeanList.get(i).getAvatar());
            contactBeanList.add(contactBean);
        }
        return contactBeanList;
    }

    /**
     * 找出当前群中自己的好友
     */
    public static List<ContactBean> getOthersInTeam(String conversationId, int fromWhere) {
        List<ContactBean> list = getAllFriendsInTeam(conversationId);
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
        return SPTools.getJsonArray(UserUtils.getMyAccountId() + "_my_group_list", null);
    }

    /**
     * 判读我的群是否已收藏
     */
    public static boolean getMyGooupIsCollected(String convId) {
        String string = SPTools.getString(UserUtils.getMyAccountId() + "_my_group_list", null);
        if (TextUtils.isEmpty(string)){
            return false;
        }
        List<GroupData> groupDatas = JSONObject.parseArray(string, GroupData.class);
        for (GroupData data:groupDatas){
            if (data.getConv_id().equalsIgnoreCase(convId)){
                return true;
            }
        }
        return false;
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
    public static void getChatGroup(List<String> groupMembers, final String conversationId, final SquareInfoCallback mSquareInfoCallback) {
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
                        if (null != mSquareInfoCallback) {
                            mSquareInfoCallback.squareGetSuccess(jsonObject);
                        }
                        if (null != jsonObject.get("accountList")) {
                            UserInfoUtils.saveGroupUserInfo(conversationId, result.getJSONObject("data").getJSONArray("accountList"));
                        }
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != mSquareInfoCallback) {
                    mSquareInfoCallback.squareGetFail(msg);
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
    public interface SquareInfoCallback {

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
