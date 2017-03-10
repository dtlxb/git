package cn.gogoal.im.bean;

/**
 * author wangjd on 2017/3/10 0010.
 * Staff_id 1375
 * phone 18930640263
 */
public class StockBannerData {
    private int position;

    private int id;

    private String name;

    private StockBannerAd data;

    private int clickable;

    private String image;

    private String date;

    private String target_type;

    private String target_url;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StockBannerAd getData() {
        return data;
    }

    public void setData(StockBannerAd data) {
        this.data = data;
    }

    public int getClickable() {
        return clickable;
    }

    public void setClickable(int clickable) {
        this.clickable = clickable;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTarget_type() {
        return target_type;
    }

    public void setTarget_type(String target_type) {
        this.target_type = target_type;
    }

    public String getTarget_url() {
        return target_url;
    }

    public void setTarget_url(String target_url) {
        this.target_url = target_url;
    }
}