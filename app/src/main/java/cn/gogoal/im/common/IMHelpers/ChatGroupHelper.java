package cn.gogoal.im.common.IMHelpers;

import android.graphics.Bitmap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.MyApp;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.GroupFaceImage;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UserUtils;

import static cn.gogoal.im.common.UserUtils.getToken;

/**
 * Created by huangxx on 2017/4/14.
 */

public class ChatGroupHelper {

    //添加群成员
    public static void addAnyone(List<Integer> idList, final String conversationId, final chatGroupManager groupManager) {
        Map<String, String> params = new HashMap<>();
        params.put("token", getToken());
        params.put("id_list", JSONObject.toJSONString(idList));
        params.put("conv_id", conversationId);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    AVImClientManager.getInstance().refreshConversation(conversationId);
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
    public static void deleteAnyone(final List<Integer> idSet, final String conversationId, final chatGroupManager groupManager) {
        Map<String, String> params = new HashMap<>();
        params.put("token", getToken());
        params.put("id_list", JSONObject.toJSONString(idSet));
        params.put("conv_id", conversationId);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    AVImClientManager.getInstance().refreshConversation(conversationId);
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
        params.put("token", getToken());
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
        params.put("token", getToken());
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

    //群通讯录更新
    public static void upDataGroupContactInfo(String conversationID, int friendId, String avatar, String nickname) {
        JSONArray spAccountArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + conversationID + "_accountList_beans", new JSONArray());
        KLog.e(spAccountArray.toString());
        boolean hasThisGuy = false;
        //有这个人修改
        for (int i = 0; i < spAccountArray.size(); i++) {
            JSONObject oldObject = (JSONObject) spAccountArray.get(i);
            if (oldObject.getInteger("friend_id") == friendId) {
                ((JSONObject) spAccountArray.get(i)).put("avatar", avatar);
                ((JSONObject) spAccountArray.get(i)).put("nickname", nickname);
                ((JSONObject) spAccountArray.get(i)).put("friend_id", friendId);
                hasThisGuy = true;
            }
        }
        if (hasThisGuy) {
            SPTools.saveJsonArray(UserUtils.getMyAccountId() + conversationID + "_accountList_beans", spAccountArray);
        }
    }

    //获取群头像并且缓存SD
    public static void createGroupImage(final String ConversationId, List<String> gruopMemberMap, final String msgTag) {
        //群删除好友(每次删除后重新生成群头像)
        JSONArray accountArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + ConversationId + "_accountList_beans", new JSONArray());

        if (null != accountArray && accountArray.size() > 0) {
            getNinePic(accountArray, ConversationId, msgTag);
        } else {
            //如果不存在则先找这个会话
            if (gruopMemberMap == null || gruopMemberMap.size() == 0) {
                AVImClientManager.getInstance().findConversationById(ConversationId, new AVImClientManager.ChatJoinManager() {
                    @Override
                    public void joinSuccess(AVIMConversation conversation) {
                        UserUtils.getChatGroup(AppConst.CHAT_GROUP_CONTACT_BEANS, conversation.getMembers(), ConversationId, new UserUtils.getSquareInfo() {
                            @Override
                            public void squareGetSuccess(JSONObject object) {
                                JSONArray array = object.getJSONArray("accountList");
                                if (null != array && array.size() > 0)
                                    getNinePic(array, ConversationId, msgTag);
                            }

                            @Override
                            public void squareGetFail(String error) {

                            }
                        });
                    }

                    @Override
                    public void joinFail(String error) {

                    }
                });
            } else {
                UserUtils.getChatGroup(AppConst.CHAT_GROUP_CONTACT_BEANS, gruopMemberMap, ConversationId, new UserUtils.getSquareInfo() {
                    @Override
                    public void squareGetSuccess(JSONObject object) {
                        JSONArray array = object.getJSONArray("accountList");
                        if (null != array && array.size() > 0)
                            getNinePic(array, ConversationId, msgTag);
                    }

                    @Override
                    public void squareGetFail(String error) {

                    }
                });
            }
        }
    }

    public static void getNinePic(JSONArray array, final String ConversationId, final String msgTag) {
        List<String> picUrls = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject personObject = array.getJSONObject(i);
            picUrls.add(personObject.getString("avatar"));
        }
        //九宫图拼接
        GroupFaceImage.getInstance(MyApp.getAppContext(), picUrls).load(new GroupFaceImage.OnMatchingListener() {
            @Override
            public void onSuccess(Bitmap mathingBitmap) {
                String groupFaceImageName = "_" + ConversationId + ".png";
                ImageUtils.cacheBitmapFile(MyApp.getAppContext(), mathingBitmap, "imagecache", groupFaceImageName);
                if (null != mathingBitmap) {
                    AppManager.getInstance().sendMessage(msgTag, 0 + "");
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    /**
     * 现拼头像
     */
    public static void createGroupImage(final String ConversationId, final GroupInfoResponse response) {
        AVImClientManager.getInstance().findConversationById(ConversationId, new AVImClientManager.ChatJoinManager() {
            @Override
            public void joinSuccess(AVIMConversation conversation) {
                if (conversation.getMembers() == null || conversation.getMembers().isEmpty()) {
                    response.getInfoFailed(new Exception("群成员为空"));
                    return;
                }
                Map<String, String> params = new HashMap<>();
                params.put("token", getToken());
                params.put("conv_id", ConversationId);
                params.put("id_list", conversation.getMembers().toString());
                KLog.e(params);

                GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
                    @Override
                    public void onSuccess(String responseInfo) {
                        JSONObject result = JSONObject.parseObject(responseInfo);
                        KLog.e(responseInfo);
                        if ((int) result.get("code") == 0) {
                            if (null != result.getJSONObject("data")) {
                                if (null != response) {
                                    response.getInfoSuccess(result.getJSONObject("data"));
                                }
                            } else {
                                response.getInfoFailed(new Exception("请求出错:" + result.getString("message")));
                            }
                        } else {
                            response.getInfoFailed(new Exception("群信息为空"));
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        if (null != response) {
                            response.getInfoFailed(new Exception(msg));
                        }
                    }
                };
                new GGOKHTTP(params, GGOKHTTP.GET_MEMBER_INFO, ggHttpInterface).startGet();
            }

            @Override
            public void joinFail(String error) {
                KLog.e(error);
            }
        });
    }

    /**
     * 获取群信息
     */
    public interface GroupInfoResponse {
        void getInfoSuccess(JSONObject groupInfo);

        void getInfoFailed(Exception e);
    }


    /**
     * 群管理类
     */
    public interface chatGroupManager {

        void groupActionSuccess(JSONObject object);   ///< 加入房间成功

        void groupActionFail(String error);      ///< 加入房间失败
    }

}
