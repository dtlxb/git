package cn.gogoal.im.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.IMMessageBean;

/**
 * author wangjd on 2017/2/8 0008.
 * Staff_id 1375
 * phone 18930640263
 */
public class UserUtils {
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
    public static String getToken() {
        return AppConst.LEAN_CLOUD_TOKEN;
    }

    /**
     * 获取用户好友列表
     */
    public static List<ContactBean> getUserContacts() {
        String contactStringRes = SPTools.getString(getToken() + "_contact_beans", "");
        if (TextUtils.isEmpty(contactStringRes)) {
            return new ArrayList<>();
        }
        String contactArray = JSONObject.parseObject(contactStringRes).getJSONArray("data").toJSONString();

        return JSONObject.parseArray(contactArray, ContactBean.class);
    }

    /**
     * 找出当前群中自己的好友
     * <p>
     * 获取当前群的已经加入的还友，当前群可能有的不是好友，要匹配
     */
    public static List<ContactBean> getFriendsInTeam(String conversationId) {

        JSONArray userInTeamArray = SPTools.getJsonArray(UserUtils.getToken() + conversationId + "_accountList_beans", new JSONArray());

        List<ContactBean> contacts = getUserContacts();

        if (null == contacts || contacts.isEmpty()) {
            return new ArrayList<>();//用户没有好友,返回空集合，别返回空
        }
        //获取我的conversationId群中的全部用户
        List<ContactBean> list = JSON.parseArray(String.valueOf(userInTeamArray), ContactBean.class);

        List<ContactBean> result = new ArrayList<>();

        for (ContactBean bean : list) {
            if (contacts.contains(bean)) {
                result.add(bean);
            }
        }
        return result;
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

}
