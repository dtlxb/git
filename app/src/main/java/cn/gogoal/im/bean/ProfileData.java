package cn.gogoal.im.bean;

/**
 * Created by dave.
 * Date: 2017/6/8.
 * Desc: description
 */
public class ProfileData {

    private String name;
    private String content;

    public ProfileData(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
