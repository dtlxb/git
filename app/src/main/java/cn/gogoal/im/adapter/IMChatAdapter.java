package cn.gogoal.im.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.IMPersonDetailActivity;
import cn.gogoal.im.activity.ImageDetailActivity;
import cn.gogoal.im.activity.WatchLiveActivity;
import cn.gogoal.im.activity.copy.CopyStockDetailActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.IMHelpers.MessageUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.ImageUtils.UFileImageHelper;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.recording.MediaManager;

/**
 * Created by huangxx on 2017/2/27.
 */

public class IMChatAdapter extends RecyclerView.Adapter {

    //文字
    private static int TYPE_LEFT_TEXT_MESSAGE = 0x01;
    private static int TYPE_RIGHT_TEXT_MESSAGE = 0x02;
    //图片
    private static int TYPE_LEFT_IMAGE_MESSAGE = 0x03;
    private static int TYPE_RIGHT_IMAGE_MESSAGE = 0x04;
    //语音
    private static int TYPE_LEFT_VOICE_MESSAGE = 0x05;
    private static int TYPE_RIGHT_VOICE_MESSAGE = 0x06;
    //股票
    private static int TYPE_LEFT_STOCK_MESSAGE = 0x07;
    private static int TYPE_RIGHT_STOCK_MESSAGE = 0x08;
    //未知
    private static int TYPE_LEFT_UNKONW_MESSAGE = 0x09;
    private static int TYPE_RIGHT_UNKONW_MESSAGE = 0x10;
    //分享
    private static int TYPE_LEFT_NORMOAL_SHARE = 0x11;
    private static int TYPE_RIGHT_NORMOAL_SHARE = 0x12;
    //系统
    private static int TYPE_SYSTEM_MESSAGE = 0x13;
    private List<AVIMMessage> messageList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int chatType;
    private Boolean isYourSelf;

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
        } else if (viewType == TYPE_RIGHT_NORMOAL_SHARE) {
            return new RightShareViewHolder(mLayoutInflater.inflate(R.layout.item_right_share, parent, false));
        } else if (viewType == TYPE_LEFT_NORMOAL_SHARE) {
            return new LeftShareViewHolder(mLayoutInflater.inflate(R.layout.item_left_share, parent, false));
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final AVIMMessage avimMessage = messageList.get(position);
        JSONObject contentObject = JSON.parseObject(avimMessage.getContent());
        final JSONObject lcattrsObject = (JSONObject) contentObject.get("_lcattrs");
        String messageType = contentObject.getString("_lctype");
        String headPicUrl;
        if (!messageType.equals("5") && !messageType.equals("6") && !messageType.equals("8")) {
            if (chatType == 1001) {
                ((IMCHatViewHolder) holder).user_name.setVisibility(View.GONE);
            } else if (chatType == 1002) {
                ((IMCHatViewHolder) holder).user_name.setVisibility(View.VISIBLE);
            } else {
                ((IMCHatViewHolder) holder).user_name.setVisibility(View.VISIBLE);
            }
            //头像
            if (isYourSelf) {
                headPicUrl = UserUtils.getUserAvatar();
            } else {
                if (!TextUtils.isEmpty(MessageUtils.getItsHeadPic(Integer.parseInt(avimMessage.getFrom()), chatType, ""))) {
                    headPicUrl = MessageUtils.getItsHeadPic(Integer.parseInt(avimMessage.getFrom()), chatType, "");
                } else {
                    headPicUrl = (String) lcattrsObject.get("avatar");
                }
            }
            ImageDisplay.loadRoundedRectangleImage(
                    mContext,
                    UFileImageHelper.load(headPicUrl).compress(33).get(),
                    ((IMCHatViewHolder) holder).user_head_photo);
            //点击头像展开详情
            ((IMCHatViewHolder) holder).user_head_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, IMPersonDetailActivity.class);
                    intent.putExtra("account_id", Integer.parseInt(avimMessage.getFrom()));
                    mContext.startActivity(intent);
                }
            });
        } else {
        }
        if (holder instanceof LeftTextViewHolder) {
            final AVIMTextMessage textMessage = (AVIMTextMessage) avimMessage;
            ((LeftTextViewHolder) holder).user_name.setText((String) textMessage.getAttrs().get("username"));
            showMessageTime(position, ((LeftTextViewHolder) holder).message_time);
            SpannableString spannableString = StringUtils.isOurEmoji(mContext, textMessage.getText(), AppDevice.sp2px(mContext,
                    ((LeftTextViewHolder) holder).what_user_send.getTextSize() / 2));
            ((LeftTextViewHolder) holder).what_user_send.setText(spannableString);
            //复制消息
            ((LeftTextViewHolder) holder).what_user_send.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    textClickAction(textMessage);
                    return false;
                }
            });

        } else if (holder instanceof RightTextViewHolder) {
            final AVIMTextMessage textMessage = (AVIMTextMessage) avimMessage;
            ((RightTextViewHolder) holder).user_name.setVisibility(View.GONE);
            showMessageTime(position, ((RightTextViewHolder) holder).message_time);
            SpannableString spannableString = StringUtils.isOurEmoji(mContext, textMessage.getText(), AppDevice.sp2px(mContext,
                    ((RightTextViewHolder) holder).what_user_send.getTextSize() / 2));
            ((RightTextViewHolder) holder).what_user_send.setText(spannableString);
            //复制消息
            ((RightTextViewHolder) holder).what_user_send.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    textClickAction(textMessage);
                    return false;
                }
            });
        } else if (holder instanceof LeftImageViewHolder) {
            final AVIMImageMessage imageMessage = (AVIMImageMessage) avimMessage;
            ((LeftImageViewHolder) holder).user_name.setText((String) imageMessage.getAttrs().get("username"));

            //获取后台图片大小设置
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((LeftImageViewHolder) holder).image_user_send.getLayoutParams();
            setImageSize(params, imageMessage);
            ((LeftImageViewHolder) holder).image_user_send.setLayoutParams(params);
            ImageDisplay.loadImage(mContext, imageMessage.getAVFile().getUrl(), ((LeftImageViewHolder) holder).image_user_send);
            showMessageTime(position, ((LeftImageViewHolder) holder).message_time);

            ((LeftImageViewHolder) holder).image_user_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageClickAction(imageMessage);
                }
            });

        } else if (holder instanceof RightImageViewHolder) {
            final AVIMImageMessage imageMessage = (AVIMImageMessage) avimMessage;
            ((RightImageViewHolder) holder).user_name.setVisibility(View.GONE);
            //获取后台图片大小设置
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((RightImageViewHolder) holder).image_user_send.getLayoutParams();
            setImageSize(params, imageMessage);
            ((RightImageViewHolder) holder).image_user_send.setLayoutParams(params);
            ImageDisplay.loadImage(mContext, imageMessage.getAVFile().getUrl(), ((RightImageViewHolder) holder).image_user_send);
            showMessageTime(position, ((RightImageViewHolder) holder).message_time);

            ((RightImageViewHolder) holder).image_user_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageClickAction(imageMessage);
                }
            });

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
                final String stockCode = lcattrsObject.getString("stockCode");
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder("$ " + stockCode + " " + lcattrsObject.getString("stockName"));
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#f53f3f")); // 设置字体颜色
                stringBuilder.setSpan(fcs, 2, stockCode.length() + 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                ((LeftStockViewHolder) holder).what_user_send.setText(stringBuilder);
                ((LeftStockViewHolder) holder).user_name.setText(lcattrsObject.getString("username"));

                ((LeftStockViewHolder) holder).what_user_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, CopyStockDetailActivity.class);
                        intent.putExtra("stock_code", stockCode);
                        intent.putExtra("stock_name", lcattrsObject.getString("stockName"));
                        mContext.startActivity(intent);
                    }
                });
            }

            showMessageTime(position, ((LeftStockViewHolder) holder).message_time);
        } else if (holder instanceof RightStockViewHolder) {
            if (null != lcattrsObject) {
                final String stockCode = lcattrsObject.getString("stockCode");
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder("$ " + stockCode + " " + lcattrsObject.getString("stockName"));
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#f53f3f")); // 设置字体颜色
                stringBuilder.setSpan(fcs, 2, stockCode.length() + 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                ((RightStockViewHolder) holder).what_user_send.setText(stringBuilder);
                ((RightStockViewHolder) holder).user_name.setVisibility(View.GONE);

                ((RightStockViewHolder) holder).what_user_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, CopyStockDetailActivity.class);
                        intent.putExtra("stock_code", stockCode);
                        intent.putExtra("stock_name", lcattrsObject.getString("stockName"));
                        mContext.startActivity(intent);
                    }
                });
            }

            showMessageTime(position, ((RightStockViewHolder) holder).message_time);
        } else if (holder instanceof LeftShareViewHolder) {
            ((LeftShareViewHolder) holder).user_name.setText(avimMessage.getFrom());
            getLayoutSize(((LeftShareViewHolder) holder).user_layout);
            if (lcattrsObject.getString("toolType").equals("1")) {
                ((LeftShareViewHolder) holder).layout_normal.setVisibility(View.VISIBLE);
                ((LeftShareViewHolder) holder).live_layout.setVisibility(View.GONE);
                ((LeftShareViewHolder) holder).tv_share_title.setText(lcattrsObject.getString("title"));
                ((LeftShareViewHolder) holder).tv_share.setText(lcattrsObject.getString("content"));
                ImageDisplay.loadImage(mContext, lcattrsObject.getString("thumUrl"), ((LeftShareViewHolder) holder).iv_share);
            } else if (lcattrsObject.getString("toolType").equals("2")) {
                ((LeftShareViewHolder) holder).layout_normal.setVisibility(View.GONE);
                ((LeftShareViewHolder) holder).live_layout.setVisibility(View.VISIBLE);
                ((LeftShareViewHolder) holder).tv_share_title.setText(lcattrsObject.getString("title"));
                ((LeftShareViewHolder) holder).tv_live_share.setText(lcattrsObject.getString("content"));
                getImageSize(((LeftShareViewHolder) holder).iv_live_share);
                ImageDisplay.loadImage(mContext, lcattrsObject.getString("thumUrl"), ((LeftShareViewHolder) holder).iv_live_share);
            }

            ((LeftShareViewHolder) holder).card_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareAction(lcattrsObject);
                }
            });
            showMessageTime(position, ((LeftShareViewHolder) holder).message_time);
        } else if (holder instanceof RightShareViewHolder) {
            getLayoutSize(((RightShareViewHolder) holder).user_layout);
            ((RightShareViewHolder) holder).user_name.setVisibility(View.GONE);
            if (lcattrsObject.getString("toolType").equals("1")) {
                ((RightShareViewHolder) holder).layout_normal.setVisibility(View.VISIBLE);
                ((RightShareViewHolder) holder).live_layout.setVisibility(View.GONE);
                ((RightShareViewHolder) holder).tv_share_title.setText(lcattrsObject.getString("title"));
                ((RightShareViewHolder) holder).tv_share.setText(lcattrsObject.getString("content"));
                ImageDisplay.loadImage(mContext, lcattrsObject.getString("thumUrl"), ((RightShareViewHolder) holder).iv_share);
            } else if (lcattrsObject.getString("toolType").equals("2")) {
                ((RightShareViewHolder) holder).layout_normal.setVisibility(View.GONE);
                ((RightShareViewHolder) holder).live_layout.setVisibility(View.VISIBLE);
                ((RightShareViewHolder) holder).tv_share_title.setText(lcattrsObject.getString("title"));
                ((RightShareViewHolder) holder).tv_live_share.setText(lcattrsObject.getString("content"));
                getImageSize(((RightShareViewHolder) holder).iv_live_share);
                ImageDisplay.loadImage(mContext, lcattrsObject.getString("thumUrl"), ((RightShareViewHolder) holder).iv_live_share);
            }

            showMessageTime(position, ((RightShareViewHolder) holder).message_time);
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

    private void getLayoutSize(RelativeLayout relativeLayout) {
        ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
        params.width = (AppDevice.getWidth(mContext) * 3) / 5;
        relativeLayout.setLayoutParams(params);
    }

    private void getImageSize(ImageView iv_share) {
        ViewGroup.LayoutParams params = iv_share.getLayoutParams();
        params.width = (AppDevice.getWidth(mContext) * 3) / 5 - AppDevice.dp2px(mContext, 28);
        params.height = (int) (params.width / 1.82);
        iv_share.setLayoutParams(params);
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
        isYourSelf = message.getFrom().equals(UserUtils.getMyAccountId());
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
            case "13":
                if (isYourSelf) {
                    return TYPE_RIGHT_NORMOAL_SHARE;
                } else {
                    return TYPE_LEFT_NORMOAL_SHARE;
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
        String rateText;

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

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    //消息来了
    public void addItem(AVIMMessage message) {
        messageList.add(message);
    }

    //时间处理
    private boolean showTime(Long lastTime, Long rightNow) {
        Long timeDiffer = rightNow - lastTime;
        return timeDiffer >= 5 * 60 * 1000;
    }

    private void showMessageTime(int position, TextView view) {
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


    private void shareAction(JSONObject jsonObject) {
        if (jsonObject.getString("toolType").equals("1")) {
            NormalIntentUtils.go2WebActivity(mContext, jsonObject.getString("link"), jsonObject.getString("title"));
        } else if (jsonObject.getString("toolType").equals("2")) {
            Intent intent = new Intent(mContext, WatchLiveActivity.class);
            intent.putExtra("live_id", jsonObject.getString("live_id"));
            mContext.startActivity(intent);
        }
    }


    private void imageClickAction(AVIMImageMessage imageMessage) {
        List<String> urls = new ArrayList<>();
        urls.add(imageMessage.getAVFile().getUrl());
        Intent intent = new Intent(mContext, ImageDetailActivity.class);
        intent.putStringArrayListExtra("image_urls", (ArrayList<String>) urls);
        intent.putExtra("account_Id", "");
        mContext.startActivity(intent);
    }

    private void textClickAction(final AVIMTextMessage imageMessage) {
        DialogHelp.getSelectDialog(mContext, "", new String[]{"复制文字"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(imageMessage.getText());
            }
        }, false).show();
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

    private class LeftShareViewHolder extends IMCHatViewHolder {
        private ImageView iv_share;
        private TextView tv_share;
        private TextView tv_share_title;
        private RelativeLayout layout_normal;

        private ImageView iv_live_share;
        private TextView tv_live_share;
        private LinearLayout live_layout;

        private LinearLayout card_layout;

        LeftShareViewHolder(View itemView) {
            super(itemView);
            iv_share = (ImageView) itemView.findViewById(R.id.iv_share);
            tv_share = (TextView) itemView.findViewById(R.id.tv_share);
            tv_share_title = (TextView) itemView.findViewById(R.id.tv_share_title);
            layout_normal = (RelativeLayout) itemView.findViewById(R.id.layout_normal);

            iv_live_share = (ImageView) itemView.findViewById(R.id.iv_live_share);
            tv_live_share = (TextView) itemView.findViewById(R.id.tv_live_share);
            live_layout = (LinearLayout) itemView.findViewById(R.id.live_layout);
            card_layout = (LinearLayout) itemView.findViewById(R.id.card_layout);
        }

    }

    private class RightShareViewHolder extends IMCHatViewHolder {
        private ImageView iv_share;
        private TextView tv_share;
        private TextView tv_share_title;
        private RelativeLayout layout_normal;

        private ImageView iv_live_share;
        private TextView tv_live_share;
        private LinearLayout live_layout;

        private LinearLayout card_layout;

        RightShareViewHolder(View itemView) {
            super(itemView);
            iv_share = (ImageView) itemView.findViewById(R.id.iv_share);
            tv_share = (TextView) itemView.findViewById(R.id.tv_share);
            tv_share_title = (TextView) itemView.findViewById(R.id.tv_share_title);
            layout_normal = (RelativeLayout) itemView.findViewById(R.id.layout_normal);

            iv_live_share = (ImageView) itemView.findViewById(R.id.iv_live_share);
            tv_live_share = (TextView) itemView.findViewById(R.id.tv_live_share);
            live_layout = (LinearLayout) itemView.findViewById(R.id.live_layout);
            card_layout = (LinearLayout) itemView.findViewById(R.id.card_layout);
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
