package cn.gogoal.im.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.socks.library.KLog;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.recording.MediaManager;

/**
 * Created by huangxx on 2017/2/27.
 */

public class IMChatAdapter extends RecyclerView.Adapter {

    //文字
    public static int TYPE_LEFT_TEXT_MESSAGE = 0x01;
    public static int TYPE_RIGHT_TEXT_MESSAGE = 0x02;
    //图片
    public static int TYPE_LEFT_IMAGE_MESSAGE = 0x03;
    public static int TYPE_RIGHT_IMAGE_MESSAGE = 0x04;
    //语音
    public static int TYPE_LEFT_VOICE_MESSAGE = 0x05;
    public static int TYPE_RIGHT_VOICE_MESSAGE = 0x06;
    //股票
    public static int TYPE_LEFT_STOCK_MESSAGE = 0x07;
    public static int TYPE_RIGHT_STOCK_MESSAGE = 0x08;
    //未知消息
    public static int TYPE_LEFT_UNKONW_MESSAGE = 0x09;
    public static int TYPE_RIGHT_UNKONW_MESSAGE = 0x10;
    //系统
    public static int TYPE_SYSTEM_MESSAGE = 0x11;
    private List<AVIMMessage> messageList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int chatType;

    public IMChatAdapter(Context mContext, List<AVIMMessage> messageList) {
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.messageList = messageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LEFT_TEXT_MESSAGE) {
            return new LeftTextViewHolder(mLayoutInflater.inflate(R.layout.item_left_text, parent, false));
        } else if (viewType == TYPE_RIGHT_TEXT_MESSAGE) {
            return new RightTextViewHolder(mLayoutInflater.inflate(R.layout.item_right_text, parent, false));
        } else if (viewType == TYPE_LEFT_IMAGE_MESSAGE) {
            return new LeftImageViewHolder(mLayoutInflater.inflate(R.layout.item_left_image, parent, false));
        } else if (viewType == TYPE_RIGHT_IMAGE_MESSAGE) {
            return new RightImageViewHolder(mLayoutInflater.inflate(R.layout.item_right_image, parent, false));
        } else if (viewType == TYPE_LEFT_VOICE_MESSAGE) {
            return new LeftAudioViewHolder(mLayoutInflater.inflate(R.layout.item_left_audio, parent, false));
        } else if (viewType == TYPE_RIGHT_VOICE_MESSAGE) {
            return new RightAudioViewHolder(mLayoutInflater.inflate(R.layout.item_right_audio, parent, false));
        } else if (viewType == TYPE_RIGHT_STOCK_MESSAGE) {
            return new RightStockViewHolder(mLayoutInflater.inflate(R.layout.item_right_text, parent, false));
        } else if (viewType == TYPE_LEFT_STOCK_MESSAGE) {
            return new LeftStockViewHolder(mLayoutInflater.inflate(R.layout.item_left_text, parent, false));
        } else if (viewType == TYPE_RIGHT_UNKONW_MESSAGE) {
            return new LeftUnKonwViewHolder(mLayoutInflater.inflate(R.layout.item_left_unknow, parent, false));
        } else if (viewType == TYPE_LEFT_UNKONW_MESSAGE) {
            return new RightUnKonwViewHolder(mLayoutInflater.inflate(R.layout.item_right_unknow, parent, false));
        } else if (viewType == TYPE_SYSTEM_MESSAGE) {
            return new ChatGroupAddViewHolder(mLayoutInflater.inflate(R.layout.item_system_notify, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        AVIMMessage avimMessage = messageList.get(position);
        JSONObject contentObject = JSON.parseObject(avimMessage.getContent());
        JSONObject lcattrsObject = (JSONObject) contentObject.get("_lcattrs");
        String messageType = contentObject.getString("_lctype");
        KLog.e(messageType);
        if (!messageType.equals("5") && !messageType.equals("6") && !messageType.equals("8")) {
            if (chatType == 1001) {
                ((IMCHatViewHolder) holder).user_name.setVisibility(View.GONE);
            } else if (chatType == 1002) {
                ((IMCHatViewHolder) holder).user_name.setVisibility(View.VISIBLE);
            } else {
                ((IMCHatViewHolder) holder).user_name.setVisibility(View.VISIBLE);
            }
            ImageDisplay.loadNetImage(mContext, (String) lcattrsObject.get("avatar"), ((IMCHatViewHolder) holder).user_head_photo);
        } else {
        }
        if (holder instanceof LeftTextViewHolder) {
            AVIMTextMessage textMessage = (AVIMTextMessage) avimMessage;
            ((LeftTextViewHolder) holder).user_name.setText((String) textMessage.getAttrs().get("username"));
            ((LeftTextViewHolder) holder).what_user_send.setText(textMessage.getText());

            showMessageTime(position, ((LeftTextViewHolder) holder).message_time);

        } else if (holder instanceof RightTextViewHolder) {
            AVIMTextMessage textMessage = (AVIMTextMessage) avimMessage;
            ((RightTextViewHolder) holder).what_user_send.setText(textMessage.getText());
            ((RightTextViewHolder) holder).user_name.setVisibility(View.GONE);

            showMessageTime(position, ((RightTextViewHolder) holder).message_time);

        } else if (holder instanceof LeftImageViewHolder) {
            AVIMImageMessage imageMessage = (AVIMImageMessage) avimMessage;
            ((LeftImageViewHolder) holder).user_name.setText((String) imageMessage.getAttrs().get("username"));

            //获取后台图片大小设置
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((LeftImageViewHolder) holder).image_user_send.getLayoutParams();
            setImageSize(params, imageMessage);
            ((LeftImageViewHolder) holder).image_user_send.setLayoutParams(params);
            ImageDisplay.loadNetImage(mContext, imageMessage.getAVFile().getUrl(), ((LeftImageViewHolder) holder).image_user_send);

            showMessageTime(position, ((LeftImageViewHolder) holder).message_time);

        } else if (holder instanceof RightImageViewHolder) {
            AVIMImageMessage imageMessage = (AVIMImageMessage) avimMessage;
            ((RightImageViewHolder) holder).user_name.setVisibility(View.GONE);
            //获取后台图片大小设置
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((RightImageViewHolder) holder).image_user_send.getLayoutParams();
            setImageSize(params, imageMessage);
            ((RightImageViewHolder) holder).image_user_send.setLayoutParams(params);
            ImageDisplay.loadNetImage(mContext, imageMessage.getAVFile().getUrl(), ((RightImageViewHolder) holder).image_user_send);
            showMessageTime(position, ((RightImageViewHolder) holder).message_time);

        } else if (holder instanceof RightAudioViewHolder) {
            final AVIMAudioMessage audioMessage = (AVIMAudioMessage) avimMessage;
            ((RightAudioViewHolder) holder).user_name.setVisibility(View.GONE);
            //设置语音宽度
            ViewGroup.LayoutParams params = ((RightAudioViewHolder) holder).recorder_length.getLayoutParams();
            int mMaxItemWidth = AppDevice.getWidth(mContext) - AppDevice.dp2px(mContext, 160);
            int mMinItemWidth = (int) (AppDevice.getWidth(mContext) * 0.16f);
            params.width = (int) (mMinItemWidth + (mMaxItemWidth / 60f) * ((Number) audioMessage.getFileMetaData().get("duration")).doubleValue());
            if (params.width > mMaxItemWidth) {
                params.width = mMaxItemWidth;
            }

            ((RightAudioViewHolder) holder).recorder_length.setLayoutParams(params);
            //设置语音时长
            if ((int) ((Number) audioMessage.getFileMetaData().get("duration")).doubleValue() > 60) {
                ((RightAudioViewHolder) holder).recorder_time.setText(60 + "\"");
            } else {
                ((RightAudioViewHolder) holder).recorder_time.setText((int) ((Number) audioMessage.getFileMetaData().get("duration")).doubleValue() + "\"");
            }
            ((RightAudioViewHolder) holder).recorder_length.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //播放动画
                    ((RightAudioViewHolder) holder).animView.setBackgroundResource(R.drawable.right_play_anim);
                    AnimationDrawable anim = (AnimationDrawable) ((RightAudioViewHolder) holder).animView.getBackground();
                    anim.start();
                    //播放音频
                    MediaManager.playSound(audioMessage.getAVFile().getUrl(), new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            ((RightAudioViewHolder) holder).animView.setBackgroundResource(R.mipmap.right_voice_anime3);
                        }
                    });
                }
            });

            showMessageTime(position, ((RightAudioViewHolder) holder).message_time);
        } else if (holder instanceof LeftAudioViewHolder) {
            final AVIMAudioMessage audioMessage = (AVIMAudioMessage) avimMessage;
            //设置语音宽度
            final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((LeftAudioViewHolder) holder).recorder_length.getLayoutParams();
            int mMaxItemWidth = AppDevice.getWidth(mContext) - AppDevice.dp2px(mContext, 160);
            int mMinItemWidth = (int) (AppDevice.getWidth(mContext) * 0.16f);
            params.width = (int) (mMinItemWidth + (mMaxItemWidth / 60f) * ((Number) audioMessage.getFileMetaData().get("duration")).doubleValue());
            if (params.width > mMaxItemWidth) {
                params.width = mMaxItemWidth;
            }
            if (chatType == 1001) {
                params.setMargins(0, 0, 0, 0);
            } else if (chatType == 1002) {
                params.setMargins(0, AppDevice.dp2px(mContext, 13), 0, 0);
            }
            ((LeftAudioViewHolder) holder).recorder_length.setLayoutParams(params);
            //设置语音时长
            if ((int) ((Number) audioMessage.getFileMetaData().get("duration")).doubleValue() > 60) {
                ((LeftAudioViewHolder) holder).recorder_time.setText(60 + "\"");
            } else {
                ((LeftAudioViewHolder) holder).recorder_time.setText((int) ((Number) audioMessage.getFileMetaData().get("duration")).doubleValue() + "\"");
            }
            //点击播放语音
            ((LeftAudioViewHolder) holder).recorder_length.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (((LeftAudioViewHolder) holder).animView != null) {
                        ((LeftAudioViewHolder) holder).animView.setBackgroundResource(R.drawable.v_anim3);
                        ((LeftAudioViewHolder) holder).animView = null;
                    }*/
                    //播放动画
                    ((LeftAudioViewHolder) holder).animView.setBackgroundResource(R.drawable.left_play_anim);
                    AnimationDrawable anim = (AnimationDrawable) ((LeftAudioViewHolder) holder).animView.getBackground();
                    anim.start();
                    //播放音频
                    MediaManager.playSound(audioMessage.getAVFile().getUrl(), new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            ((LeftAudioViewHolder) holder).animView.setBackgroundResource(R.mipmap.left_voice_anime3);
                        }
                    });
                }
            });

            showMessageTime(position, ((LeftAudioViewHolder) holder).message_time);
        } else if (holder instanceof LeftStockViewHolder) {
            if (null != lcattrsObject) {
                String stockCode = lcattrsObject.getString("stockCode");
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder("$ " + stockCode + " " + lcattrsObject.getString("stockName"));
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#f53f3f")); // 设置字体颜色
                stringBuilder.setSpan(fcs, 2, stockCode.length() + 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                ((LeftStockViewHolder) holder).what_user_send.setText(stringBuilder);
                ((LeftStockViewHolder) holder).user_name.setText(lcattrsObject.getString("username"));
            }

            showMessageTime(position, ((LeftStockViewHolder) holder).message_time);
        } else if (holder instanceof RightStockViewHolder) {
            if (null != lcattrsObject) {
                String stockCode = lcattrsObject.getString("stockCode");
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder("$ " + stockCode + " " + lcattrsObject.getString("stockName"));
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#f53f3f")); // 设置字体颜色
                stringBuilder.setSpan(fcs, 2, stockCode.length() + 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                ((RightStockViewHolder) holder).what_user_send.setText(stringBuilder);
                ((RightStockViewHolder) holder).user_name.setVisibility(View.GONE);
            }

            showMessageTime(position, ((RightStockViewHolder) holder).message_time);
        } else if (holder instanceof LeftUnKonwViewHolder) {
            ((LeftUnKonwViewHolder) holder).user_name.setText(avimMessage.getFrom());
            showMessageTime(position, ((LeftUnKonwViewHolder) holder).message_time);
        } else if (holder instanceof RightUnKonwViewHolder) {
            ((RightUnKonwViewHolder) holder).user_name.setVisibility(View.GONE);
            showMessageTime(position, ((RightUnKonwViewHolder) holder).message_time);
        } else if (holder instanceof ChatGroupAddViewHolder) {
            showMessageTime(position, ((ChatGroupAddViewHolder) holder).message_time);
            ((ChatGroupAddViewHolder) holder).message_content.setText(contentObject.getString("_lctext"));
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        AVIMMessage message = messageList.get(position);

        JSONObject contentObject = JSON.parseObject(message.getContent());
        String _lctype = contentObject.getString("_lctype");
        boolean isYourSelf = message.getFrom().endsWith(UserUtils.getUserAccountId());
        KLog.e(contentObject);
        switch (_lctype) {
            case "-1":
                if (isYourSelf) {
                    return TYPE_RIGHT_TEXT_MESSAGE;
                } else {
                    return TYPE_LEFT_TEXT_MESSAGE;
                }
            case "-2":
                if (isYourSelf) {
                    return TYPE_RIGHT_IMAGE_MESSAGE;
                } else {
                    return TYPE_LEFT_IMAGE_MESSAGE;
                }
            case "-3":
                if (isYourSelf) {
                    return TYPE_RIGHT_VOICE_MESSAGE;
                } else {
                    return TYPE_LEFT_VOICE_MESSAGE;
                }
            case "5":
            case "6":
            case "8":
                return TYPE_SYSTEM_MESSAGE;
            case "11":
                if (isYourSelf) {
                    return TYPE_RIGHT_STOCK_MESSAGE;
                } else {
                    return TYPE_LEFT_STOCK_MESSAGE;
                }
            default:
                if (isYourSelf) {
                    return TYPE_RIGHT_UNKONW_MESSAGE;
                } else {
                    return TYPE_LEFT_UNKONW_MESSAGE;
                }
        }
    }

    /**
     * 图片大小计算
     */
    private void setImageSize(RelativeLayout.LayoutParams params, AVIMImageMessage message) {
        int maxWidth = (int) (AppDevice.getWidth(mContext) * 0.4);
        String rateText = "";

        if (null != message && 0 != ((Number) message.getFileMetaData().get("height")).intValue() && ((Number) message.getFileMetaData().get("width")).intValue() != 0) {
            int dpWidth = ((Number) message.getFileMetaData().get("width")).intValue();
            int dpHeight = ((Number) message.getFileMetaData().get("height")).intValue();
            if (dpWidth > maxWidth) {
                params.width = maxWidth;
                rateText = StringUtils.getAnyPointFloat("%.2f", (float) maxWidth / dpWidth);
                params.height = (int) (Float.parseFloat(rateText) * dpHeight);
            } else if (dpWidth < maxWidth) {
                params.width = dpWidth;
                params.height = dpHeight;
            }
        }

    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    //消息来了
    public void addItem(AVIMMessage message) {
        messageList.add(message);
    }

    //时间处理
    public boolean showTime(Long lastTime, Long rightNow) {
        Long timeDiffer = rightNow - lastTime;
        if (timeDiffer >= 5 * 60 * 1000) {
            return true;
        } else {
            return false;
        }
    }

    public void showMessageTime(int position, TextView view) {
        if (position == 0) {
            view.setVisibility(View.VISIBLE);
            view.setText(CalendarUtils.parseDateIMMessageFormat(messageList.get(position).getTimestamp()));
        } else if (position > 0) {
            if (showTime(messageList.get(position - 1).getTimestamp(), messageList.get(position).getTimestamp())) {
                view.setVisibility(View.VISIBLE);
                view.setText(CalendarUtils.parseDateIMMessageFormat(messageList.get(position).getTimestamp()));
            } else {
                view.setVisibility(View.GONE);
            }
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private static class IMCHatViewHolder extends RecyclerView.ViewHolder {

        protected ImageView user_head_photo;
        protected RelativeLayout user_layout;
        protected TextView user_name;
        protected TextView message_time;

        IMCHatViewHolder(View itemView) {
            super(itemView);

            user_name = (TextView) itemView.findViewById(R.id.user_name);
            message_time = (TextView) itemView.findViewById(R.id.message_time);
            user_head_photo = (ImageView) itemView.findViewById(R.id.user_head_photo);
            user_layout = (RelativeLayout) itemView.findViewById(R.id.user_layout);

            user_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("LEAN_CLOUD", "TextViewHolder" + getPosition());
                }
            });
        }
    }

    private class RightTextViewHolder extends IMCHatViewHolder {

        private TextView what_user_send;

        RightTextViewHolder(View itemView) {
            super(itemView);
            what_user_send = (TextView) itemView.findViewById(R.id.what_user_send);
        }

    }

    private class LeftTextViewHolder extends IMCHatViewHolder {

        private TextView what_user_send;

        LeftTextViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            what_user_send = (TextView) itemView.findViewById(R.id.what_user_send);
        }

    }

    private class RightImageViewHolder extends IMCHatViewHolder {

        private ImageView image_user_send;

        RightImageViewHolder(View itemView) {
            super(itemView);
            image_user_send = (ImageView) itemView.findViewById(R.id.image_user_send);
        }
    }

    private class LeftImageViewHolder extends IMCHatViewHolder {

        private ImageView image_user_send;

        LeftImageViewHolder(View itemView) {
            super(itemView);
            image_user_send = (ImageView) itemView.findViewById(R.id.image_user_send);
        }
    }

    private class RightAudioViewHolder extends IMCHatViewHolder {

        private FrameLayout recorder_length;
        private View animView;
        private TextView recorder_time;

        RightAudioViewHolder(View itemView) {
            super(itemView);
            recorder_length = (FrameLayout) itemView.findViewById(R.id.recorder_length);
            animView = itemView.findViewById(R.id.recorder_anim);
            recorder_time = (TextView) itemView.findViewById(R.id.recorder_time);
        }
    }

    private class LeftAudioViewHolder extends IMCHatViewHolder {

        private FrameLayout recorder_length;
        private View animView;
        private TextView recorder_time;

        LeftAudioViewHolder(View itemView) {
            super(itemView);
            recorder_length = (FrameLayout) itemView.findViewById(R.id.recorder_length);
            animView = itemView.findViewById(R.id.recorder_anim);
            recorder_time = (TextView) itemView.findViewById(R.id.recorder_time);
        }
    }

    private class RightStockViewHolder extends IMCHatViewHolder {

        private TextView what_user_send;

        RightStockViewHolder(View itemView) {
            super(itemView);
            what_user_send = (TextView) itemView.findViewById(R.id.what_user_send);
        }
    }

    private class LeftStockViewHolder extends IMCHatViewHolder {

        private TextView what_user_send;

        LeftStockViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            what_user_send = (TextView) itemView.findViewById(R.id.what_user_send);
        }

    }

    private class LeftUnKonwViewHolder extends IMCHatViewHolder {

        private TextView what_user_send;

        LeftUnKonwViewHolder(View itemView) {
            super(itemView);
            what_user_send = (TextView) itemView.findViewById(R.id.what_user_send);
        }

    }

    private class RightUnKonwViewHolder extends IMCHatViewHolder {

        private TextView what_user_send;

        RightUnKonwViewHolder(View itemView) {
            super(itemView);
            what_user_send = (TextView) itemView.findViewById(R.id.what_user_send);
        }
    }

    private class ChatGroupAddViewHolder extends IMCHatViewHolder {
        private TextView message_content;

        ChatGroupAddViewHolder(View itemView) {
            super(itemView);
            message_content = (TextView) itemView.findViewById(R.id.message_content);
        }
    }

}
