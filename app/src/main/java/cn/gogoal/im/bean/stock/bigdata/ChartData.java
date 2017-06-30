package cn.gogoal.im.bean.stock.bigdata;

/**
 * author wangjd on 2017/6/29 0029.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ChartData implements Cloneable{
    /**
     * date : 2016-12-28 00:00:00
     * themeword : 有色金属
     * attention : 3.805615765176961E-4
     * index_price : 1000
     */

    private String date;
    private String themeword;
    private double attention;
    private int index_price;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getThemeword() {
        return themeword;
    }

    public void setThemeword(String themeword) {
        this.themeword = themeword;
    }

    public double getAttention() {
        return attention;
    }

    public void setAttention(double attention) {
        this.attention = attention;
    }

    public int getIndex_price() {
        return index_price;
    }

    public void setIndex_price(int index_price) {
        this.index_price = index_price;
    }

    @Override
    public ChartData clone() {
        try {
            return (ChartData) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
