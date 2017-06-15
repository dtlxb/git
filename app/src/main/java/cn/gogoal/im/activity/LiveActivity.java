package cn.gogoal.im.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.livecloud.live.AlivcMediaFormat;
import com.alivc.player.MediaPlayer;
import com.alivc.publisher.IMediaPublisher;
import com.alivc.publisher.MediaConstants;
import com.alivc.publisher.MediaError;
import com.alivc.videochat.AlivcVideoChatHost;
import com.alivc.videochat.IVideoChatHost;
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
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.LiveOnlinePersonData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVIMClientManager;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.PlayerUtils.CountDownTimerView;
import cn.gogoal.im.common.PlayerUtils.MyDownTimer;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.linkUtils.ConnectivityMonitor;
import cn.gogoal.im.common.linkUtils.HeadsetMonitor;
import cn.gogoal.im.common.linkUtils.LinkConst;
import cn.gogoal.im.common.linkUtils.VideoChatStatus;
import cn.gogoal.im.fragment.WatchBottomFragment;
import cn.gogoal.im.ui.dialog.WaitDialog;

/*
* 推流直播页面
* */
public class LiveActivity extends BaseActivity {

    @BindView(R.id.root_container)
    FrameLayout mRootContainer;

    //邀请连麦
    @BindView(R.id.liveTogether)
    TextView liveTogether;
    //推流预览的SurfaceView
    @BindView(R.id.surfaceLive)
    SurfaceView mPreviewSurfaceView;
    //连麦显示
    @BindView(R.id.parter_view_container)
    LinearLayout mParterViewContainer;
    //连麦关闭按钮
    @BindView(R.id.imgPlayClose)
    ImageView imgPlayClose;

    //详情相关控件
    @BindView(R.id.imgPalyer)
    RoundedImageView imgPalyer;
    @BindView(R.id.textCompany)
    TextView textCompany;
    @BindView(R.id.textOnlineNumber)
    TextView textOnlineNumber;
    @BindView(R.id.recyAudience)
    RecyclerView recyAudience;
    //倒计时
    @BindView(R.id.countDownTimer)
    CountDownTimerView countDownTimer;
    //聊天显示列表
    @BindView(R.id.recycPortrait)
    RecyclerView recyler_chat;
    //直播开始倒数
    @BindView(R.id.textCount)
    TextView textCount;

    /**
     * 如果正在连麦的时候有新的连麦邀请过来，则在同意连麦的情况下会主动关闭之前的连麦
     */
    private boolean mIsLive = false;                //直播是否处在正在推流的状态
    private boolean isBeautyOn = true;              //美颜是否开启
    private boolean isFlashOn = false;              //闪光灯是否开启
    private boolean mIsPublishPaused = false;       //推流是否处于暂停的状态
    private boolean mHasPermission = false;         //是否有权限
    private boolean mIsRequestCloseChat = false;    //是否主动请求关闭连麦

    /*
    * 直播所需定义参数
    * */
    enum SurfaceStatus {
        UNINITED, CREATED, CHANGED, DESTROYED, RECREATED  //surface状态
    }

    private SurfaceStatus mPreviewSurfaceStatus = SurfaceStatus.UNINITED;
    private SurfaceStatus mPlayerSurfaceStatus = SurfaceStatus.UNINITED;

    private AlivcVideoChatHost mChatHost;
    private Map<String, String> mMediaParam = new HashMap<>(); //推流器参数
    private int mCameraFacing = AlivcMediaFormat.CAMERA_FACING_FRONT;
    private Map mFilterMap = new HashMap<>(); //滤镜参数（目前只有美颜一个滤镜）

    private GestureDetector mDetector; //对焦
    private ScaleGestureDetector mScaleDetector; //缩放

    private ConnectivityMonitor mConnectivityMonitor = new ConnectivityMonitor();
    private HeadsetMonitor mHeadsetMonitor = new HeadsetMonitor();

    private int mPreviewWidth = 0;
    private int mPreviewHeight = 0;

    private SurfaceView mPlaySurfaceView; //连麦播放的小窗SurfaceView
    private VideoChatStatus mVideoChatStatus = VideoChatStatus.UNCHAT; //未连麦的状态

    private String publishUrl; //推流播放网址
    private String mSmallDelayPlayUrl; //连麦延迟播放网址

    //聊天对象
    private List<AVIMMessage> messageList = new ArrayList<>();
    private LiveChatAdapter mLiveChatAdapter;
    private AVIMConversation imConversation;
    //在线人头像显示
    private List<LiveOnlinePersonData> audienceList = new ArrayList<>();
    private LiveOnlineAdapter audienceAdapter;

    private String appoint_account;

    private String room_id;

    private String live_id;

    private String live_type;

    //弹窗
    private int screenHeight;
    private JSONObject anchor;

    private WatchBottomFragment mBottomFragment;
    //倒数数
    private MyDownTimer downTimer;
    //连麦者
    private ContactBean mContactBean;

    private AlertDialog mChatCloseConfirmDialog;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LinkConst.MSG_WHAT_INVITE_CHAT_TIMEOUT://连麦响应超时
                    if (mVideoChatStatus == VideoChatStatus.INVITE_FOR_RES) {
                        mVideoChatStatus = VideoChatStatus.UNCHAT;
                        UIHelper.toast(getContext(), R.string.invite_timeout_tip); //提醒：对方长时间未响应，已取消连麦邀请
                        //liveTogether.setEnabled(true);
                    }
                    break;
                case LinkConst.MSG_WHAT_PROCESS_INVITING_TIMEOUT:
                    //feedbackInviting(false);  //自动反馈不同意连麦
                    UIHelper.toast(getContext(), R.string.inviting_process_timeout); //提醒超时未处理，已经自动拒绝对方的连麦邀请
                    break;
                case LinkConst.MSG_WHAT_MIX_STREAM_TIMEOUT:
                case LinkConst.MSG_WHAT_MIX_STREAM_ERROR:
                    //直接结束连麦
                    closeLiveChat();
                    UIHelper.toast(getContext(), R.string.mix_stream_timeout_tip); //提示等待混流超时
            }
        }
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_live;
    }

    @Override
    public void doBusiness(Context mContext) {

        screenHeight = AppDevice.getHeight(getContext());

        live_id = getIntent().getStringExtra("live_id");

        mPreviewSurfaceView.getHolder().addCallback(mPreviewCallback);
        mPreviewSurfaceView.setOnTouchListener(mOnTouchListener);

        //对焦，缩放
        mDetector = new GestureDetector(this, mGestureDetector);
        mScaleDetector = new ScaleGestureDetector(this, mScaleGestureListener);

        initRecorder();

        mHeadsetMonitor.setHeadsetStatusChangedListener(new LivePresenter());

        initRecycleView(recyler_chat, null);
        mLiveChatAdapter = new LiveChatAdapter(R.layout.item_live_chat, messageList);
        recyler_chat.setAdapter(mLiveChatAdapter);

        initHorizontalRecycleView(recyAudience);
        audienceAdapter = new LiveOnlineAdapter(audienceList);
        recyAudience.setAdapter(audienceAdapter);

        getPlayerInfo();

        downTimer();
    }

    /**
     * 初始化水平方向的列表
     */
    private void initHorizontalRecycleView(RecyclerView rvHorizontal) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHorizontal.setLayoutManager(layoutManager);
    }

    // 初始化计时器
    private void downTimer() {

        downTimer = new MyDownTimer(4, new MyDownTimer.Runner() {
            @Override
            public void run(long sec) {
                textCount.setVisibility(View.VISIBLE);
                textCount.setText("" + sec);
            }

            @Override
            public void finish() {
                textCount.setVisibility(View.GONE);

                if (mChatHost != null) {
                    mIsLive = true;
                    mChatHost.startToPublish(publishUrl);
                }
            }
        });
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
                    UserUtils.getChatGroup(conversation.getMembers(), conversation.getConversationId(), new UserUtils.SquareInfoCallback() {
                        @Override
                        public void squareGetSuccess(JSONObject object) {
                            KLog.e(object.toJSONString());
                            List<LiveOnlinePersonData> accountList = JSONArray.parseArray(String.valueOf(object.getJSONArray("accountList")), LiveOnlinePersonData.class);
                            if (accountList != null) {
                                for (int i = 0; i < accountList.size(); i++) {
                                    if (accountList.get(i).getFriend_id().equals(appoint_account)) {
                                        accountList.remove(i);
                                    }
                                }
                                audienceList.clear();
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

    private class LiveChatAdapter extends CommonAdapter<AVIMMessage, BaseViewHolder> {

        LiveChatAdapter(int layoutId, List<AVIMMessage> datas) {
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

    private WatchBottomFragment.RecorderUIClickListener mUIClickListener = new WatchBottomFragment.RecorderUIClickListener() {

        @Override
        public void onOpenComment() {
            if (imConversation != null) {
                mBottomFragment.showCommentEditUI();
            } else {
                WaitDialog dialog = WaitDialog.getInstance("初始化聊天失败", R.mipmap.login_error, false);
                dialog.show(getSupportFragmentManager());
                dialog.dismiss(false);
            }
        }

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
                params.put("chat_type", "1003");
                params.put("message", JSONObject.toJSON(messageMap).toString());

                //发送文字消息
                GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
                    @Override
                    public void onSuccess(String responseInfo) {
                        KLog.json(responseInfo);
                        player_edit.setText("");
                        mBottomFragment.hideCommentEditUI();

                        /*player_edit.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
        }

        @Override
        public int onSwitchCamera() {
            if (mChatHost != null) {
                mChatHost.switchCamera();
            }
            return -1;
        }

        @Override
        public void onFinish() {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case DialogInterface.BUTTON_POSITIVE: //确定
                            //结束直播
                            closeLiveRoom();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE: //取消
                            break;
                    }
                }
            };
            DialogHelp.getConfirmDialog(getContext(), getString(R.string.close_live_call_confirm_message),
                    listener).setTitle(getString(R.string.close_live_call_title))
                    .setCancelable(false).show();
        }

    };

    /*
    * 结束直播推流
    * */
    private void closeLiveRoom() {

        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("live_id", live_id);
        param.put("live_type", live_type);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                JSONObject data = object.getJSONObject("data");
                if (object.getIntValue("code") == 0 && data.getIntValue("code") == 1) {
                    onBackPressed();
                } else {
                    UIHelper.toast(getContext(), R.string.close_live_failed);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.VIDEOCALL_CLOSE_LIVE, ggHttpInterface).startGet();
    }

    SurfaceHolder.Callback mPreviewCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(final SurfaceHolder holder) {
            KLog.d("LiveActivity-->Preview surface created");
            //记录Surface的状态
            if (mPreviewSurfaceStatus == SurfaceStatus.UNINITED) {
                mPreviewSurfaceStatus = SurfaceStatus.CREATED;
                startPreView(holder);
            } else if (mPreviewSurfaceStatus == SurfaceStatus.DESTROYED) {
                mPreviewSurfaceStatus = SurfaceStatus.RECREATED;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            KLog.d("LiveActivity-->Preview surface changed");
            mPreviewSurfaceStatus = SurfaceStatus.CHANGED;
            mPreviewWidth = width;
            mPreviewHeight = height;

            if ((mPlayerSurfaceStatus == SurfaceStatus.CHANGED || mPlaySurfaceView == null) && mPreviewSurfaceStatus == SurfaceStatus.CHANGED) {
                resumePublishStream(mPreviewSurfaceView, mPlaySurfaceView);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            KLog.d("LiveActivity-->Preview surface destroyed");
            mPreviewSurfaceStatus = SurfaceStatus.DESTROYED;
        }
    };

    /**
     * 开启预览
     */
    public void startPreView(final SurfaceHolder holder) {
        //开启预览
        KLog.d("LiveActivity-->mChatHost.prepareToPublish()");
        mChatHost.prepareToPublish(holder.getSurface(), 360, 640, mMediaParam);
        if (mCameraFacing == AlivcMediaFormat.CAMERA_FACING_FRONT) {
            mChatHost.setFilterParam(mFilterMap);
        }
    }

    /**
     * 调用结束连麦的REST API
     */
    public void closeLiveChat() {
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
                    abortChat(true);
                } else {
                    //显示结束连麦失败的
                    KLog.e("Close chatting failed");
                    UIHelper.toast(getContext(), R.string.close_chat_failed_for_new_chat);
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
     * 终止连麦
     */
    public void abortChat(boolean isShowUI) {
        if (mChatHost != null && isChatting()) {
            KLog.d("LiveActivity-->mChatHost.abortChat()");
            mChatHost.abortChat();
            mVideoChatStatus = VideoChatStatus.UNCHAT;
            if (isShowUI) {
                showAbortChatUI();
            }
        }
    }

    /**
     * 是否正在进行连麦
     */
    public boolean isChatting() {
        return mVideoChatStatus == VideoChatStatus.MIX_SUCC || mVideoChatStatus == VideoChatStatus.TRY_MIX;
    }

    /**
     * 结束连麦播放,并且隐藏小窗的UI
     * 注意：因为下一次再连麦播放时需要一个新的SurfaceView，因此，这里需要将当前的playSurfaceView与mPlayCallback解绑，
     * 并且将当前的playSurfaceView从mParterViewContainer移除
     */
    public void showAbortChatUI() {
        if (mPlaySurfaceView != null) {
            mParterViewContainer.removeAllViews();
            mPlaySurfaceView.getHolder().removeCallback(mPlayCallback);
            mPlaySurfaceView = null;
        }
        imgPlayClose.setVisibility(View.GONE);
        mParterViewContainer.setVisibility(View.GONE);
        //liveTogether.setEnabled(true);
    }

    private SurfaceHolder.Callback mPlayCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            KLog.e("host player surface create.");
            if (mPlayerSurfaceStatus == SurfaceStatus.UNINITED) {
                mPlayerSurfaceStatus = SurfaceStatus.CREATED;
                launchChat(mPlaySurfaceView);
            } else if (mPlayerSurfaceStatus == SurfaceStatus.DESTROYED) {
                mPlayerSurfaceStatus = SurfaceStatus.RECREATED;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            KLog.e("host player surface changed.");
            mPlayerSurfaceStatus = SurfaceStatus.CHANGED;
            // 此处有两种情况:
            // 1.首次创建此surface,那么launchChat
            // 2.非首次,那么resume
            if (mPlayerSurfaceStatus == SurfaceStatus.CHANGED && mPreviewSurfaceStatus == SurfaceStatus.CHANGED)
                resumePublishStream(mPreviewSurfaceView, mPlaySurfaceView);

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            KLog.e("host player surface destroy.");
            mPlayerSurfaceStatus = SurfaceStatus.DESTROYED;
        }
    };

    /**
     * 进行连麦播放
     */
    public void launchChat(SurfaceView playSurfaceView) {
        if (mSmallDelayPlayUrl != null && isChatting()) {
            KLog.d("LiveActivity-->mChatHost.launchChat()");
            mChatHost.launchChat(mSmallDelayPlayUrl, playSurfaceView);
        }
    }

    /**
     * 恢复推流/连麦
     */
    public void resumePublishStream(SurfaceView mPreviewSurf, SurfaceView mPlaySurf) {
        if (mChatHost != null && mIsPublishPaused) {
            KLog.d("LiveActivity --> mChatHost.resume()");
            mChatHost.resume(mPreviewSurf, mPlaySurf);
            mIsPublishPaused = false;
        }
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mDetector.onTouchEvent(motionEvent);
            mScaleDetector.onTouchEvent(motionEvent);
            return true;
        }
    };

    /**
     * 自动对焦
     */
    private GestureDetector.OnGestureListener mGestureDetector = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (mPreviewWidth > 0 && mPreviewHeight > 0) {
                float x = motionEvent.getX() / mPreviewWidth;
                float y = motionEvent.getY() / mPreviewHeight;
                if (mChatHost != null) {
                    mChatHost.focusCameraAtAdjustedPoint(x, y);
                }
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
    };

    /**
     * 摄像头缩放
     */
    private ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (mChatHost != null) {
                mChatHost.zoomCamera(scaleGestureDetector.getScaleFactor());
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }
    };

    /**
     * 初始化推流器
     */
    private void initRecorder() {
        //设置推流器推流相关参数
        mMediaParam.put(MediaConstants.PUBLISHER_PARAM_ORIGINAL_BITRATE, "" + 800000);        //初始码率
        mMediaParam.put(MediaConstants.PUBLISHER_PARAM_MIN_BITRATE, "" + 600000);        //最小码率
        mMediaParam.put(MediaConstants.PUBLISHER_PARAM_MAX_BITRATE, "" + 1000000);        //最大码率

        mChatHost = new AlivcVideoChatHost();
        KLog.e("LiveActivity--> mChatHost.init()");
        mChatHost.init(this);
        mChatHost.setHostViewScalingMode(IMediaPublisher.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        mChatHost.setParterViewScalingMode(MediaPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

        //设置错误信息回调
        mChatHost.setErrorListener(mOnErrorListener);

        //设置状态信息回调
        mChatHost.setInfoListener(mInfoListener);

        //设置美颜开启
        mFilterMap.put(AlivcVideoChatHost.ALIVC_FILTER_PARAM_BEAUTY_ON, Boolean.toString(true));
        mChatHost.setFilterParam(mFilterMap);
    }

    /**
     * 推流器错误回调
     */
    AlivcVideoChatHost.OnErrorListener mOnErrorListener = new IVideoChatHost.OnErrorListener() {
        @Override
        public boolean onError(IVideoChatHost iVideoChatHost, int what, int extra) {
            if (what == 0) {
                return false;
            }
            KLog.d("Live stream connection error-->" + what);
            switch (what) {
                case MediaError.ALIVC_ERR_PUBLISHER_NETWORK_POOR:
                    UIHelper.toast(getContext(), R.string.network_busy);
                    break;
                case MediaError.ALIVC_ERR_PLAYER_NO_NETWORK:
                    UIHelper.toast(getContext(), R.string.no_network);
                    break;
                case MediaError.ALIVC_ERR_PLAYER_READ_PACKET_TIMEOUT:
                    UIHelper.toast(getContext(), R.string.network_busy);
                    break;
                case MediaError.ALIVC_ERR_PLAYER_INVALID_INPUTFILE:
                case MediaError.ALIVC_ERR_PLAYER_NO_MEMORY:
                case MediaError.ALIVC_ERR_PLAYER_INVALID_CODEC:
                case MediaError.ALIVC_ERR_PLAYER_NO_SURFACEVIEW:
                    //超时等状态需要提示连麦结束
                    UIHelper.toast(getContext(), R.string.video_chatting_finished);
                    abortChat(true);
                    break;
                case MediaError.ALIVC_ERR_PLAYER_TIMEOUT:
                    KLog.d("encounter player timeout, so call restartToPlayer");
                    mChatHost.reconnectChat();
                    break;
                case MediaError.ALIVC_ERR_PUBLISHER_AUDIO_CAPTURE_DISABLED://音频采集关闭
                case MediaError.ALIVC_ERR_PUBLISHER_AUDIO_CAPTURE_NO_DATA://音频采集失败
                    if (isChatting()) {
                        closeLiveChat();
                    }
                    if (mIsLive) {
                        closeLive();
                    }
                    break;
                case MediaError.ALIVC_ERR_PUBLISHER_VIDEO_CAPTURE_DISABLED: //摄像头开启失败
                    UIHelper.toast(getContext(), R.string.camera_open_failure_for_live);
                    finish();
                    break;
                case MediaError.ALIVC_ERR_PUBLISHER_ENCODE_AUDIO_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_VIDEO_ENCODER_INIT_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_MALLOC_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_NETWORK_UNCONNECTED:
                case MediaError.ALIVC_ERR_PUBLISHER_ILLEGAL_ARGUMENT:
                default:
                    KLog.e("Live stream connection error-->" + what);
                    break;
            }
            return false;
        }
    };

    /**
     * 调用关闭直播的Rest API
     */
    public void closeLive() {
        //显示关闭直播成功
        finish();
    }

    /**
     * 推流器状态回调
     */
    AlivcVideoChatHost.OnInfoListener mInfoListener = new AlivcVideoChatHost.OnInfoListener() {

        @Override
        public boolean onInfo(IVideoChatHost iVideoChatHost, int what, int extra) {
            KLog.e("LiveActivity --> what = " + what + ", extra = " + extra);
            switch (what) {
                case MediaError.ALIVC_INFO_PUBLISH_NETWORK_GOOD:

                    break;
                case MediaError.ALIVC_INFO_PUBLISH_RECONNECT_FAILURE:
                    UIHelper.toast(getContext(), R.string.network_busy);
                    break;
                case MediaError.ALIVC_INFO_PUBLISH_RECONNECT_START:
                    break;
                case MediaError.ALIVC_INFO_PUBLISH_RECONNECT_SUCCESS:
                    break;
            }
            return false;
        }
    };

    /**
     * 设置对耳机状态的监听，并且通知给SDK
     */
    class LivePresenter implements HeadsetMonitor.HeadSetStatusChangedListener {

        @Override
        public void onHeadsetStatusChanged(boolean on) {
            mChatHost.changeEarPhoneWhenChat(on);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnectivityMonitor.register(this); //注册对网络状态的监听
        mHeadsetMonitor.register(this); //注册对耳机状态的监听

        if (mPreviewSurfaceStatus != SurfaceStatus.DESTROYED) {
            KLog.e("LiveActivity -->chatHost.resume(), onResume");
            if (mPlayerSurfaceStatus != SurfaceStatus.DESTROYED) {
                KLog.e("LiveActivity-->previewSurface and playSurface all is null");
                resumePublishStream(null, mPlaySurfaceView);
            } else {
                //恢复推流
                KLog.e("LiveActivity-->previewSurface is null, playSurface isn't null");
                resumePublishStream(null, mPlaySurfaceView);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConnectivityMonitor.unRegister(this); //取消对网络状态的监听
        mHeadsetMonitor.unRegister(this); //取消对耳机状态的监听

        pausePublishStream();

    }

    /**
     * 暂停推流 or 连麦
     */
    public void pausePublishStream() {
        if (mChatHost != null && !mIsPublishPaused) {
            KLog.e("LiveActivity-->mChatHost.pause()");
            //暂停推流
            mChatHost.pause();
            mIsPublishPaused = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        onLiveDestroy();
    }

    public void onLiveDestroy() {
        releaseRecorder();      //释放推流器资源

        if (null != imConversation) {
            quiteSquare(imConversation);
        }
    }

    /**
     * 释放推流器资源
     */
    public void releaseRecorder() {
        stopPublish();
        if (isChatting()) {
            abortChat(false);
            closeLiveChat();
        }
        if (mChatHost != null) {
            KLog.e("LiveActivity -->mChatHost.release()");
            mChatHost.release();
            mChatHost = null;
        }
    }

    /**
     * 停止推流
     */
    public void stopPublish() {
        if (null != mChatHost) {
            KLog.e("LiveActivity-->mChatHost.stopPublishing()");
            mChatHost.stopPublishing();
            KLog.e("LiveActivity-->mChatHost.finishPublishing()");
            mChatHost.finishPublishing();
            mIsLive = false;
        }
    }

    @OnClick({R.id.liveTogether, R.id.imgPlayClose})
    public void closeOnClick(View v) {
        switch (v.getId()) {
            case R.id.liveTogether: //邀请连麦
                if (imConversation != null) {
                    imConversation.fetchInfoInBackground(new AVIMConversationCallback() {
                        @Override
                        public void done(AVIMException e) {
                            //拉取群通讯录并缓存本地
                            getChatGroupInfo(imConversation);
                        }
                    });
                } else {

                }
                break;
            case R.id.imgPlayClose: //关闭连麦
                showChatCloseConfirmDialog();
                break;
        }
    }

    /**
     * 显示确认结束连麦的dialog
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
                            abortChat(true); //停止客户端连麦拉流播放
                            closeLiveChat(); //通知APP Server结束连麦，调用REST API
                            //liveTogether.setEnabled(true);
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
     * 关闭直播推流
     */
    public void onBackPressed() {
        if (isChatting()) {
            closeLiveChat();
            abortChat(false);
        }

        if (mIsLive) {
            stopPublish();
            closeLive();
        }

        finish();
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

                    live_type = data.getString("live_type");

                    //主播介绍
                    anchor = data.getJSONObject("anchor");

                    //倒计时
                    if (data.getLongValue("launch_time") > 0) {
                        countDownTimer.setVisibility(View.VISIBLE);
                        countDownTimer.addTime(data.getString("live_time_start"));
                        countDownTimer.start();

                        countDownTimer.setDownCallBack(new CountDownTimerView.CountDownCallBack() {
                            @Override
                            public void startPlayer() {
                                countDownTimer.setVisibility(View.GONE);
                                downTimer.start();
                            }
                        });
                    } else {
                        countDownTimer.setVisibility(View.GONE);

                        downTimer.start();
                    }

                    publishUrl = data.getString("stream_url");
                    room_id = data.getString("room_id");

                    AVIMClientManager.getInstance().findConversationById(room_id, new AVIMClientManager.ChatJoinManager() {
                        @Override
                        public void joinSuccess(AVIMConversation conversation) {
                            imConversation = conversation;
                            joinSquare(imConversation);
                        }

                        @Override
                        public void joinFail(String error) {
                        }
                    });

                    getOnlineCount(room_id);

                    JSONObject introduce = new JSONObject();
                    introduce.put("screenHeight", screenHeight);
                    introduce.put("live_id", live_id);
                    introduce.put("anchor", anchor);
                    introduce.put("introduction_img", data.getString("introduction_img"));
                    introduce.put("introduction", data.getString("introduction"));
                    introduce.put("live_large_img", data.getString("live_large_img"));

                    mBottomFragment = WatchBottomFragment.newInstance(String.valueOf(introduce));
                    mBottomFragment.setRecordUIClickListener(mUIClickListener);
                    mBottomFragment.setActivityRootView(mRootContainer);
                    mBottomFragment.setType(0);
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
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_ONLINE_COUNT, ggHttpInterface).startGet();
    }


    //获取群通讯录
    private void getChatGroupInfo(final AVIMConversation conversation) {
        UserUtils.getChatGroup(conversation.getMembers(), conversation.getConversationId(), new UserUtils.SquareInfoCallback() {
            @Override
            public void squareGetSuccess(JSONObject object) {
                KLog.e(object.toJSONString());
                JSONArray accountList = object.getJSONArray("accountList");
                if (accountList.size() >= 2) {
                    Intent intent = new Intent(getContext(), ChooseContactActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("square_action", AppConst.LIVE_CONTACT_SOMEBODY);
                    bundle.putString("conversation_id", conversation.getConversationId());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, AppConst.LIVE_CONTACT_SOMEBODY);
                } else {
                    UIHelper.toast(getContext(), "暂时没有观众观看");
                }
            }

            @Override
            public void squareGetFail(String error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == AppConst.LIVE_CONTACT_SOMEBODY) {
                mContactBean = (ContactBean) data.getSerializableExtra("choose_connect_livebean");
                KLog.e(mContactBean);
                if (mVideoChatStatus == VideoChatStatus.UNCHAT) {
                    sendLiveInvite(String.valueOf(mContactBean.getFriend_id()));
                    mVideoChatStatus = VideoChatStatus.INVITE_FOR_RES;
                } else {
                    UIHelper.toast(getContext(), R.string.not_allow_repeat_call);
                }

                //liveTogether.setEnabled(false);
            }
        }
    }

    /*
    * 直播邀请连麦
    * */
    private void sendLiveInvite(String invitee_id) {

        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("invitee_id", invitee_id);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                JSONObject data = object.getJSONObject("data");
                if (object.getIntValue("code") == 0 && data.getBooleanValue("success")) {
                    //倒计时，10s后未收到回复，自动认为对方决绝。
                    //mHandler.sendEmptyMessageDelayed(LinkConst.MSG_WHAT_INVITE_CHAT_TIMEOUT, LinkConst.INVITE_CHAT_TIMEOUT_DELAY);
                    mVideoChatStatus = VideoChatStatus.INVITE_FOR_RES;
                    UIHelper.toast(getContext(), R.string.invite_succeed);
                    //liveTogether.setEnabled(false);
                } else {
                    UIHelper.toast(getContext(), R.string.invite_failed);
                    mVideoChatStatus = VideoChatStatus.UNCHAT;
                    //liveTogether.setEnabled(true);
                }

            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.VIDEOCALL_INVITE, ggHttpInterface).startGet();
    }

    /**
     * Live消息接收
     */
    @Subscriber(tag = "Live_Message")
    public void handleMessage(BaseMessage baseMessage) {
        if (null != imConversation && null != baseMessage) {
            Map<String, Object> map = baseMessage.getOthers();
            AVIMMessage message = (AVIMMessage) map.get("message");
            AVIMConversation conversation = (AVIMConversation) map.get("conversation");

            JSONObject contentObject = JSON.parseObject(message.getContent());
            int _lctype = contentObject.getIntValue("_lctype");
            KLog.e(message.getContent());
            int chatType = (int) conversation.getAttribute("chat_type");

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

            } else if (chatType == 1008) {
                //连麦动作处理
                JSONObject content = JSONObject.parseObject(message.getContent());
                JSONObject lcattrs = content.getJSONObject("_lcattrs");

                switch (lcattrs.getString("code")) {
                    case "feedback":
                        boolean feedback_result = lcattrs.getBooleanValue("feedback_result");
                        boolean startmix_result = lcattrs.getBooleanValue("startmix_result");
                        if (startmix_result) {
                            feedbackInviteResult(feedback_result);
                        } else {
                            DialogHelp.getMessageDialog(getActivity(), getString(R.string.not_link_live)).setCancelable(false)
                                    .setTitle(R.string.merge_stream_failed).show();

                            if (mVideoChatStatus == VideoChatStatus.INVITE_FOR_RES) {
                                mVideoChatStatus = VideoChatStatus.UNCHAT;
                            }
                            //liveTogether.setEnabled(true);
                        }
                        break;
                    case "mixresult":
                        //混流成功
                        mSmallDelayPlayUrl = lcattrs.getString("short_play_url");
                        getMergeStreamSuccess();
                        break;
                    case "close":
                        //对方关闭连麦
                        if (mIsRequestCloseChat) { //主动请求关闭连麦的情况下，需要忽略再次收到的连麦关闭消息
                            mIsRequestCloseChat = false;
                            return;
                        }

                        if (isChatting()) {
                            abortChat(true);
                            showChatCloseNotifyDialog();
                        }
                        break;
                }
            }
        }
    }

    /**
     * 邀请连麦反馈
     */
    private void feedbackInviteResult(boolean feedback_result) {
        //移除邀请等待响应超时倒计时的消息
        mHandler.removeMessages(LinkConst.MSG_WHAT_INVITE_CHAT_TIMEOUT);
        if (feedback_result) {
            //同意连麦
            //如果当前是已经发送邀请，等待对方反馈的状态，则处理这个消息，否则视为无效的消息，不作处理
            if (mVideoChatStatus == VideoChatStatus.INVITE_FOR_RES) {
                mVideoChatStatus = VideoChatStatus.TRY_MIX;
            }
        } else {
            //不同意连麦
            //如果当前是已经发送邀请，等待对方反馈的状态，则处理这个消息，否则视为无效的消息，不作处理
            if (mVideoChatStatus == VideoChatStatus.INVITE_FOR_RES) {
                mVideoChatStatus = VideoChatStatus.UNCHAT;
            }
        }
        showFeedbackResultDialog(feedback_result);
    }

    /**
     * 邀请连麦结果显示
     */
    private void showFeedbackResultDialog(boolean isAgree) {
        String message = null;
        if (isAgree) {
            message = getString(R.string.agree_message);
        } else {
            //liveTogether.setEnabled(true);
            message = getString(R.string.not_agree_message);
        }

        DialogHelp.getMessageDialog(getActivity(), message).setCancelable(false)
                .setTitle(R.string.title_video_call_response).show();
    }

    /**
     * 混流成功处理
     */
    private void getMergeStreamSuccess() {
        //如果当前是开始混流并且等待混流成功的状态，则处理这条消息，否则视为无效的消息，不作处理
        if (mVideoChatStatus == VideoChatStatus.TRY_MIX) {
            mVideoChatStatus = VideoChatStatus.MIX_SUCC;
            showLaunchChatUI();
        }
    }

    /**
     * 进行连麦播放（小窗播放连麦对方的短延时播放地址）
     * * 这里需要新创建一个SurfaceView，并且绑定mPlayCallback
     * * 注意：播放时每次都需要一个新的SurfaceView，让原来的SurfaceView解绑mPlayCallback,
     * 然后绑定到新创建的SurfaceView
     */
    private void showLaunchChatUI() {
        mParterViewContainer.setBackgroundColor(Color.rgb(0, 0, 0));
        mPlaySurfaceView = new SurfaceView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mPlaySurfaceView.setLayoutParams(params);
        mParterViewContainer.removeAllViews();
        mParterViewContainer.addView(mPlaySurfaceView);
        mPlaySurfaceView.setZOrderMediaOverlay(true);
        mPlayerSurfaceStatus = SurfaceStatus.UNINITED;
        mPlaySurfaceView.getHolder().addCallback(mPlayCallback);

        mParterViewContainer.setVisibility(View.VISIBLE);
        imgPlayClose.setVisibility(View.VISIBLE);
    }

    /**
     * 显示连麦关闭的通知Dialog
     */
    private void showChatCloseNotifyDialog() {
        if (mChatCloseConfirmDialog != null && mChatCloseConfirmDialog.isShowing()) {
            mChatCloseConfirmDialog.dismiss();
        }

        DialogHelp.getMessageDialog(getActivity(), getString(R.string.close_video_call_notify_message))
                .setCancelable(false).setTitle(R.string.close_video_call_title).show();
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

    private LiveActivity getContext() {
        return LiveActivity.this;
    }
}
