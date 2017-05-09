package cn.gogoal.im.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.GGShareEntity;
import cn.gogoal.im.bean.ShareListBean;
import cn.gogoal.im.common.AppDevice;
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

    public static ShareMessageDialog newInstance(GGShareEntity entity, ShareListBean data) {
        ShareMessageDialog dialog = new ShareMessageDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("gg_share_entity", entity);
        bundle.putSerializable("gg_share_list_bean", data);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void bindView(View v) {
        GGShareEntity entity = getArguments().getParcelable("gg_share_entity");
        final ShareListBean data = (ShareListBean) getArguments().getSerializable("gg_share_list_bean");

        ImageView icon = (ImageView) v.findViewById(R.id.item_contacts_iv_icon);
        TextView name = (TextView) v.findViewById(R.id.item_contacts_tv_nickname);
        TextView tvShareMsgDesc = (TextView) v.findViewById(R.id.tv_dialog_share_msg_desc);
        EditText etMessageInput = (EditText) v.findViewById(R.id.et_dialog_share_msg_input);

        TextView tvCancle = (TextView) v.findViewById(R.id.btn_dialog_share_msg_cancle);
        TextView tvSend = (TextView) v.findViewById(R.id.btn_dialog_share_msg_send);

        if (data != null) {
            ImageDisplay.loadRoundedRectangleImage(v.getContext(), data.getItemImage(), icon);
            name.setText(data.getText());

        }
        if (entity != null) {
            tvShareMsgDesc.setText(entity.getDesc());
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
            }
        });
    }
}
