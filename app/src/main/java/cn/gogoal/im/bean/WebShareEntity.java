package cn.gogoal.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author wangjd on 2017/5/4 0004.
 * Staff_id 1375
 * phone 18930640263
 * description :${
 * <p>
 * /**
 * {
 * "desc":"一分钟了解股价走势与业绩相关性",
 * "icon":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/2dcc4122.png",
 * "title":"平安银行  000001"
 * }
 */
public class WebShareEntity implements Parcelable{

    private String desc;

    private String icon;

    private String title;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.desc);
        dest.writeString(this.icon);
        dest.writeString(this.title);
    }

    public WebShareEntity() {
    }

    protected WebShareEntity(Parcel in) {
        this.desc = in.readString();
        this.icon = in.readString();
        this.title = in.readString();
    }

    public static final Creator<WebShareEntity> CREATOR = new Creator<WebShareEntity>() {
        @Override
        public WebShareEntity createFromParcel(Parcel source) {
            return new WebShareEntity(source);
        }

        @Override
        public WebShareEntity[] newArray(int size) {
            return new WebShareEntity[size];
        }
    };
}
