package cn.gogoal.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.ChooseContactActivity;
import cn.gogoal.im.adapter.IMChatAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.EmojiBean;
import cn.gogoal.im.bean.FoundData;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.bean.UserBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.AsyncTaskUtil;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVIMClientManager;
import cn.gogoal.im.common.IMHelpers.GGAudioMessage;
import cn.gogoal.im.common.IMHelpers.GGGroupAddMessage;
import cn.gogoal.im.common.IMHelpers.GGGroupDelMessage;
import cn.gogoal.im.common.IMHelpers.GGImageMessage;
import cn.gogoal.im.common.IMHelpers.GGShareMessage;
import cn.gogoal.im.common.IMHelpers.GGStockMessage;
import cn.gogoal.im.common.IMHelpers.GGSystemMessage;
import cn.gogoal.im.common.IMHelpers.GGTextMessage;
import cn.gogoal.im.common.IMHelpers.GGUnReadMessage;
import cn.gogoal.im.common.IMHelpers.MessageListUtils;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UFileUpload;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.recording.MediaManager;
import cn.gogoal.im.ui.KeyboardLaunchLinearLayout;
import cn.gogoal.im.ui.view.SwitchImageView;
import cn.gogoal.im.ui.view.VoiceButton;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by huangxx on 2017/2/21.
 */

public class ChatFragment extends BaseFragment {

    @BindView(R.id.message_swipe)
    SwipeRefreshLayout message_swipe;

    @BindView(R.id.message_list)
    RecyclerView message_recycler;

    @BindView(R.id.find_more_layout)
    RelativeLayout find_more_layout;

    @BindView(R.id.img_voice)
    SwitchImageView imgVoice;

    @BindView(R.id.et_input)
    EditText etInput;

    @BindView(R.id.img_emoji)
    CheckBox EmojiCheckBox;

    @BindView(R.id.img_function)
    CheckBox functionCheckBox;

    @BindView(R.id.btn_send)
    Button btnSend;

    @BindView(R.id.voiveView)
    VoiceButton voiceView;

    @BindView(R.id.chat_root_keyboard_layout)
    KeyboardLaunchLinearLayout keyboardLayout;
    private int chatType;

    private AVIMConversation imConversation;
    private List<AVIMMessage> messageList = new ArrayList<>();
    private IMChatAdapter imChatAdapter;
    private List<FoundData.ItemPojos> itemPojosList;
    private UserBean userBean;
    private EmojiFragment emojiFragment;
    private FunctionFragment functionFragment;
    private FragmentTransaction transaction;
    private FragmentManager childManager;
    private int keyBordHeight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childManager = getChildFragmentManager();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_imchat;
    }

    @Override
    public void doBusiness(Context mContext) {

        BaseActivity.iniRefresh(message_swipe);
        BaseActivity.initRecycleView(message_recycler, null);

        imChatAdapter = new IMChatAdapter(getActivity(), messageList);
        message_recycler.setAdapter(imChatAdapter);

        initFragment();

        //多功能消息框
        itemPojosList = new ArrayList<>();
        itemPojosList.add(new FoundData.ItemPojos("照片", R.mipmap.chat_add_photo, "tag"));
        itemPojosList.add(new FoundData.ItemPojos("股票", R.mipmap.chat_add_stock, "tag"));

        keyBordHeight = SPTools.getInt("soft_keybord_height", AppDevice.dp2px(getActivity(), 220));
        setContentHeight(keyBordHeight, find_more_layout);

        keyboardLayout.setOnKeyboardChangeListener(new KeyboardLaunchLinearLayout.OnKeyboardChangeListener() {
            @Override
            public void OnKeyboardPop(int height) {
                SPTools.saveInt("soft_keybord_height", height);
                setContentHeight(height, find_more_layout);
                message_recycler.getLayoutManager().scrollToPosition(imChatAdapter.getItemCount() - 1);
            }

            @Override
            public void OnKeyboardClose() {

            }
        });

        //消息下拉刷新
        message_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (null != messageList && messageList.size() > 0) {
                    imConversation.queryMessages(messageList.get(0).getMessageId(), messageList.get(0).getTimestamp(), 15, new AVIMMessagesQueryCallback() {
                        @Override
                        public void done(List<AVIMMessage> list, AVIMException e) {
                            if (e == null && null != list && list.size() > 0) {
                                messageList.addAll(0, list);
                                imChatAdapter.notifyItemRangeInserted(0, list.size());
                            }
                            message_swipe.setRefreshing(false);
                        }
                    });
                }
            }
        });

        message_recycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //更改键盘弹起效果(下次顶起来)
                InputMethodManager manager = (InputMethodManager) etInput.getContext().getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(etInput.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                initChecked();
                return false;
            }

        });

        //发送文字消息(向公司服务器发送消息，然后后台再处理给LeanCloud发送消息)
        btnSend.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                if (null == imConversation) {
                    UIHelper.toast(getActivity(), R.string.network_busy);
                    etInput.setText("");
                    return;
                }

                //显示自己的文字消息
                GGTextMessage mTextMessage = createTextMessage(etInput.getText().toString());
                refreshRecyclerView(mTextMessage, true);

                //文字消息基本信息
                Map<Object, Object> messageMap = new HashMap<>();
                messageMap.put("_lctype", "-1");
                messageMap.put("_lctext", etInput.getText().toString());
                messageMap.put("_lcattrs", AVIMClientManager.getInstance().userBaseInfo());

                Map<String, String> params = new HashMap<>();
                params.put("token", UserUtils.getToken());
                params.put("conv_id", imConversation.getConversationId());
                params.put("chat_type", String.valueOf(chatType));
                params.put("message", JSONObject.toJSONString(messageMap));

                etInput.setText("");
                SPTools.clearItem(imConversation.getConversationId());
                //发送文字消息
                sendAVIMMessage(params, mTextMessage);

            }
        });

        //语音和文字切换
        imgVoice.setOnSwitchListener(new SwitchImageView.OnSwitchListener() {
            @Override
            public void onSwitch(View view, int state) {
                initInputStatus(state);
            }
        });

        //输入监听
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etInput.getText().toString().trim().equals("")) {
                    functionCheckBox.setVisibility(View.VISIBLE);
                    btnSend.setVisibility(View.INVISIBLE);
                } else if (etInput.getText().toString().trim().endsWith("@") && chatType == AppConst.IM_CHAT_TYPE_SQUARE) {
                    //@过后跳转加人
                    Intent intent = new Intent(getActivity(), ChooseContactActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("conversation_id", imConversation.getConversationId());
                    bundle.putInt("square_action", AppConst.SQUARE_ROOM_AT_SOMEONE);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, AppConst.SQUARE_ROOM_AT_SOMEONE);
                } else {
                    functionCheckBox.setVisibility(View.INVISIBLE);
                    btnSend.setVisibility(View.VISIBLE);
                }
            }
        });

        etInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
                    String backString = StringUtils.StringFilter(etInput.getText().toString());
                    etInput.setText(backString);
                    etInput.setSelection(backString.length());
                } else if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().finish();
                }
                return true;
            }
        });

        //录音完成发送录音
        voiceView.setAudioFinishRecorderListener(new VoiceButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                //上传UFile然后发送公司后台；
                sendVoiceToUCloud(seconds, filePath);
            }
        });

    }

    //生成未读消息
    private GGUnReadMessage createUnReadMessage(String text) {
        Map<String, Object> lcattrsMap = AVIMClientManager.getInstance().userBaseInfo();
        GGUnReadMessage unReadMessage = new GGUnReadMessage();
        unReadMessage.setTimestamp(CalendarUtils.getCurrentTime());
        unReadMessage.setFrom(UserUtils.getMyAccountId());
        unReadMessage.setMessageId(UUID.randomUUID().toString());

        unReadMessage.setText(text);
        unReadMessage.setAttrs(lcattrsMap);

        return unReadMessage;
    }

    //生成草稿消息
    private GGTextMessage createTextMessage(String text) {
        GGTextMessage mTextMessage = new GGTextMessage();
        HashMap<String, Object> attrsMap = new HashMap<>();
        attrsMap.put("username", UserUtils.getNickname());
        attrsMap.put("avatar", UserUtils.getUserAvatar());
        mTextMessage.setAttrs(attrsMap);
        mTextMessage.setTimestamp(CalendarUtils.getCurrentTime());
        mTextMessage.setFrom(UserUtils.getMyAccountId());
        mTextMessage.setMessageSendStatus(AppConst.MESSAGE_SEND_STATUS_SENDING);
        mTextMessage.setText(text);
        return mTextMessage;
    }

    private void initInputStatus(int state) {
        switch (state) {
            case 0:
                voiceView.setVisibility(View.GONE);
                etInput.setVisibility(View.VISIBLE);
                etInput.requestFocus();
                //更改键盘弹起效果(下次顶起来)
                AppDevice.showSoftChangeMethod(etInput);
                find_more_layout.setVisibility(View.GONE);
                imgVoice.setImageResource(R.mipmap.chat_voice);
                break;
            case 1:
                //更改键盘弹起效果(下次不顶)
                AppDevice.hideSoftChangeMethod(etInput);
                voiceView.setVisibility(View.VISIBLE);
                etInput.setVisibility(View.GONE);
                imgVoice.setImageResource(R.mipmap.chat_key_bord);
                initChecked();
                break;
        }
    }

    private void initChecked() {
        find_more_layout.setVisibility(View.GONE);
        if (emojiFragment != null && emojiFragment.isAdded()) {
            transaction.remove(emojiFragment);
        }
        if (functionFragment != null && functionFragment.isAdded()) {
            transaction.remove(functionFragment);
        }
        EmojiCheckBox.setChecked(false);
        functionCheckBox.setChecked(false);
    }

    private void initFragment() {
        functionFragment = new FunctionFragment();
        emojiFragment = new EmojiFragment();
    }

    //输入框内元素点击事件
    @OnClick({R.id.img_function, R.id.img_emoji})
    void chatClick(View view) {
        transaction = childManager.beginTransaction();
        //设置语音输入不见
        voiceView.setVisibility(View.GONE);
        etInput.setVisibility(View.VISIBLE);
        imgVoice.setImageResource(R.mipmap.chat_voice);
        switch (view.getId()) {
            case R.id.img_function:
                find_more_layout.setVisibility(View.VISIBLE);
                if (!functionFragment.isAdded()) {
                    transaction.add(R.id.find_more_layout, functionFragment, "FunctionFragment");
                }

                message_recycler.getLayoutManager().scrollToPosition(imChatAdapter.getItemCount() - 1);

                transaction.show(functionFragment).hide(emojiFragment);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                EmojiCheckBox.setChecked(false);
                checkAction(view);
                break;
            case R.id.img_emoji:
                find_more_layout.setVisibility(View.VISIBLE);
                if (!emojiFragment.isAdded()) {
                    transaction.add(R.id.find_more_layout, emojiFragment, "EmojiFragment");
                }

                message_recycler.getLayoutManager().scrollToPosition(imChatAdapter.getItemCount() - 1);

                transaction.show(emojiFragment).hide(functionFragment);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                functionCheckBox.setChecked(false);
                checkAction(view);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    public void checkAction(View view) {
        if (((CheckBox) view).isChecked()) {
            etInput.clearFocus();
            InputMethodManager manager = (InputMethodManager) etInput.getContext().getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(etInput.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            etInput.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) etInput.getContext().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(etInput, 0);
        }
    }

    public void sendAVIMMessage(final Map<String, String> params, final AVIMMessage message) {

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    JSONObject data = result.getJSONObject("data");
                    if (data.getBoolean("success")) {
                        saveToMessageList(message);
                        messageStatusChange(message, AppConst.MESSAGE_SEND_STATUS_SUCCESS);
                    } else {
                        GGUnReadMessage failMessage = null;
                        messageStatusChange(message, AppConst.MESSAGE_SEND_STATUS_FAIL);
                        switch (data.getInteger("code")) {
                            case AppConst.MESSAGE_SEND_FAIL_PARAMS_LACK:
                                //缺少参数
                                //createUnReadMessage();
                                break;
                            case AppConst.MESSAGE_SEND_FAIL_NOT_FRIEND:
                                failMessage = createUnReadMessage(getResources().getString(R.string.im_message_send_fail));
                                //不是好友
                                break;
                            case AppConst.MESSAGE_SEND_FAIL_NET_ERROR:
                                //网络异常
                                //createUnReadMessage();
                                break;
                            case AppConst.MESSAGE_SEND_FAIL_NOT_MEMBER:
                                //不是群成员
                                //createUnReadMessage();
                                break;
                            case AppConst.MESSAGE_SEND_FAIL_DISCONNECT:
                                //长连接断开
                                //createUnReadMessage();
                                break;
                            case AppConst.MESSAGE_SEND_FAIL_MEMBER_LIMIT:
                                //群成员超限
                                //createUnReadMessage();
                                break;
                            default:
                                //createUnReadMessage();
                                break;
                        }
                        refreshRecyclerView(failMessage, true);
                    }
                } else {
                    messageStatusChange(message, AppConst.MESSAGE_SEND_STATUS_FAIL);
                }
            }

            @Override
            public void onFailure(String msg) {
                messageStatusChange(message, AppConst.MESSAGE_SEND_STATUS_FAIL);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.CHAT_SEND_MESSAGE, ggHttpInterface).startGet();

    }

    private void messageStatusChange(AVIMMessage message, int statusType) {
        //消息状态更改
        if (message instanceof GGTextMessage) {
            ((GGTextMessage) message).setMessageSendStatus(statusType);
        } else if (message instanceof GGAudioMessage) {
            ((GGAudioMessage) message).setMessageSendStatus(statusType);
        } else if (message instanceof GGImageMessage) {
            ((GGImageMessage) message).setMessageSendStatus(statusType);
        } else if (message instanceof GGStockMessage) {
            ((GGStockMessage) message).setMessageSendStatus(statusType);
        }
        imChatAdapter.notifyItemChanged(messageList.size() - 1);
    }

    //缓存消息至消息列表
    private void saveToMessageList(AVIMMessage message) {
        //头像暂时未保存
        IMMessageBean imMessageBean = null;
        if (message instanceof GGAudioMessage) {
            ((GGAudioMessage) message).setListItem(null);
        }
        if (chatType == AppConst.IM_CHAT_TYPE_SINGLE) {
            if (null != userBean) {
                //"0"开始:未读数-对话名字-对方名字-对话头像-最后信息
                KLog.e(userBean);
                imMessageBean = new IMMessageBean(imConversation.getConversationId(), chatType, message.getTimestamp(), "0",
                        null != userBean.getNickname() ? userBean.getNickname() : "",
                        String.valueOf(userBean.getFriend_id()), String.valueOf(userBean.getAvatar()), JSON.toJSONString(message));
            }
        } else if (chatType == AppConst.IM_CHAT_TYPE_SQUARE) {
            //"0"开始:未读数-对话名字-对方名字-对话头像-最后信息(群对象和群头像暂时为空)
            if (message instanceof GGTextMessage) {
                String strMessage = ((GGTextMessage) message).getText();
                if (StringUtils.StringFilter(strMessage, "@*[\\S]*[ \r\n]")) {
                    ((GGTextMessage) message).setText(strMessage.replace(" ", ""));
                }
            }
            imMessageBean = new IMMessageBean(imConversation.getConversationId(), chatType, message.getTimestamp(), "0", imConversation.getName(),
                    "", imConversation.getAttribute("avatar") != null ? (String) imConversation.getAttribute("avatar") : "", JSON.toJSONString(message));
        }
        MessageListUtils.saveMessageInfo(imMessageBean);
    }

    private void sendStockMessage(String stockCode, String stockName) {

        if (null == imConversation) {
            UIHelper.toast(getActivity(), R.string.network_busy);
            return;
        }

        GGStockMessage mStockMessage = new GGStockMessage();
        mStockMessage.setTimestamp(CalendarUtils.getCurrentTime());
        mStockMessage.setMessageSendStatus(AppConst.MESSAGE_SEND_STATUS_SENDING);
        mStockMessage.setFrom(UserUtils.getMyAccountId());

        //添加股票信息
        Map<String, Object> lcattrsMap;
        lcattrsMap = AVIMClientManager.getInstance().userBaseInfo();
        lcattrsMap.put("stockCode", stockCode);
        lcattrsMap.put("stockName", stockName);

        //消息基本信息
        Map<Object, Object> messageMap = new HashMap<>();
        messageMap.put("_lctype", "11");
        messageMap.put("_lctext", "股票消息");
        messageMap.put("_lcattrs", lcattrsMap);

        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", imConversation.getConversationId());
        params.put("chat_type", String.valueOf(chatType));
        params.put("message", JSONObject.toJSONString(messageMap));

        mStockMessage.setText("股票消息");
        mStockMessage.setAttrs(lcattrsMap);
        refreshRecyclerView(mStockMessage, true);

        etInput.setText("");
        //发送股票消息
        sendAVIMMessage(params, mStockMessage);
    }

    private void refreshRecyclerView(AVIMMessage avimMessage, boolean needJump) {
        if (avimMessage != null) {
            imChatAdapter.addItem(avimMessage);
            imChatAdapter.notifyItemInserted(messageList.size() - 1);
            if (needJump) {
                message_recycler.smoothScrollToPosition(messageList.size() - 1);
            }
        }
    }

    private void sendImageToZyyx(final File file) {
        //图片宽高处理
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();

        //封装一个AVFile对象
        HashMap<String, Object> metaData = new HashMap<>();
        metaData.put("width", width);
        metaData.put("height", height);
        AVFile imageFile = new AVFile("imageFile", file.getPath(), metaData);

        //显示自己的图片消息
        HashMap<String, Object> attrsMap = new HashMap<>();
        attrsMap.put("username", UserUtils.getNickname());
        attrsMap.put("avatar", UserUtils.getUserAvatar());
        final GGImageMessage mImageMessage = new GGImageMessage(imageFile);
        mImageMessage.setFrom(UserUtils.getMyAccountId());
        mImageMessage.setAttrs(attrsMap);
        mImageMessage.setMessageSendStatus(AppConst.MESSAGE_SEND_STATUS_SENDING);
        mImageMessage.setTimestamp(CalendarUtils.getCurrentTime());

        refreshRecyclerView(mImageMessage, true);

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

                        if (null == imConversation) {
                            UIHelper.toast(getActivity(), R.string.network_busy);
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
                        params.put("conv_id", imConversation.getConversationId());
                        params.put("chat_type", String.valueOf(chatType));
                        params.put("message", JSONObject.toJSON(messageMap).toString());
                        KLog.e(onlineUri);

                        //发送图片消息
                        sendAVIMMessage(params, mImageMessage);
                    }

                    @Override
                    public void onFailed() {
                        messageStatusChange(mImageMessage, AppConst.MESSAGE_SEND_STATUS_FAIL);
                    }
                });
            }

            @Override
            public void onPostExecute() {

            }
        });
    }

    private void sendVoiceToUCloud(final float seconds, final String voicePath) {
        AsyncTaskUtil.doAsync(new AsyncTaskUtil.AsyncCallBack() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void doInBackground() {

                //自己显示语音消息
                HashMap<String, Object> attrsMap = new HashMap<>();
                attrsMap.put("username", UserUtils.getNickname());
                attrsMap.put("avatar", UserUtils.getUserAvatar());

                //封装一个AVFile对象
                HashMap<String, Object> metaData = new HashMap<>();
                metaData.put("duration", seconds);
                AVFile AudioFile = new AVFile("", voicePath, metaData);

                //页面卡死（消息绑定了holder,导致缓存时卡死）
                final GGAudioMessage mAudioMessage = new GGAudioMessage(AudioFile);

                mAudioMessage.setFrom(UserUtils.getMyAccountId());
                mAudioMessage.setAttrs(attrsMap);
                mAudioMessage.setMessageSendStatus(AppConst.MESSAGE_SEND_STATUS_SENDING);
                mAudioMessage.setTimestamp(CalendarUtils.getCurrentTime());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //此时已在主线程中，可以更新UI了
                        refreshRecyclerView(mAudioMessage, true);
                    }
                });


                UFileUpload.getInstance().upload(new File(voicePath), UFileUpload.Type.AUDIO, new UFileUpload.UploadListener() {
                    @Override
                    public void onUploading(int progress) {

                    }

                    @Override
                    public void onSuccess(String onlineUri) {

                        if (null == imConversation) {
                            UIHelper.toast(getActivity(), R.string.network_busy);
                            return;
                        }

                        //语音消息基本信息
                        Map<Object, Object> messageMap = new HashMap<>();
                        messageMap.put("_lctype", "-3");
                        messageMap.put("_lctext", "");
                        messageMap.put("_lcattrs", AVIMClientManager.getInstance().userBaseInfo());
                        messageMap.put("url", onlineUri);
                        messageMap.put("format", "amr");
                        messageMap.put("duration", String.valueOf(seconds));

                        Map<String, String> params = new HashMap<>();
                        params.put("token", UserUtils.getToken());
                        params.put("conv_id", imConversation.getConversationId());
                        params.put("chat_type", String.valueOf(chatType));
                        params.put("message", JSONObject.toJSON(messageMap).toString());

                        //发送语音消息
                        sendAVIMMessage(params, mAudioMessage);
                    }

                    @Override
                    public void onFailed() {
                        messageStatusChange(mAudioMessage, AppConst.MESSAGE_SEND_STATUS_FAIL);
                    }
                });
            }

            @Override
            public void onPostExecute() {

            }
        });
    }

    private void getHistoryMessage(final boolean needUpdate, List<AVIMMessage> avimMessages) {
        if (null != imConversation) {
            //查询消息处理
            if (null != avimMessages && avimMessages.size() > 0) {
                KLog.e(avimMessages.size());
                messageList.addAll(avimMessages);
                dealSquareMessage();
                imChatAdapter.setChatType(chatType);
                imChatAdapter.notifyDataSetChanged();
                message_recycler.getLayoutManager().scrollToPosition(0);
            } else {
                imConversation.queryMessagesFromCache(15, new AVIMMessagesQueryCallback() {
                    @Override
                    public void done(List<AVIMMessage> list, AVIMException e) {
                        if (null == e && null != list && list.size() > 0) {
                            dealMessages(list, needUpdate);
                        } else {
                            imConversation.queryMessages(15, new AVIMMessagesQueryCallback() {
                                @Override
                                public void done(List<AVIMMessage> list, AVIMException e) {
                                    dealMessages(list, needUpdate);
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    //寻找信息  显示草稿
    private void setCacheMessage() {
        List<IMMessageBean> cacheBeans = DataSupport.where("conversationID = ?",
                imConversation.getConversationId()).find(IMMessageBean.class);
        if (null != cacheBeans && cacheBeans.size() > 0 && null != cacheBeans.get(0).getLastMessage()) {
            AVIMMessage cacheMessage = JSON.parseObject(cacheBeans.get(0).getLastMessage(), AVIMMessage.class);
            String content = cacheMessage.getContent();
            JSONObject contentObject = JSON.parseObject(content);
            String _lctype = contentObject.getString("_lctype");
            if (_lctype.equals(AppConst.IM_MESSAGE_TYPE_TEXT)) {
                String messageText = contentObject.getString("_lctext");
                if (messageText.startsWith("[草稿] ")) {
                    String text = messageText.substring(5, messageText.length());
                    etInput.setText(text);
                    etInput.setSelection(messageText.substring(5, messageText.length()).length());
                    AppDevice.showSoftChangeMethod(etInput);
                }
            }
        }
    }

    private void dealMessages(List<AVIMMessage> list, boolean needUpdate) {
        messageList.addAll(list);
        dealSquareMessage();
        setCacheMessage();
        //单聊，群聊处理(没发消息的时候不保存)
        if (messageList.size() > 0 && needUpdate) {
            saveToMessageList(messageList.get(messageList.size() - 1));
        }
        imChatAdapter.setChatType(chatType);
        imChatAdapter.notifyDataSetChanged();
        message_recycler.getLayoutManager().scrollToPosition(messageList.size() - 1);
    }

    private void dealSquareMessage() {
        if (chatType == AppConst.IM_CHAT_TYPE_SQUARE) {
            //加群消息特殊处理
            for (int i = 0; i < messageList.size(); i++) {
                if (messageList.get(i) instanceof GGGroupAddMessage || messageList.get(i) instanceof GGGroupDelMessage) {
                    GGSystemMessage systemMessage = (GGSystemMessage) messageList.get(i);
                    JSONArray accountArray = (JSONArray) systemMessage.getAttrs().get("accountList");
                    String _lctext = MessageListUtils.findSquarePeople(accountArray, String.valueOf(systemMessage.getMessageType()));
                    systemMessage.setText(_lctext);
                }

            }
        }
    }

    /**
     * 设置多功能布局宽高
     */
    private void setContentHeight(int keyBordHeight, View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = keyBordHeight;
        find_more_layout.setLayoutParams(params);
    }

    //群会话入口
    public void setConversation(AVIMConversation conversation, boolean needUpdate, int actionType, UserBean user, List<AVIMMessage> messageList) {
        if (null != conversation) {
            imConversation = conversation;
            chatType = (int) imConversation.getAttribute("chat_type");
            if (chatType == AppConst.IM_CHAT_TYPE_SINGLE && user != null) {
                userBean = user;
            }
            //(刚创建群的时候不拉消息)
            if (actionType == AppConst.CREATE_SQUARE_ROOM_BUILD || actionType == AppConst.CREATE_SQUARE_ROOM_BY_ONE) {
            } else {
                getHistoryMessage(needUpdate, messageList);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!TextUtils.isEmpty(etInput.getText())) {
            GGTextMessage mTextMessage = createTextMessage("[草稿] " + etInput.getText());
            saveToMessageList(mTextMessage);
        }
        MediaManager.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == AppConst.SQUARE_ROOM_AT_SOMEONE) {
                StringBuilder strBuilder = new StringBuilder();
                String inputStr = etInput.getText().toString();
                strBuilder.append(inputStr.substring(0, inputStr.length() - 1));
                List<ContactBean> changeContactBeans = (List<ContactBean>) data.getSerializableExtra("choose_friend_array");
                for (int i = 0; i < changeContactBeans.size(); i++) {
                    strBuilder.append("@" + changeContactBeans.get(i).getTarget() + " ");
                }
                etInput.setText(strBuilder.toString());
                etInput.setSelection(strBuilder.toString().length());
            }
        }

    }

    /**
     * 分享消息(分享后返回这个页面)
     */
    @Subscriber(tag = "oneShare")
    public void getOneShare(BaseMessage baseMessage) {
        Map map = baseMessage.getOthers();
        String share_convid = (String) map.get("share_convid");
        if (share_convid.equals(imConversation.getConversationId())) {
            GGShareMessage mShareMessage = (GGShareMessage) map.get("share_message");
            refreshRecyclerView(mShareMessage, true);
        }
    }

    /**
     * 表情处理
     */
    @Subscriber(tag = "oneEmoji")
    public void getOneEmoji(BaseMessage baseMessage) {
        Map map = baseMessage.getOthers();
        EmojiBean emojiBean = (EmojiBean) map.get("emojiBean");
        if (emojiBean.getEmojiName().equals("[擦掉]")) {
            etInput.setText(StringUtils.StringFilter(etInput.getText().toString()));
        } else {
            etInput.setText(etInput.getText().toString() + emojiBean.getEmojiName());
        }
        etInput.setSelection(etInput.length());
    }

    /**
     * 长按@某人
     */
    @Subscriber(tag = "oneSpeaker")
    public void getOneSpeaker(BaseMessage baseMessage) {
        Map map = baseMessage.getOthers();
        String speakerName = (String) map.get("speakerName");
        String etString = etInput.getText().toString();
        if (!etString.contains("@" + speakerName + " ")) {
            etInput.setText(etString + "@" + speakerName + " ");
        }
        etInput.setSelection(etInput.length());
        //AppDevice.showSoftChangeMethod(etInput);
    }

    /**
     * 图片处理
     */
    @Subscriber(tag = "onePhoto")
    public void getOnePhoto(BaseMessage baseMessage) {
        Map map = baseMessage.getOthers();
        File file = (File) map.get("photoFile");
        sendImageToZyyx(file);
    }

    /**
     * 股票处理
     */
    @Subscriber(tag = "oneStock")
    public void getOneStock(BaseMessage baseMessage) {
        find_more_layout.setVisibility(View.GONE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Map map = baseMessage.getOthers();
        String stockName = (String) map.get("stock_name");
        String stockCode = (String) map.get("stock_code");
        if (!TextUtils.isEmpty(stockName) && !TextUtils.isEmpty(stockCode)) {
            sendStockMessage(stockCode, stockName);
        }
    }

    //判断recyclerView是否停留在底部
    public static boolean isVisBottom(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        //RecyclerView的滑动状态
        int state = recyclerView.getScrollState();

        if (visibleItemCount > 1 && lastVisibleItemPosition == totalItemCount - 1 && state == recyclerView.SCROLL_STATE_IDLE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 消息接收
     */
    @Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        if (null != imConversation && null != baseMessage) {
            Map map = baseMessage.getOthers();
            AVIMMessage message = (AVIMMessage) map.get("message");
            AVIMConversation conversation = (AVIMConversation) map.get("conversation");

            //判断房间一致然后做消息接收处理
            if (imConversation.getConversationId().equals(conversation.getConversationId())) {
                refreshRecyclerView(message, isVisBottom(message_recycler));
                //此处头像，昵称日后有数据再改
                IMMessageBean imMessageBean = null;
                if (chatType == AppConst.IM_CHAT_TYPE_SINGLE) {
                    imMessageBean = new IMMessageBean(imConversation.getConversationId(), chatType, message.getTimestamp(),
                            "0", userBean.getNickname(), String.valueOf(userBean.getFriend_id()), String.valueOf(userBean.getAvatar()), JSON.toJSONString(message));

                } else if (chatType == AppConst.IM_CHAT_TYPE_SQUARE) {
                    //加人删人逻辑
                    if (message instanceof GGGroupDelMessage || message instanceof GGGroupAddMessage) {
                        GGSystemMessage systemMessage = (GGSystemMessage) message;
                        JSONArray accountArray = (JSONArray) systemMessage.getAttrs().get("accountList");
                        String _lctext = MessageListUtils.findSquarePeople(accountArray, String.valueOf(systemMessage.getMessageType()));
                        systemMessage.setText(_lctext);
                    }

                    //群对象和群头像暂时为空
                    imMessageBean = new IMMessageBean(imConversation.getConversationId(), chatType, message.getTimestamp(),
                            "0", conversation.getName(), "", "", JSON.toJSONString(message));
                }

                MessageListUtils.saveMessageInfo(imMessageBean);

            }
        }
    }

}
