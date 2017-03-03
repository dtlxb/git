package com.gogoal.app.ui.recorder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogoal.app.R;
import com.gogoal.app.common.AppDevice;
import com.gogoal.app.common.DialogHelp;

public class DialogManager {
    private Dialog mDialog;
    private ImageView mIcon;
    private ImageView mVoice;
    private TextView mLable;

    private Context mContext;

    public DialogManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 显示录音的对话框
     */
    public void showRecordingDialog(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_recorder,null);
        mDialog = DialogHelp.getWindoDialog(mContext,view, AppDevice.getWidth(mContext)/2,0);

        mIcon = (ImageView) mDialog.findViewById(R.id.recorder_dialog_icon);
        mVoice = (ImageView) mDialog.findViewById(R.id.recorder_dialog_voice);
        mLable = (TextView) mDialog.findViewById(R.id.tv_recorder_dialog_label);

        mDialog.show();
    }

    public void recording(){
        if(mDialog != null && mDialog.isShowing()) { //显示状态
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.recorder);
            mLable.setBackgroundColor(Color.TRANSPARENT);
            mLable.setText(mContext.getString(R.string.str_recorder_dialog_recording));
        }
    }

    /**
     * 显示想取消的对话框
     */
    public void wantToCancel(){
        if(mDialog != null && mDialog.isShowing()) { //显示状态
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.cancel);

            mLable.setText(mContext.getString(R.string.str_recorder_dialog_cancle));
            mLable.setBackgroundResource(R.drawable.shape_wechat_record_text_error);
        }
    }

    /**
     * 显示时间过短的对话框
     */
    public void tooShort(){
        if(mDialog != null && mDialog.isShowing()) { //显示状态
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.voice_to_short);
            mLable.setText(mContext.getString(R.string.str_recorder_dialog_22short));
        }
    }

    /**
     * 显示取消的对话框
     */
    public void dimissDialog(){
        if(mDialog != null && mDialog.isShowing()) { //显示状态
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 显示更新音量级别的对话框
     * @param level 1-7
     */
    public void updateVoiceLevel(int level){
        if(mDialog != null && mDialog.isShowing()) {
            int resId = mContext.getResources().getIdentifier("v"+level,"drawable",mContext.getPackageName());
            mVoice.setImageResource(resId);
        }
    }
}
