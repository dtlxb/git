package cn.gogoal.im.bean;

/**
 * Created by huangxx on 2017/4/7.
 */

public class ContentTitleBean {

    private String color;
    private String word;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "ContentTitleBean{" +
                "color='" + color + '\'' +
                ", word='" + word + '\'' +
                '}';
    }
}

