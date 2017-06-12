package cn.gogoal.im.bean.group;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * author wangjd on 2017/5/22 0022.
 * Staff_id 1375
 * phone 18930640263
 * description :群详细信息
 */
public class GroupData implements Parcelable{
    private GroupAttr attr;
    private String c;
    private String name;
    private String conv_id;
    private int m_size;
    private boolean is_in;
    private boolean is_creator;
    private String name_in_group;
    private List<String> m;
    private ArrayList<GroupMemberInfo> m_info;

    private Bitmap avatar;

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public GroupAttr getAttr() {
        return attr;
    }

    public void setAttr(GroupAttr attr) {
        this.attr = attr;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConv_id() {
        return conv_id;
    }

    public void setConv_id(String conv_id) {
        this.conv_id = conv_id;
    }

    public int getM_size() {
        return m_size;
    }

    public void setM_size(int m_size) {
        this.m_size = m_size;
    }

    public boolean is_in() {
        return is_in;
    }

    public void setIs_in(boolean is_in) {
        this.is_in = is_in;
    }

    public boolean is_creator() {
        return is_creator;
    }

    public void setIs_creator(boolean is_creator) {
        this.is_creator = is_creator;
    }

    public String getName_in_group() {
        return name_in_group;
    }

    public void setName_in_group(String name_in_group) {
        this.name_in_group = name_in_group;
    }

    public List<String> getM() {
        return m;
    }

    public void setM(List<String> m) {
        this.m = m;
    }

    public ArrayList<GroupMemberInfo> getM_info() {
        return m_info;
    }

    public void setM_info(ArrayList<GroupMemberInfo> m_info) {
        this.m_info = m_info;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.attr, flags);
        dest.writeString(this.c);
        dest.writeString(this.name);
        dest.writeString(this.conv_id);
        dest.writeInt(this.m_size);
        dest.writeByte(this.is_in ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_creator ? (byte) 1 : (byte) 0);
        dest.writeString(this.name_in_group);
        dest.writeStringList(this.m);
        dest.writeTypedList(this.m_info);
        dest.writeParcelable(this.avatar, flags);
    }

    public GroupData() {
    }

    protected GroupData(Parcel in) {
        this.attr = in.readParcelable(GroupAttr.class.getClassLoader());
        this.c = in.readString();
        this.name = in.readString();
        this.conv_id = in.readString();
        this.m_size = in.readInt();
        this.is_in = in.readByte() != 0;
        this.is_creator = in.readByte() != 0;
        this.name_in_group = in.readString();
        this.m = in.createStringArrayList();
        this.m_info = in.createTypedArrayList(GroupMemberInfo.CREATOR);
        this.avatar = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<GroupData> CREATOR = new Creator<GroupData>() {
        @Override
        public GroupData createFromParcel(Parcel source) {
            return new GroupData(source);
        }

        @Override
        public GroupData[] newArray(int size) {
            return new GroupData[size];
        }
    };
}
