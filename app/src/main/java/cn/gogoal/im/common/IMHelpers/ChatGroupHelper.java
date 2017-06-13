package cn.gogoal.im.common.IMHelpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.MyApp;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.GGShareEntity;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.bean.ShareItemInfo;
import cn.gogoal.im.common.AsyncTaskUtil;
import cn.gogoal.im.common.AvatarTakeListener;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.GroupFaceImage;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.MyFilter;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UFileUpload;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;

import static cn.gogoal.im.common.UserUtils.getToken;

/**
 * Created by huangxx on 2017/4/14.
 */

public class ChatGroupHelper {

    //添加群成员
    public static void addAnyone(List<Integer> idList, final String conversationId, final ChatGroupManager groupManager) {
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
                    AVIMClientManager.getInstance().refreshConversation(conversationId);
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
    public static void deleteAnyone(final List<Integer> idSet, final String conversationId, final ChatGroupManager groupManager) {
        Map<String, String> params = new HashMap<>();
        params.put("token", getToken());
        params.put("id_list", JSONObject.toJSONString(idSet));
        params.put("conv_id", conversationId);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    AVIMClientManager.getInstance().refreshConversation(conversationId);
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
    public static void collectGroup(String conversationId, final ChatGroupManager groupManager) {
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
    public static void deleteGroup(String conversationId, final ChatGroupManager groupManager) {
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

    //消息免打扰，isSetMute为true设置可打扰
    public static void controlMute(final boolean isSetMute, final String conversationId) {

        SPTools.saveBoolean(UserUtils.getMyAccountId() + conversationId + "noBother", isSetMute);
        AVIMClientManager.getInstance().refreshConversation(conversationId);

        Map<String, String> params = new HashMap<>();
        params.put("token", getToken());
        params.put("conv_id", conversationId);
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                JSONObject object = result.getJSONObject("data");
                boolean success = (boolean) object.get("success");
                if (success) {
                }
            }

            @Override
            public void onFailure(String msg) {
                SPTools.saveBoolean(UserUtils.getMyAccountId() + conversationId + "noBother", !isSetMute);
            }
        };
        if (isSetMute) {
            new GGOKHTTP(params, GGOKHTTP.SET_MUTE, ggHttpInterface).startGet();
        } else {
            new GGOKHTTP(params, GGOKHTTP.CANCEL_MUTE, ggHttpInterface).startGet();
        }
    }

    //获取群头像并且缓存SD
    public static void createGroupImage(AVIMConversation conversation, final String msgTag) {
        //群删除好友(每次删除后重新生成群头像)
        final String ConversationId = conversation.getConversationId();
        List<String> groupMemberMap = conversation.getMembers();
        //如果不存在则先找这个会话
        if (groupMemberMap == null || groupMemberMap.size() == 0) {
            UserUtils.getChatGroup(groupMemberMap, ConversationId, new UserUtils.SquareInfoCallback() {
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
        } else {
            UserUtils.getChatGroup(groupMemberMap, ConversationId, new UserUtils.SquareInfoCallback() {
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

    public static void getNinePic(JSONArray array, final String ConversationId, final String msgTag) {
        List<String> picUrls = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject personObject = array.getJSONObject(i);
            picUrls.add(personObject.getString("avatar"));
        }
        //九宫图拼接
        GroupFaceImage.getInstance(MyApp.getAppContext(), picUrls).load(new GroupFaceImage.OnMatchingListener() {
            @Override
            public void onSuccess(Bitmap matchingBitmap) {
                cacheGroupAvatar(ConversationId, matchingBitmap);
                if (null != matchingBitmap) {
                    AppManager.getInstance().sendMessage(msgTag, 0 + "");
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    /**
     * 缓存群拼接的九宫头像
     */
    public static void cacheGroupAvatar(String conversationId, Bitmap bitmap) {
        ImageUtils.saveImageToSD(MyApp.getAppContext(),
                MyApp.getAppContext().getExternalFilesDir("imagecache")
                        .getAbsolutePath() + File.separator + "_" + conversationId + ".png",
                bitmap, 100);
    }

    public static void setGroupAvatar(String conversationId, AvatarTakeListener listener) {
        setGroupAvatar(conversationId, listener, false);
    }

    public static void setGroupAvatar(final String conversationId,
                                      final AvatarTakeListener listener, boolean needPlaceHolder) {

        final List<String> listAvatar = new ArrayList<>();

        if (StringUtils.isActuallyEmpty(getBitmapFilePath(conversationId))) {
            createGroupImage(conversationId, new GroupInfoResponse() {
                @Override
                public void getInfoSuccess(JSONObject groupInfo) {
                    final JSONArray list = groupInfo.getJSONArray("accountList");
                    for (int i = 0; i < list.size(); i++) {
                        listAvatar.add(list.getJSONObject(i).getString("avatar"));
                    }

                    GroupFaceImage.getInstance(MyApp.getAppContext(), listAvatar).load(new GroupFaceImage.OnMatchingListener() {
                        @Override
                        public void onSuccess(Bitmap matchingBitmap) {
                            if (listener != null) {
                                listener.success(matchingBitmap);
                            }

                            cacheGroupAvatar(conversationId, matchingBitmap);
                        }

                        public void onError(Exception e) {
                            if (listener != null) {
                                listener.failed(e);
                            }
                        }
                    });
                }

                @Override
                public void getInfoFailed(Exception e) {
                }
            });
        } else {
            String imagecache = MyApp.getAppContext().getExternalFilesDir("imagecache").getAbsoluteFile()
                    + File.separator + "_" + conversationId + ".png";

            if (listener != null) {
                listener.success(BitmapFactory.decodeFile(imagecache));
            }
        }
    }

    /**
     * 拿取本地缓存去群头像
     */
    public static String getBitmapFilePath(String conversationID) {
        File filesDir = MyApp.getAppContext().getExternalFilesDir("imagecache");
        String bitmapPath = "";
        if (filesDir == null || !filesDir.exists()) {
            return null;
        }
        String[] fileList = filesDir.list(new MyFilter(".png"));
        for (String path : fileList) {
            if (path.equals("_" + conversationID + ".png")) {
                bitmapPath = filesDir.getPath() + "/" + path;
                break;
            }
        }
        return bitmapPath;
    }

    /**
     * 现拼头像
     */
    private static void createGroupImage(final String ConversationId, final GroupInfoResponse response) {
        AVIMClientManager.getInstance().findConversationById(ConversationId, new AVIMClientManager.ChatJoinManager() {
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

                GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
                    @Override
                    public void onSuccess(String responseInfo) {
                        JSONObject result = JSONObject.parseObject(responseInfo);
                        if (result.getIntValue("code") == 0) {
                            JSONObject data = result.getJSONObject("data");
                            if (null != data) {
                                response.getInfoSuccess(data);
                            } else {
                                response.getInfoFailed(new Exception(data.getString("msg")));
                            }
                        } else {
                            response.getInfoFailed(new Exception(result.getString("message")));
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

    public static void sendShareMessage(final ContactBean contactBean, GGShareEntity shareEntity, final GroupInfoResponse response) {
        Map<String, Object> lcattrsMap = new HashMap<>();
        lcattrsMap.put("username", UserUtils.getNickname());
        lcattrsMap.put("avatar", UserUtils.getUserAvatar());
        lcattrsMap.put("content", shareEntity.getDesc());
        lcattrsMap.put("title", shareEntity.getTitle());
        lcattrsMap.put("thumUrl", shareEntity.getIcon());
        lcattrsMap.put("link", shareEntity.getLink());
        lcattrsMap.put("toolType", shareEntity.getShareType());
        lcattrsMap.put("live_id", shareEntity.getLive_id());
        lcattrsMap.put("source", shareEntity.getSource());

        Map<Object, Object> messageMap = new HashMap<>();
        messageMap.put("_lctype", "13");
        messageMap.put("_lctext", shareEntity.getTitle());
        messageMap.put("_lcattrs", lcattrsMap);

        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", contactBean.getConv_id());
        params.put("chat_type", "1001");
        params.put("message", JSONObject.toJSONString(messageMap));
        KLog.e(params);

        //分享消息
        final GGShareMessage shareMessage = new GGShareMessage();
        shareMessage.setAttrs(lcattrsMap);
        shareMessage.setText(shareEntity.getTitle());
        shareMessage.setTimestamp(CalendarUtils.getCurrentTime());
        shareMessage.setFrom(UserUtils.getMyAccountId());

        //发送至聊天室
        Map<Object, Object> objectMap = new HashMap<>();
        objectMap.put("share_message", shareMessage);
        objectMap.put("share_convid", contactBean.getConv_id());
        BaseMessage baseMessage = new BaseMessage("message_map", objectMap);
        AppManager.getInstance().sendMessage("oneShare", baseMessage);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(result.get("code"));
                if ((int) result.get("code") == 0) {
                    if (null != response) {
                        response.getInfoSuccess(result.getJSONObject("data"));
                    }
                    //头像暂时未保存
                    IMMessageBean imMessageBean = null;
                    if (null != contactBean) {
                        imMessageBean = new IMMessageBean(contactBean.getConv_id(), 1001, System.currentTimeMillis(),
                                "0", null != contactBean.getTarget() ? contactBean.getTarget() : "", String.valueOf(contactBean.getUserId()),
                                String.valueOf(contactBean.getAvatar()), JSON.toJSONString(shareMessage));
                    }
                    MessageListUtils.saveMessageInfo(imMessageBean);
                }
            }

            @Override
            public void onFailure(String msg) {
                response.getInfoFailed(new Exception(msg));
            }
        };
        new GGOKHTTP(params, GGOKHTTP.CHAT_SEND_MESSAGE, ggHttpInterface).startGet();
    }

    public static void sendShareMessage(final ShareItemInfo shareItemInfo, final GroupInfoResponse response) {
        Map<String, Object> lcattrsMap = new HashMap<>();
        lcattrsMap.put("username", UserUtils.getNickname());
        lcattrsMap.put("avatar", UserUtils.getUserAvatar());
        lcattrsMap.put("content", shareItemInfo.getEntity().getDesc());
        lcattrsMap.put("title", shareItemInfo.getEntity().getTitle());
        lcattrsMap.put("thumUrl", shareItemInfo.getEntity().getIcon());
        lcattrsMap.put("link", shareItemInfo.getEntity().getLink());
        lcattrsMap.put("toolType", shareItemInfo.getEntity().getShareType());
        lcattrsMap.put("live_id", shareItemInfo.getEntity().getLive_id());
        lcattrsMap.put("source", shareItemInfo.getEntity().getSource());

        Map<Object, Object> messageMap = new HashMap<>();
        messageMap.put("_lctype", "13");
        messageMap.put("_lctext", shareItemInfo.getEntity().getTitle());
        messageMap.put("_lcattrs", lcattrsMap);

        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", shareItemInfo.getImMessageBean().getConversationID());
        params.put("chat_type", String.valueOf(shareItemInfo.getImMessageBean().getChatType()));
        params.put("message", JSONObject.toJSONString(messageMap));
        KLog.e(params);

        //分享消息
        final GGShareMessage shareMessage = new GGShareMessage();
        shareMessage.setAttrs(lcattrsMap);
        shareMessage.setText(shareItemInfo.getEntity().getTitle());
        shareMessage.setTimestamp(CalendarUtils.getCurrentTime());
        shareMessage.setFrom(UserUtils.getMyAccountId());

        //发送至聊天室
        Map<Object, Object> objectMap = new HashMap<>();
        objectMap.put("share_message", shareMessage);
        objectMap.put("share_convid", shareItemInfo.getImMessageBean().getConversationID());
        BaseMessage baseMessage = new BaseMessage("message_map", objectMap);
        AppManager.getInstance().sendMessage("oneShare", baseMessage);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(result.get("code"));
                if ((int) result.get("code") == 0) {
                    if (null != response) {
                        response.getInfoSuccess(result.getJSONObject("data"));
                    }
                    //消息缓存消息列表
                    if (null != shareItemInfo.getImMessageBean()) {
                        IMMessageBean imMessageBean = new IMMessageBean(shareItemInfo.getImMessageBean().getConversationID(),
                                shareItemInfo.getImMessageBean().getChatType(), System.currentTimeMillis(), "0",
                                shareItemInfo.getImMessageBean().getNickname(), String.valueOf(shareItemInfo.getImMessageBean().getFriend_id()),
                                shareItemInfo.getImMessageBean().getAvatar(), JSON.toJSONString(shareMessage));
                        MessageListUtils.saveMessageInfo(imMessageBean);
                    }

                }
            }

            @Override
            public void onFailure(String msg) {
                response.getInfoFailed(new Exception(msg));
            }
        };
        new GGOKHTTP(params, GGOKHTTP.CHAT_SEND_MESSAGE, ggHttpInterface).startGet();
    }

    public static void sendImageMessage(final String conversationId, final String chatType, final File file, final MessageResponse messageResponse) {
        //图片宽高处理
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();

        AsyncTaskUtil.doAsync(new AsyncTaskUtil.AsyncCallBack() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void doInBackground() {
                //分个上传UFile;
                UFileUpload.getInstance().upload(file, UFileUpload.Type.IMAGE, new UFileUpload.UploadListener() {
                    @Override
                    public void onUploading(int progress) {

                    }

                    @Override
                    public void onSuccess(String onlineUri) {

                        if (TextUtils.isEmpty(conversationId)) {
                            return;
                        }

                        //图片消息基本信息
                        Map<Object, Object> messageMap = new HashMap<>();
                        messageMap.put("_lctype", "-2");
                        messageMap.put("_lctext", "");
                        messageMap.put("_lcattrs", AVIMClientManager.getInstance().userBaseInfo());
                        messageMap.put("url", onlineUri);
                        messageMap.put("name", file.getPath());
                        messageMap.put("format", "jpg");
                        messageMap.put("height", String.valueOf(height));
                        messageMap.put("width", String.valueOf(width));

                        Map<String, String> params = new HashMap<>();
                        params.put("token", UserUtils.getToken());
                        params.put("conv_id", conversationId);
                        params.put("chat_type", chatType);
                        params.put("message", JSONObject.toJSON(messageMap).toString());
                        KLog.e(onlineUri);

                        //发送图片消息
                        sendAVIMMessage(params, messageResponse);
                    }

                    @Override
                    public void onFailed() {
                    }
                });
            }

            @Override
            public void onPostExecute() {

            }
        });
    }


    public static void sendAVIMMessage(final Map<String, String> params, final MessageResponse messageResponse) {

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    JSONObject data = result.getJSONObject("data");
                    if (data.getBoolean("success")) {
                        if (null != messageResponse) {
                            messageResponse.sendSuccess();
                        }
                    } else {
                        if (null != messageResponse) {
                            messageResponse.sendFailed();
                        }
                    }
                } else {
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != messageResponse) {
                    messageResponse.sendFailed();
                }
            }
        };
        new GGOKHTTP(params, GGOKHTTP.CHAT_SEND_MESSAGE, ggHttpInterface).startGet();

    }


    /**
     * 消息发送状况
     */
    public interface MessageResponse {
        void sendSuccess();

        void sendFailed();
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
    public interface ChatGroupManager {

        void groupActionSuccess(JSONObject object);   ///< 加入房间成功

        void groupActionFail(String error);      ///< 加入房间失败
    }

    /**
     * 申请加群
     */
    public static void applyIntoGroup(final Context context, String conversationId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", conversationId);
        KLog.e(params);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(responseInfo);
                if ((int) result.get("code") == 0) {
                    JSONObject dataJson = result.getJSONObject("data");
                    if (dataJson.getBoolean("success")) {
                        UIHelper.toast(context, "入群申请发送成功");
                    } else {
                        UIHelper.toast(context, "入群申请发送失败");
                    }
                } else {
                    UIHelper.toast(context, "入群申请发送失败");
                }
            }


            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(context, msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.APPLY_INTO_GROUP, ggHttpInterface).startGet();
    }

    /**
     * 查询群信息
     */
    public static void getGroupInfo(String convId, final Impl<JSONObject> callback) {
        if (StringUtils.isActuallyEmpty(convId)) {
            return;
        }
        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("conv_id", convId);
        new GGOKHTTP(params, GGOKHTTP.GET_GROUP_INFO, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (callback != null) {
                    int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                    if (code == 0) {
                        callback.response(Impl.RESPON_DATA_SUCCESS,
                                JSONObject.parseObject(responseInfo).getJSONObject("data"));
                    } else if (code == 1001) {
                        callback.response(Impl.RESPON_DATA_EMPTY, JSONObject.parseObject("没有找到相关信息"));
                    } else {
                        callback.response(Impl.RESPON_DATA_ERROR, JSON.parseObject("请求出错"));
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (callback != null)
                    callback.response(Impl.RESPON_DATA_ERROR, JSON.parseObject("请求出错"));
            }
        }).startGet();
    }
}
