package cn.gogoal.im.common.copy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.gogoal.im.common.SPTools;

/**
 * 新闻阅读相关工具类
 * Created by lixinsheng on 15/11/23.
 */
public class NewsUtils {

    /**
     * 获取读过新闻的id
     */
    public static JSONArray getIsReadNewsId() {
        return SPTools.getJsonArray("ggIsReadNewsId", new JSONArray());
    }


    //判断新闻是否已读过
    public static boolean isAlreadyRead(String id) {
        com.alibaba.fastjson.JSONArray array = NewsUtils.getIsReadNewsId();
        boolean flag = false;
        for (int i = 0; i < array.size(); i++) {
            JSONObject list = array.getJSONObject(i);
            String new_id = String.valueOf(list.get("id"));
            if (id.equals(new_id)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}

