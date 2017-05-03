package cn.gogoal.im.bean.stock;

/**
 * author wangjd on 2017/5/2 0002.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ThreeText {
    private String name;
    private String price;
    private String value;

    public ThreeText(String name, String price, String value) {
        this.name = name;
        this.price = price;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
