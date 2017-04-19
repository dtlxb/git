package cn.gogoal.im.common.linkUtils;

/**
 * Created by dave.
 * Date: 2017/4/1.
 * Desc: 连麦用到的常量
 */
public class LinkConst {

    /**
     * 自己发送邀请，对方超时未响应，则自己更新本地的连麦状态为未连麦
     */
    public static final int MSG_WHAT_INVITE_CHAT_TIMEOUT = 1;   //连麦邀请响应超时

    /**
     * 别人发送的邀请，自己超时未处理，自动回应不同意连麦，并且在自己的UI层给出提醒
     */
    public static final int MSG_WHAT_PROCESS_INVITING_TIMEOUT = 2;

    /**
     * 同意连麦后，等待混流成功超时
     */
    public static final int MSG_WHAT_MIX_STREAM_TIMEOUT = 3;

    /**
     * InternalError, MainStreamNotExist, MixStreamNotExist都认为是混流错误，
     * 超过{WatchLivePresenter#WAITING_FOR_MIX_SUCCESS_DELAY}时间还没收到Success的code，就会结束连麦
     */
    public static final int MSG_WHAT_MIX_STREAM_ERROR = 4;



    public static final long INVITE_CHAT_TIMEOUT_DELAY = 10 * 1000;   //连麦邀请等待响应超时时间——10秒

    /**
     * 播放器使用
     */
    public static final long INTERRUPT_DELAY = 3000;

    public static final int STATUS_NOT_AGREE = 2;
    public static final int STATUS_AGREE = 1;

    public static final int INVITE_TYPE_WATCHER = 1;
    public static final int INVITE_TYPE_ANCHOR = 2;
}
