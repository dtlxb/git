package cn.gogoal.im.bean;

import cn.gogoal.im.adapter.baseAdapter.entity.MultiItemEntity;

/**
 * author wangjd on 2017/3/31 0031.
 * Staff_id 1375
 * phone 18930640263
 * description :用户详情页实体.
 */
public class UserInfo implements MultiItemEntity{
    public static final int HEAD = 1;
    public static final int SPACE = 2;
    public static final int TEXT_ITEM_2 = 3;

    private int itemType;

    private String avatar;//头像
    private String fullName;//真实姓名
    private String nickName;//昵称

    private String itemKey;//文本1 key
    private String itemValue;//文本2 value

    private boolean haveMore;//是否显示更多箭头

    public UserInfo(int itemType) {
        this.itemType = itemType;
    }

    public UserInfo(int itemType,String avatar) {
        this.itemType = itemType;
        this.avatar=avatar;
    }

    public UserInfo(int itemType,String avatar, String fullName, String nickName) {
        this.itemType=itemType;
        this.avatar = avatar;
        this.fullName = fullName;
        this.nickName = nickName;
    }

    public UserInfo(int itemType,boolean haveMore,String itemKey, String itemValue) {
        this.itemType=itemType;
        this.itemKey = itemKey;
        this.itemValue = itemValue;
        this.haveMore = haveMore;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public boolean isHaveMore() {
        return haveMore;
    }

    public void setHaveMore(boolean haveMore) {
        this.haveMore = haveMore;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
