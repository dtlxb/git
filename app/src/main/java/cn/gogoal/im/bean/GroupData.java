package cn.gogoal.im.bean;

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
public class GroupData {
    private AttrBean attr;
    private String c;
    private String name;
    private String conv_id;
    private int m_size;
    private boolean is_in;
    private boolean is_creator;
    private String name_in_group;
    private List<String> m;
    private ArrayList<MInfoBean> m_info;

    private Bitmap avatar;

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public AttrBean getAttr() {
        return attr;
    }

    public void setAttr(AttrBean attr) {
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

    public ArrayList<MInfoBean> getM_info() {
        return m_info;
    }

    public void setM_info(ArrayList<MInfoBean> m_info) {
        this.m_info = m_info;
    }

    public static class AttrBean {
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
    }

    public static class MInfoBean implements Parcelable {
        /**
         * account_id : 73040
         * account_name : E029542
         * avatar : http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_73040_1494472944298.jpg
         * city : 上海市市辖区
         * duty : 未设置
         * nickname : 燃烧的岁月2
         */

        private int account_id;
        private String account_name;
        private String avatar;
        private String city;
        private String duty;
        private String nickname;

        public int getAccount_id() {
            return account_id;
        }

        public void setAccount_id(int account_id) {
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
            dest.writeInt(this.account_id);
            dest.writeString(this.account_name);
            dest.writeString(this.avatar);
            dest.writeString(this.city);
            dest.writeString(this.duty);
            dest.writeString(this.nickname);
        }

        public MInfoBean() {
        }

        protected MInfoBean(Parcel in) {
            this.account_id = in.readInt();
            this.account_name = in.readString();
            this.avatar = in.readString();
            this.city = in.readString();
            this.duty = in.readString();
            this.nickname = in.readString();
        }

        public static final Creator<MInfoBean> CREATOR = new Creator<MInfoBean>() {
            @Override
            public MInfoBean createFromParcel(Parcel source) {
                return new MInfoBean(source);
            }

            @Override
            public MInfoBean[] newArray(int size) {
                return new MInfoBean[size];
            }
        };
    }

}
