package cn.gogoal.im.ui.dialog;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.hply.roundimage.roundImage.RoundedImageView;

import java.io.File;

import cn.gogoal.im.R;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.bean.GGShareEntity;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.bean.ShareItemInfo;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.IMHelpers.MessageListUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.dialog.base.BaseCentDailog;

/**
 * author wangjd on 2017/5/8 0008.
 * Staff_id 1375
 * phone 18930640263
 * description :分享确认弹窗
 */
public class ShareMessageDialog extends BaseCentDailog {
    @Override
    public int getLayoutRes() {
        return R.layout.dialog_share_message;
    }

    @Override
    public int getDialogStyle() {
        return R.style.CenterShareDialog;
    }

    @Override
    public int getWidth() {
        return 78 * AppDevice.getWidth(getActivity()) / 100;
    }

    @Override
    public boolean getCancelOutside() {
        return false;
    }

    @Override
    public float getDimAmount() {
        return super.getDimAmount();
    }

    public static ShareMessageDialog newInstance(ShareItemInfo info) {
        ShareMessageDialog dialog = new ShareMessageDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("gg_share_list_info", info);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void bindView(View v) {
        final ShareItemInfo entity = (ShareItemInfo) getArguments().getSerializable("gg_share_list_info");
        if (entity == null) {
            return;
        }


        final RoundedImageView icon = (RoundedImageView) v.findViewById(R.id.item_contacts_iv_icon);
        TextView name = (TextView) v.findViewById(R.id.item_contacts_tv_nickname);
        TextView tvShareMsgDesc = (TextView) v.findViewById(R.id.tv_dialog_share_msg_desc);
        ImageView ivShare = (ImageView) v.findViewById(R.id.iv_dialog_share_image);


        try {
            Glide.with(getActivity()).load(new File(entity.getEntity().getImage())).into(ivShare);
        } catch (Exception e) {
        }

        String shareType = entity.getEntity().getShareType();

        tvShareMsgDesc.setVisibility(shareType.equalsIgnoreCase(GGShareEntity.SHARE_TYPE_IMAGE) ? View.GONE : View.VISIBLE);
        ivShare.setVisibility(shareType.equalsIgnoreCase(GGShareEntity.SHARE_TYPE_IMAGE) ? View.VISIBLE : View.GONE);

        EditText etMessageInput = (EditText) v.findViewById(R.id.et_dialog_share_msg_input);

        TextView tvCancel = (TextView) v.findViewById(R.id.btn_dialog_share_msg_cancle);
        TextView tvSend = (TextView) v.findViewById(R.id.btn_dialog_share_msg_send);

        Object avatar = entity.getAvatar();
        if (avatar instanceof Bitmap) {
            icon.setImageBitmap((Bitmap) avatar);
        } else if (avatar instanceof String) {
            ImageDisplay.loadRoundedRectangleImage(v.getContext(), avatar, icon);
        }

        name.setText(entity.getName());

        if (!TextUtils.isEmpty(entity.getEntity().getDesc())) {
            tvShareMsgDesc.setText(Html.fromHtml(entity.getEntity().getDesc()));
        }

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareMessageDialog.this.dismiss();
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.getEntity() != null) {
                    switch (entity.getEntity().getShareType()) {
                        case GGShareEntity.SHARE_TYPE_TEXT:
                            break;
                        case GGShareEntity.SHARE_TYPE_IMAGE:
                            ChatGroupHelper.sendImageMessage(
                                    entity.getImMessageBean().getConversationID(),
                                    entity.getImMessageBean().getChatType(),
                                    new File(entity.getEntity().getImage()), new ChatGroupHelper.MessageResponse() {
                                        @Override
                                        public void sendSuccess() {
                                            UIHelper.toast(getActivity(), "分享成功!");
                                            //消息缓存消息列表
                                            if (null != entity.getImMessageBean()) {

                                                IMMessageBean imMessageBean = new IMMessageBean(entity.getImMessageBean().getConversationID(),
                                                        entity.getImMessageBean().getChatType(), System.currentTimeMillis(), "0",
                                                        entity.getImMessageBean().getNickname(), String.valueOf(entity.getImMessageBean().getFriend_id()),
                                                        entity.getImMessageBean().getAvatar(), JSON.toJSONString(ChatGroupHelper.getImageMessage(entity.getEntity())),
                                                        entity.getImMessageBean().isMute());

                                                MessageListUtils.saveMessageInfo(imMessageBean);
                                                //通知服务器重新获取
                                                AppManager.getInstance().sendMessage("Cache_change");
                                            }
                                        }

                                        @Override
                                        public void sendFailed() {
                                            UIHelper.toast(getActivity(), "分享失败!");
                                        }
                                    });

                            ShareMessageDialog.this.dismiss();
                            getActivity().finish();
                            break;
                        default:
                            ChatGroupHelper.sendShareMessage(entity, null);
                            ShareMessageDialog.this.dismiss();
                            getActivity().finish();
                            break;
                    }
                }
            }
        });
    }
}
