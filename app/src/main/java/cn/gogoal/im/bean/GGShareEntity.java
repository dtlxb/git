package cn.gogoal.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * author wangjd on 2017/5/4 0004.
 * Staff_id 1375
 * phone 18930640263
 * description :${
 * <p>
 * <p>
 * 分享的数据实体
 * /**
 * {
 * "desc":"一分钟了解股价走势与业绩相关性",
 * "icon":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/2dcc4122.png",
 * "title":"平安银行  000001"
 * }
 */
public class GGShareEntity implements Parcelable {

    public static final int SHARE_TYPE_TEXT = 0;

    public static final String SHARE_TYPE_WEB = "1";

    public static final String SHARE_TYPE_LIVE = "2";

    public static final String SHARE_TYPE_IMAGE = "3";

    private String desc;

    private String icon;

    private String title;

    private String link;//分享的url

    private String shareType;

    private String live_id;

    private String source;

    private Parcelable arg;//走分享时其他扩展情况

    public Parcelable getArg() {
        return arg;
    }

    public void setArg(Parcelable arg) {
        this.arg = arg;
    }

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getLive_id() {
        return live_id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public GGShareEntity() {
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
        dest.writeString(this.link);
        dest.writeString(this.shareType);
        dest.writeString(this.live_id);
        dest.writeString(this.source);
        dest.writeParcelable(this.arg, flags);
    }

    protected GGShareEntity(Parcel in) {
        this.desc = in.readString();
        this.icon = in.readString();
        this.title = in.readString();
        this.link = in.readString();
        this.shareType = in.readString();
        this.live_id = in.readString();
        this.source = in.readString();
        this.arg = in.readParcelable(Objects.class.getClassLoader());
    }

    public static final Creator<GGShareEntity> CREATOR = new Creator<GGShareEntity>() {
        @Override
        public GGShareEntity createFromParcel(Parcel source) {
            return new GGShareEntity(source);
        }

        @Override
        public GGShareEntity[] newArray(int size) {
            return new GGShareEntity[size];
        }
    };
}