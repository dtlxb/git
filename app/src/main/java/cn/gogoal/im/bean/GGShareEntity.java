package cn.gogoal.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.gogoal.im.common.ShareType;

/**
 * author wangjd on 2017/5/4 0004.
 * Staff_id 1375
 * phone 18930640263
 * description :${
 * <p>
 *
 *     分享的数据实体
 * /**
 * {
 * "desc":"一分钟了解股价走势与业绩相关性",
 * "icon":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/2dcc4122.png",
 * "title":"平安银行  000001"
 * }
 */
public class GGShareEntity implements Parcelable {

    private String desc;

    private String icon;

    private String title;

    private ShareType shareType;

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

    public ShareType getShareType() {
        return shareType;
    }

    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
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
        dest.writeInt(this.shareType == null ? -1 : this.shareType.ordinal());
    }

    public GGShareEntity() {
    }

    protected GGShareEntity(Parcel in) {
        this.desc = in.readString();
        this.icon = in.readString();
        this.title = in.readString();
        int tmpShareType = in.readInt();
        this.shareType = tmpShareType == -1 ? null : ShareType.values()[tmpShareType];
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
