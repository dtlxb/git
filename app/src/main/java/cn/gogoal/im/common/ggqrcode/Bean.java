package cn.gogoal.im.common.ggqrcode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author wangjd on 2017/6/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :二维码中包含字段
 */
public class Bean implements Parcelable {

    public static final int QR_CODE_TYPE_PERSIONAL = 0x0;

    public static final int QR_CODE_TYPE_GROUP = 0x1;

    private int qrType;

    private String account_id;

    private String conv_id;

    public int getQrType() {
        return qrType;
    }

    public void setQrType(int qrType) {
        this.qrType = qrType;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getConv_id() {
        return conv_id;
    }

    public void setConv_id(String conv_id) {
        this.conv_id = conv_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.qrType);
        dest.writeString(this.account_id);
        dest.writeString(this.conv_id);
    }

    public Bean() {
    }

    protected Bean(Parcel in) {
        this.qrType = in.readInt();
        this.account_id = in.readString();
        this.conv_id = in.readString();
    }

    public static final Creator<Bean> CREATOR = new Creator<Bean>() {
        @Override
        public Bean createFromParcel(Parcel source) {
            return new Bean(source);
        }

        @Override
        public Bean[] newArray(int size) {
            return new Bean[size];
        }
    };
}
