package cn.gogoal.im.bean;

import android.graphics.Bitmap;

/**
 * author wangjd on 2017/5/16 0016.
 * Staff_id 1375
 * phone 18930640263
 * description :手机联系人
 */
public class PhoneContact {
    private String name;            //联系人备注名
    private String mobile;          //联系人手机号
    private Bitmap phoneAvatar;     //联系人头像
    private long contactid;         //联系人Id

    public PhoneContact(String name, String mobile, Bitmap phoneAvatar, long contactid) {
        this.name = name;
        this.mobile = mobile;
        this.phoneAvatar = phoneAvatar;
        this.contactid = contactid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Bitmap getPhoneAvatar() {
        return phoneAvatar;
    }

    public void setPhoneAvatar(Bitmap phoneAvatar) {
        this.phoneAvatar = phoneAvatar;
    }

    public long getContactid() {
        return contactid;
    }

    public void setContactid(long contactid) {
        this.contactid = contactid;
    }
}
