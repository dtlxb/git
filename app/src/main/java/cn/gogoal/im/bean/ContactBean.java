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
public class ContactBean<T> extends BaseIndexPinyinBean implements Serializable{

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

    //=============================================
    private String phone;
    private String simple_avatar;
    private String token;
    private int parent_account_id;
    private int login_type;
    private int is_parent_account;
    private int login_id;
    private int code;
    private int account_status;
    private String photo;
    private int organization_id;
    private String organization_name;
    private String organization_address;
    private String weibo;
    private int is_tc_org;

    private String duty;//职务

    private String org_name;//

    private String email;

    private String full_name;

    private String gender;

    private String mobile;

    private String account_name;

    private String department;

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

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setmPinyin(String mPinyin) {
        this.mPinyin = mPinyin;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSimple_avatar() {
        return simple_avatar;
    }

    public void setSimple_avatar(String simple_avatar) {
        this.simple_avatar = simple_avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getParent_account_id() {
        return parent_account_id;
    }

    public void setParent_account_id(int parent_account_id) {
        this.parent_account_id = parent_account_id;
    }

    public int getLogin_type() {
        return login_type;
    }

    public void setLogin_type(int login_type) {
        this.login_type = login_type;
    }

    public int getIs_parent_account() {
        return is_parent_account;
    }

    public void setIs_parent_account(int is_parent_account) {
        this.is_parent_account = is_parent_account;
    }

    public int getLogin_id() {
        return login_id;
    }

    public void setLogin_id(int login_id) {
        this.login_id = login_id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getAccount_status() {
        return account_status;
    }

    public void setAccount_status(int account_status) {
        this.account_status = account_status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(int organization_id) {
        this.organization_id = organization_id;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getOrganization_address() {
        return organization_address;
    }

    public void setOrganization_address(String organization_address) {
        this.organization_address = organization_address;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public int getIs_tc_org() {
        return is_tc_org;
    }

    public void setIs_tc_org(int is_tc_org) {
        this.is_tc_org = is_tc_org;
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
                "tag=" + tag +
                ", contactType=" + contactType +
                ", id=" + id +
                ", friend_id=" + friend_id +
                ", account_id=" + account_id +
                ", userId=" + userId +
                ", remark='" + remark + '\'' +
                ", conv_id='" + conv_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar=" + avatar +
                ", mPinyin='" + mPinyin + '\'' +
                ", phone='" + phone + '\'' +
                ", simple_avatar='" + simple_avatar + '\'' +
                ", token='" + token + '\'' +
                ", parent_account_id=" + parent_account_id +
                ", login_type=" + login_type +
                ", is_parent_account=" + is_parent_account +
                ", login_id=" + login_id +
                ", code=" + code +
                ", account_status=" + account_status +
                ", photo='" + photo + '\'' +
                ", organization_id=" + organization_id +
                ", organization_name='" + organization_name + '\'' +
                ", organization_address='" + organization_address + '\'' +
                ", weibo='" + weibo + '\'' +
                ", is_tc_org=" + is_tc_org +
                ", duty='" + duty + '\'' +
                ", org_name='" + org_name + '\'' +
                ", email='" + email + '\'' +
                ", full_name='" + full_name + '\'' +
                ", gender='" + gender + '\'' +
                ", mobile='" + mobile + '\'' +
                ", account_name='" + account_name + '\'' +
                ", department='" + department + '\'' +
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
