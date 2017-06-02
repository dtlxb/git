package cn.gogoal.im.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by huangxx on 2017/6/2.
 */

public class UserBean<T> extends DataSupport {

    private ContactBean.ContactType contactType;//item类型

    private int id;

    private int friend_id;

    private int account_id;

    private String remark;//备注

    private String conv_id;

    private String nickname;//昵称

    private T avatar;//头像URL

    private String mPinyin;//昵称/备注的全拼

    public ContactBean.ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactBean.ContactType contactType) {
        this.contactType = contactType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
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
        return mPinyin;
    }

    public void setmPinyin(String mPinyin) {
        this.mPinyin = mPinyin;
    }
}
