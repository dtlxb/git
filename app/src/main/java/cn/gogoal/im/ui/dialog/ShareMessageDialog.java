package cn.gogoal.im.ui.dialog;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.roundimage.roundImage.RoundedImageView;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.ShareItemInfo;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.AvatarTakeListener;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.ui.dialog.base.BaseCentDailog;

/**
 * author wangjd on 2017/5/8 0008.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
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

        final RoundedImageView icon = (RoundedImageView) v.findViewById(R.id.item_contacts_iv_icon);
        TextView name = (TextView) v.findViewById(R.id.item_contacts_tv_nickname);
        TextView tvShareMsgDesc = (TextView) v.findViewById(R.id.tv_dialog_share_msg_desc);
        EditText etMessageInput = (EditText) v.findViewById(R.id.et_dialog_share_msg_input);

        TextView tvCancle = (TextView) v.findViewById(R.id.btn_dialog_share_msg_cancle);
        TextView tvSend = (TextView) v.findViewById(R.id.btn_dialog_share_msg_send);

        if (entity != null) {
            if (entity.getImMessageBean().getChatType() == AppConst.IM_CHAT_TYPE_SINGLE) {
                ImageDisplay.loadRoundedRectangleImage(v.getContext(), entity.getAvatar(), icon);
            } else if (entity.getImMessageBean().getChatType() == AppConst.IM_CHAT_TYPE_SQUARE) {
                ChatGroupHelper.setGroupAvatar(entity.getImMessageBean().getConversationID(), new AvatarTakeListener() {
                    @Override
                    public void success(Bitmap bitmap) {
                        icon.setImageBitmap(bitmap);
                    }

                    public void failed(Exception e) {
                    }
                });
            }
            name.setText(entity.getName());
            tvShareMsgDesc.setText(entity.getEntity().getDesc());
        }

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareMessageDialog.this.dismiss();
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送
                ChatGroupHelper.sendShareMessage(entity, new ChatGroupHelper.GroupInfoResponse() {
                    @Override
                    public void getInfoSuccess(JSONObject groupInfo) {
                        ShareMessageDialog.this.dismiss();
                        getActivity().finish();
                    }

                    @Override
                    public void getInfoFailed(Exception e) {

                    }
                });
            }
        });
    }
}
