package com.gogoal.app.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.gogoal.app.R;
import com.gogoal.app.common.AppConst;
import com.gogoal.app.common.AppDevice;
import com.gogoal.app.common.ImageUtils.ImageDisplay;

import java.io.IOException;
import java.util.List;

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
    //cache_chat_img_voice
    public static int TYPE_LEFT_VOICE_MESSAGE = 0x05;
    public static int TYPE_RIGHT_VOICE_MESSAGE = 0x06;

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
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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

            Log.e("+++image", "MessageHeight:" + imageMessage.getHeight() + "---" + "MessageWidth:" + imageMessage.getWidth());

            setImageSize(params, imageMessage);

            Log.e("+++params", "paramsHeight:" + params.height + "---" + "paramsWidth:" + params.width);

            ((LeftImageViewHolder) holder).image_user_send.setLayoutParams(params);

            ImageDisplay.loadNetImage(mContext, imageMessage.getFileUrl(), ((LeftImageViewHolder) holder).image_user_send);
        } else if (holder instanceof RightImageViewHolder) {
            AVIMImageMessage imageMessage = (AVIMImageMessage) messageList.get(position);

            //获取后台图片大小设置
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((RightImageViewHolder) holder).image_user_send.getLayoutParams();

            setImageSize(params, imageMessage);

            ((RightImageViewHolder) holder).image_user_send.setLayoutParams(params);

            ImageDisplay.loadNetImage(mContext, imageMessage.getFileUrl(), ((RightImageViewHolder) holder).image_user_send);
        } else if (holder instanceof RightAudioViewHolder) {
            AVIMAudioMessage audioMessage = (AVIMAudioMessage) messageList.get(position);

            ((RightAudioViewHolder) holder).audio_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (holder instanceof LeftAudioViewHolder) {
            AVIMAudioMessage audioMessage = (AVIMAudioMessage) messageList.get(position);
            final MediaPlayer player = new MediaPlayer();
            try {
                player.setDataSource(audioMessage.getFileUrl());
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((LeftAudioViewHolder) holder).audio_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player.start();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        AVIMMessage message = messageList.get(position);

        if (messageList.get(position).getFrom().equals(AppConst.LEAN_CLOUD_TOKEN)) {
            if (message instanceof AVIMTextMessage) {
                return TYPE_RIGHT_TEXT_MESSAGE;
            } else if (message instanceof AVIMImageMessage) {
                return TYPE_RIGHT_IMAGE_MESSAGE;
            } else if (message instanceof AVIMAudioMessage) {
                return TYPE_RIGHT_VOICE_MESSAGE;
            } else {
                return 0;
            }
        } else {
            if (message instanceof AVIMTextMessage) {
                return TYPE_LEFT_TEXT_MESSAGE;
            } else if (message instanceof AVIMImageMessage) {
                return TYPE_LEFT_IMAGE_MESSAGE;
            } else if (message instanceof AVIMAudioMessage) {
                return TYPE_LEFT_VOICE_MESSAGE;
            } else {
                return 0;
            }
        }
    }

    /**
     * 图片大小计算
     */
    private void setImageSize(RelativeLayout.LayoutParams params, AVIMImageMessage message) {
        int maxWidth = (int) (AppDevice.getWidth(mContext) * 0.5);
        int maxHeight = AppDevice.dp2px(mContext, 150);

        if (0 != message.getWidth() && message.getHeight() != 0) {
            int dpWidth = AppDevice.px2dp(mContext, message.getWidth());
            int dpHeight = AppDevice.px2dp(mContext, message.getHeight());

            if (dpWidth > maxWidth && dpHeight < maxHeight) {
                params.width = maxWidth;
                params.height = (dpHeight / dpWidth) * maxHeight;
            } else if (dpHeight > maxHeight && dpWidth < maxWidth) {
                params.height = maxHeight;
                params.width = (dpWidth / dpHeight) * maxWidth;
            } else if (dpHeight > maxHeight && dpWidth > maxWidth) {
                params.height = maxHeight;
                params.width = maxWidth;
            }
        } else {
            params.height = maxHeight;
            params.width = maxWidth;
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

        private Button audio_button;

        public RightAudioViewHolder(View itemView) {
            super(itemView);
            audio_button = (Button) itemView.findViewById(R.id.audio_button);
        }
    }

    private class LeftAudioViewHolder extends IMCHatViewHolder {

        private Button audio_button;

        public LeftAudioViewHolder(View itemView) {
            super(itemView);
            audio_button = (Button) itemView.findViewById(R.id.audio_button);
        }
    }

}
