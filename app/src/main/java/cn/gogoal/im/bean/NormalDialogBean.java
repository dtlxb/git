package cn.gogoal.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author wangjd on 2017/6/22 0022.
 * Staff_id 1375
 * phone 18930640263
 * description :普通弹窗数据
 *
 * @see cn.gogoal.im.ui.dialog.NormalAlertDialog
 */
public class NormalDialogBean implements Parcelable{

    private String okText;

    private String cancleText;

    private String messageText;

    /**
     * 双参数
     *
     * @param messageText:弹窗提示文本
     */
    public NormalDialogBean(String messageText) {
        this.messageText = messageText;
    }

    /**
     * 三参数
     *
     * @param messageText:弹窗提示文本
     * @param okText                   确定按钮文本文字
     */
    public NormalDialogBean(String messageText, String okText) {
        this.messageText = messageText;
        this.okText = okText;
    }

    /**
     * 全参
     * */
    public NormalDialogBean(String okText, String cancleText, String messageText) {
        this.okText = okText;
        this.cancleText = cancleText;
        this.messageText = messageText;
    }

    public String getOkText() {
        return okText;
    }

    public void setOkText(String okText) {
        this.okText = okText;
    }

    public String getCancleText() {
        return cancleText;
    }

    public void setCancleText(String cancleText) {
        this.cancleText = cancleText;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.okText);
        dest.writeString(this.cancleText);
        dest.writeString(this.messageText);
    }

    protected NormalDialogBean(Parcel in) {
        this.okText = in.readString();
        this.cancleText = in.readString();
        this.messageText = in.readString();
    }

    public static final Creator<NormalDialogBean> CREATOR = new Creator<NormalDialogBean>() {
        @Override
        public NormalDialogBean createFromParcel(Parcel source) {
            return new NormalDialogBean(source);
        }

        @Override
        public NormalDialogBean[] newArray(int size) {
            return new NormalDialogBean[size];
        }
    };
}
