package cn.gogoal.im.bean.group;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author wangjd on 2017/6/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :群成员信息
 */
public class GroupMemberInfo implements Parcelable {
    /**
     * account_id : 73040
     * account_name : E029542
     * avatar : http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_73040_1494472944298.jpg
     * city : 上海市市辖区
     * duty : 未设置
     * nickname : 燃烧的岁月2
     */

    private String account_id;
    private String account_name;
    private String avatar;
    private String city;
    private String duty;
    private String nickname;

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.account_id);
        dest.writeString(this.account_name);
        dest.writeString(this.avatar);
        dest.writeString(this.city);
        dest.writeString(this.duty);
        dest.writeString(this.nickname);
    }

    public GroupMemberInfo() {
    }

    protected GroupMemberInfo(Parcel in) {
        this.account_id = in.readString();
        this.account_name = in.readString();
        this.avatar = in.readString();
        this.city = in.readString();
        this.duty = in.readString();
        this.nickname = in.readString();
    }

    public static final Creator<GroupMemberInfo> CREATOR = new Creator<GroupMemberInfo>() {
        @Override
        public GroupMemberInfo createFromParcel(Parcel source) {
            return new GroupMemberInfo(source);
        }

        @Override
        public GroupMemberInfo[] newArray(int size) {
            return new GroupMemberInfo[size];
        }
    };
}
