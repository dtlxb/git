package cn.gogoal.im.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.socks.library.KLog;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.StringUtils;
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
    //未知消息
    public static int TYPE_LEFT_UNKONW_MESSAGE = 0x07;
    public static int TYPE_RIGHT_UNKONW_MESSAGE = 0x08;

    private List<AVIMMessage> messageList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

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
        } else if (viewType == TYPE_RIGHT_UNKONW_MESSAGE) {
            return new LeftUnKonwViewHolder(mLayoutInflater.inflate(R.layout.item_right_audio, parent, false));
        } else if (viewType == TYPE_LEFT_UNKONW_MESSAGE) {
            return new RightUnKonwViewHolder(mLayoutInflater.inflate(R.layout.item_right_audio, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LeftTextViewHolder) {
            AVIMTextMessage textMessage = (AVIMTextMessage) messageList.get(position);
            ((LeftTextViewHolder) holder).user_name.setText((String) textMessage.getAttrs().get("username"));
            ((LeftTextViewHolder) holder).what_user_send.setText(textMessage.getText());
        } else if (holder instanceof RightTextViewHolder) {
            AVIMTextMessage textMessage = (AVIMTextMessage) messageList.get(position);
            ((RightTextViewHolder) holder).what_user_send.setText(textMessage.getText());
        } else if (holder instanceof LeftImageViewHolder) {
            AVIMImageMessage imageMessage = (AVIMImageMessage) messageList.get(position);
            ((LeftImageViewHolder) holder).user_name.setText((String) imageMessage.getAttrs().get("username"));

            //获取后台图片大小设置
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((LeftImageViewHolder) holder).image_user_send.getLayoutParams();

            setImageSize(params, imageMessage);

            ((LeftImageViewHolder) holder).image_user_send.setLayoutParams(params);

            ImageDisplay.loadNetImage(mContext, imageMessage.getAVFile().getUrl(), ((LeftImageViewHolder) holder).image_user_send);
        } else if (holder instanceof RightImageViewHolder) {
            AVIMImageMessage imageMessage = (AVIMImageMessage) messageList.get(position);

            //获取后台图片大小设置
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((RightImageViewHolder) holder).image_user_send.getLayoutParams();

            setImageSize(params, imageMessage);

            ((RightImageViewHolder) holder).image_user_send.setLayoutParams(params);

            ImageDisplay.loadNetImage(mContext, imageMessage.getAVFile().getUrl(), ((RightImageViewHolder) holder).image_user_send);
        } else if (holder instanceof RightAudioViewHolder) {
            final AVIMAudioMessage audioMessage = (AVIMAudioMessage) messageList.get(position);
            //设置语音宽度
            ViewGroup.LayoutParams params = ((RightAudioViewHolder) holder).recorder_length.getLayoutParams();
            int mMaxItemWidth = (int) (AppDevice.getWidth(mContext) * 0.7f);
            int mMinItemWidth = (int) (AppDevice.getWidth(mContext) * 0.16f);
            params.width = (int) (mMinItemWidth + (mMaxItemWidth / 60f) * ((Number) audioMessage.getFileMetaData().get("duration")).doubleValue());
            ((RightAudioViewHolder) holder).recorder_length.setLayoutParams(params);
            //设置语音时长
            ((RightAudioViewHolder) holder).recorder_time.setText((int) ((Number) audioMessage.getFileMetaData().get("duration")).doubleValue() + "\"");

            ((RightAudioViewHolder) holder).recorder_length.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //播放动画
                    ((RightAudioViewHolder) holder).animView.setBackgroundResource(R.drawable.play_anim);
                    AnimationDrawable anim = (AnimationDrawable) ((RightAudioViewHolder) holder).animView.getBackground();
                    anim.start();
                    //播放音频
                    MediaManager.playSound(audioMessage.getAVFile().getUrl(), new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            ((RightAudioViewHolder) holder).animView.setBackgroundResource(R.drawable.v_anim3);
                        }
                    });
                }
            });
        } else if (holder instanceof LeftAudioViewHolder) {
            final AVIMAudioMessage audioMessage = (AVIMAudioMessage) messageList.get(position);
            //设置语音宽度
            final ViewGroup.LayoutParams params = ((LeftAudioViewHolder) holder).recorder_length.getLayoutParams();
            int mMaxItemWidth = (int) (AppDevice.getWidth(mContext) * 0.7f);
            int mMinItemWidth = (int) (AppDevice.getWidth(mContext) * 0.16f);
            params.width = (int) (mMinItemWidth + (mMaxItemWidth / 60f) * ((Number) audioMessage.getFileMetaData().get("duration")).doubleValue());
            ((LeftAudioViewHolder) holder).recorder_length.setLayoutParams(params);
            //设置语音时长
            ((LeftAudioViewHolder) holder).recorder_time.setText((int) ((Number) audioMessage.getFileMetaData().get("duration")).doubleValue() + "\"");
            //点击播放语音
            ((LeftAudioViewHolder) holder).recorder_length.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (((LeftAudioViewHolder) holder).animView != null) {
                        ((LeftAudioViewHolder) holder).animView.setBackgroundResource(R.drawable.v_anim3);
                        ((LeftAudioViewHolder) holder).animView = null;
                    }*/
                    //播放动画
                    ((LeftAudioViewHolder) holder).animView.setBackgroundResource(R.drawable.play_anim);
                    AnimationDrawable anim = (AnimationDrawable) ((LeftAudioViewHolder) holder).animView.getBackground();
                    anim.start();
                    //播放音频
                    MediaManager.playSound(audioMessage.getAVFile().getUrl(), new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            ((LeftAudioViewHolder) holder).animView.setBackgroundResource(R.drawable.v_anim3);
                        }
                    });
                }
            });
        } else if (holder instanceof LeftUnKonwViewHolder) {
            AVIMMessage message = messageList.get(position);
            ((LeftUnKonwViewHolder) holder).user_name.setText(message.getFrom());
        } else if (holder instanceof RightUnKonwViewHolder) {
        }
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        AVIMMessage message = messageList.get(position);
        if (message.getFrom().equals(AppConst.LEAN_CLOUD_TOKEN)) {
            if (message instanceof AVIMTextMessage) {
                return TYPE_RIGHT_TEXT_MESSAGE;
            } else if (message instanceof AVIMImageMessage) {
                return TYPE_RIGHT_IMAGE_MESSAGE;
            } else if (message instanceof AVIMAudioMessage) {
                return TYPE_RIGHT_VOICE_MESSAGE;
            } else {
                return TYPE_RIGHT_UNKONW_MESSAGE;
            }
        } else {
            if (message instanceof AVIMTextMessage) {
                return TYPE_LEFT_TEXT_MESSAGE;
            } else if (message instanceof AVIMImageMessage) {
                return TYPE_LEFT_IMAGE_MESSAGE;
            } else if (message instanceof AVIMAudioMessage) {
                return TYPE_LEFT_VOICE_MESSAGE;
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

    public void addItem(AVIMMessage message) {
        messageList.add(message);
        this.notifyDataSetChanged();
    }

    private static class IMCHatViewHolder extends RecyclerView.ViewHolder {

        protected ImageView user_head_photo;
        protected RelativeLayout user_layout;
        protected TextView user_name;

        public IMCHatViewHolder(View itemView) {
            super(itemView);

            user_name = (TextView) itemView.findViewById(R.id.user_name);
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

        public RightTextViewHolder(View itemView) {
            super(itemView);
            what_user_send = (TextView) itemView.findViewById(R.id.what_user_send);
        }

    }

    private class LeftTextViewHolder extends IMCHatViewHolder {

        private TextView what_user_send;

        public LeftTextViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            what_user_send = (TextView) itemView.findViewById(R.id.what_user_send);
        }

    }

    private class RightImageViewHolder extends IMCHatViewHolder {

        private ImageView image_user_send;

        public RightImageViewHolder(View itemView) {
            super(itemView);
            image_user_send = (ImageView) itemView.findViewById(R.id.image_user_send);
        }
    }

    private class LeftImageViewHolder extends IMCHatViewHolder {

        private ImageView image_user_send;

        public LeftImageViewHolder(View itemView) {
            super(itemView);
            image_user_send = (ImageView) itemView.findViewById(R.id.image_user_send);
        }
    }

    private class RightAudioViewHolder extends IMCHatViewHolder {

        private FrameLayout recorder_length;
        private View animView;
        private TextView recorder_time;

        public RightAudioViewHolder(View itemView) {
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

        public LeftAudioViewHolder(View itemView) {
            super(itemView);
            recorder_length = (FrameLayout) itemView.findViewById(R.id.recorder_length);
            animView = itemView.findViewById(R.id.recorder_anim);
            recorder_time = (TextView) itemView.findViewById(R.id.recorder_time);
        }
    }

    private class LeftUnKonwViewHolder extends IMCHatViewHolder {

        private TextView what_user_send;

        public LeftUnKonwViewHolder(View itemView) {
            super(itemView);
            what_user_send = (TextView) itemView.findViewById(R.id.what_user_send);
        }

    }

    private class RightUnKonwViewHolder extends IMCHatViewHolder {

        private TextView what_user_send;

        public RightUnKonwViewHolder(View itemView) {
            super(itemView);
            what_user_send = (TextView) itemView.findViewById(R.id.what_user_send);
        }
    }

}
