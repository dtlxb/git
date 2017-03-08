package cn.gogoal.im.bean;

import cn.gogoal.im.ui.index.BaseIndexPinyinBean;

/**
 * author wangjd on 2017/2/28 0028.
 * Staff_id 1375
 * phone 18930640263
 */
public class ContactBean<T> extends BaseIndexPinyinBean {

    private ContactType contactType;//item类型

    private String mAccountId;//账号Id

    private String mAccount;//账号

    private String mName;//昵称

    private String mAlias;//备注

    private String mPinyin;//昵称/备注的全拼

    private T mAvatar;//头像地址

    public ContactType getContactType() {
        return contactType;
    }

    public ContactBean setContactType(ContactType contactType) {
        this.contactType = contactType;
        return this;
    }

    public String getmAccountId() {
        return mAccountId;
    }

    public ContactBean setmAccountId(String mAccountId) {
        this.mAccountId = mAccountId;
        return this;
    }

    public String getmAccount() {
        return mAccount;
    }

    public ContactBean setmAccount(String mAccount) {
        this.mAccount = mAccount;
        return this;
    }

    public String getmName() {
        return mName;
    }

    public ContactBean setmName(String mName) {
        this.mName = mName;
        return this;
    }

    public String getmAlias() {
        return mAlias;
    }

    public ContactBean setmAlias(String mAlias) {
        this.mAlias = mAlias;
        return this;
    }

    public String getmPinyin() {
        return mPinyin;
    }

    public ContactBean setmPinyin(String mPinyin) {
        this.mPinyin = mPinyin;
        return this;
    }

    public T getmAvatar() {
        return mAvatar;
    }

    public ContactBean setmAvatar(T mAvatar) {
        this.mAvatar = mAvatar;
        return this;
    }

    @Override
    public String getTarget() {
        return mName;
    }

    @Override
    public boolean isNeedToPinyin() {
        return getContactType()==ContactType.PersionItem;
    }


    @Override
    public boolean isShowSuspension() {
        return getContactType()==ContactType.PersionItem;
    }


    public enum ContactType{
        FunctionItem(0),PersionItem(0);
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
}
