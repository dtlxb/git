package cn.gogoal.im.common.recording;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.gogoal.im.R;


public class DialogManager {
    private Dialog mDialog;
    private ImageView mIcon;
    private ImageView mVoiceLevel;
    private TextView mLable;

    private Context mContext;

    public DialogManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 显示录音的对话框
     */
    public void showRecordingDialog() {
        mDialog = new Dialog(mContext, R.style.WindowDialogStyle);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_recorder, new LinearLayout(mContext), false);
        mDialog.setContentView(view);

        mIcon = (ImageView) mDialog.findViewById(R.id.recorder_dialog_icon);
        mVoiceLevel = (ImageView) mDialog.findViewById(R.id.recorder_dialog_voice_level);
        mLable = (TextView) mDialog.findViewById(R.id.tv_recorder_dialog_label);

        mDialog.show();
    }

    public void recording() {
        if (mDialog != null && mDialog.isShowing()) { //显示状态
            mIcon.setVisibility(View.VISIBLE);
            mVoiceLevel.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.recorder);
            mLable.setText(mContext.getString(R.string.str_recorder_dialog_recording));
            mLable.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /**
     * 显示想取消的对话框
     */
    public void wantToCancel() {
        if (mDialog != null && mDialog.isShowing()) { //显示状态
            mIcon.setVisibility(View.VISIBLE);
            mVoiceLevel.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.voice_cancel);
            mLable.setText(mContext.getString(R.string.str_recorder_dialog_cancle));
            mLable.setBackgroundResource(R.drawable.shape_wechat_record_text_error);
        }
    }

    /**
     * 显示时间过短的对话框
     */
    public void showError(int type) {
        if (mDialog != null && mDialog.isShowing()) { //显示状态
            mIcon.setVisibility(View.VISIBLE);
            mVoiceLevel.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.voice_to_short);
            if (type == 0) {
                mLable.setText(mContext.getString(R.string.str_recorder_dialog_2short));
            } else if (type == 1) {
                mLable.setText(mContext.getString(R.string.str_recorder_dialog_2long));
            }
        }
    }

    /**
     * 显示取消的对话框
     */
    public void dimissDialog() {
        if (mDialog != null && mDialog.isShowing()) { //显示状态
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 显示更新音量级别的对话框
     *
     * @param level 1-7
     */
    public void updateVoiceLevel(int level) {
        if (mDialog != null && mDialog.isShowing()) {
         /* mIcon.setVisibility(View.VISIBLE);
            mVoiceLevel.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);*/

            int resId = mContext.getResources().getIdentifier("v" + level, "drawable", mContext.getPackageName());
            mVoiceLevel.setImageResource(resId);
        }
    }
}
