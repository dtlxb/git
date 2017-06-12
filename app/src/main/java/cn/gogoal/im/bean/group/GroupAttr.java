package cn.gogoal.im.bean.group;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author wangjd on 2017/6/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :群属性
 */
public class GroupAttr implements Parcelable{
    /**
     * avatar : http://file.go-goal.cn/ggimages/community/446265b4.png?iopcmd=thumbnail&type=8&width=100&height=100
     * chat_type : 1002
     * creator_duty : 待定
     * creator_encourage : 待定
     * creator_img : http://file.go-goal.cn/ggimages/community/0326bdf5.png?iopcmd=thumbnail&type=8&width=80&height=80
     * group_gist : 一个定期认知、专业交流、促进社交的平台
     * group_rule : 待定
     * intro : 2006 年由上海朝阳永续公司发起成立的中国研究员专业网目前已经发展成为国内规模最大的中国证券行业证券研究精英的专业网站。 随着朝阳永续业务的不断扩大，最佳伯乐奖的参赛选手、精英赛选手及高净值投资者也逐步加入到中国研究员专业网中。为了更好的分享不同研究员的智慧，提供更加直接的交流平台，研网俱乐部在每月第三周周六举办的研究员倶乐部休闲与交流活动，参加者以中国研究员专业网里的研究精英为主。
     * notice : 待定
     */

    private String avatar;
    private int chat_type;
    private String creator_duty;
    private String creator_encourage;
    private String creator_img;
    private String group_gist;
    private String group_rule;
    private String intro;
    private String notice;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getChat_type() {
        return chat_type;
    }

    public void setChat_type(int chat_type) {
        this.chat_type = chat_type;
    }

    public String getCreator_duty() {
        return creator_duty;
    }

    public void setCreator_duty(String creator_duty) {
        this.creator_duty = creator_duty;
    }

    public String getCreator_encourage() {
        return creator_encourage;
    }

    public void setCreator_encourage(String creator_encourage) {
        this.creator_encourage = creator_encourage;
    }

    public String getCreator_img() {
        return creator_img;
    }

    public void setCreator_img(String creator_img) {
        this.creator_img = creator_img;
    }

    public String getGroup_gist() {
        return group_gist;
    }

    public void setGroup_gist(String group_gist) {
        this.group_gist = group_gist;
    }

    public String getGroup_rule() {
        return group_rule;
    }

    public void setGroup_rule(String group_rule) {
        this.group_rule = group_rule;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar);
        dest.writeInt(this.chat_type);
        dest.writeString(this.creator_duty);
        dest.writeString(this.creator_encourage);
        dest.writeString(this.creator_img);
        dest.writeString(this.group_gist);
        dest.writeString(this.group_rule);
        dest.writeString(this.intro);
        dest.writeString(this.notice);
    }

    public GroupAttr() {
    }

    protected GroupAttr(Parcel in) {
        this.avatar = in.readString();
        this.chat_type = in.readInt();
        this.creator_duty = in.readString();
        this.creator_encourage = in.readString();
        this.creator_img = in.readString();
        this.group_gist = in.readString();
        this.group_rule = in.readString();
        this.intro = in.readString();
        this.notice = in.readString();
    }

    public static final Creator<GroupAttr> CREATOR = new Creator<GroupAttr>() {
        @Override
        public GroupAttr createFromParcel(Parcel source) {
            return new GroupAttr(source);
        }

        @Override
        public GroupAttr[] newArray(int size) {
            return new GroupAttr[size];
        }
    };
}
