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
import com.alibaba.fastjson.TypeReference;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.hply.imagepicker.ITakePhoto;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.ChooseContactActivity;
import cn.gogoal.im.adapter.ChatFunctionAdapter;
import cn.gogoal.im.adapter.IMChatAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.EmojiBean;
import cn.gogoal.im.bean.FoundData;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.AsyncTaskUtil;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.IMHelpers.MessageUtils;
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

    private MyListener listener;
    private int chatType;

    //消息类型
    private static int TEXT_MESSAGE = -1;
    private static int IMAGE_MESSAGE = -2;
    private static int AUDIO_MESSAGE = -3;
    private static int STOCK_MESSAGE = 11;

    private AVIMConversation imConversation;
    private List<AVIMMessage> messageList = new ArrayList<>();
    private IMChatAdapter imChatAdapter;
    private List<FoundData.ItemPojos> itemPojosList;
    private JSONArray jsonArray;
    private ContactBean<String> contactBean;
    private EmojiFragment emojiFragment;
    private FunctionFragment functionFragment;
    private FragmentTransaction transaction;
    private FragmentManager childManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (MyListener) context;
    }

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

        int keyBordHeight = SPTools.getInt("soft_keybord_height", AppDevice.dp2px(getActivity(), 220));
        setContentHeight(keyBordHeight);

        keyboardLayout.setOnKeyboardChangeListener(new KeyboardLaunchLinearLayout.OnKeyboardChangeListener() {
            @Override
            public void OnKeyboardPop(int height) {
                SPTools.saveInt("soft_keybord_height", height);
                setContentHeight(height);
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
                } else {
                    message_swipe.setRefreshing(false);
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

                find_more_layout.setVisibility(View.GONE);
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
                AVIMTextMessage mTextMessage = new AVIMTextMessage();
                HashMap<String, Object> attrsMap = new HashMap<>();
                attrsMap.put("username", UserUtils.getNickname());
                attrsMap.put("avatar", UserUtils.getUserAvatar());
                mTextMessage.setAttrs(attrsMap);
                mTextMessage.setTimestamp(CalendarUtils.getCurrentTime());
                mTextMessage.setFrom(UserUtils.getMyAccountId());
                mTextMessage.setText(etInput.getText().toString());

                imChatAdapter.addItem(mTextMessage);
                imChatAdapter.notifyItemInserted(messageList.size() - 1);
                message_recycler.smoothScrollToPosition(messageList.size() - 1);

                //文字消息基本信息
                Map<Object, Object> messageMap = new HashMap<>();
                messageMap.put("_lctype", "-1");
                messageMap.put("_lctext", etInput.getText().toString());
                messageMap.put("_lcattrs", AVImClientManager.getInstance().userBaseInfo());

                Map<String, String> params = new HashMap<>();
                params.put("token", UserUtils.getToken());
                params.put("conv_id", imConversation.getConversationId());
                params.put("chat_type", String.valueOf(chatType));
                params.put("message", JSONObject.toJSONString(messageMap));
                KLog.e(params);

                etInput.setText("");
                //发送文字消息
                sendAVIMMessage(TEXT_MESSAGE, params, mTextMessage);

            }
        });

        //语音和文字切换
        imgVoice.setOnSwitchListener(new SwitchImageView.OnSwitchListener()

        {
            @Override
            public void onSwitch(View view, int state) {
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
                        find_more_layout.setVisibility(View.GONE);
                        break;
                }
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
                } else if (etInput.getText().toString().trim().equals("@") && chatType == AppConst.IM_CHAT_TYPE_SQUARE) {
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

    private void initFragment() {
        functionFragment = new FunctionFragment();
        emojiFragment = new EmojiFragment();
    }

    //输入框内元素点击事件
    @OnClick({R.id.img_function, R.id.img_emoji})
    void chatClick(View view) {
        transaction = childManager.beginTransaction();
        switch (view.getId()) {
            case R.id.img_function:
                find_more_layout.setVisibility(View.VISIBLE);
                if (!functionFragment.isAdded()) {
                    transaction.add(R.id.find_more_layout, functionFragment, "FunctionFragment");
                }
                transaction.show(functionFragment).hide(emojiFragment);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                (EmojiCheckBox).setChecked(false);
                checkAction(view);
                break;
            case R.id.img_emoji:
                find_more_layout.setVisibility(View.VISIBLE);
                if (!emojiFragment.isAdded()) {
                    transaction.add(R.id.find_more_layout, emojiFragment, "EmojiFragment");
                }
                transaction.show(emojiFragment).hide(functionFragment);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                (functionCheckBox).setChecked(false);
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

    public void sendAVIMMessage(final int messageType, final Map<String, String> params, final AVIMMessage message) {

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(result.get("code"));
                if ((int) result.get("code") == 0) {
                    switch (messageType) {
                        case -1:
                            break;
                        case -2:
                            break;
                        case -3:
                            break;
                        case 11:
                            break;
                        default:
                            break;
                    }
                    //头像暂时未保存
                    IMMessageBean imMessageBean = null;
                    if (chatType == 1001) {
                        if (null != contactBean) {
                            imMessageBean = new IMMessageBean(imConversation.getConversationId(), chatType, message.getTimestamp(),
                                    "0", null != contactBean.getTarget() ? contactBean.getTarget() : "", String.valueOf(contactBean.getUserId()), String.valueOf(contactBean.getAvatar()), message);
                        }
                    } else if (chatType == AppConst.IM_CHAT_TYPE_SQUARE) {
                        imMessageBean = new IMMessageBean(imConversation.getConversationId(), chatType, message.getTimestamp(),
                                "0", imConversation.getName(), "", "", message);
                    } else {

                    }
                    MessageUtils.saveMessageInfo(jsonArray, imMessageBean);

                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.CHAT_SEND_MESSAGE, ggHttpInterface).startGet();

    }

    private void sendStockMessage(String stockCode, String stockName) {

        if (null == imConversation) {
            UIHelper.toast(getActivity(), R.string.network_busy);
            return;
        }
        //股票消息(消息type:11,加上stockCode,stockName);
        AVIMMessage mStockMessage = new AVIMMessage();
        mStockMessage.setTimestamp(CalendarUtils.getCurrentTime());
        mStockMessage.setFrom(UserUtils.getMyAccountId());

        //添加股票信息
        Map<String, String> lcattrsMap;
        lcattrsMap = AVImClientManager.getInstance().userBaseInfo();
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
        KLog.e(params);

        mStockMessage.setContent(JSONObject.toJSONString(messageMap));
        imChatAdapter.addItem(mStockMessage);
        imChatAdapter.notifyItemInserted(messageList.size() - 1);
        message_recycler.smoothScrollToPosition(messageList.size() - 1);

        etInput.setText("");
        //发送股票消息
        sendAVIMMessage(STOCK_MESSAGE, params, mStockMessage);
    }

    private void sendImageToZyyx(final File file) {
        KLog.e(file.getPath());
        //图片宽高处理
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();

        //封装一个AVfile对象
        HashMap<String, Object> metaData = new HashMap<>();
        metaData.put("width", width);
        metaData.put("height", height);
        AVFile imagefile = new AVFile("imagefile", file.getPath(), metaData);

        //显示自己的图片消息
        HashMap<String, Object> attrsMap = new HashMap<>();
        attrsMap.put("username", UserUtils.getNickname());
        attrsMap.put("avatar", UserUtils.getUserAvatar());
        final AVIMImageMessage mImageMessage = new AVIMImageMessage(imagefile);
        mImageMessage.setFrom(UserUtils.getMyAccountId());
        mImageMessage.setAttrs(attrsMap);
        mImageMessage.setTimestamp(CalendarUtils.getCurrentTime());

        imChatAdapter.addItem(mImageMessage);
        imChatAdapter.notifyItemInserted(messageList.size() - 1);
        message_recycler.smoothScrollToPosition(messageList.size() - 1);
        //message_recycler.getLayoutManager().scrollToPosition(messageList.size()-1);

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
                messageMap.put("_lcattrs", AVImClientManager.getInstance().userBaseInfo());
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
                KLog.e(params);

                //发送图片消息
                sendAVIMMessage(IMAGE_MESSAGE, params, mImageMessage);
            }

            @Override
            public void onFailed() {
                UIHelper.toast(getActivity(), "消息发送失败！");
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

                //封装一个AVfile对象
                HashMap<String, Object> metaData = new HashMap<>();
                metaData.put("duration", seconds);
                AVFile Audiofile = new AVFile("Audiofile", voicePath, metaData);

                final AVIMAudioMessage mAudioMessage = new AVIMAudioMessage(Audiofile);

                KLog.e(mAudioMessage.getAVFile().getUrl());
                mAudioMessage.setFrom(UserUtils.getMyAccountId());
                mAudioMessage.setAttrs(attrsMap);
                mAudioMessage.setTimestamp(CalendarUtils.getCurrentTime());

                HashMap<String, Object> map = new HashMap<>();
                map.put("audio_message", mAudioMessage);
                BaseMessage baseMessage = new BaseMessage("audio_info", map);
                AppManager.getInstance().sendMessage("refresh_recyle", baseMessage);
                message_recycler.smoothScrollToPosition(messageList.size() - 1);

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
                        messageMap.put("_lcattrs", AVImClientManager.getInstance().userBaseInfo());
                        messageMap.put("url", onlineUri);
                        messageMap.put("format", "amr");
                        messageMap.put("duration", String.valueOf(seconds));

                        Map<String, String> params = new HashMap<>();
                        params.put("token", UserUtils.getToken());
                        params.put("conv_id", imConversation.getConversationId());
                        params.put("chat_type", String.valueOf(chatType));
                        params.put("message", JSONObject.toJSON(messageMap).toString());
                        KLog.e(params);

                        //发送语音消息
                        sendAVIMMessage(AUDIO_MESSAGE, params, mAudioMessage);
                    }

                    @Override
                    public void onFailed() {
                        UIHelper.toast(getActivity(), "消息发送失败！");
                    }
                });
            }

            @Override
            public void onPostExecute() {

            }
        });
    }

    private void getHistoryMessage(final boolean needUpdate) {
        if (null != imConversation) {
            imConversation.queryMessages(15, new AVIMMessagesQueryCallback() {

                @Override
                public void done(List<AVIMMessage> list, AVIMException e) {
                    if (null == e && null != list && list.size() > 0) {

                        messageList.addAll(list);
                        jsonArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + "_conversation_beans", new JSONArray());
                        KLog.e(messageList.get(list.size() - 1).getContent());
                        if (chatType == AppConst.IM_CHAT_TYPE_SINGLE) {
                            //拿到对方信息
                            getSpeakToInfo(imConversation);
                        } else if (chatType == AppConst.IM_CHAT_TYPE_SQUARE) {
                            //加群消息特殊处理
                            for (int i = 0; i < messageList.size(); i++) {
                                JSONObject contentObject = JSON.parseObject(messageList.get(i).getContent());
                                String _lctype = contentObject.getString("_lctype");
                                if (_lctype.equals("5") || _lctype.equals("6")) {
                                    if (null != contentObject.getJSONObject("_lcattrs") && null != contentObject.getJSONObject("_lcattrs").getJSONArray("accountList")) {
                                        HashMap<String, String> map = new HashMap<>();
                                        JSONArray accountArray = contentObject.getJSONObject("_lcattrs").getJSONArray("accountList");
                                        String _lctext = MessageUtils.findSquarePeople(accountArray, _lctype);
                                        map.put("_lctext", _lctext);
                                        map.put("_lctype", _lctype);
                                        messageList.get(i).setContent(JSON.toJSONString(map));
                                    }
                                }
                            }
                        }

                        //单聊，群聊处理(没发消息的时候不保存)
                        if (messageList.size() > 0 && needUpdate) {
                            IMMessageBean imMessageBean = null;
                            AVIMMessage lastMessage = messageList.get(messageList.size() - 1);
                            if (chatType == AppConst.IM_CHAT_TYPE_SINGLE) {
                                if (null != contactBean) {
                                    //"0"开始:未读数-对话名字-对方名字-对话头像-最后信息
                                    KLog.e(contactBean);
                                    imMessageBean = new IMMessageBean(imConversation.getConversationId(), chatType, lastMessage.getTimestamp(), "0",
                                            null != contactBean.getTarget() ? contactBean.getTarget() : "",
                                            String.valueOf(contactBean.getFriend_id()), String.valueOf(contactBean.getAvatar()), lastMessage);
                                }
                            } else if (chatType == AppConst.IM_CHAT_TYPE_SQUARE) {
                                //"0"开始:未读数-对话名字-对方名字-对话头像-最后信息(群对象和群头像暂时为空)
                                imMessageBean = new IMMessageBean(imConversation.getConversationId(), chatType, lastMessage.getTimestamp(), "0", imConversation.getName(),
                                        "", "", lastMessage);
                            }
                            KLog.e(imConversation.getName());
                            MessageUtils.saveMessageInfo(jsonArray, imMessageBean);
                        }

                        imChatAdapter.setChatType(chatType);
                        imChatAdapter.notifyDataSetChanged();

                        message_recycler.getLayoutManager().scrollToPosition(messageList.size() - 1);
                    }
                }
            });

        }
    }

    private void getSpeakToInfo(AVIMConversation conversation) {
        String responseInfo = SPTools.getString(UserUtils.getMyAccountId() + "_contact_beans", "");
        List<ContactBean<String>> contactBeanList = new ArrayList<>();

        //拿到对方
        String speakTo = "";
        List<String> members = new ArrayList<>();
        members.addAll(conversation.getMembers());
        if (members.size() > 0) {
            if (members.size() == 2) {
                if (members.contains(UserUtils.getMyAccountId())) {
                    members.remove(UserUtils.getMyAccountId());
                    speakTo = members.get(0);
                }
            } else {
            }
        } else {
        }
        if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
            BaseBeanList<ContactBean<String>> beanList = JSONObject.parseObject(
                    responseInfo,
                    new TypeReference<BaseBeanList<ContactBean<String>>>() {
                    });
            List<ContactBean<String>> list = beanList.getData();

            for (ContactBean<String> bean : list) {
                bean.setContactType(ContactBean.ContactType.PERSION_ITEM);
            }

            contactBeanList.addAll(list);
        }
        for (int i = 0; i < contactBeanList.size(); i++) {
            if ((contactBeanList.get(i).getFriend_id() + "").equals(speakTo)) {
                contactBean = contactBeanList.get(i);
                if (null != listener) {
                    listener.setData(contactBean);
                }
                KLog.e(contactBean);
            }
        }
    }

    /**
     * 设置多功能布局宽高
     */
    private void setContentHeight(int keyBordHeight) {
        ViewGroup.LayoutParams params = find_more_layout.getLayoutParams();
        params.height = keyBordHeight;
        find_more_layout.setLayoutParams(params);
    }

    //群会话入口
    public void setConversation(AVIMConversation conversation, boolean needUpdate, int actionType) {
        if (null != conversation) {
            imConversation = conversation;
            chatType = (int) imConversation.getAttribute("chat_type");
            //(刚创建群的时候不拉消息)
            if (actionType == AppConst.CREATE_SQUARE_ROOM_BUILD || actionType == AppConst.CREATE_SQUARE_ROOM_BY_ONE) {
            } else {
                getHistoryMessage(needUpdate);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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

    //activiy必须实现这个接口
    public interface MyListener {
        void setData(ContactBean contactBean);
    }

    @Subscriber(tag = "refresh_recyle")
    public void audioRefresh(BaseMessage<AVIMAudioMessage> message) {
        AVIMAudioMessage audioMessage = message.getOthers().get("audio_message");
        imChatAdapter.addItem(audioMessage);
        imChatAdapter.notifyItemInserted(messageList.size() - 1);
        message_recycler.smoothScrollToPosition(messageList.size() - 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == AppConst.SQUARE_ROOM_AT_SOMEONE) {
                StringBuilder strBuilder = new StringBuilder();

                List<ContactBean> changeContactBeens = (List<ContactBean>) data.getSerializableExtra("choose_friend_array");
                for (int i = 0; i < changeContactBeens.size(); i++) {
                    strBuilder.append("@" + changeContactBeens.get(i).getTarget() + " ");
                }
                etInput.setText(strBuilder.toString());
                etInput.setSelection(strBuilder.toString().length());
            }
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

    /**
     * 消息接收
     */
    @Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        if (null != imConversation && null != baseMessage) {
            Map map = baseMessage.getOthers();
            AVIMMessage message = (AVIMMessage) map.get("message");
            AVIMConversation conversation = (AVIMConversation) map.get("conversation");
            JSONObject contentObject = JSON.parseObject(message.getContent());
            String _lctype = contentObject.getString("_lctype");

            KLog.e(contentObject);
            //判断房间一致然后做消息接收处理
            if (imConversation.getConversationId().equals(conversation.getConversationId())) {
                imChatAdapter.addItem(message);
                imChatAdapter.notifyItemInserted(messageList.size() - 1);
                message_recycler.smoothScrollToPosition(messageList.size() - 1);
                //此处头像，昵称日后有数据再改
                IMMessageBean imMessageBean = null;
                if (chatType == AppConst.IM_CHAT_TYPE_SINGLE) {
                    imMessageBean = new IMMessageBean(imConversation.getConversationId(), chatType, message.getTimestamp(),
                            "0", message.getFrom(), String.valueOf(contactBean.getFriend_id()), String.valueOf(contactBean.getAvatar()), message);

                } else if (chatType == AppConst.IM_CHAT_TYPE_SQUARE) {
                    //加人删人逻辑
                    if (_lctype.equals("5") || _lctype.equals("6")) {
                        JSONArray accountArray = contentObject.getJSONObject("_lcattrs").getJSONArray("accountList");
                        String _lctext = MessageUtils.findSquarePeople(accountArray, _lctype);
                        map.put("_lctext", _lctext);
                        map.put("_lctype", _lctype);
                        message.setContent(JSON.toJSONString(map));
                    }

                    //群对象和群头像暂时为空
                    imMessageBean = new IMMessageBean(imConversation.getConversationId(), chatType, message.getTimestamp(),
                            "0", conversation.getName(), "", "", message);
                }

                MessageUtils.saveMessageInfo(jsonArray, imMessageBean);
            }
        }
    }

}
