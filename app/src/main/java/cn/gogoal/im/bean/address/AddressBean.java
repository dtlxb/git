package cn.gogoal.im.bean.address;

import java.util.List;

/**
 * author wangjd on 2017/4/18 0018.
 * Staff_id 1375
 * phone 18930640263
 * description :三级联动数据
 */
public class AddressBean {

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
}
