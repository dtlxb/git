package cn.gogoal.im.bean;

/**
 * Created by huangxx on 2017/4/7.
 */

public class ContentBean {
    /**
     * color : #999999
     * word : 770b597d8fea65af5c3c53735c065f0056ed768498986750ff0c8fd94e006ce270924f5c8fea65af5c3c76844e3b6d415e94662f4ea4901a5ba28fd0ff085982600676300160061130016006627b49ff09300157288fea65af5c3c96448fd16709767e8d277b496d888d3952065e97ff08598260082730016008387b49ff09ff0c62168fea65af5c3c4ea754c14e1353567c7b7684-----770b597d8fea65af5c3c53735c065f0056ed768498986750ff0c8fd94e006ce270924f5c8fea65af5c3c76844e3b6d415e94662f4ea4901a5ba28fd0ff085982600676300160061130016006627b49ff09300157288fea65af5c3c96448fd16709767e8d277b496d888d3952065e97ff08598260082730016008387b49ff09ff0c62168fea65af5c3c4ea754c14e1353567c7b7684... ,
     */

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
        return "ContentBean{" +
                "color='" + color + '\'' +
                ", word='" + word + '\'' +
                '}';
    }
}

