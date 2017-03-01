package com.gogoal.app.bean;

import java.io.Serializable;

/**
 * author wangjd on 2017/2/28 0028.
 * Staff_id 1375
 * phone 18930640263
 */
public class ContactBean implements Comparable<ContactBean>, Serializable {

    private String mAccountId;//账号Id

    private String mAccount;//账号

    private String mName;//昵称

    private String mAlias;//备注

    private String mPinyin;//昵称/备注的全拼

    private String mAvatar;//头像地址

    @Override
    public int compareTo(ContactBean o) {
        return this.mPinyin.compareTo(o.getmPinyin());
    }

    public String getmAccountId() {
        return mAccountId;
    }

    public void setmAccountId(String mAccountId) {
        this.mAccountId = mAccountId;
    }

    public String getmAccount() {
        return mAccount;
    }

    public void setmAccount(String mAccount) {
        this.mAccount = mAccount;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmAlias() {
        return mAlias;
    }

    public void setmAlias(String mAlias) {
        this.mAlias = mAlias;
    }

    public String getmPinyin() {
        return mPinyin;
    }

    public void setmPinyin(String mPinyin) {
        this.mPinyin = mPinyin;
    }

    public String getmAvatar() {
        return mAvatar;
    }

    public void setmAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
    }
}
