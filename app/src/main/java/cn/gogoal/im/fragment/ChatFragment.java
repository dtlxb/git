package cn.gogoal.im.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.IMChatAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AsyncTaskUtil;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.IMHelpers.AudioRecoderUtils;
import cn.gogoal.im.common.IMHelpers.MessageUtils;
import cn.gogoal.im.common.ImageUtils.ImageTakeUtils;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UFileUpload;
import cn.gogoal.im.common.UIHelper;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by huangxx on 2017/2/21.
 */

public class ChatFragment extends BaseFragment {

    @BindView(R.id.message_list)
    RecyclerView message_recycler;

    @BindView(R.id.find_more_layout)
    RelativeLayout find_more_layout;

    @BindView(R.id.take_photo)
    Button take_photo;

    //底部消息发送栏
    @BindView(R.id.live_chat_edit)
    EditText input_text;

    @BindView(R.id.recode_voice)
    Button recode_voice;

    @BindView(R.id.message_send)
    TextView send_text;

    @BindView(R.id.expand_layout)
    RelativeLayout expand_layout;

    @BindView(R.id.voice_layout)
    RelativeLayout voice_layout;

    @BindView(R.id.voice_iv)
    ImageView voice_iv;

    //消息类型
    private static int TEXT_MESSAGE = 1;
    private static int IMAGE_MESSAGE = 2;
    private static int AUDIO_MESSAGE = 3;

    private InputMethodManager inputMethodManager;
    private AVIMConversation imConversation;
    private List<AVIMMessage> messageList = new ArrayList<>();
    private IMChatAdapter imChatAdapter;

    private JSONArray jsonArray;
    private ContactBean contactBean;
    //语音消息处理
    private AudioRecoderUtils mAudioRecoderUtils;
    private boolean hasRecode = false;
    private int duration = 0;

    @Override
    public int bindLayout() {
        return R.layout.fragment_imchat;
    }

    @Override
    public void doBusiness(Context mContext) {

        initRecycleView(message_recycler, null);

        mAudioRecoderUtils = new AudioRecoderUtils(mContext.getCacheDir().getPath());

        imChatAdapter = new IMChatAdapter(getContext(), messageList);

        message_recycler.setAdapter(imChatAdapter);

        //拍照
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageTakeUtils.getInstance().takePhoto(getContext(), 1, false, new ITakePhoto() {
                    @Override
                    public void success(List<String> uriPaths, boolean isOriginalPic) {
                        if (uriPaths != null) {
                            /*//返回的图片集合不为空，执行上传操作
                            if (isOriginalPic) {
                                //批量发送至公司后台
                                doUpload(uriPaths);
                            } else {
                            }*/
                            //压缩后上传
                            compressPhoto(uriPaths);
                        }
                    }

                    @Override
                    public void error() {

                    }
                });
            }
        });

        //发送文字消息(向公司服务器发送消息，然后后台再处理给LeanCloud发送消息)
        send_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示自己的文字消息
                AVIMTextMessage mTextMessage = new AVIMTextMessage();
                HashMap<String, Object> attrsMap = new HashMap<>();
                attrsMap.put("username", AppConst.LEAN_CLOUD_TOKEN);
                mTextMessage.setAttrs(attrsMap);
                mTextMessage.setTimestamp(CalendarUtils.getCurrentTime());
                mTextMessage.setFrom(AppConst.LEAN_CLOUD_TOKEN);
                mTextMessage.setText(input_text.getText().toString());

                imChatAdapter.addItem(mTextMessage);
//                message_recycler.smoothScrollToPosition(messageList.size());
                message_recycler.getLayoutManager().scrollToPosition(messageList.size()-1);

                //文字消息基本信息
                Map<Object, Object> messageMap = new HashMap<>();
                messageMap.put("_lctype", "-1");
                messageMap.put("_lctext", input_text.getText().toString());
                messageMap.put("_lcattrs", AVImClientManager.getInstance().userBaseInfo());

                Map<String, String> params = new HashMap<>();
                params.put("token", AppConst.LEAN_CLOUD_TOKEN);
                params.put("conv_id", imConversation.getConversationId());
                params.put("chat_type", imConversation.getAttribute("chat_type") == null ? "1001" : imConversation.getAttribute("chat_type").toString());
                params.put("message", JSONObject.toJSON(messageMap).toString());
                KLog.e(params);

                //发送文字消息
                sendAVIMMessage(TEXT_MESSAGE, params, mTextMessage);

            }
        });

        //打开多功能
        expand_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals("un_expanded")) {
                    find_more_layout.setVisibility(View.VISIBLE);
                    v.setTag("expanded");

                } else if (v.getTag().equals("expanded")) {
                    find_more_layout.setVisibility(View.GONE);
                    v.setTag("un_expanded");

                }
            }
        });

        //语音和文字切换
        voice_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voice_iv.getTag().equals("key_bod")) {
                    voice_iv.setTag("voice");
                    voice_iv.setImageResource(R.drawable.ic_mic_none_black_24dp);
                    recode_voice.setVisibility(View.VISIBLE);
                    input_text.setVisibility(View.GONE);
                } else if (voice_iv.getTag().equals("voice")) {
                    voice_iv.setImageResource(R.drawable.ic_apps_black_24dp);
                    voice_iv.setTag("key_bod");
                    recode_voice.setVisibility(View.GONE);
                    input_text.setVisibility(View.VISIBLE);
                }
            }
        });

        //输入监听
        input_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (input_text.getText().toString().trim().equals("")) {
                    expand_layout.setVisibility(View.VISIBLE);
                    send_text.setVisibility(View.GONE);
                } else {
                    expand_layout.setVisibility(View.GONE);
                    send_text.setVisibility(View.VISIBLE);
                }
            }
        });

        //录音完成发送录音
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {
                //根据分贝值来设置录音时话筒图标的上下波动，下面有讲解
            }

            @Override
            public void onStop(String filePath) {
                //上传UFile然后发送公司后台；
                sendVoiceToUCloud(filePath);

            }
        });

        //录音
        recode_voice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        recode_voice.setText("松开 结束");
                        mAudioRecoderUtils.startRecord();
                        hasRecode = true;
                        duration = 0;
                        break;
                    case MotionEvent.ACTION_UP:
                        recode_voice.setText("按住 说话");
                        duration = (int) ((mAudioRecoderUtils.stopRecord()) / 1000);
                        hasRecode = false;
                        KLog.e(duration);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (hasRecode) {
            mAudioRecoderUtils.cancelRecord();
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
                        case 1:
                            input_text.setText("");
                            break;
                        case 2:
                            UIHelper.toast(getActivity(), "图片发送成功");
                            break;
                        case 3:
                            UIHelper.toast(getActivity(), "语音发送成功");
                            break;
                        default:
                            break;
                    }
                    //头像暂时未保存
                    IMMessageBean imMessageBean = new IMMessageBean(imConversation.getConversationId(), message.getTimestamp(),
                            "0", contactBean.getNickname(), String.valueOf(contactBean.getFriend_id()), String.valueOf(contactBean.getAvatar()), message);

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

    private void compressPhoto(List<String> uriPaths) {
        KLog.e(uriPaths);
        for (int i = 0; i < uriPaths.size(); i++) {
            Luban.get(getActivity())
                    .load(new File(uriPaths.get(i)))                     //传人要压缩的图片
                    .putGear(Luban.THIRD_GEAR)                           //设定压缩档次，默认三挡
                    .setCompressListener(new OnCompressListener() {      //设置回调

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(final File file) {
                            sendImageToZyyx(file);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    }).launch();
        }
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
        attrsMap.put("username", AppConst.LEAN_CLOUD_TOKEN);
        final AVIMImageMessage mImageMessage = new AVIMImageMessage(imagefile);
        mImageMessage.setFrom(AppConst.LEAN_CLOUD_TOKEN);
        mImageMessage.setAttrs(attrsMap);
        mImageMessage.setTimestamp(CalendarUtils.getCurrentTime());

        imChatAdapter.addItem(mImageMessage);
//        message_recycler.smoothScrollToPosition(messageList.size());
        message_recycler.getLayoutManager().scrollToPosition(messageList.size()-1);

        //分个上传UFile;
        UFileUpload.getInstance().upload(file, UFileUpload.Type.IMAGE, new UFileUpload.UploadListener() {
            @Override
            public void onUploading(int progress) {

            }

            @Override
            public void onSuccess(String onlineUri) {


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
                messageMap.put("size", "");

                Map<String, String> params = new HashMap<>();
                params.put("token", AppConst.LEAN_CLOUD_TOKEN);
                params.put("conv_id", imConversation.getConversationId());
                params.put("chat_type", imConversation.getAttribute("chat_type") == null ? "1001" : imConversation.getAttribute("chat_type").toString());
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

    private void doUpload(final List<String> uriPaths) {
        for (int i = 0; i < uriPaths.size(); i++) {
            sendImageToZyyx(new File(uriPaths.get(i)));
        }
    }

    private void sendVoiceToUCloud(final String voicePath) {
        AsyncTaskUtil.doAsync(new AsyncTaskUtil.AsyncCallBack() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void doInBackground() {

                //自己显示语音消息
                HashMap<String, Object> attrsMap = new HashMap<>();
                attrsMap.put("username", AppConst.LEAN_CLOUD_TOKEN);

                //封装一个AVfile对象
                HashMap<String, Object> metaData = new HashMap<>();
                metaData.put("duration", duration);
                AVFile Audiofile = new AVFile("Audiofile", voicePath, metaData);

                final AVIMAudioMessage mAudioMessage = new AVIMAudioMessage(Audiofile);

                KLog.e(mAudioMessage.getAVFile().getUrl());
                mAudioMessage.setFrom(AppConst.LEAN_CLOUD_TOKEN);
                mAudioMessage.setAttrs(attrsMap);
                mAudioMessage.setTimestamp(CalendarUtils.getCurrentTime());

                HashMap<String, Object> map = new HashMap<>();
                map.put("audio_message", mAudioMessage);
                BaseMessage baseMessage = new BaseMessage("audio_info", map);
                AppManager.getInstance().sendMessage("refresh_recyle", baseMessage);
//                message_recycler.smoothScrollToPosition(messageList.size());
                message_recycler.getLayoutManager().scrollToPosition(messageList.size()-1);

                UFileUpload.getInstance().upload(new File(voicePath), UFileUpload.Type.AUDIO, new UFileUpload.UploadListener() {
                    @Override
                    public void onUploading(int progress) {

                    }

                    @Override
                    public void onSuccess(String onlineUri) {

                        //语音消息基本信息
                        Map<Object, Object> messageMap = new HashMap<>();
                        messageMap.put("_lctype", "-3");
                        messageMap.put("_lctext", "");
                        messageMap.put("_lcattrs", AVImClientManager.getInstance().userBaseInfo());
                        messageMap.put("url", onlineUri);
                        messageMap.put("format", "amr");
                        messageMap.put("duration", String.valueOf(duration));

                        Map<String, String> params = new HashMap<>();
                        params.put("token", AppConst.LEAN_CLOUD_TOKEN);
                        params.put("conv_id", imConversation.getConversationId());
                        params.put("chat_type", imConversation.getAttribute("chat_type") == null ? "1001" : imConversation.getAttribute("chat_type").toString());
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

    private void getHistoryMessage() {
        if (null != imConversation) {
            imConversation.queryMessages(20, new AVIMMessagesQueryCallback() {
                @Override
                public void done(List<AVIMMessage> list, AVIMException e) {
                    if (null == e) {
                        for (int i = 0; i < list.size(); i++) {

                        }
                        messageList.addAll(list);

                        //拿到对方信息
                        getSpeakToInfo(imConversation);
                        jsonArray = SPTools.getJsonArray(AppConst.LEAN_CLOUD_TOKEN + "_conversation_beans", new JSONArray());
                        if (messageList.size() > 0 && null != contactBean) {
                            AVIMMessage lastMessage = messageList.get(messageList.size() - 1);

                            IMMessageBean imMessageBean = new IMMessageBean(imConversation.getConversationId(), lastMessage.getTimestamp(),
                                    "0", contactBean.getNickname(), String.valueOf(contactBean.getFriend_id()), String.valueOf(contactBean.getAvatar()), lastMessage);

                            MessageUtils.saveMessageInfo(jsonArray, imMessageBean);
                        }

                        imChatAdapter.setChatType(imConversation.getAttribute("chat_type") == null ? "1001" : imConversation.getAttribute("chat_type").toString());
                        imChatAdapter.notifyDataSetChanged();
//                        message_recycler.smoothScrollToPosition(messageList.size());
                        message_recycler.getLayoutManager().scrollToPosition(messageList.size()-1);
                    }
                }
            });
        }
    }

    private void getSpeakToInfo(AVIMConversation conversation) {
        String responseInfo = SPTools.getString(AppConst.LEAN_CLOUD_TOKEN + "_Contacts", "");
        List<ContactBean> contactBeanList = new ArrayList<>();

        //拿到对方
        String speakTo = "";
        List<String> members = new ArrayList<>();
        members.addAll(conversation.getMembers());

        if (members.size() > 0) {
            if (members.size() == 2) {
                if (members.contains(AppConst.LEAN_CLOUD_TOKEN)) {
                    members.remove(AppConst.LEAN_CLOUD_TOKEN);
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
            }
        }
    }

    public void setConversation(AVIMConversation conversation) {
        if (null != conversation) {
            imConversation = conversation;
            //拉取历史记录(直接从LeanCloud拉取)
            getHistoryMessage();
        }
    }

    @Subscriber(tag = "refresh_recyle")
    public void audioRefresh(BaseMessage message) {
        AVIMAudioMessage audioMessage = (AVIMAudioMessage) message.getOthers().get("audio_message");
        imChatAdapter.addItem(audioMessage);
    }

    /**
     * 消息接收
     */
    @Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        if (null != imConversation && null != baseMessage) {
            Map<String, Object> map = baseMessage.getOthers();
            AVIMMessage message = (AVIMMessage) map.get("message");
            AVIMConversation conversation = (AVIMConversation) map.get("conversation");

            //判断房间一致然后做消息接收处理
            if (imConversation.getConversationId().equals(conversation.getConversationId())) {
                imChatAdapter.addItem(message);
//                message_recycler.smoothScrollToPosition(messageList.size());
                message_recycler.getLayoutManager().scrollToPosition(messageList.size()-1);

                //此处头像，昵称日后有数据再改
                IMMessageBean imMessageBean = new IMMessageBean(imConversation.getConversationId(), message.getTimestamp(),
                        "0", message.getFrom(), String.valueOf(contactBean.getFriend_id()), String.valueOf(contactBean.getAvatar()), message);

                MessageUtils.saveMessageInfo(jsonArray, imMessageBean);
            }
        }
    }

}
