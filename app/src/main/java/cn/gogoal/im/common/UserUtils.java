package cn.gogoal.im.common;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;

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

    public static String updataFriendList(String newFriendJson) {
        String responseInfo = SPTools.getString(getToken() + "_contact_beans", "");
        if (TextUtils.isEmpty(responseInfo)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(responseInfo);
        JSONArray friendList = (JSONArray) jsonObject.remove("data");

        JSONObject newObject = JSONObject.parseObject(newFriendJson);

        if (!jsonArrauContain(friendList,newObject,"friend_id")){
            friendList.add(newObject);
        }
        jsonObject.put("data", friendList);
        return jsonObject.toString();
    }

    private static boolean jsonArrauContain(JSONArray array, JSONObject ele, String flag) {
        Iterator<Object> iterator = array.iterator();

        while (iterator.hasNext()) {
            JSONObject object = (JSONObject) iterator.next();
            if (object.getString(flag).equals(ele.getString(flag))) {
                return true;
            }
        }
        return false;
    }

    // TODO: 临时token
    public static String getToken() {
        return AppConst.LEAN_CLOUD_TOKEN;
    }
}
