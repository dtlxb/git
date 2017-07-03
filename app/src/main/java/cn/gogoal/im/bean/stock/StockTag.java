package cn.gogoal.im.bean.stock;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author wangjd on 2017/7/3 0003.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class StockTag implements Parcelable{
    /**
     * _id : -2
     * name : 好公司
     * remark : 历史业绩符合预期或超预期
     */

    private int type;//1：好公司 2：希望之星  0：历史好公司 -1 未选入
    private int _id;
    private String name;
    private String remark;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._id);
        dest.writeString(this.name);
        dest.writeString(this.remark);
    }

    public StockTag() {
    }

    protected StockTag(Parcel in) {
        this._id = in.readInt();
        this.name = in.readString();
        this.remark = in.readString();
    }

    public static final Creator<StockTag> CREATOR = new Creator<StockTag>() {
        @Override
        public StockTag createFromParcel(Parcel source) {
            return new StockTag(source);
        }

        @Override
        public StockTag[] newArray(int size) {
            return new StockTag[size];
        }
    };
}
