package cn.gogoal.im.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alivc.player.MediaPlayer;
import com.alivc.publisher.IMediaPublisher;
import com.alivc.publisher.MediaConstants;
import com.alivc.publisher.MediaError;
import com.alivc.videochat.AlivcVideoChatParter;
import com.alivc.videochat.IVideoChatParter;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.hply.roundimage.roundImage.RoundedImageView;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.LiveOnlinePersonData;
import cn.gogoal.im.common.AnimationUtils;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVIMClientManager;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.PlayerUtils.CountDownTimerView;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.linkUtils.ConnectivityMonitor;
import cn.gogoal.im.common.linkUtils.HeadsetMonitor;
import cn.gogoal.im.common.linkUtils.LinkConst;
import cn.gogoal.im.common.linkUtils.PlayDataStatistics;
import cn.gogoal.im.common.linkUtils.VideoChatStatus;
import cn.gogoal.im.fragment.WatchBottomFragment;
import cn.gogoal.im.ui.dialog.LiveCloseDialog;

/*
* 观看直播页面
* */
public class WatchLiveActivity extends BaseActivity {

    @BindView(R.id.root_container)
    FrameLayout mRootContainer;

    //推流预览的SurfaceView
    @BindView(R.id.preview_surface_view)
    SurfaceView mPreviewSurfaceView;
    //播放的SurfaceView
    @BindView(R.id.play_surface_view)
    SurfaceView mPlaySurfaceView;
    //播放关闭按钮
    @BindView(R.id.iv_abort_chat)
    ImageView mIvChatClose;
    //详情相关控件
    @BindView(R.id.imgPalyer)
    RoundedImageView imgPalyer;
    @BindView(R.id.textCompany)
    TextView textCompany;
    @BindView(R.id.textOnlineNumber)
    TextView textOnlineNumber;
    @BindView(R.id.textAddFriend)
    TextView textAddFriend;
    @BindView(R.id.recyAudience)
    RecyclerView recyAudience;
    //倒计时
    @BindView(R.id.countDownTimer)
    CountDownTimerView countDownTimer;
    //缓冲控件
    @BindView(R.id.LayoutTip)
    LinearLayout LayoutTip;
    @BindView(R.id.text_tip)
    ImageView text_tip;

    //聊天显示列表
    @BindView(R.id.recycPortrait)
    RecyclerView recyler_chat;

    private RotateAnimation animation;

    /*
    * 直播所需定义参数
    * */
    enum SurfaceStatus {
        UNINITED, CREATED, CHANGED, DESTROYED, RECREATED; //surface状态
    }

    private SurfaceStatus mPreviewSurfaceStatus = SurfaceStatus.UNINITED;
    private SurfaceStatus mPlaySurfaceStatus = SurfaceStatus.UNINITED;

    private VideoChatStatus mChatStatus = VideoChatStatus.UNCHAT; //当前的连麦状态，初始为未连麦状态

    AlivcVideoChatParter mChatParter;
    Map<String, String> mMediaParam = new HashMap<>();
    Map mFilterMap = new HashMap<>();
    private long mStartTime = 0;
    private boolean isBeautyOn = false;
    private boolean isFlashOn = false;
    private Boolean isCaching = false;

    public boolean isPlaying = false;
    private boolean mIsPublishPaused = false;
    private boolean isDestoyed = false; //activity是否销毁了，如果销毁了，则关闭连麦时就不需要做UI提醒了

    private String mIMFailedMessage;
    private ConnectivityMonitor mConnectivityMonitor = new ConnectivityMonitor();
    private HeadsetMonitor mHeadsetMonitor = new HeadsetMonitor();
    private boolean shouldOffLine = false; //surfaceChange时是否应该结束连麦

    //播放地址
    private String mPlayUrl;
    //推送地址
    private String mPushUrl;
    //连麦延迟播放网址
    private String mSmallDelayPlayUrl;

    //聊天对象
    private List<AVIMMessage> messageList = new ArrayList<>();
    private LiveChatAdapter mLiveChatAdapter;
    private AVIMConversation imConversation;
    //在线人头像显示
    private List<LiveOnlinePersonData> audienceList = new ArrayList<>();
    private LiveOnlineAdapter audienceAdapter;

    private String room_id;
    private String live_id;

    private String appoint_account;

    //弹窗
    private JSONObject anchor;

    private WatchBottomFragment mBottomFragment;

    private AlertDialog mFeedbackChooseDialog;
    private AlertDialog mChatCloseConfirmDialog;
    private LiveCloseDialog mLiveCloseDialog = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                /**
                 * 自己发送邀请，对方超时未响应，则自己更新本地的连麦状态为未连麦
                 */
                case LinkConst.MSG_WHAT_INVITE_CHAT_TIMEOUT://连麦响应超时
                    mChatStatus = VideoChatStatus.UNCHAT;
                    UIHelper.toast(getContext(), R.string.invite_timeout_tip); //提醒：对方长时间未响应，已取消连麦邀请
                    break;
                case LinkConst.MSG_WHAT_PROCESS_INVITING_TIMEOUT:
                    mFeedbackChooseDialog.dismiss();
                    mFeedbackChooseDialog = null;
                    feedbackInvite(LinkConst.STATUS_NOT_AGREE); //自动反馈不同意连麦
                    UIHelper.toast(getContext(), R.string.inviting_process_timeout); //提醒超时未处理，已经自动拒绝对方的连麦邀请
                    break;
                case LinkConst.MSG_WHAT_MIX_STREAM_ERROR:
                    if (isChatting()) {
                        closeVideoCall();
                    }
            }
        }
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_watch_live;
    }

    @Override
    public void doBusiness(Context mContext) {

        live_id = getIntent().getStringExtra("live_id");

        animation = AnimationUtils.getInstance().setLoadingAnime(text_tip, R.mipmap.login_loading);
        animation.startNow();

        initHorizontalRecycleView(recyAudience);
        audienceAdapter = new LiveOnlineAdapter(audienceList);
        recyAudience.setAdapter(audienceAdapter);

        mMediaParam.put(MediaConstants.PUBLISHER_PARAM_ORIGINAL_BITRATE, "" + 800000);
        mMediaParam.put(MediaConstants.PUBLISHER_PARAM_MIN_BITRATE, "" + 600000);
        mMediaParam.put(MediaConstants.PUBLISHER_PARAM_MAX_BITRATE, "" + 1000000);

        initPlayer();

        //一进入界面就播放主播直播
        mPlaySurfaceView.setZOrderMediaOverlay(true);
        mPlaySurfaceView.getHolder().addCallback(mPlaySurfaceCB);
        mPreviewSurfaceView.setZOrderOnTop(false);
        mPreviewSurfaceView.getHolder().addCallback(mPublishSurfaceCB);

        mHeadsetMonitor.setHeadsetStatusChangedListener(new WatchLivePresenter());

        initRecycleView(recyler_chat, null);
        mLiveChatAdapter = new LiveChatAdapter(R.layout.item_live_chat, messageList);
        recyler_chat.setAdapter(mLiveChatAdapter);

    }

    /**
     * 初始化水平方向的列表
     */
    private void initHorizontalRecycleView(RecyclerView rvHorizontal) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHorizontal.setLayoutManager(layoutManager);
    }

    /**
     * 加入聊天室
     */
    private void joinSquare(final AVIMConversation conversation) {
        List<Integer> idList = new ArrayList<>();

        idList.add(Integer.parseInt(UserUtils.getMyAccountId()));
        ChatGroupHelper.addAnyone(idList, conversation.getConversationId(), new ChatGroupHelper.ChatGroupManager() {
            @Override
            public void groupActionSuccess(JSONObject object) {

            }

            @Override
            public void groupActionFail(String error) {

            }
        });

        //获取当前群聊天人信息
        if (conversation != null) {
            conversation.fetchInfoInBackground(new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    //拉取群通讯录并缓存本地
                    UserUtils.getChatGroup(AppConst.LIVE_GROUP_CONTACT_BEANS, conversation.getMembers(), conversation.getConversationId(), new UserUtils.getSquareInfo() {
                        @Override
                        public void squareGetSuccess(JSONObject object) {
                            KLog.e(object.toJSONString());
                            List<LiveOnlinePersonData> accountList = JSONArray.parseArray(String.valueOf(object.getJSONArray("accountList")), LiveOnlinePersonData.class);
                            if (accountList != null) {
                                for (int i = 0; i < accountList.size(); i++) {
                                    if (accountList.get(i).getFriend_id().equals(appoint_account) || accountList.get(i).getFriend_id().equals(UserUtils.getMyAccountId())) {
                                        accountList.remove(i);
                                    }
                                }
                                audienceList.addAll(accountList);
                                audienceAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void squareGetFail(String error) {

                        }
                    });
                }
            });
        }
    }

    /**
     * 退出聊天室
     */
    private void quiteSquare(AVIMConversation conversation) {
        List<Integer> idList = new ArrayList<>();

        idList.add(Integer.parseInt(UserUtils.getMyAccountId()));

        ChatGroupHelper.deleteAnyone(idList, conversation.getConversationId(), new ChatGroupHelper.ChatGroupManager() {
            @Override
            public void groupActionSuccess(JSONObject object) {

            }

            @Override
            public void groupActionFail(String error) {

            }
        });
    }

    /**
     * 聊天适配器
     */
    class LiveChatAdapter extends CommonAdapter<AVIMMessage, BaseViewHolder> {

        public LiveChatAdapter(int layoutId, List<AVIMMessage> datas) {
            super(layoutId, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, AVIMMessage message, int position) {
            TextView textSend = holder.getView(R.id.text_you_send);
            if (null != message) {
                JSONObject contentObject = JSON.parseObject(message.getContent());
                JSONObject lcattrsObject = JSON.parseObject(contentObject.getString("_lcattrs"));
                String _lctype = contentObject.getString("_lctype");
                String label = "";
                String content = "";
                String username = "";
                if (_lctype.equals("-1")) {
                    label = lcattrsObject.getString("username") + ": ";
                    content = contentObject.getString("_lctext");
                } else if (_lctype.equals("5") || _lctype.equals("6")) {
                    if (null != lcattrsObject.get("accountList")) {
                        JSONArray jsonArray = lcattrsObject.getJSONArray("accountList");
                        if (jsonArray.size() > 0) {
                            username = ((JSONObject) jsonArray.get(0)).getString("nickname");
                            label = "系统消息: ";
                        }
                        if (_lctype.equals("5")) {
                            content = (username + "加入了直播间");
                        } else {
                            content = (username + "离开了直播间");
                        }
                    }
                } else {

                }
                textSend.setText(label + content);

                SpannableStringBuilder builder = new SpannableStringBuilder(textSend.getText().toString());
                ForegroundColorSpan Span1 = new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.live_chat_yellow));
                ForegroundColorSpan Span2 = null;
                if (_lctype.equals("-1")) {
                    Span2 = new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.live_chat_white));
                } else if (_lctype.equals("5") || _lctype.equals("6")) {
                    Span2 = new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.live_start_text));
                }
                builder.setSpan(Span1, 0, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(Span2, label.length(), textSend.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textSend.setText(builder);
            }
        }
    }

    /**
     * 底部按钮点击
     */
    private WatchBottomFragment.RecorderUIClickListener mUIClickListener = new WatchBottomFragment.RecorderUIClickListener() {

        @Override
        public void onSendComment(final EditText player_edit) {
            //发送评论
            if (null != imConversation) {
                //前端显示
                HashMap<String, Object> attrsMap = new HashMap<String, Object>();
                attrsMap.put("username", UserUtils.getUserName());

                final AVIMTextMessage msg = new AVIMTextMessage();
                msg.setText(player_edit.getText().toString());
                msg.setAttrs(attrsMap);

                messageList.add(msg);
                mLiveChatAdapter.notifyDataSetChanged();
                recyler_chat.smoothScrollToPosition(messageList.size());

                //聊天内容发往后台
                Map<Object, Object> messageMap = new HashMap<>();
                messageMap.put("_lctype", "-1");
                messageMap.put("_lctext", player_edit.getText().toString());
                messageMap.put("_lcattrs", AVIMClientManager.getInstance().userBaseInfo());

                HashMap<String, String> params = new HashMap<>();
                params.put("token", UserUtils.getToken());
                params.put("conv_id", imConversation.getConversationId());
                params.put("chat_type", "1009");
                params.put("message", JSONObject.toJSON(messageMap).toString());

                //发送文字消息
                GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
                    @Override
                    public void onSuccess(String responseInfo) {
                        KLog.json(responseInfo);
                        player_edit.setText("");
                        mBottomFragment.hideCommentEditUI();

                        /*player_edit.setVisibility(View.GONE);
                        InputMethodManager imm =
                                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(player_edit.getWindowToken(), 0);*/
                    }

                    @Override
                    public void onFailure(String msg) {
                        KLog.json(msg);
                    }
                };
                new GGOKHTTP(params, GGOKHTTP.CHAT_SEND_MESSAGE, ggHttpInterface).startGet();
            }
        }

        @Override
        public void onSwitchFullScreen() {
            if (AppDevice.isLandscape(getContext())) {
                mBottomFragment.showChatView();
                recyler_chat.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (AppDevice.isPortrait(getContext())) {
                mBottomFragment.hideChatView();
                recyler_chat.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        @Override
        public int onSwitchCamera() {
            if (mChatParter != null) {
                mChatParter.switchCamera();
            }
            return -1;
        }

        @Override
        public void onFinish() {
            if (AppDevice.isLandscape(getContext())) {
                mBottomFragment.showChatView();
                recyler_chat.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (AppDevice.isPortrait(getContext())) {
                isPlaying = false;
                onBackPressed();
            }
        }

    };

    /**
     * 初始化播放器
     */
    private void initPlayer() {
        mChatParter = new AlivcVideoChatParter();
        mChatParter.setErrorListener(mPlayerErrorListener);
        mChatParter.init(this);
        mChatParter.setInfoListener(mPlayerInfoListener);
        mChatParter.setParterViewScalingMode(IMediaPublisher.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

        //设置连麦预览/推流时开启美颜
        mFilterMap.put(AlivcVideoChatParter.ALIVC_FILTER_PARAM_BEAUTY_ON, Boolean.toString(true));
        mChatParter.setFilterParam(mFilterMap);
    }

    /**
     * 播放错误回调
     */
    AlivcVideoChatParter.OnErrorListener mPlayerErrorListener = new AlivcVideoChatParter.OnErrorListener() {

        @Override
        public boolean onError(IVideoChatParter iVideoChatParter, int what, int extra) {
            KLog.e("WatchLiveActivity-->what = " + what + ", extra = " + extra);
            if (what == 0) {
                return false;
            }
            if (mChatParter == null) {
                return false;
            }

            switch (what) {
                case MediaError.ALIVC_ERR_PLAYER_INVALID_INPUTFILE:
                case MediaError.ALIVC_ERR_PLAYER_OPEN_FAILED:
                case MediaError.ALIVC_ERR_PLAYER_NO_MEMORY:
                case MediaError.ALIVC_ERR_PLAYER_INVALID_CODEC:
                case MediaError.ALIVC_ERR_PLAYER_NO_SURFACEVIEW:
                case MediaError.ALIVC_ERR_PLAYER_UNSUPPORTED:
                case MediaError.ALIVC_ERR_PLAYER_READ_PACKET_TIMEOUT:
                case MediaError.ALIVC_ERR_PLAYER_NO_NETWORK:
                case MediaError.ALIVC_ERR_PLAYER_UNKNOW:
                    UIHelper.toast(getContext(), R.string.error_stop_playing);
                    //如果正在连麦则结束连麦
                    if (isChatting()) {
                        closeVideoCall();
                    }
                    stopPlaying();
                    break;
                case MediaError.ALIVC_ERR_PLAYER_TIMEOUT:
                    KLog.e("encounter player timeout, so call restartToPlayer");
                    mChatParter.reconnect();
                    break;
                case MediaError.ALIVC_ERR_PUBLISHER_AUDIO_CAPTURE_DISABLED://音频采集失败
                case MediaError.ALIVC_ERR_PUBLISHER_AUDIO_CAPTURE_NO_DATA:
                    if (isChatting()) {
                        KLog.e("音频采集失败，结束连麦");
                        closeVideoCall();
                    } else {
                        KLog.e("音频采集失败，但是当前没有处于连麦状态");
                    }
                    break;
                case MediaError.ALIVC_ERR_PUBLISHER_VIDEO_CAPTURE_DISABLED:
                    if (isChatting()) {
                        UIHelper.toast(getContext(), R.string.camera_open_failure_for_chat);
                        closeVideoCall();
                    }
                    break;
                case MediaError.ALIVC_ERR_PUBLISHER_ENCODE_AUDIO_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_AUDIO_ENCODER_INIT_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_MALLOC_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_NETWORK_UNCONNECTED:
                case MediaError.ALIVC_ERR_PUBLISHER_NETWORK_POOR:
                case MediaError.ALIVC_ERR_PUBLISHER_SEND_DATA_TIMEOUT:
                case MediaError.ALIVC_ERR_PUBLISHER_ENCODE_VIDEO_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_VIDEO_ENCODER_INIT_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_ILLEGAL_ARGUMENT:
                    UIHelper.toast(getContext(), R.string.network_busy);
                    break;
                default:
                    UIHelper.toast(getContext(), R.string.error_unknown);
            }
            return true;
        }
    };

    /**
     * 播放状态回调
     */
    AlivcVideoChatParter.OnInfoListener mPlayerInfoListener = new AlivcVideoChatParter.OnInfoListener() {

        @Override
        public boolean onInfo(IVideoChatParter iVideoChatParter, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_UNKNOW:
                    //未知
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    //开始缓冲
                    synchronized (isCaching) {
                        if (!isCaching) {
                            show_buffering_ui(true);
                            isCaching = true;
                        }
                    }
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    synchronized (isCaching) {
                        if (isCaching) {
                            show_buffering_ui(false);
                            // 结束缓冲
                            isCaching = false;
                        }
                    }
                    break;
                case MediaError.ALIVC_INFO_PLAYER_FIRST_FRAME_RENDERED:
                    // 首帧显示时间
                    if (!isChatting()) {
                        //计算首帧耗时
                        KLog.e("首帧耗时: " + (System.currentTimeMillis() - mStartTime) + "ms");
                    }
                    show_buffering_ui(false);
                    break;
                case MediaError.ALIVC_INFO_PUBLISH_DISPLAY_FIRST_FRAME:
                    //预览首帧渲染完成
                    if (isChatting()) {//如果是正在连麦的状态，则首帧渲染完后需要通知UI层隐藏【正在连麦】的view
                    }
                    break;
                case MediaError.ALIVC_INFO_PUBLISH_NETWORK_GOOD:
                case MediaError.ALIVC_INFO_PUBLISH_RECONNECT_START:
                case MediaError.ALIVC_INFO_PUBLISH_RECONNECT_SUCCESS:
                case MediaError.ALIVC_INFO_PUBLISH_RECONNECT_FAILURE:
                case MediaError.ALIVC_INFO_PLAYER_PREPARED_PROCESS_FINISHED:
                case MediaError.ALIVC_INFO_PLAYER_INTERRUPT_PLAYING:
                case MediaError.ALIVC_INFO_PLAYER_STOP_PROCESS_FINISHED:
            }
            KLog.e("MediaPlayer onInfo, what =" + what + ", extra = " + extra);
            return false;
        }
    };

    /**
     * 播放surface
     */
    SurfaceHolder.Callback mPlaySurfaceCB = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            KLog.e("parter player surface create.");
            holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
            holder.setKeepScreenOn(true);

            if (mPlaySurfaceStatus == SurfaceStatus.UNINITED) {
                mPlaySurfaceStatus = SurfaceStatus.CREATED;
                getPlayerInfo();
                KLog.e("Player surface status is created");
            } else if (mPlaySurfaceStatus == SurfaceStatus.DESTROYED) {
                mPlaySurfaceStatus = SurfaceStatus.RECREATED;
                KLog.e("Player surface status is recreated");
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            KLog.e("parter player surface change.");
            mPlaySurfaceStatus = SurfaceStatus.CHANGED;

            if (mPreviewSurfaceStatus == null
                    || mPreviewSurfaceStatus == SurfaceStatus.UNINITED
                    || mPreviewSurfaceStatus == SurfaceStatus.CHANGED) {
                KLog.e("WatchLivePresenter", "SurfaceChanged resume");
                mediaResume(mPlaySurfaceView, mPreviewSurfaceView);

                //TODO：对于需要结束连麦的情况，因为surface很可能重建了，所以需要sleep一秒，不然可能出现offlineChat后不能恢复播放的情况
                if (shouldOffLine) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (shouldOffLine) {
                //在这里调用真正的offlineChat，保证渲染出得最后一帧数据是正常播放的尺寸，而不是小窗播放的尺寸
                sdkOfflineChat();
                shouldOffLine = false;
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mPlaySurfaceStatus = SurfaceStatus.DESTROYED;
            KLog.e("parter player surface destroy.");
        }
    };

    /**
     * 推流surface
     */
    SurfaceHolder.Callback mPublishSurfaceCB = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            KLog.e("WatchLiveActivity-->preview surface created");
            holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
            holder.setKeepScreenOn(true);

            if (mPreviewSurfaceStatus == SurfaceStatus.UNINITED) {
                mPreviewSurfaceStatus = SurfaceStatus.CREATED;
            } else if (mPreviewSurfaceStatus == SurfaceStatus.DESTROYED) {
                mPreviewSurfaceStatus = SurfaceStatus.RECREATED;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            KLog.e("WatchLiveActivity-->preview surface changed, width=" + width + ",height=" + height);
            mPreviewSurfaceStatus = SurfaceStatus.CHANGED;
            if (mPreviewSurfaceStatus == SurfaceStatus.CHANGED && mPlaySurfaceStatus == SurfaceStatus.CHANGED) {
                mediaResume(mPlaySurfaceView, mPreviewSurfaceView);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            KLog.e("WatchLiveActivity-->preview surface destroyed");
            mPreviewSurfaceStatus = SurfaceStatus.DESTROYED;
        }
    };

    /**
     * 开始直播播放（大窗）
     *
     * @param surfaceView
     * @param mPlayUrl
     */
    public void startToPlay(String mPlayUrl, final SurfaceView surfaceView) {
        if (mChatParter == null) {
            initPlayer();
        }
        mStartTime = System.currentTimeMillis();
        KLog.e("WatchActivity --> mChatParter.startToPlay");
        mChatParter.startToPlay(mPlayUrl, surfaceView); //开始直播
        isPlaying = true;
        //TODO:显示loading
    }

    /**
     * 恢复播放/连麦
     *
     * @param previewSurf
     * @param playSurf
     */
    public void mediaResume(SurfaceView previewSurf, SurfaceView playSurf) {
        if (mChatParter != null && mIsPublishPaused) {
            KLog.e("WatchLiveActivity-->mChatParter.resume(), previewSurf = " + previewSurf + ", playSurf = " + playSurf);
            mChatParter.resume(previewSurf, playSurf);
            mIsPublishPaused = false;
        }
    }

    /**
     * 暂停播放 or 连麦
     */
    public void mediaPause() {
        if (mChatParter != null && !mIsPublishPaused) {
            mChatParter.pause();
            KLog.e("WatchLiveActivity--> mChatParter.pause()");
            mIsPublishPaused = true;
        }
    }

    /**
     * sdk调用offlineChat，结束连麦，这里之所以要暴露出接口，是为了playSurfaceView的surfaceChanged中可以调用，
     * 需要在surfaceChange发生后再调用真正的offlineChat，否则会出现左上角显示小窗的问题
     */
    public void sdkOfflineChat() {
        if (mChatParter != null && isChatting()) {
            KLog.e("WatchActivity --> offlineChat, Thread id: " + Thread.currentThread().getId());
            mChatParter.offlineChat();      //客户端SDK结束连麦
            mChatStatus = VideoChatStatus.UNCHAT; //更新当前连麦状态为未连麦状态
        } else {
            KLog.e("WatchActivity --> offlineChat, but mChatParter is null");
        }
    }

    /**
     * 设置对耳机状态的监听，并且通知给SDK
     */
    class WatchLivePresenter implements HeadsetMonitor.HeadSetStatusChangedListener {

        @Override
        public void onHeadsetStatusChanged(boolean on) {
            mChatParter.changeEarPhoneWhenChat(on);
        }
    }

    /**
     * 当前是否正在连麦
     *
     * @return
     */
    private boolean isChatting() {
        return mChatStatus == VideoChatStatus.MIX_SUCC || mChatStatus == VideoChatStatus.TRY_MIX; //混流成功和正在混流都认为是正在连麦的状态
    }

    /**
     * 结束连麦
     */
    public void closeVideoCall() {
        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                JSONObject data = object.getJSONObject("data");
                if (object.getIntValue("code") == 0 && data.getBooleanValue("result")) {
                    //调用api结束连麦成功
                    if (isChatting()) {
                        abortChat(true);
                    }
                } else {
                    //显示结束连麦失败的
                    KLog.e("Close video chat failed");
                    UIHelper.toast(getContext(), R.string.close_video_chatting_failed);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.VIDEOCALL_CLOSE, ggHttpInterface).startGet();
    }

    /**
     * 结束连麦成功处理
     *
     * @param isShowUI 是否需要显示相应的UI
     */
    private void abortChat(boolean isShowUI) {
        KLog.e("call abortChat(" + isShowUI + "), isDestroyed = " + isDestoyed);
        if (isShowUI && !isDestoyed) {
            changePlayViewToNormalMode();
            closeVideoChatSmallView();
        }
    }

    /**
     * 播放的View切换到正常的播放模式（小窗变大窗）
     */
    public void changePlayViewToNormalMode() {
        if (mPlaySurfaceView != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mPlaySurfaceView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            } else {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            layoutParams.bottomMargin = 0;
            layoutParams.rightMargin = 0;
            layoutParams.gravity = Gravity.NO_GRAVITY;
            mPlaySurfaceView.setLayoutParams(layoutParams);
        }
        mIvChatClose.setVisibility(View.GONE);
        KLog.e("mIvChatClose setVisibility gone");
        mBottomFragment.hideCameraView();
        shouldOffLine = true; //表示在surfaceChanged时需要调用真正的offlineChat，来结束连麦
    }

    /**
     * 关闭小窗播放的UI更新
     */
    public void closeVideoChatSmallView() {
        mIvChatClose.setVisibility(View.GONE);
        mBottomFragment.hideCameraView();
    }

    /**
     * 停止播放
     */
    public void stopPlaying() {
        if (mChatParter != null) {
            KLog.e("WatchActivity --> mChatPartter.stopPlaying(), Thread id: " + Thread.currentThread().getId());
            mChatParter.stopPlaying();
        }
    }

    private Runnable mShowInterruptRun = new Runnable() {
        @Override
        public void run() {
            LayoutTip.setVisibility(View.VISIBLE);
            animation.startNow();
        }
    };

    /*
    * 缓冲显示
    * */
    private void show_buffering_ui(boolean bShowTip) {
        if (bShowTip) {
            mHandler.postDelayed(mShowInterruptRun, LinkConst.INTERRUPT_DELAY);
        } else {
            mHandler.removeCallbacks(mShowInterruptRun);
            LayoutTip.setVisibility(View.GONE);
            if (animation != null) {
                animation.cancel();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnectivityMonitor.register(this); //注册对网络状态的监听
        mHeadsetMonitor.register(this); //注册对耳机状态的监听

        if (mPlaySurfaceStatus != null && mPlaySurfaceStatus != SurfaceStatus.DESTROYED) {
            mediaResume(mPlaySurfaceView, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConnectivityMonitor.unRegister(this);
        mHeadsetMonitor.unRegister(this);

        mediaPause();
        isCaching = false;
        show_buffering_ui(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KLog.e("WatchLiveActivity--> onDestroy");
        isDestoyed = true; //标记当前activity销毁了，则不需要再做UI提醒了
        if (mChatParter != null) {
            mChatParter.setErrorListener(null);
            //如果当前正在连麦，则要结束连麦
            if (isChatting()) {
                closeVideoCall();
            }
            mChatParter.stopPlaying(); //结束播放
            mChatParter.release();
        }

        //TODO:通知服务端，退出观看

        if (null != imConversation) {
            quiteSquare(imConversation);
        }
    }

    @OnClick({R.id.iv_abort_chat, R.id.textAddFriend})
    public void setClickFunctionBar(View v) {
        switch (v.getId()) {
            case R.id.iv_abort_chat: //关闭播放
                showChatCloseConfirmDialog();
                break;
            case R.id.textAddFriend: //加好友
                Intent intent = new Intent(getContext(), IMAddFriendActivity.class);
                intent.putExtra("user_id", appoint_account);
                startActivity(intent);
                break;
        }
    }

    /**
     * 显示结束连麦确认的dialog
     */
    private void showChatCloseConfirmDialog() {
        AlertDialog.Builder dialog = null;
        if (mChatCloseConfirmDialog == null) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case DialogInterface.BUTTON_POSITIVE: //确定
                            //结束连麦
                            closeVideoCall();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE: //取消
                            mChatCloseConfirmDialog.dismiss();
                            break;
                    }
                    mChatCloseConfirmDialog = null;
                }
            };
            dialog = DialogHelp.getConfirmDialog(getContext(), getString(R.string.close_video_call_title),
                    getString(R.string.close_video_call_confirm_message), getString(R.string.sure),
                    getString(R.string.cancel), listener, listener);
            dialog.setCancelable(false);
        }

        mChatCloseConfirmDialog = dialog.show();
    }

    /**
     * 获取直播详情
     */
    private void getPlayerInfo() {

        Map<String, String> param = new HashMap<>();

        param.put("live_id", live_id);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONArray("data").getJSONObject(0);
                    //直播详情
                    ImageDisplay.loadCircleImage(getContext(), data.getString("face_url"), imgPalyer);
                    textCompany.setText(data.getString("anchor_name"));
                    appoint_account = data.getString("appoint_account");

                    /*if (appoint_account != null) {
                        if (UserUtils.isMyFriend(data.getIntValue("appoint_account"))) {
                            textAddFriend.setVisibility(View.GONE);
                        } else {
                            textAddFriend.setVisibility(View.VISIBLE);
                        }
                    } else {
                        textAddFriend.setVisibility(View.GONE);
                    }*/

                    //主播介绍
                    anchor = data.getJSONObject("anchor");

                    mPlayUrl = data.getString("url_rtmp");

                    //倒计时
                    if (data.getLongValue("launch_time") > 0) {
                        show_buffering_ui(false);

                        countDownTimer.setVisibility(View.VISIBLE);
                        countDownTimer.addTime(data.getString("live_time_start"));
                        countDownTimer.start();

                        countDownTimer.setDownCallBack(new CountDownTimerView.CountDownCallBack() {
                            @Override
                            public void startPlayer() {
                                countDownTimer.setVisibility(View.GONE);
                                startToPlay(mPlayUrl, mPlaySurfaceView);
                            }
                        });
                    } else {
                        countDownTimer.setVisibility(View.GONE);

                        PlayDataStatistics.getStatisticalData(getContext(), "1", live_id, "2", "1");
                    }

                    startToPlay(mPlayUrl, mPlaySurfaceView);

                    room_id = data.getString("room_id");

                    AVIMClientManager.getInstance().findConversationById(room_id, new AVIMClientManager.ChatJoinManager() {
                        @Override
                        public void joinSuccess(AVIMConversation conversation) {
                            imConversation = conversation;
                            joinSquare(imConversation);
                        }

                        @Override
                        public void joinFail(String error) {
                            UIHelper.toast(getActivity(), "获取聊天房间失败");
                        }
                    });

                    getOnlineCount(room_id);

                    mBottomFragment = WatchBottomFragment.newInstance(live_id, String.valueOf(anchor), data.getString("introduction_img"), data.getString("introduction"));
                    mBottomFragment.setRecordUIClickListener(mUIClickListener);
                    mBottomFragment.setActivityRootView(mRootContainer);
                    mBottomFragment.setType(1);
                    getSupportFragmentManager().beginTransaction().replace(R.id.bottom_container, mBottomFragment).commit();

                } else {
                    UIHelper.toast(getContext(), R.string.net_erro_hint);
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };

        new GGOKHTTP(param, GGOKHTTP.GET_STUDIO_LIST, ggHttpInterface).startGet();
    }

    /*
    * 获取直播在线人数
    * */
    private void getOnlineCount(String room_id) {

        Map<String, String> param = new HashMap<>();
        param.put("conv_id", room_id);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    textOnlineNumber.setText(data.getIntValue("result") + "在线");
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_ONLINE_COUNT, ggHttpInterface).startGet();
    }

    /**
     * 消息接收
     */
    @Subscriber(tag = "Live_Message")
    public void handleMessage(BaseMessage baseMessage) {

        if (null != imConversation && null != baseMessage) {
            Map<String, Object> map = baseMessage.getOthers();
            AVIMMessage message = (AVIMMessage) map.get("message");
            AVIMConversation conversation = (AVIMConversation) map.get("conversation");

            //lctype==12关闭
            JSONObject contentObject = JSON.parseObject(message.getContent());
            int _lctype = contentObject.getIntValue("_lctype");
            int chatType = (int) conversation.getAttribute("chat_type");

            KLog.e(message.getContent());

            if (imConversation.getConversationId().equals(conversation.getConversationId()) && chatType == 1009) {
                //Live消息处理
                messageList.add(message);
                mLiveChatAdapter.notifyDataSetChanged();
                recyler_chat.smoothScrollToPosition(messageList.size());

                //显示头像
                JSONObject lcattrsObject = JSON.parseObject(contentObject.getString("_lcattrs"));
                JSONArray jsonArray = lcattrsObject.getJSONArray("accountList");
                List<LiveOnlinePersonData> personData = JSONObject.parseArray(String.valueOf(jsonArray), LiveOnlinePersonData.class);
                if (!personData.get(0).getFriend_id().equals(appoint_account)) {
                    if (_lctype == 5) {
                        audienceList.addAll(0, personData);
                        audienceAdapter.notifyDataSetChanged();
                    } else if (_lctype == 6) {
                        for (int i = 0; i < audienceList.size(); i++) {
                            if (audienceList.get(i).getFriend_id().equals(personData.get(0).getFriend_id())) {
                                audienceList.remove(i);
                            }
                        }
                        audienceAdapter.notifyDataSetChanged();
                    }
                }

                //主播关闭直播了
                if (_lctype == 12) {
                    KLog.e("结束直播");
                    showLiveCloseUI();
                }

            } else if (chatType == 1008) {
                //连麦动作处理
                JSONObject content = JSONObject.parseObject(message.getContent());
                JSONObject lcattrs = content.getJSONObject("_lcattrs");

                switch (lcattrs.getString("code")) {
                    case "invite":
                        showFeedbackChooseDialog();
                        //更新当前连麦状态为收到邀请等待反馈状态
                        mChatStatus = VideoChatStatus.RECEIVED_INVITE;
                        //超过10s自动拒绝连麦
                        //mHandler.sendEmptyMessageDelayed(LinkConst.MSG_WHAT_PROCESS_INVITING_TIMEOUT,LinkConst.INVITE_CHAT_TIMEOUT_DELAY);
                        break;
                    case "mixresult":
                        //混流失败
                        if (isChatting()) {
                            UIHelper.toast(getContext(), R.string.merge_stream_failed);
                            closeVideoCall();
                        }
                        break;
                    case "close":
                        //对方关闭连麦
                        if (isChatting()) {
                            //中断连麦推流
                            abortChat(true);
                            //显示对方结束连麦的UI提示
                            showChatCloseNotifyUI();
                        } else if (mChatStatus == VideoChatStatus.RECEIVED_INVITE) {
                            //如果当前是收到邀请还未处理的状态，则需要隐藏邀请反馈选择的dialog
                            if (mFeedbackChooseDialog != null && mFeedbackChooseDialog.isShowing()) {
                                mFeedbackChooseDialog.dismiss();
                            }
                            UIHelper.toast(getContext(), R.string.video_chatting_timeout);
                            mChatStatus = VideoChatStatus.UNCHAT;
                        }
                        break;

                }
            }
        }
    }

    /**
     * 显示直播邀请结果选择的Dialog
     */
    private void showFeedbackChooseDialog() {
        AlertDialog.Builder dialog = null;
        if (mFeedbackChooseDialog == null) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case DialogInterface.BUTTON_POSITIVE: //同意
                            feedbackInvite(LinkConst.STATUS_AGREE);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE: //不同意
                            feedbackInvite(LinkConst.STATUS_NOT_AGREE);
                            break;
                    }

                    mFeedbackChooseDialog = null;
                }
            };
            dialog = DialogHelp.getConfirmDialog(getContext(), getString(R.string.received_calling),
                    getString(R.string.video_callling_message), getString(R.string.agree),
                    getString(R.string.not_agree), listener, listener);
            dialog.setCancelable(false);
        }

        mFeedbackChooseDialog = dialog.show();
    }

    /**
     * 反馈连麦邀请
     *
     * @param status 反馈的结果：同意1， 不同意2
     */
    public void feedbackInvite(final int status) {
        if (mChatStatus == VideoChatStatus.RECEIVED_INVITE) {

            if (status == LinkConst.STATUS_NOT_AGREE) {
                mChatStatus = VideoChatStatus.UNCHAT; //不同意的情况需要更新当前状态为未连麦状态
            }

            Map<String, String> param = new HashMap<>();
            param.put("token", UserUtils.getToken());
            if (status == LinkConst.STATUS_AGREE) {
                param.put("feedback_result", "true");
            } else if (status == LinkConst.STATUS_NOT_AGREE) {
                param.put("feedback_result", "false");
            }

            GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
                @Override
                public void onSuccess(String responseInfo) {
                    KLog.e(responseInfo);
                    JSONObject object = JSONObject.parseObject(responseInfo);
                    JSONObject data = object.getJSONObject("data");
                    if (object.getIntValue("code") == 0 && data.getBooleanValue("success")) {
                        if (data.getBooleanValue("startmix_result")) {
                            if (data.getBooleanValue("result")) {
                                //缓存推流URL和主播的短延迟播放URL
                                mPushUrl = data.getString("push_stream_url");
                                mSmallDelayPlayUrl = data.getString("short_play_url");
                                //开始连麦
                                startLaunchChat();
                            } else {
                                mChatStatus = VideoChatStatus.UNCHAT;
                            }
                        } else {
                            mChatStatus = VideoChatStatus.UNCHAT;
                            DialogHelp.getMessageDialog(getActivity(), getString(R.string.not_link_live)).setCancelable(false)
                                    .setTitle(R.string.merge_stream_failed).show();
                        }
                    }
                }

                @Override
                public void onFailure(String msg) {
                    UIHelper.toast(getContext(), R.string.net_erro_hint);
                }
            };
            new GGOKHTTP(param, GGOKHTTP.VIDEOCALL_FEEDBACK, ggHttpInterface).startGet();

            //移除倒计时的消息
            mHandler.removeMessages(LinkConst.MSG_WHAT_PROCESS_INVITING_TIMEOUT);
        } else {
            UIHelper.toast(getContext(), R.string.no_inviting_for_response); //当前没有连麦邀请需要反馈
        }
    }

    /**
     * 开始连麦
     */
    private void startLaunchChat() {
        //更新当前连麦状态为开始推流并尝试混流，等待混流成功
        mChatStatus = VideoChatStatus.TRY_MIX;

        changePlayViewToChatMode();

        //注意： 这里推流输出视频尺寸必须是360 * 640
        mChatParter.onlineChat(mPushUrl, 360, 640,
                mPreviewSurfaceView.getHolder().getSurface(), mMediaParam, mSmallDelayPlayUrl);

    }

    /**
     * UI层从普通播放模式更改到连麦模式
     */
    private void changePlayViewToChatMode() {
        if (mPlaySurfaceView != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mPlaySurfaceView.getLayoutParams();
            final Resources resources = getResources();
            if (layoutParams == null) {
                layoutParams = new FrameLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.chat_play_window_width),
                        resources.getDimensionPixelSize(R.dimen.chat_play_window_height));
            } else {
                layoutParams.width = resources.getDimensionPixelSize(R.dimen.chat_play_window_width);
                layoutParams.height = resources.getDimensionPixelSize(R.dimen.chat_play_window_height);
            }

            layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            layoutParams.rightMargin = resources.getDimensionPixelSize(R.dimen.chat_play_window_right_margin);
            layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.chat_play_window_bottom_margin);
            mPlaySurfaceView.setLayoutParams(layoutParams);
        }

        mIvChatClose.setVisibility(View.VISIBLE);
        mPreviewSurfaceView.setVisibility(View.VISIBLE);
        mBottomFragment.showCameraView();
    }

    /**
     * 显示连麦关闭的通知Dialog
     */
    private void showChatCloseNotifyUI() {
        if (mChatCloseConfirmDialog != null && mChatCloseConfirmDialog.isShowing()) {
            mChatCloseConfirmDialog.dismiss();
        }

        DialogHelp.getMessageDialog(getActivity(), getString(R.string.close_video_call_notify_message))
                .setCancelable(false).setTitle(R.string.close_video_call_title).show();
    }

    /**
     * 显示主播结束直播
     */
    private void showLiveCloseUI() {
        if (mChatCloseConfirmDialog != null && mChatCloseConfirmDialog.isShowing()) {
            mChatCloseConfirmDialog.dismiss();
        }

        if (mLiveCloseDialog == null) {
            mLiveCloseDialog = LiveCloseDialog.newInstance(getString(R.string.live_finished));
        }

        if (!mLiveCloseDialog.isShow()) {
            mLiveCloseDialog.show(getSupportFragmentManager(), LiveCloseDialog.TAG);
        }
    }

    /**
     * 显示在线观众头像适配器
     */
    class LiveOnlineAdapter extends CommonAdapter<LiveOnlinePersonData, BaseViewHolder> {

        public LiveOnlineAdapter(List<LiveOnlinePersonData> datas) {
            super(R.layout.item_live_online_person, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, LiveOnlinePersonData personList, int position) {
            ImageView viewGrade = holder.getView(R.id.viewGrade);
            if (position == 0) {
                viewGrade.setVisibility(View.VISIBLE);
                viewGrade.setBackground(getResDrawable(R.mipmap.live_person_top1));
            } else if (position == 1) {
                viewGrade.setVisibility(View.VISIBLE);
                viewGrade.setBackground(getResDrawable(R.mipmap.live_person_top2));
            } else if (position == 2) {
                viewGrade.setVisibility(View.VISIBLE);
                viewGrade.setBackground(getResDrawable(R.mipmap.live_person_top3));
            } else {
                viewGrade.setVisibility(View.GONE);
            }

            RoundedImageView imgAvatar = holder.getView(R.id.imgAvatar);
            ImageDisplay.loadCircleImage(getContext(), personList.getAvatar(), imgAvatar);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (AppDevice.isLandscape(getContext())) {
                mBottomFragment.showChatView();
                recyler_chat.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (AppDevice.isPortrait(getContext())) {
                isPlaying = false;
                onBackPressed();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private WatchLiveActivity getContext() {
        return WatchLiveActivity.this;
    }
}
