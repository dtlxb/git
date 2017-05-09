package cn.gogoal.im.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.ShareItemInfo;
import cn.gogoal.im.common.AppDevice;
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
        ShareItemInfo entity = (ShareItemInfo) getArguments().getSerializable("gg_share_list_info");

        ImageView icon = (ImageView) v.findViewById(R.id.item_contacts_iv_icon);
        TextView name = (TextView) v.findViewById(R.id.item_contacts_tv_nickname);
        TextView tvShareMsgDesc = (TextView) v.findViewById(R.id.tv_dialog_share_msg_desc);
        EditText etMessageInput = (EditText) v.findViewById(R.id.et_dialog_share_msg_input);

        TextView tvCancle = (TextView) v.findViewById(R.id.btn_dialog_share_msg_cancle);
        TextView tvSend = (TextView) v.findViewById(R.id.btn_dialog_share_msg_send);

        if (entity != null) {
            try {
                ImageDisplay.loadRoundedRectangleImage(v.getContext(), entity.getAvatar(), icon);
            } catch (Exception e) {
                ImageDisplay.loadImage(v.getContext(), entity.getAvatar(), icon);
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
                //ChatGroupHelper.sendShareMessage();
            }
        });
    }
}
