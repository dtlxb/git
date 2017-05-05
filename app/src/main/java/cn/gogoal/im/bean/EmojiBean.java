package cn.gogoal.im.bean;

/**
 * Created by huangxx on 2017/5/3.
 */

public class EmojiBean {
    private String emojiName;
    private int emojiUrl;

    public String getEmojiName() {
        return emojiName;
    }

    public void setEmojiName(String emojiName) {
        this.emojiName = emojiName;
    }

    public int getEmojiUrl() {
        return emojiUrl;
    }

    public void setEmojiUrl(int emojiUrl) {
        this.emojiUrl = emojiUrl;
    }

    public EmojiBean(String emojiName, int emojiUrl) {
        this.emojiName = emojiName;
        this.emojiUrl = emojiUrl;
    }

    @Override
    public String toString() {
        return "emojiBean{" +
                "emojiName='" + emojiName + '\'' +
                ", emojiUrl=" + emojiUrl +
                '}';
    }
}
