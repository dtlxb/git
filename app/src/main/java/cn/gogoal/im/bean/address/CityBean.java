package cn.gogoal.im.bean.address;

import java.util.List;

/**
 * author wangjd on 2017/5/25 0025.
 * Staff_id 1375
 * phone 18930640263
 * description :三级联动——城市
 */
public class CityBean{

    private String id;
    private String city;
    private List<DistrictBean> district;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<DistrictBean> getDistrict() {
        return district;
    }

    public void setDistrict(List<DistrictBean> district) {
        this.district = district;
    }

}
