package cn.gogoal.im.bean.address;

import java.util.List;

/**
 * author wangjd on 2017/5/25 0025.
 * Staff_id 1375
 * phone 18930640263
 * description :三级联动——省.
 */
public class ProvinceBean {

    private String id;
    private String province;
    private List<CityBean> city;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<CityBean> getCity() {
        return city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }
}