package cn.gogoal.im.bean;

/**
 * author wangjd on 2017/3/31 0031.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class UserInfo {
    private String avatar;
    private String fullName;
    private String nickName;

    private String itemText1;
    private String itemText2;

    private ItemType itemType;
    private boolean haveMore;

    public UserInfo(ItemType itemType) {
        this.itemType = itemType;
    }

    public UserInfo(ItemType itemType, String avatar, String fullName, String nickName) {
        this.avatar = avatar;
        this.fullName = fullName;
        this.nickName = nickName;
        this.itemType = itemType;
    }

    public UserInfo(ItemType itemType, boolean haveMore,String itemText1, String itemText2) {
        this.itemText1 = itemText1;
        this.itemText2 = itemText2;
        this.itemType = itemType;
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

    public String getItemText1() {
        return itemText1;
    }

    public void setItemText1(String itemText1) {
        this.itemText1 = itemText1;
    }

    public String getItemText2() {
        return itemText2;
    }

    public void setItemText2(String itemText2) {
        this.itemText2 = itemText2;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public boolean isHaveMore() {
        return haveMore;
    }

    public void setHaveMore(boolean haveMore) {
        this.haveMore = haveMore;
    }

    public enum ItemType {
        HEAD(10086), TEXT(10010), SPACE(10001);
        private int type;

        ItemType(int type) {
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
