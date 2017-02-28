package com.gogoal.app.common;

import android.view.View;

import com.gogoal.app.R;
import com.gogoal.app.ui.widget.ShareBottomDialog;

/**
 * author wangjd on 2017/2/28 0028.
 * Staff_id 1375
 * phone 18930640263
 */
public class ShareOnClick implements android.view.View.OnClickListener{
    private ShareBottomDialog dialog;
    private String url;
    private String imageUrl;
    private String title;
    private String description;

    public ShareOnClick(ShareBottomDialog dialog, String url, String imageUrl, String title, String description) {
        this.dialog = dialog;
        this.url = url;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dialog_share_gogoal:
                UIHelper.toast(view.getContext(), "Go-Goal好友");
                break;
            case R.id.tv_dialog_share_wx:
                UIHelper.toast(view.getContext(), "微信好友");
                UIHelper.WXshare(0, view.getContext(), url, imageUrl, title, description);
                break;
            case R.id.tv_dialog_share_wx_circle:
                UIHelper.toast(view.getContext(), "微信朋友圈");
                UIHelper.WXshare(1, view.getContext(), url, imageUrl, title, description);
                break;
            case R.id.tv_dialog_share_copy:
                AppDevice.copyTextToBoard(view.getContext(),url);
                UIHelper.toast(view.getContext(), "复制成功");
                break;
        }
        dialog.dismiss();
    }
}
