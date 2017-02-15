package com.github.lzyzsd.jsbridge;

/**
 * @author wangjd on 2016/12/7 0007.
 * @Staff_id 1375
 * @phone 18930640263
 */

public class BaseMag {
    private String code;
    private String message;

    public BaseMag(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
