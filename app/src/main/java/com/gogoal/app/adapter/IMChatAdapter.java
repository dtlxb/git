package com.gogoal.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.gogoal.app.R;
import com.gogoal.app.common.AppConst;

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
    //语音
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
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AVIMTextMessage textMessage = (AVIMTextMessage) messageList.get(position);
        if (holder instanceof LeftTextViewHolder) {
            ((LeftTextViewHolder) holder).user_name.setText((String) textMessage.getAttrs().get("username"));
            ((LeftTextViewHolder) holder).what_user_send.setText(textMessage.getText());
        } else if (holder instanceof RightTextViewHolder) {
            ((RightTextViewHolder) holder).what_user_send.setText(textMessage.getText());
        }
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getFrom().equals(AppConst.LEAN_CLOUD_TOKEN)) {
            return TYPE_RIGHT_TEXT_MESSAGE;
        } else {
            return TYPE_LEFT_TEXT_MESSAGE;
        }
    }

    public void addItem(AVIMMessage message) {
        messageList.add(message);
        this.notifyDataSetChanged();
    }

    /*public static class TextViewHolder extends RecyclerView.ViewHolder {
        private ImageView user_head_photo;
        private RelativeLayout user_layout;
        private TextView user_name;
        private TextView what_user_send;

        public TextViewHolder(View itemView) {
            super(itemView);
            user_head_photo = (ImageView) itemView.findViewById(R.id.user_head_photo);
            user_layout = (RelativeLayout) itemView.findViewById(R.id.user_layout);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            what_user_send = (TextView) itemView.findViewById(R.id.what_user_send);

            user_head_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("LEAN_CLOUD", "TextViewHolder" + getPosition());
                }
            });
        }
    }*/

    private static class IMCHatViewHolder extends RecyclerView.ViewHolder {

        public ImageView user_head_photo;
        public RelativeLayout user_layout;

        public IMCHatViewHolder(View itemView) {
            super(itemView);

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

        private TextView user_name;
        private TextView what_user_send;

        public RightTextViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            what_user_send = (TextView) itemView.findViewById(R.id.what_user_send);
        }

    }

    private class LeftTextViewHolder extends IMCHatViewHolder {

        private TextView user_name;
        private TextView what_user_send;

        public LeftTextViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            what_user_send = (TextView) itemView.findViewById(R.id.what_user_send);
        }

    }

}
