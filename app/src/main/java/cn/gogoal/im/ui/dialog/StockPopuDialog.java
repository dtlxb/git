package cn.gogoal.im.ui.dialog;

import android.view.View;
import android.widget.TextView;

import cn.gogoal.im.R;
import cn.gogoal.im.ui.dialog.base.BaseBottomDialog;

/**
 * author wangjd on 2017/2/28 0028.
 * Staff_id 1375
 * phone 18930640263
 *
 * 分享 封装弹窗
 */
public class StockPopuDialog extends BaseBottomDialog {

    @Override
    public int getLayoutRes() {
        return R.layout.diagnose_view;
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
                StockPopuDialog.this.dismiss();
            }
        });

    }
}
