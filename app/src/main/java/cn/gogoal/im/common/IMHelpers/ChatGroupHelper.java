package cn.gogoal.im.common.IMHelpers;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UserUtils;

/**
 * Created by huangxx on 2017/4/14.
 */

public class ChatGroupHelper {

    //添加群成员
    public static void addAnyone(List<Integer> idList, String conversationId, final chatGroupManager groupManager) {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("id_list", JSONObject.toJSONString(idList));
        params.put("conv_id", conversationId);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    if (null != groupManager) {
                        groupManager.groupActionSuccess(result);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != groupManager) {
                    groupManager.groupActionFail(msg);
                }
            }
        };
        new GGOKHTTP(params, GGOKHTTP.ADD_MEMBER, ggHttpInterface).startGet();
    }

    //删除群成员
    public static void deleteAnyone(final List<Integer> idSet, String conversationId, final chatGroupManager groupManager) {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("id_list", JSONObject.toJSONString(idSet));
        params.put("conv_id", conversationId);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    if (null != groupManager) {
                        groupManager.groupActionSuccess(result);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != groupManager) {
                    groupManager.groupActionFail(msg);
                }
            }
        };
        new GGOKHTTP(params, GGOKHTTP.DELETE_MEMBER, ggHttpInterface).startGet();
    }


    //收藏群
    public static void collcetGroup(String conversationId, final chatGroupManager groupManager) {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", conversationId);
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    if (null != groupManager) {
                        groupManager.groupActionSuccess(result);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != groupManager) {
                    groupManager.groupActionFail(msg);
                }
            }
        };
        new GGOKHTTP(params, GGOKHTTP.COLLECT_GROUP, ggHttpInterface).startGet();
    }

    //取消群收藏
    public static void deleteGroup(String conversationId, final chatGroupManager groupManager) {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", conversationId);
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    if (null != groupManager) {
                        groupManager.groupActionSuccess(result);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != groupManager) {
                    groupManager.groupActionFail(msg);
                }
            }
        };
        new GGOKHTTP(params, GGOKHTTP.CANCEL_COLLECT_GROUP, ggHttpInterface).startGet();
    }

    /**
     * 群管理类
     */
    public interface chatGroupManager {

        void groupActionSuccess(JSONObject object);   ///< 加入房间成功

        void groupActionFail(String error);      ///< 加入房间失败
    }

}
