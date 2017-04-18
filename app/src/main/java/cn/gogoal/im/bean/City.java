package cn.gogoal.im.bean;

import java.util.List;

/**
 * author wangjd on 2017/4/18 0018.
 * Staff_id 1375
 * phone 18930640263
 * description :三级联动数据
 */
public class City {

    private String reason;
    private int error_code;
    private List<ProvinceBean> result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<ProvinceBean> getResult() {
        return result;
    }

    public void setResult(List<ProvinceBean> result) {
        this.result = result;
    }

    public static class ProvinceBean {

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

        public static class CityBean {

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

            public static class DistrictBean {

                private String id;
                private String district;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getDistrict() {
                    return district;
                }

                public void setDistrict(String district) {
                    this.district = district;
                }
            }
        }
    }
}
