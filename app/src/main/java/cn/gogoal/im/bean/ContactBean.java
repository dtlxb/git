package cn.gogoal.im.bean;

import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;

import java.io.Serializable;

import cn.gogoal.im.ui.index.BaseIndexPinyinBean;

/**
 * author wangjd on 2017/2/28 0028.
 * Staff_id 1375
 * phone 18930640263
 */
public class ContactBean<T> extends BaseIndexPinyinBean implements Serializable {

    private int tag;//预留字段，特殊处理用

//    public int getTag() {
//        return tag;
//    }
//
//    public void setTag(int tag) {
//        this.tag = tag;
//    }

    private ContactType contactType;//item类型

    private int id;

    private int friend_id;

    private int account_id;

    private int userId;

    public int getUserId() {
        return getFriend_id()==0?getAccount_id():getFriend_id();
    }

    private String remark;//备注

    private String conv_id;

    private String nickname;//昵称

    private T avatar;//头像URL

    private String mPinyin;//昵称/备注的全拼

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getConv_id() {
        return conv_id;
    }

    public void setConv_id(String conv_id) {
        this.conv_id = conv_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public T getAvatar() {
        return avatar;
    }

    public void setAvatar(T avatar) {
        this.avatar = avatar;
    }

    public String getmPinyin() {
        return Pinyin.toPinyin(getTarget().trim(), "");
    }

    @Override
    public String getTarget() {
        return isEmpty(getRemark()) ?
                (isEmpty(getNickname()) ? "--" : getNickname()) :
                getRemark();
    }

    @Override
    public boolean isNeedToPinyin() {
        return getContactType() == ContactType.PERSION_ITEM;
    }


    @Override
    public boolean isShowSuspension() {
        return getContactType() == ContactType.PERSION_ITEM;
    }


    public enum ContactType {
        FUNCTION_ITEM(0), PERSION_ITEM(0);
        private int type;

        ContactType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    @Override
    public String toString() {
        return "ContactBean{" +
                "contactType=" + contactType +
                ", id=" + id +
                ", friend_id=" + friend_id +
                ", remark='" + remark + '\'' +
                ", conv_id='" + conv_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar=" + avatar +
                ", mPinyin='" + mPinyin + '\'' +
                '}';
    }

    private boolean isEmpty(String s) {
        return null == s || TextUtils.isEmpty(s) || s.trim().replace(" ", "").equals("");
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ContactBean) &&((ContactBean) obj).getFriend_id() == this.getFriend_id();
    }
}
