package cn.gogoal.im.ui.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.gogoal.im.R;
import cn.gogoal.im.common.ShareOnClick;

/**
 * author wangjd on 2017/2/28 0028.
 * Staff_id 1375
 * phone 18930640263
 */
public class ShareBottomDialog extends BaseBottomDialog {

    public static ShareBottomDialog getInstance(String url, String imageUrl, String title, String description) {
        ShareBottomDialog dialog = new ShareBottomDialog();
        Bundle bundle = new Bundle();
        bundle.putString("share_url", url);
        bundle.putString("share_imageUrl", imageUrl);
        bundle.putString("share_title", title);
        bundle.putString("share_description", description);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_share_layout;
    }

    @Override
    public void bindView(View v) {
        String url = getArguments().getString("share_url");
        String imageUrl = getArguments().getString("share_imageUrl");
        String title = getArguments().getString("share_title");
        String description = getArguments().getString("share_description");
        ;

        TextView tvShareGoGoal = (TextView) v.findViewById(R.id.tv_dialog_share_gogoal);
        TextView tvShareWeChat = (TextView) v.findViewById(R.id.tv_dialog_share_wx);
        TextView tvShareWeChatCircle = (TextView) v.findViewById(R.id.tv_dialog_share_wx_circle);
        TextView tvShareCopyUrl = (TextView) v.findViewById(R.id.tv_dialog_share_copy);
        TextView tvShareCancle = (TextView) v.findViewById(R.id.tv_dialog_cancle);

        tvShareCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareBottomDialog.this.dismiss();
            }
        });

        tvShareGoGoal.setOnClickListener(new ShareOnClick(this, url, imageUrl, title, description));
        tvShareWeChat.setOnClickListener(new ShareOnClick(this, url, imageUrl, title, description));
        tvShareWeChatCircle.setOnClickListener(new ShareOnClick(this, url, imageUrl, title, description));
        tvShareCopyUrl.setOnClickListener(new ShareOnClick(this, url, imageUrl, title, description));
    }
}
